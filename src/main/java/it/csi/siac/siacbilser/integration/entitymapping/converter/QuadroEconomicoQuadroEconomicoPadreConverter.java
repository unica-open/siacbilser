/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacTQuadroEconomico;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacbilser.model.QuadroEconomico;

/**
 * The Class CausaleEPContoConverter.
 *
 * @author Domenico
 */
@Component
public class QuadroEconomicoQuadroEconomicoPadreConverter extends ExtendedDozerConverter<QuadroEconomico, SiacTQuadroEconomico > {
	
	public QuadroEconomicoQuadroEconomicoPadreConverter() {
		super(QuadroEconomico.class, SiacTQuadroEconomico.class);
	}

	@Override
	public QuadroEconomico convertFrom(SiacTQuadroEconomico src, QuadroEconomico dest) {
		if(src.getSiacTQuadroEconomicoPadre() == null) {
			return dest;
		}
		QuadroEconomico classificatoreGSAPadre = map(src.getSiacTQuadroEconomicoPadre(), QuadroEconomico.class, BilMapId.SiacTQuadroEconomico_QuadroEconomico_ModelDetail);
		dest.setQuadroEconomicoPadre(classificatoreGSAPadre);
		return dest;
	}


	@Override
	public SiacTQuadroEconomico convertTo(QuadroEconomico src, SiacTQuadroEconomico dest) {
		if(src == null || src.getQuadroEconomicoPadre() == null || src.getQuadroEconomicoPadre().getUid() == 0) {
			return dest;
		}
		
		SiacTQuadroEconomico siacTQuadroEconomico = new SiacTQuadroEconomico();
		siacTQuadroEconomico.setUid(src.getQuadroEconomicoPadre().getUid());
		dest.setSiacTQuadroEconomicoPadre(siacTQuadroEconomico);
		
		return dest;
	}


}
