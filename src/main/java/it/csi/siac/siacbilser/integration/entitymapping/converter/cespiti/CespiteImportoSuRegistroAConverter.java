/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter.cespiti;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.integration.dao.cespiti.SiacTCespitiRepository;
import it.csi.siac.siacbilser.integration.entity.SiacRCespitiMovEpDet;
import it.csi.siac.siacbilser.integration.entity.SiacTCespiti;
import it.csi.siac.siacbilser.integration.entitymapping.converter.ExtendedDozerConverter;
import it.csi.siac.siaccespser.model.Cespite;
import it.csi.siac.siacgenser.model.MovimentoDettaglio;

/**
 * The Class CespitiTipoBeneCespiteConverter.
 *
 * @author Anto
 */
@Component
public class CespiteImportoSuRegistroAConverter extends ExtendedDozerConverter<Cespite, SiacTCespiti > {
	
	@Autowired
	SiacTCespitiRepository siacTCespitiRepository;
	
	public CespiteImportoSuRegistroAConverter() {
		super(Cespite.class, SiacTCespiti.class);
	}

	@Override
	public Cespite convertFrom(SiacTCespiti src, Cespite dest) {
		
		List<SiacRCespitiMovEpDet> siacRCespitiMovEpDets = src.getSiacRCespitiMovEpDets();
		if(siacRCespitiMovEpDets == null || siacRCespitiMovEpDets.isEmpty()) {
			return dest;
		}
		MovimentoDettaglio movDet = Utility.MDETTL.get();
		Integer uidMovimento  = movDet != null && movDet.getUid() != 0? Integer.valueOf(movDet.getUid()) : null;
		for (SiacRCespitiMovEpDet siacRCespitiMovEpDet : siacRCespitiMovEpDets) {
			if(siacRCespitiMovEpDet.getDataCancellazione() != null) {
				continue;
			}
			if( uidMovimento == null || (siacRCespitiMovEpDet.getSiacTMovEpDet() != null && siacRCespitiMovEpDet.getSiacTMovEpDet().getUid() == uidMovimento.intValue())) {
				dest.setImportoSuRegistroA(siacRCespitiMovEpDet.getImportoSuPrimaNota());
				return dest;
			}
		}
		return dest;
	}

	@Override
	public SiacTCespiti convertTo(Cespite src, SiacTCespiti dest) {
		return dest;
	}


}
