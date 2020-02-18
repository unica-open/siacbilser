/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter.cespiti;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacDCespitiBeneTipo;
import it.csi.siac.siacbilser.integration.entity.SiacTCespiti;
import it.csi.siac.siacbilser.integration.entitymapping.CespMapId;
import it.csi.siac.siacbilser.integration.entitymapping.converter.ExtendedDozerConverter;
import it.csi.siac.siaccespser.model.Cespite;
import it.csi.siac.siaccespser.model.TipoBeneCespite;

/**
 * The Class CespitiTipoBeneCespiteConverter.
 *
 * @author Anto
 */
@Component
public class CespiteTipoBeneCespiteConverter extends ExtendedDozerConverter<Cespite, SiacTCespiti > {
	
	public CespiteTipoBeneCespiteConverter() {
		super(Cespite.class, SiacTCespiti.class);
	}

	@Override
	public Cespite convertFrom(SiacTCespiti src, Cespite dest) {
		String methodName = "convertFrom";
		
		if(src.getSiacDCespitiBeneTipo()== null){
			log.warn(methodName, "Cespite [uid: "+src.getUid()+"] privo del Tipo Bene! Controllare su DB. Entita associata:"+src.getClass().getSimpleName());
			return dest;
		}
	
				
		TipoBeneCespite tipoBeneCespiti = mapNotNull(src.getSiacDCespitiBeneTipo(),TipoBeneCespite.class,CespMapId.SiacDCespitiBeneTipo_TipoBeneCespite_ModelDetail );
		
		dest.setTipoBeneCespite(tipoBeneCespiti );
		return dest;
		
	}

	@Override
	public SiacTCespiti convertTo(Cespite src, SiacTCespiti dest) {
		if(src.getTipoBeneCespite() == null || src.getTipoBeneCespite().getUid() == 0) {
			return dest;
		}
		SiacDCespitiBeneTipo siacDCespitiBeneTipo = new SiacDCespitiBeneTipo();
		siacDCespitiBeneTipo.setUid(src.getTipoBeneCespite().getUid());
		dest.setSiacDCespitiBeneTipo(siacDCespitiBeneTipo);
		return dest;
	}


}
