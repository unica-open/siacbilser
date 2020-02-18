/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.business.service.movgest;

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
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siacfinser.business.service.AbstractBaseService;
import it.csi.siac.siacfinser.business.service.enumeration.CodiceEventoEnum;
import it.csi.siac.siacfinser.frontend.webservice.msg.InserisceAccertamenti;
import it.csi.siac.siacfinser.frontend.webservice.msg.InserisceAccertamentiResponse;
import it.csi.siac.siacfinser.integration.dad.CommonDad;
import it.csi.siac.siacfinser.integration.dao.common.dto.DatiOperazioneDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.EsitoAttivaRegistrazioniMovFinFINGSADto;
import it.csi.siac.siacfinser.integration.dao.common.dto.EsitoInserimentoMovimentoGestioneDto;
import it.csi.siac.siacfinser.integration.util.Operazione;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.errore.ErroreFin;
import it.csi.siac.siacgenser.model.TipoCollegamento;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class InserisceAccertamentiService extends AbstractBaseService<InserisceAccertamenti, InserisceAccertamentiResponse> {
	
	@Autowired
	CommonDad commonDad;
	
	@Autowired
	ProvvedimentoDad provvedimentoDad;
	
	@Override
	protected void init() {

	}	
	
	
	@Override
	@Transactional
	public InserisceAccertamentiResponse executeService(
			InserisceAccertamenti serviceRequest) {
		
		return super.executeService(serviceRequest);
	}	
	
	
	@Override
	public void execute() {
		final String methodName = "execute";
		log.debug(methodName, "- Begin");
		
		// mi servir� nelle scritture perch� il bilancio della request, se in doppia gestione, viene sovrascritto
		Integer annoBilancioRequest = req.getBilancio().getAnno();
		
		setBilancio(req.getBilancio());
		
		Accertamento primoAccertamentoDaInserire = req.getPrimoAccertamentoDaInserire();
		
		Integer annoScritturaEconomicoPatrimoniale = primoAccertamentoDaInserire.getAnnoScritturaEconomicoPatrimoniale();
		
		List<Accertamento> altriAccertamentiDaInserire = req.getAltriAccertamentiDaInserire();
		Ente ente = req.getRichiedente().getAccount().getEnte();
		List<Accertamento> elencoAccertamentiInseriti = new ArrayList<Accertamento>();			
		Accertamento accertamento = null;
		
		//2. Si inizializza l'oggetto DatiOperazioneDto, dto di comodo generico che verra' passato tra i metodi:
		DatiOperazioneDto datiOperazione = commonDad.inizializzaDatiOperazione(ente, req.getRichiedente(), Operazione.INSERIMENTO, bilancio.getAnno());
		
		
		//2-BIS  carico il capitolo di entrata fresco
		CapitoloEntrataGestione capEG = new CapitoloEntrataGestione();
		ListaPaginata<CapitoloEntrataGestione> listaCapitoliRestituita = getRicercaSinteticaCapitoloEntrata(req.getRichiedente(), ente, primoAccertamentoDaInserire, bilancio);
		if(null!=listaCapitoliRestituita && listaCapitoliRestituita.size()>0){
			if(listaCapitoliRestituita.size()>1){
				// errore piu di un capitolo
				res.setErrori(Arrays.asList(ErroreFin.PIU_RISULTATI__PROVV_TROVATI.getErrore("")));
				res.setElencoAccertamentiInseriti(null);
				res.setEsito(Esito.FALLIMENTO);
				return;
			}else{
				// trovato un solo capitolo in maniera corretta
				// travaso il dato nel accertamento il primo e unico elemento di capitolo
				capEG = listaCapitoliRestituita.get(0);
				primoAccertamentoDaInserire.setCapitoloEntrataGestione(capEG);
			}
		}	
		
		//3. Si invoca il metodo che esegue l'operazione interna di inserimento di impegni o accertamenti:
		Integer numeroAccertamento = null;
		if(primoAccertamentoDaInserire.getNumero()!=null && primoAccertamentoDaInserire.getNumero().intValue()>0){
			numeroAccertamento = primoAccertamentoDaInserire.getNumero().intValue();
		}
		
		if(primoAccertamentoDaInserire.getAttoAmministrativo() != null && primoAccertamentoDaInserire.getAttoAmministrativo().getUid() > 0){
			RicercaAtti ricercaAtti = new RicercaAtti();
			ricercaAtti.setUid(primoAccertamentoDaInserire.getAttoAmministrativo().getUid());
			provvedimentoDad.setLoginOperazione(loginOperazione);
			provvedimentoDad.setEnte(req.getEnte());
			List<AttoAmministrativo> listaAtti = provvedimentoDad.ricerca(ricercaAtti);
			if(listaAtti != null && !listaAtti.isEmpty()){
				AttoAmministrativo attoAmm = listaAtti.get(0);
				if(attoAmm.getBloccoRagioneria() != null && attoAmm.getBloccoRagioneria().booleanValue()){
					res.setErrori(Arrays.asList(ErroreFin.OGGETTO_BLOCCATO_DALLA_RAGIONERIA.getErrore("Numero Provvedimento " + 
							attoAmm.getNumero() + " Oggetto " + attoAmm.getOggetto())));
					res.setElencoAccertamentiInseriti(null);
					res.setEsito(Esito.FALLIMENTO);
					return;
				}
			}
		}
		
		EsitoInserimentoMovimentoGestioneDto esitoOperazioneInterna = accertamentoOttimizzatoDad.operazioneInternaInserisceAccertamento(req.getRichiedente(), ente, bilancio, primoAccertamentoDaInserire, datiOperazione,numeroAccertamento);
		
		if(esitoOperazioneInterna.getListaErrori()!=null && esitoOperazioneInterna.getListaErrori().size()>0){
			//Se l'operazione interna riporte degli errori il servizio termina qui con esito negativo.
			res.setErrori(esitoOperazioneInterna.getListaErrori());
			log.warn(methodName, "errori durante l'inserimento.");
			return;
		}

		//aggiungo l'accertamento inserito alla lista di riepilogo degli accertamenti inseriti:
		accertamento = (Accertamento)esitoOperazioneInterna.getMovimentoGestione();
		elencoAccertamentiInseriti.add(accertamento);
		
		
		//Il servizio e' predisposto per poter inserire anche degli altri movimenti oltre a quello principale:
		if(null!=accertamento && altriAccertamentiDaInserire!=null && altriAccertamentiDaInserire.size()>0){
			//si cicla sugli eventuali altri accertamenti da inserire:
			for(Accertamento altroAccertamentoDaInserire : altriAccertamentiDaInserire){
				
				// riprendo il capitolo del accertamento dal quale siamo partiti per trovare poi le disponibilita
				altroAccertamentoDaInserire.setCapitoloEntrataGestione(listaCapitoliRestituita.get(0));
				
				//per ognuno di essi si invoca la routine di operazione interna inserimento:
				EsitoInserimentoMovimentoGestioneDto esitoOperazioneInternaCiclo = accertamentoOttimizzatoDad.operazioneInternaInserisceAccertamento(req.getRichiedente(), ente, bilancio, altroAccertamentoDaInserire, datiOperazione,null);
				if(esitoOperazioneInternaCiclo.getListaErrori()!=null && esitoOperazioneInternaCiclo.getListaErrori().size()>0){
					//in caso di errore il servizio termina qui (tutto il resto e' ovviamente rollbackato perche' siamo ancora in transactional)
					res.setErrori(esitoOperazioneInternaCiclo.getListaErrori());
					return;
				}
				//aggiungo l'accertamento inserito alla lista di riepilogo degli accertamenti inseriti:
				Accertamento accertamentoAggiuntivo = (Accertamento)esitoOperazioneInternaCiclo.getMovimentoGestione();
				elencoAccertamentiInseriti.add(accertamentoAggiuntivo);
			}
		}
		
		for (Accertamento accertamentoInserito : elencoAccertamentiInseriti) {
			
		
			
			// non uso l'impegno ritornato da esitoOperazioneInterna perch� � incompleto del capitolo, contiene solo la chiave del capitolo
			// quindi per non rischiare di spaccare qualcosa uso impegnoDaRegistrare su cui setto il capitolo completo (letto prima dell'inserimento, � inutile rifare una lettura) 
			Accertamento accertamentoDaRegistrare = accertamentoInserito;
			accertamentoDaRegistrare.setCapitoloEntrataGestione(capEG);
			
			boolean competenzaEconomicoPatrimonialeEsercizioInCorso = annoScritturaEconomicoPatrimoniale != null; 
			// innesto fin-gen
			// CR SIAC-3719, se l'anno del movimento � successivo all'anno di bilancio non faccio nulla
			// A meno che --> CR-552 (siac-4943): l'utente non abbia confermato lato client che la competenza economico patrimoniale 
			// 									  del movimento � dell'esercizio in corso
			if(accertamentoDaRegistrare.getAnnoMovimento() == annoBilancioRequest || competenzaEconomicoPatrimonialeEsercizioInCorso){
				EsitoAttivaRegistrazioniMovFinFINGSADto gestisciRegistrazioneGENPerAccertamento = gestisciRegistrazioneGENPerAccertamento(accertamentoDaRegistrare, TipoCollegamento.ACCERTAMENTO, CodiceEventoEnum.INSERISCI_ACCERTAMENTO.getCodice(), annoBilancioRequest, req.isSaltaInserimentoPrimaNota());
				//SIAC-5333
				res.setRegistrazioneMovFinFIN(gestisciRegistrazioneGENPerAccertamento.getRegistrazioneMovFinFINInserita());
			}
		
		}
		
		

		// ordino con l'anno movimento + basso		
		Collections.sort(elencoAccertamentiInseriti, new Comparator<Accertamento>() {
			@Override
			public int compare(Accertamento o1, Accertamento o2) {
				return (o1.getAnnoMovimento() > o2.getAnnoMovimento()) ? 1 : -1;
			}			
		});
		//costruisco la response di ritorno:
		res.setEsito(Esito.SUCCESSO);
		res.setElencoAccertamentiInseriti(elencoAccertamentiInseriti);		
	}
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {		
		//dati di input presi da request:
		Ente ente = req.getEnte();
		Bilancio bilancio = req.getBilancio();
		Accertamento primoAccertamentoDaInserire = req.getPrimoAccertamentoDaInserire();
		List<Accertamento> altriAccertamentiDaInserire = req.getAltriAccertamentiDaInserire();
		
		if(null==primoAccertamentoDaInserire && null==bilancio && null==ente && null==altriAccertamentiDaInserire){
			checkCondition(false, ErroreCore.NESSUN_CRITERIO_RICERCA.getErrore("NESSUN PARAMETRO INIZIALIZZATO"));
		} else if(null==primoAccertamentoDaInserire){
			checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("PRIMO_ACCERTAMENTO_DA_INSERIRE"));
		} else if(null==bilancio){
			checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("BILANCIO"));
		} else if(null==ente){
			checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ENTE"));
		}
	}
	

}