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
import it.csi.siac.siacbilser.integration.entity.SiacDOnere;
import it.csi.siac.siacbilser.integration.entity.SiacDSplitreverseIvaTipo;
import it.csi.siac.siacbilser.integration.entity.SiacROnereSplitreverseIvaTipo;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDSplitreverseIvaTipoEnum;
import it.csi.siac.siacfin2ser.model.TipoIvaSplitReverse;
import it.csi.siac.siacfin2ser.model.TipoOnere;


/**
 * The Class TipoOnereTipoIvaSplitReverseConverter.
 */
@Component
public class TipoOnereTipoIvaSplitReverseConverter extends DozerConverter<TipoOnere, SiacDOnere > {
	
	/** The eef. */
	@Autowired
	private EnumEntityFactory eef;
	
	/**
	 * Instantiates a new tipo onere tipo iva split reverse converter.
	 */
	public TipoOnereTipoIvaSplitReverseConverter() {
		super(TipoOnere.class, SiacDOnere.class);
	}

	@Override
	public TipoOnere convertFrom(SiacDOnere src, TipoOnere dest) {
		if(src.getSiacROnereSplitreverseIvaTipos() != null) {
			for(SiacROnereSplitreverseIvaTipo srosit : src.getSiacROnereSplitreverseIvaTipos()) {
				if(srosit.getDataCancellazione() == null) {
					SiacDSplitreverseIvaTipo siacDSplitreverseIvaTipo = srosit.getSiacDSplitreverseIvaTipo();
					TipoIvaSplitReverse tipoIvaSplitReverse = SiacDSplitreverseIvaTipoEnum.byCodice(siacDSplitreverseIvaTipo.getSrivaTipoCode()).getTipoIvaSplitReverse();
					dest.setTipoIvaSplitReverse(tipoIvaSplitReverse);
				}
			}
		}
		
		return dest;
	}

	@Override
	public SiacDOnere convertTo(TipoOnere src, SiacDOnere dest) {
		if(src.getTipoIvaSplitReverse() != null) {
			SiacDSplitreverseIvaTipoEnum siacDSplitreverseIvaTipoEnum = SiacDSplitreverseIvaTipoEnum.byTipoIvaSplitReverse(src.getTipoIvaSplitReverse());
			SiacDSplitreverseIvaTipo siacDSplitreverseIvaTipo = eef.getEntity(siacDSplitreverseIvaTipoEnum, src.getEnte().getUid(), SiacDSplitreverseIvaTipo.class);
			
			// Costruisco la lista
			List<SiacROnereSplitreverseIvaTipo> siacROnereSplitreverseIvaTipos = new ArrayList<SiacROnereSplitreverseIvaTipo>();
			// Costruisco la R
			SiacROnereSplitreverseIvaTipo srosit = new SiacROnereSplitreverseIvaTipo();
			srosit.setSiacDOnere(dest);
			srosit.setSiacDSplitreverseIvaTipo(siacDSplitreverseIvaTipo);
			srosit.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
			srosit.setLoginOperazione(dest.getLoginOperazione());
			
			siacROnereSplitreverseIvaTipos.add(srosit);
			dest.setSiacROnereSplitreverseIvaTipos(siacROnereSplitreverseIvaTipos);
		}
		
		return dest;
	}
	
}
