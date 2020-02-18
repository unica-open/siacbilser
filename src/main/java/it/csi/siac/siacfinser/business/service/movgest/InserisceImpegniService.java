/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.business.service.movgest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacattser.model.ric.RicercaAtti;
import it.csi.siac.siacbilser.integration.dad.ProvvedimentoDad;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siacfinser.CommonUtils;
import it.csi.siac.siacfinser.business.service.AbstractBaseService;
import it.csi.siac.siacfinser.business.service.enumeration.CodiceEventoEnum;
import it.csi.siac.siacfinser.frontend.webservice.msg.InserisceImpegni;
import it.csi.siac.siacfinser.frontend.webservice.msg.InserisceImpegniResponse;
import it.csi.siac.siacfinser.integration.dad.CommonDad;
import it.csi.siac.siacfinser.integration.dad.StoricoImpegnoAccertamentoDad;
import it.csi.siac.siacfinser.integration.dao.common.dto.DatiOperazioneDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.EsitoAttivaRegistrazioniMovFinFINGSADto;
import it.csi.siac.siacfinser.integration.dao.common.dto.EsitoInserimentoMovimentoGestioneDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.ModificaVincoliImpegnoInfoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.MovimentoInInserimentoInfoDto;
import it.csi.siac.siacfinser.integration.util.Operazione;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.StoricoImpegnoAccertamento;
import it.csi.siac.siacfinser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaStoricoImpegnoAccertamento;
import it.csi.siac.siacgenser.model.TipoCollegamento;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class InserisceImpegniService extends AbstractBaseService<InserisceImpegni, InserisceImpegniResponse> {
	
	@Autowired
	CommonDad commonDad;
	
	@Autowired
	private StoricoImpegnoAccertamentoDad storicoImpegnoAccertamentoDad;
	
	@Autowired
	private ProvvedimentoDad provvedimentoDad;
	
	private Impegno impegno;
	private Impegno impegnoDaRegistrare ;
	
	
	@Override
	protected void init() {
		final String methodName = "InserisceImpegniService : init()";
		log.debug(methodName, "- Begin");
	}
	
	
	/*
	 * VARIAZIONE LINEE GUIDA 3.2.2.2
	 */
	
	@Override
	@Transactional(timeout = 2500)
	public InserisceImpegniResponse executeService(InserisceImpegni serviceRequest) {
		return super.executeService(serviceRequest);
	}	
	
	@Override
	public void execute() {
		final String methodName = "InserisceImpegniService : execute()";
		log.debug(methodName, "- Begin");
		
		Integer annoBilancioRequest = req.getBilancio().getAnno();
			
		setBilancio(req.getBilancio());
		
		
		//SIAC-6929
		if(req.getPrimoImpegnoDaInserire() != null && req.getPrimoImpegnoDaInserire().getAttoAmministrativo()!= null
				&& req.getPrimoImpegnoDaInserire().getAttoAmministrativo().getUid()!= 0 ){
			
			RicercaAtti ricercaAtti = new RicercaAtti();
			ricercaAtti.setUid(req.getPrimoImpegnoDaInserire().getAttoAmministrativo().getUid());
			provvedimentoDad.setLoginOperazione(loginOperazione);
			provvedimentoDad.setEnte(req.getEnte());
			List<AttoAmministrativo> listaAtti = provvedimentoDad.ricerca(ricercaAtti);
			if(listaAtti!= null && !listaAtti.isEmpty()){
				AttoAmministrativo atto = listaAtti.get(0);
				if(atto.getBloccoRagioneria()!= null && atto.getBloccoRagioneria().booleanValue()){
					res.setErrori(Arrays.asList(ErroreFin.OGGETTO_BLOCCATO_DALLA_RAGIONERIA.getErrore("Numero Provvedimento " + 
							atto.getNumero() + " Oggetto " + atto.getOggetto())));
					res.setElencoImpegniInseriti(null);
					res.setEsito(Esito.FALLIMENTO);
					return;
				}
			}
			
		}
		
		
		
		Impegno impegnoDaInserire = req.getPrimoImpegnoDaInserire();
		
		Integer annoScritturaEconomicoPatrimoniale = impegnoDaInserire.getAnnoScritturaEconomicoPatrimoniale();
		
		List<Impegno> altriImpegniDaInserire = req.getAltriImpegniDaInserire();
		List<Impegno> elencoImpegniInseriti = new ArrayList<Impegno>();			
		
		//2. Si inizializza l'oggetto DatiOperazioneDto, dto di comodo generico che verra' passato tra i metodi:
		DatiOperazioneDto datiOperazione = commonDad.inizializzaDatiOperazione(ente, req.getRichiedente(), Operazione.INSERIMENTO, bilancio.getAnno());
		
		//carico il capitolo letto da db
		ListaPaginata<CapitoloUscitaGestione> listaCapitoliRestituita = getRicercaSinteticaCapitoloUscita(req.getRichiedente(), ente, impegnoDaInserire, bilancio);
		
		CapitoloUscitaGestione capUG = new CapitoloUscitaGestione();

		
		if(null!=listaCapitoliRestituita && listaCapitoliRestituita.size()>0){
			
			if(listaCapitoliRestituita.size()>1){
				// errore piu di un capitolo
				res.setErrori(Arrays.asList(ErroreFin.PIU_RISULTATI__PROVV_TROVATI.getErrore("")));
				res.setElencoImpegniInseriti(null);
				res.setEsito(Esito.FALLIMENTO);
				return;
			}else{
				// trovato un solo capitolo in maniera corretta
				// travaso il dato nell'impegno (il primo e unico elemento di capitolo)
				capUG = listaCapitoliRestituita.get(0);
				impegnoDaInserire.setCapitoloUscitaGestione(capUG);
			}
			
		}
		
		
		//IN CASO DI DOPPIA GESTIONE ANTICIPIAMO IL CARICAMENTO DI DATI CHE SERVIRANNO NELLE ROUTINE DI DOPPIA GESTIONE
		//MA CHE NON SARA' POSSIBILE CARICARE INTERAMENTE DAI DAD:
		MovimentoInInserimentoInfoDto infoIns = new MovimentoInInserimentoInfoDto();
		ModificaVincoliImpegnoInfoDto infoVincoliValutati = null;
		boolean inserireDoppiaGestione = impegnoOttimizzatoDad.inserireDoppiaGestione(bilancio, impegnoDaInserire, datiOperazione);
		if(inserireDoppiaGestione){
			infoVincoliValutati = caricaInfoAccVincoliPerDoppiaGest(bilancio,datiOperazione,null,impegnoDaInserire);
			infoIns.setElencoInfoAccertamentiCoinvolti(infoVincoliValutati.getElencoInfoAccertamentiCoinvolti());
		}
		
		
		/* Se dovessero servire i capitoli partire da questo codice:
		//Inoltre dobbiamo caricare anticipatamente i capitoli degli eventuali accertamenti degli eventuali vincoli indicati:
		 HashMap<Integer, CapitoloEntrataGestione> mappaCapitoliEntrata = new HashMap<Integer, CapitoloEntrataGestione>();
		if(primoImpegnoDaInserire.getVincoliImpegno()!=null && primoImpegnoDaInserire.getVincoliImpegno().size()>0){
			for(VincoloImpegno vincoloIterato: primoImpegnoDaInserire.getVincoliImpegno()){
				if(vincoloIterato!=null && vincoloIterato.getAccertamento()!=null){
					Accertamento accVincolo = vincoloIterato.getAccertamento();
					AccertamentoDaVincolareInfoDto infoAcc = impegnoOttimizzatoDad.getDatiGeneraliAccertamentoDaVincolare(accVincolo, datiOperazione, bilancio);
					Integer chiaveCapitolo = infoAcc.getChiaveCapitolo();
					Integer chiaveCapitoloResiduo = infoAcc.getChiaveCapitoloResiduo();
				}
			}
		} */
		//
		
		//3. Si invoca il metodo che esegue l'operazione interna di inserimento di impegni o accertamenti:
		
		Integer numeroImpegno = null;
		if(impegnoDaInserire.getNumero()!=null && impegnoDaInserire.getNumero().intValue()>0){
			numeroImpegno = impegnoDaInserire.getNumero().intValue();
		}
		
		EsitoInserimentoMovimentoGestioneDto esitoOperazioneInterna = impegnoOttimizzatoDad.operazioneInternaInserisceImpegno(req.getRichiedente(), ente, bilancio, impegnoDaInserire, datiOperazione,numeroImpegno,infoIns);
		
		if(esitoOperazioneInterna.getListaErrori()!=null && esitoOperazioneInterna.getListaErrori().size()>0){
			//Se l'operazione intera riporte degli errori il servizio termina qui con esito negativo.
			res.setErrori(esitoOperazioneInterna.getListaErrori());
			res.setEsito(Esito.FALLIMENTO);
			return;
		}
		
		impegnoDaInserire.setUid(esitoOperazioneInterna.getMovimentoGestione().getUid());
		//SIAC-6702: modifiche da riaccertamento
		effettuaOperazioniSuStoricoImpegnoAccertamento(impegnoDaInserire, datiOperazione);
		
		//eventuali messaggi non blocanti da comunicare al front-end:
		res.setErrori(CommonUtils.addAll(res.getErrori(), esitoOperazioneInterna.getListaWarning()));
		
		//aggiungo l'impegno inserito alla lista di riepilogo degli impegni inseriti:
		impegno = (Impegno)esitoOperazioneInterna.getMovimentoGestione();
		
				
		elencoImpegniInseriti.add(impegno);
		
				
		//Il servizio e' predisposto per poter inserire anche degli altri movimenti oltre a quello principale:
		if(null!=impegno && altriImpegniDaInserire!=null && altriImpegniDaInserire.size()>0){
			
			for(Impegno altroImpegnoDaInserire : altriImpegniDaInserire){
				// riprendo il capitolo del impegno dal quale siamo partiti per trovare poi le disponibilita
				altroImpegnoDaInserire.setCapitoloUscitaGestione(listaCapitoliRestituita.get(0));
				
				//per ognuno di essi si invoca la routine di operazione interna inserimento:
				EsitoInserimentoMovimentoGestioneDto esitoOperazioneInternaCiclo = impegnoOttimizzatoDad.operazioneInternaInserisceImpegno(req.getRichiedente(), ente, bilancio, altroImpegnoDaInserire, datiOperazione,null,null);
				if(esitoOperazioneInternaCiclo.getListaErrori()!=null && esitoOperazioneInternaCiclo.getListaErrori().size()>0){
					//in caso di errore il servizio termina qui (tutto il resto e' ovviamente rollbackato perche' siamo ancora in transactional)
					res.setErrori(esitoOperazioneInternaCiclo.getListaErrori());
					return;
				}
				//aggiungo l'impegno inserito alla lista di riepilogo degli impegni inseriti:
				Impegno impegnoAggiuntivo = (Impegno)esitoOperazioneInternaCiclo.getMovimentoGestione();
				
				elencoImpegniInseriti.add(impegnoAggiuntivo);
				
				//SIAC-6702: modifiche da riaccertamento
				effettuaOperazioniSuStoricoImpegnoAccertamento(impegnoAggiuntivo, datiOperazione);
			}
		}

		
		// devo spostare la logica della registrazione qui! cosi che sia valida anche nel caso dei pluriennali ..aggiungo un ciclo sugli impegni da registrare!
		// non uso l'impegno ritornato da esitoOperazioneInterna xchè è incompleto del capitolo, contiene solo la chiave del capitolo
		// quindi per non rischiare di spaccare qualcosa uso impegnoDaRegistrare su cui setto il capitolo completo (letto prima dell'inserimento,  inutile rifare una lettura) 
		for (Impegno impegno : elencoImpegniInseriti) {
			
			impegnoDaRegistrare = impegno;
			impegnoDaRegistrare.setCapitoloUscitaGestione(capUG);
			
			boolean competenzaEconomicoPatrimonialeEsercizioInCorso = annoScritturaEconomicoPatrimoniale != null; 
			
			// innesto fin-gen
			// CR SIAC-3719, se l'anno del movimento è successivo all'anno di bilancio non faccio nulla
			// A meno che --> CR-552 (siac-4943): l'utente non abbia confermato lato client che la competenza economico patrimoniale 
			// 									  del movimento è dell'esercizio in corso
			if(impegnoDaRegistrare.getAnnoMovimento() ==  annoBilancioRequest || competenzaEconomicoPatrimonialeEsercizioInCorso){
				EsitoAttivaRegistrazioniMovFinFINGSADto gestisciRegistrazioneGENPerImpegno = gestisciRegistrazioneGENPerImpegno(impegnoDaRegistrare, TipoCollegamento.IMPEGNO, false, CodiceEventoEnum.INSERISCI_IMPEGNO.getCodice(), annoBilancioRequest, req.isSaltaInserimentoPrimaNota());
				res.setRegistrazioneMovFinFIN(gestisciRegistrazioneGENPerImpegno.getRegistrazioneMovFinFINInserita());
			}
		
		}
		
		
		
		// ordino con l'anno movimento + basso		
		Collections.sort(elencoImpegniInseriti, new Comparator<Impegno>() {
			@Override
			public int compare(Impegno o1, Impegno o2) {
				return (o1.getAnnoMovimento() > o2.getAnnoMovimento()) ? 1 : -1;
			}
			
		});
		
		
		res.setEsito(Esito.SUCCESSO);
		res.setElencoImpegniInseriti(elencoImpegniInseriti);
	}

	
	private void effettuaOperazioniSuStoricoImpegnoAccertamento(Impegno impegnoInserito, DatiOperazioneDto datiOperazione) {
		final String methodName = "effettuaOperazioniSuStoricoImpegnoAccertamento";
		if(impegnoInserito.getNumeroRiaccertato() == null || BigDecimal.ZERO.compareTo(impegnoInserito.getNumeroRiaccertato()) == 0 || impegnoInserito.getAnnoRiaccertato() == 0) {
			//non e' un impegno inserito in seguito ad una modifica di importo di tipo "RIAC"
			return;
		}
		List<StoricoImpegnoAccertamento> storici = caricaStoricoImpegnoAccertamento(impegnoInserito.getAnnoRiaccertato(), impegnoInserito.getNumeroRiaccertato());
		
		if(storici == null) {
			log.debug(methodName, "Non sono presenti legami storici nell'impegno di origine, non li ribalto.");
			return;
		}
		
		for (StoricoImpegnoAccertamento storicoDaRibaltareSuNuovoImpegno : storici) {
			
			log.debug(methodName, "Ribalto il legame storico con uid: " + storicoDaRibaltareSuNuovoImpegno.getUid());
			
			StoricoImpegnoAccertamento storicoRibaltato = new StoricoImpegnoAccertamento();
			storicoRibaltato.setEnte(req.getEnte());
			storicoRibaltato.setImpegno(impegnoInserito);
			storicoRibaltato.setSubImpegno(null);
			storicoRibaltato.setAccertamento(storicoDaRibaltareSuNuovoImpegno.getAccertamento());
			storicoRibaltato.setSubAccertamento(storicoDaRibaltareSuNuovoImpegno.getSubAccertamento());
			storicoImpegnoAccertamentoDad.inserisciStorico(storicoRibaltato, datiOperazione);
		}
	}


	private List<StoricoImpegnoAccertamento> caricaStoricoImpegnoAccertamento(int annoRiaccertato, BigDecimal numeroRiaccertato) {
		ParametroRicercaStoricoImpegnoAccertamento parametroRicercaStoricoImpegnoAccertamento = new ParametroRicercaStoricoImpegnoAccertamento();
		parametroRicercaStoricoImpegnoAccertamento.setBilancio(req.getBilancio());
		parametroRicercaStoricoImpegnoAccertamento.setStoricoImpegnoAccertamento(new StoricoImpegnoAccertamento());
		parametroRicercaStoricoImpegnoAccertamento.getStoricoImpegnoAccertamento().setImpegno(new Impegno());
		parametroRicercaStoricoImpegnoAccertamento.getStoricoImpegnoAccertamento().getImpegno().setAnnoMovimento(annoRiaccertato);
		parametroRicercaStoricoImpegnoAccertamento.getStoricoImpegnoAccertamento().getImpegno().setNumero(numeroRiaccertato);
		parametroRicercaStoricoImpegnoAccertamento.getStoricoImpegnoAccertamento().setEnte(req.getEnte());
		return storicoImpegnoAccertamentoDad.ricercaSinteticaStorico(ente, parametroRicercaStoricoImpegnoAccertamento, 0, Integer.MAX_VALUE);
	}


	@Override
	protected void checkServiceParam() throws ServiceParamError {		
		final String methodName = "InserisceImpegniService : checkServiceParam()";
		log.debug(methodName, "- Begin");
		
		//dati di input presi da request:
		Ente ente = req.getEnte();
		Bilancio bilancio = req.getBilancio();
		Impegno primoImpegnoDaInserire = req.getPrimoImpegnoDaInserire();
		List<Impegno> altriImpegniDaInserire = req.getAltriImpegniDaInserire();
		
		if(null==primoImpegnoDaInserire && null==bilancio && null==ente && null==altriImpegniDaInserire){
			checkCondition(false, ErroreCore.NESSUN_CRITERIO_RICERCA.getErrore("NESSUN PARAMETRO INIZIALIZZATO"));
		} else if(null==primoImpegnoDaInserire){
			checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("PRIMO_IMPEGNO_DA_INSERIRE"));
		} else if(null==bilancio){
			checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("BILANCIO"));
		} else if(null==ente){
			checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ENTE"));
		}
	}

	

}