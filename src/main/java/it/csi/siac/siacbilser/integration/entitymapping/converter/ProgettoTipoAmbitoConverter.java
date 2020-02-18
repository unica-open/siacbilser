/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacRProgrammaClass;
import it.csi.siac.siacbilser.integration.entity.SiacTClass;
import it.csi.siac.siacbilser.integration.entity.SiacTProgramma;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDClassTipoEnum;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacbilser.model.TipoAmbito;

/**
 * Converter per il TipoAmbito tra Progetto e SiacTProgramma.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 05/02/2014
 *
 */
@Component
public class ProgettoTipoAmbitoConverter extends ExtendedDozerConverter<TipoAmbito, SiacTProgramma> {
	
	/**
	 * Instantiates a new progetto tipo ambito converter.
	 */
	public ProgettoTipoAmbitoConverter() {
		super(TipoAmbito.class, SiacTProgramma.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public TipoAmbito convertFrom(SiacTProgramma src, TipoAmbito dest) {
		for(SiacRProgrammaClass siacRProgrammaClass : src.getSiacRProgrammaClasses()) {
			SiacTClass siacTClass = siacRProgrammaClass.getSiacTClass();
			SiacDClassTipoEnum siacDClassTipoEnum = SiacDClassTipoEnum.byCodiceEvenNull(siacTClass.getSiacDClassTipo().getClassifTipoCode());
			
			if(siacRProgrammaClass.getDataCancellazione() == null && SiacDClassTipoEnum.TipoAmbito.equals(siacDClassTipoEnum)) {
				dest = new TipoAmbito();
				map(siacTClass, dest, BilMapId.SiacTClass_ClassificatoreGenerico);
				return dest;
			}
		}
		
		return null;
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacTProgramma convertTo(TipoAmbito src, SiacTProgramma dest) {
		final String methodName = "convertTo";
		//jira 1935 quando non viene scelto il tipo ambito--->uid=0 significa il tipo classificatore e' null
		if(src == null || src.getUid()==0) {
			return dest;
		}
		
		List<SiacRProgrammaClass> siacRProgrammaClasses = new ArrayList<SiacRProgrammaClass>();
		SiacRProgrammaClass siacRProgrammaClass = new SiacRProgrammaClass();
		SiacTClass siacTClass = new SiacTClass();
		
		siacTClass.setUid(src.getUid());
		
		siacRProgrammaClass.setSiacTClass(siacTClass);
		siacRProgrammaClass.setSiacTProgramma(dest);
		siacRProgrammaClass.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
		siacRProgrammaClass.setLoginOperazione(dest.getLoginOperazione());
		
		siacRProgrammaClasses.add(siacRProgrammaClass);
		dest.setSiacRProgrammaClasses(siacRProgrammaClasses);
		
		log.debug(methodName, "Coversione effettuata per uid: " + src.getUid());
		return dest;
	}
	
}
