/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.SiacTDocRepository;
import it.csi.siac.siacbilser.integration.entity.SiacTDoc;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDDocFamTipoEnum;
import it.csi.siac.siacfin2ser.model.Documento;

/**
 * The Class DocumentoTotaleImportoDocumentiSpesaFiglioConverter.
 */
@Component
public class DocumentoTotaliImportiNoteCreditoConverter extends ExtendedDozerConverter<Documento<?, ?>, SiacTDoc> {
	
	@Autowired
	private SiacTDocRepository siacTDocRepository;
	
	/**
	 * Instantiates a new documento totali importi quote converter
	 */
	@SuppressWarnings("unchecked")
	public DocumentoTotaliImportiNoteCreditoConverter() {
		super((Class<Documento<?, ?>>)(Class<?>)Documento.class, SiacTDoc.class);
	}

	@Override
	public Documento<?, ?> convertFrom(SiacTDoc src, Documento<?, ?> dest) {
		Collection<String> docFamTipoCodes = Arrays.asList(SiacDDocFamTipoEnum.Spesa.getCodice(), SiacDDocFamTipoEnum.IvaSpesa.getCodice());
		
		Object[] totali = siacTDocRepository.sumDocImportoDocImportoDaDedurreNoteCreditoByDocId(src.getUid(), docFamTipoCodes);
		if(totali != null) {
			dest.setImportoTotaleNoteCollegate((BigDecimal) totali[0]);
			dest.setImportoTotaleDaDedurreSuFatturaNoteCollegate((BigDecimal) totali[1]);
		}
		
		return dest;
	}
	
	@Override
	public SiacTDoc convertTo(Documento<?, ?> src, SiacTDoc dest) {
		return dest;
	}

}
