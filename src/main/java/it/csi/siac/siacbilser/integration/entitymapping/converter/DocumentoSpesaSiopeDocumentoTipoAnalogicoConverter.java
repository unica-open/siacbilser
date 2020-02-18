/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacDSiopeDocumentoTipoAnalogico;
import it.csi.siac.siacbilser.integration.entity.SiacTDoc;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfinser.model.siopeplus.SiopeDocumentoTipoAnalogico;

/**
 * The Class DocumentoSpesaSiopeDocumentoTipoAnalogicoConverter.
 */
@Component
public class DocumentoSpesaSiopeDocumentoTipoAnalogicoConverter extends ExtendedDozerConverter<DocumentoSpesa, SiacTDoc> {

	/**
	 * Instantiates a new documento spesa siope documento tipo analogico converter.
	 */
	public DocumentoSpesaSiopeDocumentoTipoAnalogicoConverter() {
		super(DocumentoSpesa.class, SiacTDoc.class);
	}

	@Override
	public DocumentoSpesa convertFrom(SiacTDoc src, DocumentoSpesa dest) {
		if(src.getSiacDSiopeDocumentoTipoAnalogico() == null || dest == null) {
			return dest;
		}
		
		SiopeDocumentoTipoAnalogico siopeDocumentoTipoAnalogico = map(src.getSiacDSiopeDocumentoTipoAnalogico(), SiopeDocumentoTipoAnalogico.class, BilMapId.SiacDSiopeDocumentoTipoAnalogico_SiopeDocumentoTipoAnalogico);
		dest.setSiopeDocumentoTipoAnalogico(siopeDocumentoTipoAnalogico);
		
		return dest;
	}

	@Override
	public SiacTDoc convertTo(DocumentoSpesa src, SiacTDoc dest) {
		if(src.getSiopeDocumentoTipoAnalogico() == null || src.getSiopeDocumentoTipoAnalogico().getUid() == 0 || dest == null){
			return dest;
		}
		SiacDSiopeDocumentoTipoAnalogico siacDSiopeDocumentoTipoAnalogico = new SiacDSiopeDocumentoTipoAnalogico();
		siacDSiopeDocumentoTipoAnalogico.setUid(src.getSiopeDocumentoTipoAnalogico().getUid());
		dest.setSiacDSiopeDocumentoTipoAnalogico(siacDSiopeDocumentoTipoAnalogico);
		return dest;
	}

}
