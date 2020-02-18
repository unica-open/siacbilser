/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.EnumEntityFactory;
import it.csi.siac.siacbilser.integration.entity.SiacDQuadroEconomicoParte;
import it.csi.siac.siacbilser.integration.entity.SiacTQuadroEconomico;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDQuadroEconomicoParteEnum;
import it.csi.siac.siacbilser.model.ParteQuadroEconomico;
import it.csi.siac.siacbilser.model.QuadroEconomico;

/**
 * The Class CausaleEPContoConverter.
 *
 * @author Domenico
 */
@Component
public class QuadroEconomicoParteConverter extends ExtendedDozerConverter<QuadroEconomico, SiacTQuadroEconomico > {
	
	/** The eef. */
	@Autowired private EnumEntityFactory eef;

	/**
	 * Instantiates a new quadro economico stato converter.
	 */
	public QuadroEconomicoParteConverter() {
		super(QuadroEconomico.class, SiacTQuadroEconomico.class);
	}

	@Override
	public QuadroEconomico convertFrom(SiacTQuadroEconomico src, QuadroEconomico dest) {
		if(src==null){
			return dest;
		}
		ParteQuadroEconomico parteQuadroEconomico = SiacDQuadroEconomicoParteEnum.byCodice(src.getSiacDQuadroEconomicoParte().getParteCode()).getParteQuadroEconomico();				
		dest.setParteQuadroEconomico(parteQuadroEconomico);
		return dest;
	}
	
	@Override
	public SiacTQuadroEconomico convertTo(QuadroEconomico src, SiacTQuadroEconomico dest) {
		if(dest== null) {
			return dest;
		}

		SiacDQuadroEconomicoParteEnum siacDQuadroEconomicoParteEnum = SiacDQuadroEconomicoParteEnum.byParte(src.getParteQuadroEconomico());
		
		SiacDQuadroEconomicoParte siacDQuadroEconomicoParte = eef.getEntity(siacDQuadroEconomicoParteEnum, dest.getSiacTEnteProprietario().getUid(), SiacDQuadroEconomicoParte.class); 
				
		dest.setSiacDQuadroEconomicoParte(siacDQuadroEconomicoParte);
				
		return dest;
	}

}
