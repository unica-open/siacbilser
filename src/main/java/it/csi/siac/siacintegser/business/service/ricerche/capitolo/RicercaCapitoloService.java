/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacintegser.business.service.ricerche.capitolo;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacintegser.business.service.base.RicercaPaginataBaseService;
import it.csi.siac.siacintegser.frontend.webservice.msg.ricerche.capitolo.RicercaCapitolo;
import it.csi.siac.siacintegser.frontend.webservice.msg.ricerche.capitolo.RicercaCapitoloResponse;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public abstract class RicercaCapitoloService<RPREQ extends RicercaCapitolo, RPRES extends RicercaCapitoloResponse>
		extends RicercaPaginataBaseService<RPREQ,RPRES> {

	@Override
	protected void checkServiceParameters(RPREQ ireq) throws ServiceParamError {		
		
		// controllo parametri in input
		// Obbligatori Numero Capitolo e Numero Articolo 
		assertParamNotNull(ireq.getNumeroCapitolo(),
				ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("numero capitolo"));
		
		assertParamNotNull(ireq.getNumeroArticolo(),
				ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("numero articolo"));
		

	}	


}
