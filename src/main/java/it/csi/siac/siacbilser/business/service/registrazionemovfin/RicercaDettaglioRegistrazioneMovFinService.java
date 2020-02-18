/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.registrazionemovfin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.RegistrazioneMovFinDad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioRegistrazioneMovFin;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioRegistrazioneMovFinResponse;
import it.csi.siac.siacgenser.model.RegistrazioneMovFin;

/**
 * Ricerca sintetica di una RegistrazioneMovFin
 * 
 * @author Valentina
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaDettaglioRegistrazioneMovFinService extends CheckedAccountBaseService<RicercaDettaglioRegistrazioneMovFin, RicercaDettaglioRegistrazioneMovFinResponse> {
	
	@Autowired 
	private RegistrazioneMovFinDad registrazioneMovFinDad;
	
	private RegistrazioneMovFin registrazioneMovFin;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkEntita(req.getRegistrazioneMovFin(), "registrazione");
		registrazioneMovFin = req.getRegistrazioneMovFin();
		
	}
	
	
	@Override
	@Transactional(readOnly=true)
	public RicercaDettaglioRegistrazioneMovFinResponse executeService(RicercaDettaglioRegistrazioneMovFin serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	
	@Override
	protected void init() {
		registrazioneMovFinDad.setEnte(ente);
	}
	
	@Override
	protected void execute() {
		
		 registrazioneMovFin = registrazioneMovFinDad.ricercaDettaglioRegistrazioneMovFin(registrazioneMovFin.getUid());
		 res.setRegistrazioneMovFin(registrazioneMovFin);
		
	}
	

}
