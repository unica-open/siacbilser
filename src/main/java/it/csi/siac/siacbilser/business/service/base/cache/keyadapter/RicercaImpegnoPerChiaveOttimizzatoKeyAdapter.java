/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.base.cache.keyadapter;

import it.csi.siac.siaccommonser.business.service.base.cache.keyadapter.BaseKeyAdapter;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siacfinser.frontend.webservice.msg.DatiOpzionaliCapitoli;
import it.csi.siac.siacfinser.frontend.webservice.msg.DatiOpzionaliElencoSubTuttiConSoloGliIds;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.model.ric.RicercaImpegnoK;

public class RicercaImpegnoPerChiaveOttimizzatoKeyAdapter extends BaseKeyAdapter<RicercaImpegnoPerChiaveOttimizzato> {
	
	private final Ente ente;
	
	public RicercaImpegnoPerChiaveOttimizzatoKeyAdapter(Ente ente) {
		this.ente = ente;
	}
	
	@Override
	public void computeKeyBase(RicercaImpegnoPerChiaveOttimizzato r) {
		
		append(ente);
		
		// Ricerca impegno K
		if(r.getpRicercaImpegnoK() != null) {
			RicercaImpegnoK rik = r.getpRicercaImpegnoK();
			
			append(rik.getAnnoEsercizio());
			append(rik.getAnnoImpegno());
			append(rik.getNumeroImpegno());
			append(rik.getCaricaDatiUlteriori());
			append(rik.getCaricaSediEModalitaPagamento());
			append(rik.getNumeroSubDaCercare());
		} else {
			append("NULLpRicercaImpegnoK");
		}
		
		// Dati opzionali elenco subimpegni
		if(r.getDatiOpzionaliElencoSubTuttiConSoloGliIds() != null) {
			DatiOpzionaliElencoSubTuttiConSoloGliIds dop = r.getDatiOpzionaliElencoSubTuttiConSoloGliIds();
			
			append(dop.isCaricaCig());
			append(dop.isCaricaCup());
			append(dop.isCaricaDisponibileFinanziare());
			append(dop.isCaricaDisponibileLiquidareEDisponibilitaInModifica());
			append(dop.isCaricaDisponibileFinanziare());
			append(dop.isCaricaDisponibilePagare());
			append(dop.isCaricaElencoModificheMovGest());
			append(dop.isCaricaMutui());
			append(dop.isCaricaVociMutuo());
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
