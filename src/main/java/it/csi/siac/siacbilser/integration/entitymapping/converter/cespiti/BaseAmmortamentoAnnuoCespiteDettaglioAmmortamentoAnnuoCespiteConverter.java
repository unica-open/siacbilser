/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter.cespiti;

import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.integration.entity.SiacTCespitiAmmortamento;
import it.csi.siac.siacbilser.integration.entity.SiacTCespitiAmmortamentoDett;
import it.csi.siac.siacbilser.integration.entitymapping.CespMapId;
import it.csi.siac.siacbilser.integration.entitymapping.converter.ExtendedDozerConverter;
import it.csi.siac.siaccespser.model.AmmortamentoAnnuoCespite;
import it.csi.siac.siaccespser.model.AnteprimaAmmortamentoAnnuoCespite;
import it.csi.siac.siaccespser.model.DettaglioAmmortamentoAnnuoCespite;

/**
 * The Class AmmortamentoAnnuoCespiteStatoAmmortamentoAnnuoCespiteConverter.
 * @author elisa
 * @version 1.0.0 - 30-08-2018
 */
public abstract class BaseAmmortamentoAnnuoCespiteDettaglioAmmortamentoAnnuoCespiteConverter extends ExtendedDozerConverter<AmmortamentoAnnuoCespite, SiacTCespitiAmmortamento> {
	
	/**
	 *  Costruttore.
	 */
	public BaseAmmortamentoAnnuoCespiteDettaglioAmmortamentoAnnuoCespiteConverter() {
		super(AmmortamentoAnnuoCespite.class, SiacTCespitiAmmortamento.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public AmmortamentoAnnuoCespite convertFrom(SiacTCespitiAmmortamento src, AmmortamentoAnnuoCespite dest) {
		if(src.getSiacTCespitiAmmortamentoDetts() == null || src.getSiacTCespitiAmmortamentoDetts().isEmpty()) {
			return dest;
		}
		List<DettaglioAmmortamentoAnnuoCespite> dettagliAmmortamentoAnnuoCespite  = new ArrayList<DettaglioAmmortamentoAnnuoCespite>();
		AnteprimaAmmortamentoAnnuoCespite anteprimaAmmortamentoAnnuoCespite = Utility.AAACTL.get();
		for (SiacTCespitiAmmortamentoDett siacTCespitiAmmortamentoDett : src.getSiacTCespitiAmmortamentoDetts()) {
			if(aggiungiSiacTCespitiAmmortamentoDett(siacTCespitiAmmortamentoDett, anteprimaAmmortamentoAnnuoCespite)) {
				dettagliAmmortamentoAnnuoCespite.add(mapNotNull(siacTCespitiAmmortamentoDett, DettaglioAmmortamentoAnnuoCespite.class, CespMapId.SiacTCespitiAmmortamentoDett_DettaglioAmmortamentoAnnuoCespite_ModelDetail));
			}
		}
		dest.setDettagliAmmortamentoAnnuoCespite(dettagliAmmortamentoAnnuoCespite);
		return dest;
	}

	/**
	 * @param siacTCespitiAmmortamentoDett
	 * @param dest 
	 * @return
	 */
	protected abstract boolean aggiungiSiacTCespitiAmmortamentoDett(SiacTCespitiAmmortamentoDett siacTCespitiAmmortamentoDett, AnteprimaAmmortamentoAnnuoCespite anteprimaAmmortamentoAnnuoCespite);
	
	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacTCespitiAmmortamento convertTo(AmmortamentoAnnuoCespite src, SiacTCespitiAmmortamento dest) {
		if(src.getDettagliAmmortamentoAnnuoCespite() == null || src.getDettagliAmmortamentoAnnuoCespite().isEmpty()) {
			return dest;
		}
		List<SiacTCespitiAmmortamentoDett> siacTCespitiAmmortamentoDetts = new ArrayList<SiacTCespitiAmmortamentoDett>();
		for (DettaglioAmmortamentoAnnuoCespite dettaglioAmmortamentoAnnuoCespite : src.getDettagliAmmortamentoAnnuoCespite()) {
			SiacTCespitiAmmortamentoDett siacTCespitiAmmortamentoDett = map(dettaglioAmmortamentoAnnuoCespite, SiacTCespitiAmmortamentoDett.class, CespMapId.SiacTCespitiAmmortamentoDett_DettaglioAmmortamentoAnnuoCespite_ModelDetail);
			siacTCespitiAmmortamentoDetts.add(siacTCespitiAmmortamentoDett);
		}
		dest.setSiacTCespitiAmmortamentoDetts(siacTCespitiAmmortamentoDetts);
		return dest;
	}


}
