/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.EnumEntityFactory;
import it.csi.siac.siacbilser.integration.entity.SiacDCausaleEpTipo;
import it.csi.siac.siacbilser.integration.entity.SiacTCausaleEp;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDCausaleEpTipoEnum;
import it.csi.siac.siacgenser.model.CausaleEP;
import it.csi.siac.siacgenser.model.TipoCausale;

@Component
public class CausaleEPTipoConverter extends ExtendedDozerConverter<CausaleEP, SiacTCausaleEp > {
	
//	<a>this</a> <!-- tipoCausale -->
//	<b>this</b> <!-- siacDCausaleEpTipo -->
	
	@Autowired
	private EnumEntityFactory eef;
	

	public CausaleEPTipoConverter() {
		super(CausaleEP.class, SiacTCausaleEp.class);
	}

	@Override
	public CausaleEP convertFrom(SiacTCausaleEp src, CausaleEP dest) {
		final String methodName = "convertFrom";
		
		TipoCausale tipoCausale = SiacDCausaleEpTipoEnum.byCodice(src.getSiacDCausaleEpTipo().getCausaleEpTipoCode()).getTipoCausale();
		
		log .debug(methodName, "setting tipoCausale to: "+tipoCausale+ " [SiacTCausaleEp.uid:"+src.getUid()+"]");
		
		dest.setTipoCausale(tipoCausale);
		return dest;
	}

	@Override
	public SiacTCausaleEp convertTo(CausaleEP src, SiacTCausaleEp dest) {
		final String methodName = "convertTo";
		
		if(src.getTipoCausale() == null) {
			return dest;
		}
		
		SiacDCausaleEpTipoEnum tipo =  SiacDCausaleEpTipoEnum.byTipoCausale(src.getTipoCausale());
		SiacDCausaleEpTipo siacDCausaleEpTipo = eef.getEntity(tipo, dest.getSiacTEnteProprietario().getUid(), SiacDCausaleEpTipo.class); 
				
		log .debug(methodName, "setting siacDCausaleEpTipo to: "+siacDCausaleEpTipo.getCausaleEpTipoCode()+ " [uid:"+siacDCausaleEpTipo.getUid()+"]");
		dest.setSiacDCausaleEpTipo(siacDCausaleEpTipo);
		
		return dest;
	}



	

}
