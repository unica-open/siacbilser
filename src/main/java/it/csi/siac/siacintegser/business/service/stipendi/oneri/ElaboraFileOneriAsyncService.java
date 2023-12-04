/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacintegser.business.service.stipendi.oneri;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import it.csi.siac.siacintegser.business.service.base.ElaboraFileAsyncBaseService;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ElaboraFileOneriAsyncService extends ElaboraFileAsyncBaseService<ElaboraFileOneriAsyncResponseHandler, ElaboraFileOneriService> { 
	
	@Override
	protected String getNomeAzione() {
		return "OP-FLUSSO-STIPENDI";
	}

}
