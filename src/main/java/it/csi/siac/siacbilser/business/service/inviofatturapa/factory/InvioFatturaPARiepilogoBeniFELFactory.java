/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.inviofatturapa.factory;

import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.sirfelser.frontend.webservice.msg.inviofatturapa.services.model.DatiRiepilogoType;
import it.csi.siac.sirfelser.model.FatturaFEL;
import it.csi.siac.sirfelser.model.NaturaFEL;
import it.csi.siac.sirfelser.model.RiepilogoBeniFEL;

public final class InvioFatturaPARiepilogoBeniFELFactory {
	
	/** Costruttore vuoto privato per non permettere l'instanziazione */
	private InvioFatturaPARiepilogoBeniFELFactory() {
		// Previene l'instanziazione
	}

	public static RiepilogoBeniFEL init(FatturaFEL fatturaFEL, Integer progressivo, DatiRiepilogoType datiRiepilogo, Ente ente) {
		RiepilogoBeniFEL riepilogoBeniFEL = new RiepilogoBeniFEL();
		riepilogoBeniFEL.setProgressivo(progressivo);
		riepilogoBeniFEL.setEnte(ente);
		riepilogoBeniFEL.setFattura(fatturaFEL);
		
		if(datiRiepilogo == null) {
			return riepilogoBeniFEL;
		}
		
		riepilogoBeniFEL.setAliquotaIva(datiRiepilogo.getAliquotaIVA());
		riepilogoBeniFEL.setSpeseAccessorie(datiRiepilogo.getSpeseAccessorie());
		riepilogoBeniFEL.setArrotondamento(datiRiepilogo.getArrotondamento());
		riepilogoBeniFEL.setImponibileImporto(datiRiepilogo.getImponibileImporto());
		riepilogoBeniFEL.setImposta(datiRiepilogo.getImposta());
		setNaturaFEL(riepilogoBeniFEL, datiRiepilogo);
		setEsigibilitaIva(riepilogoBeniFEL, datiRiepilogo);
		
		return riepilogoBeniFEL;
	}

	private static void setNaturaFEL(RiepilogoBeniFEL riepilogoBeniFEL, DatiRiepilogoType datiRiepilogoType) {
		if(datiRiepilogoType.getNatura() != null) {
			riepilogoBeniFEL.setNaturaFEL(NaturaFEL.byCodice(datiRiepilogoType.getNatura().value()));
		}
	}
	
	private static void setEsigibilitaIva(RiepilogoBeniFEL riepilogoBeniFEL, DatiRiepilogoType datiRiepilogoType) {
		if(datiRiepilogoType.getEsigibilitaIVA() != null) {
			riepilogoBeniFEL.setEsigibilitaIva(datiRiepilogoType.getEsigibilitaIVA().value());
		}
	}

}
