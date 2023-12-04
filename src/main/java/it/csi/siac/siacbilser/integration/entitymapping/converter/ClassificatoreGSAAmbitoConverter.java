/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.EnumEntityFactory;
import it.csi.siac.siacbilser.integration.entity.SiacDAmbito;
import it.csi.siac.siacbilser.integration.entity.SiacTGsaClassif;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDAmbitoEnum;
import it.csi.siac.siacbilser.model.Ambito;
import it.csi.siac.siacgenser.model.ClassificatoreGSA;

/**
 * The Class CausaleEPContoConverter.
 *
 * @author Domenico
 */
@Component
public class ClassificatoreGSAAmbitoConverter extends ExtendedDozerConverter<ClassificatoreGSA, SiacTGsaClassif > {
	
	@Autowired private EnumEntityFactory eef;

	/**
	 * Instantiates a new classificatore GSA classificatore GSA ambito converter.
	 */
	public ClassificatoreGSAAmbitoConverter() {
		super(ClassificatoreGSA.class, SiacTGsaClassif.class);
	}

	@Override
	public ClassificatoreGSA convertFrom(SiacTGsaClassif src, ClassificatoreGSA dest) {
		final String methodName = "convertFrom";
		
		if(src.getSiacDAmbito() == null){
			log.warn(methodName, "CausaleEP [uid: "+src.getUid()+"] priva di Ambito! Controllare su DB. Entita associata:"+src.getClass().getSimpleName());
			return dest;
		}
		
		Ambito ambito = SiacDAmbitoEnum.byCodice(src.getSiacDAmbito().getAmbitoCode()).getAmbito();
		
		dest.setAmbito(ambito);
		return dest;
		
	}


	@Override
	public SiacTGsaClassif convertTo(ClassificatoreGSA src, SiacTGsaClassif dest) {
		final String methodName = "convertTo";
		if(src.getAmbito() == null) {
			log.error(methodName,"Impossibile inserire un record senza un ambito associato ");
			throw new IllegalArgumentException("Ambito associato alla CausaleEP obbligatorio. non specificato. [null]");
		}
		
		SiacDAmbitoEnum siacDAmbitoEnum = SiacDAmbitoEnum.byAmbito(src.getAmbito());
		SiacDAmbito siacDAmbito = eef.getEntity(siacDAmbitoEnum, dest.getSiacTEnteProprietario().getUid(), SiacDAmbito.class); 
		
		dest.setSiacDAmbito(siacDAmbito);
		return dest;
	}


}
