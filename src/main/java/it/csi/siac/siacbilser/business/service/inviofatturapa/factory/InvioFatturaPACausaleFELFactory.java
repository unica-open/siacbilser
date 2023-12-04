/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.inviofatturapa.factory;

import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.sirfelser.model.CausaleFEL;
import it.csi.siac.sirfelser.model.FatturaFEL;

public final class InvioFatturaPACausaleFELFactory {
	
	/** Costruttore vuoto privato per non permettere l'instanziazione */
	private InvioFatturaPACausaleFELFactory() {
		// Previene l'instanziazione
	}

	public static CausaleFEL init(FatturaFEL fatturaFEL, Integer progressivo, String causale, Ente ente) {
		CausaleFEL causaleFEL = new CausaleFEL();
		
		causaleFEL.setFattura(fatturaFEL);
		causaleFEL.setProgressivo(progressivo);
		causaleFEL.setCausale(causale);
		causaleFEL.setEnte(ente);
		
		return causaleFEL;
	}
	
}
