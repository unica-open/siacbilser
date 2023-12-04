/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacintegser.business.service.ordinativo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinser.frontend.webservice.OrdinativoService;
import it.csi.siac.siacintegser.business.service.base.ElaboraFileBaseService;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public abstract class GeneraFlussoOrdinativoService extends ElaboraFileBaseService {

	@Autowired
	protected OrdinativoService ordinativoService;

	@Override
	protected void checkServiceParam() throws ServiceParamError {
		super.checkServiceParam();
		checkNotNull(req.getRichiedente().getAccount(),
				ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("account"));
		checkNotNull(req.getRichiedente().getAccount().getEnte(),
				ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente"));
	}

}
