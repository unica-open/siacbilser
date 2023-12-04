/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacintegser.business.service.base;

import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacintegser.frontend.webservice.msg.base.ricerche.RicercaPaginataRequest;
import it.csi.siac.siacintegser.frontend.webservice.msg.base.ricerche.RicercaPaginataResponse;

public abstract class RicercaPaginataBaseService<RPREQ extends RicercaPaginataRequest, RPRES extends RicercaPaginataResponse>
		extends IntegBaseService<RPREQ, RPRES>
{

	@Override
	protected void checkServiceBaseParameters(RPREQ rpreq) throws ServiceParamError
	{
		assertParamNotNull(rpreq.getNumeroPagina(),
				ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("numero pagina"));
		
		assertParamNotNull(rpreq.getNumeroElementiPerPagina(),
				ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("numero elementi per pagina"));
		
		assertParamCondition(
				rpreq.getNumeroElementiPerPagina() <= 5,
				ErroreCore.VALORE_NON_CONSENTITO.getErrore("numero elementi per pagina",
						rpreq.getNumeroElementiPerPagina()));

		super.checkServiceBaseParameters(rpreq);
	}

}
