/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.allegatoatto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacattser.model.StatoOperativoAtti;
import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.business.utility.ElaborazioniAttiveKeyHandler;
import it.csi.siac.siacbilser.integration.dad.AllegatoAttoDad;
import it.csi.siac.siacbilser.integration.dad.AttoAmministrativoDad;
import it.csi.siac.siacbilser.integration.utility.ElaborazioniManager;
import it.csi.siac.siacbilser.model.ElabKeys;
import it.csi.siac.siacbilser.model.errore.ErroreBil;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaAllegatoAtto;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaAllegatoAttoResponse;
import it.csi.siac.siacfin2ser.model.AllegatoAtto;
import it.csi.siac.siacfin2ser.model.StatoOperativoAllegatoAtto;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AggiornaAllegatoAttoService extends CheckedAccountBaseService<AggiornaAllegatoAtto, AggiornaAllegatoAttoResponse> {
	
	private AllegatoAtto allegatoAtto;
	
	@Autowired
	private AllegatoAttoDad allegatoAttoDad;
	
	@Autowired
	private AttoAmministrativoDad attoAmministrativoDad;
	
	// SIAC-6095
	@Autowired
	private ElaborazioniManager elaborazioniManager;
	//gestione del blocco delle altre operazioni
	private ElabKeys completaAllegatoKeySelector = ElabKeys.COMPLETA_ALLEGATO_ATTO;
	private ElaborazioniAttiveKeyHandler eakh;


	@Override
	protected void checkServiceParam() throws ServiceParamError {
		this.allegatoAtto = req.getAllegatoAtto();
		
		checkNotNull(allegatoAtto, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("allegato atto"));
		checkNotNull(allegatoAtto.getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente"));
		checkCondition(allegatoAtto.getEnte().getUid() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid ente"));
		checkNotNull(allegatoAtto.getStatoOperativoAllegatoAtto(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("stato operativo allegato atto"));
		checkNotNull(allegatoAtto.getDatiSensibili(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("dati sensibili atto allegato"), false);
		checkNotNull(allegatoAtto.getAttoAmministrativo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("atto amministrativo allegato atto"));
		
		checkNotNull(allegatoAtto.getElenchiDocumentiAllegato(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("elenchi documenti allegato atto allegato"));
		//checkCondition(!allegatoAtto.getElenchiDocumentiAllegato().isEmpty(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("elenchi documenti allegato atto allegato"));
		
		
	}
	
	@Override
	@Transactional
	public AggiornaAllegatoAttoResponse executeService(AggiornaAllegatoAtto serviceRequest) {
		return super.executeService(serviceRequest);
	}	
	
	@Override
	protected void init() {
		allegatoAttoDad.setLoginOperazione(loginOperazione);
		allegatoAttoDad.setEnte(allegatoAtto.getEnte());
		elaborazioniManager.init(allegatoAtto.getEnte(), req.getRichiedente().getOperatore().getCodiceFiscale());
	}
	
	@Override
	protected void execute() {
		checkAttoAmministrativoInStatoDefinitivo();
		checkAttoAmministrativoNonAbbinato();
		defaultPerAllegatoAtto();
		checkAggiornamentoFlagRitenute();
		
		
		// instanzio l'handler per la gestione centralizzata delle chiavi di blocco delle elaborazioni
		eakh = new ElaborazioniAttiveKeyHandler(allegatoAtto.getUid());
		checkElaborazioniInCorso();
		
		// aggiorno l'AllegAtto
		allegatoAtto = req.getAllegatoAtto();		
		
		allegatoAttoDad.aggiornaAllegatoAtto(allegatoAtto);		
		res.setAllegatoAtto(allegatoAtto);
	}
	
	/**
	 * L'attoAmministrativo non deve essere gi&agrave; stato associato  ad altro "allegato atto" e a nessuna quota documento,  
	 * in caso contrario viene inviato il messaggio <FIN_ERR_214, Atto gi&agrave; abbinato>.
	 */
	private void checkAttoAmministrativoNonAbbinato() {
		AllegatoAtto aa = allegatoAttoDad.findAllegatoAttoByAttoAmministrativo(allegatoAtto.getAttoAmministrativo());
		if(aa != null && aa.getUid() != allegatoAtto.getUid()){
			throw new BusinessException(ErroreFin.ATTO_GIA_ABBINATO.getErrore(""));
		}
	}

	/**
	 * Il provvedimento passato in input  deve essere identificato in modo univoco 
	 * e  in stato DEFINITIVO altrimenti viene visualizzato il messaggio <FIN_ERR_0075, 
	 * Stato Provvedimento non consentito, ‘Gestione Allegato atto, ‘Definitivo’>.
	 * 
	 */
	private void checkAttoAmministrativoInStatoDefinitivo() {
		String methodName = "checkAttoAmministrativoInStatoDefinitivo";
		StatoOperativoAtti stato = attoAmministrativoDad.findStatoOperativoAttoAmministrativo(allegatoAtto.getAttoAmministrativo());
		
		log.debug(methodName, "stato: "+stato);
		if(!StatoOperativoAtti.DEFINITIVO.equals(stato)){
			throw new BusinessException(ErroreFin.STATO_PROVVEDIMENTO_NON_CONSENTITO.getErrore("Gestione Allegato Atto", "Definitivo"));
		}
	}

	/**
	 * Imposta i dati di default per l'allegato
	 */
	private void defaultPerAllegatoAtto() {
		if(allegatoAtto.getFlagRitenute() == null) {
			allegatoAtto.setFlagRitenute(Boolean.FALSE);
		}
	}

	/**
	 * Il flag ritenute &eacute; aggiornabile solo se lo stato &eacute; diverso da COMPLETATO e ANNULLATO
	 */
	private void checkAggiornamentoFlagRitenute() {
		final String methodName = "checkAggiornamentoFlagRitenute";
		if(!StatoOperativoAllegatoAtto.COMPLETATO.equals(allegatoAtto.getStatoOperativoAllegatoAtto()) && !StatoOperativoAllegatoAtto.ANNULLATO.equals(allegatoAtto.getStatoOperativoAllegatoAtto())) {
			log.debug(methodName, "Lo stato operativo e' " + allegatoAtto.getStatoOperativoAllegatoAtto() + " - non necessito dei controlli sul flag");
			return;
		}
		// Ottengo il flag originale
		Boolean flagRitenute = allegatoAttoDad.getFlagRitenute(allegatoAtto);
		
		if(flagRitenute == null) {
			log.debug(methodName, "Flag null su db: errore");
			throw new BusinessException(ErroreCore.ERRORE_DI_SISTEMA.getErrore("dati incongruenti su base dati, l'allegato " + allegatoAtto.getUid() + " non ha il flag rilevante"));
		}
		if(!flagRitenute.equals(allegatoAtto.getFlagRitenute())) {
			log.debug(methodName, "Flag ritenute non coerente: valore atteso " + flagRitenute + " - valore ottenuto " + allegatoAtto.getFlagRitenute());
			throw new BusinessException(ErroreCore.VALORE_NON_CONSENTITO.getErrore("flag ritenute", "deve essere pari a " + flagRitenute));
		}
	}
	
	/**
	 * Controlla che non vi sia un servizio che sta facendo la stessa elaborazione.
	 * Nel caso in cui ci&ograve; non accada, viene lanciata un'eccezione
	 */
	private void checkElaborazioniInCorso() {
		
		boolean esisteElaborazioneAttiva = elaborazioniManager.esisteElaborazioneAttiva(eakh.creaElabServiceFromPattern(completaAllegatoKeySelector), eakh.creaElabKeyFromPattern(completaAllegatoKeySelector));
		
		if(esisteElaborazioneAttiva){
			//non posso procedere. lancio un'eccezione.
			throw new BusinessException(ErroreBil.ELABORAZIONE_ATTIVA.getErrore("Esiste una operazione di completamento in corso [uid allegato: "+ allegatoAtto.getUid()+"]"));
		} 
	}
	

}
