/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.base.cache.keyadapter;

import it.csi.siac.siaccommonser.business.service.base.cache.keyadapter.BaseKeyAdapter;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentiSubAccertamenti;

public class RicercaAccertamentiSubAccertamentiKeyAdapter extends BaseKeyAdapter<RicercaAccertamentiSubAccertamenti> {
	
	@Override
	public void computeKeyBase(RicercaAccertamentiSubAccertamenti r) {
		append(r.getEnte());
		append(r.getParametroRicercaAccSubAcc().getAnnoEsercizio());
		append(r.getParametroRicercaAccSubAcc().getAnnoAccertamento());
		append(r.getParametroRicercaAccSubAcc().isDisponibilitaAdIncassare());
		append(r.getParametroRicercaAccSubAcc().getNumeroCapitolo());
		append(r.getParametroRicercaAccSubAcc().getNumeroArticolo());
		append(r.getParametroRicercaAccSubAcc().getUidCapitolo());
		append(r.getNumRisultatiPerPagina());
		append(r.getNumPagina());
	}
	
}
