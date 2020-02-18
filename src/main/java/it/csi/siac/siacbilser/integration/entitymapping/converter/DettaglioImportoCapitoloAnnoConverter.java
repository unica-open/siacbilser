/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.SiacTPeriodoRepository;
import it.csi.siac.siacbilser.integration.entity.SiacTBilElemDet;
import it.csi.siac.siacbilser.integration.entity.SiacTPeriodo;
import it.csi.siac.siacbilser.model.utils.DettaglioImportoCapitolo;

/**
 * The Class DettaglioImportoCapitoloAnnoConverter.
 */
@Component
public class DettaglioImportoCapitoloAnnoConverter extends ExtendedDozerConverter<DettaglioImportoCapitolo, SiacTBilElemDet> {
	
	@Autowired private SiacTPeriodoRepository siacTPeriodoRepository;

	/**
	 * Constructor
	 */
	public DettaglioImportoCapitoloAnnoConverter() {
		super(DettaglioImportoCapitolo.class, SiacTBilElemDet.class);
	}

	@Override
	public DettaglioImportoCapitolo convertFrom(SiacTBilElemDet src, DettaglioImportoCapitolo dest) {
		if(src.getSiacTPeriodo() != null) {
			dest.setAnno(Integer.valueOf(src.getSiacTPeriodo().getAnno()));
		}
		return dest;
	}
	

	@Override
	public SiacTBilElemDet convertTo(DettaglioImportoCapitolo src, SiacTBilElemDet dest) {
		if(src.getAnno() != null && src.getEnte() != null && src.getEnte().getUid() != 0) {
			SiacTPeriodo siacTPeriodo = siacTPeriodoRepository.findByAnnoAndEnteProprietario(src.getAnno().toString(), src.getEnte().getUid());
			dest.setSiacTPeriodo(siacTPeriodo);
		}
		return dest;
	}

}
