/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.movimentogestione;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.frontend.webservice.msg.movimentogestione.RicercaDettaglioImpegno;
import it.csi.siac.siacbilser.frontend.webservice.msg.movimentogestione.RicercaDettaglioImpegnoResponse;
import it.csi.siac.siacbilser.integration.dad.ImpegnoBilDad;
import it.csi.siac.siacfinser.model.Impegno;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaDettaglioImpegnoService extends RicercaDettaglioMovimentoGestioneService<Impegno, RicercaDettaglioImpegno, RicercaDettaglioImpegnoResponse>
{
	
	private @Autowired ImpegnoBilDad impegnoBilDad;
	
	@Override
	@Transactional
	public RicercaDettaglioImpegnoResponse executeService(RicercaDettaglioImpegno serviceRequest) {
		return super.executeService(serviceRequest);
	}

	@Override
	protected void execute() {
		Impegno impegno = impegnoBilDad.ricercaDettaglioImpegno(req.getImpegno(), req.getImpegnoModelDetails());
		res.setImpegno(impegno);
	}

}
