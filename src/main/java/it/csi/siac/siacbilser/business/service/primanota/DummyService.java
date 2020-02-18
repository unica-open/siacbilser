/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.primanota;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import it.csi.siac.siacbilser.business.service.base.ExtendedBaseService;
import it.csi.siac.siacgenser.frontend.webservice.msg.InseriscePrimaNotaAutomatica;
import it.csi.siac.siacgenser.frontend.webservice.msg.InseriscePrimaNotaAutomaticaResponse;

/**
 * The Class DummyService.
 *
 * @author Domenico
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class DummyService extends ExtendedBaseService<InseriscePrimaNotaAutomatica, InseriscePrimaNotaAutomaticaResponse> {

	@Override
	protected void execute() {
		final String methodName = "execute";
		String tokenOperazione = req.getRichiedente().getTokenOperazione();
		
		String threadName = Thread.currentThread().getName();
		log.info(methodName, ">>>>>>"+threadName+">>>>>>>>>> DummyService execute" + " ["+tokenOperazione+"]");
		
		int sleepMillis = 5000;
		log.info(methodName, ">>>>>>"+threadName+">>>>>>>>>> DummyService sleepMillis: " +sleepMillis + " ["+tokenOperazione+"]");
		try {
			Thread.sleep(sleepMillis);
		} catch (InterruptedException e) {
			log.error(methodName, ">>>>>>"+threadName+">>>>>>>>>> DummyService ARRESTATO!!!!! end" + " ["+tokenOperazione+"]", e);
		}
		
		log.info(methodName, ">>>>>>"+threadName+">>>>>>>>>> DummyService execute end" + " ["+tokenOperazione+"]");
		
	}
	
	
	
	
}
