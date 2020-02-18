/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter.cespiti;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.EnumEntityFactory;
import it.csi.siac.siacbilser.integration.entity.SiacTCespiti;
import it.csi.siac.siacbilser.integration.entitymapping.converter.ExtendedDozerConverter;
import it.csi.siac.siaccespser.model.Cespite;

/**
 * The Class CespitiTipoBeneCespiteConverter.
 *
 * @author Anto
 */
@Component
public class CespiteFondoAmmortamentoConverter extends ExtendedDozerConverter<Cespite, SiacTCespiti > {
	
	@Autowired private EnumEntityFactory eef;
	

	public CespiteFondoAmmortamentoConverter() {
		super(Cespite.class, SiacTCespiti.class);
	}

	@Override
	public Cespite convertFrom(SiacTCespiti src, Cespite dest) {
		//Per calcolare il fondo ammortamento devo fare questo giro: siac_t_cespiti -> siac-t_cespiti_ammortamento -> siac_t_ammortamento_det e li' sommare tutti gli importi che trovo per quel cespite
		//valutare come fare
		//per ora, forzo il campo a zero solo per rendere palese il fatto di essere passata di qui
		dest.setFondoAmmortamento(BigDecimal.ZERO);
		return dest;
		
	}

	@Override
	public SiacTCespiti convertTo(Cespite src, SiacTCespiti dest) {
		return dest;
	}
}
