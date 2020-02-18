/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.base;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.integration.exception.ElaborazioneAttivaException;
import it.csi.siac.siacbilser.integration.utility.ElaborazioniManager;
import it.csi.siac.siaccorser.model.ServiceRequest;
import it.csi.siac.siaccorser.model.ServiceResponse;

/**
 * Assicura che il servizio verra' eseguito solo se non esiste un altra elaborazione attiva per lo stesso
 * gruppo (definito da {@link #getGroup()}) e per la stessa chiave o sottoinsime di chiavi (definito da {@link #getElabKeys()}) di invocazione del servizio.
 * 
 * @author Domenico Lisi
 *
 * @param <REQ>
 * @param <RES>
 */
public abstract class CheckedElabGroupBaseService<REQ extends ServiceRequest,RES extends ServiceResponse> extends CheckedElabBaseService<REQ, RES> {


	@Autowired
	protected ElaborazioniManager elaborazioniManager;
	
	protected Set<String> elabKeys = new HashSet<String>();
	
	
	@Override
	protected void init() {
		super.init();
		elaborazioniManager.init(ente, loginOperazione);
		elabKeys = new HashSet<String>();
	}
	
	/**
	 * Controlla l'esistenza di una elaborazione attiva per la stessa chiave fornita dal metodo {@link #getElabKeys()}.
	 * 
	 * @return true se esiste.
	 */
	@Override
	protected boolean esisteElaborazioneAttiva() {
		return elaborazioniManager.esistonoElaborazioniAttive(getGroup(), getElabKeysArray());
	}

	/**
	 * Contrassegna come Avviata l'elaborazione.
	 * 
	 * @throws ElaborazioneAttivaException
	 */
	@Override
	protected void startElaborazione() throws ElaborazioneAttivaException {
		elaborazioniManager.startElaborazioni(getGroup(), getElabKeysArray());
	}
	
	/**
	 * Segna come terminata l'elaborazione attiva per la chiave fornita dal metodo {@link #getElabKeys()}.
	 * 
	 * @return true se terminata, false se era gia' terminata in precedenza.
	 */
	@Override
	protected boolean endElaborazioneAttiva() {
		elaborazioniManager.endElaborazioni(getGroup(), getElabKeysArray());
		return true;
	}
	
	/**
	 * Nome del gruppo per il quale eseguire il blocco sulla chiave restituita da {@link #getElabKeys()}.
	 * @return
	 */
	protected abstract String getGroup();
	
	/**
	 * Chiave/i univoca dell'elaborazione.
	 * 
	 * @return la/le Chiave/i.
	 */
	protected String[] getElabKeysArray(){
		Set<String> keys = getElabKeys();
		if(keys==null || keys.isEmpty()){
			throw new IllegalArgumentException("E' necessario specificare almeno una chiave di elaborazione.");
		}
		return keys.toArray(new String[keys.size()]);
	}
	
	/**
	 * Chiave/i univoca dell'elaborazione.
	 * 
	 * @return la/le Chiave/i.
	 */
	private Set<String> getElabKeys() {
		return elabKeys;
	}
	

}
