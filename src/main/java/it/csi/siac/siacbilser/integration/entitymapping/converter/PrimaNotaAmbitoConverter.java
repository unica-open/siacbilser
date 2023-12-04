/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.EnumEntityFactory;
import it.csi.siac.siacbilser.integration.entity.SiacDAmbito;
import it.csi.siac.siacbilser.integration.entity.SiacTPrimaNota;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDAmbitoEnum;
import it.csi.siac.siacbilser.model.Ambito;
import it.csi.siac.siacgenser.model.PrimaNota;

/**
 * The Class PrimaNotaAmbitoConverter.
 *
 * @author Domenico
 */
@Component
public class PrimaNotaAmbitoConverter extends ExtendedDozerConverter<PrimaNota, SiacTPrimaNota > {
	
	@Autowired
	private EnumEntityFactory eef;
	

	public PrimaNotaAmbitoConverter() {
		super(PrimaNota.class, SiacTPrimaNota.class);
	}

	@Override
	public PrimaNota convertFrom(SiacTPrimaNota src, PrimaNota dest) {
		String methodName = "convertFrom";
		
		if(src.getSiacDAmbito() == null){
			log.warn(methodName, "PrimaNota [uid: "+src.getUid()+"] priva di Ambito! Controllare su DB. Entita associata:"+src.getClass().getSimpleName());
			return dest;
		}
		
		Ambito ambito = SiacDAmbitoEnum.byCodice(src.getSiacDAmbito().getAmbitoCode()).getAmbito();
		
		dest.setAmbito(ambito);
		return dest;
		
	}

	@Override
	public SiacTPrimaNota convertTo(PrimaNota src, SiacTPrimaNota dest) {
		
		if(src.getAmbito() == null) {
			throw new IllegalArgumentException("Ambito associato alla PrimaNota e' obbligatorio. non specificato. [null]");
		}
		
		SiacDAmbitoEnum siacDAmbitoEnum = SiacDAmbitoEnum.byAmbito(src.getAmbito());
		SiacDAmbito siacDAmbito = eef.getEntity(siacDAmbitoEnum, dest.getSiacTEnteProprietario().getUid(), SiacDAmbito.class); 
		
		dest.setSiacDAmbito(siacDAmbito);
		
		return dest;
	}



	

}
