/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.base;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.integration.dad.AccountDad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Entita;
import it.csi.siac.siaccorser.model.ServiceRequest;
import it.csi.siac.siaccorser.model.ServiceResponse;
import it.csi.siac.siaccorser.model.TipologiaGestioneLivelli;
import it.csi.siac.siaccorser.model.errore.ErroreCore;


/**
 * Classe base dell'implementazione della business logic di un generico servizio.
 * Rispetto BaseService aggiunge il controllo che l'account dell'utente sia autorizzato ad eseguire un determinato servizio.
 * 
 * Estendendo questa classe bisogna aggiugere le seguenti annotazioni di Spring:
 * 
 * 		\@Service
 * 		\@Scope(BeanDefinition.SCOPE_PROTOTYPE)
 * 
 * 
 * @author Domenico Lisi
 *
 * @param <REQ> Input del servizio che estende ServiceRequest
 * @param <RES> Output del servizio che estende ServiceResponse
 */
public abstract class CheckedAccountBaseService<REQ extends ServiceRequest,RES extends ServiceResponse> extends ExtendedBaseService<REQ,RES> {
	
	@Autowired
	protected AccountDad accountDad;
	
	protected List<String> codiciAzioniConsentite;
	
	@Resource(name="azioniRichiesteProperties")
	private Properties azioniRichiesteProperties;

	/** Ente ottenuto dall'Account del richiedente.. */
	protected Ente ente;
	
	/** Login operazione da propagare nei DAD. */
	protected String loginOperazione;
	
	@Override
	protected void checkRichiedente() throws ServiceParamError {
		super.checkRichiedente();
		
		checkNotNull(req.getRichiedente().getAccount(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("account richiedente"));
		//checkCondition(req.getRichiedente().getAccount().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("account richiedente")); //puo' essere 0!
		
		//loginOperazione = req.getRichiedente().getOperatore().getCodiceFiscale();
		loginOperazione = accountDad.findLoginOperazioneByAccountId(req.getRichiedente().getAccount().getUid());
		
		caricaEnteAssociatoAdAccount();
		caricaAzioniConsentiteRichiedente();
		checkAzioneConsentita();
		
		initBilancio();
	}

	private void initBilancio() {
		Utility.BTL.initBilancio(req.getAnnoBilancio());
	}

	private void caricaEnteAssociatoAdAccount() {
		this.ente = accountDad.findEnteAssocciatoAdAccount(req.getRichiedente().getAccount().getUid());
	}

	protected void checkAzioneConsentita() throws ServiceParamError {
		final String methodName = "checkAzioneConsentita";
		
		List<String> codiciAzioniRichieste = getCodiciAzioniRichieste();
		if(log.isTraceEnabled()) {
			log.trace(methodName, "Azioni consentite per l'account con uid " + req.getRichiedente().getAccount().getUid() + ": "+ codiciAzioniConsentite + "\nAzioni richieste dal servizio: "+ codiciAzioniRichieste + " size: "+ codiciAzioniRichieste.size());
		}
		
		checkCondition(isAzioneConsentita(codiciAzioniRichieste), 
				ErroreCore.ERRORE_DI_SISTEMA.getErrore("Operazione "+codiciAzioniRichieste+" non consentita per l'account con uid: "+ req.getRichiedente().getAccount().getUid() )); //TODO cercare un codice di errore appropriato 
		
	}

	private void caricaAzioniConsentiteRichiedente() {
		this.codiciAzioniConsentite = accountDad.findCodiciAzioniConsentite(req.getRichiedente().getAccount().getUid());
		
	}

	/**
	 * Controlla se almeno uno dei codiciAzioniRichieste è contenuto nei codiciAzioniConsentite
	 * @param codiciAzioniConsentite
	 * @param codiciAzioniRichieste
	 * @return
	 */
	protected boolean isAzioneConsentita(List<String> codiciAzioniRichieste) {
		if(codiciAzioniRichieste.isEmpty()){
			return true;
		}
		for(String codiceAzione: codiciAzioniRichieste){
			if(isAzioneConsentita(codiceAzione)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Controlla se il codiceAzione passato come parametro è contenuto nell'elenco dei codiciAzioniConsentite
	 * 
	 * @param codiciAzioniConsentite
	 * @param codiceAzione
	 * @return
	 */
	protected boolean isAzioneConsentita(String codiceAzione) {
		return codiciAzioniConsentite.indexOf(codiceAzione)!=-1;
	}

	/**
	 * Restituisce l'elenco dei codici di azione associati al servizio. 
	 * E' sufficiente che l'utente abbia almeno uno dei codici in questo elenco per essere autorizzato ad eseguire il servizio.
	 * 
	 * @return
	 */
	protected List<String> getCodiciAzioniRichieste() {		
		String codiciAzioni = azioniRichiesteProperties.getProperty(this.getClass().getName());
		if(codiciAzioni == null){
			return new ArrayList<String>();
		}
		String[] codiciAzioniArray = codiciAzioni.split("\\s*,\\s*");
		return Arrays.asList(codiciAzioniArray);
		
	}
	
	/**
	 * Ottiene, per l'ente dell'account attuale, il livello di gestione 
	 * del tipo specificato come parametro.
	 *
	 * @param tipologiaGestioneLivelli the tipologia gestione livelli
	 * @return the gestione livello
	 * 
	 * @see TipologiaGestioneLivelli
	 */
	protected String getGestioneLivello(TipologiaGestioneLivelli tipologiaGestioneLivelli) {
		return ente.getGestioneLivelli().get(tipologiaGestioneLivelli);
	}
	
	/**
	 * Controlla se l'ente sia abilitato alla gestione delle UEB.
	 *
	 * @return true, se l'ente gestisce le UEB.
	 */
	protected boolean isGestioneUEB() {
		String livelloGestioneBilancio = getGestioneLivello(TipologiaGestioneLivelli.LIVELLO_GESTIONE_BILANCIO);
		return "GESTIONE_UEB".equals(livelloGestioneBilancio); //TODO manca un enum per i livelli!!
	}
	
	/**
	 * Controlla che l'entit&agrave; sia valorizzata con uid
	 * @param entita l'entita da controllare
	 * @return true se l'enti&agrave; &eacute; valorizzata
	 */
	protected boolean entitaConUid(Entita entita) {
		return entita != null && entita.getUid() != 0;
	}
}
