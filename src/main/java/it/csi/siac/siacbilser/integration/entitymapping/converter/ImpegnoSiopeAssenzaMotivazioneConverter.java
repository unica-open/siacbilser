/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacTMovgest;
import it.csi.siac.siacbilser.integration.entity.SiacTMovgestT;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.siopeplus.SiopeAssenzaMotivazione;

/**
 * The Class ImpegnoSiopeAssenzaMotivazioneConverter.
 */
@Component
public class ImpegnoSiopeAssenzaMotivazioneConverter extends ExtendedDozerConverter<Impegno, SiacTMovgest> {

	/**
	 * Instantiates a new impegno - siope assenza motivazione converter.
	 */
	public ImpegnoSiopeAssenzaMotivazioneConverter() {
		super(Impegno.class, SiacTMovgest.class);
	}
	@Override
	public Impegno convertFrom(SiacTMovgest src, Impegno dest) {
		SiacTMovgestT siacTMovgestT = getSiacTMovgestT(src);
		if(siacTMovgestT == null || siacTMovgestT.getSiacDSiopeAssenzaMotivazione() == null || dest == null){
			return dest;
		}
		
		SiopeAssenzaMotivazione siopeAssenzaMotivazione = map(siacTMovgestT.getSiacDSiopeAssenzaMotivazione(), SiopeAssenzaMotivazione.class, BilMapId.SiacDSiopeAssenzaMotivazione_SiopeAssenzaMotivazione);
		dest.setSiopeAssenzaMotivazione(siopeAssenzaMotivazione);
		
		return dest;
	}

	/**
	 * Trova il ts corrispondente alla testata
	 * @param src la testata
	 * @return il ts
	 */
	private SiacTMovgestT getSiacTMovgestT(SiacTMovgest src) {
		if(src == null || src.getSiacTMovgestTs() == null) {
			return null;
		}
		for(SiacTMovgestT siacTMovgestT : src.getSiacTMovgestTs()){
			if(siacTMovgestT.getDataCancellazione() == null && "T".equals(siacTMovgestT.getSiacDMovgestTsTipo().getMovgestTsTipoCode())) {
				return siacTMovgestT;
			}
		}
		return null;
	}
	
	@Override
	public SiacTMovgest convertTo(Impegno src, SiacTMovgest dest) {
		return dest;
	}

}
