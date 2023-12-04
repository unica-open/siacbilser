/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter.cespiti;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.integration.entity.SiacTCespitiAmmortamento;
import it.csi.siac.siacbilser.integration.entity.SiacTCespitiAmmortamentoDett;
import it.csi.siac.siacbilser.integration.entitymapping.CespMapId;
import it.csi.siac.siacbilser.integration.entitymapping.converter.ExtendedDozerConverter;
import it.csi.siac.siacbilser.integration.entitymapping.converter.base.Converters;
import it.csi.siac.siaccespser.model.AmmortamentoAnnuoCespite;
import it.csi.siac.siaccespser.model.AmmortamentoAnnuoCespiteModelDetail;
import it.csi.siac.siaccespser.model.DettaglioAmmortamentoAnnuoCespite;

/**
 * The Class AmmortamentoAnnuoCespiteStatoAmmortamentoAnnuoCespiteConverter.
 * @author elisa
 * @version 1.0.0 - 30-08-2018
 */
@Component
public class DettaglioAmmortamentoAnnuoCespiteAmmortamentoAnnuoCespiteModelDetailConverter extends ExtendedDozerConverter<DettaglioAmmortamentoAnnuoCespite, SiacTCespitiAmmortamentoDett> {
	
	/**
	 *  Costruttore.
	 */
	public DettaglioAmmortamentoAnnuoCespiteAmmortamentoAnnuoCespiteModelDetailConverter() {
		super(DettaglioAmmortamentoAnnuoCespite.class, SiacTCespitiAmmortamentoDett.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public DettaglioAmmortamentoAnnuoCespite convertFrom(SiacTCespitiAmmortamentoDett src, DettaglioAmmortamentoAnnuoCespite dest) {
		AmmortamentoAnnuoCespite ammortamento = mapNotNull(src.getSiacTCespitiAmmortamento(), AmmortamentoAnnuoCespite.class, CespMapId.SiacTCespitiAmmortamento_AmmortamentoAnnuoCespite_ModelDetail, Converters.byModelDetails(Utility.MDTL.byModelDetailClass(AmmortamentoAnnuoCespiteModelDetail.class)));
		dest.setAmmortamentoAnnuoCespite(ammortamento);
		return dest;
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacTCespitiAmmortamentoDett convertTo(DettaglioAmmortamentoAnnuoCespite src, SiacTCespitiAmmortamentoDett dest) {
		
		if(src.getAmmortamentoAnnuoCespite() == null || src.getAmmortamentoAnnuoCespite().getUid() == 0) {
			return dest;
		}
		
		
		SiacTCespitiAmmortamento siacTCespitiAmmortamento = new SiacTCespitiAmmortamento();
		siacTCespitiAmmortamento.setUid(src.getAmmortamentoAnnuoCespite().getUid());
		dest.setSiacTCespitiAmmortamento(siacTCespitiAmmortamento);
		return dest;
	}


}
