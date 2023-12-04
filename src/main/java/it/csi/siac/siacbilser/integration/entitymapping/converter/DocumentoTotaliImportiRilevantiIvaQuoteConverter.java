/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.SiacTSubdocRepository;
import it.csi.siac.siacbilser.integration.entity.SiacTDoc;
import it.csi.siac.siacfin2ser.model.Documento;

/**
 * The Class DocumentoTotaliImportiRilevantiIvaQuoteConverter.
 */
@Component
public class DocumentoTotaliImportiRilevantiIvaQuoteConverter extends ExtendedDozerConverter<Documento<?, ?>, SiacTDoc> {
	
	@Autowired
	private SiacTSubdocRepository siacTSubdocRepository;
	
	/**
	 * Instantiates a new documento totali importi quote converter
	 */
	@SuppressWarnings("unchecked")
	public DocumentoTotaliImportiRilevantiIvaQuoteConverter() {
		super((Class<Documento<?, ?>>)(Class<?>)Documento.class, SiacTDoc.class);
	}

	@Override
	public Documento<?, ?> convertFrom(SiacTDoc src, Documento<?, ?> dest) {
		// Totale rilevante IVA
		BigDecimal importoRilevante = siacTSubdocRepository.sumSubdocImportoRilevanteIvaByDocId(src.getUid());
		BigDecimal importoNonRilevante = siacTSubdocRepository.sumSubdocImportoNonRilevanteIvaByDocId(src.getUid());
		dest.setTotaleImportoRilevanteIvaQuote(importoRilevante);
		dest.setTotaleImportoNonRilevanteIvaQuote(importoNonRilevante);
		return dest;
	}
	
	@Override
	public SiacTDoc convertTo(Documento<?, ?> src, SiacTDoc dest) {
		return dest;
	}

}
