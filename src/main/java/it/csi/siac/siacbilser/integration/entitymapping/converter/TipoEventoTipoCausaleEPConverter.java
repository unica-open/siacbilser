/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.ArrayList;
import java.util.List;

import org.dozer.DozerConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.EnumEntityFactory;
import it.csi.siac.siacbilser.integration.entity.SiacDEventoTipo;
import it.csi.siac.siacbilser.integration.entity.SiacRCausaleEpTipoEventoTipo;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDCausaleEpTipoEnum;
import it.csi.siac.siacgenser.model.TipoCausale;
import it.csi.siac.siacgenser.model.TipoEvento;

@Component
public class TipoEventoTipoCausaleEPConverter extends DozerConverter<TipoEvento, SiacDEventoTipo> {
	@Autowired
	private EnumEntityFactory eef;
	
	public TipoEventoTipoCausaleEPConverter() {
		super(TipoEvento.class, SiacDEventoTipo.class);
	}

	@Override
	public TipoEvento convertFrom(SiacDEventoTipo src, TipoEvento dest) {
		//final String methodName = "convertFrom";
		List<TipoCausale> listaTipoCausaleEP= new ArrayList<TipoCausale>();
		if(src.getSiacRCausaleEpTipoEventoTipos()!=null){
			for(SiacRCausaleEpTipoEventoTipo r : src.getSiacRCausaleEpTipoEventoTipos()){
				if(r.getDataCancellazione()!=null) {
					continue;
				}
				
				TipoCausale tipoCausale = SiacDCausaleEpTipoEnum.byCodice(r.getSiacDCausaleEpTipo().getCausaleEpTipoCode()).getTipoCausale();
				listaTipoCausaleEP.add(tipoCausale);
			}
			dest.setListaTipoCausaleEP(listaTipoCausaleEP);
		}
		return dest;
	}

	@Override
	public SiacDEventoTipo convertTo(TipoEvento src, SiacDEventoTipo dest) {
		
		return dest;
	}

}
