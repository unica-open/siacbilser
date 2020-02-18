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
import it.csi.siac.siacfinser.model.siopeplus.SiopeTipoDebito;

/**
 * The Class ImpegnoSiopeTipoDebitoConverter.
 */
@Component
public class ImpegnoSiopeTipoDebitoConverter extends ExtendedDozerConverter<Impegno, SiacTMovgest> {

	/**
	 * Instantiates a new impegno - siope tipo debito converter.
	 */
	public ImpegnoSiopeTipoDebitoConverter() {
		super(Impegno.class, SiacTMovgest.class);
	}
	@Override
	public Impegno convertFrom(SiacTMovgest src, Impegno dest) {
		SiacTMovgestT siacTMovgestT = getSiacTMovgestT(src);
		if(siacTMovgestT == null || siacTMovgestT.getSiacDSiopeTipoDebito() == null || dest == null){
			return dest;
		}
		
		SiopeTipoDebito siopeTipoDebito = map(siacTMovgestT.getSiacDSiopeTipoDebito(), SiopeTipoDebito.class, BilMapId.SiacDSiopeTipoDebito_SiopeTipoDebito);
		dest.setSiopeTipoDebito(siopeTipoDebito);
		
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
