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

import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siacfinser.frontend.webservice.msg.CountOrdinativiMif;
import it.csi.siac.siacfinser.frontend.webservice.msg.CountOrdinativiMifSiopePlusResponse;
import it.csi.siac.siacfinser.integration.dad.oil.CountOrdinativiMifSiopePlusDad;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class CountOrdinativiMifSiopePlusService extends BaseCountOrdinativiMifService<CountOrdinativiMifSiopePlusResponse>
{
	@Autowired
	private CountOrdinativiMifSiopePlusDad countOrdinativiMifSiopePlusDad;

	@Transactional
	@Override
	public CountOrdinativiMifSiopePlusResponse executeService(CountOrdinativiMif serviceRequest)
	{
		return super.executeService(serviceRequest);
	}

	@Override
	protected void execute()
	{
		res.setNumeroOrdinativiPerAnnoEsercizioCodiceIstat(countOrdinativiMifSiopePlusDad.countOrdinativiPerAnnoEsercizioCodiceIstat(req.getIdElaborazione())); 
		res.setEsito(Esito.SUCCESSO);
	}
}
