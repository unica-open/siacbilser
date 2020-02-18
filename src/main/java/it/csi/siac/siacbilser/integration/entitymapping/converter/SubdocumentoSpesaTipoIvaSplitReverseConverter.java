/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.EnumEntityFactory;
import it.csi.siac.siacbilser.integration.entity.SiacDSplitreverseIvaTipo;
import it.csi.siac.siacbilser.integration.entity.SiacRSubdocSplitreverseIvaTipo;
import it.csi.siac.siacbilser.integration.entity.SiacTSubdoc;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDSplitreverseIvaTipoEnum;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfin2ser.model.TipoIvaSplitReverse;

/**
 * The Class SubdocumentoSpesaTipoIvaSplitReverseConverter.
 */
@Component
public class SubdocumentoSpesaTipoIvaSplitReverseConverter extends ExtendedDozerConverter<SubdocumentoSpesa, SiacTSubdoc> {

	@Autowired
	private EnumEntityFactory eef;
	
	/**
	 * Instantiates a new subdocumento spesa tipo iva split reverse converter.
 	*/
	public SubdocumentoSpesaTipoIvaSplitReverseConverter() {
		super(SubdocumentoSpesa.class, SiacTSubdoc.class);
	}

	@Override
	public SubdocumentoSpesa convertFrom(SiacTSubdoc src, SubdocumentoSpesa dest) {
		
		if(src.getSiacRSubdocSplitreverseIvaTipos() != null) {
			for(SiacRSubdocSplitreverseIvaTipo siacRSubdocSplitreverseIvaTipo : src.getSiacRSubdocSplitreverseIvaTipos()){
				if(siacRSubdocSplitreverseIvaTipo.getDataCancellazione() != null){
					continue;
				}
				
				SiacDSplitreverseIvaTipo siacDSplitreverseIvaTipo = siacRSubdocSplitreverseIvaTipo.getSiacDSplitreverseIvaTipo();
				
				if(siacDSplitreverseIvaTipo != null) {
					TipoIvaSplitReverse tipoIvaSplitReverse = SiacDSplitreverseIvaTipoEnum.byCodice(siacDSplitreverseIvaTipo.getSrivaTipoCode()).getTipoIvaSplitReverse();
					dest.setTipoIvaSplitReverse(tipoIvaSplitReverse);
					break;
				}
			}
		}
		
		return dest;
	}

	@Override
	public SiacTSubdoc convertTo(SubdocumentoSpesa src, SiacTSubdoc dest) {
		if(src.getTipoIvaSplitReverse() != null) {
			TipoIvaSplitReverse tipoIvaSplitReverse = src.getTipoIvaSplitReverse();
			
			SiacDSplitreverseIvaTipoEnum siacDSplitreverseIvaTipoEnum = SiacDSplitreverseIvaTipoEnum.byTipoIvaSplitReverse(tipoIvaSplitReverse);
			SiacDSplitreverseIvaTipo siacDSplitreverseIvaTipo = eef.getEntity(siacDSplitreverseIvaTipoEnum, src.getEnte().getUid(), SiacDSplitreverseIvaTipo.class);
			
			SiacRSubdocSplitreverseIvaTipo siacRSubdocSplitreverseIvaTipo = new SiacRSubdocSplitreverseIvaTipo();
			siacRSubdocSplitreverseIvaTipo.setSiacTSubdoc(dest);
			siacRSubdocSplitreverseIvaTipo.setSiacDSplitreverseIvaTipo(siacDSplitreverseIvaTipo);
			siacRSubdocSplitreverseIvaTipo.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
			siacRSubdocSplitreverseIvaTipo.setLoginOperazione(dest.getLoginOperazione());
			
			List<SiacRSubdocSplitreverseIvaTipo> siacRSubdocSplitreverseIvaTipos = new ArrayList<SiacRSubdocSplitreverseIvaTipo>();
			siacRSubdocSplitreverseIvaTipos.add(siacRSubdocSplitreverseIvaTipo);
			
			dest.setSiacRSubdocSplitreverseIvaTipos(siacRSubdocSplitreverseIvaTipos);
			
		}
		return dest;
	}

}
