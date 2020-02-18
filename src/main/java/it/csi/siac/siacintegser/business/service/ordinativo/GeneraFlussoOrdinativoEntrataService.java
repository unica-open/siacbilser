/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacintegser.business.service.ordinativo;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class GeneraFlussoOrdinativoEntrataService extends GeneraFlussoOrdinativoService
{

	@Override
	protected void initFileData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void elaborateData() {
		// TODO Auto-generated method stub
		
	}

	

}
