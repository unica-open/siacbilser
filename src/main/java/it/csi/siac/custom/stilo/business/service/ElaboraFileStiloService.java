/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.custom.stilo.business.service;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.custom.business.service.base.ElaboraFileCustomService;
import it.csi.siac.siacintegser.business.service.base.ElaboraFileBaseService;
import it.csi.siac.siacintegser.business.service.base.ElaboraFileServiceInfo;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ElaboraFileStiloService extends ElaboraFileCustomService {

	@Override
	protected ElaboraFileServiceInfo getElaboraFileServiceInfo(String codiceTipoFile) {
		return new ElaboraFileServiceInfo() {
			
			@Override
			public String getServiceName() {
				return "elaboraFileAttiAmministrativiStiloService";
			}
			
			@Override
			public Class<? extends ElaboraFileBaseService> getServiceClass() {
				return ElaboraFileAttiAmministrativiStiloService.class;
			}
		};
	}
}
