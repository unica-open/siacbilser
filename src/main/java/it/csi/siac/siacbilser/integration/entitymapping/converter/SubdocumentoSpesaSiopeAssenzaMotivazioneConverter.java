/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacDSiopeAssenzaMotivazione;
import it.csi.siac.siacbilser.integration.entity.SiacTSubdoc;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfinser.model.siopeplus.SiopeAssenzaMotivazione;

/**
 * The Class SubdocumentoSpesaSiopeAssenzaMotivazioneConverter.
 */
@Component
public class SubdocumentoSpesaSiopeAssenzaMotivazioneConverter extends ExtendedDozerConverter<SubdocumentoSpesa, SiacTSubdoc> {

	/**
	 * Instantiates a new subdocumento spesa siope assenza motivazione converter.
	 */
	public SubdocumentoSpesaSiopeAssenzaMotivazioneConverter() {
		super(SubdocumentoSpesa.class, SiacTSubdoc.class);
	}

	@Override
	public SubdocumentoSpesa convertFrom(SiacTSubdoc src, SubdocumentoSpesa dest) {
		if(src.getSiacDSiopeAssenzaMotivazione() == null || dest == null) {
			return dest;
		}
		
		SiopeAssenzaMotivazione siopeAssenzaMotivazione = map(src.getSiacDSiopeAssenzaMotivazione(), SiopeAssenzaMotivazione.class, BilMapId.SiacDSiopeAssenzaMotivazione_SiopeAssenzaMotivazione);
		dest.setSiopeAssenzaMotivazione(siopeAssenzaMotivazione);
		
		return dest;
	}

	@Override
	public SiacTSubdoc convertTo(SubdocumentoSpesa src, SiacTSubdoc dest) {
		if(src.getSiopeAssenzaMotivazione() == null || src.getSiopeAssenzaMotivazione().getUid() == 0 || dest == null){
			return dest;
		}
		SiacDSiopeAssenzaMotivazione siacDSiopeAssenzaMotivazione = new SiacDSiopeAssenzaMotivazione();
		siacDSiopeAssenzaMotivazione.setUid(src.getSiopeAssenzaMotivazione().getUid());
		dest.setSiacDSiopeAssenzaMotivazione(siacDSiopeAssenzaMotivazione);
		return dest;
	}

}
