/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.inviofatturapa.factory;

import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.sirfelser.frontend.webservice.msg.inviofatturapa.services.model.AltriDatiGestionaliType;
import it.csi.siac.sirfelser.model.DatiGestionaliFEL;
import it.csi.siac.sirfelser.model.FatturaFEL;


public final class InvioFatturaPADatiGestionaliFELFactory {
	
	/** Costruttore vuoto privato per non permettere l'instanziazione */
	private InvioFatturaPADatiGestionaliFELFactory() {
		// Previene l'instanziazione
	}

	public static DatiGestionaliFEL init(FatturaFEL fatturaFEL, Integer progressivo, AltriDatiGestionaliType altriDatiGestionaliType, Ente ente) {
		DatiGestionaliFEL datiGestionaliFEL = new DatiGestionaliFEL();
		datiGestionaliFEL.setProgressivo(progressivo);
		datiGestionaliFEL.setEnte(ente);
		datiGestionaliFEL.setFattura(fatturaFEL);
		
		if(altriDatiGestionaliType == null) {
			return datiGestionaliFEL;
		}
		datiGestionaliFEL.setTipoDato(altriDatiGestionaliType.getTipoDato());
		setRiferimentoData(datiGestionaliFEL, altriDatiGestionaliType);
		
		return datiGestionaliFEL;
	}

	private static void setRiferimentoData(DatiGestionaliFEL datiGestionaliFEL, AltriDatiGestionaliType altriDatiGestionaliType) {
		if (altriDatiGestionaliType.getRiferimentoData() != null) {
			datiGestionaliFEL.setRiferimentoData(altriDatiGestionaliType.getRiferimentoData().toGregorianCalendar().getTime());
		}
	}

}
