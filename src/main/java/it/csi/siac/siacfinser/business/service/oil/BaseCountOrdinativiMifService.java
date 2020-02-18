/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.business.service.oil;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siacfinser.frontend.webservice.msg.BaseCountOrdinativiMifResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.CountOrdinativiMif;

public abstract class BaseCountOrdinativiMifService<COMRES extends BaseCountOrdinativiMifResponse>
		extends CheckedAccountBaseService<CountOrdinativiMif, COMRES>
{
	@Override
	protected void checkServiceParam() throws ServiceParamError
	{
		checkNotNull(req.getIdElaborazione(), "idElaborazione");
	}
}
