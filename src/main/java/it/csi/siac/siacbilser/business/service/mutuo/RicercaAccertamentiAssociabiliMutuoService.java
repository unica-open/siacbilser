/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.mutuo;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.RicercaAccertamentiAssociabiliMutuo;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.RicercaAccertamentiAssociabiliMutuoResponse;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siacfin2ser.model.AccertamentoModelDetail;
import it.csi.siac.siacfinser.model.Accertamento;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaAccertamentiAssociabiliMutuoService 
	extends RicercaMovimentiGestioneMutuoService<Accertamento, RicercaAccertamentiAssociabiliMutuo, RicercaAccertamentiAssociabiliMutuoResponse> {

	@Override
	@Transactional(readOnly = true)
	public RicercaAccertamentiAssociabiliMutuoResponse executeService(RicercaAccertamentiAssociabiliMutuo serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void execute() {
		ListaPaginata<Accertamento> listaAccertamenti = 
				mutuoDad.ricercaAccertamentiAssociabiliMutuo(
						req.getMovimentoGestione(), req.getParametriPaginazione(), 
						AccertamentoModelDetail.Dettaglio, 
						AccertamentoModelDetail.DettaglioAccertamentoCapitolo);
		
		res.setAccertamenti(listaAccertamenti);
		res.setCardinalitaComplessiva(listaAccertamenti.size());
	}
}
