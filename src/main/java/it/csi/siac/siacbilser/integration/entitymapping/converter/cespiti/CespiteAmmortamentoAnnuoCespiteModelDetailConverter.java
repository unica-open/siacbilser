/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter.cespiti;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.integration.entity.SiacTCespiti;
import it.csi.siac.siacbilser.integration.entity.SiacTCespitiAmmortamento;
import it.csi.siac.siacbilser.integration.entitymapping.CespMapId;
import it.csi.siac.siacbilser.integration.entitymapping.converter.ExtendedDozerConverter;
import it.csi.siac.siacbilser.integration.entitymapping.converter.base.Converters;
import it.csi.siac.siaccespser.model.AmmortamentoAnnuoCespite;
import it.csi.siac.siaccespser.model.AmmortamentoAnnuoCespiteModelDetail;
import it.csi.siac.siaccespser.model.Cespite;

/**
 * The Class CespitiTipoBeneCespiteConverter.
 *
 * @author Anto
 */
@Component
public class CespiteAmmortamentoAnnuoCespiteModelDetailConverter extends ExtendedDozerConverter<Cespite, SiacTCespiti > {
	
	public CespiteAmmortamentoAnnuoCespiteModelDetailConverter() {
		super(Cespite.class, SiacTCespiti.class);
	}

	@Override
	public Cespite convertFrom(SiacTCespiti src, Cespite dest) {
		String methodName = "convertFrom";
		
		if(src.getSiacTCespitiAmmortamentos()== null){
			return dest;
		}
		
		for (SiacTCespitiAmmortamento siacTCespitiAmmortamento : src.getSiacTCespitiAmmortamentos()) {
			if(siacTCespitiAmmortamento == null || siacTCespitiAmmortamento.getDataCancellazione() != null) {
				continue;
			}
			AmmortamentoAnnuoCespite ammortamentoAnnuoCespite =  mapNotNull(siacTCespitiAmmortamento, AmmortamentoAnnuoCespite.class, CespMapId.SiacTCespitiAmmortamento_AmmortamentoAnnuoCespite_ModelDetail, Converters.byModelDetails(Utility.MDTL.byModelDetailClass(AmmortamentoAnnuoCespiteModelDetail.class)));
			dest.setAmmortamentoAnnuoCespite(ammortamentoAnnuoCespite);
			return dest;
		}
		return dest;
		
	}

	@Override
	public SiacTCespiti convertTo(Cespite src, SiacTCespiti dest) {
		return dest;
	}


}
