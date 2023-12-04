/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/

package it.csi.siac.siacbilser.business.service.mutuo;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.RicercaImpegniAssociabiliMutuo;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.RicercaImpegniAssociabiliMutuoResponse;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siacfin2ser.model.ImpegnoModelDetail;
import it.csi.siac.siacfinser.model.Impegno;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaImpegniAssociabiliMutuoService extends RicercaMovimentiGestioneMutuoService<Impegno, RicercaImpegniAssociabiliMutuo, RicercaImpegniAssociabiliMutuoResponse> {

	@Override
	@Transactional(readOnly = true)
	public RicercaImpegniAssociabiliMutuoResponse executeService(RicercaImpegniAssociabiliMutuo serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void execute() {
		super.execute();
		
		ListaPaginata<Impegno> listaImpegni = mutuoDad.ricercaImpegniAssociabiliMutuo(req.getMovimentoGestione(), req.getParametriPaginazione(),
				ImpegnoModelDetail.Dettaglio,ImpegnoModelDetail.DettaglioImpegnoCapitolo);
		res.setImpegni(listaImpegni);
		
		res.setCardinalitaComplessiva(listaImpegni.size());
	}
}
