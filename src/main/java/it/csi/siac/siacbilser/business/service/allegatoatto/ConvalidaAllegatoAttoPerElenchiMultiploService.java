/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.allegatoatto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.AsyncBaseService;
import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.AllegatoAttoDad;
import it.csi.siac.siacbilser.integration.dad.ElencoDocumentiAllegatoDad;
import it.csi.siac.siaccommonser.business.service.base.ResponseHandler;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.Messaggio;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.ConvalidaAllegatoAttoPerElenchi;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.ConvalidaAllegatoAttoPerElenchiMultiplo;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.ConvalidaAllegatoAttoPerElenchiMultiploResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.ConvalidaAllegatoAttoPerElenchiResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaAllegatoAtto;
import it.csi.siac.siacfin2ser.model.AllegatoAtto;
import it.csi.siac.siacfin2ser.model.AllegatoAttoModelDetail;
import it.csi.siac.siacfin2ser.model.ElencoDocumentiAllegato;
import it.csi.siac.siacfin2ser.model.ElencoDocumentiAllegatoModelDetail;
import it.csi.siac.siacfin2ser.model.StatoOperativoAllegatoAtto;
import it.csi.siac.siacfin2ser.model.StatoOperativoElencoDocumenti;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;

/**
 * Completa l'{@link AllegatoAtto} con tutti i suoi {@link ElencoDocumentiAllegato}
 * 
 * @author elisa
 * @version 1.0.0 - 28-02-2018
 *
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ConvalidaAllegatoAttoPerElenchiMultiploService extends CheckedAccountBaseService<ConvalidaAllegatoAttoPerElenchiMultiplo,ConvalidaAllegatoAttoPerElenchiMultiploResponse> {

	//gestione del blocco delle altre operazioni
	private static final String codiceElaborazione = "CONVALIDA_ATTO_MULTIPLO";
	private static final String CODICE_RIEPILOGO_CONVALIDA ="CONV_INFO_RIEP";
	private static final String CODICE_DETTAGLIO_CONVALIDA = "CONV_INFO_QUOTA";
	private static final String CODICE_ELAB_ELENCHI ="CONV_INFO_ELAB_ELENCHI";
	private static final String CODICE_ELAB_QUOTE = "CONV_INFO_ELAB_QUOTA";
	private static final String CODICE_ELAB_ERRORE = "CONV_ERRORE";
	private static final Map<String, String> MAPPA_CODICI_ERRORI;
	
	static {
		Map<String, String> temp = new HashMap<String, String>();
		temp.put(ErroreFin.CONVALIDA_NON_POSSIBILE.getCodice(), CODICE_ELAB_QUOTE);
		temp.put(ErroreCore.OPERAZIONE_INCOMPATIBILE_CON_STATO_ENTITA.getCodice(), CODICE_ELAB_ELENCHI);
		temp.put(ErroreFin.	DOCUMENTI_SUBORDINATI_NON_PRESENTI.getCodice(), CODICE_ELAB_ELENCHI);
		MAPPA_CODICI_ERRORI = Collections.unmodifiableMap(temp);
		temp = null;
	}

	@Autowired
	private AllegatoAttoDad allegatoAttoDad;
	@Autowired
	private ElencoDocumentiAllegatoDad elencoDocumentiAllegatoDad;

	private List<AllegatoAtto> allegatiAttoDaElaborare = new ArrayList<AllegatoAtto>();
	private List<AllegatoAtto> allegatiAttoFromRequest = new ArrayList<AllegatoAtto>();
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		boolean presenteRicercaSintatica = req.getRicercaAllegatoAtto() != null;
		boolean presenteListaAllegati = req.getUidsAllegatiAtto()!= null && !req.getUidsAllegatiAtto().isEmpty();
		checkCondition(presenteListaAllegati || presenteRicercaSintatica, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("lista allegato atto"), true);
		checkNotNull(req.getFlagConvalidaManuale(), "flag convalida", false);
	}
	
	@Override
	protected void init() {
		super.init();
		
		allegatoAttoDad.setEnte(ente);
		allegatoAttoDad.setLoginOperazione(loginOperazione);
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW, timeout=AsyncBaseService.TIMEOUT * 8)
	public ConvalidaAllegatoAttoPerElenchiMultiploResponse executeServiceTxRequiresNew(ConvalidaAllegatoAttoPerElenchiMultiplo serviceRequest) {
		return super.executeServiceTxRequiresNew(serviceRequest);
	}
	
	@Override
	@Transactional
	public ConvalidaAllegatoAttoPerElenchiMultiploResponse executeService(ConvalidaAllegatoAttoPerElenchiMultiplo serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void execute() {
		
		caricaListaAllegati();
		
		impostaAllegatiDaScartareOElaborare();
		
		chiamaServizioDiConvalida();
		
	}
	
	
	/**
	 * Carica la lista degli allegati da db oppure tramite ricerca sintetica
	 */
	private void caricaListaAllegati() {
		if(req.getUidsAllegatiAtto()!= null && !req.getUidsAllegatiAtto().isEmpty()) {
			// TODO: Caricare i dati base: base + model detail sul metodo del DAD, passato con array vuoto
			this.allegatiAttoFromRequest = allegatoAttoDad.findAllegatiAttoByIds(req.getUidsAllegatiAtto(), new AllegatoAttoModelDetail[0]);
			return;
		}
		
		RicercaAllegatoAtto reqRAA = req.getRicercaAllegatoAtto();
		
		ListaPaginata<AllegatoAtto> allegatiAtto = allegatoAttoDad.ricercaSinteticaAllegatoAtto(reqRAA.getAllegatoAtto(), 
				reqRAA.getElencoDocumentiAllegato(),
				reqRAA.getDataScadenzaDa(),
				reqRAA.getDataScadenzaA(),
				reqRAA.getFlagRitenute(),
				reqRAA.getSoggetto(),
				reqRAA.getImpegno(),
				reqRAA.getSubImpegno(),
				reqRAA.getStatiOperativiFiltri(),
				reqRAA.getListaAttoAmministrativo(),
				reqRAA.getBilancio(),
				ParametriPaginazione.TUTTI_GLI_ELEMENTI,
				new AllegatoAttoModelDetail[0]);
		this.allegatiAttoFromRequest = allegatiAtto;
		
		if(this.allegatiAttoFromRequest == null || this.allegatiAttoFromRequest.isEmpty()) {
			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA_SINGOLO_MSG.getErrore("lista di allegati richiesta"));
		}
		
	}

	/**
	 * Decide in base a parametri base quali degli allegati atti richiesti possa essere completato inserendoli nella lista allegatiAttoDaElaborare.
	 * Per gli allegati che non superano questi controlli di base viene inserito un messaggio in response con la motivazione nello scarto 
	 * e viene popolata la lista allegatiattoiscartati.
	 */
	private void impostaAllegatiDaScartareOElaborare() {
		List<AllegatoAtto> allegati = new ArrayList<AllegatoAtto>();
		for(AllegatoAtto aa : this.allegatiAttoFromRequest) {
			
			if(scartaAllegatoAtto(aa)) {
				res.getAllegatiScartati().add(aa);
				continue;
			}
			
			allegati.add(aa);
		}
		this.allegatiAttoDaElaborare = allegati;
	}

	/**
	 * Chiama servizio CompleataAllegatoAttoService per tutti gli allegati presenti nella lista allegatiAttoDaElaborare.
	 * Gestisce inoltre la response di tale servizio.
	 */
	private void chiamaServizioDiConvalida() {
		
		for(AllegatoAtto all : this.allegatiAttoDaElaborare) {
			caricaElenchiConvalidabiliInAllegato(all);
			convalidaSingoloAttoAllegato(all);
		}
		
	}

	/**
	 * @param all l'allegato di cui si devono caricare gli elenchi
	 */
	private void caricaElenchiConvalidabiliInAllegato(AllegatoAtto all) {
		List<ElencoDocumentiAllegato> elenchiDocumentiAllegato = caricaElenchiAllegatoAtto(all);
		all.setElenchiDocumentiAllegato(elenchiDocumentiAllegato);
	}

	private List<ElencoDocumentiAllegato> caricaElenchiAllegatoAtto(AllegatoAtto all) {
		return elencoDocumentiAllegatoDad.findElenchiByAllegatoAttoAndStatoElenco(all, StatoOperativoElencoDocumenti.COMPLETATO, new ElencoDocumentiAllegatoModelDetail[0]);
	}

	/**
	 * Chiama il servizio {@link CompletaAllegatoattoService} per l'allegato fornito in input. 
	 * Imposta nella response {@link CompletaAllegatoattoMultiplorReponse} gli eventuali errori di tale servizio tramite la classe {@link CompletaAllegatoAttoSingoloResponseHandler}
	 *
	 * @param all the all
	 */
	private void convalidaSingoloAttoAllegato(AllegatoAtto all) {
		ConvalidaAllegatoAttoPerElenchi reqCAA = new ConvalidaAllegatoAttoPerElenchi();
		
		reqCAA.setFlagConvalidaManuale(req.getFlagConvalidaManuale());
		
		// Creo l'allegato per la request
		reqCAA.setAllegatoAtto(all);
		
		// Creo gli elenchi a partire dagli uid
		reqCAA.setRichiedente(req.getRichiedente());

		//TODO: chiedere  in CSI, magari non vogliono la nuova transazione
		serviceExecutor.executeServiceTxRequiresNew(ConvalidaAllegatoAttoPerElenchiService.class, reqCAA, new ConvalidaAllegatoAttoPerElenchiSingoloResponseHandler());  
	}

	
	/**
	 * Decide se un allegato atto &egrave; da scartare o meno.
	 *
	 * @param allegatoAtto the allegato atto
	 * @return true se l'allegato atto deve essere scartato, false altrimenti.
	 */
	private boolean scartaAllegatoAtto(AllegatoAtto allegatoAtto) {
		//TODO: valutare se togliere controllo sullo stato
		return !isStatoOperativoAllegatoAttoCorretto(allegatoAtto);
	}
	
	/**
	 * Controllo lo stato operativo dell'allegato atto.
	 * <br>
	 * L'allegato deve essere in stato DA COMPLETARE, in caso contrario si invia il messaggio &lt;FIN_ERR_0226, Stato Allegato atto incongruente&gt;.
	 * <br>
	 * Verifica se &eacute; possibile annullare l'Allegato Atto controllando il diagramma degli stati dell'atto,
	 * se non &eacute; possibile segnala il messaggio &lt;FIN_ERR_0226, Stato Allegato Atto incongruente&gt;.
	 */
	protected boolean  isStatoOperativoAllegatoAttoCorretto(AllegatoAtto allegato) {
		boolean statoConvalidabile = StatoOperativoAllegatoAtto.COMPLETATO.equals(allegato.getStatoOperativoAllegatoAtto()) || StatoOperativoAllegatoAtto.PARZIALMENTE_CONVALIDATO.equals(allegato.getStatoOperativoAllegatoAtto());
		if(!statoConvalidabile) {
			impostaInResponseMessaggioDiScarto(allegato, "Stato non valido: " + allegato.getStatoOperativoAllegatoAtto() + ". Atteso uno dei seguenti: " + StatoOperativoAllegatoAtto.COMPLETATO + " , " + StatoOperativoAllegatoAtto.PARZIALMENTE_CONVALIDATO);
		}
		return statoConvalidabile;
	}
	
	
	/**
	 * Imposta in response il messaggio messaggio di scarto.
	 *
	 * @param aa the aa
	 * @param motivazioneScarto the motivazione scarto
	 */
	private void impostaInResponseMessaggioDiScarto(AllegatoAtto aa, String motivazioneScarto) {
		Messaggio m = new Messaggio();
		m.setCodice(codiceElaborazione);
		m.setDescrizione(creaStringaAllegatoAttoScartato(aa != null? aa : null, motivazioneScarto));
		res.addMessaggio(m);
	}
	
	
	/**
	 * Crea una stringa che presenti i dati minimi dell'atto e la motivazione per cui 6egrave; stato scartato.
	 * Esempio di strnga creata: "Atto 2017/5-0 scartato con la seguente motivazione: atto non utilizzabile "
	 *
	 * @param aa the aa l'allegato atto scartato
	 * @param motivazioneScarto la motivazione dello scarto
	 * @return  la stringa creata
	 */
	private String creaStringaAllegatoAttoScartato(AllegatoAtto aa, String motivazioneScarto) {
		final String methodName = "creaStringaAllegatoAttoScartato";
		StringBuilder sb = new StringBuilder();
		StringBuilder sbMotivazione = new StringBuilder();
		if(StringUtils.isNotBlank(motivazioneScarto)) {
			sbMotivazione.append( " con la seguente motivazione: ")
			.append(motivazioneScarto);
		}
		if(aa == null) {
			log.info(methodName, "Non posso creare una descrizione per l'allegato perche' e' null");
			sb.append("allegato scartato ")
				.append(sbMotivazione.toString());
		}else {
			sb.append(creaDescrizioneAllegato(aa))
				.append(" scartato ")
				.append(sbMotivazione.toString());
		}
		return sb.toString();
		
	}

	/**
	 * Imposta nella response un messaggio di operazione conclusasi correttamente per l'allegato atto passato in input.
	 *
	 * @param aa l'allegato la cui elaborazione &egrave; avvenuta correttamente.
	 */
	private void impostaMessaggioOperazioneAvvenutaCorrettamente(AllegatoAtto aa) {
		StringBuilder sb = new StringBuilder();
		sb.append("Elaborazione per ")
			.append(creaDescrizioneAllegato(aa))			
			.append(" conclusasi correttamente.");
		log.debug("stampaDettaglioOperazione", sb.toString());
		Messaggio m = new Messaggio();
		m.setCodice(codiceElaborazione);
		m.setDescrizione(sb.toString());
		res.addMessaggio(m);
	}
	
	/**
	 * Crea una stringa con i parametri che definiscono l'allegato atto
	 *
	 * @param aa l'atto allegato di cui si vuole la descrizione
	 * @return la stringa creata
	 */
	private String creaDescrizioneAllegato(AllegatoAtto aa) {
		StringBuilder desc = new StringBuilder();
		if(aa == null) {
			return "";
		}
		desc.append("Atto ")
		.append((aa.getAttoAmministrativo() != null && aa.getAttoAmministrativo().getAnno() != 0) ? aa.getAttoAmministrativo().getAnno() : " ")
		.append("/")
		.append((aa.getAttoAmministrativo() != null && aa.getAttoAmministrativo().getNumero() != 0) ? aa.getAttoAmministrativo().getNumero() : " ")
		.append("-")
		.append(aa.getVersioneInvioFirmaNotNull());
		return desc.toString();
	}
	
	/**
	 * @param allegatoElaborato
	 * @param erroriVeri
	 */
	private void impostaMessaggioDiScartoFromErrore(AllegatoAtto allegatoElaborato, List<String> erroriVeri) {
		if(erroriVeri == null || erroriVeri.isEmpty()) {
			return;
		}
		impostaInResponseMessaggioDiScarto(allegatoElaborato, StringUtils.join(erroriVeri, ", "));
	}
	
	private void impostaMessaggioErroriSuQuoteEdElenchi(AllegatoAtto aa, List<String> listElenchi, List<String> listQuote) {
		boolean presentiErroriElenco = listElenchi != null  && !listElenchi.isEmpty();
		boolean presentiErroriQuote = listQuote != null && !listQuote.isEmpty();
		if(!presentiErroriElenco && !presentiErroriQuote) {
			return;
		}
		StringBuilder sb = new StringBuilder();
		sb.append(creaDescrizioneAllegato(aa));
		if(presentiErroriElenco) {
			sb.append(". Presenti i seguenti errori in alcuni degli elenchi: ")
			.append(StringUtils.join(listElenchi, ", "));
		}
		if(presentiErroriQuote) {
			sb.append(". Presenti i seguenti errori in alcuni dei subdocumenti degli elenchi: ")
			.append(StringUtils.join(listQuote, ", "));
		}
		Messaggio m = new Messaggio();
		m.setCodice(codiceElaborazione);
		m.setDescrizione(sb.toString());
		res.addMessaggio(m);
	}
	
	/**
	 * Separa errori by causa.
	 *
	 * @param errori the errori
	 * @return the map
	 */
	private Map<String, List<String>> separaErroriByCausa(List<Errore> errori) {
		Map<String, List<String>> mappaErrori = new HashMap<String, List<String>>();
		for (Errore errore : errori) {
			String key = MAPPA_CODICI_ERRORI.get(errore.getCodice()) != null ? MAPPA_CODICI_ERRORI.get(errore.getCodice()) : CODICE_ELAB_ERRORE;
			if(mappaErrori.get(key) == null) {
				mappaErrori.put(key, new ArrayList<String>());
			}
			mappaErrori.get(key).add(errore.getDescrizione());
		}
		return mappaErrori;
	}
	/**
	 * Cra un messaggio univoco a partire da n messaggi
	 * @param allegatoElaborato
	 * @param messaggi
	 */
	private void impostaInResponseUnicoMessaggioDaMessaggi(AllegatoAtto allegatoElaborato, List<Messaggio> messaggi) {
		if( messaggi == null || messaggi.isEmpty()) {
			return;
		}
		Map<String, List<String>> mappa = new HashMap<String, List<String>>();
		mappa.put(CODICE_RIEPILOGO_CONVALIDA, new ArrayList<String>());
		mappa.put(CODICE_DETTAGLIO_CONVALIDA, new ArrayList<String>());
		
		for (Messaggio msg : messaggi) {
			mappa.get(msg.getCodice()).add(msg.getDescrizione());
		}
		
		List<String> riepilogos = mappa.get(CODICE_RIEPILOGO_CONVALIDA);
		List<String> dettaglios = mappa.get(CODICE_DETTAGLIO_CONVALIDA);
		StringBuilder sb = new StringBuilder();
		sb.append(creaDescrizioneAllegato(allegatoElaborato))
		.append( " : ")
		.append(riepilogos != null && !riepilogos.isEmpty()? StringUtils.join(riepilogos, ", ") : "")
		.append(". Dettaglio operazione: ")
		.append(dettaglios != null && !dettaglios.isEmpty()? StringUtils.join(dettaglios, ", ") : "");
		String descOperaz = sb.toString();
		Messaggio m = new Messaggio();
		m.setCodice(codiceElaborazione);
		m.setDescrizione(descOperaz);
		res.addMessaggio(m);

	}
	
	/**
	 * The Class CompletaAllegatoAttoSingoloResponseHandler.
	 */
	private class ConvalidaAllegatoAttoPerElenchiSingoloResponseHandler extends ResponseHandler<ConvalidaAllegatoAttoPerElenchiResponse>{
		
		
		@Override
		protected void handleResponse(ConvalidaAllegatoAttoPerElenchiResponse resEAA) {
			AllegatoAtto allegatoElaborato = resEAA.getAllegatoAtto();
			
			//la faccio qua per farla una sola volta
			if(resEAA.hasErrori()) {
				//il servizio di completamento mette insieme errori di sistema e semplici indicazioni dl fatto che una quota magari e' gia' in stato convalidato. S
				//Separo gli errori "veri" che portano ad uno scarto dell'allegto atto ed errori legati ad elenchi o quote
				Map<String, List<String>> mappaErrori = separaErroriByCausa(resEAA.getErrori());
				
				impostaMessaggioDiScartoFromErrore(allegatoElaborato, mappaErrori.get(CODICE_ELAB_ERRORE));
				
				impostaMessaggioErroriSuQuoteEdElenchi(allegatoElaborato, mappaErrori.get(CODICE_ELAB_ELENCHI), mappaErrori.get(CODICE_ELAB_QUOTE));
			}

			impostaInResponseUnicoMessaggioDaMessaggi(allegatoElaborato, resEAA.getMessaggi());
			//se sono arrivato qui, il servizio ha risposto ok
//			impostaMessaggioOperazioneAvvenutaCorrettamente(allegatoElaborato);
		}

	}

	
}
