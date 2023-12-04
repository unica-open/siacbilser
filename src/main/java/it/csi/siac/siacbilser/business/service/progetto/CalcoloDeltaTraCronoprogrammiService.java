/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.progetto;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.frontend.webservice.msg.CalcoloDeltaTraCronoprogrammi;
import it.csi.siac.siacbilser.frontend.webservice.msg.CalcoloDeltaTraCronoprogrammiResponse;

// TODO: Auto-generated Javadoc
/**
 * The Class CalcoloDeltaTraCronoprogrammiService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class CalcoloDeltaTraCronoprogrammiService extends CheckedAccountBaseService<CalcoloDeltaTraCronoprogrammi, CalcoloDeltaTraCronoprogrammiResponse> {

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		// TODO Auto-generated method stub

	}

}
