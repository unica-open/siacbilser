/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.business.service.movgest;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
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
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siacfinser.CommonUtils;
import it.csi.siac.siacfinser.Constanti;
import it.csi.siac.siacfinser.ModelUtils;
import it.csi.siac.siacfinser.StringUtils;
import it.csi.siac.siacfinser.business.service.AbstractBaseService;
import it.csi.siac.siacfinser.business.service.enumeration.CodiceEventoEnum;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaImpegno;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaImpegnoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.DatiOpzionaliElencoSubTuttiConSoloGliIds;
import it.csi.siac.siacfinser.integration.dad.CommonDad;
import it.csi.siac.siacfinser.integration.dad.ImpegnoOttimizzatoDad;
import it.csi.siac.siacfinser.integration.dao.common.dto.AttiAmmModificatiGestioneSpesaInfoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.CapitoliInfoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.DatiOperazioneDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.EsitoAggiornamentoMovimentoGestioneDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.EsitoAttivaRegistrazioniMovFinFINGSADto;
import it.csi.siac.siacfinser.integration.dao.common.dto.EsitoCompilaListaSubImpegniConTuttiGliIdsDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.EsitoControlliAggiornamentoImpegnoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.EsitoControlliDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.ImpegnoInModificaInfoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.ModificaMovimentoGestioneSpesaInfoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.ModificaVincoliImpegnoInfoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.OttimizzazioneModificheMovimentoGestioneDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.OttimizzazioneMovGestDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.SubMovgestInModificaInfoDto;
import it.csi.siac.siacfinser.integration.entity.SiacTMovgestTsFin;
import it.csi.siac.siacfinser.integration.util.Operazione;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.UnitaElementareGestione;
import it.csi.siac.siacfinser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneSpesa;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacgenser.model.TipoCollegamento;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AggiornaImpegnoService extends AbstractBaseService<AggiornaImpegno, AggiornaImpegnoResponse>{

	@Autowired
	private ImpegnoOttimizzatoDad impegnoOttimizzatoDad;
	
	@Autowired
	private ProvvedimentoDad provvedimentoDad;
	
	@Autowired
	CommonDad commonDad;
	
	@Override
	protected void init() {
		final String methodName = "AggiornaImpegnoService : init()";
		log.trace(methodName, "- Begin");		
	
	}	

	@Override
	@Transactional(timeout=5000, readOnly=true)
	public AggiornaImpegnoResponse executeService(AggiornaImpegno serviceRequest) {
		
		return super.executeService(serviceRequest);
	}
	
	@Override
	public void execute() {
		
		
		//1. Leggiamo i dati ricevuti dalla request:	
		Impegno impegnoDaAggiornare = req.getImpegno();
		Soggetto soggettoCreditore = req.getCreditoreImpegno();
		ente = req.getRichiedente().getAccount().getEnte();
		Richiedente richiedente = req.getRichiedente();
		UnitaElementareGestione unitaElemDiGest = req.getUnitaElementareDiGestione();
		
		Integer annoBilancioRequest = req.getBilancio().getAnno();
		
		setBilancio(req.getBilancio());
		//String annoBilancio = Integer.toString(bilancio.getAnno()); 
		
		
		//FEB_MARZO 2016 Nuova gestione paginazione sub. L'eliminazione dei sub ora e' guidata tramite questo parametro:
		List<SubImpegno> subImpegniDaEliminare = req.getElencoSubImpegniDaEliminare();
		//e l'inserimento/modifica dei sub prevede che vengano passati solo quelli che si vuole inserire/modificare (tipicamente uno solo):
		List<SubImpegno> subImpegniDaInserireOModificare = impegnoDaAggiornare.getElencoSubImpegni();
		//
		
		
		
		
		
		// caching su atto amministrativo
		HashMap<String, AttoAmministrativo> cacheAttoAmm = new HashMap<String, AttoAmministrativo>();
		
		//2. Si inizializza l'oggetto DatiOperazioneDto, dto di comodo generic che verra' passato tra i metodi 
		DatiOperazioneDto datiOperazione = commonDad.inizializzaDatiOperazione(ente, req.getRichiedente(), Operazione.MODIFICA, bilancio.getAnno());
		
		//3. Si valorizza l'oggetto ImpegnoInModificaInfoDto, dto di comodo specifico di questo servizio
		@SuppressWarnings("rawtypes")
		ImpegnoInModificaInfoDto impegnoInModificaInfoDto = impegnoOttimizzatoDad.getDatiGeneraliImpegnoInAggiornamento(impegnoDaAggiornare, datiOperazione, bilancio);

		//Valutiamo anche quali sub sono o meno da modificare:
		//FEBBRAIO 2016, nuovo comportamento, viene INTERDETTA la possibilita' di eliminare i SUB IMPEGNI TRAMITE QUESTO SERVIZIO. Il motivo e' la paginazione dei sub.
		impegnoInModificaInfoDto = nuovaValutazioneSubPerGestirePaginazione(impegnoDaAggiornare, richiedente, Integer.toString(annoBilancioRequest), impegnoInModificaInfoDto,subImpegniDaEliminare,subImpegniDaInserireOModificare, datiOperazione);
		
		
		//  SIAC-5529 mi segno lo stato prima degli aggiornamenti, 
		//  mi servira' per capire se e' necessario effettuare le registrazioni 
		//  per passaggio a stato definitivo da definitivo non liquidabile:
		String statoCodOld = accertamentoOttimizzatoDad.getStatoCode(impegnoInModificaInfoDto.getSiacTMovgestTs(), datiOperazione);
		//  faccio lo stesso ragionamento per gli eventuali sub inseriti/modificati:
		List<SubImpegno> subDaInserireOModificareConStatoOld = clone(subImpegniDaInserireOModificare);
		ricaricaStatoCod(subDaInserireOModificareConStatoOld, datiOperazione);
		List<SubImpegno> subInvariati = null;
		if(impegnoInModificaInfoDto.getInfoSubValutati()!=null && impegnoInModificaInfoDto.getInfoSubValutati().getSubImpegniInvariati()!=null){
			subInvariati = clone(impegnoInModificaInfoDto.getInfoSubValutati().getSubImpegniInvariati());
		}
		//
		
		
		//Dati modifiche:
		OttimizzazioneModificheMovimentoGestioneDto ottimizzazioneModDto = null;
		ottimizzazioneModDto = impegnoOttimizzatoDad.caricaOttimizzazioneModificheMovimentoGestioneDto(impegnoInModificaInfoDto.getSiacTMovgest().getSiacTMovgestTs());
		impegnoInModificaInfoDto.getOttimizzazioneMovGest().setOttimizzazioneModDto(ottimizzazioneModDto);
		
		//4. Carichiamo i dati del capitolo:
		HashMap<Integer, CapitoloUscitaGestione> capitoliDaServizio = 
				caricaCapitoloUscitaGestioneEResiduo(richiedente, impegnoDaAggiornare.getChiaveCapitoloUscitaGestione(), impegnoInModificaInfoDto.getChiaveCapitoloResiduo());
		
		CapitoliInfoDto capitoliInfo = new CapitoliInfoDto();
		capitoliInfo.setCapitoliDaServizioUscita(capitoliDaServizio);
		
		//5. Dobbiamo capire se il servizio e' stato invocato per:
		//A. Aggiornare l'impegno in senso stretto
		//B. Aggiornare le modifiche movimento
		ModificaMovimentoGestioneSpesaInfoDto valutazioneModMov = impegnoOttimizzatoDad.valutaModificheMovimentoSpesa(impegnoDaAggiornare.getListaModificheMovimentoGestioneSpesa(),datiOperazione,impegnoInModificaInfoDto);
		List<ModificaMovimentoGestioneSpesaInfoDto> valutazioneModMovSubs = impegnoOttimizzatoDad.valutaModificheMovimentoSubImp(impegnoDaAggiornare.getElencoSubImpegni(), ente.getUid(), datiOperazione,impegnoInModificaInfoDto);
		
		
		// creiamo una lista dove ci sono le valutazione sia delle (eventuali) modifiche sull'impegno
		// che sui suoi subImpegni
		List<ModificaMovimentoGestioneSpesaInfoDto> valutazioneModMovAll = CommonUtils.toList( CommonUtils.toList(valutazioneModMov),valutazioneModMovSubs);
		
		boolean modifichePresenti = impegnoOttimizzatoDad.presentiModificheSpesa(valutazioneModMovAll);
		
		//6. CONTROLLI INIZIALI (pre-operazione interna)
		List<Errore> listaErrori = new ArrayList<Errore>();
		EsitoControlliAggiornamentoImpegnoDto esitoControlliAggAcc = null;
		List<BigDecimal> numeriSubCoinvolitiNelleMod = null;
		if(!modifichePresenti){
			//Siamo nel caso: A. Aggiornare l'accertamento in senso stretto
			//lanciamo quindi i controlli descritti nell'Operazione "Aggiorna Accertamento"
			esitoControlliAggAcc =  impegnoOttimizzatoDad.controlliDiMeritoAggiornamentoImpegno(req.getRichiedente(),  req.getEnte(), 
					bilancio, impegnoDaAggiornare,datiOperazione,impegnoInModificaInfoDto);
			listaErrori = esitoControlliAggAcc.getListaErrori();
			
			if(listaErrori == null){
				listaErrori = new ArrayList<Errore>();
			}
			
			/*
			 * SIAC-6929
			 * DATO IMPEGNO 
			 */
			if(req.getImpegno() != null  ){

				/*
				 * SIAC-6929
				 * DATO SUBIMPEGNO 
				 */
				if(subImpegniDaInserireOModificare!= null && !subImpegniDaInserireOModificare.isEmpty()){
					for(int i=0; i<subImpegniDaInserireOModificare.size();i++){
							checkAttoAmministrativo(listaErrori, subImpegniDaInserireOModificare.get(i).getAttoAmministrativo());
					}
				}else {
					checkAttoAmministrativo(listaErrori, req.getImpegno().getAttoAmministrativo());
				}
			
			}
			
			
		} else {
			
			numeriSubCoinvolitiNelleMod = getNumeriSubCoinvoltiNelleModifiche(valutazioneModMovSubs);
			
			//Siamo nel caso: B. Aggiornare le modifiche movimento
			//lanciamo quindi i controlli descritti nell'Operazione "Gestisce Modifica Movimento Entrata"
			
			//Bisogna assicurarsi che non ci sia piu' di una nuova modifica per acc e per sub (quelle in update non danno problemi
			//perche' per esse l'importo e' non modificabile:
			EsitoControlliDto esitoContrAggModMov = controlliAggiornaModificheMovimento(valutazioneModMov, valutazioneModMovSubs, cacheAttoAmm, datiOperazione, impegnoInModificaInfoDto,capitoliInfo);
			listaErrori = esitoContrAggModMov.getListaErrori();
			addWarningToResp(esitoContrAggModMov);
			
			// 	SIAC-5945 veniva perso il SIOPE nell'impegno residuo, occorre ricaricare i dati che 
			//  il front end si dimentica di passare in caso di inserimento modifiche:
			impegnoDaAggiornare = (Impegno) impegnoOttimizzatoDad.completaDatiPerDoppiaGestioneDaModifiche(impegnoInModificaInfoDto, impegnoDaAggiornare);
			//
		}
		
		if(listaErrori!=null && listaErrori.size()>0){
			res.setErrori(listaErrori);
			res.setEsito(Esito.FALLIMENTO);
			return;
		}
		
		
		//IN CASO DI DOPPIA GESTIONE ANTICIPIAMO IL CARICAMENTO DI DATI CHE SERVIRANNO NELLE ROUTINE DI DOPPIA GESTIONE
		//MA CHE NON SARA' POSSIBILE CARICARE INTERAMENTE DAI DAD:
		ModificaVincoliImpegnoInfoDto infoVincoliValutati = null;
		boolean inserireDoppiaGestione = impegnoOttimizzatoDad.inserireDoppiaGestione(bilancio, impegnoDaAggiornare, datiOperazione);
		if(inserireDoppiaGestione){
			//
			infoVincoliValutati = caricaInfoAccVincoliPerDoppiaGest(bilancio,datiOperazione,impegnoInModificaInfoDto,impegnoDaAggiornare);
		}
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		// 5. Si invoca il metodo che esegue l'operazione "core" di aggiornamento di impegni e accertamenti:
		// qui gli passa il codice creditore ma è null
		EsitoAggiornamentoMovimentoGestioneDto esito = impegnoOttimizzatoDad.operazioneInternaAggiornaImpegno(richiedente, ente, bilancio, impegnoDaAggiornare,soggettoCreditore, unitaElemDiGest,datiOperazione,impegnoInModificaInfoDto ,false,capitoliInfo,infoVincoliValutati);
		
		//6. Costruzione response:
		if (esito.getListaErrori()!=null && esito.getListaErrori().size()>0) {
			//Esito negativo da operazione interna
			res.setErrori(esito.getListaErrori());
			res.setEsito(Esito.FALLIMENTO);
			res.setImpegno(null);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return;
		} else {
			
			//eventuali messaggi non blocanti da comunicare al front-end:
			res.setErrori(CommonUtils.addAll(res.getErrori(), esito.getListaWarning()));
			TransactionAspectSupport.currentTransactionStatus().flush();
			Impegno fromUpdate = (Impegno) esito.getMovimentoGestione();
			
			//FEBBRAIO-MARZO 2016, ottimizzazione e paginazione ricerca sub, tolgo la valorizzazione completa dei sub da aggiorna impegno 
			//per motivi di performance
			List<SubImpegno> subImpegni = fromUpdate.getElencoSubImpegni();
			fromUpdate.setElencoSubImpegni(null);
			//
			
			//Completiamo i dati relativi agli atti amministrativi, "completaDatiRicercaImpegnoPk" coordina opportunamente i provvedimentoService
			fromUpdate = completaDatiRicercaImpegnoPk(richiedente, fromUpdate, String.valueOf(annoBilancioRequest));
			
			//FEBBRAIO-MARZO 2016, ottimizzazione e paginazione ricerca sub, tolgo la valorizzazione completa dei sub da aggiorna impegno 
			//per motivi di performance
			fromUpdate.setElencoSubImpegni(subImpegni);
			//
			
			// Prima di restituire la response scatta la registrazione fin -> gen
			// se non esiste una prima nota annullata inserisco la registrazione, in base all'evento passo il codice corretto
			// jira 3419 non annullo e reinserisco tutto ma cerco di gestire solo quello che è modifcato 
			// jira 3719 se l'anno di bilancio è uguale all'anno del movimento richiamo tutta la logica di registrazione sul movimento o sui suoi sub/modifiche
			// jira 4351 (CR) in caso di fase = predisposizione consuntivo devo controllare se l'impegno è competenza o residuo e se l'importo della modifica è meno o più  
			// cambiano poi anche le causali di scrittura prima nota
			boolean registraPerAnnoBilancioCorrente = annoBilancioRequest == fromUpdate.getAnnoMovimento();
			boolean residuo = annoBilancioRequest > fromUpdate.getAnnoMovimento();
			boolean pluriennale = fromUpdate.getAnnoMovimento() > annoBilancioRequest ;
			boolean registraPerPredisposizioneConsuntivo = false;
			
			boolean registraPerModificaImportoPluriennale = pluriennale && fromUpdate.getAnnoScritturaEconomicoPatrimoniale() != null;
			
			//	  SIAC-5529 leggo lo stato dopo degli aggiornamenti, 
			//    per capire se e' cambiato in definitivo:
			DatiOperazioneDto datiOperazioneAvanzaTs = impegnoOttimizzatoDad.avanzaAClockTime(datiOperazione);
			String statoCodNew = impegnoOttimizzatoDad.getStatoCode(fromUpdate.getUid(), datiOperazioneAvanzaTs );
			boolean passaggioAStatoDefinitivo = CommonUtils.passaggioAStatoDefinitivo(statoCodOld, statoCodNew);
			//
			
			String codiceFaseBilancio= impegnoOttimizzatoDad.caricaCodiceBilancio(datiOperazione, annoBilancioRequest);
			if (!StringUtils.isEmpty(codiceFaseBilancio) && Constanti.BIL_FASE_OPERATIVA_PREDISPOSIZIONE_CONSUNTIVO.equals(codiceFaseBilancio)) {
				registraPerPredisposizioneConsuntivo = true;
			}
			
			if(passaggioAStatoDefinitivo){
				//SIAC-6000
				boolean saltaInserimentoPrimaNota = req.isSaltaInserimentoPrimaNota();
				EsitoAttivaRegistrazioniMovFinFINGSADto esitoReg = gestisciRegistrazioneGENPerImpegno(fromUpdate, TipoCollegamento.IMPEGNO, false, CodiceEventoEnum.INSERISCI_IMPEGNO.getCodice(), annoBilancioRequest, saltaInserimentoPrimaNota);
				res.setRegistrazioneMovFinFIN(esitoReg.getRegistrazioneMovFinFINInserita());
				//
			}
			
			if(registraPerAnnoBilancioCorrente || registraPerPredisposizioneConsuntivo || registraPerModificaImportoPluriennale){
				
				if(valutazioneModMov.isModificheDaCrearePresenti()){
					gestisciRegistrazioneGENModificheMovimentoSpesa(fromUpdate, null, TipoCollegamento.MODIFICA_MOVIMENTO_GESTIONE_SPESA, registraPerAnnoBilancioCorrente, registraPerPredisposizioneConsuntivo, residuo, registraPerModificaImportoPluriennale, annoBilancioRequest);
				}
							
				if(presentiModificheSpesaSuiSubDaInserire(valutazioneModMovSubs)){
					
					List<SubImpegno> subsConModifichePopolate = impegnoOttimizzatoDad.caricaModificheSubImpegni(richiedente, Integer.toString(annoBilancioRequest), impegnoDaAggiornare.getAnnoMovimento(), impegnoDaAggiornare.getNumero(), datiOperazione);
					
					for (SubImpegno sub : fromUpdate.getElencoSubImpegni()) {
						
						//Giugno 2016 Fix per  SIAC-3653
						SubImpegno subConModifichePopolate = ModelUtils.getSubImpByNumero(subsConModifichePopolate, sub.getNumero());
						sub.setListaModificheMovimentoGestioneSpesa(((SubImpegno)subConModifichePopolate).getListaModificheMovimentoGestioneSpesa());
						
						
						if(!StringUtils.isEmpty(sub.getListaModificheMovimentoGestioneSpesa())
								&& numeriSubCoinvolitiNelleMod!=null && numeriSubCoinvolitiNelleMod.contains(sub.getNumero())){
							gestisciRegistrazioneGENModificheMovimentoSpesa(fromUpdate, sub, TipoCollegamento.MODIFICA_MOVIMENTO_GESTIONE_SPESA, registraPerAnnoBilancioCorrente , registraPerPredisposizioneConsuntivo, residuo, registraPerModificaImportoPluriennale, annoBilancioRequest);
						}
					}
				}
			}
			
				
			//SIAC-6249: 
//			if(registraPerAnnoBilancioCorrente || registraPerModificaImportoPluriennale){	
				if(esito.isSubInseriti()) {
					for (SubImpegno sub: fromUpdate.getElencoSubImpegni()) {
					 
						//Solo se il sub iterato e' stato inserito o modificato:
						if(!CommonUtils.contenutoInLista(subInvariati, sub.getUid())){
							
							// fix per SIAC-5529 e SIAC-5980:
							String statoCodNewSub = impegnoOttimizzatoDad.getStatoCodeByTsId(sub.getUid(), datiOperazioneAvanzaTs);
							boolean passaggioAStatoDefinitivoSub = false;
							String statoCodOldSub = determinaStatoOldSub(subDaInserireOModificareConStatoOld, sub);
							passaggioAStatoDefinitivoSub = CommonUtils.passaggioAStatoDefinitivo(statoCodOldSub, statoCodNewSub);
							//
							
							if(passaggioAStatoDefinitivoSub){
								//SIAC-5937
								EsitoAttivaRegistrazioniMovFinFINGSADto esitoDto = gestisciRegistrazioneGENPerSubImpegno(fromUpdate, sub, TipoCollegamento.SUBIMPEGNO, true, CodiceEventoEnum.INSERISCI_SUBIMPEGNO.getCodice(), annoBilancioRequest, req.isSaltaInserimentoPrimaNotaSuSub(), registraPerAnnoBilancioCorrente);
								res.setRegistrazioneMovFinFIN(esitoDto.getRegistrazioneMovFinFINInserita());
							}
						}
						
					}
				}
			
			//costruisco la response esito OK:			
			res.setImpegno(fromUpdate);
			res.setEsito(Esito.SUCCESSO);
		}
	}
	
	private String determinaStatoOldSub(List<SubImpegno> subDaInserireOModificareConStatoOld,SubImpegno sub){
		SubImpegno subPrima = CommonUtils.getById(subDaInserireOModificareConStatoOld, sub.getUid());
		String statoCodOldSub = null;
		if(subPrima!=null){
			// si tratta di un sub modificato
			statoCodOldSub = subPrima.getStatoOperativoMovimentoGestioneSpesa();
		}
		return statoCodOldSub;
	}
	
	
	private void ricaricaStatoCod(List<SubImpegno> subDaInserireOModificareConStatoOld,DatiOperazioneDto datiOperazione){
		if(!isEmpty(subDaInserireOModificareConStatoOld)){
			for(SubImpegno it: subDaInserireOModificareConStatoOld){
				if(it!=null && it.getUid()>0){
					//per quelli modificati ricarico lo stato
					String statoCodOldSub = accertamentoOttimizzatoDad.getStatoCodeByTsId(it.getUid(), datiOperazione);
					it.setStatoOperativoMovimentoGestioneSpesa(statoCodOldSub);
				}
			}
		}
	}
	
	private List<BigDecimal> getNumeriSubCoinvoltiNelleModifiche(List<ModificaMovimentoGestioneSpesaInfoDto> valutazioneModMovSubs){
		 List<BigDecimal> numeriSubCoinvoliti = new ArrayList<BigDecimal>();
		 if(!StringUtils.isEmpty(valutazioneModMovSubs)){
			 for(ModificaMovimentoGestioneSpesaInfoDto it : valutazioneModMovSubs){
				 if(it!=null){
					 List<ModificaMovimentoGestioneSpesa> modDaAggiornare = it.getModificheDaAggiornare();
					 List<ModificaMovimentoGestioneSpesa> modDaCreare = it.getModificheDaCreare();
					 List<ModificaMovimentoGestioneSpesa> modResidue = it.getModificheResidue();
					 
					 List<ModificaMovimentoGestioneSpesa> tutte = CommonUtils.addAllConNew(CommonUtils.addAllConNew(modDaAggiornare, modDaCreare),modResidue);
					 
					 if(!StringUtils.isEmpty(tutte)){
						 
						 for(ModificaMovimentoGestioneSpesa modIt : tutte){
							 if(modIt!=null && modIt.getNumeroSubImpegno()!=null && !numeriSubCoinvoliti.contains(new BigDecimal(modIt.getNumeroSubImpegno()))){
								 numeriSubCoinvoliti.add(new BigDecimal(modIt.getNumeroSubImpegno()));
							 }
						 }
						 
					 }
					 
				 }
			 }
		 }
		 return numeriSubCoinvoliti;
	}
	
	private ImpegnoInModificaInfoDto nuovaValutazioneSubPerGestirePaginazione(Impegno impegnoDaAggiornare, Richiedente richiedente,String annoBilancio,
			ImpegnoInModificaInfoDto impegnoInModificaInfoDto,List<SubImpegno> subImpegniDaEliminare,List<SubImpegno> subImpegniDaInserireOModificare, DatiOperazioneDto datiOperazione){
		//FEBBRAIO 2016, nuovo comportamento, viene INTERDETTA la possibilita' di eliminare i SUB IMPEGNI TRAMITE
		//LA VECCHIA MODALITA'. Prima venivano eliminati tutti i sub non ricevuti dal front end.
		//Ora non e' piu' possibile ragionare cosi: Il motivo e' la paginazione dei sub.
		
		long start_A = System.currentTimeMillis();
		
		EsitoCompilaListaSubImpegniConTuttiGliIdsDto esitoCompilaSub = compilaListaSubImpegniConTuttiGliIds(datiOperazione,impegnoDaAggiornare, richiedente, annoBilancio);
		List<SubImpegno> subImpegniDaFeConAggiuntiINonPresenti= esitoCompilaSub.getSubImpegniDaFeConAggiuntiINonPresenti();
		impegnoDaAggiornare.setElencoSubImpegni(subImpegniDaFeConAggiuntiINonPresenti);
		impegnoInModificaInfoDto.setTuttiISubSoloGliIds(esitoCompilaSub.getTuttiISubSoloGliIds());
		impegnoInModificaInfoDto.setOttimizzazioneMovGest(esitoCompilaSub.getOttimizzazioneMovGest());
		//
		
		long end_A = System.currentTimeMillis();
		long start_B = System.currentTimeMillis();
		
		//passiamo solo i sub strattamente ricevuti dal front end:
		Impegno impegnoClone = clone(impegnoDaAggiornare);
		impegnoClone.setElencoSubImpegni(subImpegniDaInserireOModificare);
		//
		
		impegnoInModificaInfoDto =impegnoOttimizzatoDad.valutaSubImp(impegnoClone, impegnoInModificaInfoDto, datiOperazione, bilancio);
		
		long end_B = System.currentTimeMillis();
		long start_C = System.currentTimeMillis();
		
		//QUELLI NON RICEVUTI DA FRONT-END NON VENGONO PIU' ELIMINATI MA IMPOSTATI COME INVARIATI:
		SubMovgestInModificaInfoDto valutati = riValutaSoloIVeramenteModificati( impegnoInModificaInfoDto.getInfoSubValutati(), esitoCompilaSub,subImpegniDaInserireOModificare);
		//
		
		long end_C = System.currentTimeMillis();
		long start_D = System.currentTimeMillis();
		
		//GLI ELIMINATI VENGONO ESPRESSAMENTE INDICATI IN UNA LISTA A PARTE:
		valutati = impostaSubDaEliminare(valutati, subImpegniDaEliminare);
		//
		
		long end_D = System.currentTimeMillis();
		
		long totA = end_A - start_A;
		long totB = end_B - start_B;
		long totC = end_C - start_C;
		long totD = end_D - start_D;
		
		CommonUtils.println("totA: " + totA + " - totB: " + totB + " - totC: " + totC + " - totD: " + totD);
		
		impegnoInModificaInfoDto.setInfoSubValutati(valutati);
		
		return impegnoInModificaInfoDto;
		//
	}
	
	/**
	 * Dal front end ricevo una lista di sub relativa alla sola pagina di sub che sto navigando
	 * 
	 * per evitare che il servizio di aggiorna elimini i sub non indicati vengono aggiunti gli ids dei sub delle altre pagine
	 * 
	 * @param impegnoDaAggiornare
	 * @param richiedente
	 * @param annoBilancio
	 * @param tipoMovimento
	 * @return
	 */
	private EsitoCompilaListaSubImpegniConTuttiGliIdsDto compilaListaSubImpegniConTuttiGliIds(DatiOperazioneDto datiOperazione, Impegno impegnoDaAggiornare,Richiedente richiedente,String annoBilancio){
		
		EsitoCompilaListaSubImpegniConTuttiGliIdsDto esito = new EsitoCompilaListaSubImpegniConTuttiGliIdsDto();
		
		
		if(impegnoDaAggiornare!=null && richiedente!=null && annoBilancio!=null){
			
			//FEBBRAIO 2016, nuovo comportamento, viene INTERDETTA la possibilita' di eliminare i SUB IMPEGNI TRAMITE
			//QUESTO SERVIZIO. Il motivo e' la paginazione dei sub.
			
			String tipoMovimento = Constanti.MOVGEST_TIPO_IMPEGNO;
			
			List<SubImpegno> subImpegniDaFe = (List<SubImpegno>) ((Impegno)impegnoDaAggiornare).getElencoSubImpegni();
			
			//DATI OPZIONALI:
			DatiOpzionaliElencoSubTuttiConSoloGliIds datiOpzionali = new DatiOpzionaliElencoSubTuttiConSoloGliIds();
			datiOpzionali.setCaricaDisponibileLiquidareEDisponibilitaInModifica(true);
			//SIAC-5785 - servono anche:
			datiOpzionali.setCaricaCig(true);
			datiOpzionali.setCaricaCup(true);
			//
			
			//
			OttimizzazioneMovGestDto ottimizzazioneMovGest = new OttimizzazioneMovGestDto();
			List<SubImpegno> tuttiISubSoloGliIds = (List<SubImpegno>) impegnoOttimizzatoDad.caricaElencoIdsSubMovimenti(datiOperazione,richiedente, ente, annoBilancio, impegnoDaAggiornare.getAnnoMovimento(),impegnoDaAggiornare.getNumero(), tipoMovimento,ottimizzazioneMovGest ,datiOpzionali);
			esito.setOttimizzazioneMovGest(ottimizzazioneMovGest);
			//
			
			List<SubImpegno> elencoIdsSubNonPresentiDaFe = new ArrayList<SubImpegno>();
			
			
			List<SubImpegno> subImpegniDaFeConAggiuntiINonPresenti = new ArrayList<SubImpegno>();
			subImpegniDaFeConAggiuntiINonPresenti = CommonUtils.addAll(subImpegniDaFeConAggiuntiINonPresenti, subImpegniDaFe);
			
			if(tuttiISubSoloGliIds!=null && tuttiISubSoloGliIds.size()>0){
				for(SubImpegno subIterato : tuttiISubSoloGliIds){
					boolean presenteDaFe = presenteByNumero(subIterato, subImpegniDaFe);
					if(!presenteDaFe){
						elencoIdsSubNonPresentiDaFe.add(subIterato);
					}
				}
			}
			subImpegniDaFeConAggiuntiINonPresenti = CommonUtils.addAll(subImpegniDaFeConAggiuntiINonPresenti, elencoIdsSubNonPresentiDaFe);
			//
			
			esito.setElencoIdsSubNonPresentiDaFe(elencoIdsSubNonPresentiDaFe);
			esito.setSubImpegniDaFeConAggiuntiINonPresenti(subImpegniDaFeConAggiuntiINonPresenti);
			esito.setTuttiISubSoloGliIds(tuttiISubSoloGliIds);
			
		}
		
		return esito;
	}
	
	
	private SubMovgestInModificaInfoDto riValutaSoloIVeramenteModificati(SubMovgestInModificaInfoDto valutati,
			EsitoCompilaListaSubImpegniConTuttiGliIdsDto esitoCompilaSub,List<SubImpegno> subImpegniDaInserireOModificare){
		//FEBBRAIO 2016, nuovo comportamento, viene INTERDETTA la possibilita' di eliminare i SUB IMPEGNI TRAMITE
		//QUESTO SERVIZIO. Il motivo e' la paginazione dei sub.
		
		if(valutati!=null && esitoCompilaSub!=null){
			
			List<SubImpegno> elencoIdsSubNonPresentiDaFe = esitoCompilaSub.getElencoIdsSubNonPresentiDaFe();
			
			ArrayList<SubImpegno> daModificare = valutati.getSubImpegniDaModificare();
			ArrayList<SubImpegno> daModificareVeramente = new ArrayList<SubImpegno>();
			ArrayList<SubImpegno> nonSonoDaModificare = new ArrayList<SubImpegno>();
			if(daModificare!=null && daModificare.size()>0){
				for(SubImpegno daModIt : daModificare){
					boolean nonRicevutoDaFe = presenteByNumero(daModIt, elencoIdsSubNonPresentiDaFe);
					if(nonRicevutoDaFe){
						//NON E' VERAMENTE DA MODIFICARE
						nonSonoDaModificare.add(daModIt);
					} else {
						//VERAMENTE DA MODIFICARE
						daModificareVeramente.add(daModIt);
					}
				}
			}
			
			//RIVEDIAMO LA VALUTAZIONE:
			valutati.setSubImpegniDaModificare(daModificareVeramente);
			
			ArrayList<SubImpegno> invariati = valutati.getSubImpegniInvariati();
			ArrayList<SubImpegno> invariatiRicostruita = new ArrayList<SubImpegno>();
			invariatiRicostruita = (ArrayList<SubImpegno>) CommonUtils.addAll(invariatiRicostruita, invariati);
			if(nonSonoDaModificare!=null && nonSonoDaModificare.size()>0){
				for(SubImpegno subIt : nonSonoDaModificare){
					boolean giaPresente = presenteByNumero(subIt, invariati);
					if(!giaPresente){
						invariatiRicostruita.add(subIt);
					}
				}
			}
			
			//CICLIAMO SU TUTTI:
			if(esitoCompilaSub.getTuttiISubSoloGliIds()!=null && esitoCompilaSub.getTuttiISubSoloGliIds().size()>0){
				for(SubImpegno subIT : esitoCompilaSub.getTuttiISubSoloGliIds()){
					//se non e' espressamente indicato come da modificare:
					if(!presenteByNumero(subIT, subImpegniDaInserireOModificare)){
						//per evitare doppi inserimenti:
						if(!presenteByNumero(subIT, invariatiRicostruita)){
							invariatiRicostruita.add(subIT);
						}
						//
					}
					
				}
			}
			
			
			
			valutati.setSubImpegniInvariati(invariatiRicostruita);
			
		}
		
		return valutati;
		//
	}
	
	/**
	 * imposta i sub da eliminare
	 * rimuove dalla lista invariati i sub da eliminare
	 * @param valutati
	 * @param subImpegniDaEliminare
	 * @return
	 */
	private SubMovgestInModificaInfoDto impostaSubDaEliminare(SubMovgestInModificaInfoDto valutati,List<SubImpegno> subImpegniDaEliminare){
		
		//La nuova modalita' per eliminare i sub prevede che vengano espressamente indicati:
		
		//in valutati potrei avere degli impegni settati come "da eliminare" ma che sono semplicemente quelli invariati:
		valutati.setSubImpegniDaEliminare(new ArrayList<SubImpegno>());
		//
		
		if(subImpegniDaEliminare!=null && subImpegniDaEliminare.size()>0){
			
			ArrayList<SubImpegno> invariati = valutati.getSubImpegniInvariati();
			ArrayList<SubImpegno> invariatiRicostruiti = new ArrayList<SubImpegno>();
			
			for(SubImpegno iterato : invariati){
				if(iterato!=null){
					SubImpegno daEliminare = trovaByNumero(iterato, subImpegniDaEliminare);
					if(daEliminare==null){
						invariatiRicostruiti.add(iterato);
					}
				}
			}
			
			List<SiacTMovgestTsFin> oldSubs = valutati.getSubImpegniOld();
			ArrayList<SiacTMovgestTsFin> subImpegniDaEliminareRicostruiti = new ArrayList<SiacTMovgestTsFin>();
			
			for(SubImpegno iterato : subImpegniDaEliminare){
				if(iterato!=null){
					SiacTMovgestTsFin trovato = trovaByNumero(iterato.getNumero(), oldSubs);
					if(trovato!=null){
						subImpegniDaEliminareRicostruiti.add(trovato);
					}
				}
			}
			
			valutati.setSubImpegniDaEliminare(subImpegniDaEliminareRicostruiti);
			valutati.setSubImpegniInvariati(invariatiRicostruiti);
			
		}
		
		return valutati;
	}
	
	
	private boolean presenteByNumero(SubImpegno sub, List<SubImpegno> lista){
		boolean presenteInLista = false;
		SubImpegno trovato = trovaByNumero(sub, lista);
		if(trovato!=null){
			presenteInLista = true;
		}
		return presenteInLista;
	}
	
	private SubImpegno trovaByNumero(SubImpegno sub, List<SubImpegno> lista){
		SubImpegno trovato = null;
		if(sub!=null && lista!=null && lista.size()>0){
			for(SubImpegno subFeIterato : lista){
				if(subFeIterato!=null && subFeIterato.getNumero()!=null && sub.getNumero()!=null 
						&& subFeIterato.getNumero().intValue()==sub.getNumero().intValue()){
					trovato = subFeIterato;
					break;
				}
			}
		}
		return trovato;
	}
	
	private SiacTMovgestTsFin trovaByNumero(BigDecimal numero, List<SiacTMovgestTsFin> lista){
		SiacTMovgestTsFin trovato = null;
		if(numero!=null && lista!=null && lista.size()>0){
			for(SiacTMovgestTsFin subFeIterato : lista){
				if(subFeIterato!=null && subFeIterato.getMovgestTsCode()!=null){
					BigDecimal numeroIterato = new BigDecimal(subFeIterato.getMovgestTsCode());
					if(numeroIterato.intValue()==numero.intValue()){
						trovato = subFeIterato;
						break;
					}
				}
			}
		}
		return trovato;
	}
	
	/**
	 * Raggruppa tutti i controlli relativi al servizio Gestisce Modifica Movimento Spesa 
	 * @param valutazioneModMov
	 * @param valutazioneModMovSubs
	 * @param cacheAttoAmm
	 * @param datiOperazione
	 * @param impegnoInModificaInfoDto
	 * @param capitoliInfo
	 * @return
	 */
	private EsitoControlliDto controlliAggiornaModificheMovimento(ModificaMovimentoGestioneSpesaInfoDto valutazioneModMov,List<ModificaMovimentoGestioneSpesaInfoDto> valutazioneModMovSubs,
			HashMap<String, AttoAmministrativo> cacheAttoAmm, DatiOperazioneDto datiOperazione, ImpegnoInModificaInfoDto impegnoInModificaInfoDto, CapitoliInfoDto capitoliInfo){
		
		EsitoControlliDto esito = new EsitoControlliDto();
		
		List<Errore> listaErrori = new ArrayList<Errore>();
		
		Impegno accertamentoDaAggiornare = req.getImpegno();
		Richiedente richiedente = req.getRichiedente();
		Bilancio bilancio = req.getBilancio();
		
		List<ModificaMovimentoGestioneSpesaInfoDto> valutazioneModMovAll = CommonUtils.toList( CommonUtils.toList(valutazioneModMov),valutazioneModMovSubs);
		
		//Bisogna assicurarsi che non ci sia piu' di una nuova modifica per acc e per sub (quelle in update non danno problemi
		//perche' per esse l'importo e' non modificabile:
		listaErrori = controlliNumeroModificheImporto(valutazioneModMov,valutazioneModMovSubs);
		
		if(listaErrori!=null && listaErrori.size()>0){
			esito.setListaErrori(listaErrori);
			return esito;
		}
		
		//Prima di chiamare i controlli devo recuperare i provvedimenti (da servizio) legati alle modifiche movimento richieste
		//per validarne lo stato e l'esistenza
		listaErrori = controlloValiditaAttiModalitaPagamento(richiedente, valutazioneModMovAll, cacheAttoAmm);
		
		if(listaErrori!=null && listaErrori.size()>0){
			esito.setListaErrori(listaErrori);
			return esito;
		}

		//altri controlli non dipendenti da servizi esterni:
		EsitoControlliDto esitoConMerito =  accertamentoOttimizzatoDad.controlliDiMeritoAggiornamentoModificaMovimentoSpesa(req.getRichiedente(), Constanti.AMBITO_FIN, req.getEnte(), bilancio, accertamentoDaAggiornare,datiOperazione,
				impegnoInModificaInfoDto,valutazioneModMov,valutazioneModMovSubs,capitoliInfo);
		if(esitoConMerito.getListaErrori()!=null && esitoConMerito.getListaErrori().size()>0){
			esito.setListaErrori(listaErrori);
			return esito;
		}
		esito.addWarning(esitoConMerito.getListaWarning());
		
		return esito;
	}
	
	@SuppressWarnings("unchecked")
	private List<Errore> controlliNumeroModificheImporto(ModificaMovimentoGestioneSpesaInfoDto valutazioneModMov,List<ModificaMovimentoGestioneSpesaInfoDto> valutazioneModMovSubs) {
		List<Errore> listaErrori = new ArrayList<Errore>();
		
		
		//1. Se si riscontrano piu di una modifica di Impegno inviare un messaggio bloccante:  
		//	<COR_ERR_0031 Aggiornamento non possibile (<entità> :  L'impegno   <ANNO/NUMERO>,  <operazione>: e' collegato a più di una modifica )>.
		//2. Se si riscontrano più di una modifica di SubImpegno (anche relativa a Sub differenti) inviare un messaggio bloccante:  
		//	<COR_ERR_0031 Aggiornamento non possibile (<entit�> : L'impegno   <ANNO/NUMERO >,  
		//		<operazione>: collegato a più di una modifica di SubImpegno )>.
		
		
		int numeroModificheDiImportoAccertamento = 0;
	
		List<ModificaMovimentoGestioneSpesa> nuoveAcc = valutazioneModMov.getModificheDaCreare();
		
		for(ModificaMovimentoGestioneSpesa nuovaAccIt : nuoveAcc){
			if(nuovaAccIt.isModificaDiImporto()){
				numeroModificheDiImportoAccertamento++;
			}
		}
		if(numeroModificheDiImportoAccertamento>1){
			listaErrori.add(ErroreFin.CAMPO_NON_ACCETTABILE.getErrore("Non si puo' indicare piu' di una nuova modifica importo alla volta"));
			return listaErrori;
		}
		
		int numeroModificheDiImportoSub = 0;
		List<ModificaMovimentoGestioneSpesa> nuoveSub = null;
		for(ModificaMovimentoGestioneSpesaInfoDto subModInfoIt : valutazioneModMovSubs){
			nuoveSub = CommonUtils.toList(nuoveSub, subModInfoIt.getModificheDaCreare());
		}
		if(nuoveSub!=null && nuoveSub.size()>0){
			for(ModificaMovimentoGestioneSpesa nuovaSubIt : nuoveSub){
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
	 *  definito nel capitolo "3.	Verifica dati trasmessi", DEL CAPITOLO 2.4.5	Operazione: Gestisce Modifica Movimento Spesa 
	 * @return
	 */
	public List<Errore> controlloValiditaAttiModalitaPagamento(Richiedente richiedente, List<ModificaMovimentoGestioneSpesaInfoDto> valutazioneModMovAll,HashMap<String, AttoAmministrativo> cacheAttoAmm){
		List<Errore> listaErrori = new ArrayList<Errore>();
		
		//Stato Atto: L'atto amministrativo da associare alla modifica deve esistere e non essere ANNULLATO (vedi Ricerca Provvedimento), 
		//in caso contrario viene emesso l'errore:
		//<FIN_ERR_0075. Stato Provvedimento non consentito (operazione = Modifica Movimento, stato = Definitivo)
		
		for(ModificaMovimentoGestioneSpesaInfoDto modIt : valutazioneModMovAll){
			List<AttiAmmModificatiGestioneSpesaInfoDto> attiDaValidare = modIt.getAttiInseritiEModificati();
			if(attiDaValidare!=null && attiDaValidare.size()>0){
				for(AttiAmmModificatiGestioneSpesaInfoDto attIt: attiDaValidare){
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
