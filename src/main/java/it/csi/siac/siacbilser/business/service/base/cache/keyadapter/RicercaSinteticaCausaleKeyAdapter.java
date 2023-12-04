/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.base.cache.keyadapter;

import it.csi.siac.siaccommonser.business.service.base.cache.keyadapter.BaseKeyAdapter;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaCausale;
import it.csi.siac.siacgenser.model.CausaleEP;
import it.csi.siac.siacgenser.model.Conto;
import it.csi.siac.siacgenser.model.ContoTipoOperazione;
import it.csi.siac.siacgenser.model.Evento;

public class RicercaSinteticaCausaleKeyAdapter extends BaseKeyAdapter<RicercaSinteticaCausale> {
	@Override
	public void computeKeyBase(RicercaSinteticaCausale r) {
		CausaleEP c = r.getCausaleEP();
		
		if(c!=null) {
			ContoTipoOperazione cto = c.getContiTipoOperazione()!=null && !c.getContiTipoOperazione().isEmpty() ? c.getContiTipoOperazione().get(0) : null;
			Conto conto = cto != null ? cto.getConto() : null;
			
			Evento evento = c.getEventi() != null && !c.getEventi().isEmpty() ? c.getEventi().get(0) : null;
		
			append(c.getEnte());
			append(c.getDataInizioValiditaFiltro());
			append(c.getCodice());
			append(c.getDescrizione());
			append(c.getTipoCausale());
			append(c.getStatoOperativoCausaleEP());
			append(conto);
			append(evento);
			append(c.getElementoPianoDeiConti());
			append(c.getSoggetto());
		}
		
		append(r.getTipoEvento());
		append(r.getBilancio());
	}
	
}
