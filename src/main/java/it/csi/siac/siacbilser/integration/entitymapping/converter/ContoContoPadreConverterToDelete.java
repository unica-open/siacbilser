/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacTPdceConto;
import it.csi.siac.siacbilser.integration.entitymapping.GenMapId;
import it.csi.siac.siacgenser.model.Conto;

/**
 * The Class ContoContoPadreConverter.
 */
@Component
public class ContoContoPadreConverterToDelete extends ExtendedDozerConverter<Conto, SiacTPdceConto> {
	

	/**
	 * Instantiates a new conto padre converter.
	 */
	public ContoContoPadreConverterToDelete() {
		super(Conto.class, SiacTPdceConto.class);
	}

	@Override
	public Conto convertFrom(SiacTPdceConto src, Conto dest) {
		
		
		if(src.getSiacTPdceConto()==null){
			return dest;
		}
		
		Conto contoPadre = new Conto();
		contoPadre.setDataInizioValiditaFiltro(dest.getDataInizioValiditaFiltro());
		map(src.getSiacTPdceConto(), contoPadre, GenMapId.SiacTPdceConto_Conto_Base); //occhio questo converter a sua volta non deve mappare il padre!!
		dest.setContoPadre(contoPadre);
		
		return dest;
	}
	

	@Override
	public SiacTPdceConto convertTo(Conto src, SiacTPdceConto dest) {
		
		if(src.getContoPadre()==null || src.getContoPadre().getUid()==0){
			return dest;
		}
		
		SiacTPdceConto siacTPdceConto = new SiacTPdceConto();
		siacTPdceConto.setUid(src.getContoPadre().getUid());
		dest.setSiacTPdceConto(siacTPdceConto);
		
		return dest;
	}



}
