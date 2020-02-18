/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity.converter;

import org.dozer.DozerConverter;
import org.springframework.stereotype.Component;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siaccommon.util.log.LogUtil;
import it.csi.siac.siacfinser.integration.entity.SiacTAttoAmmFin;
import it.csi.siac.siacfinser.integration.entity.SiacTCartacontFin;
import it.csi.siac.siacfinser.integration.util.EntityToModelConverter;

@Component
public class CartaAttoAmmConverter extends DozerConverter<AttoAmministrativo, SiacTCartacontFin> {

	private LogUtil log = new LogUtil(this.getClass());
	
	public CartaAttoAmmConverter() {
		super(AttoAmministrativo.class, SiacTCartacontFin.class);
	}
	
	@Override
	public AttoAmministrativo convertFrom(SiacTCartacontFin src,AttoAmministrativo dest) {
		
		final String methodName = "convertFrom";
		
		SiacTAttoAmmFin siacTAttoAmm = src.getSiacTAttoAmm();
		
		// L'atto e' facoltativo quindi potrebbe essere passato null.
		if(siacTAttoAmm!=null){
			log.debug(methodName, "siacRProgrammaAttoAmm.siacTAttoAmm.uid: " + siacTAttoAmm.getUid());		
			if(dest == null){
				dest = new AttoAmministrativo();
			}
			AttoAmministrativo attoAmm = EntityToModelConverter.siacTAttoToAttoAmministrativo(siacTAttoAmm);
			if(attoAmm!=null){
				dest = attoAmm;
			}
		}else{
			dest = new AttoAmministrativo();
		}
		
		
		return dest;
	}

	@Override
	public SiacTCartacontFin convertTo(AttoAmministrativo src, SiacTCartacontFin dest) {
		
		SiacTAttoAmmFin siacTAttoAmm = null;
		// L'atto e' facoltativo quindi potrebbe essere passato null.
		if(src != null && src.getUid() != 0) {
			siacTAttoAmm = new SiacTAttoAmmFin();
			siacTAttoAmm.setAttoammId(src.getUid());
		}
		
		dest.setSiacTAttoAmm(siacTAttoAmm);
		
		return dest;
	}

}
