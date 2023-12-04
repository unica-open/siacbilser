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
 * The Class DocumentoTotaliImportiQuoteConverter.
 */
@Component
public class DocumentoTotaliImportiQuoteConverter extends ExtendedDozerConverter<Documento<?, ?>, SiacTDoc> {
	
	@Autowired
	private SiacTSubdocRepository siacTSubdocRepository;
	
	/**
	 * Instantiates a new documento totali importi quote converter
	 */
	@SuppressWarnings("unchecked")
	public DocumentoTotaliImportiQuoteConverter() {
		super((Class<Documento<?, ?>>)(Class<?>)Documento.class, SiacTDoc.class);
	}

	@Override
	public Documento<?, ?> convertFrom(SiacTDoc src, Documento<?, ?> dest) {
		Object[] totali = siacTSubdocRepository.sumSubdocImportoSubdocImportoDaDedurreByDocIds(src.getUid());
		if(totali != null) {
			dest.setTotaleImportoQuote((BigDecimal) totali[0]);
			dest.setTotaleImportoDaDedurreQuote((BigDecimal) totali[1]);
		}
		
		return dest;
	}
	
	@Override
	public SiacTDoc convertTo(Documento<?, ?> src, SiacTDoc dest) {
		return dest;
	}

}
