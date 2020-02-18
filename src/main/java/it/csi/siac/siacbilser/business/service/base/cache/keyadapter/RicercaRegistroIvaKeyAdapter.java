/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.base.cache.keyadapter;

import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siaccommonser.business.service.base.cache.keyadapter.BaseKeyAdapter;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaRegistroIva;
import it.csi.siac.siacfin2ser.model.AttivitaIva;
import it.csi.siac.siacfin2ser.model.RegistroIva;

public class RicercaRegistroIvaKeyAdapter extends BaseKeyAdapter<RicercaRegistroIva> {
	@Override
	public void computeKeyBase(RicercaRegistroIva r) {
		RegistroIva regIva = r.getRegistroIva();
		
		append(regIva.getEnte());
		append(regIva.getGruppoAttivitaIva());
		append(regIva.getTipoRegistroIva());
		
		List<String> listaUidAttivitaIva = new ArrayList<String>();
		if(regIva.getGruppoAttivitaIva() != null && regIva.getGruppoAttivitaIva().getListaAttivitaIva() != null) {
			for(AttivitaIva ai : regIva.getGruppoAttivitaIva().getListaAttivitaIva()) {
				if(ai != null && ai.getUid() != 0) {
					listaUidAttivitaIva.add(Integer.toString(ai.getUid()));
				}
			}
		}
		
		append(listaUidAttivitaIva.toString());
	}
	
}
