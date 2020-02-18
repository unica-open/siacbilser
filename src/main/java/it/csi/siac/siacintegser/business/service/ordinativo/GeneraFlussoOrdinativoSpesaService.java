/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacintegser.business.service.ordinativo;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacintegser.frontend.webservice.msg.ElaboraFile;
import it.csi.siac.siacintegser.frontend.webservice.msg.ElaboraFileResponse;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class GeneraFlussoOrdinativoSpesaService extends GeneraFlussoOrdinativoService
{

	@Override
	@Transactional
	public ElaboraFileResponse executeService(ElaboraFile serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void initFileData() {
		// TODO Auto-generated method stub
	}




	@Override
	protected void elaborateData() {
		// TODO Auto-generated method stub
	}
	
	




	
}
