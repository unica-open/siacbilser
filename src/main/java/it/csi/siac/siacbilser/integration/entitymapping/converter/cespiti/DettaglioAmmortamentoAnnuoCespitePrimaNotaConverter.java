/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter.cespiti;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacTCespitiAmmortamentoDett;
import it.csi.siac.siacbilser.integration.entity.SiacTPrimaNota;
import it.csi.siac.siacbilser.integration.entitymapping.GenMapId;
import it.csi.siac.siacbilser.integration.entitymapping.converter.ExtendedDozerConverter;
import it.csi.siac.siaccespser.model.DettaglioAmmortamentoAnnuoCespite;
import it.csi.siac.siacgenser.model.PrimaNota;

/**
 * The Class AmmortamentoAnnuoCespiteStatoAmmortamentoAnnuoCespiteConverter.
 * @author elisa
 * @version 1.0.0 - 30-08-2018
 */
@Component
public class DettaglioAmmortamentoAnnuoCespitePrimaNotaConverter extends ExtendedDozerConverter<DettaglioAmmortamentoAnnuoCespite, SiacTCespitiAmmortamentoDett> {
	
	/**
	 *  Costruttore.
	 */
	public DettaglioAmmortamentoAnnuoCespitePrimaNotaConverter() {
		super(DettaglioAmmortamentoAnnuoCespite.class, SiacTCespitiAmmortamentoDett.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public DettaglioAmmortamentoAnnuoCespite convertFrom(SiacTCespitiAmmortamentoDett src, DettaglioAmmortamentoAnnuoCespite dest) {
		PrimaNota primaNota = mapNotNull(src.getSiacTPrimaNota(), PrimaNota.class, GenMapId.SiacTPrimaNota_PrimaNota_Minimal);
		dest.setPrimaNota(primaNota);
		return dest;
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacTCespitiAmmortamentoDett convertTo(DettaglioAmmortamentoAnnuoCespite src, SiacTCespitiAmmortamentoDett dest) {
		if(src.getPrimaNota() == null || src.getPrimaNota().getUid() == 0) {
			return dest;
		}
		SiacTPrimaNota siacTPrimaNota = new SiacTPrimaNota();
		siacTPrimaNota.setUid(src.getPrimaNota().getUid());
		dest.setSiacTPrimaNota(siacTPrimaNota);
		return dest;
	}


}
