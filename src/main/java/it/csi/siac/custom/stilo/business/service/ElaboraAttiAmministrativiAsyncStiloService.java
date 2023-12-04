/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.custom.stilo.business.service;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import it.csi.siac.siacintegser.business.service.base.ElaboraFileAsyncService;
import it.csi.siac.siacintegser.business.service.documenti.BaseElaboraAttiAmministrativiAsyncService;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ElaboraAttiAmministrativiAsyncStiloService extends BaseElaboraAttiAmministrativiAsyncService {

	@Override
	protected Class<? extends ElaboraFileAsyncService> getElaboraFileAsyncServiceClass() { 
		return ElaboraFileAsyncStiloService.class;
	}
}
