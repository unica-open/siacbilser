/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter.cespiti;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacTCespiti;
import it.csi.siac.siacbilser.integration.entity.SiacTCespitiAmmortamento;
import it.csi.siac.siacbilser.integration.entitymapping.CespMapId;
import it.csi.siac.siacbilser.integration.entitymapping.converter.ExtendedDozerConverter;
import it.csi.siac.siaccespser.model.AmmortamentoAnnuoCespite;
import it.csi.siac.siaccespser.model.Cespite;

/**
 * The Class AmmortamentoAnnuoCespiteStatoAmmortamentoAnnuoCespiteConverter.
 * @author elisa
 * @version 1.0.0 - 30-08-2018
 */
@Component
public class AmmortamentoAnnuoCespiteCespiteConverter extends ExtendedDozerConverter<AmmortamentoAnnuoCespite, SiacTCespitiAmmortamento> {
	
	/**
	 *  Costruttore.
	 */
	public AmmortamentoAnnuoCespiteCespiteConverter() {
		super(AmmortamentoAnnuoCespite.class, SiacTCespitiAmmortamento.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public AmmortamentoAnnuoCespite convertFrom(SiacTCespitiAmmortamento src, AmmortamentoAnnuoCespite dest) {
		Cespite cespite = mapNotNull(src.getSiacTCespiti(), Cespite.class, CespMapId.SiacTCespiti_Cespite_ModelDetail);
		dest.setCespite(cespite);
		return dest;
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacTCespitiAmmortamento convertTo(AmmortamentoAnnuoCespite src, SiacTCespitiAmmortamento dest) {
		if(src.getCespite() == null || src.getCespite().getUid() == 0) {
			return dest;
		}
		SiacTCespiti siacTCespiti = new SiacTCespiti();
		siacTCespiti.setUid(src.getCespite().getUid());
		dest.setSiacTCespiti(siacTCespiti);
		return dest;
	}


}
