/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.base.cache.keyadapter;

import it.csi.siac.siaccommonser.business.service.base.cache.keyadapter.BaseKeyAdapter;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegniSubImpegni;

public class RicercaImpegniSubImpegniKeyAdapter extends BaseKeyAdapter<RicercaImpegniSubImpegni> {
	
	@Override
	public void computeKeyBase(RicercaImpegniSubImpegni r) {
		append(r.getEnte());
		append(r.getParametroRicercaImpSub().getAnnoEsercizio());
		append(r.getParametroRicercaImpSub().getAnnoImpegno());
		append(r.getParametroRicercaImpSub().getIsRicercaDaImpegno());
		append(r.getParametroRicercaImpSub().getNumeroCapitolo());
		append(r.getParametroRicercaImpSub().getNumeroArticolo());
		append(r.getParametroRicercaImpSub().getUidCapitolo());
		append(r.getNumRisultatiPerPagina());
		append(r.getNumPagina());
	}
	
}
