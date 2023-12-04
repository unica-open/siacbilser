/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.EnumEntityFactory;
import it.csi.siac.siacbilser.integration.entity.SiacDAmbito;
import it.csi.siac.siacbilser.integration.entity.SiacTMovEpDet;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDAmbitoEnum;
import it.csi.siac.siacbilser.model.Ambito;
import it.csi.siac.siacgenser.model.MovimentoDettaglio;

/**
 * The Class MovimentoDettaglioAmbitoConverter.
 *
 * @author Domenico
 */
@Component
public class MovimentoDettaglioAmbitoConverter extends ExtendedDozerConverter<MovimentoDettaglio, SiacTMovEpDet > {
	
	@Autowired
	private EnumEntityFactory eef;
	

	public MovimentoDettaglioAmbitoConverter() {
		super(MovimentoDettaglio.class, SiacTMovEpDet.class);
	}

	@Override
	public MovimentoDettaglio convertFrom(SiacTMovEpDet src, MovimentoDettaglio dest) {
		String methodName = "convertFrom";
		
		if(src.getSiacDAmbito() == null){
			log.warn(methodName, "MovimentoDettaglio [uid: "+src.getUid()+"] privo di Ambito! Controllare su DB. Entita associata:"+src.getClass().getSimpleName());
			return dest;
		}
		
		Ambito ambito = SiacDAmbitoEnum.byCodice(src.getSiacDAmbito().getAmbitoCode()).getAmbito();
		
		dest.setAmbito(ambito);
		return dest;
		
	}

	@Override
	public SiacTMovEpDet convertTo(MovimentoDettaglio src, SiacTMovEpDet dest) {
		
		if(src.getAmbito() == null) {
			throw new IllegalArgumentException("Ambito associato alla MovimentoDettaglio obbligatorio. Non specificato. [null]");
		}
		
		SiacDAmbitoEnum siacDAmbitoEnum = SiacDAmbitoEnum.byAmbito(src.getAmbito());
		SiacDAmbito siacDAmbito = eef.getEntity(siacDAmbitoEnum, dest.getSiacTEnteProprietario().getUid(), SiacDAmbito.class); 
		
		dest.setSiacDAmbito(siacDAmbito);
		
		return dest;
	}



	

}
