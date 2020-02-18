/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacTRegistroPcc;
import it.csi.siac.siacbilser.integration.entity.SiacTSubdoc;
import it.csi.siac.siacfin2ser.model.RegistroComunicazioniPCC;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;

/**
 * The Class RegistroComunicazioniPCCSubdocumentoSpesaConverter.
 */
@Component
public class RegistroComunicazioniPCCSubdocumentoSpesaConverter extends ExtendedDozerConverter<RegistroComunicazioniPCC, SiacTRegistroPcc> {

	public RegistroComunicazioniPCCSubdocumentoSpesaConverter() {
		super(RegistroComunicazioniPCC.class, SiacTRegistroPcc.class);
	}

	@Override
	public RegistroComunicazioniPCC convertFrom(SiacTRegistroPcc src, RegistroComunicazioniPCC dest) {
		
		if(src.getSiacTSubdoc() != null && src.getSiacTSubdoc().getDataCancellazione() == null) {
			SubdocumentoSpesa subdocumentoSpesa = new SubdocumentoSpesa();
			subdocumentoSpesa.setUid(src.getSiacTSubdoc().getUid());
			dest.setSubdocumentoSpesa(subdocumentoSpesa);
		}
		
		return dest;
	}

	@Override
	public SiacTRegistroPcc convertTo(RegistroComunicazioniPCC src, SiacTRegistroPcc dest) {
		if(src.getSubdocumentoSpesa() != null && src.getSubdocumentoSpesa().getUid() != 0) {
			SiacTSubdoc siacTSubdoc = new SiacTSubdoc();
			siacTSubdoc.setUid(src.getSubdocumentoSpesa().getUid());
			dest.setSiacTSubdoc(siacTSubdoc);
		}
		return dest;
	}

}
