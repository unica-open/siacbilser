/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.inviofatturapa.factory;

import java.math.BigDecimal;
import java.util.List;

import it.csi.siac.siacbilser.business.utility.BilUtilities;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.sirfelser.frontend.webservice.msg.inviofatturapa.services.model.DatiBolloType;
import it.csi.siac.sirfelser.frontend.webservice.msg.inviofatturapa.services.model.DatiGeneraliDocumentoType;
import it.csi.siac.sirfelser.frontend.webservice.msg.inviofatturapa.services.model.DatiGeneraliType;
import it.csi.siac.sirfelser.frontend.webservice.msg.inviofatturapa.services.model.DatiRitenutaType;
import it.csi.siac.sirfelser.frontend.webservice.msg.inviofatturapa.services.model.DatiTrasmissioneType;
import it.csi.siac.sirfelser.frontend.webservice.msg.inviofatturapa.services.model.DettaglioLineeType;
import it.csi.siac.sirfelser.model.FatturaFEL;
import it.csi.siac.sirfelser.model.PrestatoreFEL;
import it.csi.siac.sirfelser.model.TipoDocumentoFEL;

public final class InvioFatturaPAFatturaFELFactory {
	
	/** Costruttore vuoto privato per non permettere l'instanziazione */
	private InvioFatturaPAFatturaFELFactory() {
		// Previene l'instanziazione
	}
	
	public static FatturaFEL init(DatiGeneraliType datiGeneraliType, PrestatoreFEL prestatoreFEL, DatiTrasmissioneType datiTrasmissioneType, List<DettaglioLineeType> dettaglioLineeTypes, Ente ente) {
		FatturaFEL fatturaFEL = new FatturaFEL();
		fatturaFEL.setEnte(ente);
		fatturaFEL.setPrestatore(prestatoreFEL);
		
		// Dati generali
		if(datiGeneraliType != null) {
			DatiGeneraliDocumentoType datiGeneraliDocumentoType = datiGeneraliType.getDatiGeneraliDocumento();
			DatiRitenutaType datiRitenutaType = datiGeneraliDocumentoType.getDatiRitenuta();
			DatiBolloType datiBolloType = datiGeneraliDocumentoType.getDatiBollo();
			
			fatturaFEL.setNumero(datiGeneraliDocumentoType.getNumero());
			fatturaFEL.setDivisa(datiGeneraliDocumentoType.getDivisa());
			setData(fatturaFEL, datiGeneraliDocumentoType);
			
			if (datiRitenutaType != null) {
				fatturaFEL.setImportoRitenuta(BilUtilities.arrotondaAllaSecondaCifra(datiRitenutaType.getImportoRitenuta()));
				fatturaFEL.setAliquotaRitenuta(datiRitenutaType.getAliquotaRitenuta());
				
				setTipoRitenuta(fatturaFEL, datiRitenutaType);
				setCausalePagamento(fatturaFEL, datiRitenutaType);
			}
			
			if (datiBolloType != null) {
				fatturaFEL.setImportoBollo(datiBolloType.getImportoBollo());
				setBolloVirtuale(fatturaFEL, datiBolloType);
			}
			fatturaFEL.setImportoTotaleDocumento(BilUtilities.arrotondaAllaSecondaCifra(datiGeneraliDocumentoType.getImportoTotaleDocumento()));
			fatturaFEL.setArrotondamento(BilUtilities.arrotondaAllaSecondaCifra(datiGeneraliDocumentoType.getArrotondamento()));
			
			setTipoDocumentoFEL(fatturaFEL, datiGeneraliDocumentoType);
		}
		
		if(datiTrasmissioneType != null) {
			fatturaFEL.setCodiceTrasmittente(datiTrasmissioneType.getIdTrasmittente().getIdCodice());
			fatturaFEL.setCodiceDestinatario(datiTrasmissioneType.getCodiceDestinatario());
		}
		
		BigDecimal importoTotaleNetto = BigDecimal.ZERO;
		if(dettaglioLineeTypes != null && !dettaglioLineeTypes.isEmpty()) {
			for (DettaglioLineeType linea : dettaglioLineeTypes) {
				importoTotaleNetto = importoTotaleNetto.add(linea.getPrezzoTotale());
			}
		}
		fatturaFEL.setImportoTotaleNetto(BilUtilities.arrotondaAllaSecondaCifra(importoTotaleNetto));
		
		return fatturaFEL;
	}

	private static void setData(FatturaFEL fatturaFEL, DatiGeneraliDocumentoType datiGeneraliDocumentoType) {
		if(datiGeneraliDocumentoType.getData() != null) {
			fatturaFEL.setData(datiGeneraliDocumentoType.getData().toGregorianCalendar().getTime());
		}
	}
	
	private static void setTipoRitenuta(FatturaFEL fatturaFEL, DatiRitenutaType datiRitenutaType) {
		if(datiRitenutaType.getTipoRitenuta() != null) {
			fatturaFEL.setTipoRitenuta(datiRitenutaType.getTipoRitenuta().value());
		}
	}
	
	private static void setCausalePagamento(FatturaFEL fatturaFEL, DatiRitenutaType datiRitenutaType) {
		if(datiRitenutaType.getCausalePagamento() != null) {
			fatturaFEL.setCausalePagamento(datiRitenutaType.getCausalePagamento().value());
		}
	}
	
	private static void setBolloVirtuale(FatturaFEL fatturaFEL, DatiBolloType datiBolloType) {
		if(datiBolloType.getBolloVirtuale() != null) {
			fatturaFEL.setBolloVirtuale(datiBolloType.getBolloVirtuale().value());
		}
	}
	
	private static void setTipoDocumentoFEL(FatturaFEL fatturaFEL, DatiGeneraliDocumentoType datiGeneraliDocumentoType) {
		if(datiGeneraliDocumentoType.getTipoDocumento() != null) {
			fatturaFEL.setTipoDocumentoFEL(TipoDocumentoFEL.byCodice(datiGeneraliDocumentoType.getTipoDocumento().value()));
		}
	}
	
}
