/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.inviofatturapa.factory;

import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.sirfelser.frontend.webservice.msg.inviofatturapa.services.model.DatiDocumentiCorrelatiType;
import it.csi.siac.sirfelser.model.FatturaFEL;
import it.csi.siac.sirfelser.model.FattureCollegateFEL;

public final class InvioFatturaPAFattureCollegateFELFactory {
	
	/** Costruttore vuoto privato per non permettere l'instanziazione */
	private InvioFatturaPAFattureCollegateFELFactory() {
		// Previene l'instanziazione
	}

	public static FattureCollegateFEL init(FatturaFEL fatturaFEL, Integer progressivo, DatiDocumentiCorrelatiType datiDocumentiCorrelatiType, Ente ente) {
		FattureCollegateFEL fattureCollegateFEL = new FattureCollegateFEL();
		fattureCollegateFEL.setProgressivo(progressivo);
		fattureCollegateFEL.setEnte(ente);
		fattureCollegateFEL.setFattura(fatturaFEL);
		
		if(datiDocumentiCorrelatiType == null) {
			return fattureCollegateFEL;
		}
		
		fattureCollegateFEL.setNumero(datiDocumentiCorrelatiType.getIdDocumento());
		fattureCollegateFEL.setCodiceCup(datiDocumentiCorrelatiType.getCodiceCUP());
		fattureCollegateFEL.setCodiceCig(datiDocumentiCorrelatiType.getCodiceCIG());
		
		setData(fattureCollegateFEL, datiDocumentiCorrelatiType);
		
		return fattureCollegateFEL;
	}

	private static void setData(FattureCollegateFEL fattureCollegateFEL, DatiDocumentiCorrelatiType datiDocumentiCorrelatiType) {
		if(datiDocumentiCorrelatiType.getData() != null) {
			fattureCollegateFEL.setData(datiDocumentiCorrelatiType.getData().toGregorianCalendar().getTime());
		}
	}

}
