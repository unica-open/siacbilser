/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacDSiopeDocumentoTipo;
import it.csi.siac.siacbilser.integration.entity.SiacTDoc;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfinser.model.siopeplus.SiopeDocumentoTipo;

/**
 * The Class DocumentoSpesaSiopeDocumentoTipoConverter.
 */
@Component
public class DocumentoSpesaSiopeDocumentoTipoConverter extends ExtendedDozerConverter<DocumentoSpesa, SiacTDoc> {

	/**
	 * Instantiates a new documento spesa siope documento tipo converter.
	 */
	public DocumentoSpesaSiopeDocumentoTipoConverter() {
		super(DocumentoSpesa.class, SiacTDoc.class);
	}

	@Override
	public DocumentoSpesa convertFrom(SiacTDoc src, DocumentoSpesa dest) {
		if(src.getSiacDSiopeDocumentoTipo() == null || dest == null) {
			return dest;
		}
		
		SiopeDocumentoTipo siopeDocumentoTipo = map(src.getSiacDSiopeDocumentoTipo(), SiopeDocumentoTipo.class, BilMapId.SiacDSiopeDocumentoTipo_SiopeDocumentoTipo);
		dest.setSiopeDocumentoTipo(siopeDocumentoTipo);
		
		return dest;
	}

	@Override
	public SiacTDoc convertTo(DocumentoSpesa src, SiacTDoc dest) {
		if(src.getSiopeDocumentoTipo() == null || src.getSiopeDocumentoTipo().getUid() == 0 || dest == null){
			return dest;
		}
		SiacDSiopeDocumentoTipo siacDSiopeDocumentoTipo = new SiacDSiopeDocumentoTipo();
		siacDSiopeDocumentoTipo.setUid(src.getSiopeDocumentoTipo().getUid());
		dest.setSiacDSiopeDocumentoTipo(siacDSiopeDocumentoTipo);
		return dest;
	}

}
