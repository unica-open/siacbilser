/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.frontend.webservice;

import javax.jws.WebService;

import it.csi.siac.siacbilser.business.service.base.BaseServiceExecutor;
import it.csi.siac.siacbilser.business.service.movimentogestione.RicercaDettaglioAccertamentoService;
import it.csi.siac.siacbilser.business.service.movimentogestione.RicercaDettaglioImpegnoService;
import it.csi.siac.siacbilser.frontend.webservice.msg.movimentogestione.RicercaDettaglioAccertamento;
import it.csi.siac.siacbilser.frontend.webservice.msg.movimentogestione.RicercaDettaglioAccertamentoResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.movimentogestione.RicercaDettaglioImpegno;
import it.csi.siac.siacbilser.frontend.webservice.msg.movimentogestione.RicercaDettaglioImpegnoResponse;
import it.csi.siac.siaccommonser.webservice.BaseWebServiceImpl;

@WebService(serviceName = "MovimentoGestioneBilService",
portName = "MovimentoGestioneBilServicePort",
targetNamespace = BILSvcDictionary.NAMESPACE,
endpointInterface = "it.csi.siac.siacbilser.frontend.webservice.MovimentoGestioneService")
public class MovimentoGestioneServiceImpl extends BaseWebServiceImpl implements MovimentoGestioneService {

	@Override
	public RicercaDettaglioImpegnoResponse ricercaDettaglioImpegno(RicercaDettaglioImpegno parameters) {
		// TODO Auto-generated method stub
		return BaseServiceExecutor.execute(appCtx, RicercaDettaglioImpegnoService.class, parameters);
	}

	@Override
	public RicercaDettaglioAccertamentoResponse ricercaDettaglioAccertamento(RicercaDettaglioAccertamento parameters) {
		// TODO Auto-generated method stub
		return BaseServiceExecutor.execute(appCtx, RicercaDettaglioAccertamentoService.class, parameters);
	}
}
