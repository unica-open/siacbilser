/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.mutuo;

import java.util.List;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.RicercaAccertamentiAssociatiMutuo;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.RicercaAccertamentiAssociatiMutuoResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.RicercaImpegniAssociatiMutuo;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.RicercaImpegniAssociatiMutuoResponse;
import it.csi.siac.siacbilser.model.mutuo.AccertamentoAssociatoMutuo;
import it.csi.siac.siacbilser.model.mutuo.ImpegnoAssociatoMutuo;
import it.csi.siac.siacfinser.model.Accertamento;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaAccertamentiAssociatiMutuoService extends RicercaMovimentiGestioneAssociatiMutuoService<Accertamento,AccertamentoAssociatoMutuo, RicercaAccertamentiAssociatiMutuo, RicercaAccertamentiAssociatiMutuoResponse> {

	@Override
	@Transactional(readOnly = true)
	public RicercaAccertamentiAssociatiMutuoResponse executeService(RicercaAccertamentiAssociatiMutuo serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void execute() {
		super.execute();
		
		List<AccertamentoAssociatoMutuo> listaAccertamentiAssociatiMutuo = mutuoDad.ricercaAccertamentiAssociatiMutuo(req.getMutuo());
		res.setAccertamentiAssociatiMutuo(listaAccertamentiAssociatiMutuo);
		
	}
}
