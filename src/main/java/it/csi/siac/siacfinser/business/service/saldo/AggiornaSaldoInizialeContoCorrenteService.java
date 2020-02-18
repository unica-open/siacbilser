/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.business.service.saldo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import it.csi.siac.siacbilser.business.service.base.ExtendedBaseService;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaSaldoInizialeContoCorrente;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaSaldoInizialeContoCorrenteResponse;
import it.csi.siac.siacfinser.integration.dad.SaldoDad;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AggiornaSaldoInizialeContoCorrenteService
		extends ExtendedBaseService<AggiornaSaldoInizialeContoCorrente, AggiornaSaldoInizialeContoCorrenteResponse>
{
	@Autowired
	private SaldoDad saldoDad;

	@Override
	protected void execute()
	{
		saldoDad.aggiornaSaldoIniziale(req.getVociContoCorrente(), req.getEnte(),
				req.getRichiedente().getOperatore().getCodiceFiscale());

	}

}