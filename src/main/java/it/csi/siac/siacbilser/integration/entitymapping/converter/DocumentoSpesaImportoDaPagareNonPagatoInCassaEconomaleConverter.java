/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacTDoc;
import it.csi.siac.siacbilser.integration.entity.SiacTSubdoc;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;

/**
 * The Class DocumentoSpesaImportoDaPagareNonPagatoInCassaEconomaleConverter.
 */
@Component
public class DocumentoSpesaImportoDaPagareNonPagatoInCassaEconomaleConverter extends ExtendedDozerConverter<DocumentoSpesa, SiacTDoc> {
	
	/**
	 * Instantiates a new documento spesa codice pcc converter.
	 */
	public DocumentoSpesaImportoDaPagareNonPagatoInCassaEconomaleConverter() {
		super(DocumentoSpesa.class, SiacTDoc.class);
	}

	@Override
	public DocumentoSpesa convertFrom(SiacTDoc src, DocumentoSpesa dest) {
		if(!Boolean.TRUE.equals(src.getDocCollegatoCec())) {
			return dest;
		}
		
		// Somma degli importi da pagare (campo derivato del subdocumento di spesa) delle quote, escluse quelle gia' pagate in cassa economale (Subdocumento.pagatoinCEC=true)
		BigDecimal totale = BigDecimal.ZERO;
		if(src.getSiacTSubdocs() != null) {
			for(SiacTSubdoc siacTSubdoc : src.getSiacTSubdocs()) {
				if(!Boolean.TRUE.equals(siacTSubdoc.getSubdocPagatoCec())) {
					// importo da pagare = importo - importo da dedurre
					BigDecimal importo = initBigDecimal(siacTSubdoc.getSubdocImporto());
					BigDecimal importoDaDedurre = initBigDecimal(siacTSubdoc.getSubdocImportoDaDedurre());
					
					BigDecimal augend = importo.subtract(importoDaDedurre);
					totale = totale.add(augend);
				}
			}
		}
		dest.setImportoDaPagareNonPagatoInCassaEconomale(totale);
		
		return dest;
	}
	
	private BigDecimal initBigDecimal(BigDecimal bd) {
		return bd != null ? bd : BigDecimal.ZERO;
	}

	@Override
	public SiacTDoc convertTo(DocumentoSpesa src, SiacTDoc dest) {
		// Nulla da fare
		return dest;
	}

}
