/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.SiacTBilRepository;
import it.csi.siac.siacbilser.integration.entity.SiacTBil;
import it.csi.siac.siacbilser.integration.entity.SiacTPeriodo;
import it.csi.siac.siacbilser.integration.entity.SiacTVincolo;
import it.csi.siac.siacbilser.model.Vincolo;

// TODO: Auto-generated Javadoc
/**
 * Converte da TipoVincoloCapitoli a SiacDVincoloTipo e viceversa.
 * 
 * @author Domenico
 *
 */
@Component
public class VincoloBilancioPeriodoConverter extends ExtendedDozerConverter<Vincolo, SiacTVincolo> {
	
	@Autowired
	private SiacTBilRepository siacTBilRepository;
	
	/**
	 * Instantiates a new vincolo tipo converter.
	 */
	public VincoloBilancioPeriodoConverter() {
		super(Vincolo.class, SiacTVincolo.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public Vincolo convertFrom(SiacTVincolo src, Vincolo dest) {
		return dest;
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacTVincolo convertTo(Vincolo src, SiacTVincolo dest) {
		if(src.getBilancio() == null){
			return dest;
		}
		SiacTBil siacTBil = siacTBilRepository.findOne(src.getBilancio().getUid());
		SiacTPeriodo siacTPeriodo = new SiacTPeriodo();
		siacTPeriodo.setUid(siacTBil.getSiacTPeriodo().getUid());
		dest.setSiacTPeriodo(siacTPeriodo);
		return dest;
	}
	

}
