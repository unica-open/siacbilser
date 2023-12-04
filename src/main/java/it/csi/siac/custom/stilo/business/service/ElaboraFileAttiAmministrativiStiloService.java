/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.custom.stilo.business.service;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import it.csi.siac.custom.stilo.business.service.attiamministrativi.factory.AttoAmministrativoStiloFactory;
import it.csi.siac.siacintegser.business.service.attiamministrativi.ElaboraAttoAmministrativoService;
import it.csi.siac.siacintegser.business.service.attiamministrativi.ElaboraFileAttiAmministrativiService;
import it.csi.siac.siacintegser.business.service.attiamministrativi.model.AttoAmministrativoElab;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ElaboraFileAttiAmministrativiStiloService extends ElaboraFileAttiAmministrativiService {

	private AttoAmministrativoStiloFactory attoAmministrativoStiloFactory;

	@Override
	protected void init() {
		super.init();
		attoAmministrativoStiloFactory = new AttoAmministrativoStiloFactory();
	}

	@Override
	protected AttoAmministrativoElab getAttoAmministrativoElabInstance(String line, int lineNumber, String codiceAccount) {
		return attoAmministrativoStiloFactory.newInstanceFromFlussoAttiAmministrativi(line, lineNumber, codiceAccount);
	}
	
	@Override
	protected Class<? extends ElaboraAttoAmministrativoService> getElaboraAttoAmministrativoServiceClass() {
		return ElaboraAttoAmministrativoStiloService.class;
	}
}
