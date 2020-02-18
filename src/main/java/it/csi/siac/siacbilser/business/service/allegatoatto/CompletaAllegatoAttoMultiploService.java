/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.allegatoatto;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.AsyncBaseService;
import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.business.utility.ElaborazioniAttiveKeyHandler;
import it.csi.siac.siacbilser.integration.dad.AllegatoAttoDad;
import it.csi.siac.siacbilser.integration.exception.ElaborazioneAttivaException;
import it.csi.siac.siacbilser.integration.utility.ElaborazioniManager;
import it.csi.siac.siacbilser.model.ElabKeys;
import it.csi.siac.siaccommonser.business.service.base.ResponseHandler;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.Messaggio;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.CompletaAllegatoAtto;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.CompletaAllegatoAttoMultiplo;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.CompletaAllegatoAttoMultiploResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.CompletaAllegatoAttoResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaAllegatoAtto;
import it.csi.siac.siacfin2ser.model.AllegatoAtto;
import it.csi.siac.siacfin2ser.model.AllegatoAttoModelDetail;
import it.csi.siac.siacfin2ser.model.ElencoDocumentiAllegato;
import it.csi.siac.siacfin2ser.model.StatoOperativoAllegatoAtto;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;

/**
 * Completa l'{@link AllegatoAtto} con tutti i suoi {@link ElencoDocumentiAllegato}
 * 
 * @author elisa
 * @version 1.0.0 - 28-02-2018
 *
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class CompletaAllegatoAttoMultiploService extends CheckedAccountBaseService<CompletaAllegatoAttoMultiplo,CompletaAllegatoAttoMultiploResponse> {

	//gestione del blocco delle altre operazioni
	private static final ElabKeys completaAllegatoKeySelector = ElabKeys.COMPLETA_ALLEGATO_ATTO;
	private static final String codiceElaborazione = "COMPLETA_ATTO_MULTIPLO";

	@Autowired
	private AllegatoAttoDad allegatoAttoDad;
	@Autowired
	private ElaborazioniManager elaborazioniManager;

	private List<AllegatoAtto> allegatiAttoDaElaborare = new ArrayList<AllegatoAtto>();
	private List<AllegatoAtto> allegatiAttoFromRequest = new ArrayList<AllegatoAtto>();
	
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		boolean presenteRicercaSintatica = req.getRicercaAllegatoAtto() != null;
		boolean presenteListaAllegati = req.getUidsAllegatiAtto()!= null && !req.getUidsAllegatiAtto().isEmpty();
		checkCondition(presenteListaAllegati || presenteRicercaSintatica, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("lista allegato atto"), true);
		checkEntita(req.getBilancio(), "bilancio", false);
	}
	
	@Override
	protected void init() {
		super.init();
		
		elaborazioniManager.init(ente, loginOperazione);
		allegatoAttoDad.setEnte(ente);
		allegatoAttoDad.setLoginOperazione(loginOperazione);
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW, timeout=AsyncBaseService.TIMEOUT * 8)
	public CompletaAllegatoAttoMultiploResponse executeServiceTxRequiresNew(CompletaAllegatoAttoMultiplo serviceRequest) {
		return super.executeServiceTxRequiresNew(serviceRequest);
	}
	
	@Override
	@Transactional
	public CompletaAllegatoAttoMultiploResponse executeService(CompletaAllegatoAttoMultiplo serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void execute() {
		
		caricaListaAllegati();
		
		impostaAllegatiDaScartareOElaborare();
		
		chiamaServizioDiCompletamento();
		
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
	private void chiamaServizioDiCompletamento() {
		
		for(AllegatoAtto all : this.allegatiAttoDaElaborare) {
			completaSingoloAttoAllegato(all);
		}
		
	}

	/**
	 * Chiama il servizio {@link CompletaAllegatoattoService} per l'allegato fornito in input. 
	 * Imposta nella response {@link CompletaAllegatoattoMultiplorReponse} gli eventuali errori di tale servizio tramite la classe {@link CompletaAllegatoAttoSingoloResponseHandler}
	 *
	 * @param all the all
	 */
	private void completaSingoloAttoAllegato(AllegatoAtto all) {
		if(!startElaborazioneAllegatoAttoEffettuataConSuccesso(all)) {
			return;
		}
		CompletaAllegatoAtto reqCAA = new CompletaAllegatoAtto();

		reqCAA.setAllegatoAtto(all);
		reqCAA.setBilancio(req.getBilancio());
		reqCAA.setRichiedente(req.getRichiedente());

		//TODO: chiedere  in CSI, magari non vogliono la nuova transazione
		serviceExecutor.executeServiceTxRequiresNew(CompletaAllegatoAttoService.class, reqCAA, new CompletaAllegatoAttoSingoloResponseHandler());
	}

	
	/**
	 * Chiudi elaborazione attiva per l'allegato atto passato in input.
	 *
	 * @param aa the aa
	 */
	private void chiudiElaborazioneAttivaAllegato(AllegatoAtto aa) {
		final String methodName = "chiudiElaborazioneAttivaAllegato";
		ElaborazioniAttiveKeyHandler eakh = new ElaborazioniAttiveKeyHandler(aa.getUid(), CompletaAllegatoAttoAsyncService.class);
		ElabKeys completaAllegatoAttoKeySelector = ElabKeys.COMPLETA_ALLEGATO_ATTO;
		boolean endElab = elaborazioniManager.endElaborazione(eakh.creaElabServiceFromPattern(completaAllegatoAttoKeySelector), eakh.creaElabKeyFromPattern(completaAllegatoAttoKeySelector));
		log.info(methodName, "Elaborazione segnata come terminata? " + endElab);
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
		boolean statoDaCompletare = StatoOperativoAllegatoAtto.DA_COMPLETARE.equals(allegato.getStatoOperativoAllegatoAtto());
		if(!statoDaCompletare) {
			impostaInResponseMessaggioDiScarto(allegato, "Stato non valido: " + allegato.getStatoOperativoAllegatoAtto() + ". Atteso " + StatoOperativoAllegatoAtto.DA_COMPLETARE);
		}
		return statoDaCompletare;
	}
	
	
	/**
	 * Tenta di bloccare altre elaborazioni sull'allegato atto.
	 * 
	 * @return <code>true</code> se risulta impossibile, <code>false</code> altrimenti
	 */
	private boolean startElaborazioneAllegatoAttoEffettuataConSuccesso(AllegatoAtto allegato) {
		final String methodName = "checkElaborazioniInCorso";
		ElaborazioniAttiveKeyHandler eakh = new ElaborazioniAttiveKeyHandler(allegato.getUid(), CompletaAllegatoAttoAsyncService.class);
		try {
			elaborazioniManager.startElaborazione(eakh.creaElabServiceFromPattern(completaAllegatoKeySelector), eakh.creaElabKeyFromPattern(completaAllegatoKeySelector));
		} catch (ElaborazioneAttivaException e) {
			log.info(methodName, "esiste un'elaborazione attiva con chiave: " + eakh.creaElabKeyFromPattern(completaAllegatoKeySelector) + "per l'allegato atto con uid: " + allegato.getUid());
			impostaInResponseMessaggioDiScarto(allegato, "elaborazione gia' in corso.");
			return false;
		}
		return true;	
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
	 * Imposta in response gli errori.
	 *
	 * @param errori the errori
	 * @param aa the aa
	 */
	private String creaStringaErrori(List<Errore> errori) {
		StringBuilder msgErrore = new StringBuilder();
		for (Errore err : errori) {
			msgErrore.append(err.getDescrizione())
			.append(", ");
		}
		return msgErrore.toString();
	}
	
	/**
	 * The Class CompletaAllegatoAttoSingoloResponseHandler.
	 */
	private class CompletaAllegatoAttoSingoloResponseHandler extends ResponseHandler<CompletaAllegatoAttoResponse>{
		@Override
		protected void handleResponse(CompletaAllegatoAttoResponse resEAA) {
			AllegatoAtto allegatoElaborato = resEAA.getAllegatoAtto();
			//la faccio qua per farla una sola volta
			chiudiElaborazioneAttivaAllegato(allegatoElaborato);
			if(resEAA.hasErrori()) {
				impostaInResponseMessaggioDiScarto(allegatoElaborato,creaStringaErrori(resEAA.getErrori()));
				return;
			}
			
			if(resEAA.getSubdocumentiScartati()!= null && !resEAA.getSubdocumentiScartati().isEmpty()) {
				StringBuilder messaggioQuota = new StringBuilder();
				//FIXME: dovrebbe esserci una distinzione tra i messaggi relativi all'allegato scartato e quelli delle quote
				for(SubdocumentoSpesa subdoc : resEAA.getSubdocumentiScartati()) {
					messaggioQuota.append( ": quota ")
					.append( subdoc.getNumero())
					.append(subdoc.getDocumento() != null ? " del documento " + subdoc.getDocumento().getDescAnnoNumeroTipoDoc() : "")
					.append( " [uid:" + subdoc.getUid() + "]")
					.append(": subdcumento sospeso");
				}
				String msg = ErroreCore.AGGIORNAMENTO_NON_POSSIBILE.getErrore(creaDescrizioneAllegato(allegatoElaborato), messaggioQuota.toString()).getTesto();
				Messaggio m = new Messaggio();
				m.setCodice("QUOTE_SCARTATE");
				m.setDescrizione(msg);
				res.addMessaggio(m);
				return;
			}
			//se sono arrivato qui, il servizio ha risposto ok
			impostaMessaggioOperazioneAvvenutaCorrettamente(allegatoElaborato);
		}
	
	}

	
}
