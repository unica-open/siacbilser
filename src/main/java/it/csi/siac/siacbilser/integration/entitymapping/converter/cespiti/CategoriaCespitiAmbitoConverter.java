/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter.cespiti;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.EnumEntityFactory;
import it.csi.siac.siacbilser.integration.entity.SiacDAmbito;
import it.csi.siac.siacbilser.integration.entity.SiacDCespitiCategoria;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDAmbitoEnum;
import it.csi.siac.siacbilser.integration.entitymapping.converter.ExtendedDozerConverter;
import it.csi.siac.siacbilser.model.Ambito;
import it.csi.siac.siaccespser.model.CategoriaCespiti;

/**
 * The Class CategoriaCespitiAmbitoConverter.
 *
 * @author Domenico
 */
@Component
public class CategoriaCespitiAmbitoConverter extends ExtendedDozerConverter<CategoriaCespiti, SiacDCespitiCategoria > {
	
	@Autowired private EnumEntityFactory eef;
	

	/**
	 * Instantiates a new categoria cespiti ambito converter.
	 */
	public CategoriaCespitiAmbitoConverter() {
		super(CategoriaCespiti.class, SiacDCespitiCategoria.class);
	}

	@Override
	public CategoriaCespiti convertFrom(SiacDCespitiCategoria src, CategoriaCespiti dest) {
		String methodName = "convertFrom";
		if(src.getSiacDAmbito() == null){
			log.warn(methodName, "CategoriaCespiti [uid: "+src.getUid()+"] priva di Ambito! Controllare su DB. Entita associata:"+src.getClass().getSimpleName());
			return dest;
		}
		
		Ambito ambito = SiacDAmbitoEnum.byCodice(src.getSiacDAmbito().getAmbitoCode()).getAmbito();
		
		dest.setAmbito(ambito);
		log.info(methodName, "START");

		return dest;
		
	}

	@Override
	public SiacDCespitiCategoria convertTo(CategoriaCespiti src, SiacDCespitiCategoria dest) {
		String methodName = "convertTo";
		if(src.getAmbito() == null) {
			throw new IllegalArgumentException("Ambito associato alla CategoriaCespiti obbligatorio. non specificato. [null]");
		}
		
		SiacDAmbitoEnum siacDAmbitoEnum = SiacDAmbitoEnum.byAmbito(src.getAmbito());
		SiacDAmbito siacDAmbito = eef.getEntity(siacDAmbitoEnum, dest.getSiacTEnteProprietario().getUid(), SiacDAmbito.class); 
		dest.setSiacDAmbito(siacDAmbito);
		return dest;
	}

}
