/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.epay.epaywso.rendicontazione.FlussoDettaglioType;
import it.csi.epay.epaywso.rendicontazione.FlussoRiconciliazioneType;
import it.csi.epay.epaywso.rendicontazione.FlussoSintesiType;
import it.csi.epay.epaywso.rendicontazione.TestataFlussoRiconciliazioneType;
import it.csi.epay.epaywso.riconciliazione_versamenti_psp.types.TestataTrasmissioneFlussiType;
import it.csi.epay.epaywso.types.PersonaFisicaType;
import it.csi.epay.epaywso.types.PersonaGiuridicaType;
import it.csi.epay.epaywso.types.SoggettoType;
import it.csi.siac.siacbilser.business.service.pagopa.util.PagoPAUtils;
import it.csi.siac.siacbilser.integration.dao.PagoPADao;
import it.csi.siac.siacbilser.integration.dao.PagopaTRiconciliazioneDetRepository;
import it.csi.siac.siacbilser.integration.dao.PagopaTRiconciliazioneRepository;
import it.csi.siac.siacbilser.integration.dao.SiacDFilePagopaStatoRepository;
import it.csi.siac.siacbilser.integration.dao.SiacTEnteProprietarioRepository;
import it.csi.siac.siacbilser.integration.dao.SiacTFilePagopaRepository;
import it.csi.siac.siacbilser.integration.entity.PagopaTRiconciliazione;
import it.csi.siac.siacbilser.integration.entity.PagopaTRiconciliazioneDet;
import it.csi.siac.siacbilser.integration.entity.SiacTFilePagopa;
import it.csi.siac.siacbilser.integration.entity.SiacTProvCassa;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDFilePagopaStatoEnum;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class PagoPADad extends ExtendedBaseDadImpl {
	
	@Autowired
	private PagoPADao pagoPADao;

	@Autowired
	private SiacTFilePagopaRepository siacTFilePagopaRepository;

	@Autowired
	private SiacTEnteProprietarioRepository siacTEnteProprietarioRepository;
	
	@Autowired
	private SiacDFilePagopaStatoRepository siacDFilePagopaStatoRepository;
	
	@Autowired
	private PagopaTRiconciliazioneRepository pagopaTRiconciliazioneRepository;
	
	@Autowired
	private PagopaTRiconciliazioneDetRepository pagopaTRiconciliazioneDetRepository;
	
	public SiacTFilePagopa inserisciFlusso(TestataTrasmissioneFlussiType testata, byte[] flussoSintetico) {
		
		SiacTFilePagopa siacTFilePagopa = new SiacTFilePagopa();
		Date now = new Date();
		
		siacTFilePagopa.setFilePagopaCode(testata.getIdMessaggio());
		siacTFilePagopa.setFilePagopaIdFlusso(testata.getIdentificativoFlusso());
		siacTFilePagopa.setFilePagopaIdPsp(testata.getIdPSP());
		siacTFilePagopa.setFilePagopa(flussoSintetico);
		siacTFilePagopa.setFilePagopaSize(Long.valueOf(flussoSintetico.length));
		
		siacTFilePagopa.setFilePagopaAnno(Calendar.getInstance().get(Calendar.YEAR));

		siacTFilePagopa.setSiacDFilePagopaStato(
				siacDFilePagopaStatoRepository.getStatoByCodice(siacTEnteProprietario, SiacDFilePagopaStatoEnum.TRASMESSO.getCodice()));
		
		siacTFilePagopa.setSiacTEnteProprietario(siacTEnteProprietario);
		siacTFilePagopa.setDataInizioValidita(now);
		siacTFilePagopa.setDataCreazione(now);
		siacTFilePagopa.setDataModifica(now);
		siacTFilePagopa.setLoginOperazione(loginOperazione);		

		siacTFilePagopaRepository.saveAndFlush(siacTFilePagopa);
		
		return siacTFilePagopa;
	}

	public void initSiacTEnteProprietario(String codiceFiscaleEnte) {
		siacTEnteProprietario = siacTEnteProprietarioRepository.ricercaEnte(codiceFiscaleEnte);
		
		if (siacTEnteProprietario == null) {
			throw new IllegalArgumentException(ErroreCore.ENTITA_NON_TROVATA.getErrore("ente", String.format("codice fiscale %s", codiceFiscaleEnte)).getTesto());
		}
	}

	public List<SiacTFilePagopa> leggiElencoFlussiAcquisiti() {
		return siacTFilePagopaRepository.findByStatoCode(siacTEnteProprietario, SiacDFilePagopaStatoEnum.TRASMESSO.getCodice());
	}

	public void salvaFlusso(FlussoRiconciliazioneType flussoRiconciliazione, SiacTFilePagopa siacTFilePagopa) {
		
		TestataFlussoRiconciliazioneType testataFlussoRiconciliazione = flussoRiconciliazione.getTestataFlusso();
		Date now = new Date();
		
		for (FlussoSintesiType singolaRigaSintesi : flussoRiconciliazione.getRigheSintesi().getSingolaRigaSintesi()) {
		
			PagopaTRiconciliazione pagopaTRiconciliazione = savePagopaTRiconciliazione(buildTestataPagopaTRiconciliazione(testataFlussoRiconciliazione, siacTFilePagopa), singolaRigaSintesi, now);
			
			for (FlussoDettaglioType singolaRigaDettaglio : singolaRigaSintesi.getRigheDettaglio().getSingolaRigaDettaglio()) {
				savePagopaTRiconciliazioneDet(pagopaTRiconciliazione, singolaRigaDettaglio, now);
			}
		}
	}

	private PagopaTRiconciliazioneDet savePagopaTRiconciliazioneDet(PagopaTRiconciliazione pagopaTRiconciliazione, FlussoDettaglioType singolaRigaDettaglio, Date now) {

		PagopaTRiconciliazioneDet pagopaTRiconciliazioneDet = new PagopaTRiconciliazioneDet();
		
		pagopaTRiconciliazioneDet.setDataInizioValidita(now);
		pagopaTRiconciliazioneDet.setDataCreazione(now);
		pagopaTRiconciliazioneDet.setDataModifica(now);
		pagopaTRiconciliazioneDet.setSiacTEnteProprietario(pagopaTRiconciliazione.getSiacTEnteProprietario());
		pagopaTRiconciliazioneDet.setLoginOperazione(pagopaTRiconciliazione.getLoginOperazione());
		
		pagopaTRiconciliazioneDet.setPagopaTRiconciliazione(pagopaTRiconciliazione);
		
		SoggettoType anagraficaPagatore = singolaRigaDettaglio.getAnagraficaPagatore();
		
		if (anagraficaPagatore != null) {
			
			PersonaFisicaType personaFisica = anagraficaPagatore.getPersonaFisica();

			if (personaFisica != null) {
				pagopaTRiconciliazioneDet.setPagopaDetAnagCognome(personaFisica.getCognome());	
				pagopaTRiconciliazioneDet.setPagopaDetAnagNome(personaFisica.getNome());	
			}
			
			PersonaGiuridicaType personaGiuridica = anagraficaPagatore.getPersonaGiuridica();
			pagopaTRiconciliazioneDet.setPagopaDetAnagRagioneSociale(personaGiuridica != null ? personaGiuridica.getRagioneSociale() : null);	
			
			pagopaTRiconciliazioneDet.setPagopaDetAnagCodiceFiscale(anagraficaPagatore.getIdentificativoUnivocoFiscale());
			pagopaTRiconciliazioneDet.setPagopaDetAnagIndirizzo(anagraficaPagatore.getIndirizzo());
			pagopaTRiconciliazioneDet.setPagopaDetAnagCivico(anagraficaPagatore.getCivico());
			pagopaTRiconciliazioneDet.setPagopaDetAnagCap(anagraficaPagatore.getCAP());
			pagopaTRiconciliazioneDet.setPagopaDetAnagLocalita(anagraficaPagatore.getLocalita());
			pagopaTRiconciliazioneDet.setPagopaDetAnagProvincia(anagraficaPagatore.getProvincia());
			pagopaTRiconciliazioneDet.setPagopaDetAnagNazione(anagraficaPagatore.getNazione());
			pagopaTRiconciliazioneDet.setPagopaDetAnagEmail(anagraficaPagatore.getEMail());
		}
		
		pagopaTRiconciliazioneDet.setPagopaDetCausaleVersamentoDesc(singolaRigaDettaglio.getDescrizioneCausaleVersamento());
		pagopaTRiconciliazioneDet.setPagopaDetCausale(singolaRigaDettaglio.getCausale());
		pagopaTRiconciliazioneDet.setPagopaDetDataPagamento(PagoPAUtils.xmlGregorianCalendarToDate(singolaRigaDettaglio.getDataPagamento()));
		pagopaTRiconciliazioneDet.setPagopaDetEsitoPagamento(singolaRigaDettaglio.getEsitoPagamento());
		pagopaTRiconciliazioneDet.setPagopaDetImportoVersamento(singolaRigaDettaglio.getImportoSingoloVersamento());
		pagopaTRiconciliazioneDet.setPagopaDetIndiceVersamento(singolaRigaDettaglio.getIndiceSingoloVersamento());
		pagopaTRiconciliazioneDet.setPagopaDetTransactionId(singolaRigaDettaglio.getTransactionid());
		pagopaTRiconciliazioneDet.setPagopaDetVersamentoId(singolaRigaDettaglio.getIdentificativoUnivocoVersamento());
		pagopaTRiconciliazioneDet.setPagopaDetRiscossioneId(singolaRigaDettaglio.getIdentificativoUnivocoRiscossione());
		
		return pagopaTRiconciliazioneDetRepository.saveAndFlush(pagopaTRiconciliazioneDet);
	}

	private PagopaTRiconciliazione savePagopaTRiconciliazione(PagopaTRiconciliazione pagopaTRiconciliazione, FlussoSintesiType singolaRigaSintesi, Date now) {
		
		pagopaTRiconciliazione.setDataInizioValidita(now);
		pagopaTRiconciliazione.setDataCreazione(now);
		pagopaTRiconciliazione.setDataModifica(now);

		pagopaTRiconciliazione.setPagopaRicFileNumFlussi(1);
		
		pagopaTRiconciliazione.setPagopaRicFlussoVoceCode(singolaRigaSintesi.getCodiceVersamento());
		pagopaTRiconciliazione.setPagopaRicFlussoVoceDesc(singolaRigaSintesi.getDescrizioneCodiceVersamento());
		pagopaTRiconciliazione.setPagopaRicFlussoSottovoceCode(singolaRigaSintesi.getDatiSpecificiDiRiscossione());
		pagopaTRiconciliazione.setPagopaRicFlussoSottovoceDesc(singolaRigaSintesi.getDescrizioneDatiSpecifici());
		pagopaTRiconciliazione.setPagopaRicFlussoTematica(singolaRigaSintesi.getTematica());
		pagopaTRiconciliazione.setPagopaRicFlussoSottovoceImporto(singolaRigaSintesi.getImportoQuotaAggregazione());
		pagopaTRiconciliazione.setPagopaRicFlussoNumCapitolo(PagoPAUtils.parseInt(singolaRigaSintesi.getCapitolo())); 
		pagopaTRiconciliazione.setPagopaRicFlussoNumArticolo(PagoPAUtils.parseInt(singolaRigaSintesi.getArticolo())); 
		pagopaTRiconciliazione.setPagopaRicFlussoPdcVFin(singolaRigaSintesi.getPdC());
		pagopaTRiconciliazione.setPagopaRicFlussoAnnoAccertamento(singolaRigaSintesi.getAccertamentoAnno());
		pagopaTRiconciliazione.setPagopaRicFlussoNumAccertamento(singolaRigaSintesi.getAccertamentoNro());
		
		return pagopaTRiconciliazioneRepository.saveAndFlush(pagopaTRiconciliazione);
	}

	private PagopaTRiconciliazione buildTestataPagopaTRiconciliazione(TestataFlussoRiconciliazioneType testataFlussoRiconciliazione, SiacTFilePagopa siacTFilePagopa) {
		
		PagopaTRiconciliazione pagopaTRiconciliazione = new PagopaTRiconciliazione();

		pagopaTRiconciliazione.setSiacTFilePagopa(siacTFilePagopa);
		pagopaTRiconciliazione.setPagopaRicFileId(siacTFilePagopa.getFilePagopaCode());

		pagopaTRiconciliazione.setSiacTEnteProprietario(siacTEnteProprietario);
		pagopaTRiconciliazione.setLoginOperazione(loginOperazione);

		pagopaTRiconciliazione.setPagopaRicFileOra(PagoPAUtils.xmlGregorianCalendarToDate(testataFlussoRiconciliazione.getDataOraMessaggio()));
		pagopaTRiconciliazione.setPagopaRicFlussoData(PagoPAUtils.xmlGregorianCalendarToDate(testataFlussoRiconciliazione.getDataOraMessaggio()));
		pagopaTRiconciliazione.setPagopaRicFileEnte(testataFlussoRiconciliazione.getDenominazioneEnte());
		pagopaTRiconciliazione.setPagopaRicFlussoNomeMittente(testataFlussoRiconciliazione.getDenominazionePSP());
		pagopaTRiconciliazione.setPagopaRicFlussoId(testataFlussoRiconciliazione.getIdentificativoFlusso());
		pagopaTRiconciliazione
				.setPagopaRicFlussoTotPagam(testataFlussoRiconciliazione.getImportoTotalePagamentiFlusso());
		pagopaTRiconciliazione.setPagopaRicFlussoAnnoEsercizio(testataFlussoRiconciliazione.getProvvisorioAnno());
		pagopaTRiconciliazione.setPagopaRicFlussoAnnoProvvisorio(testataFlussoRiconciliazione.getProvvisorioAnno());
		siacTFilePagopa.setFilePagopaAnno(testataFlussoRiconciliazione.getProvvisorioAnno());
		pagopaTRiconciliazione.setPagopaRicFlussoNumProvvisorio(testataFlussoRiconciliazione.getProvvisorioNumero());
		
		return pagopaTRiconciliazione;
	}

	public void aggiornaStato(Integer siacTFilePagopatUid, SiacDFilePagopaStatoEnum siacDFilePagopaStato) {
		siacTFilePagopaRepository.aggiornaStato(siacTFilePagopatUid, siacDFilePagopaStato.getCodice());

		log.info("aggiornaStato", "flusso in stato " + siacDFilePagopaStato.name());
	}

	public SiacTFilePagopa findSiacTFilePagopaByIdMessaggio(String idMessaggio) {
		return StringUtils.isBlank(idMessaggio) ? null : 
			siacTFilePagopaRepository.findByCode(siacTEnteProprietario, idMessaggio);
	}

	public SiacTFilePagopa findSiacTFilePagopaByIdentificativoFlusso(String identificativoFlusso) {
		if (StringUtils.isBlank(identificativoFlusso)) {
			return null;
		}
		
		return siacTFilePagopaRepository.findByFlussoId(siacTEnteProprietario, identificativoFlusso);
	}

	public List<SiacTProvCassa> ricercaProvvisori(Integer annoEsercizio, List<String> causali, BigInteger numeroDa, BigInteger numeroA, 
			Date dataEmissioneDa, Date dataEmissioneA, Boolean isStatoValido) {
		return pagoPADao.findProvvisoriCassa(
						siacTEnteProprietario.getUid(), 
						annoEsercizio, 
						causali, 
						numeroDa == null ? null : new BigDecimal(numeroDa), 
						numeroA == null ? null : new BigDecimal(numeroA), 
						dataEmissioneDa, 
						dataEmissioneA, 
						isStatoValido
				);
	}
}
