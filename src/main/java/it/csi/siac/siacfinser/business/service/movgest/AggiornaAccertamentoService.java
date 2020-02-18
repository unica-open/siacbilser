/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.business.service.movgest;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacattser.model.ric.RicercaAtti;
import it.csi.siac.siacbilser.integration.dad.ProvvedimentoDad;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siacfinser.CommonUtils;
import it.csi.siac.siacfinser.Constanti;
import it.csi.siac.siacfinser.StringUtils;
import it.csi.siac.siacfinser.business.service.AbstractBaseService;
import it.csi.siac.siacfinser.business.service.enumeration.CodiceEventoEnum;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaAccertamento;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaAccertamentoResponse;
import it.csi.siac.siacfinser.integration.dad.CommonDad;
import it.csi.siac.siacfinser.integration.dao.common.dto.AttiAmmModificatiGestioneEntrataInfoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.CapitoliInfoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.DatiOperazioneDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.EsitoAggiornamentoMovimentoGestioneDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.EsitoAttivaRegistrazioniMovFinFINGSADto;
import it.csi.siac.siacfinser.integration.dao.common.dto.EsitoControlliAggiornamentoAccertamentoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.EsitoControlliDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.ImpegnoInModificaInfoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.ModificaMovimentoGestioneEntrataInfoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.OttimizzazioneModificheMovimentoGestioneDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.OttimizzazioneMovGestDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.SubMovgestInModificaInfoDto;
import it.csi.siac.siacfinser.integration.util.Operazione;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.SubAccertamento;
import it.csi.siac.siacfinser.model.UnitaElementareGestione;
import it.csi.siac.siacfinser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneEntrata;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacgenser.model.TipoCollegamento;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AggiornaAccertamentoService extends AbstractBaseService<AggiornaAccertamento, AggiornaAccertamentoResponse>{

	@Autowired
	CommonDad commonDad;
	
	@Autowired
	ProvvedimentoDad provvedimentoDad;
	
	@Override
	protected void init() {

	}	


	@Override
	@Transactional
	public AggiornaAccertamentoResponse executeService(AggiornaAccertamento serviceRequest) {

		return super.executeService(serviceRequest);
	}

	@Override
	public void execute() {
		final String methodName = "AggiornaAccertamentoService : execute()";
		
		//1. Leggiamo i dati ricevuti dalla request:	
		Accertamento accertamentoDaAggiornare = req.getAccertamento();
		Soggetto soggettoCreditore = req.getCreditoreAccertamento();
		Ente ente = req.getRichiedente().getAccount().getEnte();
		Richiedente richiedente = req.getRichiedente();
		UnitaElementareGestione unitaElemDiGest = req.getUnitaElementareGestioneE();
		
		Integer annoBilancioRequest = req.getBilancio().getAnno();
		
		setBilancio(req.getBilancio());
		
		
		// caching su atto amministrativo
		HashMap<String, AttoAmministrativo> cacheAttoAmm = new HashMap<String, AttoAmministrativo>();
		
		//2. Si inizializza l'oggetto DatiOperazioneDto, dto di comodo generico che verra' passato tra i metodi 
		DatiOperazioneDto datiOperazione = commonDad.inizializzaDatiOperazione(ente, req.getRichiedente(), Operazione.MODIFICA, bilancio.getAnno());
		
		
		//3. Si valorizza l'oggetto ImpegnoInModificaInfoDto, dto di comodo specifico di questo servizio
		ImpegnoInModificaInfoDto accertamentoInModificaInfoDto = accertamentoOttimizzatoDad.getDatiGeneraliImpegnoInAggiornamento((Accertamento)accertamentoDaAggiornare, datiOperazione, bilancio);
		
		log.debug(methodName, "impostati i dati necessari  all'aggiornamento. valuto se vi siano o meno sub da modificare. ");
		//Valutiamo anche quali sub sono o meno da modificare:
		accertamentoInModificaInfoDto = accertamentoOttimizzatoDad.valutaSubImp(accertamentoDaAggiornare, accertamentoInModificaInfoDto, datiOperazione, bilancio);
		
		
		//	 SIAC-5529 mi segno lo stato prima degli aggiornamenti: mi servira' per capire se e' necessario effettuare le registrazioni 
		//  per passaggio a stato definitivo da definitivo non liquidabile:
		String statoCodOld = accertamentoOttimizzatoDad.getStatoCode(accertamentoInModificaInfoDto.getSiacTMovgestTs(), datiOperazione);
		//  faccio lo stesso ragionamento per gli eventuali sub inseriti/modificati:
		List<SubAccertamento> subInvariati = null;
		List<SubAccertamento> subDaInserireOModificareConStatoOld = null;
	    List<SubAccertamento> subImpegniDaInserireOModificare = new ArrayList<SubAccertamento>();

		
		if(accertamentoInModificaInfoDto.getInfoSubValutati()!=null){
			SubMovgestInModificaInfoDto isv = accertamentoInModificaInfoDto.getInfoSubValutati();
			subImpegniDaInserireOModificare = addAllConNew(isv.getSubImpegniDaInserire(), isv.getSubImpegniDaModificare());
			subDaInserireOModificareConStatoOld = clone(subImpegniDaInserireOModificare);
			ricaricaStatoCod(subDaInserireOModificareConStatoOld, datiOperazione);
			if(isv.getSubImpegniInvariati()!=null){
				subInvariati = clone(isv.getSubImpegniInvariati());
				log.debug(methodName, "subimpegni invariati: " + isv.getSubImpegniInvariati().size());
			}
		}
		//
		
		//Dati modifiche:
		OttimizzazioneModificheMovimentoGestioneDto ottimizzazioneModDto = null;
		ottimizzazioneModDto = impegnoOttimizzatoDad.caricaOttimizzazioneModificheMovimentoGestioneDto(accertamentoInModificaInfoDto.getSiacTMovgest().getSiacTMovgestTs());
		if(accertamentoInModificaInfoDto.getOttimizzazioneMovGest()==null){
			accertamentoInModificaInfoDto.setOttimizzazioneMovGest(new OttimizzazioneMovGestDto());
		}
		accertamentoInModificaInfoDto.getOttimizzazioneMovGest().setOttimizzazioneModDto(ottimizzazioneModDto);
		
		//4. Carichiamo i dati del capitolo:
		HashMap<Integer, CapitoloEntrataGestione> capitoliDaServizio = 	caricaCapitolEntrataGestioneEResiduo(richiedente, accertamentoDaAggiornare.getChiaveCapitoloEntrataGestione(), accertamentoInModificaInfoDto.getChiaveCapitoloResiduo());
		CapitoliInfoDto capitoliInfo = new CapitoliInfoDto();
		capitoliInfo.setCapitoliDaServizioEntrata(capitoliDaServizio);
				
				
		//5.	Dobbiamo capire se il servizio e' stato invocato per:
			//A. Aggiornare l'accertamento in senso stretto
			//B. Aggiornare le modifiche movimento
		ModificaMovimentoGestioneEntrataInfoDto valutazioneModMov = accertamentoOttimizzatoDad.valutaModificheMovimentoEntrata(accertamentoDaAggiornare.getListaModificheMovimentoGestioneEntrata(),datiOperazione,accertamentoInModificaInfoDto);
		List<ModificaMovimentoGestioneEntrataInfoDto> valutazioneModMovSubs = accertamentoOttimizzatoDad.valutaModificheMovimentoSubAcc(accertamentoDaAggiornare.getElencoSubAccertamenti(), ente.getUid(), datiOperazione,accertamentoInModificaInfoDto);
		
		//creiamo una lista dove ci sono le valutazione sia delle (eventuali) modifiche sull'accertamento
		//che sui suoi subaccertamenti:
		List<ModificaMovimentoGestioneEntrataInfoDto> valutazioneModMovAll = CommonUtils.toList( CommonUtils.toList(valutazioneModMov),valutazioneModMovSubs);
		
		boolean modifichePresenti = accertamentoOttimizzatoDad.presentiModificheEntrata(valutazioneModMovAll);
		
		log.debug(methodName, new StringBuilder()
				.append("modifiche presenti? ")
				.append(modifichePresenti)
				.toString());
		
		List<Errore> listaErrori = new ArrayList<Errore>();
		EsitoControlliAggiornamentoAccertamentoDto esitoControlliAggAcc = null;
		if(!modifichePresenti){
			//Siamo nel caso: A. Aggiornare l'accertamento in senso stretto
			//lanciamo quindi i controlli descritti nell'Operazione "Aggiorna Accertamento"
			esitoControlliAggAcc =  accertamentoOttimizzatoDad.controlliDiMeritoAggiornamentoAccertamento(req.getRichiedente(),  req.getEnte(), 
					bilancio, accertamentoDaAggiornare,datiOperazione,accertamentoInModificaInfoDto);
			listaErrori = esitoControlliAggAcc.getListaErrori();
			
			if(listaErrori == null){
				listaErrori = new ArrayList<Errore>();
			}
			
			/*
			 * SIAC-6929
			 * DATO IMPEGNO 
			 */
			if(req.getAccertamento() != null  ){

				/*
				 * SIAC-6929
				 * DATO SUBACCERTAMENTO 
				 */
				if(subImpegniDaInserireOModificare!= null && !subImpegniDaInserireOModificare.isEmpty()){
					for(int i=0; i<subImpegniDaInserireOModificare.size();i++){
							checkAttoAmministrativo(listaErrori, subImpegniDaInserireOModificare.get(i).getAttoAmministrativo());
					}
				}else {
					checkAttoAmministrativo(listaErrori, req.getAccertamento().getAttoAmministrativo());
				}
			
			}
			
			
//			//SIAC-6929
//			if(accertamentoDaAggiornare.getAttoAmministrativo() != null && accertamentoDaAggiornare.getAttoAmministrativo().getUid() > 0){
//				RicercaAtti ricercaAtti = new RicercaAtti();
//				ricercaAtti.setUid(accertamentoDaAggiornare.getAttoAmministrativo().getUid());
//				provvedimentoDad.setLoginOperazione(loginOperazione);
//				provvedimentoDad.setEnte(req.getEnte());
//				List<AttoAmministrativo> listaAtti = provvedimentoDad.ricerca(ricercaAtti);
//				if(listaAtti != null && !listaAtti.isEmpty()){
//					AttoAmministrativo attoAmm = listaAtti.get(0);
//					if(attoAmm.getBloccoRagioneria() != null && attoAmm.getBloccoRagioneria().booleanValue()){
//						res.setErrori(Arrays.asList(ErroreFin.OGGETTO_BLOCCATO_DALLA_RAGIONERIA.getErrore("Numero Provvedimento " + 
//								attoAmm.getNumero() + " Oggetto " + attoAmm.getOggetto())));
//						res.setAccertamento(null);
//						res.setEsito(Esito.FALLIMENTO);
//						return;
//					}
//				}
//			}
			
		} else {
			//Siamo nel caso: B. Aggiornare le modifiche movimento
			//lanciamo quindi i controlli descritti nell'Operazione "Gestisce Modifica Movimento Entrata"
			
			//Bisogna assicurarsi che non ci sia piu' di una nuova modifica per acc e per sub (quelle in update non danno problemi
			//perche' per esse l'importo e' non modificabile:
			EsitoControlliDto esitoContrAggModMov = controlliAggiornaModificheMovimento(valutazioneModMov, valutazioneModMovSubs, cacheAttoAmm, datiOperazione, accertamentoInModificaInfoDto,capitoliInfo);
			listaErrori = esitoContrAggModMov.getListaErrori();
			addWarningToResp(esitoContrAggModMov);
			
		 	// SIAC-5945 veniva perso il SIOPE nell'impegno residuo, occorre ricaricare i dati che 
			//  il front end si dimentica di passare in caso di inserimento modifiche:
			accertamentoDaAggiornare = (Accertamento) accertamentoOttimizzatoDad.completaDatiPerDoppiaGestioneDaModifiche(accertamentoInModificaInfoDto, accertamentoDaAggiornare);
			//
		}		
		
		if(!modifichePresenti){
			//aggiungiamo l'importo utilizzabile per pilotare correttamente l'operazione interna:
			//(che non puo' essere ricevuto dal front-end)
			BigDecimal importoUtilizzabileNew =esitoControlliAggAcc.getImportoUtilizzabileNew();
			accertamentoDaAggiornare.setImportoUtilizzabile(importoUtilizzabileNew);		
		}
		
		if(listaErrori!=null && listaErrori.size()>0){
			res.setErrori(listaErrori);
			res.setEsito(Esito.FALLIMENTO);
			return;
		}
		
		//6. Si invoca il metodo che esegue l'operazione "core" di aggiornamento di impegni e accertamenti:
		EsitoAggiornamentoMovimentoGestioneDto esito = accertamentoOttimizzatoDad.operazioneInternaAggiornaAccertamento(richiedente, ente,
				bilancio, accertamentoDaAggiornare, soggettoCreditore, unitaElemDiGest,datiOperazione, accertamentoInModificaInfoDto,false,capitoliInfo);

		//7. Costruzione response:
		if (esito.getListaErrori()!=null && esito.getListaErrori().size()>0) {
			//Esito negativo da operazione interna
			res.setErrori(esito.getListaErrori());
			res.setEsito(Esito.FALLIMENTO);
			res.setAccertamento(null);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return;
		} else {
			TransactionAspectSupport.currentTransactionStatus().flush();
			Accertamento fromUpdate = (Accertamento) esito.getMovimentoGestione();
			//Completiamo i dati relativi agli atti amministrativi, "completaDatiRicercaAccertamentoPk" coordina opportunamente i provvedimentoService
			fromUpdate = completaDatiRicercaAccertamentoPk(richiedente, fromUpdate);
			//Completo i dati delle disponibilita:
			Accertamento accertamentoDisp = completaDisponibilitaAccertamento(richiedente, ente, fromUpdate, bilancio);
			// setto i dati delle disponibilita per l'accertamento
			fromUpdate.setDisponibilitaUtilizzare(accertamentoDisp.getDisponibilitaUtilizzare());
			fromUpdate.setMotivazioneDisponibilitaUtilizzare(accertamentoDisp.getMotivazioneDisponibilitaUtilizzare());
			fromUpdate.setDisponibilitaIncassare(accertamentoDisp.getDisponibilitaIncassare());
			fromUpdate.setMotivazioneDisponibilitaIncassare(accertamentoDisp.getMotivazioneDisponibilitaIncassare());
			fromUpdate.setDisponibilitaSubAccertare(accertamentoDisp.getDisponibilitaSubAccertare());
			fromUpdate.setMotivazioneDisponibilitaSubAccertare(accertamentoDisp.getMotivazioneDisponibilitaSubAccertare());
			fromUpdate.setTotaleSubAccertamenti(accertamentoDisp.getTotaleSubAccertamenti());
			// setto i dati per le disponibilita per i vari SUBACCERTAMENTI associati
			if(fromUpdate.getElencoSubAccertamenti()!=null && fromUpdate.getElencoSubAccertamenti().size()>0){
				Iterator<SubAccertamento> it = fromUpdate.getElencoSubAccertamenti().iterator();
				while(it.hasNext()){
					SubAccertamento sub = it.next();
					if(accertamentoDisp.getElencoSubAccertamenti()!=null && accertamentoDisp.getElencoSubAccertamenti().size()>0){
						Iterator<SubAccertamento> itInterno = accertamentoDisp.getElencoSubAccertamenti().iterator();
						while(itInterno.hasNext()){
							SubAccertamento subInterno = itInterno.next();
							if(sub.getUid()==subInterno.getUid()){
								sub.setDisponibilitaIncassare(subInterno.getDisponibilitaIncassare());
								sub.setMotivazioneDisponibilitaIncassare(subInterno.getMotivazioneDisponibilitaIncassare());
								break;
							}
						}
					}
				}
			}
			
			// Prima di restituire la response scatta la registrazione fin -> gen , se non esiste una prima nota annullata inserisco la registrazione 
			// jira 3719 se l'anno di bilancio è uguale all'anno del movimento richiamo tutta la logica di registrazione sul movimento o sui suoi sub/modifiche
			boolean registraPerAnnoBilancioCorrente = annoBilancioRequest == fromUpdate.getAnnoMovimento();
			
			// CR-552 si registra anche se annoMovimento > annoBilancio se 
			boolean registraPerModificaImportoPluriennale = fromUpdate.getAnnoMovimento() > annoBilancioRequest && fromUpdate.getAnnoScritturaEconomicoPatrimoniale() != null;
			
			// 	SIAC-5529 leggo lo stato dopo degli aggiornamenti, 
			//  per capire se e' cambiato in definitivo:
			DatiOperazioneDto datiOperazioneAvanzaTs = impegnoOttimizzatoDad.avanzaAClockTime(datiOperazione);
			String statoCodNew = accertamentoOttimizzatoDad.getStatoCode(fromUpdate.getUid(), datiOperazioneAvanzaTs);
			boolean passaggioAStatoDefinitivo = CommonUtils.passaggioAStatoDefinitivo(statoCodOld, statoCodNew);
			//

			// siac-4163
			// SE aggiorno il flagfattura da NO A SI devo ANNULLARE le possibili registrazioni,  di accertamento se definitivo e se non è definitivo di sub e modifiche
			boolean flagFatturaModificatoInSi = !accertamentoInModificaInfoDto.isFlagFattura() && fromUpdate.isFlagFattura();
			boolean flagFatturaModificatoInNo = accertamentoInModificaInfoDto.isFlagFattura() && !fromUpdate.isFlagFattura();
			boolean flagFatturaNonModificato = accertamentoInModificaInfoDto.isFlagFattura() == fromUpdate.isFlagFattura();
			
			// jira 4351 (CR) in fase di bilancio = predisposizione consuntivo devo controllare se l'impegno è competenza o residuo e se l'importo della modifica è meno o più
			// cambiano poi anche le causali di scrittura prima nota
			boolean residuo = annoBilancioRequest > fromUpdate.getAnnoMovimento();
			boolean registraPerPredisposizioneConsuntivo = false;
			
			String codiceFaseBilancio= accertamentoOttimizzatoDad.caricaCodiceBilancio(datiOperazione, annoBilancioRequest);
			if (!StringUtils.isEmpty(codiceFaseBilancio) && Constanti.BIL_FASE_OPERATIVA_PREDISPOSIZIONE_CONSUNTIVO.equals(codiceFaseBilancio)) {
				registraPerPredisposizioneConsuntivo = true;
			}
			
			if(flagFatturaModificatoInSi){
				
				// se definitivo quindi senza sub
				if(fromUpdate.getStatoOperativoMovimentoGestioneEntrata().equalsIgnoreCase(Constanti.MOVGEST_STATO_DEFINITIVO)){
					annullaRegistrazioneEPrimaNotaMovimentoGestione(TipoCollegamento.ACCERTAMENTO, fromUpdate, annoBilancioRequest);
					
					// annullo le possibili registrazioni delle modifiche di importo/soggetto 
					if(fromUpdate.getListaModificheMovimentoGestioneEntrata()!=null && 
							!fromUpdate.getListaModificheMovimentoGestioneEntrata().isEmpty())
						for (ModificaMovimentoGestioneEntrata modificaAcc : fromUpdate.getListaModificheMovimentoGestioneEntrata()) {
							annullaRegistrazioneEPrimaNotaMovimentoGestione(TipoCollegamento.MODIFICA_MOVIMENTO_GESTIONE_ENTRATA,modificaAcc, annoBilancioRequest );
						}
						
				}
				
				// se con sub
				if(fromUpdate.getStatoOperativoMovimentoGestioneEntrata().equalsIgnoreCase(Constanti.MOVGEST_STATO_DEFINITIVO_NON_LIQUIDABILE)){
					
					// CERCO i sub e li annullo
					if(fromUpdate.getElencoSubAccertamenti()!=null && !fromUpdate.getElencoSubAccertamenti().isEmpty()){
						
						for (SubAccertamento sub : fromUpdate.getElencoSubAccertamenti()) {
							
							annullaRegistrazioneEPrimaNotaMovimentoGestione(TipoCollegamento.SUBACCERTAMENTO, sub, annoBilancioRequest);
											
							// se annullo il sub devo annullare anche tutte le possibili modifiche di importo / soggetto
							if(sub.getListaModificheMovimentoGestioneEntrata()!=null && !sub.getListaModificheMovimentoGestioneEntrata().isEmpty()){
								
								for (ModificaMovimentoGestioneEntrata modificaMovimentoGestioneEntrata : sub.getListaModificheMovimentoGestioneEntrata()) {
								
									annullaRegistrazioneEPrimaNotaMovimentoGestione(TipoCollegamento.MODIFICA_MOVIMENTO_GESTIONE_ENTRATA, modificaMovimentoGestioneEntrata, annoBilancioRequest);
								}
								
							}
						}
						
					}
					
					
				}
			}
			
			if(flagFatturaModificatoInNo || passaggioAStatoDefinitivo){
				//OLD gestisciRegistrazioneGENPerAccertamento(fromUpdate, TipoCollegamento.ACCERTAMENTO, CodiceEventoEnum.INSERISCI_ACCERTAMENTO.getCodice(), annoBilancioRequest);
				//NEW:
				//SIAC-6000
				boolean saltaInserimentoPrimaNota = req.isSaltaInserimentoPrimaNota();
				EsitoAttivaRegistrazioniMovFinFINGSADto esitoReg = gestisciRegistrazioneGENPerAccertamento(fromUpdate, TipoCollegamento.ACCERTAMENTO, CodiceEventoEnum.INSERISCI_ACCERTAMENTO.getCodice(), annoBilancioRequest, saltaInserimentoPrimaNota);
				res.setRegistrazioneMovFinFIN(esitoReg.getRegistrazioneMovFinFINInserita());
				//
			
			}
			
			
			
			if((registraPerAnnoBilancioCorrente && flagFatturaNonModificato) || registraPerPredisposizioneConsuntivo || registraPerModificaImportoPluriennale){
				
				/**System.out.println("registraPerModificaImportoPluriennale: " + registraPerModificaImportoPluriennale);
				System.out.println("flagFatturaNonModificato: " + flagFatturaNonModificato);
				System.out.println("registraPerPredisposizioneConsuntivo: " + registraPerPredisposizioneConsuntivo);
				System.out.println("registraPerAnnoBilancioCorrente: " + registraPerAnnoBilancioCorrente);*/
				
				if(valutazioneModMov.isModificheDaCrearePresenti()){
					gestisciRegistrazioneGENModificheMovimentoEntrata(fromUpdate, null, TipoCollegamento.MODIFICA_MOVIMENTO_GESTIONE_ENTRATA, registraPerAnnoBilancioCorrente,flagFatturaNonModificato, registraPerPredisposizioneConsuntivo, residuo, registraPerModificaImportoPluriennale, annoBilancioRequest);
				}
				
				if(presentiModificheEntrataSuiSubDaInserire(valutazioneModMovSubs)){
					for (SubAccertamento sub : fromUpdate.getElencoSubAccertamenti()) {
						gestisciRegistrazioneGENModificheMovimentoEntrata(fromUpdate, sub, TipoCollegamento.MODIFICA_MOVIMENTO_GESTIONE_ENTRATA, registraPerAnnoBilancioCorrente, flagFatturaNonModificato, registraPerPredisposizioneConsuntivo, residuo, registraPerModificaImportoPluriennale, annoBilancioRequest);
					}
					
				}
			
				
			}
			
			
			if(registraPerAnnoBilancioCorrente && flagFatturaNonModificato || registraPerModificaImportoPluriennale){
				
				if(esito.isSubInseriti()) {
					for (SubAccertamento sub: fromUpdate.getElencoSubAccertamenti()) {
						
						//Solo se il sub iterato e' stato inserito o modificato:
						if(!CommonUtils.contenutoInLista(subInvariati, sub.getUid())){
							
							// fix per SIAC-5529 e SIAC-5980:
							String statoCodNewSub = accertamentoOttimizzatoDad.getStatoCodeByTsId(sub.getUid(), datiOperazioneAvanzaTs);
							boolean passaggioAStatoDefinitivoSub = false;
							String statoCodOldSub = determinaStatoOldSub(subDaInserireOModificareConStatoOld, sub);
							passaggioAStatoDefinitivoSub = CommonUtils.passaggioAStatoDefinitivo(statoCodOldSub, statoCodNewSub);
							//
							
							if(passaggioAStatoDefinitivoSub){
								EsitoAttivaRegistrazioniMovFinFINGSADto esitoDto = gestisciRegistrazioneGENPerSubAccertamento(fromUpdate, sub, TipoCollegamento.SUBACCERTAMENTO, CodiceEventoEnum.INSERISCI_SUBACCERTAMENTO.getCodice(), annoBilancioRequest, req.isSaltaInserimentoPrimaNotaSuSub());
								res.setRegistrazioneMovFinFIN(esitoDto.getRegistrazioneMovFinFINInserita());
							}
						}
					}
				}
			}
			
			//costruisco la response esito OK:
			res.setAccertamento(fromUpdate);
			res.setEsito(Esito.SUCCESSO);
		}
	}
	
	private String determinaStatoOldSub(List<SubAccertamento> subDaInserireOModificareConStatoOld,SubAccertamento sub){
		SubAccertamento subPrima = CommonUtils.getById(subDaInserireOModificareConStatoOld, sub.getUid());
		String statoCodOldSub = null;
		if(subPrima!=null){
			// si tratta di un sub modificato
			statoCodOldSub = subPrima.getStatoOperativoMovimentoGestioneEntrata();
		}
		return statoCodOldSub;
	}
	
	private void ricaricaStatoCod(List<SubAccertamento> subDaInserireOModificareConStatoOld,DatiOperazioneDto datiOperazione){
		if(!isEmpty(subDaInserireOModificareConStatoOld)){
			for(SubAccertamento it: subDaInserireOModificareConStatoOld){
				if(it!=null && it.getUid()>0){
					//per quelli modificati ricarico lo stato
					String statoCodOldSub = accertamentoOttimizzatoDad.getStatoCodeByTsId(it.getUid(), datiOperazione);
					it.setStatoOperativoMovimentoGestioneEntrata(statoCodOldSub);
				}
			}
		}
	}
	
	/**
	 * Raggruppa tutti i controlli relativi al servizio Gestisce Modifica Movimento Entrata 
	 * @param valutazioneModMov
	 * @param valutazioneModMovSubs
	 * @param cacheAttoAmm
	 * @param datiOperazione
	 * @param impegnoInModificaInfoDto
	 * @param capitoliInfo
	 * @return
	 */
	private EsitoControlliDto controlliAggiornaModificheMovimento(ModificaMovimentoGestioneEntrataInfoDto valutazioneModMov,List<ModificaMovimentoGestioneEntrataInfoDto> valutazioneModMovSubs,
			HashMap<String, AttoAmministrativo> cacheAttoAmm, DatiOperazioneDto datiOperazione, ImpegnoInModificaInfoDto impegnoInModificaInfoDto, CapitoliInfoDto capitoliInfo){
		final String methodName="controlliAggiornaModificheMovimento";
		EsitoControlliDto esito = new EsitoControlliDto();
		
		List<Errore> listaErrori = new ArrayList<Errore>();
		
		Accertamento accertamentoDaAggiornare = req.getAccertamento();
		Richiedente richiedente = req.getRichiedente();
		Bilancio bilancio = req.getBilancio();
		
		List<ModificaMovimentoGestioneEntrataInfoDto> valutazioneModMovAll = CommonUtils.toList( CommonUtils.toList(valutazioneModMov),valutazioneModMovSubs);
		
		//Bisogna assicurarsi che non ci sia piu' di una nuova modifica per acc e per sub (quelle in update non danno problemi
		//perche' per esse l'importo e' non modificabile:
		listaErrori = controlliNumeroModificheImporto(valutazioneModMov,valutazioneModMovSubs);
		
		if(listaErrori!=null && listaErrori.size()>0){
			log.debug(methodName, "Non ci possono essere piu' di una nuova modifica di importo per acc e per sub: esco.");
			esito.setListaErrori(listaErrori);
			return esito;
		}
		
		//Prima di chiamare i controlli devo recuperare i provvedimenti (da servizio) legati alle modifiche movimento richieste
		//per validarne lo stato e l'esistenza (non posso farlo dopo perche' i 
		listaErrori = controlloValiditaAttiModalitaPagamento(richiedente, valutazioneModMovAll, cacheAttoAmm);
		
		if(listaErrori!=null && listaErrori.size()>0){
			log.debug(methodName, "Errori durante i controlli sui provvedimenti associati alle modifiche.");
			esito.setListaErrori(listaErrori);
			return esito;
		}

		//altri controlli non dipendenti da servizi esterni:
		EsitoControlliDto esitoConMerito =  accertamentoOttimizzatoDad.controlliDiMeritoAggiornamentoModificaMovimentoEntrata(req.getRichiedente(),  req.getEnte(), bilancio, accertamentoDaAggiornare,datiOperazione,
				impegnoInModificaInfoDto,valutazioneModMov,valutazioneModMovSubs,capitoliInfo);
		if(esitoConMerito.getListaErrori()!=null && esitoConMerito.getListaErrori().size()>0){
			log.debug(methodName, "i controlli non dipendenti da servizi esterni presentano errori.");
			esito.setListaErrori(listaErrori);
			return esito;
		}
		esito.addWarning(esitoConMerito.getListaWarning());
		
		log.debug(methodName, "l'accertamento e le sue modifiche hanno superato positivamente  tutti i controlli.");
		return esito;
	}
	
	private List<Errore> controlliNumeroModificheImporto(ModificaMovimentoGestioneEntrataInfoDto valutazioneModMov,List<ModificaMovimentoGestioneEntrataInfoDto> valutazioneModMovSubs) {
		List<Errore> listaErrori = new ArrayList<Errore>();
		
		
		//1. Se si riscontrano pi� di una modifica di Accertamento inviare un messaggio bloccante:  
		//<COR_ERR_0031 Aggiornamento non possibile (<entit�> : � L�accertamento   <ANNO/NUMERO>�,  <operazione>: �collegato a pi� di una modifica� )>.
		//2. Se si riscontrano pi� di una modifica di SubAccertamento (anche relativa a Sub differenti) inviare un messaggio bloccante:  
		//<COR_ERR_0031 Aggiornamento non possibile (<entit�> : � L�accertamento   <ANNO/NUMERO >�,  
		//		<operazione>: �collegato a pi� di una modifica di SubAccertamento� )>.
		
		
		int numeroModificheDiImportoAccertamento = 0;
	
		List<ModificaMovimentoGestioneEntrata> nuoveAcc = valutazioneModMov.getModificheDaCreare();
		
		for(ModificaMovimentoGestioneEntrata nuovaAccIt : nuoveAcc){
			if(nuovaAccIt.isModificaDiImporto()){
				numeroModificheDiImportoAccertamento++;
			}
		}
		if(numeroModificheDiImportoAccertamento>1){
			listaErrori.add(ErroreFin.CAMPO_NON_ACCETTABILE.getErrore("Non si puo' indicare piu' di una nuova modifica importo alla volta"));
			return listaErrori;
		}
		
		int numeroModificheDiImportoSub = 0;
		List<ModificaMovimentoGestioneEntrata> nuoveSub = null;
		for(ModificaMovimentoGestioneEntrataInfoDto subModInfoIt : valutazioneModMovSubs){
			nuoveSub = CommonUtils.toList(nuoveSub, subModInfoIt.getModificheDaCreare());
		}
		if(nuoveSub!=null && nuoveSub.size()>0){
			for(ModificaMovimentoGestioneEntrata nuovaSubIt : nuoveSub){
				if(nuovaSubIt.isModificaDiImporto()){
					numeroModificheDiImportoSub++;
				}
			}
		}
		if(numeroModificheDiImportoSub>1){
			listaErrori.add(ErroreFin.CAMPO_NON_ACCETTABILE.getErrore("Non si puo' indicare piu' di una nuova modifica importo alla volta"));
			return listaErrori;
		}
		
		return listaErrori;
	}
	
	/**
	 * 	IN QUESTO METODO VIENE ESEGUITO IL SOLO CONTROLLO RELATIVO ALLA VALIDATA DEGLI ATTI
	 *   definito nel capitolo "3.	Verifica dati trasmessi"
	 * 	DEL CAPITOLO 2.4.5	Operazione: Gestisce Modifica Movimento Entrata 
	 * @return
	 */
	public List<Errore> controlloValiditaAttiModalitaPagamento(Richiedente richiedente, List<ModificaMovimentoGestioneEntrataInfoDto> valutazioneModMovAll,HashMap<String, AttoAmministrativo> cacheAttoAmm){
		List<Errore> listaErrori = new ArrayList<Errore>();
		
		//Stato Atto: L'atto amministrativo da associare alla modifica deve esistere e non essere ANNULLATO (vedi Ricerca Provvedimento), 
		//in caso contrario viene emesso l'errore:
		//<FIN_ERR_0075. Stato Provvedimento non consentito (operazione = Modifica Movimento, stato = Definitivo)
		
		for(ModificaMovimentoGestioneEntrataInfoDto modIt : valutazioneModMovAll){
			List<AttiAmmModificatiGestioneEntrataInfoDto> attiDaValidare = modIt.getAttiInseritiEModificati();
			if(attiDaValidare!=null && attiDaValidare.size()>0){
				for(AttiAmmModificatiGestioneEntrataInfoDto attIt: attiDaValidare){
					AttoAmministrativo attoDaService = estraiAttoAmministrativoCaching(richiedente,attIt.getAttoRicevutoInInput(), cacheAttoAmm);
					if(attoDaService==null || attoDaService.getStatoOperativo().equalsIgnoreCase(Constanti.ATTO_AMM_STATO_ANNULLATO)){
						listaErrori.add(ErroreFin.STATO_PROVVEDIMENTO_NON_CONSENTITO.getErrore("Modifica Movimento","Definitivo"));
						return listaErrori;
					}
				}
			}
		}
		
		return listaErrori;
	}
	
	
	//SIAC-6929
	private void checkAttoAmministrativo(List<Errore> listaErrori, AttoAmministrativo attoAmm){
		
		if( attoAmm!= null	&& attoAmm.getUid()!= 0 ){
			
			RicercaAtti ricercaAtti = new RicercaAtti();
			ricercaAtti.setUid(attoAmm.getUid());
			provvedimentoDad.setLoginOperazione(loginOperazione);
			provvedimentoDad.setEnte(req.getEnte());
			List<AttoAmministrativo> listaAtti = provvedimentoDad.ricerca(ricercaAtti);
			if(listaAtti!= null && !listaAtti.isEmpty()){
				AttoAmministrativo atto = listaAtti.get(0);
				if(atto.getBloccoRagioneria()!= null && atto.getBloccoRagioneria().booleanValue()){

					listaErrori.add(ErroreFin.OGGETTO_BLOCCATO_DALLA_RAGIONERIA.getErrore("Numero Provvedimento " + 
							atto.getNumero() + " Oggetto " + atto.getOggetto()));
				}
			}
		}
		
	}
	
}
