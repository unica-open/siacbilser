/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.custom.stilo.business.service.attiamministrativi;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import it.csi.siac.custom.stilo.business.service.ElaboraFileAttiAmministrativiStiloService;
import it.csi.siac.siacintegser.business.service.attiamministrativi.ElaboraFileAttiAmministrativiAsyncBaseService;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ElaboraFileAttiAmministrativiAsyncStiloService extends ElaboraFileAttiAmministrativiAsyncBaseService<ElaboraFileAttiAmministrativiStiloService> { 
}
