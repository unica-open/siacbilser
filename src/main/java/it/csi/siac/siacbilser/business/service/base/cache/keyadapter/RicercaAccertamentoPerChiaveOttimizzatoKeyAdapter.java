/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.base.cache.keyadapter;

import it.csi.siac.siaccommonser.business.service.base.cache.keyadapter.BaseKeyAdapter;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siacfinser.frontend.webservice.msg.DatiOpzionaliCapitoli;
import it.csi.siac.siacfinser.frontend.webservice.msg.DatiOpzionaliElencoSubTuttiConSoloGliIds;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.model.ric.RicercaAccertamentoK;

public class RicercaAccertamentoPerChiaveOttimizzatoKeyAdapter extends BaseKeyAdapter<RicercaAccertamentoPerChiaveOttimizzato> {
	
	private final Ente ente;
	
	public RicercaAccertamentoPerChiaveOttimizzatoKeyAdapter(Ente ente) {
		this.ente = ente;
	}
	
	@Override
	public void computeKeyBase(RicercaAccertamentoPerChiaveOttimizzato r) {
		
		append(ente);
		
		// Ricerca impegno K
		if(r.getpRicercaAccertamentoK() != null) {
			RicercaAccertamentoK rik = r.getpRicercaAccertamentoK();
			
			append(rik.getAnnoEsercizio());
			append(rik.getAnnoAccertamento());
			append(rik.getNumeroAccertamento());
			append(rik.getNumeroSubDaCercare());
		} else {
			append("NULLpRicercaAccertamentoK");
		}
		
		// Dati opzionali elenco subaccertamenti
		if(r.getDatiOpzionaliElencoSubTuttiConSoloGliIds() != null) {
			DatiOpzionaliElencoSubTuttiConSoloGliIds dop = r.getDatiOpzionaliElencoSubTuttiConSoloGliIds();
			
			append(dop.isCaricaCig());
			append(dop.isCaricaCup());
			append(dop.isCaricaDisponibileFinanziare());
			append(dop.isCaricaDisponibileLiquidareEDisponibilitaInModifica());
			append(dop.isCaricaDisponibileFinanziare());
			append(dop.isCaricaDisponibilePagare());
			append(dop.isCaricaElencoModificheMovGest());
			append(dop.isEscludiAnnullati());
			append(dop.isCaricaElencoModificheMovGest());
		} else {
			append("NULLDatiOpzionaliElencoSubTuttiConSoloGliIds");
		}
		
		append(r.getNumPagina());
		append(r.getNumRisultatiPerPagina());
		append(r.isCaricaSub());
		append(r.isEscludiSubAnnullati());
		append(r.isPaginazioneSuDatiMinimi());
		append(r.isSubPaginati());
		
		if(r.getDatiOpzionaliCapitoli() != null) {
			DatiOpzionaliCapitoli doc = r.getDatiOpzionaliCapitoli();
			
			append(doc.getImportiDerivatiRichiesti());
			append(doc.getTipologieClassificatoriRichiesti());
		} else {
			append("NULLDatiOpzionaliCapitoli");
		}
	}
	
}
