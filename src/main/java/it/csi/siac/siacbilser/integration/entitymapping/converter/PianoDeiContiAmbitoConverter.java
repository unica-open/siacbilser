/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.EnumEntityFactory;
import it.csi.siac.siacbilser.integration.entity.SiacDAmbito;
import it.csi.siac.siacbilser.integration.entity.SiacTPdceFamTree;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDAmbitoEnum;
import it.csi.siac.siacbilser.model.Ambito;
import it.csi.siac.siacgenser.model.PianoDeiConti;

@Component
public class PianoDeiContiAmbitoConverter extends ExtendedDozerConverter<PianoDeiConti, SiacTPdceFamTree> {
	
	@Autowired
	private EnumEntityFactory eef;
	

	public PianoDeiContiAmbitoConverter() {
		super(PianoDeiConti.class, SiacTPdceFamTree.class);
	}

	@Override
	public PianoDeiConti convertFrom(SiacTPdceFamTree src, PianoDeiConti dest) {
		final String methodName = "convertFrom";
		
		if(src.getSiacDAmbito() == null){
			return dest;
		}
		
		Ambito ambito = SiacDAmbitoEnum.byCodice(src.getSiacDAmbito().getAmbitoCode()).getAmbito();
		log .debug(methodName, "setting ambito to: "+ambito+ " [SiacTPdceConto.uid:"+src.getUid()+"]");
		
		dest.setAmbito(ambito);
		return dest;
	}

	@Override
	public SiacTPdceFamTree convertTo(PianoDeiConti src, SiacTPdceFamTree dest) {
		final String methodName = "convertTo";
		
		if(src.getAmbito() == null) {
			return dest;
		}
		
		SiacDAmbitoEnum siacDAmbitoEnum = SiacDAmbitoEnum.byAmbito(src.getAmbito());
		SiacDAmbito siacDAmbito = eef.getEntity(siacDAmbitoEnum, dest.getSiacTEnteProprietario().getUid(), SiacDAmbito.class);
		
		log .debug(methodName, "setting siacDAmbito to: "+siacDAmbito.getAmbitoCode()+ " [uid:"+siacDAmbito.getUid()+"]");
		dest.setSiacDAmbito(siacDAmbito);
		
		return dest;
	}



	

}
