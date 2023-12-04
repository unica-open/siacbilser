/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
import it.csi.siac.pagopa.model.Elaborazione;
import it.csi.siac.pagopa.model.Riconciliazione;
import it.csi.siac.pagopa.model.RiconciliazioneDoc;
import it.csi.siac.siacbilser.business.service.pagopa.util.PagoPAUtils;
import it.csi.siac.siacbilser.integration.dao.PagoPADao;
import it.csi.siac.siacbilser.integration.dao.PagopaElaborazioneDao;
import it.csi.siac.siacbilser.integration.dao.PagopaErroreDao;
import it.csi.siac.siacbilser.integration.dao.PagopaTRiconciliazioneDetRepository;
import it.csi.siac.siacbilser.integration.dao.PagopaTRiconciliazioneRepository;
import it.csi.siac.siacbilser.integration.dao.SiacDFilePagopaStatoRepository;
import it.csi.siac.siacbilser.integration.dao.SiacTEnteProprietarioRepository;
import it.csi.siac.siacbilser.integration.dao.SiacTFilePagopaRepository;
import it.csi.siac.siacbilser.integration.dao.SiacTProvCassaBilRepository;
import it.csi.siac.siacbilser.integration.entity.PagopaDRiconciliazioneErrore;
import it.csi.siac.siacbilser.integration.entity.PagopaTElaborazioneFlusso;
import it.csi.siac.siacbilser.integration.entity.PagopaTRiconciliazione;
import it.csi.siac.siacbilser.integration.entity.PagopaTRiconciliazioneDet;
import it.csi.siac.siacbilser.integration.entity.PagopaTRiconciliazioneDoc;
import it.csi.siac.siacbilser.integration.entity.SiacDFilePagopaStato;
import it.csi.siac.siacbilser.integration.entity.SiacTFilePagopa;
import it.csi.siac.siacbilser.integration.entity.SiacTProvCassa;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDFilePagopaStatoEnum;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacbilser.integration.utility.function.SimpleJDBCFunctionInvoker;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class PagoPADad extends ExtendedBaseDadImpl {
	
	@Autowired
	private PagoPADao pagoPADao;
	
	@Autowired
	private PagopaElaborazioneDao pagopaElaborazioneDao;
	
	@Autowired
	private PagopaErroreDao pagopaErroreDao;
	
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
	
	@Autowired
	private SiacTProvCassaBilRepository siacTProvCassaBilRepository;
	
	@Autowired
	protected SimpleJDBCFunctionInvoker fi;
	
	public SiacTFilePagopa inserisciFlusso(TestataTrasmissioneFlussiType testata, byte[] flussoSintetico) {
		SiacTFilePagopa siacTFilePagopa = new SiacTFilePagopa();
		Date now = new Date();
		siacTFilePagopa.setFilePagopaCode(testata.getIdMessaggio());
		siacTFilePagopa.setFilePagopaIdFlusso(testata.getIdentificativoFlusso());
		siacTFilePagopa.setFilePagopaIdPsp(testata.getIdPSP());
		siacTFilePagopa.setFilePagopa(flussoSintetico);
		siacTFilePagopa.setFilePagopaSize(Long.valueOf(flussoSintetico.length));
		siacTFilePagopa.setFilePagopaAnno(Calendar.getInstance().get(Calendar.YEAR));
		siacTFilePagopa.setSiacDFilePagopaStato(getSiacDFilePagopaStato(SiacDFilePagopaStatoEnum.TRASMESSO));
		siacTFilePagopa.setSiacTEnteProprietario(siacTEnteProprietario);
		siacTFilePagopa.setDataInizioValidita(now);
		siacTFilePagopa.setDataCreazione(now);
		siacTFilePagopa.setDataModifica(now);
		siacTFilePagopa.setLoginOperazione(loginOperazione);		
		siacTFilePagopaRepository.saveAndFlush(siacTFilePagopa);
		return siacTFilePagopa;
	}

	private SiacDFilePagopaStato getSiacDFilePagopaStato(SiacDFilePagopaStatoEnum siacDFilePagopaStatoEnum) {
		return siacDFilePagopaStatoRepository.getStatoByCodice(siacTEnteProprietario, siacDFilePagopaStatoEnum.getCodice());
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
		
		Date now = new Date();
		siacTFilePagopa.setSiacDFilePagopaStato(getSiacDFilePagopaStato(SiacDFilePagopaStatoEnum.ACQUISITO));
		siacTFilePagopa.setDataModifica(now);

		TestataFlussoRiconciliazioneType testataFlussoRiconciliazione = flussoRiconciliazione.getTestataFlusso();

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
		pagopaTRiconciliazione.setPagopaRicFlussoTotPagam(testataFlussoRiconciliazione.getImportoTotalePagamentiFlusso());
		pagopaTRiconciliazione.setPagopaRicFlussoAnnoEsercizio(testataFlussoRiconciliazione.getProvvisorioAnno());
		pagopaTRiconciliazione.setPagopaRicFlussoAnnoProvvisorio(testataFlussoRiconciliazione.getProvvisorioAnno());
		siacTFilePagopa.setFilePagopaAnno(testataFlussoRiconciliazione.getProvvisorioAnno());
		pagopaTRiconciliazione.setPagopaRicFlussoNumProvvisorio(testataFlussoRiconciliazione.getProvvisorioNumero());
		return pagopaTRiconciliazione;
	}

	public void aggiornaStato(SiacTFilePagopa siacTFilePagopa, SiacDFilePagopaStatoEnum siacDFilePagopaStato) {
		siacTFilePagopa.setSiacDFilePagopaStato(getSiacDFilePagopaStato(siacDFilePagopaStato));
		siacTFilePagopa.setDataModifica(new Date());
		siacTFilePagopaRepository.saveAndFlush(siacTFilePagopa);

		log.info("aggiornaStato", "flusso in stato " + siacDFilePagopaStato.name());
	}

	public void aggiornaStato(Integer siacTFilePagopatUid, SiacDFilePagopaStatoEnum siacDFilePagopaStato) {
		siacTFilePagopaRepository.aggiornaStato(siacTFilePagopatUid, siacDFilePagopaStato.getCodice());
		log.info("aggiornaStato", "flusso in stato " + siacDFilePagopaStato.name());
	}

	public SiacTFilePagopa findSiacTFilePagopaByIdMessaggio(String idMessaggio) {
		return StringUtils.isBlank(idMessaggio) ? null : 
			siacTFilePagopaRepository.findByCode(siacTEnteProprietario, idMessaggio);
	}

	public SiacTFilePagopa findSiacTFilePagopaByIdFlusso(String idFlusso) {
		return StringUtils.isBlank(idFlusso) ? null : 
			siacTFilePagopaRepository.findByIdFlusso(siacTEnteProprietario, idFlusso);
	}

	public SiacTFilePagopa findSiacTFilePagopaByIdentificativoFlusso(String identificativoFlusso) {
		if (StringUtils.isBlank(identificativoFlusso)) {
			return null;
		}
		return siacTFilePagopaRepository.findByFlussoId(siacTEnteProprietario, identificativoFlusso);
	}

	public List<SiacTProvCassa> ricercaProvvisori(Integer annoEsercizio, List<String> causali, BigInteger numeroDa, BigInteger numeroA, Date dataEmissioneDa, Date dataEmissioneA, Boolean isStatoValido) {
		return pagoPADao.findProvvisoriCassa(siacTEnteProprietario.getUid(), annoEsercizio, causali, numeroDa == null ? null : new BigDecimal(numeroDa), numeroA == null ? null : new BigDecimal(numeroA), dataEmissioneDa, dataEmissioneA, isStatoValido);
	}
 
	//INIZIO MODIFICHE SIAC-7556 FL per curscotto PAGOPA
	public ListaPaginata<Elaborazione> ricercaElaborazioni(Elaborazione elaborazione, Date dataEmissioneDa, Date dataEmissioneA, Date dataElaborazioneFlussoDa, Date dataElaborazioneFlussoA, String esitoElaborazioneFlussoFE, ParametriPaginazione parametriPaginazione) {		
		Page<PagopaTElaborazioneFlusso> pagopaTElaborazioneFlusso = pagopaElaborazioneDao.ricercaElaborazioni(ente.getUid(), elaborazione.getNumeroProvvisorio(), elaborazione.getFlusso(),
																		dataEmissioneDa, dataEmissioneA, dataElaborazioneFlussoDa, dataElaborazioneFlussoA, esitoElaborazioneFlussoFE, toPageable(parametriPaginazione));
		/* 
		 * Recupero tramite query dei dati del provvisorio di cassa
		 * per il setting della data di emissione 
		 * causa mancanza della valorizzazione della FK
		 * nella tabella elaborazione flussi
		 */
		if(pagopaTElaborazioneFlusso!= null && !pagopaTElaborazioneFlusso.getContent().isEmpty()){
			for(PagopaTElaborazioneFlusso ptef: pagopaTElaborazioneFlusso.getContent()){
				if(ptef.getPagopaElabFlussoNumProvvisorio()!= null && ptef.getPagopaElabFlussoAnnoProvvisorio() != null){
					BigDecimal numProv = new BigDecimal(ptef.getPagopaElabFlussoNumProvvisorio());
					List<SiacTProvCassa> provCassList = siacTProvCassaBilRepository.findProvCassaByAnnoNumTipo(numProv, ptef.getPagopaElabFlussoAnnoProvvisorio(), "E" ,ente.getUid());
					if(provCassList!= null && !provCassList.isEmpty()){
						ptef.setDataEmissioneProv(provCassList.get(0).getProvcDataEmissione());
					}
				}
				//SIAC-7556 - 08/09/2020 - GM: l'esito elaborazione di un flusso va recuperato dalle riconciliazioni legate al flusso stesso
				//se esiste almeno un KO (=doc assente) allora anche il flusso sarà "KO"
				if(ptef.getUid() != null){
					List<PagopaTRiconciliazioneDoc> riconciliazioni = pagopaElaborazioneDao.ricercaRiconciliazioniDocByElabFlussoId(ente.getUid(), ptef.getUid());
					ptef.setEsitoElaborazione("OK");
					if(riconciliazioni != null && !riconciliazioni.isEmpty()){
						for(PagopaTRiconciliazioneDoc ricDoc: riconciliazioni){
							if(ricDoc.getPagopaRicDocSubdocId() == null){
								ptef.setEsitoElaborazione("KO");
								break;
							}
						}
					}
				}				
			}
		}				
		return toListaPaginata(pagopaTElaborazioneFlusso, Elaborazione.class, BilMapId.PagopaTElaborazioneFlusso_Elaborazione);
	}
	
	
	public ListaPaginata<Riconciliazione> ricercaRiconciliazioni(Riconciliazione riconciliazione,   ParametriPaginazione parametriPaginazione) {		
		Page<PagopaTRiconciliazione> pagopaTRiconciliazione = pagopaElaborazioneDao.ricercaRiconciliazioni(ente.getUid(), riconciliazione, toPageable(parametriPaginazione));
		return toListaPaginata(pagopaTRiconciliazione, Riconciliazione.class, BilMapId.PagopaTRiconciliazione_Riconciliazione);
	}
	
	public ListaPaginata<RiconciliazioneDoc> ricercaRiconciliazioniDoc(RiconciliazioneDoc riconciliazioneDoc,   ParametriPaginazione parametriPaginazione) {		
		Page<PagopaTRiconciliazioneDoc> pagopaTRiconciliazioneDoc = pagopaElaborazioneDao.ricercaRiconciliazioniDoc(ente.getUid(), riconciliazioneDoc, toPageable(parametriPaginazione));
		return toListaPaginata(pagopaTRiconciliazioneDoc, RiconciliazioneDoc.class, BilMapId.PagopaTRiconciliazioneDoc_RiconciliazioneDoc);
	}
	
	public ListaPaginata<RiconciliazioneDoc> ricercaRiconciliazioniConDettagli(RiconciliazioneDoc riconciliazione,   ParametriPaginazione parametriPaginazione) {
		Page<Object[]> objects = pagopaElaborazioneDao.ricercaRiconciliazioniConDettagli(ente.getUid(), riconciliazione, toPageable(parametriPaginazione));
		List resultList = new ArrayList();
		for(Object[] objs : objects) {
			PagopaTRiconciliazioneDoc recordSet = new PagopaTRiconciliazioneDoc();
			// SIAC-8046 CM 16/03/2021 Task 2.0 Inizio
			PagopaTRiconciliazione pagopaTRiconciliazione = new PagopaTRiconciliazione();
			// SAIC-8046 CM 19/03/2021 Task 2.1 Inizio
			SiacTFilePagopa siacTFilePagopa = new SiacTFilePagopa();
			SiacDFilePagopaStato siacDFilePagopaStato = new SiacDFilePagopaStato();
			siacTFilePagopa.setSiacDFilePagopaStato(siacDFilePagopaStato);
			// SAIC-8046 CM 19/03/2021 Task 2.1 Fine
			recordSet.setPagopaTRiconciliazione(pagopaTRiconciliazione);
			// SIAC-8046 CM 16/03/2021 Task 2.0 Fine
			if(objs[0] != null){
				recordSet.setErroreDettaglio((Integer)objs[0]);
				try{
					PagopaDRiconciliazioneErrore erroreRiconc = getErrore(recordSet.getErroreDettaglio());
					recordSet.setPagopaDRiconciliazioneErrore(erroreRiconc);
				}catch(Exception e){}
			}
			if(objs[1] != null)
				recordSet.setPagopaRicDocVoceCode((String)objs[1]);
			if(objs[2] != null)
				recordSet.setPagopaRicDocVoceDesc((String)objs[2]);
			if(objs[3] != null)
				recordSet.setPagopaRicDocSottovoceCode((String)objs[3]);
			if(objs[4] != null)
				recordSet.setPagopaRicDocAnnoAccertamento((Integer)objs[4]);
			if(objs[5] != null)
				recordSet.setPagopaRicDocNumAccertamento((Integer)objs[5]);
			// SIAC-8046 CM 16/03/2021 Task 2.0 Inizio
			if(objs[6] != null)
				pagopaTRiconciliazione.setPagopaRicFlussoAnnoAccertamento((Integer)objs[6]);
			if(objs[7] != null)
				pagopaTRiconciliazione.setPagopaRicFlussoNumAccertamento((Integer)objs[7]);
			// SIAC-8046 CM 16/03/2021 Fine
			if(objs[11] != null){
				try{
					PagopaDRiconciliazioneErrore erroreRiconc = getErrore((Integer)objs[11]);
					recordSet.setPagopaDRiconciliazioneErrore(erroreRiconc);
				}catch(Exception e){}
			}
			if(objs[8] != null){
				PagopaTElaborazioneFlusso pagopaTElaborazioneFlusso = new PagopaTElaborazioneFlusso();
				pagopaTElaborazioneFlusso.setUid((Integer)objs[8]);
				try{
					PagopaTElaborazioneFlusso pagopaTElaborazioneFlussoFound = pagopaElaborazioneDao.findById(pagopaTElaborazioneFlusso.getUid());
					recordSet.setPagopaTElaborazioneFlusso(pagopaTElaborazioneFlussoFound);
				}catch(Exception e){
					recordSet.setPagopaTElaborazioneFlusso(pagopaTElaborazioneFlusso);
				}
			}
			if(objs[9] != null)
				recordSet.setPagopaRicDocSottovoceImporto((BigDecimal)objs[9]);
			if(objs[10] != null){
				pagopaTRiconciliazione.setUid((Integer)objs[10]);
			}
			//SIAC-8123 CM 29/03/2021 Intervento 2 Inizio
			if(objs[14] != null){
				recordSet.setPagopaRicDocFlagConDett((Boolean)objs[14]);
			}
			//SIAC-8123 CM 29/03/2021 Intervento 2 Fine
			//SIAC-8046 CM 19/03/2021 Task 2.1 Inizio
			if(objs[15] != null){
				siacTFilePagopa.getSiacDFilePagopaStato().setFilePagopaStatoCode((String)objs[15]);
				pagopaTRiconciliazione.setSiacTFilePagopa(siacTFilePagopa);
			}
			//SIAC-8046 CM 19/03/2021 Task 2.1 Fine
			// SIAC-8046 CM 16/03/2021 Task 2.0 Inizio
			recordSet.setPagopaTRiconciliazione(pagopaTRiconciliazione);
			// SIAC-8046 CM 16/03/2021 Task 2.0 Fine

			if(objs[12] != null){
				//verifico l'esistenza del doc creato, significa che l'elaborazione è andata a buon fine
				recordSet.setPagopaRicDocSubdocId((Integer)objs[12]);
			}else{
				//se non c'è il doc devo verificare lo stato per capire se censire l'errore o rimandare errore generico
				if(objs[13] != null){
					String statoElaborazione = (String)objs[13];
					if(statoElaborazione.equalsIgnoreCase("X")){
						if(recordSet.getPagopaDRiconciliazioneErrore() != null){
							recordSet.setMessaggioErrore(recordSet.getPagopaDRiconciliazioneErrore().getPagopaRicErroreDesc());
						}else{
							recordSet.setMessaggioErrore("ERRORE GENERICO NON CENSITO");
						}
					}else if(statoElaborazione.equalsIgnoreCase("N")){
						
						//SIAC-8123 CM Intervento 2 30/04/2021 Inizio
						if(((recordSet.getPagopaDRiconciliazioneErrore() != null 
								&& recordSet.getPagopaDRiconciliazioneErrore().getPagopaRicErroreId() == null) 
								|| recordSet.getPagopaDRiconciliazioneErrore() == null)
								&& recordSet.getErroreDettaglio() == null 
								&& recordSet.getPagopaRicDocFlagConDett().equals(true)) {
							
							String resDettagliElaboratiDiAggregato = contaRicDocDettagliElaboratiDiAggregato(ente.getUid(), recordSet);
							//non dovrebbero esserci problemi con numeri negativi
							Integer resDettagliElaboratiDiAggregatoConverter = Integer.parseInt(resDettagliElaboratiDiAggregato);
							recordSet.setRicDocDettagliElaboratiDiAggregato(resDettagliElaboratiDiAggregatoConverter);
							
							if(resDettagliElaboratiDiAggregatoConverter.equals(0)) {
								//lo setto vuoto perchè in questo caso non c'è errore perchè sono stati elaborati tutti 
								//i dettagli della riconciliazione (dato aggregato) e l'elaborzione di tutti questi
								//dettagli è andata a buon fine
								recordSet.setMessaggioErrore("");
							}else {
								//in questo caso i dettagli della riconciliazione (aggregato) non sono stati elaborati
								//oppure non sono andate a buon fine le elaborazioni
								//recordSet.setMessaggioErrore("ERRORE NON ELABORATO");
								recordSet.setMessaggioErrore("DETT. NON ELABORATO - ERRORE IN ALTRA RICONCILIAZIONE");

							}
						} else if(((recordSet.getPagopaDRiconciliazioneErrore() != null 
								&& recordSet.getPagopaDRiconciliazioneErrore().getPagopaRicErroreId() == null) 
								|| recordSet.getPagopaDRiconciliazioneErrore() == null)
								&& recordSet.getErroreDettaglio() == null 
								&& recordSet.getPagopaRicDocFlagConDett().equals(false)) {
							
							//recordSet.setMessaggioErrore("NON ELABORATO PER ERRORE IN ALTRA RIGA");
							recordSet.setMessaggioErrore("DETT. NON ELABORATO - ERRORE IN ALTRA RICONCILIAZIONE");
							
						} else {
							//non ricadiamo nei casi della SIAC-8123
							recordSet.setMessaggioErrore("ERRORE GENERICO NON CENSITO");
						}
						//SIAC-8123 CM Intervento 2 30/04/2021 Fine
					}
				}
			}	
			
			resultList.add(recordSet);
		}
		long count = objects.getTotalElements();
		Page pagedList = new PageImpl(resultList, toPageable(parametriPaginazione), count);
		return toListaPaginata(pagedList, RiconciliazioneDoc.class, BilMapId.PagopaTRiconciliazioneDocConDettagli);
	}

	private PagopaDRiconciliazioneErrore getErrore(Integer erroreId){
		return pagopaErroreDao.findById(erroreId);
	}
	//FINE MODIFICHE SIAC-7556 FL per curscotto PAGOPA
	
	
	//SIAC-8046 Task 2.2 CM 31/03/2021 Inizio
	/**
	 * CHECK Se modifica di tipo classe 
	 * @param stm
	 * @return
	 */
	public boolean checkAccertamentoExist(int codiceEnte, Integer annoEsercizio, RiconciliazioneDoc riconciliazioneDoc){

		Integer codiceEnteConverter = Integer.valueOf(codiceEnte);
		String annoEsercizioConverter = annoEsercizio.toString();
		
		boolean exist = false;
		Integer res = 0;
		
		res = pagopaElaborazioneDao.ricercaAccertamentoInRiconciliazione(codiceEnteConverter, annoEsercizioConverter, riconciliazioneDoc);
		
		if(res > 0) {
			exist=true;
		}
		return exist;
	}
	//SIAC-8046 Task 2.2 CM 31/03/2021 Fine
	
	//SIAC-8046 Task 2.2-2.3 CM 13/04/2021 Inizio
	/**
	 * CHECK Se modifica di tipo classe 
	 * 
	 * @param stm
	 * @return
	 */
	public List<Object[]> aggiornaAccertamentoRicModalePagoPa(int codiceEnte, Integer annoEsercizio, RiconciliazioneDoc riconciliazioneDoc){

		Integer codiceEnteConverter = Integer.valueOf(codiceEnte);
    	Integer annoAccConvert = Integer.parseInt(riconciliazioneDoc.getRiconciliazione().getAnno());
		Integer numeroAccConverter = Integer.parseInt(riconciliazioneDoc.getRiconciliazione().getNumeroAccertamento());
		
		final String methodName = "aggiornaAccertamentoRicModale";
		pagopaElaborazioneDao.flush();
		List<Object[]> ris = null;
		//fare un controllo sull'oggetto riconciliazione contenuto in riconciliazioneDoc
		if(riconciliazioneDoc.getRiconciliazione() != null) {
			ris = fi.invokeFunctionToObjectArray("fnc_pagopa_t_riconc_aggiorna_accertamento", riconciliazioneDoc.getRiconciliazione().getRicId(), annoAccConvert, numeroAccConverter, codiceEnteConverter, annoEsercizio);		 
		}

		log.debug(methodName, "elaborazione della funzione fnc_pagopa_t_riconc_aggiorna_accertamento terminata con esito= "+ (ris!= null && !ris.isEmpty() ? ris.get(0).toString():"null"));		
		log.debug(methodName, "accertamento aggiornato");
		return ris;
	}
	//SIAC-8046 Task 2.2-2.3 CM 13/04/2021 Fine
	
	//SIAC-8123 Intervento 2 CM 30/04/2021 Inizio
	/**
	 * fnc_pagopa_t_riconc_doc_cerca_dettagli_elab_di_aggregato
	 * Query che torna il numero di dettagli elaborati correttamente di una riconciliazione (dato aggregato) che si sta considerando
	 * 
	 * @param stm
	 * @return
	 */
	public String contaRicDocDettagliElaboratiDiAggregato(int codiceEnte, PagopaTRiconciliazioneDoc riconciliazioneDoc){

		Integer codiceEnteConverter = Integer.valueOf(codiceEnte);
    	Integer pagopaRicFlussoId = 0;
    	Integer pagopaRicId = 0;
    	
    	if(riconciliazioneDoc.getPagopaTElaborazioneFlusso() != null) {
    		pagopaRicFlussoId = riconciliazioneDoc.getPagopaTElaborazioneFlusso().getUid();
    	}
    	
    	if(riconciliazioneDoc.getPagopaTRiconciliazione() != null) {
    		pagopaRicId = riconciliazioneDoc.getPagopaTRiconciliazione().getUid(); //riconciliazioneDoc.getRiconciliazione().getRicId()
    	}
    	
		final String methodName = "contaRicDocDettagliElaboratiDiAggregato";
		pagopaElaborazioneDao.flush();

		String ris = null;
		//fare un controllo sull'oggetto riconciliazione contenuto in riconciliazioneDoc
		ris = fi.invokeFunctionSingleResult("fnc_pagopa_t_riconc_doc_cerca_dettagli_elab_di_aggregato", pagopaRicId, pagopaRicFlussoId, codiceEnteConverter);		 
		

		log.debug(methodName, "elaborazione della funzione fnc_pagopa_t_riconc_doc_cerca_dettagli_elab_di_aggregato terminata con esito= "+ (ris!= null && !ris.isEmpty() ? ris : "null"));		
		return ris;
	}
	//SIAC-8123 Intervento 2 CM 30/04/2021 Fine
}