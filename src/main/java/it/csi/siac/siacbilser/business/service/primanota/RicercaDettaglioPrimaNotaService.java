/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.primanota;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.PrimaNotaDad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioPrimaNota;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioPrimaNotaResponse;
import it.csi.siac.siacgenser.model.PrimaNota;

/**
 * Ricerca di dettaglio di una PrimaNota
 * 
 * @author Valentina
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaDettaglioPrimaNotaService extends CheckedAccountBaseService<RicercaDettaglioPrimaNota, RicercaDettaglioPrimaNotaResponse> {

	
	@Autowired
	private PrimaNotaDad primaNotaDad;

	private PrimaNota primaNota;
	
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkEntita(req.getPrimaNota(), "prima nota");
		primaNota = req.getPrimaNota();
	}
	
	
	
	@Override
	@Transactional(readOnly=true)
	public RicercaDettaglioPrimaNotaResponse executeService(RicercaDettaglioPrimaNota serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	
	@Override
	protected void execute() {
		// SIAC-5356
		primaNota = primaNotaDad.findPrimaNotaDettaglioContoByUid(primaNota.getUid());
		res.setPrimaNota(primaNota);
	}
	
	
}
