/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter.cespiti;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.cespiti.SiacTCespitiRepository;
import it.csi.siac.siacbilser.integration.entity.SiacRCespitiMovEpDet;
import it.csi.siac.siacbilser.integration.entity.SiacTCespiti;
import it.csi.siac.siacbilser.integration.entitymapping.converter.ExtendedDozerConverter;
import it.csi.siac.siaccespser.model.Cespite;

/**
 * The Class CespitiTipoBeneCespiteConverter.
 *
 * @author Anto
 */
@Component
public class CespiteIsInserimentoContestualeRegistroAConverter extends ExtendedDozerConverter<Cespite, SiacTCespiti > {
	
	@Autowired
	SiacTCespitiRepository siacTCespitiRepository;
	
	public CespiteIsInserimentoContestualeRegistroAConverter() {
		super(Cespite.class, SiacTCespiti.class);
	}

	@Override
	public Cespite convertFrom(SiacTCespiti src, Cespite dest) {
		
		List<SiacRCespitiMovEpDet> siacRCespitiMovEpDets = src.getSiacRCespitiMovEpDets();
		if(siacRCespitiMovEpDets == null || siacRCespitiMovEpDets.isEmpty()) {
			return dest;
		}
		
		for (SiacRCespitiMovEpDet siacRCespitiMovEpDet : siacRCespitiMovEpDets) {
			if(siacRCespitiMovEpDet.getDataCancellazione() == null) {
				dest.setInserimentoContestualeRegistroA(siacRCespitiMovEpDet.getCesContestuale());
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
