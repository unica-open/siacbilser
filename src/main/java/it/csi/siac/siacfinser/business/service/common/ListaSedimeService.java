/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.business.service.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siacfinser.business.service.AbstractBaseService;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListaSedime;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListaSedimeResponse;
import it.csi.siac.siacfinser.integration.dad.CommonDad;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ListaSedimeService extends AbstractBaseService<ListaSedime, ListaSedimeResponse> {
	
	
	@Autowired
	CommonDad commonDad;
	
	@Override
	protected void init() {
		
		
	}
	
//	@Override
//	@Transactional(readOnly=true)
//	public ListaSedimeResponse executeService(ListaSedime serviceRequest) {
//		return super.executeService(serviceRequest);
//	}
	
	@Override
	public void execute() {
		String methodName = "ListaSedimeService - execute()";
		Ente ente = req.getRichiedente().getAccount().getEnte();
		
		/*
		 * utilizzato da ajax per avere una lista di sedimi in like (ad es: via, viale....) 
		 */
		
		res.setListaSedime(commonDad.findSedimeLike(req.getDescrizioneSedime(), ente));
	}

	
	@Override
	protected void checkServiceParam() throws ServiceParamError {	
		
	}
}
