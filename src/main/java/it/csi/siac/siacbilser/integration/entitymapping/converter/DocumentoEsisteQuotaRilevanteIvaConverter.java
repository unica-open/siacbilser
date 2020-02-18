/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.SiacTSubdocRepository;
import it.csi.siac.siacbilser.integration.entity.SiacTDoc;
import it.csi.siac.siacfin2ser.model.Documento;

/**
 * The Class DocumentoEsisteQuotaRilevanteIvaConverter.
 */
@Component
public class DocumentoEsisteQuotaRilevanteIvaConverter extends ExtendedDozerConverter<Documento<?, ?>, SiacTDoc> {
	
	@Autowired
	private SiacTSubdocRepository siacTSubdocRepository;
	
	/**
	 * Instantiates a new documento esiste quota rilevante iva converter
	 */
	@SuppressWarnings("unchecked")
	public DocumentoEsisteQuotaRilevanteIvaConverter() {
		super((Class<Documento<?, ?>>)(Class<?>)Documento.class, SiacTDoc.class);
	}

	@Override
	public Documento<?, ?> convertFrom(SiacTDoc src, Documento<?, ?> dest) {
		// Totale rilevante IVA
		Long numSubdocRilevantiIva = siacTSubdocRepository.countSubdocRilevanteIvaByDocId(src.getUid());
		boolean esisteQuotaRilevanteIva = numSubdocRilevantiIva != null && numSubdocRilevantiIva.longValue() > 0L;
		dest.setEsisteQuotaRilevanteIva(Boolean.valueOf(esisteQuotaRilevanteIva));
		return dest;
	}
	
	@Override
	public SiacTDoc convertTo(Documento<?, ?> src, SiacTDoc dest) {
		return dest;
	}

}
