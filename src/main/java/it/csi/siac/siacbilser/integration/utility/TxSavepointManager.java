/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.utility;

import java.sql.SQLException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siaccommon.util.log.LogUtil;

/**
 * Gestisce i savepoint all'interno di una transazione.
 * 
 * @version 0.0 alfa
 * 
 */
@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class TxSavepointManager {
	
	private LogUtil log = new LogUtil(getClass());
	
	@PersistenceContext
	protected EntityManager entityManager;
	

	/**
	 * Crea un savepoint.
	 * 
	 * @return nome del savepoint creato
	 */
	public String createSavepoint() {
		String savepointName = getRandomSavepointName();
		createSavepoint(savepointName);
		return savepointName;
		
	}
	
	/**
	 * Crea un savepoint con il nome passato come parametro.
	 * 
	 * @param savepointName
	 * @throws SQLException
	 * @throws Exception
	 */
	private void createSavepoint(String savepointName) {
		final String methodName = "createSavepoint";
		entityManager.flush();
		Query query = entityManager.createNativeQuery("SAVEPOINT " + savepointName);
		query.executeUpdate();
		entityManager.flush();
		log.debug(methodName, "Created SAVEPOINT for name " + savepointName);
	}

	

	/**
	 * Distrugge il savepoint con il nome passato come parametro. Non sarà più possibile effettuare rollback su questo savepoint.
	 * 
	 * @param savepointName
	 */
	public void releaseSavepoint(String savepointName) {
		final String methodName = "releaseSavepoint";
		entityManager.flush();
		Query query = entityManager.createNativeQuery("RELEASE SAVEPOINT " + savepointName);
		query.executeUpdate();
		entityManager.flush();
		log.debug(methodName, "Released SAVEPOINT for name " + savepointName);
	}

	
	/**
	 * Effettua il rollback fino al savepoint indicato cme parametro.
	 * 
	 * @param savepointName
	 */
	public void rollbackToSavepoint(String savepointName) {
		final String methodName = "rollbackToSavepoint";
		
		entityManager.flush();
		Query query = entityManager.createNativeQuery("ROLLBACK TO SAVEPOINT " + savepointName);
		query.executeUpdate();
		entityManager.flush();
		log.debug(methodName, "Rollbacked SAVEPOINT for name " + savepointName);
	}
	
	

	/**
	 * Ottiene un nome casuale e univoco da assegnare al savepoint.
	 * 
	 * @return il nome del savepoint
	 */
	public String getRandomSavepointName() {
		return "Savepoint" + System.nanoTime() + "_" + Math.round(Math.random()*1000);
	}
	

	public void flushData() throws Exception {
		flushData("");
	}
	
	public void flushData(String errorMsg) throws Exception {
		String methodName = "flushData";
		try {
			log.debug(methodName , "Flush dei dati scritti finora.");
			entityManager.flush();
		} catch(Exception e){
			log.error(methodName, "Impossibile effettuare il flush dei dati scritti finora. "+ e.getMessage() + " " +errorMsg,e);
			throw e;
		}
	}
	
}
