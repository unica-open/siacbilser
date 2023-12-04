/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacTMovgestT;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.siopeplus.SiopeTipoDebito;

/**
 * The Class SubImpegnoSiopeTipoDebitoConverter.
 */
@Component
public class SubImpegnoSiopeTipoDebitoConverter extends ExtendedDozerConverter<SubImpegno, SiacTMovgestT> {

	/**
	 * Instantiates a new sub impegno - siope tipo debito converter.
	 */
	public SubImpegnoSiopeTipoDebitoConverter() {
		super(SubImpegno.class, SiacTMovgestT.class);
	}
	@Override
	public SubImpegno convertFrom(SiacTMovgestT src, SubImpegno dest) {
		if(src == null || src.getSiacDSiopeTipoDebito() == null || dest == null){
			return dest;
		}
		
		SiopeTipoDebito siopeTipoDebito = map(src.getSiacDSiopeTipoDebito(), SiopeTipoDebito.class, BilMapId.SiacDSiopeTipoDebito_SiopeTipoDebito);
		dest.setSiopeTipoDebito(siopeTipoDebito);
		
		return dest;
	}
	
	@Override
	public SiacTMovgestT convertTo(SubImpegno src, SiacTMovgestT dest) {
		return dest;
	}

}
