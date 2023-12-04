/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacTDoc;
import it.csi.siac.siacbilser.integration.entity.SiacTRegistroPcc;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.RegistroComunicazioniPCC;

/**
 * The Class RegistroComunicazioniPCCDocumentoSpesaConverter.
 */
@Component
public class RegistroComunicazioniPCCDocumentoSpesaConverter extends ExtendedDozerConverter<RegistroComunicazioniPCC, SiacTRegistroPcc> {

	public RegistroComunicazioniPCCDocumentoSpesaConverter() {
		super(RegistroComunicazioniPCC.class, SiacTRegistroPcc.class);
	}

	@Override
	public RegistroComunicazioniPCC convertFrom(SiacTRegistroPcc src, RegistroComunicazioniPCC dest) {
		
		if(src.getSiacTDoc() != null && src.getSiacTDoc().getDataCancellazione() == null) {
			DocumentoSpesa documentoSpesa = new DocumentoSpesa();
			documentoSpesa.setUid(src.getSiacTDoc().getUid());
			dest.setDocumentoSpesa(documentoSpesa);
		}
		
		return dest;
	}

	@Override
	public SiacTRegistroPcc convertTo(RegistroComunicazioniPCC src, SiacTRegistroPcc dest) {
		if(src.getDocumentoSpesa() != null && src.getDocumentoSpesa().getUid() != 0) {
			SiacTDoc siacTDoc = new SiacTDoc();
			siacTDoc.setUid(src.getDocumentoSpesa().getUid());
			dest.setSiacTDoc(siacTDoc);
		}
		return dest;
	}

}
