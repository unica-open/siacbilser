/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.integration.dao.SiacTElaborazioniAttiveRepository;
import it.csi.siac.siacbilser.integration.entity.SiacTElaborazioniAttive;
import it.csi.siac.siacbilser.integration.entity.SiacTEnteProprietario;
import it.csi.siac.siacbilser.integration.exception.ElaborazioneAttivaException;

/**
 * Data access delegate di Elaborazioni .
 *
 * @author Domenico
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class ElaborazioniDad extends ExtendedBaseDadImpl {
	
	/** The siac t subdoc repository */
	@Autowired
	private SiacTElaborazioniAttiveRepository siacTElaborazioniAttiveRepository;
	
	/**
	 * Mark the async operation as started.
	 * 
	 * @param elabService async service name
	 * @param elabKey concurrency key
	 * @throws ElaborazioneAttivaException se eiste gia' un'elaborazione attiva per la chiave specificata.
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void startElaborazione(String elabService, String elabKey) throws ElaborazioneAttivaException {
		final String methodName = "startElaborazione";
		boolean esisteElaborazioneAttiva = esisteElaborazioneAttiva(elabService, elabKey);
		if(esisteElaborazioneAttiva) {
			String msg = "Esiste gia' un elaborazione in corso per "+elabService+" con la chiave " + elabKey;
			log.warn(methodName, msg);
			throw new ElaborazioneAttivaException(msg);
		}
		
		SiacTElaborazioniAttive siacTElaborazioniAttive = new SiacTElaborazioniAttive();
		siacTElaborazioniAttive.setElabService(elabService);
		siacTElaborazioniAttive.setElabKey(elabKey);
		SiacTEnteProprietario siacTEnteProprietario = new SiacTEnteProprietario();
		siacTEnteProprietario.setEnteProprietarioId(ente.getUid());
		siacTElaborazioniAttive.setSiacTEnteProprietario(siacTEnteProprietario);
		siacTElaborazioniAttive.setDataModificaInserimento(new Date());
		siacTElaborazioniAttive.setLoginOperazione(loginOperazione);
		siacTElaborazioniAttiveRepository.saveAndFlush(siacTElaborazioniAttive);
		
		log.info(methodName, "Elaborazione per "+elabService+" con la chiave " + elabKey + " segnata come attiva.");
		
	}
	
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void startElaborazioni(String elabService, String... elabKeys) throws ElaborazioneAttivaException {
		final String methodName = "startElaborazione";		
		for(String elabKey : elabKeys){
			startElaborazione(elabService, elabKey);
		}
		log.info(methodName, "Elaborazioni per "+elabService+" segnate come attive con le seguenti chiavi: " + ReflectionToStringBuilder.toString(elabKeys) + ".");
	}
	
	
	/**
	 * Controlla se esiste un elaborazione attiva.
	 * 
	 * @param elabService
	 * @param elabKey
	 * @return true se esiste un elaborazione attiva per la chiave specificata.
	 */
	@Transactional(readOnly=true)
	public boolean esisteElaborazioneAttiva(String elabService, String elabKey) {
		final String methodName = "esisteElaborazioneAttiva";
		SiacTElaborazioniAttive siacTElaborazioniAttive = siacTElaborazioniAttiveRepository.findByElabServiceAndElabKey(elabService, elabKey);
		log.debug(methodName, "elabService: "+ elabService +" elabKey: "+ elabKey + " Trovato: "+(siacTElaborazioniAttive!=null));
		return siacTElaborazioniAttive!=null;
	}
	
	/**
	 * Controlla se esiste almeno una elaborazione attiva con le chiavi specificate.
	 * 
	 * @param elabService
	 * @param elabKey
	 * @return true se esiste un elaborazione attiva per la chiave specificata.
	 */
	@Transactional(readOnly=true)
	public boolean esistonoElaborazioniAttive(String elabService, String... elabKeys) {
		final String methodName = "esistonoElaborazioniAttive";
		
		for(String elabKey : elabKeys){
			boolean esiste = esisteElaborazioneAttiva(elabService, elabKey);
			if(esiste) {
				log.debug(methodName, "elabService: "+ elabService +" elabKey: "+ elabKey + " Trovato almeno uno: returning true.");
				return true;
			}
		}
		
		log.debug(methodName, "elabService: "+ elabService +". Nessuna chiave trovata tra quelle in elenco: returnin false.");
		return false;
		
	}
	
	
	/**
	 * Mark the async operation as terminated.
	 * 
	 * @param elabService async service name
	 * @param elabKey concurrency key
	 *  @return true if stopped, false otherwise...
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public boolean endElaborazione(String elabService, String elabKey) {
		final String methodName = "endElaborazione";		
		SiacTElaborazioniAttive siacTElaborazioniAttive = siacTElaborazioniAttiveRepository.findByElabServiceAndElabKey(elabService, elabKey);
		log.debug(methodName, "elabService: "+ elabService +" elabKey: "+ elabKey + " Trovato: "+(siacTElaborazioniAttive!=null));
		if(siacTElaborazioniAttive == null) {
			String msg = "Impossibile trovare l'elaborazione asincrona! elabService: "+ elabService +" elabKey: "+ elabKey;
			log.error(methodName, msg);
			//throw new IllegalStateException(msg);
			return false;
		}
		
		siacTElaborazioniAttive.setDataCancellazioneIfNotSet(new Date());
		siacTElaborazioniAttiveRepository.saveAndFlush(siacTElaborazioniAttive);
		log.info(methodName, "Elaborazione per "+elabService+" con la chiave " + elabKey + " disattivata.");
		return true;
	}


	
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void endElaborazioni(String elabService, String... elabKeys) {
		final String methodName = "endElaborazioni";		
		for(String elabKey : elabKeys){
			endElaborazione(elabService, elabKey);
		}
		log.info(methodName, "Elaborazioni per "+elabService+" segnate come disattivate con le seguenti chiavi: " + ReflectionToStringBuilder.toString(elabKeys) + ".");
	}
	
	@Transactional(readOnly=true)
	public List<String> getElabKeyElaborazioneAttivaByElabService(String elabService) {
		final String methodName = "esisteElaborazioneAttiva";
		List<String> elabKeys = siacTElaborazioniAttiveRepository.findByElabKeyByElabService(elabService, ente.getUid());
		log.debug(methodName, "elabService: "+ elabService );
		return elabKeys;
	}
	

}
