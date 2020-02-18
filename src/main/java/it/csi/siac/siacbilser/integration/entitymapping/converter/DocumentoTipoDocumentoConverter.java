/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.SiacDDocTipoRepository;
import it.csi.siac.siacbilser.integration.entity.SiacDDocTipo;
import it.csi.siac.siacbilser.integration.entity.SiacTDoc;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacfin2ser.model.Documento;
import it.csi.siac.siacfin2ser.model.TipoDocumento;


// TODO: Auto-generated Javadoc
/**
 * The Class DocumentoSpesaAttrConverter.
 */
@Component
public class DocumentoTipoDocumentoConverter extends ExtendedDozerConverter<Documento<?, ?>, SiacTDoc> {
	
	@Autowired
	private SiacDDocTipoRepository siacDDocTipoRepository;
	
	/**
	 * Instantiates a new documento spesa attr converter.
	 */
	@SuppressWarnings("unchecked")
	public DocumentoTipoDocumentoConverter() {
		super((Class<Documento<?, ?>>)(Class<?>)Documento.class, SiacTDoc.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public Documento<?, ?> convertFrom(SiacTDoc src, Documento<?, ?> dest) {
		if(src.getSiacDDocTipo() != null && src.getSiacDDocTipo().getDocTipoId() != null) {
			SiacDDocTipo siacDDocTipo = siacDDocTipoRepository.findOne(src.getSiacDDocTipo().getDocTipoId());
			TipoDocumento tipoDocumento = map(siacDDocTipo, TipoDocumento.class, BilMapId.SiacDDocTipo_TipoDocumento);
			dest.setTipoDocumento(tipoDocumento);
		}
		return dest;
	}

	@Override
	public SiacTDoc convertTo(Documento<?, ?> src, SiacTDoc dest) {
		if(src.getTipoDocumento() != null) {
			SiacDDocTipo siacDDocTipo = siacDDocTipoRepository.findOne(src.getTipoDocumento().getUid());
			dest.setSiacDDocTipo(siacDDocTipo);
		}
		return dest;
	}
	
}
