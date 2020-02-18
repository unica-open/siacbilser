/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.SiacTPeriodoRepository;
import it.csi.siac.siacbilser.integration.entity.SiacDBilElemDetCompTipo;
import it.csi.siac.siacbilser.integration.entity.SiacTPeriodo;
import it.csi.siac.siacbilser.model.TipoComponenteImportiCapitolo;

@Component
public class TipoComponenteImportiCapitoloAnnoConverter extends ExtendedDozerConverter<TipoComponenteImportiCapitolo, SiacDBilElemDetCompTipo> {

	@Autowired
	private SiacTPeriodoRepository siacTPeriodoRepository;
	
	public TipoComponenteImportiCapitoloAnnoConverter() {
		super(TipoComponenteImportiCapitolo.class, SiacDBilElemDetCompTipo.class);
	}

	@Override
	public TipoComponenteImportiCapitolo convertFrom(SiacDBilElemDetCompTipo src, TipoComponenteImportiCapitolo dest) {
		if(src.getSiacTPeriodo() != null) {
			SiacTPeriodo siacTPeriodo = src.getSiacTPeriodo();
			dest.setAnno(Integer.valueOf(siacTPeriodo.getAnno()));
		}
		
        return dest;
	}

	@Override
	public SiacDBilElemDetCompTipo convertTo(TipoComponenteImportiCapitolo src, SiacDBilElemDetCompTipo dest) {

		if (src.getAnno() != null) {
			SiacTPeriodo siacTPeriodo = siacTPeriodoRepository.findByAnnoAndEnteProprietario(
				src.getAnno().toString(), 
				dest.getSiacTEnteProprietario().getUid());
			
			 dest.setSiacTPeriodo(siacTPeriodo);
		}
		 
		return dest;
	}
}
