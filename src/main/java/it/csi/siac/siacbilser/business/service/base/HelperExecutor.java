/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.base;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.model.exception.HelperException;

/**
 * Permette di eseguire un helper specificando il tipo di propagazione della transazione.
 * 
 * @author Domenico Lisi
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class HelperExecutor {
	
	/**
	 * Esecuzione dell'helper
	 * @param helper l'helper da eseguire
	 * @return il risultato dell'invocazione dell'helper
	 * @throws HelperException nel caso in cui l'helper lanci un'eccezione
	 */
	public <T> T executeHelper(Helper<T> helper) {
		return helper.helpExecute();
	}
	/**
	 * Esecuzione dell'helper in una nuova transazione
	 * @param helper l'helper da eseguire
	 * @return il risultato dell'invocazione dell'helper
	 * @throws HelperException nel caso in cui l'helper lanci un'eccezione
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public <T> T executeHelperTxRequiresNew(Helper<T> helper) {
		return helper.helpExecute();
	}
}
