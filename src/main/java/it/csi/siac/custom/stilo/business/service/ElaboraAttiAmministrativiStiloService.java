/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.custom.stilo.business.service;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import it.csi.siac.siacintegser.business.service.base.ElaboraFileService;
import it.csi.siac.siacintegser.business.service.documenti.BaseElaboraAttiAmministrativiSyncService;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ElaboraAttiAmministrativiStiloService extends BaseElaboraAttiAmministrativiSyncService {
	
	@Override
	protected Class<? extends ElaboraFileService> getElaboraFileServiceClass() {
		return ElaboraFileStiloService.class;
	}
}
