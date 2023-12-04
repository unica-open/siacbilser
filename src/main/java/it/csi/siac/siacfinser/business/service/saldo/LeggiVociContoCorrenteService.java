/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.business.service.saldo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.ExtendedBaseService;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinser.frontend.webservice.msg.LeggiVociContoCorrente;
import it.csi.siac.siacfinser.frontend.webservice.msg.LeggiVociContoCorrenteResponse;
import it.csi.siac.siacfinser.integration.dad.SaldoDad;
import it.csi.siac.siacfinser.model.saldo.VociContoCorrente;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class LeggiVociContoCorrenteService
		extends ExtendedBaseService<LeggiVociContoCorrente, LeggiVociContoCorrenteResponse>
{

	@Autowired
	private SaldoDad saldoDad;

	@Override
	@Transactional
	public LeggiVociContoCorrenteResponse executeService(LeggiVociContoCorrente serviceRequest)
	{
		return super.executeService(serviceRequest);
	}

	@Override
	protected void checkServiceParam() throws ServiceParamError
	{
		checkNotNull(req.getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente"));
		checkNotNull(req.getAnno(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno"));
		checkNotNull(req.getIdClassifContoCorrente(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("contoCorrente").getTesto());
	}

	@Override
	protected void execute()
	{
		VociContoCorrente vociContoCorrente = saldoDad.leggiVociContoCorrente(req.getEnte().getUid(), req.getAnno(),
				req.getIdClassifContoCorrente(), req.getDataInizio(), req.getDataFine());

		res.setVociContoCorrente(vociContoCorrente);
	}
}