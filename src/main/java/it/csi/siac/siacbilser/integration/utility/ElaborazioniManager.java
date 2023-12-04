/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.utility;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.integration.dad.ElaborazioniDad;
import it.csi.siac.siacbilser.integration.exception.ElaborazioneAttivaException;
import it.csi.siac.siaccommonser.util.log.LogSrvUtil;
import it.csi.siac.siaccommonser.business.service.base.BaseService;
import it.csi.siac.siaccorser.model.Ente;

/**
 * Manager delle elaborazioni attive.
 * 
 * @author Domenico Lisi
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ElaborazioniManager {
	protected static LogSrvUtil log = new LogSrvUtil(ElaborazioniManager.class);
	
	@Autowired
	private ElaborazioniDad elaborazioniDad;
	
	
	/**
	 * Inizializzazione 
	 * 
	 * @param ente
	 * @param loginOperazione
	 */
	public void init(Ente ente, String loginOperazione) {
		elaborazioniDad.setEnte(ente);
		elaborazioniDad.setLoginOperazione(loginOperazione);
	}
	
	/* ##################################################################################################################
	 * #################################### Based on Service Class ######################################################
	 * ##################################################################################################################
	 */
	
	
	/**
	 * Segna l'elaborazione come attiva.
	 * 
	 * @param serviceClass
	 * @param elabKey
	 * @throws ElaborazioneAttivaException
	 */
	@Transactional(propagation=Propagation.NOT_SUPPORTED) //Sospendo la tx
	public void startElaborazione(Class<? extends BaseService<?, ?>> serviceClass, String elabKey) throws ElaborazioneAttivaException {
		startElaborazione(serviceClass.getSimpleName(), elabKey);
	}
	
	/**
	 * Segna le elaborazioni come attive.
	 * 
	 * @param serviceClass
	 * @param elabKey
	 * @throws ElaborazioneAttivaException
	 */
	@Transactional(propagation=Propagation.NOT_SUPPORTED) //Sospendo la tx
	public void startElaborazioni(Class<? extends BaseService<?, ?>> serviceClass, String... elabKeys) throws ElaborazioneAttivaException {
		startElaborazioni(serviceClass.getSimpleName(), elabKeys);
	}

	/**
	 * Controlla se esiste un elaborazione attiva.
	 * 
	 * @param serviceClass
	 * @param elabKey
	 * @return true se esiste un elaborazione attiva per la chiave specificata.
	 */
	public boolean esisteElaborazioneAttiva(Class<? extends BaseService<?, ?>> serviceClass, String elabKey) {
		return esisteElaborazioneAttiva(serviceClass.getSimpleName(), elabKey);
	}
	
	/**
	 * Controlla se esiste almeno una elaborazione attiva con le chiavi specificate.
	 * 
	 * @param serviceClass
	 * @param elabKey
	 * @return true se esiste un elaborazione attiva per la chiave specificata.
	 */
	public boolean esistonoElaborazioniAttive(Class<? extends BaseService<?, ?>> serviceClass, String... elabKeys) {
		return esistonoElaborazioniAttive(serviceClass.getSimpleName(), elabKeys);
	}
	
	
	/**
	 * Segna l'elaborazione come terminata.
	 * 
	 * @param elabService async service name
	 * @param elabKey concurrency key
	 * @return true if stopped, false otherwise...
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public boolean endElaborazione(Class<? extends BaseService<?, ?>> serviceClass, String elabKey) {
		return endElaborazione(serviceClass.getSimpleName(), elabKey);
	}
	
	/**
	 * Segna le elaborazioni come terminate.
	 * 
	 * @param elabService async service name
	 * @param elabKey concurrency key
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void endElaborazioni(Class<? extends BaseService<?, ?>> serviceClass, String... elabKeys) {
		endElaborazioni(serviceClass.getSimpleName(), elabKeys);
	}
	
	
	/* ##################################################################################################################
	 * #################################### Based on "abstract concept" of Group ###########################################
	 * ##################################################################################################################
	 */
	
	/**
	 * Segna l'elaborazione come attiva.
	 * 
	 * @param group
	 * @param elabKey
	 * @throws ElaborazioneAttivaException
	 */
	@Transactional(propagation=Propagation.NOT_SUPPORTED) //Sospendo la tx
	public void startElaborazione(final String group, final String elabKey) throws ElaborazioneAttivaException {
		SynchronizedStarter.run(new Starter() {
			@Override
			public void start() throws ElaborazioneAttivaException {
				// TODO In futuro si potrebbe effettuare il Lock solo per group.
				elaborazioniDad.startElaborazione(group, elabKey);
				// startElaborazioni(group, elabKey);
				// a questo punto il dad ha effettuato il commit.
			}

			@Override
			public String lockKey() {
				return group; //Lock solo per gruppo
			}
		});
	}
	
	/**
	 * Segna le elaborazioni come attive.
	 * 
	 * @param group
	 * @param elabKey
	 * @throws ElaborazioneAttivaException
	 */
	@Transactional(propagation=Propagation.NOT_SUPPORTED) //Sospendo la tx
	public void startElaborazioni(final String group, final String... elabKeys) throws ElaborazioneAttivaException {
		SynchronizedStarter.run(new Starter() {
			@Override
			public void start() throws ElaborazioneAttivaException {
				elaborazioniDad.startElaborazioni(group, elabKeys);
				// a questo punto il dad ha effettuato il commit.
			}
			
			@Override
			public String lockKey() {
				return group; //Lock solo per gruppo
			}
		});
		
	}

	/**
	 * Controlla se esiste un elaborazione attiva.
	 * 
	 * @param group
	 * @param elabKey
	 * @return true se esiste un elaborazione attiva per la chiave specificata.
	 */
	public boolean esisteElaborazioneAttiva(String group, String elabKey) {
		return elaborazioniDad.esisteElaborazioneAttiva(group, elabKey);
	}
	
	/**
	 * Controlla se esiste almeno una elaborazione attiva con le chiavi specificate.
	 * 
	 * @param group
	 * @param elabKey
	 * @return true se esiste un elaborazione attiva per la chiave specificata.
	 */
	public boolean esistonoElaborazioniAttive(String group, String... elabKeys) {
		return elaborazioniDad.esistonoElaborazioniAttive(group, elabKeys);
	}
	
	/**
	 * Gets the elab keys elaborazioni attive.
	 *
	 * @param elabService the elab service
	 * @return the elab keys elaborazioni attive
	 */
	public List<String> getElabKeysElaborazioniAttive(String elabService){
		return elaborazioniDad.getElabKeyElaborazioneAttivaByElabService(elabService);
	}
	/**
	 * Segna l'elaborazione come terminata.
	 * 
	 * @param elabService async service name
	 * @param elabKey concurrency key
	 * @return true if stopped, false otherwise...
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public boolean endElaborazione(String group, String elabKey) {
		return elaborazioniDad.endElaborazione(group, elabKey);
	}
	
	/**
	 * Segna le elaborazioni come terminate.
	 * 
	 * @param elabService async service name
	 * @param elabKey concurrency key
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void endElaborazioni(String group, String... elabKeys) {
		elaborazioniDad.endElaborazioni(group, elabKeys);
	}
	
	/**
	 * Esegue in modo sincronizzato un blocco in base a una lockKey.
	 * 
	 * @author Domenico
	 *
	 */
	private static final class SynchronizedStarter {
		private static LogSrvUtil log = new LogSrvUtil(SynchronizedStarter.class);
		private static final ConcurrentMap<String, Lock> LOCKS = new ConcurrentHashMap<String, Lock>();
		
		/** Costruttore privato */
		private SynchronizedStarter() {
			// Prevenire l'instanziazione
		}
		
		public static void run(Starter starter) throws ElaborazioneAttivaException {
			final String methodName = "run";
				
			String lockKey = starter.lockKey();
			String lockInfo = "[lockKey: "+lockKey +" threadName: "+Thread.currentThread().getName()+"]";
			
			Lock lock = new ReentrantLock();
			Lock lockPrecedente = LOCKS.putIfAbsent(new String(lockKey), lock); 
			if(lockPrecedente!=null) {
				log.debug(methodName , "Lock era gia' presente nella map, lo uso.. "+lockInfo);
				lock = lockPrecedente;
			} else {
				log.debug(methodName , "Nuovo Lock aggiunto alla map. "+lockInfo);
			}
			
			log.debug(methodName , "Eseguo il lock... "+lockInfo);
			lock.lock();
			try {
				log.debug(methodName , "Ho preso il lock. "+lockInfo);
				
				lockPrecedente = LOCKS.putIfAbsent(new String(lockKey), lock); 
				if(lockPrecedente == null) {
					log.debug(methodName , "Lock era gia' rimosso dalla map.. ma ora c'e' di nuovo! (sei sicuramente in concorrenza di thread) :) "+lockInfo);
				} else {
					log.debug(methodName , "Potrebbe non esserci stata concorrenza di thread. "+(lockPrecedente.equals(lock))+" "+lockInfo);
				}
				
				starter.start();
				log.debug(methodName , "Run terminato correttamente. "+lockInfo);
			} finally {
				try {
					LOCKS.remove(lockKey);
					log.debug(methodName , "Sto per rilasciare il lock. "+lockInfo);
				} finally {
					lock.unlock();
					log.debug(methodName , "Lock rilasciato. "+lockInfo);
				}
				
			}

		}
		
		
	}
	
	private interface Starter {
		void start() throws ElaborazioneAttivaException;
		String lockKey();
	}

}
