/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacintegser.business.service.util.converter;


import java.math.BigDecimal;

import org.dozer.DozerConverter;
import org.springframework.stereotype.Component;

import it.csi.siac.siacfin2ser.model.DocumentoSpesa;

/**
 * The Class DocumentoSpesaImportoDaPagareNonPagatoInCassaEconomaleConverter.
 */
@Component
public class DocumentoSpesaImportoNettoConverter extends DozerConverter< DocumentoSpesa, it.csi.siac.siacintegser.model.integ.DocumentoSpesa>  {
	
	/**
	 * Instantiates a new documento spesa codice pcc converter.
	 */
	public DocumentoSpesaImportoNettoConverter() {
		super( DocumentoSpesa.class, it.csi.siac.siacintegser.model.integ.DocumentoSpesa.class);
	}

	@Override
	public DocumentoSpesa convertFrom(it.csi.siac.siacintegser.model.integ.DocumentoSpesa src, DocumentoSpesa dest) {
		return dest;
	}
	
	private BigDecimal initBigDecimal(BigDecimal bd) {
		return bd != null ? bd : BigDecimal.ZERO;
	}

	@Override
	public it.csi.siac.siacintegser.model.integ.DocumentoSpesa convertTo(
			DocumentoSpesa src, it.csi.siac.siacintegser.model.integ.DocumentoSpesa dest) {
		
		// Somma degli importi delle note di credito
		
		BigDecimal importoTotaleNoteDiCreditoDaDedurre = initBigDecimal(src.calcolaImportoTotaleNoteCollegateSpesa());
		BigDecimal importoDoc =initBigDecimal(src.getImporto());
		BigDecimal arrotondamento =initBigDecimal(src.getArrotondamento());
		
		BigDecimal importoNetto = importoDoc.add(arrotondamento).subtract(importoTotaleNoteDiCreditoDaDedurre);
		dest.setImportoNettoDocumento(importoNetto);
		
		return dest;
	}

}
