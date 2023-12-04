/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.apache.commons.lang.StringUtils;
import org.dozer.DozerConverter;

import it.csi.siac.siacbilser.integration.entity.SirfelTFattura;
import it.csi.siac.sirfelser.model.StatoAcquisizioneFEL;

/**
 * The Class DocumentoSpesaStatoConverter.
 */
public class FatturaFELStatoAcquisizioneConverter extends DozerConverter<StatoAcquisizioneFEL, SirfelTFattura > {
	
	/**
	 * Instantiates a new documento spesa stato converter.
	 */
	public FatturaFELStatoAcquisizioneConverter() {
		super(StatoAcquisizioneFEL.class, SirfelTFattura.class);
	}

	@Override
	public StatoAcquisizioneFEL convertFrom(SirfelTFattura src, StatoAcquisizioneFEL dest) {
		if(src.getStatoFattura() != null && StringUtils.isNotBlank(src.getStatoFattura())){
			dest = StatoAcquisizioneFEL.byCodice(src.getStatoFattura());
		}
		return dest;
	}

	@Override
	public SirfelTFattura convertTo(StatoAcquisizioneFEL src, SirfelTFattura dest) {
		if(src != null){
			dest.setStatoFattura(src.getCodice());
		}
		return dest;
	}



	

}
