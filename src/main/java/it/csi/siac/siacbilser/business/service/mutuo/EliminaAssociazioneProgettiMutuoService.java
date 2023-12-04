/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.mutuo;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.EliminaAssociazioneProgettiMutuo;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.EliminaAssociazioneProgettiMutuoResponse;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class EliminaAssociazioneProgettiMutuoService extends 
	BaseInserisciEliminaAssociazioneProgettiMutuoService<EliminaAssociazioneProgettiMutuo, EliminaAssociazioneProgettiMutuoResponse> {
	
	@Override
	@Transactional
	public EliminaAssociazioneProgettiMutuoResponse executeService(EliminaAssociazioneProgettiMutuo serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void execute() {
		
		super.execute();
		
		for (int progettoId : req.getElencoIdProgetti()) {
			mutuoDad.eliminaAssociazioneProgettoMutuo(mutuo, progettoId);
		}
	}
}