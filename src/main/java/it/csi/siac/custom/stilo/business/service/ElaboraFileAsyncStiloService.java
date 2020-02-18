/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.custom.stilo.business.service;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.custom.business.service.ElaboraFileCustomAsyncService;
import it.csi.siac.custom.stilo.business.service.attiamministrativi.ElaboraFileAttiAmministrativiAsyncStiloService;
import it.csi.siac.siacintegser.business.service.base.ElaboraFileAsyncBaseService;
import it.csi.siac.siacintegser.business.service.base.ElaboraFileAsyncServiceInfo;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ElaboraFileAsyncStiloService extends ElaboraFileCustomAsyncService {

	@Override
	protected ElaboraFileAsyncServiceInfo getElaboraFileAsyncServiceInfo(String codiceTipoFile) {
		return new ElaboraFileAsyncServiceInfo() {

			@Override
			public String getAsyncServiceName() {
				return "elaboraFileAttiAmministrativiAsyncStiloService";
			}

			@Override
			public Class<? extends ElaboraFileAsyncBaseService<?, ?>> getAsyncServiceClass() {
				return ElaboraFileAttiAmministrativiAsyncStiloService.class;
			}
		};
	}
}
