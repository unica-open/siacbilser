/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.business.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaStrutturaAmministrativa;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaStrutturaAmministrativaResponse;
import it.csi.siac.siacfinser.integration.dad.StrutturaAmministrativaDad;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaStrutturaAmministrativaService extends AbstractBaseService<RicercaStrutturaAmministrativa, RicercaStrutturaAmministrativaResponse> {

	@Autowired
	StrutturaAmministrativaDad strutturaAmministrativaDad;
	
	@Override
	protected void init() {
			
	}	

	@Override
	@Transactional(readOnly=true)
	public RicercaStrutturaAmministrativaResponse executeService(RicercaStrutturaAmministrativa serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	public void execute() {
		
		StrutturaAmministrativoContabile sac = new StrutturaAmministrativoContabile();
		
		sac = strutturaAmministrativaDad.getStrutturaAmministrativaByCodice(req.getEnte(), req.getCodice(), req.getTipoCodice());
		
		res.setStrutturaAmministrativoContabile(sac);
	}



}

