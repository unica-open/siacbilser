/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacintegser.business.service.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import it.csi.siac.siaccorser.frontend.webservice.util.ServiceUtils;
import it.csi.siac.siaccorser.model.ServiceResponse;

public abstract class IntegServiceHelper {
	
	@Autowired protected ApplicationContext appCtx;	

	protected <RES extends ServiceResponse> void checkServiceResponse(RES res)	
	{
		ServiceUtils.checkServiceResponse(res);
	}
}
