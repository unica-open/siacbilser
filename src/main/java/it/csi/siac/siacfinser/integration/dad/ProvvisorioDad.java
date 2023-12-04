/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dad;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccorser.model.Account;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.util.AzioneConsentitaEnum;
import it.csi.siac.siacfinser.CommonUtil;
import it.csi.siac.siacfinser.TimingUtils;
import it.csi.siac.siacfinser.integration.dao.common.dto.DatiOperazioneDto;
import it.csi.siac.siacfinser.integration.dao.movgest.SiacRProvCassaClassFinRepository;
import it.csi.siac.siacfinser.integration.dao.movgest.SiacTClassFinRepository;
import it.csi.siac.siacfinser.integration.dao.ordinativo.SiacROrdinativoProvCassaRepository;
import it.csi.siac.siacfinser.integration.dao.ordinativo.SiacTProvCassaRepository;
import it.csi.siac.siacfinser.integration.dao.provvisorioDiCassa.ProvvisorioDiCassaDao;
import it.csi.siac.siacfinser.integration.dao.provvisorioDiCassa.SiacDProvCassaTipoRepository;
import it.csi.siac.siacfinser.integration.dao.provvisorioDiCassa.SiacSProvCassaFinRepository;
import it.csi.siac.siacfinser.integration.dao.provvisorioDiCassa.SiacTRicercaCausaliPagopaFinRepository;
import it.csi.siac.siacfinser.integration.entity.SiacDProvCassaTipoFin;
import it.csi.siac.siacfinser.integration.entity.SiacRProvCassaClassFin;
import it.csi.siac.siacfinser.integration.entity.SiacSProvCassaFin;
import it.csi.siac.siacfinser.integration.entity.SiacTClassFin;
import it.csi.siac.siacfinser.integration.entity.SiacTEnteProprietarioFin;
import it.csi.siac.siacfinser.integration.entity.SiacTProvCassaFin;
import it.csi.siac.siacfinser.integration.entity.SiacTRicercaCausaliPagopaFin;
import it.csi.siac.siacfinser.integration.entity.mapping.FinMapId;
import it.csi.siac.siacfinser.integration.util.DatiOperazioneUtil;
import it.csi.siac.siacfinser.integration.util.EntityProvvisorioDiCassaToModelProvvisorioDiCassaConverter;
import it.csi.siac.siacfinser.integration.util.Operazione;
import it.csi.siac.siacfinser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.model.provvisoriDiCassa.ProvvisorioDiCassa;
import it.csi.siac.siacfinser.model.provvisoriDiCassa.ProvvisorioDiCassa.TipoProvvisorioDiCassa;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaProvvisorio;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class ProvvisorioDad extends AbstractFinDad
{

	@Autowired
	private ProvvisorioDiCassaDao provvisorioDiCassaDao;

	@Autowired
	private SiacROrdinativoProvCassaRepository siacROrdinativoProvCassaRepository;

	@Autowired
	private SiacTProvCassaRepository siacTProvCassaRepository;

	//SIAC-8140
	@Autowired
	private SiacSProvCassaFinRepository siacSProvCassaRepository;
	
	@Autowired
	private SiacTRicercaCausaliPagopaFinRepository siacTRicercaCausaliPagopaRepository;

	@Autowired
	private SiacDProvCassaTipoRepository siacDProvCassaTipoRepository;

	@Autowired
	private SiacRProvCassaClassFinRepository siacRProvCassaClassFinRepository;

	@Autowired
	private SiacTClassFinRepository siacTClassFinRepository;

	public List<Integer> ricercaProvvisoriDiCassaSoloIds(ParametroRicercaProvvisorio prp, Integer idEnte)
	{
		
		List<SiacTRicercaCausaliPagopaFin> lricPa = new ArrayList<SiacTRicercaCausaliPagopaFin>();
		List<String> listaCodici = new ArrayList<String>();
		
		if (prp.getFlagProvvisoriPagoPA() !=null && prp.getFlagProvvisoriPagoPA()){
			lricPa = siacTRicercaCausaliPagopaRepository.findBySiacTRicercaCausaliPaAll(idEnte);
			for (SiacTRicercaCausaliPagopaFin ricPa : lricPa){
				listaCodici.add(ricPa.getRiccausCode());
			}
		}
		
		List<Integer> ids = provvisorioDiCassaDao.ricercaProvvisoriDiCassaSoloIds(idEnte, prp,listaCodici);
		// List<Integer> idsNoFunction =
		// provvisorioDiCassaDao.ricercaProvvisoriDiCassaSoloIdsOldNoFunction(idEnte,
		// prp);
		return ids;
	}

	public List<ProvvisorioDiCassa> caricaPaginaProvvisoriDiCassa(DatiOperazioneDto datiOperazione, int numeroPagina,
			int numeroRisultatiPerPagina, List<Integer> listaId)
	{
		List<SiacTProvCassaFin> elencoSiacTProvCassa = new ArrayList<SiacTProvCassaFin>();
		List<ProvvisorioDiCassa> elencoProvvisoriDiCassa = new ArrayList<ProvvisorioDiCassa>();
		List<Integer> paginaIds = getIdsPaginatiList(listaId, numeroPagina, numeroRisultatiPerPagina);
		if (paginaIds != null && paginaIds.size() > 0)
		{
			elencoSiacTProvCassa = siacTProvCassaRepository.findByListIds(paginaIds);
			if (elencoSiacTProvCassa != null && elencoSiacTProvCassa.size() > 0)
			{
				elencoProvvisoriDiCassa = convertiListaToModel(elencoSiacTProvCassa, datiOperazione);
			}
		}
		// Termino restituendo l'oggetto di ritorno:
		return elencoProvvisoriDiCassa;
	}

	private ProvvisorioDiCassa convertiToModel(SiacTProvCassaFin siacTProvCassaFin, DatiOperazioneDto datiOperazione)
	{
		return CommonUtil.getFirst(convertiListaToModel(CommonUtil.toList(siacTProvCassaFin), datiOperazione));
	}

	private List<ProvvisorioDiCassa> convertiListaToModel(List<SiacTProvCassaFin> elencoSiacTProvCassa,
			DatiOperazioneDto datiOperazione)
	{
		List<ProvvisorioDiCassa> elencoProvvisoriDiCassa = new ArrayList<ProvvisorioDiCassa>();
		if (null != elencoSiacTProvCassa && elencoSiacTProvCassa.size() > 0)
		{

			for (SiacTProvCassaFin itsiac : elencoSiacTProvCassa)
			{
				// dati che a volte ricarica male (es. appena dopo un
				// aggiornamento):
				itsiac = setLegamiRelazionati(itsiac, datiOperazione);
				//
			}

			elencoProvvisoriDiCassa = convertiLista(elencoSiacTProvCassa, ProvvisorioDiCassa.class,
					FinMapId.SiacTProvCassa_ProvvisorioDiCassa);
			elencoProvvisoriDiCassa = EntityProvvisorioDiCassaToModelProvvisorioDiCassaConverter
					.siacTProvCassaEntityToProvvisorioDiCassaModel(elencoSiacTProvCassa, elencoProvvisoriDiCassa,
							datiOperazione);

			elencoProvvisoriDiCassa = completaImportiDaRegolarizzare(elencoProvvisoriDiCassa);
		}
		return elencoProvvisoriDiCassa;
	}

	private List<ProvvisorioDiCassa> completaImportiDaRegolarizzare(List<ProvvisorioDiCassa> elencoProvvisoriDiCassa)
	{
		// completiamo l'importo da regolarizzare:
		if (elencoProvvisoriDiCassa != null && elencoProvvisoriDiCassa.size() > 0)
		{
			for (ProvvisorioDiCassa provvIt : elencoProvvisoriDiCassa)
			{
				BigInteger idProvvIt = provvIt.getIdProvvisorioDiCassa();
				if (idProvvIt != null)
				{
					BigDecimal importoDaRegolarizzare = provvisorioDiCassaDao
							.calcolaImportoDaRegolarizzare(idProvvIt.intValue());
					provvIt.setImportoDaRegolarizzare(importoDaRegolarizzare);
				}
			}
		}
		return elencoProvvisoriDiCassa;
	}

	/**
	 * Effettua la ricerca puntuale sul provvisorio indicato
	 * 
	 * @param idEnte
	 * @param annoProvvisorio
	 * @param numeroProvvisorio
	 * @param tipoProvvisorio
	 * @param now
	 * @return
	 */
	public ProvvisorioDiCassa ricercaProvvisorioDiCassaPerChiave(Integer idEnte, Integer annoProvvisorio,
			Integer numeroProvvisorio, String tipoProvvisorio, DatiOperazioneDto datiOperazione)
	{
		ProvvisorioDiCassa provvisorioDiCassaTrovato = null;

		SiacTProvCassaFin siacTProvCassa = provvisorioDiCassaDao.ricercaProvvisorioDiCassaPerChiave(idEnte,
				annoProvvisorio, numeroProvvisorio, datiOperazione.getTs(), tipoProvvisorio);
		if (siacTProvCassa != null)
		{

			//
			siacTProvCassa = setLegamiRelazionati(siacTProvCassa, datiOperazione);
			//

			provvisorioDiCassaTrovato = map(siacTProvCassa, ProvvisorioDiCassa.class,
					FinMapId.SiacTProvCassa_ProvvisorioDiCassa);

			// SIAC-3372 Ricerca provvisorio per chiave - Importo da
			// regolarizzare
			BigDecimal importoDaRegolarizzare = provvisorioDiCassaDao
					.calcolaImportoDaRegolarizzare(siacTProvCassa.getProvcId());
			provvisorioDiCassaTrovato.setImportoDaRegolarizzare(importoDaRegolarizzare);
			//

			provvisorioDiCassaTrovato = EntityProvvisorioDiCassaToModelProvvisorioDiCassaConverter
					.siacTProvCassaEntityToProvvisorioDiCassaModelPerChiave(siacTProvCassa, provvisorioDiCassaTrovato,
							datiOperazione);

		}
		// Termino restituendo l'oggetto di ritorno:
		return provvisorioDiCassaTrovato;
	}

	private SiacTProvCassaFin setLegamiRelazionati(SiacTProvCassaFin siacTProvCassa, DatiOperazioneDto datiOperazione)
	{

		if (siacTProvCassa != null)
		{

			// legame verso ordinativo:
			siacTProvCassa.setSiacROrdinativoProvCassas(
					siacROrdinativoProvCassaRepository.findROrdinativoProvCassaByIdProvvisorio(
							siacTProvCassa.getSiacTEnteProprietario().getEnteProprietarioId(), siacTProvCassa.getUid(),
							datiOperazione.getTs()));

			// legame verso provv cassa:
			// List<SiacRProvCassaClassFin> siacRProvCassaClasses =
			// siacRProvCassaClassFinRepository.findAllValidiByProvvCassa(siacTProvCassa.getUid(),
			// datiOperazione.getTs());
			// siacTProvCassa.setSiacRProvCassaClasses(siacRProvCassaClasses );

			List<SiacRProvCassaClassFin> all = siacRProvCassaClassFinRepository
					.findAllByProvvCassa(siacTProvCassa.getUid());

			List<SiacRProvCassaClassFin> siacRProvCassaClasses = CommonUtil.soloValidiSiacTBase(all,
					datiOperazione.getTs());

			// SiacRProvCassaClassFin validoTestUno =
			// CommonUtil.getValidoSiacTBase(all, datiOperazione.getTs());
			// SiacRProvCassaClassFin validoTestDue =
			// CommonUtil.getValidoSiacTBase(all, null);
			// SiacRProvCassaClassFin validoZero =
			// CommonUtil.getFirst(siacRProvCassaClasses);

			siacTProvCassa.setSiacRProvCassaClasses(siacRProvCassaClasses);

		}

		return siacTProvCassa;
	}

	public ProvvisorioDiCassa aggiornaProvvisorioDiCassa(ProvvisorioDiCassa provvisorioDaModificare, Bilancio bilancio,
			DatiOperazioneDto datiOperazioneDto)
	{

		SiacTProvCassaFin siacTProvCassa = saveSiacTProvCassa(provvisorioDaModificare, datiOperazioneDto);

		// per leggere i validi dopo aver appena scritto:
		datiOperazioneDto.setCurrMillisec(getCurrentMilliseconds());
		//
		ProvvisorioDiCassa provvisorioDiCassa = convertiToModel(siacTProvCassa, datiOperazioneDto);

		return provvisorioDiCassa;
	}

	/**
	 * Si occupa di effettuare i controlli di merito da verificare prima
	 * dell'aggiornamento di un provvisorio
	 * 
	 * @param provvisorioDaModificare
	 * @param bilancio
	 * @param datiOperazioneDto
	 * @return
	 */
	public List<Errore> controlliDiMeritoAggiornamentoProvvisorioDiCassa(ProvvisorioDiCassa provvisorioDaModificare,
			Bilancio bilancio, DatiOperazioneDto datiOperazioneDto)
	{

		List<Errore> listaErrori = new ArrayList<Errore>();

		Errore univocitaError = verificaUnivocitaProvvisorio(provvisorioDaModificare, bilancio, datiOperazioneDto,
				provvisorioDaModificare);

		if (univocitaError != null)
		{
			listaErrori.add(univocitaError);
			return listaErrori;
		}

		// QUI SI POSSONO IMPLEMENTARE ALTRI EVENTUALI CONTROLLI SPECIFICI PER
		// L'AGGIORNAMENTO...

		return listaErrori;
	}

	/**
	 * Si occupa di effettuare i controlli di merito da verificare prima
	 * dell'inserimento di un nuovo provvisorio
	 * 
	 * @param provvisorioDaCreare
	 * @param bilancio
	 * @param datiOperazioneDto
	 * @return
	 */
	public List<Errore> controlliDiMeritoInserimentoProvvisorioDiCassa(ProvvisorioDiCassa provvisorioDaCreare,
			Bilancio bilancio, DatiOperazioneDto datiOperazioneDto)
	{

		List<Errore> listaErrori = new ArrayList<Errore>();

		Errore univocitaError = verificaUnivocitaProvvisorio(provvisorioDaCreare, bilancio, datiOperazioneDto, null);

		if (univocitaError != null)
		{
			listaErrori.add(univocitaError);
			return listaErrori;
		}

		// QUI SI POSSONO IMPLEMENTARE ALTRI EVENTUALI CONTROLLI SPECIFICI PER
		// L'INSERIMENTO...

		return listaErrori;
	}

	/**
	 * Si occupa di verificare l'univocita' di un provvisorio in inserimento o
	 * in modifica. Va a verificare che gia' non esita un altro provvisorio con
	 * lo stetto numero/anno e tipologia (s o e)
	 * 
	 * provvisorioDaModificare e' opzionale, viene popolato solo se siamo in
	 * modifica
	 * 
	 * @param provvisorioDaSalvare
	 * @param bilancio
	 * @param datiOperazioneDto
	 * @param provvisorioDaModificare
	 * @return
	 */
	private Errore verificaUnivocitaProvvisorio(ProvvisorioDiCassa provvisorioDaSalvare, Bilancio bilancio,
			DatiOperazioneDto datiOperazioneDto, ProvvisorioDiCassa provvisorioDaModificare)
	{

		Integer idEnte = datiOperazioneDto.getSiacTEnteProprietario().getEnteProprietarioId();
		Integer annoProvvisorio = provvisorioDaSalvare.getAnno();
		Integer numeroProvvisorio = provvisorioDaSalvare.getNumero();
		Timestamp now = datiOperazioneDto.getTs();
		String tipoProvvisorio = provvisorioDaSalvare.getTipoProvvisorioDiCassa().toString();

		SiacTProvCassaFin siacTProvCassa = provvisorioDiCassaDao.ricercaProvvisorioDiCassaPerChiave(idEnte,
				annoProvvisorio, numeroProvvisorio, now, tipoProvvisorio);
		if (siacTProvCassa != null && siacTProvCassa.getProvcId() != null)
		{

			boolean errore = true;

			if (provvisorioDaModificare != null && provvisorioDaModificare.getIdProvvisorioDiCassa() != null)
			{

				if (provvisorioDaModificare.getIdProvvisorioDiCassa().intValue() == siacTProvCassa.getProvcId()
						.intValue())
				{
					// NON SI LANCIA ERRORE
					errore = false;
				}

			}

			if (errore)
			{
				String provvisorioString = "Provvisorio di spesa";
				if (provvisorioDaSalvare.getTipoProvvisorioDiCassa().equals(TipoProvvisorioDiCassa.E))
				{
					provvisorioString = "Provvisorio di entrata";
				}
				return ErroreFin.ENTITA_GIA_PRESENTE.getErrore(provvisorioString,
						annoProvvisorio + "/" + numeroProvvisorio);
			}

		}

		return null;

	}

	public ProvvisorioDiCassa inserisciProvvisorioDiCassa(ProvvisorioDiCassa provvisorioDaCreare, Bilancio bilancio,
			DatiOperazioneDto datiOperazioneDto)
	{

		provvisorioDaCreare.setIdProvvisorioDiCassa(null);
		SiacTProvCassaFin siacTProvCassa = saveSiacTProvCassa(provvisorioDaCreare, datiOperazioneDto);

		// per leggere i validi dopo aver appena scritto:
		datiOperazioneDto.setCurrMillisec(getCurrentMilliseconds());
		//
		ProvvisorioDiCassa provvisorioDiCassa = convertiToModel(siacTProvCassa, datiOperazioneDto);

		return provvisorioDiCassa;
	}
	
	public void salvaStoricoProvvisorioDiCassa(ProvvisorioDiCassa provvisorioDaStoricizzare, DatiOperazioneDto datiOperazioneDto) {
		
		Integer uidProv = Integer.valueOf(provvisorioDaStoricizzare.getIdProvvisorioDiCassa().intValue());
		SiacTProvCassaFin siacTProvCassaDb = siacTProvCassaRepository.findOne(uidProv);
		Date provcDataInvioServizio = siacTProvCassaDb.getProvcDataInvioServizio();
		Boolean accettatoSuDb = siacTProvCassaDb.getAccettato();
		
		if(accettatoSuDb != null && accettatoSuDb.equals(provvisorioDaStoricizzare.getAccettato())) {
			return;
		}
		
		List<SiacTClassFin> siacTClasses = siacTProvCassaRepository.findSiacTClassValido(uidProv, datiOperazioneDto.getSiacTEnteProprietario().getUid(),datiOperazioneDto.getTs());
		if(siacTClasses != null && siacTClasses.size() >1) {
			throw new BusinessException(ErroreCore.ERRORE_DI_SISTEMA.getErrore("sono presenti piu' classificatori validi per il provvisorio"));
		}
		SiacTClassFin siacTClass = siacTClasses != null && !siacTClasses.isEmpty()? siacTClasses.get(0) : null;
		
		SiacSProvCassaFin siacStorico = popolaSiacSProvCassaFin(datiOperazioneDto, uidProv, provcDataInvioServizio,
				siacTClass);
		
		siacSProvCassaRepository.saveAndFlush(siacStorico);
		
		cancellaRecordObsoleti(datiOperazioneDto, uidProv);
	}

	protected SiacSProvCassaFin popolaSiacSProvCassaFin(DatiOperazioneDto datiOperazioneDto, Integer uidProv,
			Date provcDataInvioServizio, SiacTClassFin siacTClass) {
		//legame con la SAC
		datiOperazioneDto.setOperazione(Operazione.INSERIMENTO);
		
		SiacSProvCassaFin siacStorico = new SiacSProvCassaFin();		
		siacStorico =DatiOperazioneUtil.impostaDatiOperazioneLogin(siacStorico, datiOperazioneDto,
				siacTAccountRepository);
		
		//legame con il provvisorio
		SiacTProvCassaFin siacTProvCassa =  new SiacTProvCassaFin();		
		siacTProvCassa.setUid(uidProv);
		siacStorico.setSiacTProvCassaFin(siacTProvCassa);
		
		if(siacTClass != null) {
			siacStorico.setSiacTClassSAC(siacTClass);
			siacStorico.setSacCode(siacTClass.getClassifCode());
			siacStorico.setSacDesc(siacTClass.getClassifDesc());
			siacStorico.setSacTipoCode(siacTClass.getSiacDClassTipo().getClassifTipoCode());
			siacStorico.setSacTipoDesc(siacTClass.getSiacDClassTipo().getClassifTipoDesc());
		}
				
		siacStorico.setProvcDataInvioServizio(provcDataInvioServizio);
		return siacStorico;
	}

	protected void cancellaRecordObsoleti(DatiOperazioneDto datiOperazioneDto, Integer uidProv) {
		List<SiacSProvCassaFin> founds = siacSProvCassaRepository.findByprovcIdOrderedByDate(uidProv, datiOperazioneDto.getSiacTEnteProprietario().getUid());
		
		if(founds != null && !founds.isEmpty() && founds.size() >5) {
			datiOperazioneDto.setOperazione(Operazione.CANCELLAZIONE_LOGICA_RECORD);
			List<SiacSProvCassaFin> subList = founds.subList(5, founds.size());
			
			for (SiacSProvCassaFin siacSProvCassaFin : subList) {
				DatiOperazioneUtil.impostaDatiOperazioneLogin(siacSProvCassaFin,datiOperazioneDto,
						siacTAccountRepository);
			}
			siacSProvCassaRepository.save(subList);
		}
	}
	
	/**
	 * Si occupa di inserire o aggiornare un record di provv cassa
	 * 
	 * @param provvisorioDaCreare
	 * @param datiOperazioneDto
	 * @return
	 */
	private SiacTProvCassaFin saveSiacTProvCassa(ProvvisorioDiCassa provvisorioDaCreare,
			DatiOperazioneDto datiOperazioneDto)
	{
		SiacTProvCassaFin siacTProvCassa = new SiacTProvCassaFin();

		Integer enteProprietarioId = datiOperazioneDto.getSiacTEnteProprietario().getEnteProprietarioId();

		if (provvisorioDaCreare.getIdProvvisorioDiCassa() == null
				|| provvisorioDaCreare.getIdProvvisorioDiCassa().intValue() <= 0)
		{
			datiOperazioneDto.setOperazione(Operazione.INSERIMENTO);
			// TIPO:
			TipoProvvisorioDiCassa tipoProvv = provvisorioDaCreare.getTipoProvvisorioDiCassa();
			List<SiacDProvCassaTipoFin> tipologie = siacDProvCassaTipoRepository.findByTipoAndEnte(tipoProvv.toString(),
					enteProprietarioId, datiOperazioneDto.getTs());
			// deve contenerne uno e uno solo:
			SiacDProvCassaTipoFin tipo = CommonUtil.getFirst(tipologie);
			siacTProvCassa.setSiacDProvCassaTipo(tipo);
		}
		else
		{

			datiOperazioneDto.setOperazione(Operazione.MODIFICA);
			siacTProvCassa.setProvcId(provvisorioDaCreare.getIdProvvisorioDiCassa().intValue());

			// RICARICHIAMO DAL DB (cosi dopo verranno settati solo i dati
			// modificabili):
			siacTProvCassa = siacTProvCassaRepository.findOne(provvisorioDaCreare.getIdProvvisorioDiCassa().intValue());

		}

		// DATI LOGIN:
		siacTProvCassa = DatiOperazioneUtil.impostaDatiOperazioneLogin(siacTProvCassa, datiOperazioneDto,
				siacTAccountRepository);

		// numero
		siacTProvCassa.setProvcNumero(new BigDecimal(provvisorioDaCreare.getNumero()));

		// anno di esercizio
		siacTProvCassa.setProvcAnno(provvisorioDaCreare.getAnno());

		// data emissione
		siacTProvCassa
				.setProvcDataEmissione(TimingUtils.convertiDataInTimeStamp(provvisorioDaCreare.getDataEmissione()));

		// data trasmissione
		siacTProvCassa
		.setProvcDataTrasmissione(TimingUtils.convertiDataInTimeStamp(provvisorioDaCreare.getDataTrasmissione()));

		siacTProvCassa.setProvcDataInvioServizio(provvisorioDaCreare.getDataInvioServizio());
		siacTProvCassa.setProvcDataPresaInCaricoServizio(provvisorioDaCreare.getDataPresaInCaricoServizio());
		siacTProvCassa.setProvcDataRifiutoErrataAttribuzione(provvisorioDaCreare.getDataRifiutoErrataAttribuzione());

		// denominazione soggetto
		siacTProvCassa.setProvcDenomSoggetto(provvisorioDaCreare.getDenominazioneSoggetto());

		// des causale
		siacTProvCassa.setProvcCausale(provvisorioDaCreare.getCausale());

		// conto evidenza
		siacTProvCassa.setProvcCodiceContoEvidenza(provvisorioDaCreare.getCodiceContoEvidenza());
		siacTProvCassa.setProvcDescrizioneContoEvidenza(provvisorioDaCreare.getDescrizioneContoEvidenza());

		// importo
		siacTProvCassa.setProvcImporto(provvisorioDaCreare.getImporto());

		siacTProvCassa.setAccettato(provvisorioDaCreare.getAccettato());
		siacTProvCassa.setNote(provvisorioDaCreare.getNote());

		
		siacTProvCassa = siacTProvCassaRepository.saveAndFlush(siacTProvCassa);

		if (modificataStrutturaAmministrativa(siacTProvCassa, provvisorioDaCreare, datiOperazioneDto))
		{
			// STRUTTURA AMMINISTRATIVA CAMBIATA:
			SiacRProvCassaClassFin nuova = aggiornaStrutturaAmministrativa(siacTProvCassa, provvisorioDaCreare,
					datiOperazioneDto);
			siacTProvCassa.setSiacRProvCassaClasses(toList(nuova));// gli altri
																	// legami
																	// sono
																	// quelli
																	// invalidati..
		}

		return siacTProvCassa;
	}

	private boolean modificataStrutturaAmministrativa(SiacTProvCassaFin siacTProvCassa,
			ProvvisorioDiCassa provvisorioDaAggiornare, DatiOperazioneDto datiOperazione)
	{

		boolean modificata = false;

		if (provvisorioDaAggiornare.getIdProvvisorioDiCassa() != null
				&& provvisorioDaAggiornare.getIdProvvisorioDiCassa().intValue() > 0)
		{
			// QUESTO METODO HA SENSO SOLO SE SIAMO IN MODIFICA

			Integer nuovaStrutturaAmm = null;
			if (provvisorioDaAggiornare.getStrutturaAmministrativoContabile() != null
					&& provvisorioDaAggiornare.getStrutturaAmministrativoContabile().getUid() > 0)
			{
				nuovaStrutturaAmm = provvisorioDaAggiornare.getStrutturaAmministrativoContabile().getUid();
			}

			SiacRProvCassaClassFin legameValido = null;
			if (siacTProvCassa.getSiacRProvCassaClasses() != null)
			{
				legameValido = DatiOperazioneUtil.getValido(siacTProvCassa.getSiacRProvCassaClasses(),
						datiOperazione.getTs());
			}

			if (legameValido == null && nuovaStrutturaAmm != null)
			{
				// passa da struttura non presente a struttura presente
				modificata = true;
			}
			else if (legameValido != null && nuovaStrutturaAmm != null)
			{
				Integer idOld = legameValido.getSiacTClass().getUid();
				if (idOld.intValue() != nuovaStrutturaAmm.intValue())
				{
					// struttura gia' presente ma modificata in favore di
					// un'altra
					modificata = true;
				}
			}
			else if (legameValido != null && nuovaStrutturaAmm == null)
			{
				// passa da struttura presente a struttura non presente
				modificata = true;
			}
		}

		return modificata;
	}

	private SiacRProvCassaClassFin aggiornaStrutturaAmministrativa(SiacTProvCassaFin siacTProvCassa,
			ProvvisorioDiCassa provvisorioDaAggiornare, DatiOperazioneDto datiOperazioneDto)
	{

		SiacRProvCassaClassFin strutturaValida = null;

		Operazione operazioneIniziale = null;
		if (datiOperazioneDto != null)
		{
			operazioneIniziale = datiOperazioneDto.getOperazione();
		}

		if (provvisorioDaAggiornare.getIdProvvisorioDiCassa() != null
				&& provvisorioDaAggiornare.getIdProvvisorioDiCassa().intValue() > 0 && datiOperazioneDto != null
				&& siacTProvCassa != null)
		{
			// QUESTO METODO HA SENSO SOLO SE SIAMO IN MODIFICA

			Integer nuovaStrutturaAmm = null;
			if (provvisorioDaAggiornare.getStrutturaAmministrativoContabile() != null
					&& provvisorioDaAggiornare.getStrutturaAmministrativoContabile().getUid() > 0)
			{
				nuovaStrutturaAmm = provvisorioDaAggiornare.getStrutturaAmministrativoContabile().getUid();
			}

			List<SiacRProvCassaClassFin> legamiValidi = null;
			if (siacTProvCassa.getSiacRProvCassaClasses() != null)
			{
				legamiValidi = DatiOperazioneUtil.soloValidi(siacTProvCassa.getSiacRProvCassaClasses(),
						datiOperazioneDto.getTs());
			}

			// INVALIDIAMO L'EVENTUALE VECCHIO RECORD:
			if (legamiValidi != null && legamiValidi.size() > 0)
			{
				datiOperazioneDto.setOperazione(Operazione.CANCELLAZIONE_LOGICA_RECORD);
				// dovrebbe essere sempre e solo un valido alla volta, cosi
				// facendo
				// mi metto al sicuro da eventuali dati incoerenti sul db:
				for (SiacRProvCassaClassFin legameValido : legamiValidi)
				{
					if (legameValido != null)
					{
						DatiOperazioneUtil.cancellaRecord(legameValido, siacRProvCassaClassFinRepository,
								datiOperazioneDto, siacTAccountRepository);
					}
				}
			}

			// CREO IL NUOVO EVENTUALE LEGAME:
			if (nuovaStrutturaAmm != null)
			{
				SiacRProvCassaClassFin nuovoLegameStruttura = new SiacRProvCassaClassFin();
				SiacTClassFin nuovaStruttura = siacTClassFinRepository.findOne(nuovaStrutturaAmm);
				nuovoLegameStruttura.setSiacTClass(nuovaStruttura);
				nuovoLegameStruttura.setSiacTProvCassaFin(siacTProvCassa);

				// DATI LOGIN:
				datiOperazioneDto.setOperazione(Operazione.INSERIMENTO);
				nuovoLegameStruttura = DatiOperazioneUtil.impostaDatiOperazioneLogin(nuovoLegameStruttura,
						datiOperazioneDto, siacTAccountRepository);

				strutturaValida = siacRProvCassaClassFinRepository.saveAndFlush(nuovoLegameStruttura);

			}
			else
			{
				strutturaValida = null;// e' stata invalidata
			}

		}

		if (datiOperazioneDto != null)
		{
			datiOperazioneDto.setOperazione(operazioneIniziale);
		}

		return strutturaValida;
	}

	public void aggiornaSacProvvisoriDiCassa(List<ProvvisorioDiCassa> elencoProvvisoriDiCassa, Ente ente,
			Richiedente richiedente)
	{
		for (ProvvisorioDiCassa provvisorioDiCassa : elencoProvvisoriDiCassa) {
			
			aggiornaSacProvvisorioDiCassa(provvisorioDiCassa);
		}
	}

	private void aggiornaSacProvvisorioDiCassa(ProvvisorioDiCassa provvisorioDiCassa)
	{
		if (provvisorioDiCassa.getUid() == -1) {
			return;
		}
		
		eliminaSiacRProvCassaClass(provvisorioDiCassa);

		if (provvisorioDiCassa.getStrutturaAmministrativoContabile().getUid() > 0) {
			
			inserisciSiacRProvCassaClass(provvisorioDiCassa);

			Date current_day = DateUtils.truncate(new Date(getCurrentMilliseconds()), Calendar.DAY_OF_MONTH);
			
			aggiornaDateFlagAccettato(provvisorioDiCassa, current_day, current_day, null, Boolean.TRUE);
			
			return;
		} 
		
		aggiornaDateFlagAccettato(provvisorioDiCassa, null, null, null, null);
	}

	private void aggiornaDateFlagAccettato(
			ProvvisorioDiCassa provvisorioDiCassa, Date dataInvioServizio, Date dataPresaInCaricoServizio, Date dataRifiutoErrataAttribuzione, Boolean accettato) {
		
		SiacTProvCassaFin siacTProvCassaFin = siacTProvCassaRepository.findOne(provvisorioDiCassa.getUid());
		
		siacTProvCassaFin.setAccettato(accettato);
		siacTProvCassaFin.setProvcDataInvioServizio(dataInvioServizio);
		siacTProvCassaFin.setProvcDataPresaInCaricoServizio(dataPresaInCaricoServizio);
		siacTProvCassaFin.setProvcDataRifiutoErrataAttribuzione(dataRifiutoErrataAttribuzione);
		siacTProvCassaFin.setDataModifica(new Date());
		siacTProvCassaFin.setLoginOperazione(loginOperazione);
		
		siacTProvCassaRepository.saveAndFlush(siacTProvCassaFin);
	}

	private void inserisciSiacRProvCassaClass(ProvvisorioDiCassa provvisorioDiCassa)
	{
		SiacRProvCassaClassFin siacRProvCassaClass = new SiacRProvCassaClassFin();

		Date now = new Date(getCurrentMilliseconds());
		
		SiacTProvCassaFin siacTProvCassaFin = siacTProvCassaRepository.findOne(provvisorioDiCassa.getUid());
		siacRProvCassaClass.setSiacTProvCassaFin(siacTProvCassaFin);
		SiacTClassFin siacTClass = siacTClassFinRepository
				.findOne(provvisorioDiCassa.getStrutturaAmministrativoContabile().getUid());
		siacRProvCassaClass.setSiacTClass(siacTClass);
		siacRProvCassaClass.setLoginOperazione(loginOperazione);
		siacRProvCassaClass.setDataCreazione(now);
		siacRProvCassaClass.setDataModifica(now);
		siacRProvCassaClass.setDataInizioValidita(now);
		siacRProvCassaClass.setSiacTEnteProprietario(super.map(ente, SiacTEnteProprietarioFin.class));

		siacRProvCassaClassFinRepository.saveAndFlush(siacRProvCassaClass);
	}

	private void eliminaSiacRProvCassaClass(ProvvisorioDiCassa provvisorioDiCassa)
	{
		siacRProvCassaClassFinRepository
				.logicalRemove(provvisorioDiCassa.getUid(), loginOperazione);
	}

}
