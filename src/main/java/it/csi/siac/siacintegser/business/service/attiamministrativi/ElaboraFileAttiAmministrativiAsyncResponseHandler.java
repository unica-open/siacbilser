/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacintegser.business.service.attiamministrativi;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacintegser.business.service.base.ElaboraFileAsyncBaseResponseHandler;


@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ElaboraFileAttiAmministrativiAsyncResponseHandler extends ElaboraFileAsyncBaseResponseHandler{}