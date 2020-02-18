/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacintegser.business.service.ricerche.soggetto;

import it.csi.siac.siacfinser.business.service.soggetto.RicercaSoggettiService;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggetti;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettiResponse;
import it.csi.siac.siacintegser.business.service.base.IntegBaseService;
import it.csi.siac.siacintegser.business.service.util.converter.IntegMapId;
import it.csi.siac.siacintegser.frontend.webservice.msg.ricerche.soggetto.BaseRicercaSoggetti;
import it.csi.siac.siacintegser.frontend.webservice.msg.ricerche.soggetto.BaseRicercaSoggettiResponse;

public abstract class BaseRicercaSoggettiService<RS extends BaseRicercaSoggetti, RSRES extends BaseRicercaSoggettiResponse>
		extends IntegBaseService<RS, RSRES>
{
	protected RicercaSoggettiResponse ricercaSoggetti(RS ireq, IntegMapId mapId)
	{
		RicercaSoggetti req = map(ireq, RicercaSoggetti.class, mapId);

		req.setEnte(ente);

		RicercaSoggettiResponse res = appCtx.getBean(RicercaSoggettiService.class).executeService(
				req);

		checkBusinessServiceResponse(res);

		return res;
	}
}
