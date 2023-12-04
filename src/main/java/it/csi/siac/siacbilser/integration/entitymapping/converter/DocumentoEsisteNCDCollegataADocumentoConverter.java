/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.SiacTDocRepository;
import it.csi.siac.siacbilser.integration.entity.SiacTDoc;
import it.csi.siac.siacfin2ser.model.Documento;

/**
 * The Class DocumentoEsisteQuotaRilevanteIvaConverter.
 */
@Component
public class DocumentoEsisteNCDCollegataADocumentoConverter extends ExtendedDozerConverter<Documento<?, ?>, SiacTDoc> {
	
	@Autowired
	private SiacTDocRepository siacTDocRepository;
	
	/**
	 * Instantiates a new documento esiste quota rilevante iva converter
	 */
	@SuppressWarnings("unchecked")
	public DocumentoEsisteNCDCollegataADocumentoConverter() {
		super((Class<Documento<?, ?>>)(Class<?>)Documento.class, SiacTDoc.class);
	}

	@Override
	public Documento<?, ?> convertFrom(SiacTDoc src, Documento<?, ?> dest) {
		Long countNoteCreditoCollegate = siacTDocRepository.countNoteCreditoCollegateByDocId(src.getDocId());
		dest.setEsisteNCDCollegataADocumento(Boolean.valueOf(Long.valueOf(0).compareTo(countNoteCreditoCollegate)<0));
		return dest;
	}
	
	@Override
	public SiacTDoc convertTo(Documento<?, ?> src, SiacTDoc dest) {
		return dest;
	}

}
