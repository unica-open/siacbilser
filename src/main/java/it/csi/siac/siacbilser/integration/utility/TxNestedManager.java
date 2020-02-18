/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.utility;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.annotation.WebListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siaccommon.util.log.LogUtil;

/**
 * Esegue una opearzione simulando una transazione con propagation nested.
 * 
 * @author Domenico
 * @version 0.0 alfa
 */
@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class TxNestedManager {
	private static LogUtil log = new LogUtil(TxNestedManager.class);
	
	/**
	 * Flag a livello di thread per marcare la transazione corrente come Nested.
	 */
	private static final ThreadLocal<Boolean> isTxNested = new ThreadLocal<Boolean>() {
		 @Override 
		 protected Boolean initialValue() {
             return Boolean.FALSE;
		 }
	};
	
	@Autowired
	private TxSavepointManager txSavepointManager;
	
	public void performTxNestedOperation(NestedOperation nestedOperation) throws Exception {
		final String methodName = "performNestedOperation";
		Boolean isTxNestedPrevValue = isTxNested.get(); 
		
		String savepointName = txSavepointManager.createSavepoint(); //la creazione del savepoint va fuori dal blocco try-catch.
		//ho creato il savapoint con successo. Imposto isTxNested.
		isTxNested.set(Boolean.TRUE); 
		try {
			log.debug(methodName, "Inizio operazione nested...");
			nestedOperation.performOperation();
			log.debug(methodName, "Operazione nested terminata correttamente.");
			txSavepointManager.releaseSavepoint(savepointName);
		} catch(Exception e) {
			log.error(methodName, "Operazioni nested terminate con errori. Effettuo rollback al savepoint "+savepointName, e);
			txSavepointManager.rollbackToSavepoint(savepointName);
			log.info(methodName, "Rollback effettuato con successo al savepoint: "+savepointName);
			throw e;
		} finally {
			isTxNested.set(isTxNestedPrevValue /*Boolean.FALSE*/);
		}
		
		
	}

	/**
	 * Indica se il thread corrente sta eseguento una Nested operation.
	 *
	 * @return the boolean
	 */
	public static Boolean isTxNested() {
		return isTxNested.get();
	}


	/**
	 * Web Listener per ripulire {@link TxNestedManager#isTxNested} ad ogni terminazione della request.
	 * 
	 * @author Domenico
	 *
	 */
	@WebListener
	public static class TxNestedManagerRequestListener implements ServletRequestListener {
		
		private LogUtil log = new LogUtil(TxNestedManagerRequestListener.class);
		
		@Override
		public void requestInitialized(ServletRequestEvent sre) {
			// Nulla da eseguire
		}

		@Override
		public void requestDestroyed(ServletRequestEvent sre) {
			final String methodName = "requestDestroyed";
			log.debug(methodName, "remove cache for thread: "+ Thread.currentThread().getName());
			TxNestedManager.cleanThreadLocalIsTxNested();
		}

	}
	
	/**
	 * Riinizializza il flag isTxNested per il thread corrente
	 */
	public static void cleanThreadLocalIsTxNested() {
		isTxNested.remove();
		log.debug("cleanThreadLocalIsTxNested", "isTxNested value removed for thread: "+ Thread.currentThread().getName());
	}
	


}
