/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacintegser.business.service.stipendi.stipeoneri;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import it.csi.siac.siacintegser.business.service.base.ElaboraFileAsyncBaseService;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ElaboraFileStipeOneriAsyncService extends ElaboraFileAsyncBaseService<ElaboraFileStipeOneriAsyncResponseHandler, ElaboraFileStipeOneriService> { 

	
	@Override
	protected String getNomeAzione() {
		return "OP-FLUSSO-STIPENDI";
	}

}
