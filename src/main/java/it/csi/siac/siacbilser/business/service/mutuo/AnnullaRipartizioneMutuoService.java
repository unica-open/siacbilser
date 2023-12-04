/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.mutuo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.AnnullaRipartizioneMutuo;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.AnnullaRipartizioneMutuoResponse;
import it.csi.siac.siacbilser.integration.dad.BilancioDad;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AnnullaRipartizioneMutuoService extends BaseAggiornaAnnullaRipartizioneMutuoService<AnnullaRipartizioneMutuo, AnnullaRipartizioneMutuoResponse> {
	
	protected @Autowired BilancioDad bilancioDad;

	@Override
	@Transactional
	public AnnullaRipartizioneMutuoResponse executeService(AnnullaRipartizioneMutuo serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void execute() {
		super.execute();

		mutuoDad.annullaRipartizioneMutuo(mutuo);
	}
}
