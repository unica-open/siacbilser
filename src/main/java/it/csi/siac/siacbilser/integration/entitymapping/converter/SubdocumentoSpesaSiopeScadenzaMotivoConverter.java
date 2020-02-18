/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacDSiopeScadenzaMotivo;
import it.csi.siac.siacbilser.integration.entity.SiacTSubdoc;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfinser.model.siopeplus.SiopeScadenzaMotivo;

/**
 * The Class SubdocumentoSpesaSiopeScadenzaMotivoConverter.
 */
@Component
public class SubdocumentoSpesaSiopeScadenzaMotivoConverter extends ExtendedDozerConverter<SubdocumentoSpesa, SiacTSubdoc> {

	/**
	 * Instantiates a new subdocumento spesa siope scadenza motivo converter.
	 */
	public SubdocumentoSpesaSiopeScadenzaMotivoConverter() {
		super(SubdocumentoSpesa.class, SiacTSubdoc.class);
	}

	@Override
	public SubdocumentoSpesa convertFrom(SiacTSubdoc src, SubdocumentoSpesa dest) {
		if(src.getSiacDSiopeScadenzaMotivo() == null || dest == null) {
			return dest;
		}
		
		SiopeScadenzaMotivo siopeScadenzaMotivo = map(src.getSiacDSiopeScadenzaMotivo(), SiopeScadenzaMotivo.class, BilMapId.SiacDSiopeScadenzaMotivo_SiopeScadenzaMotivo);
		dest.setSiopeScadenzaMotivo(siopeScadenzaMotivo);
		
		return dest;
	}

	@Override
	public SiacTSubdoc convertTo(SubdocumentoSpesa src, SiacTSubdoc dest) {
		if(src.getSiopeScadenzaMotivo() == null || src.getSiopeScadenzaMotivo().getUid() == 0 || dest == null){
			return dest;
		}
		SiacDSiopeScadenzaMotivo siacDSiopeScadenzaMotivo = new SiacDSiopeScadenzaMotivo();
		siacDSiopeScadenzaMotivo.setUid(src.getSiopeScadenzaMotivo().getUid());
		dest.setSiacDSiopeScadenzaMotivo(siacDSiopeScadenzaMotivo);
		return dest;
	}

}
