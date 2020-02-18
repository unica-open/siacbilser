/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siaccommon.util.log.LogUtil;
import it.csi.siac.siacfinser.integration.dao.EnumEntityFinFactory;
import it.csi.siac.siacfinser.integration.entity.SiacDMutuoTipoFin;
import it.csi.siac.siacfinser.integration.entity.enumeration.SiacDMutuoTipoEnum;
import it.csi.siac.siacfinser.model.mutuo.Mutuo.TipoMutuo;


	
	
@Component
public class MutuoTipoConverter extends FinExtendedDozerConverter<TipoMutuo, SiacDMutuoTipoFin> { 


	private LogUtil log = new LogUtil(this.getClass());
	
	@Autowired
	private EnumEntityFinFactory eef;
	
	public MutuoTipoConverter() {
		super(TipoMutuo.class, SiacDMutuoTipoFin.class);
		
	}

	@Override
	public TipoMutuo convertFrom(SiacDMutuoTipoFin src, TipoMutuo dest) {
		
			if(src.getMutTipoCode()!=null){
				return SiacDMutuoTipoEnum.byCodice(src.getMutTipoCode()).getTipoMutuo();
				
			}else {
				return null;				
			}
			
					
	}
	
	@Override
	public SiacDMutuoTipoFin convertTo(TipoMutuo src, SiacDMutuoTipoFin dest) {
		final String methodName = "convertTo";
		
		
		
		if(dest == null) {
			return dest;
		}
		
		SiacDMutuoTipoEnum enumTipoMutuo = SiacDMutuoTipoEnum.byTipoMutuo(src);
		
		
		SiacDMutuoTipoFin siacDMutuoTipo = eef.getEntity(enumTipoMutuo, dest.getSiacTEnteProprietario().getUid(), SiacDMutuoTipoFin.class);
		
		
		return siacDMutuoTipo;
		
	
		
	}

}
