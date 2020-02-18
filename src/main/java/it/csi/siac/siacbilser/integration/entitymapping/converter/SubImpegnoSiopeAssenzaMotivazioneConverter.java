/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacTMovgestT;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.siopeplus.SiopeAssenzaMotivazione;

/**
 * The Class SubImpegnoSiopeAssenzaMotivazioneConverter.
 */
@Component
public class SubImpegnoSiopeAssenzaMotivazioneConverter extends ExtendedDozerConverter<SubImpegno, SiacTMovgestT> {

	/**
	 * Instantiates a new sub impegno - siope tipo debito converter.
	 */
	public SubImpegnoSiopeAssenzaMotivazioneConverter() {
		super(SubImpegno.class, SiacTMovgestT.class);
	}
	@Override
	public SubImpegno convertFrom(SiacTMovgestT src, SubImpegno dest) {
		if(src == null || src.getSiacDSiopeAssenzaMotivazione() == null || dest == null){
			return dest;
		}
		
		SiopeAssenzaMotivazione siopeAssenzaMotivazione = map(src.getSiacDSiopeAssenzaMotivazione(), SiopeAssenzaMotivazione.class, BilMapId.SiacDSiopeAssenzaMotivazione_SiopeAssenzaMotivazione);
		dest.setSiopeAssenzaMotivazione(siopeAssenzaMotivazione);
		
		return dest;
	}
	
	@Override
	public SiacTMovgestT convertTo(SubImpegno src, SiacTMovgestT dest) {
		return dest;
	}

}
