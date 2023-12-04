/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.inviofatturapa.factory;

import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.sirfelser.frontend.webservice.msg.inviofatturapa.services.model.DatiDocumentiCorrelatiType;
import it.csi.siac.sirfelser.model.DettaglioOrdineAcquistoFEL;
import it.csi.siac.sirfelser.model.FatturaFEL;
import it.csi.siac.sirfelser.model.OrdineAcquistoFEL;

public final class InvioFatturaPAOrdineAcquistoFELFactory {

	/** Costruttore vuoto privato per non permettere l'instanziazione */
	private InvioFatturaPAOrdineAcquistoFELFactory() {
		// Previene l'instanziazione
	}
	
	public static OrdineAcquistoFEL init(FatturaFEL fatturaFEL, DatiDocumentiCorrelatiType datiDocumentiCorrelatiType, Ente ente) {
		OrdineAcquistoFEL ordineAcquistoFEL = new OrdineAcquistoFEL();
		ordineAcquistoFEL.setEnte(ente);
		ordineAcquistoFEL.setFatturaFEL(fatturaFEL);
		
		if(datiDocumentiCorrelatiType == null) {
			return ordineAcquistoFEL;
		}
		
		ordineAcquistoFEL.setNumeroDocumento(datiDocumentiCorrelatiType.getIdDocumento());
		ordineAcquistoFEL.setNumeroVoce(datiDocumentiCorrelatiType.getNumItem());
		ordineAcquistoFEL.setCodiceCommessaConvenzione(datiDocumentiCorrelatiType.getCodiceCommessaConvenzione());
		ordineAcquistoFEL.setCup(datiDocumentiCorrelatiType.getCodiceCUP());
		ordineAcquistoFEL.setCig(datiDocumentiCorrelatiType.getCodiceCIG());
		
		setDataDocumento(ordineAcquistoFEL, datiDocumentiCorrelatiType);
		
		return ordineAcquistoFEL;
	}

	private static void setDataDocumento(OrdineAcquistoFEL ordineAcquistoFEL, DatiDocumentiCorrelatiType datiDocumentiCorrelatiType) {
		if(datiDocumentiCorrelatiType.getData() != null) {
			ordineAcquistoFEL.setDataDocumento(datiDocumentiCorrelatiType.getData().toGregorianCalendar().getTime());
		}
	}

	public static DettaglioOrdineAcquistoFEL initDettaglio(OrdineAcquistoFEL ordineAcquistoFEL, Integer numeroDettaglio, Ente ente) {
		DettaglioOrdineAcquistoFEL dettaglioOrdineAcquistoFEL = new DettaglioOrdineAcquistoFEL();
		
		dettaglioOrdineAcquistoFEL.setEnte(ente);
		dettaglioOrdineAcquistoFEL.setNumeroDettaglio(numeroDettaglio);
		dettaglioOrdineAcquistoFEL.setOrdineAcquistoFEL(ordineAcquistoFEL);
		
		return dettaglioOrdineAcquistoFEL;
	}
	
}
