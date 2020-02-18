/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dad;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siacfinser.Constanti;
import it.csi.siac.siacfinser.StringUtils;
import it.csi.siac.siacfinser.TimingUtils;
import it.csi.siac.siacfinser.integration.dao.common.SiacDAmbitoRepository;
import it.csi.siac.siacfinser.integration.dao.common.SiacTEnteProprietarioFinRepository;
import it.csi.siac.siacfinser.integration.dao.common.dto.DatiOperazioneDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.RicercaMutuoParamDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.VariazioniVociDiMutuoInfoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.VociDiMutuoInfoDto;
import it.csi.siac.siacfinser.integration.dao.movgest.SiacTAttoAmmFinRepository;
import it.csi.siac.siacfinser.integration.dao.movgest.SiacTMovgestTsRepository;
import it.csi.siac.siacfinser.integration.dao.mutuo.SiacDMutuoStatoRepository;
import it.csi.siac.siacfinser.integration.dao.mutuo.SiacDMutuoTipoRepository;
import it.csi.siac.siacfinser.integration.dao.mutuo.SiacDMutuoVarTipoRepository;
import it.csi.siac.siacfinser.integration.dao.mutuo.SiacDMutuoVoceTipoRepository;
import it.csi.siac.siacfinser.integration.dao.mutuo.SiacRMutuoAttoAmmRepository;
import it.csi.siac.siacfinser.integration.dao.mutuo.SiacRMutuoSoggettoRepository;
import it.csi.siac.siacfinser.integration.dao.mutuo.SiacRMutuoStatoRepository;
import it.csi.siac.siacfinser.integration.dao.mutuo.SiacRMutuoVoceMovgestRepository;
import it.csi.siac.siacfinser.integration.dao.mutuo.SiacTMutuoVoceRepository;
import it.csi.siac.siacfinser.integration.dao.mutuo.SiacTMutuoVoceVarRepository;
import it.csi.siac.siacfinser.integration.dao.soggetto.SiacTSoggettoFinRepository;
import it.csi.siac.siacfinser.integration.entity.SiacDAmbitoFin;
import it.csi.siac.siacfinser.integration.entity.SiacDMutuoStatoFin;
import it.csi.siac.siacfinser.integration.entity.SiacDMutuoTipoFin;
import it.csi.siac.siacfinser.integration.entity.SiacDMutuoVarTipoFin;
import it.csi.siac.siacfinser.integration.entity.SiacDMutuoVoceTipoFin;
import it.csi.siac.siacfinser.integration.entity.SiacDSoggettoStatoFin;
import it.csi.siac.siacfinser.integration.entity.SiacRMutuoAttoAmmFin;
import it.csi.siac.siacfinser.integration.entity.SiacRMutuoSoggettoFin;
import it.csi.siac.siacfinser.integration.entity.SiacRMutuoStatoFin;
import it.csi.siac.siacfinser.integration.entity.SiacRMutuoVoceLiquidazioneFin;
import it.csi.siac.siacfinser.integration.entity.SiacRMutuoVoceMovgestFin;
import it.csi.siac.siacfinser.integration.entity.SiacRSoggettoStatoFin;
import it.csi.siac.siacfinser.integration.entity.SiacTAttoAmmFin;
import it.csi.siac.siacfinser.integration.entity.SiacTEnteProprietarioFin;
import it.csi.siac.siacfinser.integration.entity.SiacTMovgestTsFin;
import it.csi.siac.siacfinser.integration.entity.SiacTMutuoFin;
import it.csi.siac.siacfinser.integration.entity.SiacTMutuoVoceFin;
import it.csi.siac.siacfinser.integration.entity.SiacTMutuoVoceVarFin;
import it.csi.siac.siacfinser.integration.entity.SiacTSoggettoFin;
import it.csi.siac.siacfinser.integration.entity.mapping.FinMapId;
import it.csi.siac.siacfinser.integration.util.DatiOperazioneUtils;
import it.csi.siac.siacfinser.integration.util.Operazione;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.model.mutuo.Mutuo;
import it.csi.siac.siacfinser.model.mutuo.Mutuo.StatoOperativoMutuo;
import it.csi.siac.siacfinser.model.mutuo.VariazioneImportoVoceMutuo;
import it.csi.siac.siacfinser.model.mutuo.VoceMutuo;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaMutuo;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacfinser.model.soggetto.Soggetto.StatoOperativoAnagrafica;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class MutuoDad extends AbstractFinDad {

	@Autowired
	private SiacDMutuoStatoRepository siacDMutuoStatoRepository;

	@Autowired
	private SiacDMutuoTipoRepository siacDMutuoTipoRepository;

	@Autowired
	private SiacDMutuoVarTipoRepository siacDMutuoVarTipoRepository;

	@Autowired
	private SiacDMutuoVoceTipoRepository siacDMutuoVoceTipoRepository;

	@Autowired
	private SiacRMutuoAttoAmmRepository siacRMutuoAttoAmmRepository;

	@Autowired
	private SiacRMutuoSoggettoRepository siacRMutuoSoggettoRepository;

	@Autowired
	private SiacRMutuoStatoRepository siacRMutuoStatoRepository;

	@Autowired
	private SiacRMutuoVoceMovgestRepository siacRMutuoVoceMovgestRepository;

	@Autowired
	private SiacTMutuoVoceRepository siacTMutuoVoceRepository;

	@Autowired
	private SiacTMutuoVoceVarRepository siacTMutuoVoceVarRepository;

	@Autowired
	private SiacTEnteProprietarioFinRepository siacTEnteProprietarioRepository;

	@Autowired
	private SiacDAmbitoRepository siacDAmbitoRepository;

	@Autowired
	private SiacTSoggettoFinRepository siacTSoggettoRepository;

	@Autowired
	private SiacTAttoAmmFinRepository siacTAttoAmmRepository;

	@Autowired
	private SiacTMovgestTsRepository siacTMovgestTsRepository;

	@Autowired
	private SoggettoFinDad soggettoDad;

	/**
	 * Inserire nel sistema un nuovo mutuo in accordo con i dati indicati
	 * @param ente
	 * @param richiedente
	 * @param mutuoInput
	 * @return
	 */
	public Mutuo inserisciMutuo(Ente ente, Richiedente richiedente,
			Mutuo mutuoInput) {

		String loginOperazione = richiedente.getAccount().getNome();

		int idEnte = ente.getUid();
		SiacTEnteProprietarioFin siacTEnteProprietario = siacTEnteProprietarioRepository.findOne(idEnte);

		DatiOperazioneDto datiOperazioneDto = new DatiOperazioneDto(getCurrentMillisecondsTrentaMaggio2017(), Operazione.INSERIMENTO,siacTEnteProprietario, richiedente.getAccount().getId());

		// 1. mutuo + tipo_mutuo (siac_t_mutuo + siac_d_mutuo_tipo)
		SiacTMutuoFin siacTMutuoInsert = new SiacTMutuoFin();

		siacTMutuoInsert.setMutDesc(mutuoInput.getDescrizioneMutuo());
		siacTMutuoInsert.setMutImportoIniziale(mutuoInput.getImportoInizialeMutuo());
		siacTMutuoInsert.setMutImportoAttuale(mutuoInput.getImportoAttualeMutuo());
		siacTMutuoInsert.setMutDurata(mutuoInput.getDurataMutuo());
		siacTMutuoInsert.setMutNote(mutuoInput.getNoteMutuo());
		siacTMutuoInsert.setMutDataInizio(mutuoInput.getDataInizioMutuo());
		siacTMutuoInsert.setMutDataFine(mutuoInput.getDataFineMutuo());
		siacTMutuoInsert.setMutNumRegistrazione(mutuoInput.getNumeroRegistrazioneMutuo());

		SiacDAmbitoFin siacDAmbitoPerCode = siacDAmbitoRepository.findAmbitoByCode(Constanti.AMBITO_FIN, idEnte);
		Integer idAmbito = siacDAmbitoPerCode.getAmbitoId();

		long nuovoCode = getMaxCode(ProgressivoType.MUTUO, idEnte, idAmbito,loginOperazione);
		siacTMutuoInsert.setMutCode(Long.toString(nuovoCode));

		SiacDMutuoTipoFin siacDMutuoTipo = new SiacDMutuoTipoFin();
		siacDMutuoTipo = siacDMutuoTipoRepository.findDMutuoTipoValidoByEnteAndCode(idEnte, mutuoInput.getTipoMutuo().toString(), datiOperazioneDto.getTs());
		siacTMutuoInsert.setSiacDMutuoTipo(siacDMutuoTipo);

		siacTMutuoInsert = DatiOperazioneUtils.impostaDatiOperazioneLogin(siacTMutuoInsert, datiOperazioneDto, siacTAccountRepository);
		siacTMutuoInsert = siacTMutuoRepository.saveAndFlush(siacTMutuoInsert);

		// 2. stato_mutuo (siac_r_mutuo_stato + siac_d_mutuo_stato)
		SiacDMutuoStatoFin siacDMutuoStato = new SiacDMutuoStatoFin();
		siacDMutuoStato = siacDMutuoStatoRepository
				.findDMutuoStatoValidoByEnteAndCode(idEnte, Constanti.statoOperativoMutuoEnumToString(mutuoInput.getStatoOperativoMutuo()), datiOperazioneDto.getTs());

		SiacRMutuoStatoFin siacRMutuoStato = new SiacRMutuoStatoFin();
		siacRMutuoStato = DatiOperazioneUtils.impostaDatiOperazioneLogin(siacRMutuoStato, datiOperazioneDto, siacTAccountRepository);
		siacRMutuoStato.setSiacTMutuo(siacTMutuoInsert);
		siacRMutuoStato.setSiacDMutuoStato(siacDMutuoStato);
		siacRMutuoStatoRepository.saveAndFlush(siacRMutuoStato);

		// 3. soggetto_mutuante (siac_r_mutuo_soggetto + siac_t_soggetto)
		SiacRMutuoSoggettoFin siacRMutuoSoggetto = new SiacRMutuoSoggettoFin();
		siacRMutuoSoggetto = DatiOperazioneUtils.impostaDatiOperazioneLogin(siacRMutuoSoggetto, datiOperazioneDto, siacTAccountRepository);

		SiacTSoggettoFin siacTSoggetto = siacTSoggettoRepository.findSoggettoByCodeAndAmbitoAndEnte(mutuoInput.getSoggettoMutuo().getCodiceSoggetto(),
																								 idEnte, 
																								 datiOperazioneDto.getTs(), 
																								 idAmbito,
																						 		 Constanti.SEDE_SECONDARIA).get(0);

		siacRMutuoSoggetto.setSiacTMutuo(siacTMutuoInsert);
		siacRMutuoSoggetto.setSiacTSoggetto(siacTSoggetto);
		siacRMutuoSoggettoRepository.saveAndFlush(siacRMutuoSoggetto);

		// 4. atto_amministrativo_mutuo (siac_r_atto_amm + siac_t_atto_amm)
		SiacRMutuoAttoAmmFin siacRMutuoAttoAmm = new SiacRMutuoAttoAmmFin();
		siacRMutuoAttoAmm = DatiOperazioneUtils.impostaDatiOperazioneLogin(siacRMutuoAttoAmm, datiOperazioneDto, siacTAccountRepository);

		SiacTAttoAmmFin siacTAttoAmm = getSiacTAttoAmmFromAttoAmministrativo(mutuoInput.getAttoAmministrativoMutuo(), idEnte);
		
		siacRMutuoAttoAmm.setSiacTMutuo(siacTMutuoInsert);
		siacRMutuoAttoAmm.setSiacTAttoAmm(siacTAttoAmm);
		siacRMutuoAttoAmmRepository.saveAndFlush(siacRMutuoAttoAmm);

		// 5. Ritorno mutuo inserito
		Mutuo mutuoReturn = new Mutuo();

		mutuoReturn.setCodiceMutuo(siacTMutuoInsert.getMutCode());
		mutuoReturn = map(siacTMutuoInsert, Mutuo.class,FinMapId.SiacTMutuo_Mutuo);
		// non piu' utilizzato mutuoReturn = EntityMutuoToModelMutuoConverter.siacTMutuoEntityToMutuoModel(siacTMutuoInsert, mutuoReturn);

		//Termino restituendo l'oggetto di ritorno: 
        return mutuoReturn;
	}

	/**
	 * converte ParametroRicercaMutuo (che e' un model) in un oggetto (non
	 * model) da poter passare al DaoImpl
	 */
	private RicercaMutuoParamDto convertForDao(ParametroRicercaMutuo prm) {
		RicercaMutuoParamDto convertito = new RicercaMutuoParamDto();

		AttoAmministrativo attoAmministrativoMutuo = prm.getAttoAmministrativo();
		if (attoAmministrativoMutuo != null) {
			convertito.setAnnoProvvedimento(attoAmministrativoMutuo.getAnno());
			convertito.setNumeroProvvedimento(attoAmministrativoMutuo.getNumero());
			if (attoAmministrativoMutuo.getTipoAtto() != null) {
				convertito.setCodiceTipoProvvedimento(attoAmministrativoMutuo.getTipoAtto().getCodice());
			}
			
			if(attoAmministrativoMutuo.getStrutturaAmmContabile()!=null && attoAmministrativoMutuo.getStrutturaAmmContabile().getUid()>0){
				Integer strutturaAmministrativoContabileDelProvvedimento = attoAmministrativoMutuo.getStrutturaAmmContabile().getUid();
				convertito.setStrutturaAmministrativoContabileDelProvvedimento(strutturaAmministrativoContabileDelProvvedimento);
			}
			
			if(attoAmministrativoMutuo.getUid()>0){
				convertito.setUidProvvedimento(attoAmministrativoMutuo.getUid());
			}
			
		}

		convertito.setNumeroMutuo(prm.getNumeroMutuo());
		convertito.setNumeroRegistrazioneMutuo(prm.getNumeroRegistrazioneMutuo());

		if (prm.getSoggetto() != null) {
			convertito.setCodiceIsitutoMutuante(prm.getSoggetto().getCodiceSoggetto());

		}
		//Termino restituendo l'oggetto di ritorno: 
        return convertito;
	}

	/**
	 * Dato un certo filtro di ricerca esegue la ricerca dei mutui
	 * @param prm
	 * @param idEnte
	 * @param numeroPagina
	 * @param numeroRisultatiPerPagina
	 * @return
	 */
	public List<Mutuo> ricercaMutui(ParametroRicercaMutuo prm, Integer idEnte,
			int numeroPagina, int numeroRisultatiPerPagina) {

		List<SiacTMutuoFin> elencoSiacTMutuo = new ArrayList<SiacTMutuoFin>();
		List<Mutuo> elencoMutui = new ArrayList<Mutuo>();

		RicercaMutuoParamDto param = convertForDao(prm);
		elencoSiacTMutuo = mutuoDao.ricercaMutui(idEnte, param, numeroPagina,numeroRisultatiPerPagina);

		if (null != elencoSiacTMutuo && elencoSiacTMutuo.size() > 0) {
			elencoMutui = convertiLista(elencoSiacTMutuo, Mutuo.class,FinMapId.SiacTMutuo_Mutuo);
			// non piu' utilizzato elencoMutui = EntityMutuoToModelMutuoConverter.siacTMutuoEntityToMutuoModel(elencoSiacTMutuo, elencoMutui);

			for (Mutuo mutuoEstratto : elencoMutui) {
				Soggetto soggettoMutuo = soggettoDad.ricercaSoggetto(Constanti.AMBITO_FIN, idEnte, mutuoEstratto.getCodiceSoggettoMutuo(), true, true);
				if (null != soggettoMutuo){
					mutuoEstratto.setSoggettoMutuo(soggettoMutuo);
				}	
			}
		}
		//Termino restituendo l'oggetto di ritorno: 
        return elencoMutui;
	}

	/**
	 * Dato un filtro di ricerca estrea l'anteprima del numero 
	 * di risultati attesi
	 * @param prm
	 * @param idEnte
	 * @return
	 */
	public Long calcolaNumeroMutuiDaEstrarre(ParametroRicercaMutuo prm,	Integer idEnte) {
		Long conteggioMutui = new Long(0);
		RicercaMutuoParamDto param = convertForDao(prm);
		conteggioMutui = mutuoDao.contaMutui(idEnte, param);
		//Termino restituendo l'oggetto di ritorno: 
        return conteggioMutui;
	}
	

	/**
	 * Filtra tra i capitoli indicati in base alla validita'
	 * @param bilElems
	 * @return
	 */
//	private Integer getCapitoloValido(List<SiacRMovgestBilElemFin> bilElems) {
//		for (SiacRMovgestBilElemFin rMovgestBilElem : bilElems){
//			if (rMovgestBilElem.getDataFineValidita() == null) {
//				return rMovgestBilElem.getSiacTBilElem().getElemId();
//			}
//		}	
//		return null;
//	}

	/**
	 * Effettua i controlli preliminari all'aggiornamento del mutuo indicato
	 * @param ente
	 * @param richiedente
	 * @param mutuo
	 * @return
	 */
	public List<Errore> controlliDiMeritoAggiornamentoMutuo(Ente ente,Richiedente richiedente, Mutuo mutuo) {

		//String loginOperazione = richiedente.getAccount().getNome();
		int idEnte = ente.getUid();
		SiacTEnteProprietarioFin siacTEnteProprietario = siacTEnteProprietarioRepository.findOne(idEnte);

		DatiOperazioneDto datiOperazioneDto = new DatiOperazioneDto(getCurrentMillisecondsTrentaMaggio2017(), Operazione.INSERIMENTO,	siacTEnteProprietario, richiedente.getAccount().getId());

		SiacDAmbitoFin siacDAmbitoPerCode = siacDAmbitoRepository.findAmbitoByCode(Constanti.AMBITO_FIN, idEnte);
		Integer idAmbito = siacDAmbitoPerCode.getAmbitoId();

		List<Errore> listaErrori = new ArrayList<Errore>();
       
		// recupero mutuo
		SiacTMutuoFin siacTMutuo = siacTMutuoRepository.findMutuoValidoByCode(idEnte, mutuo.getCodiceMutuo(), datiOperazioneDto.getTs());

		// set dello stato nella tavola di r
		if (null != siacTMutuo) {
			List<SiacRMutuoStatoFin> listaSiacRMutuoStato = siacTMutuo.getSiacRMutuoStatos();
			String statoOperativoMutuoOld = "";
			if (null != listaSiacRMutuoStato && listaSiacRMutuoStato.size() > 0) {
				for (SiacRMutuoStatoFin siacRMutuoStato : listaSiacRMutuoStato) {
					if (null != siacRMutuoStato	&& siacRMutuoStato.getDataFineValidita() == null) {
						SiacDMutuoStatoFin siacDMutuoStato = siacRMutuoStato.getSiacDMutuoStato();
						statoOperativoMutuoOld = siacDMutuoStato.getMutStatoCode().toString();
					}
				}
			}
 
			// set del legame con il soggetto
			List<SiacRMutuoSoggettoFin> listaSiacRMutuoSoggetto = siacTMutuo.getSiacRMutuoSoggettos();
			String soggettoCodeOld = "";
			if (null != listaSiacRMutuoSoggetto
					&& listaSiacRMutuoSoggetto.size() > 0) {
				for (SiacRMutuoSoggettoFin siacRMutuoSoggetto : listaSiacRMutuoSoggetto) {
					if (siacRMutuoSoggetto.getDataFineValidita() == null) {
						soggettoCodeOld = siacRMutuoSoggetto.getSiacTSoggetto().getSoggettoCode();
					}
				}
			}

			// verifica importi
			if (mutuo.getImportoInizialeMutuo().compareTo(BigDecimal.ZERO) <= 0) {
				listaErrori.add(ErroreFin.IMPORTO_MUTUO_NON_VALIDO.getErrore("Importo Mutuo Non Valido"));
			}

			if (mutuo.getImportoInizialeMutuo().compareTo(siacTMutuo.getMutImportoIniziale()) != 0 && !statoOperativoMutuoOld.equalsIgnoreCase(Constanti.statoOperativoMutuoEnumToString(StatoOperativoMutuo.PROVVISORIO))) {
				listaErrori.add(ErroreFin.IMPORTO_MUTUO_NON_MODIFICABILE.getErrore("Importo Mutuo Non Modificabile"));
			}

			// recupero entita soggetto del mutuo
			if (null != mutuo.getSoggettoMutuo()) {
				List<SiacTSoggettoFin> siacTSoggettos = siacTSoggettoRepository
						.findSoggettoByCodeAndAmbitoAndEnte(mutuo.getSoggettoMutuo().getCodiceSoggetto(),
															idEnte, 
															datiOperazioneDto.getTs(), 
															idAmbito,
															Constanti.SEDE_SECONDARIA);
				
				if (null != siacTSoggettos && siacTSoggettos.size() > 0) {
					SiacTSoggettoFin siacTSoggetto = siacTSoggettos.get(0);
					if (null != siacTSoggetto) {
						if (!soggettoCodeOld.equalsIgnoreCase("") && !soggettoCodeOld.equalsIgnoreCase(mutuo.getSoggettoMutuo().getCodiceSoggetto()) && !statoOperativoMutuoOld
										.equalsIgnoreCase(Constanti.statoOperativoMutuoEnumToString(StatoOperativoMutuo.PROVVISORIO))) {
							listaErrori.add(ErroreFin.SOGGETTO_MUTUO_NON_MODIFICABILE.getErrore("Soggetto Mutuo Non Modificabile"));
						} else {
							List<SiacRSoggettoStatoFin> listaSiacRSoggettoStato = siacTSoggetto.getSiacRSoggettoStatos();
							String statoOperativoSoggetto = "";
							if (null != listaSiacRSoggettoStato	&& listaSiacRSoggettoStato.size() > 0) {
								for (SiacRSoggettoStatoFin siacRSoggettoStato : listaSiacRSoggettoStato) {
									if (null != siacRSoggettoStato && siacRSoggettoStato.getDataFineValidita() == null) {
										SiacDSoggettoStatoFin siacDSoggettoStato = siacRSoggettoStato.getSiacDSoggettoStato();
										statoOperativoSoggetto = siacDSoggettoStato.getSoggettoStatoCode().toString();
									}
								}
							}

							if (statoOperativoSoggetto.equalsIgnoreCase(StatoOperativoAnagrafica.ANNULLATO.name()) || statoOperativoSoggetto
											.equalsIgnoreCase(StatoOperativoAnagrafica.SOSPESO.name())	|| statoOperativoSoggetto.equalsIgnoreCase(StatoOperativoAnagrafica.PROVVISORIO.name())) {
								listaErrori.add(ErroreFin.SOGGETTO_BLOCCATO_PER_MUTUO.getErrore("Soggetto Bloccato Per Mutuo"));
							}
						}
					} else {
						listaErrori.add(ErroreFin.SOGGETTO_NON_VALIDO.getErrore("Soggetto Non Valido"));
					}
				} else {
					listaErrori.add(ErroreFin.SOGGETTO_NON_VALIDO.getErrore("Soggetto Non Valido"));
				}
			}

			// Atto Amministrativo e verifica sullo stato
			if (null != mutuo.getAttoAmministrativoMutuo()) {
				List<SiacRMutuoAttoAmmFin> listaSiacRMutuoAttoAmm = siacTMutuo.getSiacRMutuoAttoAmms();
				if (null != listaSiacRMutuoAttoAmm
						&& listaSiacRMutuoAttoAmm.size() > 0) {
					for (SiacRMutuoAttoAmmFin siacRMutuoAttoAmm : listaSiacRMutuoAttoAmm) {
						if (null != siacRMutuoAttoAmm && siacRMutuoAttoAmm.getDataFineValidita() == null) {
							if (siacRMutuoAttoAmm.getSiacTAttoAmm().getAttoammId() != mutuo.getAttoAmministrativoMutuo().getUid() && !statoOperativoMutuoOld
									.equalsIgnoreCase(Constanti.statoOperativoMutuoEnumToString(StatoOperativoMutuo.PROVVISORIO))) {
								listaErrori.add(ErroreFin.PROVVEDIMENTO_MUTUO_NON_MODIFICABILE.getErrore("Provvedimento Mutuo Non Modificabile"));
							}
						}
					}
				}
			}

			// Voci di Mutuo
			long currMillisec = getCurrentMillisecondsTrentaMaggio2017();
			Date dateControlli = new Date(currMillisec);
			Timestamp timestampControlli = TimingUtils.convertiDataInTimeStamp(dateControlli);
			List<SiacTMutuoVoceFin> listaSiacTMutuoVoce = siacTMutuoVoceRepository.findVociMutuoValideByEnteEMutuoId(ente.getUid(),siacTMutuo.getMutId(), timestampControlli);

			// verifica voci di mutuo gia' presenti e/o cancellare aggiornare
			if (null != listaSiacTMutuoVoce && listaSiacTMutuoVoce.size() > 0) {
				VociDiMutuoInfoDto infoModificheVociDiMutuo = valutaVociDiMutuo(idEnte, mutuo.getListaVociMutuo(),siacTMutuo.getMutId());

				ArrayList<VoceMutuo> listaVociDaAggiornare = infoModificheVociDiMutuo.getListaDaAggiornare();
				ArrayList<VoceMutuo> listaVociDaInserire = infoModificheVociDiMutuo.getListaDaInserire();
				ArrayList<SiacTMutuoVoceFin> listaVociDaEliminare = infoModificheVociDiMutuo.getListaDaEliminare();

				if (listaVociDaEliminare != null && listaVociDaEliminare.size() > 0) {
					for (SiacTMutuoVoceFin siacTMutuoVoce : listaVociDaEliminare) {
						List<SiacRMutuoVoceLiquidazioneFin> listaSiacRMutuoVoceLiquidazione = siacTMutuoVoce.getSiacRMutuoVoceLiquidaziones();
						if (null != listaSiacRMutuoVoceLiquidazione	&& listaSiacRMutuoVoceLiquidazione.size() > 0) {
							for (SiacRMutuoVoceLiquidazioneFin siacRMutuoVoceLiquidazione : listaSiacRMutuoVoceLiquidazione) {
								if (siacRMutuoVoceLiquidazione.getDataFineValidita() == null) {
									listaErrori.add(ErroreFin.CANCELLAZIONE_VOCE_MUTUO_IMPOSSIBLE.getErrore("Cancellazione Voce Mutuo Impossibile"));
								}
							}
						}
					}
				}

				// controlli di disponibilita
				BigDecimal totaleVociMutuo = BigDecimal.ZERO;
				if (listaVociDaInserire != null	&& listaVociDaInserire.size() > 0) {
					if (mutuo.getDisponibileMutuo() != null	&& mutuo.getDisponibileMutuo().compareTo(BigDecimal.ZERO) == 0) {
						
						listaErrori.add(ErroreFin.MUTUO_TOTALMENTE_FINANZIATO.getErrore("Mutuo Totalmente Finanziato"));
						
					} else {
						for (VoceMutuo voceMutuoIns : listaVociDaInserire) {

							if (null != voceMutuoIns.getImportoAttualeVoceMutuo()) {
								totaleVociMutuo = totaleVociMutuo.add(voceMutuoIns.getImportoAttualeVoceMutuo());
							}

							Impegno impegnoSupport = null;
							if (voceMutuoIns.getImpegno().getElencoSubImpegni() != null	&& voceMutuoIns.getImpegno().getElencoSubImpegni().size() > 0
									&& voceMutuoIns.getImpegno().getElencoSubImpegni().get(0) != null) {
								
								impegnoSupport = voceMutuoIns.getImpegno().getElencoSubImpegni().get(0);
							} else {
								impegnoSupport = voceMutuoIns.getImpegno();
							}

							if (voceMutuoIns.getImportoAttualeVoceMutuo().compareTo(BigDecimal.ZERO) <= 0) {
								listaErrori.add(ErroreFin.OPERAZIONE_NON_COMPATIBILE.getErrore("Inserimento Voce Mutuo"," Importo non valido "));
								break;
							} else if (voceMutuoIns.getImportoAttualeVoceMutuo().compareTo(impegnoSupport.getDisponibilitaFinanziare()) > 1) {
								listaErrori.add(ErroreFin.IMPORTO_VOCE_MUTUO_MAGGIORE_DISPONIBILITA_IMPEGNO.getErrore("Importo Voce Mutuo Maggiore Disponibilita' Impegno"));
								break;
							} else if (voceMutuoIns.getImportoAttualeVoceMutuo().compareTo(mutuo.getDisponibileMutuo()) > 1) {
								listaErrori.add(ErroreFin.IMPORTO_VOCE_MUTUO_MAGGIORE_DISPONIBILE_MUTUO.getErrore("Importo Voce Mutuo Maggiore Disponibilita' Mutuo"));
								break;
							}
						}
					}
				}

				// verifica su variazioni
				if (listaVociDaAggiornare != null
						&& listaVociDaAggiornare.size() > 0) {
					for (VoceMutuo voceMutuoUpd : listaVociDaAggiornare) {

						if (null != voceMutuoUpd.getImportoAttualeVoceMutuo()) {
							totaleVociMutuo = totaleVociMutuo.add(voceMutuoUpd.getImportoAttualeVoceMutuo());
						}

						Impegno impegnoSupport = null;
						if (voceMutuoUpd.getImpegno().getElencoSubImpegni() != null
								&& voceMutuoUpd.getImpegno().getElencoSubImpegni().size() > 0
								&& voceMutuoUpd.getImpegno().getElencoSubImpegni().get(0) != null) {
							impegnoSupport = voceMutuoUpd.getImpegno().getElencoSubImpegni().get(0);
						} else {
							impegnoSupport = voceMutuoUpd.getImpegno();
						}

						if (voceMutuoUpd.getImportoAttualeVoceMutuo()
								.compareTo(BigDecimal.ZERO) <= 0) {
							listaErrori
							.add(ErroreFin.OPERAZIONE_NON_COMPATIBILE
									.getErrore("Variazione Voce Mutuo",
											" Importo non valido "));
							break;
						} else if (voceMutuoUpd.getImportoAttualeVoceMutuo().compareTo(impegnoSupport.getDisponibilitaFinanziare()) > 1) {
							listaErrori
							.add(ErroreFin.IMPORTO_VOCE_MUTUO_MAGGIORE_DISPONIBILITA_IMPEGNO
									.getErrore("Importo Voce Mutuo Maggiore Disponibilita' Impegno"));
							break;
						} else if (voceMutuoUpd.getImportoAttualeVoceMutuo()
								.compareTo(mutuo.getDisponibileMutuo()) > 1) {
							listaErrori
							.add(ErroreFin.IMPORTO_VOCE_MUTUO_MAGGIORE_DISPONIBILITA_IMPEGNO
									.getErrore("Importo Voce Mutuo Maggiore Disponibilita' Impegno"));
							break;
						} else if (voceMutuoUpd
								.getImportoAttualeVoceMutuo()
								.compareTo(
										voceMutuoUpd
										.getImportoLiquidatoVoceMutuo() == null ? (new BigDecimal(
												"0")) : voceMutuoUpd
												.getImportoLiquidatoVoceMutuo()) < 1) {
							listaErrori.add(ErroreFin.OPERAZIONE_NON_COMPATIBILE.getErrore("Operazione"," L'importo della voce di mutuo e' inferiore alla somma delle liquidazioni legate all'impegno "));
							break;
						}

						if (null != listaErrori && listaErrori.size() == 0) {
							if (null != voceMutuoUpd.getListaVariazioniImportoVoceMutuo() && voceMutuoUpd.getListaVariazioniImportoVoceMutuo().size() > 0) {
								// Eventuali Variazioni Voci di Mutuo
								VariazioniVociDiMutuoInfoDto infoModificheVariazioniVociDiMutuo = valutaVariazioniVociDiMutuo(datiOperazioneDto,
																															  voceMutuoUpd.getListaVariazioniImportoVoceMutuo(),
																															  voceMutuoUpd.getUid());

								ArrayList<VariazioneImportoVoceMutuo> listaVariazioniVociMutuoDaInserire = infoModificheVariazioniVociDiMutuo.getListaDaInserire();

								if (listaVariazioniVociMutuoDaInserire != null && listaVariazioniVociMutuoDaInserire.size() > 0) {
									for (VariazioneImportoVoceMutuo variazioneImportoVoceMutuoIns : listaVariazioniVociMutuoDaInserire) {
										if (variazioneImportoVoceMutuoIns.getTipoVariazioneImportoVoceMutuo().equals(Constanti.D_MUTUO_VAR_TIPO_ECONOMIA)
												|| variazioneImportoVoceMutuoIns.getTipoVariazioneImportoVoceMutuo().equals(Constanti.D_MUTUO_VAR_TIPO_RIDUZIONE)) {
											//  : non devo testare
											// getDisponibilitaPagare ma
											// IMPORTO_RIUTILIZZO_MODIFICA_DISPONIBILE
											// dell'impegno che per il momento
											// non esiste.
											if (variazioneImportoVoceMutuoIns.getImportoVariazioneVoceMutuo().compareTo(impegnoSupport.getDisponibilitaFinanziare()) > 1) {
												listaErrori.add(ErroreFin.IMPORTO_RIUTILIZZO_ECO_RID_MAGGIORE_MODIFICA_SU_IMPEGNO.getErrore("Importo Riutilizzo Eco/Rid Maggiore Modifica Su Impegno"));
												break;
											}
										}
									}
								}

								
							}
						}
					}

					// Controllo aggiunto in fase di test, da analisi non era
					// previsto ??
					if (totaleVociMutuo.compareTo(mutuo.getImportoAttualeMutuo()) > 0) {
						listaErrori.add(ErroreFin.IMPORTO_VOCE_MUTUO_MAGGIORE_DISPONIBILE_MUTUO.getErrore(""));
					}
				}
			}
		} else {
			listaErrori.add(ErroreFin.MUTUO_INESISTENTE.getErrore("Mutuo Inesistente"));
		}

		//Termino restituendo l'oggetto di ritorno: 
        return listaErrori;
	}


	/**
	 * valuta le variazioni sulle voci di mutuo: quali inserire, aggiornare o eliminare
	 * @param datiOperazioneDto
	 * @param variazioniVociMutuo
	 * @param idVoceMutuo
	 * @return
	 */
	private VariazioniVociDiMutuoInfoDto valutaVariazioniVociDiMutuo(
			DatiOperazioneDto datiOperazioneDto,
			List<VariazioneImportoVoceMutuo> variazioniVociMutuo,
			Integer idVoceMutuo) {

		VariazioniVociDiMutuoInfoDto variazioniVociDiMutuoInfoDto = new VariazioniVociDiMutuoInfoDto();
		long currMillisec = getCurrentMillisecondsTrentaMaggio2017();
		Date dateInserimento = new Date(currMillisec);
		Timestamp timestampInserimento  = TimingUtils.convertiDataInTimeStamp(dateInserimento);

		int idEnte = datiOperazioneDto.getSiacTEnteProprietario().getEnteProprietarioId();

		List<SiacTMutuoVoceVarFin> listaSiacTMutuoVoceVar = siacTMutuoVoceVarRepository
				.findVariazioniVociMutuoValideByEnteEMutuoId(idEnte,
						idVoceMutuo, timestampInserimento);

		if (variazioniVociMutuo == null) {
			variazioniVociMutuo = new ArrayList<VariazioneImportoVoceMutuo>(0);
		}

		ArrayList<VariazioneImportoVoceMutuo> listaDaAggiornare = new ArrayList<VariazioneImportoVoceMutuo>();
		ArrayList<VariazioneImportoVoceMutuo> listaDaInserire = new ArrayList<VariazioneImportoVoceMutuo>();
		ArrayList<SiacTMutuoVoceVarFin> listaDaEliminare = new ArrayList<SiacTMutuoVoceVarFin>();

		ArrayList<Integer> listaIdOld = new ArrayList<Integer>();
		ArrayList<Integer> listaIdNew = new ArrayList<Integer>();

		for (VariazioneImportoVoceMutuo variazione : variazioniVociMutuo) {
			if (variazione.getUid() > 0) {
				listaIdNew.add(variazione.getUid());
			} else {
				listaDaInserire.add(variazione);
			}
		}

		if (listaSiacTMutuoVoceVar != null && listaSiacTMutuoVoceVar.size() > 0) {
			for (SiacTMutuoVoceVarFin siacTMutuoVoceVar : listaSiacTMutuoVoceVar) {

				Integer idOld = siacTMutuoVoceVar.getMutVoceVarId();
				listaIdOld.add(idOld);

				for (VariazioneImportoVoceMutuo variazioneVoceMutuo : variazioniVociMutuo) {
					if (variazioneVoceMutuo.getUid() == idOld) {
						listaDaAggiornare.add(variazioneVoceMutuo);
					}
				}

				if (!listaIdNew.contains(idOld)) {
					listaDaEliminare.add(siacTMutuoVoceVar);
				}
			}
		}

		variazioniVociDiMutuoInfoDto.setListaDaAggiornare(listaDaAggiornare);
		variazioniVociDiMutuoInfoDto.setListaDaEliminare(listaDaEliminare);
		variazioniVociDiMutuoInfoDto.setListaDaInserire(listaDaInserire);

		//Termino restituendo l'oggetto di ritorno: 
        return variazioniVociDiMutuoInfoDto;
	}

	/**
	 * valuta quali voci di mutuo sono da inserire, modificare o eliminare
	 * @param idEnte
	 * @param vociMutuo
	 * @param idMutuo
	 * @return
	 */
	private VociDiMutuoInfoDto valutaVociDiMutuo(Integer idEnte,List<VoceMutuo> vociMutuo, Integer idMutuo) {
		VociDiMutuoInfoDto vociDiMutuoInfoDto = new VociDiMutuoInfoDto();

		long currMillisec = getCurrentMillisecondsTrentaMaggio2017();
		Date dateInserimento = new Date(currMillisec);
		Timestamp timestampInserimento = TimingUtils.convertiDataInTimeStamp(dateInserimento);

		List<SiacTMutuoVoceFin> listaSiacTMutuoVoce = siacTMutuoVoceRepository
				.findVociMutuoValideByEnteEMutuoId(idEnte, idMutuo,
						timestampInserimento);

		if (vociMutuo == null) {
			vociMutuo = new ArrayList<VoceMutuo>(0);
		}

		ArrayList<VoceMutuo> listaDaAggiornare = new ArrayList<VoceMutuo>();
		ArrayList<VoceMutuo> listaDaInserire = new ArrayList<VoceMutuo>();
		ArrayList<SiacTMutuoVoceFin> listaDaEliminare = new ArrayList<SiacTMutuoVoceFin>();

		ArrayList<Integer> listaIdOld = new ArrayList<Integer>();
		ArrayList<Integer> listaIdNew = new ArrayList<Integer>();

		for (VoceMutuo voce : vociMutuo) {
			if (voce.getUid() > 0) {
				listaIdNew.add(voce.getUid());
			} else {
				listaDaInserire.add(voce);
			}
		}

		if (listaSiacTMutuoVoce != null && listaSiacTMutuoVoce.size() > 0) {
			for (SiacTMutuoVoceFin siacTMutuoVoce : listaSiacTMutuoVoce) {

				Integer idOld = siacTMutuoVoce.getMutVoceId();
				listaIdOld.add(idOld);

				for (VoceMutuo voceMutuo : vociMutuo) {
					if (voceMutuo.getUid() == idOld) {
						listaDaAggiornare.add(voceMutuo);
					}
				}

				if (!listaIdNew.contains(idOld)) {
					listaDaEliminare.add(siacTMutuoVoce);
				}
			}
		}

		vociDiMutuoInfoDto.setListaDaAggiornare(listaDaAggiornare);
		vociDiMutuoInfoDto.setListaDaEliminare(listaDaEliminare);
		vociDiMutuoInfoDto.setListaDaInserire(listaDaInserire);

		//Termino restituendo l'oggetto di ritorno: 
        return vociDiMutuoInfoDto;
	}

	/**
	 * si occupa di inserire, aggiornare, eliminare vodi di mutuo
	 * @param vociMutuo
	 * @param mutuo
	 * @param datiOperazioneModificaDto
	 * @param richiedente
	 */
	private void gestisciVociDiMutuo(List<VoceMutuo> vociMutuo,
			SiacTMutuoFin mutuo, DatiOperazioneDto datiOperazioneModificaDto,
			Richiedente richiedente) {

		Integer idEnte = datiOperazioneModificaDto.getSiacTEnteProprietario().getEnteProprietarioId();

		DatiOperazioneDto datiOperazioneInserimentoDto = new DatiOperazioneDto(
				getCurrentMillisecondsTrentaMaggio2017(), Operazione.INSERIMENTO,
				datiOperazioneModificaDto.getSiacTEnteProprietario(),
				richiedente.getAccount().getId());

		DatiOperazioneDto datiOperazioneCancellaDto = new DatiOperazioneDto(
				getCurrentMillisecondsTrentaMaggio2017(),
				Operazione.CANCELLAZIONE_LOGICA_RECORD,
				datiOperazioneModificaDto.getSiacTEnteProprietario(),
				richiedente.getAccount().getId());

		VociDiMutuoInfoDto infoModifiche = valutaVociDiMutuo(idEnte, vociMutuo,
				mutuo.getMutId());

		ArrayList<VoceMutuo> listaDaAggiornare = infoModifiche.getListaDaAggiornare();
		ArrayList<VoceMutuo> listaDaInserire = infoModifiche.getListaDaInserire();
		ArrayList<SiacTMutuoVoceFin> listaDaEliminare = infoModifiche.getListaDaEliminare();

		if (listaDaEliminare != null && listaDaEliminare.size() > 0) {
			for (SiacTMutuoVoceFin siacTMutuoVoce : listaDaEliminare) {
				siacTMutuoVoce = DatiOperazioneUtils.impostaDatiOperazioneLogin(siacTMutuoVoce,datiOperazioneCancellaDto,siacTAccountRepository);
				siacTMutuoVoceRepository.saveAndFlush(siacTMutuoVoce);

				List<SiacRMutuoVoceMovgestFin> listaSiacRMutuoVoceMovgest = siacTMutuoVoce.getSiacRMutuoVoceMovgests();
				if (null != listaSiacRMutuoVoceMovgest
						&& listaSiacRMutuoVoceMovgest.size() > 0) {
					for (SiacRMutuoVoceMovgestFin siacRMutuoVoceMovgest : listaSiacRMutuoVoceMovgest) {
						if (siacRMutuoVoceMovgest.getDataFineValidita() == null) {
							siacRMutuoVoceMovgest = DatiOperazioneUtils.impostaDatiOperazioneLogin(siacRMutuoVoceMovgest,datiOperazioneCancellaDto,siacTAccountRepository);
							siacRMutuoVoceMovgestRepository.saveAndFlush(siacRMutuoVoceMovgest);
						}
					}
				}

				List<SiacTMutuoVoceVarFin> listaSiacTMutuoVoceVar = siacTMutuoVoce.getSiacTMutuoVoceVars();
				if (null != listaSiacTMutuoVoceVar
						&& listaSiacTMutuoVoceVar.size() > 0) {
					for (SiacTMutuoVoceVarFin siacTMutuoVoceVar : listaSiacTMutuoVoceVar) {
						if (siacTMutuoVoceVar.getDataFineValidita() == null) {
							siacTMutuoVoceVar = DatiOperazioneUtils.impostaDatiOperazioneLogin(siacTMutuoVoceVar,datiOperazioneCancellaDto,siacTAccountRepository);
							siacTMutuoVoceVarRepository.saveAndFlush(siacTMutuoVoceVar);
						}
					}
				}
			}
		}

		if (listaDaInserire != null && listaDaInserire.size() > 0) {
			for (VoceMutuo voceMutuo : listaDaInserire) {
				inserisciVoceMutuo(mutuo, voceMutuo,datiOperazioneInserimentoDto, richiedente);
			}
		}

		if (listaDaAggiornare != null && listaDaAggiornare.size() > 0) {
			for (VoceMutuo voceMutuo : listaDaAggiornare) {
				aggiornaVoceMutuo(mutuo, voceMutuo,datiOperazioneInserimentoDto, richiedente);
			}
		}

	}

	/**
	 * si occupa dell'inserimento di variazioni di importo di voci di mutuo
	 * @param siacTMutuo
	 * @param siacTMutuoVoce
	 * @param listaVariazioneImportoVoceMutuo
	 * @param datiOperazioneInserimentoDto
	 * @return
	 */
	public List<SiacTMutuoVoceVarFin> inserisciVariazioniImportoVoceMutuo(
			SiacTMutuoFin siacTMutuo, SiacTMutuoVoceFin siacTMutuoVoce,
			List<VariazioneImportoVoceMutuo> listaVariazioneImportoVoceMutuo,
			DatiOperazioneDto datiOperazioneInserimentoDto) {

		List<SiacTMutuoVoceVarFin> listaSiacTMutuoVoceVarInseriti = new ArrayList<SiacTMutuoVoceVarFin>();

		for (VariazioneImportoVoceMutuo variazioneImportoVoceMutuo : listaVariazioneImportoVoceMutuo) {
			// 1. variazione_importo_voce_mutuo +
			// tipo_variazione_importo_voce_mutuo (siac_t_mutuo_voce_var +
			// siac_d_mutuo_var_tipo)
			SiacTMutuoVoceVarFin siacTMutuoVoceVarInsert = new SiacTMutuoVoceVarFin();

			siacTMutuoVoceVarInsert = inserisciVariazioneImportoVoceMutuo(siacTMutuo, siacTMutuoVoce, variazioneImportoVoceMutuo, datiOperazioneInserimentoDto);

			listaSiacTMutuoVoceVarInseriti.add(siacTMutuoVoceVarInsert);
		}

		//Termino restituendo l'oggetto di ritorno: 
        return listaSiacTMutuoVoceVarInseriti;
	}

	/**
	 * si occupa di aggiornare le variazioni di importo delle voci di mutuo
	 * @param siacTMutuo
	 * @param siacTMutuoVoce
	 * @param variazioneImportoVoceMutuo
	 * @param datiOperazioneInserimentoDto
	 * @param richiedente
	 * @return
	 */
	public SiacTMutuoVoceVarFin aggiornaVariazioneImportoVoceMutuo(SiacTMutuoFin siacTMutuo, SiacTMutuoVoceFin siacTMutuoVoce,
																VariazioneImportoVoceMutuo variazioneImportoVoceMutuo,
																DatiOperazioneDto datiOperazioneInserimentoDto,
																Richiedente richiedente) {

		int idEnte = datiOperazioneInserimentoDto.getSiacTEnteProprietario().getEnteProprietarioId();
		SiacTEnteProprietarioFin siacTEnteProprietario = siacTEnteProprietarioRepository.findOne(idEnte);

		DatiOperazioneDto datiOperazioneModificaDto = new DatiOperazioneDto(
				getCurrentMillisecondsTrentaMaggio2017(), Operazione.MODIFICA,
				siacTEnteProprietario, richiedente.getAccount().getId());

		SiacTMutuoVoceVarFin siacTMutuoVoceVarUpdate = siacTMutuoVoceVarRepository
				.findOne(variazioneImportoVoceMutuo.getUid());

		// 1. variazione_importo_voce_mutuo + tipo_variazione_importo_voce_mutuo
		// (siac_t_mutuo_voce_var + siac_d_mutuo_var_tipo)
		BigDecimal mutVoceVarImportoOld = siacTMutuoVoceVarUpdate.getMutVoceVarImporto() == null ? BigDecimal.ZERO: siacTMutuoVoceVarUpdate.getMutVoceVarImporto();
		BigDecimal mutVoceVarImportoNew = variazioneImportoVoceMutuo.getImportoVariazioneVoceMutuo() == null ? BigDecimal.ZERO: variazioneImportoVoceMutuo.getImportoVariazioneVoceMutuo();
		if (mutVoceVarImportoOld.compareTo(mutVoceVarImportoNew) != 0){
			siacTMutuoVoceVarUpdate.setMutVoceVarImporto(mutVoceVarImportoNew);
		}	

		if (!siacTMutuoVoceVarUpdate.getSiacDMutuoVarTipo().getMutVarTipoCode().equalsIgnoreCase(Constanti.tipoVariazioneImportoVoceMutuoEnumToString(variazioneImportoVoceMutuo.getTipoVariazioneImportoVoceMutuo()))) {
			// E' cambiato il tipo variazione voce mutuo
			SiacDMutuoVarTipoFin siacDMutuoVarTipoNew = new SiacDMutuoVarTipoFin();
			siacDMutuoVarTipoNew = siacDMutuoVarTipoRepository
					.findDMutuoVarTipoValidoByEnteAndCode(idEnte,
														  Constanti.tipoVariazioneImportoVoceMutuoEnumToString(variazioneImportoVoceMutuo.getTipoVariazioneImportoVoceMutuo()),
													  	 datiOperazioneModificaDto.getTs());
			siacTMutuoVoceVarUpdate.setSiacDMutuoVarTipo(siacDMutuoVarTipoNew);
		}

		siacTMutuoVoceVarUpdate.setSiacTMutuoVoce(siacTMutuoVoce);

		siacTMutuoVoceVarUpdate = DatiOperazioneUtils.impostaDatiOperazioneLogin(siacTMutuoVoceVarUpdate,datiOperazioneModificaDto, siacTAccountRepository);
		siacTMutuoVoceVarUpdate = siacTMutuoVoceVarRepository.saveAndFlush(siacTMutuoVoceVarUpdate);

		//Termino restituendo l'oggetto di ritorno: 
		return siacTMutuoVoceVarUpdate;
	}

	/**
	 * Si occupa di inserire le variazioni di voci di mutuo
	 * @param siacTMutuo
	 * @param siacTMutuoVoce
	 * @param variazioneImportoVoceMutuo
	 * @param datiOperazioneInserimentoDto
	 * @return
	 */
	public SiacTMutuoVoceVarFin inserisciVariazioneImportoVoceMutuo(SiacTMutuoFin siacTMutuo, SiacTMutuoVoceFin siacTMutuoVoce, VariazioneImportoVoceMutuo variazioneImportoVoceMutuo,
			DatiOperazioneDto datiOperazioneInserimentoDto) {

		int idEnte = datiOperazioneInserimentoDto.getSiacTEnteProprietario().getEnteProprietarioId();
		//SiacTEnteProprietarioFin siacTEnteProprietario = siacTEnteProprietarioRepository.findOne(idEnte);

		// 1. variazione_importo_voce_mutuo + tipo_variazione_importo_voce_mutuo
		// (siac_t_mutuo_voce_var + siac_d_mutuo_var_tipo)
		SiacTMutuoVoceVarFin siacTMutuoVoceVarInsert = new SiacTMutuoVoceVarFin();

		siacTMutuoVoceVarInsert.setMutVoceVarImporto(variazioneImportoVoceMutuo
				.getImportoVariazioneVoceMutuo());
		siacTMutuoVoceVarInsert.setSiacTMutuoVoce(siacTMutuoVoce);

		SiacDMutuoVarTipoFin siacDMutuoVarTipo = new SiacDMutuoVarTipoFin();
		siacDMutuoVarTipo = siacDMutuoVarTipoRepository
				.findDMutuoVarTipoValidoByEnteAndCode(idEnte,
													  Constanti.tipoVariazioneImportoVoceMutuoEnumToString(variazioneImportoVoceMutuo.getTipoVariazioneImportoVoceMutuo()),
												  	  datiOperazioneInserimentoDto.getTs());
		siacTMutuoVoceVarInsert.setSiacDMutuoVarTipo(siacDMutuoVarTipo);

		siacTMutuoVoceVarInsert = DatiOperazioneUtils.impostaDatiOperazioneLogin(siacTMutuoVoceVarInsert,datiOperazioneInserimentoDto, siacTAccountRepository);
		siacTMutuoVoceVarInsert = siacTMutuoVoceVarRepository.saveAndFlush(siacTMutuoVoceVarInsert);

		//Termino restituendo l'oggetto di ritorno: 
        return siacTMutuoVoceVarInsert;
	}

	/**
	 * Si occupa di inserire le voci di mutuo
	 * @param siacTMutuo
	 * @param voceMutuo
	 * @param datiOperazioneInserimentoDto
	 * @param richiedente
	 * @return
	 */
	public VoceMutuo inserisciVoceMutuo(SiacTMutuoFin siacTMutuo,
			VoceMutuo voceMutuo,
			DatiOperazioneDto datiOperazioneInserimentoDto,
			Richiedente richiedente) {

		int idEnte = datiOperazioneInserimentoDto.getSiacTEnteProprietario().getUid();
		SiacTEnteProprietarioFin siacTEnteProprietario = siacTEnteProprietarioRepository.findOne(idEnte);

		SiacDAmbitoFin siacDAmbitoPerCode = siacDAmbitoRepository.findAmbitoByCode(Constanti.AMBITO_FIN, idEnte);
		Integer idAmbito = siacDAmbitoPerCode.getAmbitoId();

		DatiOperazioneDto datiOperazioneDto = new DatiOperazioneDto(
				getCurrentMillisecondsTrentaMaggio2017(), Operazione.INSERIMENTO,
				siacTEnteProprietario, richiedente.getAccount().getId());

		// 1. voce_mutuo + tipo_voce_mutuo (siac_t_mutuo_voce +
		// siac_d_mutuo_voce_tipo)
		SiacTMutuoVoceFin siacTMutuoVoceInsert = new SiacTMutuoVoceFin();

		long nuovoCode = getMaxCode(ProgressivoType.VOCE_MUTUO, idEnte,
				idAmbito, DatiOperazioneUtils.determinaUtenteLogin(
						datiOperazioneDto, siacTAccountRepository));
		siacTMutuoVoceInsert.setMutVoceCode(Long.toString(nuovoCode));

		siacTMutuoVoceInsert
		.setMutVoceDesc(voceMutuo.getDescrizioneVoceMutuo());
		siacTMutuoVoceInsert.setMutVoceImportoIniziale(voceMutuo
				.getImportoInizialeVoceMutuo());
		siacTMutuoVoceInsert.setMutVoceImportoAttuale(voceMutuo
				.getImportoAttualeVoceMutuo());
		siacTMutuoVoceInsert.setSiacTMutuo(siacTMutuo);

		SiacDMutuoVoceTipoFin siacDMutuoVoceTipo = new SiacDMutuoVoceTipoFin();
		siacDMutuoVoceTipo = siacDMutuoVoceTipoRepository
				.findDMutuoVoceTipoValidoByEnteAndCode(idEnte, Constanti
						.origineVoceMutuoEnumToString(voceMutuo
								.getOrigineVoceMutuo()), datiOperazioneDto
								.getTs());
		siacTMutuoVoceInsert.setSiacDMutuoVoceTipo(siacDMutuoVoceTipo);

		siacTMutuoVoceInsert = DatiOperazioneUtils.impostaDatiOperazioneLogin(
				siacTMutuoVoceInsert, datiOperazioneInserimentoDto,
				siacTAccountRepository);
		siacTMutuoVoceInsert = siacTMutuoVoceRepository
				.saveAndFlush(siacTMutuoVoceInsert);

		// 2. voce_mutuo + impegno o sub-impegno (siac_t_mutuo_voce +
		// siac_r_mutuo_voce_movgest + siac_t_movgest_ts)
		// La presenza o meno del sub-impegno mi permette di capire se la voce
		// di mutuo che sto per inserire e' legato
		// ad un impegno o ad un sub-impegno
		if (null != voceMutuo.getImpegno().getElencoSubImpegni()
				&& voceMutuo.getImpegno().getElencoSubImpegni().size() > 0) {
			SiacRMutuoVoceMovgestFin siacRMutuoVoceMovgest = new SiacRMutuoVoceMovgestFin();
			siacRMutuoVoceMovgest = DatiOperazioneUtils
					.impostaDatiOperazioneLogin(siacRMutuoVoceMovgest,
							datiOperazioneDto, siacTAccountRepository);

			SiacTMovgestTsFin siacTMovgestTs = siacTMovgestTsRepository
					.findMovgestTsByMovgestTsId(idEnte,
							datiOperazioneDto.getTs(), voceMutuo.getImpegno()
							.getElencoSubImpegni().get(0).getUid());

			siacRMutuoVoceMovgest.setSiacTMovgestTs(siacTMovgestTs);
			siacRMutuoVoceMovgest.setSiacTMutuoVoce(siacTMutuoVoceInsert);
			siacRMutuoVoceMovgestRepository.saveAndFlush(siacRMutuoVoceMovgest);
		} else {
			SiacRMutuoVoceMovgestFin siacRMutuoVoceMovgest = new SiacRMutuoVoceMovgestFin();
			siacRMutuoVoceMovgest = DatiOperazioneUtils
					.impostaDatiOperazioneLogin(siacRMutuoVoceMovgest,
							datiOperazioneDto, siacTAccountRepository);

			SiacTMovgestTsFin siacTMovgestTs = siacTMovgestTsRepository
					.findMovgestTsByMovgest(idEnte, datiOperazioneDto.getTs(),
							voceMutuo.getImpegno().getUid()).get(0);

			siacRMutuoVoceMovgest.setSiacTMovgestTs(siacTMovgestTs);
			siacRMutuoVoceMovgest.setSiacTMutuoVoce(siacTMutuoVoceInsert);
			siacRMutuoVoceMovgestRepository.saveAndFlush(siacRMutuoVoceMovgest);
		}

		// 3. Eventuali variazioni voce di mutuo
		if (null != voceMutuo.getListaVariazioniImportoVoceMutuo()
				&& voceMutuo.getListaVariazioniImportoVoceMutuo().size() > 0) {
			List<SiacTMutuoVoceVarFin> listaSiacTMutuoVoceVar = inserisciVariazioniImportoVoceMutuo(
					siacTMutuo, siacTMutuoVoceInsert,
					voceMutuo.getListaVariazioniImportoVoceMutuo(),
					datiOperazioneDto);
			siacTMutuoVoceInsert.setSiacTMutuoVoceVars(listaSiacTMutuoVoceVar);
		}

		// Ritorno la voce di mutuo inserita
		VoceMutuo voceMutuoReturn = new VoceMutuo();
		//Termino restituendo l'oggetto di ritorno: 
        return voceMutuoReturn;
	}

	/**
	 * Si occupa di aggiornare le voci di mutuo
	 * @param mutuo
	 * @param voceMutuoUpdate
	 * @param datiOperazioneInserimentoDto
	 * @param richiedente
	 * @return
	 */
	public VoceMutuo aggiornaVoceMutuo(SiacTMutuoFin mutuo,
			VoceMutuo voceMutuoUpdate,
			DatiOperazioneDto datiOperazioneInserimentoDto,
			Richiedente richiedente) {

		int idEnte = datiOperazioneInserimentoDto.getSiacTEnteProprietario()
				.getEnteProprietarioId();
		SiacTEnteProprietarioFin siacTEnteProprietario = siacTEnteProprietarioRepository
				.findOne(idEnte);

		DatiOperazioneDto datiOperazioneModificaDto = new DatiOperazioneDto(
				getCurrentMillisecondsTrentaMaggio2017(), Operazione.MODIFICA,
				siacTEnteProprietario, richiedente.getAccount().getId());

		SiacTMutuoVoceFin siacTMutuoVoceUpdate = siacTMutuoVoceRepository
				.findOne(voceMutuoUpdate.getUid());

		// 1. voce_mutuo + tipo_voce_mutuo (siac_t_mutuo_voce +
		// siac_d_mutuo_voce_tipo)
		String mutVoceDescOld = siacTMutuoVoceUpdate.getMutVoceDesc() == null ? "" : siacTMutuoVoceUpdate.getMutVoceDesc();
		String mutVoceDescNew = voceMutuoUpdate.getDescrizioneVoceMutuo() == null ? "" : voceMutuoUpdate.getDescrizioneVoceMutuo();
		if (!mutVoceDescOld.equalsIgnoreCase(mutVoceDescNew)){
			siacTMutuoVoceUpdate.setMutVoceDesc(mutVoceDescNew);
		}
		
		BigDecimal mutVoceImportoInizialeOld = siacTMutuoVoceUpdate.getMutVoceImportoIniziale() == null ? BigDecimal.ZERO : siacTMutuoVoceUpdate.getMutVoceImportoIniziale();
		BigDecimal mutVoceImportoInizialeNew = voceMutuoUpdate.getImportoInizialeVoceMutuo() == null ? BigDecimal.ZERO : voceMutuoUpdate.getImportoInizialeVoceMutuo();
		if (mutVoceImportoInizialeOld.compareTo(mutVoceImportoInizialeNew) != 0){
			siacTMutuoVoceUpdate.setMutVoceImportoIniziale(mutVoceImportoInizialeNew);
		}
		BigDecimal mutVoceImportoAttualeOld = siacTMutuoVoceUpdate.getMutVoceImportoAttuale() == null ? BigDecimal.ZERO : siacTMutuoVoceUpdate.getMutVoceImportoAttuale();
		BigDecimal mutVoceImportoAttualeNew = voceMutuoUpdate.getImportoAttualeVoceMutuo() == null ? BigDecimal.ZERO: voceMutuoUpdate.getImportoAttualeVoceMutuo();
		if (mutVoceImportoAttualeOld.compareTo(mutVoceImportoAttualeNew) != 0){
			siacTMutuoVoceUpdate.setMutVoceImportoAttuale(mutVoceImportoAttualeNew);
		}	

		if (!siacTMutuoVoceUpdate.getSiacDMutuoVoceTipo().getMutVoceTipoCode()
				.equalsIgnoreCase(Constanti.origineVoceMutuoEnumToString(voceMutuoUpdate.getOrigineVoceMutuo()))) {
			// E' cambiato il tipo voce mutuo
			SiacDMutuoVoceTipoFin siacDMutuoVoceTipo = new SiacDMutuoVoceTipoFin();
			siacDMutuoVoceTipo = siacDMutuoVoceTipoRepository.findDMutuoVoceTipoValidoByEnteAndCode(idEnte, 
																									Constanti.origineVoceMutuoEnumToString(voceMutuoUpdate.getOrigineVoceMutuo()),
																									datiOperazioneModificaDto.getTs());
			siacTMutuoVoceUpdate.setSiacDMutuoVoceTipo(siacDMutuoVoceTipo);
		}

		siacTMutuoVoceUpdate = DatiOperazioneUtils.impostaDatiOperazioneLogin(siacTMutuoVoceUpdate, datiOperazioneModificaDto,	siacTAccountRepository);
		siacTMutuoVoceUpdate = siacTMutuoVoceRepository	.saveAndFlush(siacTMutuoVoceUpdate);

		// 2. voce_mutuo + impegno o sub-impegno (siac_t_mutuo_voce +
		// siac_r_mutuo_voce_movgest + siac_t_movgest_ts)
		// La presenza o meno del sub-impegno mi permette di capire se la voce
		// di mutuo che sto per aggiornare e' legata
		// ad un impegno o ad un sub-impegno
		Integer idMovgest = new Integer(0);
		boolean isImpegno = true;
		if (null != voceMutuoUpdate.getImpegno().getElencoSubImpegni()
				&& voceMutuoUpdate.getImpegno().getElencoSubImpegni().size() > 0) {
			isImpegno = false;
			idMovgest = voceMutuoUpdate.getImpegno().getElencoSubImpegni()
					.get(0).getUid();
		} else {
			isImpegno = true;
			idMovgest = voceMutuoUpdate.getImpegno().getUid();
		}

		List<SiacRMutuoVoceMovgestFin> listaSiacRMutuoVoceMovgest = siacTMutuoVoceUpdate.getSiacRMutuoVoceMovgests();
		if (null != listaSiacRMutuoVoceMovgest
				&& listaSiacRMutuoVoceMovgest.size() > 0) {
			for (SiacRMutuoVoceMovgestFin siacRMutuoVoceMovgest : listaSiacRMutuoVoceMovgest) {
				if (siacRMutuoVoceMovgest.getDataFineValidita() == null) {
					SiacTMovgestTsFin siacTMovgestTsNew = new SiacTMovgestTsFin();
					if (isImpegno) {
						siacTMovgestTsNew = siacTMovgestTsRepository
								.findMovgestTsByMovgest(idEnte,
										datiOperazioneModificaDto.getTs(),
										idMovgest).get(0);
					} else {
						siacTMovgestTsNew = siacTMovgestTsRepository
								.findMovgestTsByMovgestTsId(idEnte,
										datiOperazioneModificaDto.getTs(),
										idMovgest);
					}

					if (siacRMutuoVoceMovgest.getSiacTMovgestTs().getUid().compareTo(siacTMovgestTsNew.getUid()) != 0) {
						// E' stato modificato l'impegno / sub-impegno collegato
						// alla voce di mutuo
						siacRMutuoVoceMovgest = DatiOperazioneUtils
								.impostaDatiOperazioneLogin(siacRMutuoVoceMovgest,datiOperazioneModificaDto,siacTAccountRepository);
						siacRMutuoVoceMovgest.setSiacTMovgestTs(siacTMovgestTsNew);
						siacRMutuoVoceMovgestRepository.saveAndFlush(siacRMutuoVoceMovgest);
					}
				}
			}
		}

		// 3. Eventuali variazioni voce di mutuo
		gestisciVariazioniVociDiMutuo(mutuo, siacTMutuoVoceUpdate,voceMutuoUpdate,voceMutuoUpdate.getListaVariazioniImportoVoceMutuo(),datiOperazioneModificaDto, richiedente);

		// ritorna vuoto
		VoceMutuo voceMutuoReturn = new VoceMutuo();
		//Termino restituendo l'oggetto di ritorno: 
        return voceMutuoReturn;
	}

	/**
	 * Si occupa di gestire (inserire, aggiornare) variazioni di voci di mutuo
	 * @param siacTMutuo
	 * @param siacTMutuoVoce
	 * @param voceMutuo
	 * @param listaVariazioneImportoVoceMutuo
	 * @param datiOperazioneModificaDto
	 * @param richiedente
	 */
	private void gestisciVariazioniVociDiMutuo(SiacTMutuoFin siacTMutuo,
			SiacTMutuoVoceFin siacTMutuoVoce, VoceMutuo voceMutuo,
			List<VariazioneImportoVoceMutuo> listaVariazioneImportoVoceMutuo,
			DatiOperazioneDto datiOperazioneModificaDto, Richiedente richiedente) {

		DatiOperazioneDto datiOperazioneInserimentoDto = new DatiOperazioneDto(
				getCurrentMillisecondsTrentaMaggio2017(), Operazione.INSERIMENTO,
				datiOperazioneModificaDto.getSiacTEnteProprietario(),
				richiedente.getAccount().getId());
		DatiOperazioneDto datiOperazioneCancellaDto = new DatiOperazioneDto(
				getCurrentMillisecondsTrentaMaggio2017(),
				Operazione.CANCELLAZIONE_LOGICA_RECORD,
				datiOperazioneModificaDto.getSiacTEnteProprietario(),
				richiedente.getAccount().getId());

		VariazioniVociDiMutuoInfoDto infoModifiche = valutaVariazioniVociDiMutuo(
				datiOperazioneModificaDto, listaVariazioneImportoVoceMutuo,
				siacTMutuoVoce.getMutVoceId());

		ArrayList<VariazioneImportoVoceMutuo> listaDaAggiornare = infoModifiche.getListaDaAggiornare();
		ArrayList<VariazioneImportoVoceMutuo> listaDaInserire = infoModifiche.getListaDaInserire();
		ArrayList<SiacTMutuoVoceVarFin> listaDaEliminare = infoModifiche.getListaDaEliminare();

		if (listaDaEliminare != null && listaDaEliminare.size() > 0) {
			for (SiacTMutuoVoceVarFin siacTMutuoVoceVar : listaDaEliminare) {
				siacTMutuoVoceVar = DatiOperazioneUtils.impostaDatiOperazioneLogin(siacTMutuoVoceVar,datiOperazioneCancellaDto,siacTAccountRepository);
				siacTMutuoVoceVarRepository.saveAndFlush(siacTMutuoVoceVar);
			}
		}

		if (listaDaInserire != null && listaDaInserire.size() > 0) {
			for (VariazioneImportoVoceMutuo variazioneImportoVoceMutuo : listaDaInserire) {
				inserisciVariazioneImportoVoceMutuo(siacTMutuo, siacTMutuoVoce,variazioneImportoVoceMutuo,datiOperazioneInserimentoDto);
			}
		}

		if (listaDaAggiornare != null && listaDaAggiornare.size() > 0) {
			for (VariazioneImportoVoceMutuo variazioneImportoVoceMutuo : listaDaAggiornare) {
				aggiornaVariazioneImportoVoceMutuo(siacTMutuo, siacTMutuoVoce,variazioneImportoVoceMutuo,datiOperazioneInserimentoDto, richiedente);
			}
		}

	}

	/**
	 * si occupa di aggiornare il mutuo indicato
	 * @param ente
	 * @param richiedente
	 * @param mutuo
	 * @return
	 */
	public Mutuo aggiornaMutuo(Ente ente, Richiedente richiedente, Mutuo mutuo) {

		int idEnte = ente.getUid();
		SiacTEnteProprietarioFin siacTEnteProprietario = siacTEnteProprietarioRepository.findOne(idEnte);

		DatiOperazioneDto datiOperazioneModificaDto = new DatiOperazioneDto(
				getCurrentMillisecondsTrentaMaggio2017(), Operazione.MODIFICA,
				siacTEnteProprietario, richiedente.getAccount().getId());

		SiacDAmbitoFin siacDAmbitoPerCode = siacDAmbitoRepository.findAmbitoByCode(Constanti.AMBITO_FIN, idEnte);
		Integer idAmbito = siacDAmbitoPerCode.getAmbitoId();

		SiacTMutuoFin siacTMutuoUpdate = siacTMutuoRepository.findMutuoValidoByCode(idEnte, mutuo.getCodiceMutuo(),datiOperazioneModificaDto.getTs());

		// 1. mutuo + tipo_mutuo (siac_t_mutuo + siac_d_mutuo_tipo)
		String mutDescOld = siacTMutuoUpdate.getMutDesc() == null ? "" : siacTMutuoUpdate.getMutDesc();
		String mutDescNew = mutuo.getDescrizioneMutuo() == null ? "" : mutuo.getDescrizioneMutuo();
		if (!mutDescOld.equalsIgnoreCase(mutDescNew))
			siacTMutuoUpdate.setMutDesc(mutDescNew);

		BigDecimal mutImportoInizialeOld = siacTMutuoUpdate.getMutImportoIniziale() == null ? BigDecimal.ZERO : siacTMutuoUpdate.getMutImportoIniziale();
		BigDecimal mutImportoInizialeNew = mutuo.getImportoInizialeMutuo() == null ? BigDecimal.ZERO : mutuo.getImportoInizialeMutuo();
			if (mutImportoInizialeOld.compareTo(mutImportoInizialeNew) != 0)
				siacTMutuoUpdate.setMutImportoIniziale(mutImportoInizialeNew);

		BigDecimal mutImportoAttualeOld = siacTMutuoUpdate.getMutImportoAttuale() == null ? BigDecimal.ZERO : siacTMutuoUpdate.getMutImportoAttuale();
		BigDecimal mutImportoAttualeNew = mutuo.getImportoAttualeMutuo() == null ? BigDecimal.ZERO : mutuo.getImportoAttualeMutuo();
		if (mutImportoAttualeOld.compareTo(mutImportoAttualeNew) != 0)
			siacTMutuoUpdate.setMutImportoAttuale(mutImportoAttualeNew);

		Integer mutDurataOld = siacTMutuoUpdate.getMutDurata() == null ? new Integer(0) : siacTMutuoUpdate.getMutDurata();
		Integer mutDurataNew = mutuo.getDurataMutuo() == null ? new Integer(0): mutuo.getDurataMutuo();
		if (mutDurataOld.compareTo(mutDurataNew) != 0){ 
				siacTMutuoUpdate.setMutDurata(mutDurataNew);
		}
		String mutNoteOld = siacTMutuoUpdate.getMutNote() == null ? "" : siacTMutuoUpdate.getMutNote();
		String mutNoteNew = mutuo.getNoteMutuo() == null ? "" : mutuo.getNoteMutuo();
		if (!mutNoteOld.equalsIgnoreCase(mutNoteNew)){
				siacTMutuoUpdate.setMutNote(mutNoteNew);
		}
		Date mutDataInizioOld = siacTMutuoUpdate.getMutDataInizio() == null ? new Date() : siacTMutuoUpdate.getMutDataInizio();
		Date mutDataInizioNew = mutuo.getDataInizioMutuo() == null ? new Date() : mutuo.getDataInizioMutuo();
		if (mutDataInizioOld.compareTo(mutDataInizioNew) != 0){
				siacTMutuoUpdate.setMutDataInizio(mutDataInizioNew);
		}
		Date mutDataFineOld = siacTMutuoUpdate.getMutDataFine() == null ? new Date() : siacTMutuoUpdate.getMutDataFine();
		Date mutDataFineNew = mutuo.getDataFineMutuo() == null ? new Date()	: mutuo.getDataFineMutuo();
		if (mutDataFineOld.compareTo(mutDataFineNew) != 0){
				siacTMutuoUpdate.setMutDataFine(mutDataFineNew);
		}
		
		String mutNumRegistrazioneOld = siacTMutuoUpdate.getMutNumRegistrazione();
		String mutNumRegistrazioneNew = mutuo.getNumeroRegistrazioneMutuo();
		
		if(!StringUtils.sonoUgualiTrimmed(mutNumRegistrazioneOld, mutNumRegistrazioneNew)){
			siacTMutuoUpdate.setMutNumRegistrazione(mutuo.getNumeroRegistrazioneMutuo());
		}
		if (!siacTMutuoUpdate.getSiacDMutuoTipo().getMutTipoCode().equalsIgnoreCase(mutuo.getTipoMutuo().toString())) {
			// E' cambiato il tipo mutuo
			SiacDMutuoTipoFin siacDMutuoTipo = new SiacDMutuoTipoFin();
			siacDMutuoTipo = siacDMutuoTipoRepository.findDMutuoTipoValidoByEnteAndCode(idEnte, mutuo.getTipoMutuo().toString(),datiOperazioneModificaDto.getTs());
			siacTMutuoUpdate.setSiacDMutuoTipo(siacDMutuoTipo);
		}

		siacTMutuoUpdate = DatiOperazioneUtils.impostaDatiOperazioneLogin(siacTMutuoUpdate, datiOperazioneModificaDto,	siacTAccountRepository);
		siacTMutuoUpdate = siacTMutuoRepository.saveAndFlush(siacTMutuoUpdate);

		// 2. stato_mutuo (siac_r_mutuo_stato + siac_d_mutuo_stato)
		List<SiacRMutuoStatoFin> listaSiacRMutuoStato = siacTMutuoUpdate.getSiacRMutuoStatos();
		if (null != listaSiacRMutuoStato && listaSiacRMutuoStato.size() > 0) {
			for (SiacRMutuoStatoFin siacRMutuoStato : listaSiacRMutuoStato) {
				if (siacRMutuoStato.getDataFineValidita() == null) {
					if (!siacRMutuoStato.getSiacDMutuoStato().getMutStatoCode().equalsIgnoreCase(mutuo.getStatoOperativoMutuo().toString())) {
						SiacDMutuoStatoFin siacDMutuoStatoNew = new SiacDMutuoStatoFin();
						siacDMutuoStatoNew = siacDMutuoStatoRepository.findDMutuoStatoValidoByEnteAndCode(idEnte,Constanti.statoOperativoMutuoEnumToString(mutuo.getStatoOperativoMutuo()),datiOperazioneModificaDto.getTs());

						siacRMutuoStato = DatiOperazioneUtils.impostaDatiOperazioneLogin(siacRMutuoStato,datiOperazioneModificaDto,siacTAccountRepository);
						siacRMutuoStato.setSiacDMutuoStato(siacDMutuoStatoNew);
						siacRMutuoStatoRepository.saveAndFlush(siacRMutuoStato);
					}
				}
			}
		}

		// 3. soggetto_mutuante (siac_r_mutuo_soggetto + siac_t_soggetto)
		List<SiacRMutuoSoggettoFin> listaSiacRMutuoSoggetto = siacTMutuoUpdate
				.getSiacRMutuoSoggettos();
		if (null != listaSiacRMutuoSoggetto
				&& listaSiacRMutuoSoggetto.size() > 0) {
			for (SiacRMutuoSoggettoFin siacRMutuoSoggetto : listaSiacRMutuoSoggetto) {
				if (siacRMutuoSoggetto.getDataFineValidita() == null) {
					if (!siacRMutuoSoggetto
							.getSiacTSoggetto()
							.getSoggettoCode()
							.equalsIgnoreCase(
									mutuo.getSoggettoMutuo()
									.getCodiceSoggetto())) {
						SiacTSoggettoFin siacTSoggettoNew = siacTSoggettoRepository
								.findSoggettoByCodeAndAmbitoAndEnte(
										mutuo.getSoggettoMutuo()
										.getCodiceSoggetto(), idEnte,
										datiOperazioneModificaDto.getTs(),
										idAmbito, Constanti.SEDE_SECONDARIA)
										.get(0);

						siacRMutuoSoggetto = DatiOperazioneUtils
								.impostaDatiOperazioneLogin(siacRMutuoSoggetto,
										datiOperazioneModificaDto,
										siacTAccountRepository);
						siacRMutuoSoggetto.setSiacTSoggetto(siacTSoggettoNew);
						siacRMutuoSoggettoRepository
						.saveAndFlush(siacRMutuoSoggetto);
					}
				}
			}
		}

		// 4. atto_amministrativo_mutuo (siac_r_atto_amm + siac_t_atto_amm)
		List<SiacRMutuoAttoAmmFin> listaSiacRMutuoAttoAmm = siacTMutuoUpdate
				.getSiacRMutuoAttoAmms();
		if (null != listaSiacRMutuoAttoAmm && listaSiacRMutuoAttoAmm.size() > 0) {
			for (SiacRMutuoAttoAmmFin siacRMutuoAttoAmm : listaSiacRMutuoAttoAmm) {
				if (siacRMutuoAttoAmm.getDataFineValidita() == null) {
					if (siacRMutuoAttoAmm
							.getSiacTAttoAmm()
							.getAttoammId()
							.compareTo(
									mutuo.getAttoAmministrativoMutuo().getUid()) != 0) {
						SiacTAttoAmmFin siacTAttoAmmNew = siacTAttoAmmRepository
								.findOne(mutuo.getAttoAmministrativoMutuo()
										.getUid());

						siacRMutuoAttoAmm = DatiOperazioneUtils
								.impostaDatiOperazioneLogin(siacRMutuoAttoAmm,
										datiOperazioneModificaDto,
										siacTAccountRepository);
						siacRMutuoAttoAmm.setSiacTAttoAmm(siacTAttoAmmNew);
						siacRMutuoAttoAmmRepository
						.saveAndFlush(siacRMutuoAttoAmm);
					}
				}
			}
		}

		// 5. Voci di Mutuo
		gestisciVociDiMutuo(mutuo.getListaVociMutuo(), siacTMutuoUpdate,
				datiOperazioneModificaDto, richiedente);

		// 6. Ritorno mutuo inserito
		Mutuo mutuoReturn = new Mutuo();

		mutuoReturn = map(siacTMutuoUpdate, Mutuo.class, FinMapId.SiacTMutuo_Mutuo);
		// non piu' utilizzato mutuoReturn = EntityMutuoToModelMutuoConverter.siacTMutuoEntityToMutuoModel(siacTMutuoUpdate, mutuoReturn);

		//Termino restituendo l'oggetto di ritorno: 
        return mutuoReturn;
	}

	/**
	 * verifica la presenza di vodi per il mutuo indicato
	 * @param codeMutuo
	 * @param datiOperazioneDto
	 * @return
	 */
	public boolean presenzaVociMutuo(String codeMutuo,DatiOperazioneDto datiOperazioneDto) {
		boolean risultato = false;

		SiacTMutuoFin siacTMutuo = siacTMutuoRepository.findMutuoValidoByCode(
				datiOperazioneDto.getSiacTEnteProprietario()
				.getEnteProprietarioId(), codeMutuo, datiOperazioneDto
				.getTs());

		if (null != siacTMutuo) {
			if (siacTMutuo.getSiacTMutuoVoces() != null
					&& siacTMutuo.getSiacTMutuoVoces().size() > 0) {
				for (SiacTMutuoVoceFin siacTMutuoVoce : siacTMutuo
						.getSiacTMutuoVoces()) {
					if (siacTMutuoVoce.getDataFineValidita() == null) {
						risultato = true;
						break;
					}
				}
			}
		} else {
			risultato = true;
		}

		//Termino restituendo l'oggetto di ritorno: 
        return risultato;
	}

	/**
	 * si occupa di annullare il mutuo indicato
	 * @param codeMutuo
	 * @param datiOperazioneDto
	 * @param richiedente
	 */
	public void annullaMutuo(String codeMutuo,DatiOperazioneDto datiOperazioneDto, Richiedente richiedente) {
        // carica dati mutuo
		SiacTMutuoFin siacMutuo = siacTMutuoRepository.findMutuoValidoByCode(datiOperazioneDto.getSiacTEnteProprietario().getEnteProprietarioId(), codeMutuo, datiOperazioneDto.getTs());

		if (null != siacMutuo) {
			
			// 2. stato_mutuo (siac_r_mutuo_stato + siac_d_mutuo_stato)
			SiacDMutuoStatoFin siacDMutuoStato = new SiacDMutuoStatoFin();
			siacDMutuoStato = siacDMutuoStatoRepository.findDMutuoStatoValidoByEnteAndCode(datiOperazioneDto.getSiacTEnteProprietario().getEnteProprietarioId(),
																						   Constanti.D_MUTUO_STATO_ANNULLATO.toString(),
																			   			   datiOperazioneDto.getTs());

			List<SiacRMutuoStatoFin> listaRelMutuo = siacMutuo.getSiacRMutuoStatos();
			if (null != listaRelMutuo && listaRelMutuo.size() > 0) {

				for (SiacRMutuoStatoFin itStati : listaRelMutuo) {
					// annullo lo stato per la relazione
					if (itStati.getDataFineValidita() == null) {
						itStati = DatiOperazioneUtils.impostaDatiOperazioneLogin(itStati,datiOperazioneDto,	siacTAccountRepository);
						siacRMutuoStatoRepository.saveAndFlush(itStati);

						// inserisco la riga con lo stato annullato
						SiacRMutuoStatoFin siacRMutuoStato = new SiacRMutuoStatoFin();
						// DatiOperazioneDto datiOperazioneInserimento = new
						// DatiOperazioneDto(getCurrentMillisecondsTrentaMaggio2017(),
						// datiOperazioneDto.getUtenteOperazione(),
						// Operazione.INSERIMENTO,
						// datiOperazioneDto.getSiacTEnteProprietario());
						DatiOperazioneDto datiOperazioneInserimento = new DatiOperazioneDto(getCurrentMillisecondsTrentaMaggio2017(),	Operazione.INSERIMENTO,datiOperazioneDto.getSiacTEnteProprietario(),richiedente.getAccount().getId());

						siacRMutuoStato = DatiOperazioneUtils.impostaDatiOperazioneLogin(siacRMutuoStato,datiOperazioneInserimento,	siacTAccountRepository);
						siacRMutuoStato.setSiacTMutuo(siacMutuo);
						siacRMutuoStato.setSiacDMutuoStato(siacDMutuoStato);
						siacRMutuoStatoRepository.saveAndFlush(siacRMutuoStato);

						// inserisco data fine su t_mutuo
						siacMutuo = DatiOperazioneUtils.impostaDatiOperazioneLogin(siacMutuo,datiOperazioneDto,siacTAccountRepository);
						siacTMutuoRepository.saveAndFlush(siacMutuo);
					}
				}
			}
		}
	}
}