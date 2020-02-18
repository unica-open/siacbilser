/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.business.service.movgest;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.SerializationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinser.Constanti;
import it.csi.siac.siacfinser.StringUtils;
import it.csi.siac.siacfinser.business.service.AbstractBaseService;
import it.csi.siac.siacfinser.frontend.webservice.msg.InserisciModificaImportoMovimentoGestioneEntrata;
import it.csi.siac.siacfinser.frontend.webservice.msg.InserisciModificaImportoMovimentoGestioneEntrataResponse;
import it.csi.siac.siacfinser.integration.dad.CommonDad;
import it.csi.siac.siacfinser.integration.dao.common.dto.DatiOperazioneDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.EsitoControlliDto;
import it.csi.siac.siacfinser.integration.util.Operazione;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.SubAccertamento;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneEntrata;
import it.csi.siac.siacgenser.model.TipoCollegamento;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class InserisciModificaImportoMovimentoGestioneEntrataService extends AbstractBaseService<InserisciModificaImportoMovimentoGestioneEntrata, InserisciModificaImportoMovimentoGestioneEntrataResponse>{

	@Autowired
	CommonDad commonDad;
	

	@Override
	protected void init() {
		final String methodName = "InserisciModificaImportoMovimentoGestioneEntrataService : init()";
		log.debug(methodName, "- Begin");		

	}	


	@Override
	@Transactional
	public InserisciModificaImportoMovimentoGestioneEntrataResponse executeService(InserisciModificaImportoMovimentoGestioneEntrata serviceRequest) {

		return super.executeService(serviceRequest);
	}

	@Override
	public void execute() {
		final String methodName = "InserisciModificaImportoMovimentoGestioneEntrataService : execute()";
		log.debug(methodName, "- Begin");
		
			
		Ente ente = req.getRichiedente().getAccount().getEnte();
		Richiedente richiedente = req.getRichiedente();
		Bilancio bilancio = req.getBilancio();
		
		Integer annoBilancioRequest = req.getBilancio().getAnno();
		
		ModificaMovimentoGestioneEntrata modificaMovimentoGestioneEntrata = req.getModificaMovimentoGestioneEntrata();
		
		Accertamento accertamento = modificaMovimentoGestioneEntrata.getAccertamento();
		
		Boolean isModificaDiImportoSub =  modificaMovimentoGestioneEntrata.getUidSubAccertamento()!=null && modificaMovimentoGestioneEntrata.getUidSubAccertamento()!= 0;
			
		// 1. controllo la validita dell'atto amministrativo
		checkListaErrori(controlloAttoAmministrativo(richiedente, modificaMovimentoGestioneEntrata.getAttoAmministrativo()));
		
		
		
		// 2. controllo le varie disponibilita' dell'accertamento/capitolo
		DatiOperazioneDto datiOperazione = commonDad.inizializzaDatiOperazione(ente, richiedente, Operazione.INSERIMENTO, annoBilancioRequest);
		EsitoControlliDto esitoControlli = accertamentoOttimizzatoDad.verificaStatoMovimentoEImportiDisponibilita(richiedente, ente, modificaMovimentoGestioneEntrata, isModificaDiImportoSub, req.getModificaMovimentoGestioneEntrata().getAccertamento().getCapitoloEntrataGestione(),datiOperazione);
		addWarningToResp(esitoControlli);
		checkListaErrori(esitoControlli.getListaErrori());

		ModificaMovimentoGestioneEntrata modificaInserita = accertamentoOttimizzatoDad.inserisciModificaDiImportoMovimentoGestioneEntrata(richiedente, ente, modificaMovimentoGestioneEntrata, bilancio, isModificaDiImportoSub);
		
		if(!isModificaDiImportoSub){
			
			List<ModificaMovimentoGestioneEntrata> listaModificheMovimentoGestioneEntrataAccAll = new ArrayList<ModificaMovimentoGestioneEntrata>();
			
			if(accertamento.getListaModificheMovimentoGestioneEntrata()!=null && !accertamento.getListaModificheMovimentoGestioneEntrata().isEmpty()){
				
				listaModificheMovimentoGestioneEntrataAccAll.addAll(accertamento.getListaModificheMovimentoGestioneEntrata());
				
			}
			
			listaModificheMovimentoGestioneEntrataAccAll.add(SerializationUtils.clone(modificaInserita));
			accertamento.setListaModificheMovimentoGestioneEntrata(listaModificheMovimentoGestioneEntrataAccAll);
			
		}else{
				
			if(accertamento.getElencoSubAccertamenti()!=null && !accertamento.getElencoSubAccertamenti().isEmpty()){
				
				for (SubAccertamento sub : accertamento.getElencoSubAccertamenti()) {
						
					if(sub.getUid() == modificaMovimentoGestioneEntrata.getUidSubAccertamento()){
						
						List<ModificaMovimentoGestioneEntrata> listaModificheMovimentoGestioneEntrataSubAccAll = new ArrayList<ModificaMovimentoGestioneEntrata>();
						
						if(sub.getListaModificheMovimentoGestioneEntrata()!=null && !sub.getListaModificheMovimentoGestioneEntrata().isEmpty()){
							listaModificheMovimentoGestioneEntrataSubAccAll.addAll(sub.getListaModificheMovimentoGestioneEntrata());
						}
						
						listaModificheMovimentoGestioneEntrataSubAccAll.add(SerializationUtils.clone(modificaInserita));
						sub.setListaModificheMovimentoGestioneEntrata(listaModificheMovimentoGestioneEntrataSubAccAll);
												
						break;
					}
				}
			}
			
		}
			
			
		// devo ricalcolare la dispon. a incassare dell'accertamento
		TransactionAspectSupport.currentTransactionStatus().flush();
		
		Accertamento fromUpdate = aggiornoDisponibilitaIncassare(ente, richiedente, bilancio, accertamento);

		
		// Prima di restituire la response scatta la registrazione fin -> gen , se non esiste una prima nota annullata inserisco la registrazione 
		// jira 3719 se l'anno di bilancio è uguale all'anno del movimento richiamo tutta la logica di registrazione sul movimento o sui suoi sub/modifiche
		boolean registraPerAnnoBilancioCorrente = annoBilancioRequest == fromUpdate.getAnnoMovimento();
		//boolean flagFatturaNonModificato = accertamento.isFlagFattura() == fromUpdate.isFlagFattura();		
		
		// jira 4351 (CR) in fase di bilancio = predisposizione consuntivo devo controllare se l'impegno � competenza o residuo e se l'importo della modifica � meno o piu
		// cambiano poi anche le causali di scrittura prima nota
		boolean residuo = annoBilancioRequest > fromUpdate.getAnnoMovimento();
		boolean registraPerPredisposizioneConsuntivo = false;
		
		
		//inizializzo di nuovo per refreshare il timestamp:
		datiOperazione = commonDad.inizializzaDatiOperazione(ente, richiedente, Operazione.INSERIMENTO, annoBilancioRequest);
				
		String codiceFaseBilancio= accertamentoOttimizzatoDad.caricaCodiceBilancio(datiOperazione, bilancio.getAnno());
		
		if (!StringUtils.isEmpty(codiceFaseBilancio) && Constanti.BIL_FASE_OPERATIVA_PREDISPOSIZIONE_CONSUNTIVO.equals(codiceFaseBilancio)) {
			registraPerPredisposizioneConsuntivo = true;
		}

		if(registraPerAnnoBilancioCorrente || registraPerPredisposizioneConsuntivo){
			
			if(modificaInserita!=null && !isModificaDiImportoSub){
				gestisciRegistrazioneGENModificheMovimentoEntrata(fromUpdate, null, TipoCollegamento.MODIFICA_MOVIMENTO_GESTIONE_ENTRATA, registraPerAnnoBilancioCorrente,true, registraPerPredisposizioneConsuntivo, residuo, false, annoBilancioRequest);
			}
			
			if(modificaInserita!=null && isModificaDiImportoSub){
				for (SubAccertamento sub : fromUpdate.getElencoSubAccertamenti()) {
					gestisciRegistrazioneGENModificheMovimentoEntrata(fromUpdate, sub, TipoCollegamento.MODIFICA_MOVIMENTO_GESTIONE_ENTRATA, registraPerAnnoBilancioCorrente, true, registraPerPredisposizioneConsuntivo, residuo, false, annoBilancioRequest);
				}
				
			}
		
			
		}
			
		res.setModificaMovimentoGestioneEntrata(modificaInserita);
		res.setEsito(Esito.SUCCESSO);
	}


	@SuppressWarnings("rawtypes")
	private Accertamento aggiornoDisponibilitaIncassare(Ente ente, Richiedente richiedente, Bilancio bilancio,
			Accertamento accertamento) {
		Accertamento fromUpdate = accertamento;
		//Completiamo i dati relativi agli atti amministrativi, "completaDatiRicercaAccertamentoPk" coordina opportunamente i provvedimentoService
		fromUpdate = completaDatiRicercaAccertamentoPk(richiedente, fromUpdate);
		//Completo i dati delle disponibilita:
		Accertamento accertamentoDisp = completaDisponibilitaAccertamento(richiedente, ente, fromUpdate, bilancio);
		
		fromUpdate.setFlagFattura(accertamentoDisp.isFlagFattura());
		fromUpdate.setFlagCorrispettivo(accertamentoDisp.isFlagCorrispettivo());
		
		// setto i dati delle disponibilita per l'accertamento
		fromUpdate.setDisponibilitaUtilizzare(accertamentoDisp.getDisponibilitaUtilizzare());
		fromUpdate.setMotivazioneDisponibilitaUtilizzare(accertamentoDisp.getMotivazioneDisponibilitaUtilizzare());
		fromUpdate.setDisponibilitaIncassare(accertamentoDisp.getDisponibilitaIncassare());
		fromUpdate.setMotivazioneDisponibilitaIncassare(accertamentoDisp.getMotivazioneDisponibilitaIncassare());
		fromUpdate.setDisponibilitaSubAccertare(accertamentoDisp.getDisponibilitaSubAccertare());
		fromUpdate.setMotivazioneDisponibilitaSubAccertare(accertamento.getMotivazioneDisponibilitaSubAccertare());
		fromUpdate.setTotaleSubAccertamenti(accertamentoDisp.getTotaleSubAccertamenti());
		// setto i dati per le disponibilita per i vari SUBACCERTAMENTI associati
		if(fromUpdate.getElencoSubAccertamenti()!=null && fromUpdate.getElencoSubAccertamenti().size()>0){
			Iterator it = fromUpdate.getElencoSubAccertamenti().iterator();
			while(it.hasNext()){
				SubAccertamento sub = (SubAccertamento)it.next();
				if(accertamentoDisp.getElencoSubAccertamenti()!=null && accertamentoDisp.getElencoSubAccertamenti().size()>0){
					Iterator itInterno = accertamentoDisp.getElencoSubAccertamenti().iterator();
					while(itInterno.hasNext()){
						SubAccertamento subInterno = (SubAccertamento)itInterno.next();
						if(sub.getUid()==subInterno.getUid()){
							sub.setDisponibilitaIncassare(subInterno.getDisponibilitaIncassare());
							sub.setMotivazioneDisponibilitaIncassare(subInterno.getMotivazioneDisponibilitaIncassare());
							break;
						}
					}
				}
			}
		}
		return fromUpdate;
	}


	@Override
	protected void checkServiceParam() throws ServiceParamError {		
		
		
		log.debug("", " heckServiceParam - Begin");
		
		checkNotNull(req.getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente"));
		
		checkNotNull(req.getBilancio(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("bilancio"));
		
		checkNotNull(req.getModificaMovimentoGestioneEntrata(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("modifica movimento gestione entrata"));
		
		checkNotNull(req.getModificaMovimentoGestioneEntrata().getTipoMovimento(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("modifica movimento gestione entrata [ tipo movimento ]"));
		
		checkNotNull(req.getModificaMovimentoGestioneEntrata().getDescrizione(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("modifica movimento gestione entrata [ descrizione ]"));
		
		checkNotNull(req.getModificaMovimentoGestioneEntrata().getTipoModificaMovimentoGestione(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("modifica movimento gestione entrata [ tipo modifica movimento gestione ]"));
		
		checkNotNull(req.getModificaMovimentoGestioneEntrata().getAttoAmministrativo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("modifica movimento gestione entrata [ atto amministrativo]"));
		
		checkNotNull(req.getModificaMovimentoGestioneEntrata().getAccertamento(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("modifica movimento gestione entrata [ accertamento ]"));
		
		//checkNotNull(req.getModificaMovimentoGestioneEntrata().getMotivoModificaEntrata(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("modifica movimento gestione entrata [ motivo modifica ]"));
		
		log.debug("", "checkServiceParam - End");
		
	}	

}
