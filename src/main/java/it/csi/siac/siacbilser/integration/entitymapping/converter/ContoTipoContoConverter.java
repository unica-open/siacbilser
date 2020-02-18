/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacTPdceConto;
import it.csi.siac.siacbilser.integration.entitymapping.GenMapId;
import it.csi.siac.siacgenser.model.Conto;
import it.csi.siac.siacgenser.model.TipoConto;

/**
 * The Class ContoContoCollegatoConverter.
 */
@Component
public class ContoTipoContoConverter extends ExtendedDozerConverter<Conto, SiacTPdceConto> {
	

	/**
	 * Instantiates a new conto conto collegato converter.
	 */
	public ContoTipoContoConverter() {
		super(Conto.class, SiacTPdceConto.class);
	}

	@Override
	public Conto convertFrom(SiacTPdceConto src, Conto dest) {
		
		TipoConto tipoConto = mapNotNull(src.getSiacDPdceContoTipo(), TipoConto.class, GenMapId.SiacDPdceContoTipo_TipoConto);
		
		dest.setTipoConto(tipoConto);
		
		return dest;
	}
	

	@Override
	public SiacTPdceConto convertTo(Conto src, SiacTPdceConto dest) {
		
		return dest;
	}



}
