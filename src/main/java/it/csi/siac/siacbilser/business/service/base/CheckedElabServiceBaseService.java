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
import it.csi.siac.siaccommonser.business.service.base.BaseService;
import it.csi.siac.siaccorser.model.ServiceRequest;
import it.csi.siac.siaccorser.model.ServiceResponse;

/**
 * Assicura che il servizio verra' eseguito solo se non esiste un altra elaborazione attiva per lo stesso
 * servizio (definito da {@link #getServiceClass()}) e per la stessa chiave o sottoinsime di chiavi (definito da {@link #getElabKey()})  di invocazione del servizio.
 * 
 * @author Domenico Lisi
 *
 * @param <REQ>
 * @param <RES>
 */
public abstract class CheckedElabServiceBaseService<REQ extends ServiceRequest,RES extends ServiceResponse> extends CheckedElabBaseService<REQ, RES> {


	@Autowired
	protected ElaborazioniManager elaborazioniManager;
	
	protected Set<String> elabKeys = new HashSet<String>();
	
	@Override
	protected void init() {
		super.init();
		elaborazioniManager.init(ente, loginOperazione);
	}
	
	
	/**
	 * Controlla l'esistenza di una elaborazione attiva per la stessa chiave fornita dal metodo {@link #getElabKey()}.
	 * 
	 * @return true se esiste.
	 */
	@Override
	protected boolean esisteElaborazioneAttiva() {
		return elaborazioniManager.esistonoElaborazioniAttive(getServiceClass(), getElabKeysArray());
	}
	
	/**
	 * Contrassegna come Avviata l'elaborazione.
	 * 
	 * @throws ElaborazioneAttivaException
	 */
	@Override
	protected void startElaborazione() throws ElaborazioneAttivaException {
		elaborazioniManager.startElaborazioni(getServiceClass(), getElabKeysArray());
	}
	
	/**
	 * Segna come terminata l'elaborazione attiva per la chiave fornita dal metodo {@link #getElabKey()}.
	 * 
	 * @return true se terminata, false se era gia' terminata in precedenza.
	 */
	@Override
	protected boolean endElaborazioneAttiva() {
		elaborazioniManager.endElaborazioni(getServiceClass(), getElabKeysArray());
		return true;
	}
	
	/**
	 * Classe di servizio per il quale eseguire il blocco sulla chiave restituita da {@link #getElabKey()}.
	 * 
	 * @return service class.
	 */
	protected Class<? extends BaseService<?, ?>> getServiceClass() {
		@SuppressWarnings("unchecked")
		Class<? extends BaseService<?, ?>> serviceClass = (Class<? extends BaseService<?, ?>>) this.getClass();
		return serviceClass;
	}
	
	/**
	 * Chiave/i univoca dell'elaborazione.
	 * 
	 * @return la/le Chiave/i.
	 */
	private String[] getElabKeysArray(){
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
