/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.capitolo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.frontend.webservice.msg.ContaMovimentiAssociatiACapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.ContaMovimentiAssociatiACapitoloResponse;
import it.csi.siac.siacbilser.integration.dad.CapitoloDad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;


@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ContaMovimentiAssociatiACapitoloService
		extends CheckedAccountBaseService<ContaMovimentiAssociatiACapitolo,ContaMovimentiAssociatiACapitoloResponse >{ 
	
	@Autowired
	private	CapitoloDad capitoloDad;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
	
		checkCondition(req.getIdCapitolo()!=0 , ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("id capitolo"),false);	

	}
	

	@Override
	protected void execute() {	
		Long count = capitoloDad.countMovimentiNonAnnullatiCapitolo(req.getIdCapitolo());
		res.setNumeroMovimenti(count);
	}
}
