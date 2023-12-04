/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.business.service.oil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.AsyncBaseService;
import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siacfinser.frontend.webservice.msg.GeneraXmlOrdinativiMif;
import it.csi.siac.siacfinser.frontend.webservice.msg.GeneraXmlOrdinativiMifResponse;
import it.csi.siac.siacfinser.integration.dad.oil.GeneraXmlOrdinativiMifDad;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class GeneraXmlOrdinativiMifService
		extends CheckedAccountBaseService<GeneraXmlOrdinativiMif, GeneraXmlOrdinativiMifResponse>
{
	@Autowired
	private GeneraXmlOrdinativiMifDad generaXmlOrdinativiMifDad;

	@Override
	protected void checkServiceParam() throws ServiceParamError
	{
		checkNotNull(req.getIdEnte(), "idEnte");
		checkNotNull(req.getIdElaborazione(), "idElaborazione");
		checkNotNull(req.getLimitOrdinativi(), "limitOrdinativi");
		checkNotNull(req.getOffsetOrdinativi(), "offsetOrdinativi");
	}

	@Transactional(timeout=AsyncBaseService.TIMEOUT)
	@Override
	public GeneraXmlOrdinativiMifResponse executeService(GeneraXmlOrdinativiMif serviceRequest)
	{
		return super.executeService(serviceRequest);
	}

	@Override
	protected void execute()
	{
		String xml = generaXmlOrdinativiMifDad.createXml(req.getIdEnte(), req.getIdElaborazione(), req.getAnnoEsercizio(), req.getCodiceIstat(),
				req.getLimitOrdinativi(), req.getOffsetOrdinativi());

		res.setXml(xml);

		res.setEsito(Esito.SUCCESSO);
	}

}
