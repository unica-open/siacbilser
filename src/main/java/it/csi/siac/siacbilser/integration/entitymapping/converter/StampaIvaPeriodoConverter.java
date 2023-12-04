/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.SiacTPeriodoRepository;
import it.csi.siac.siacbilser.integration.entity.SiacTIvaStampa;
import it.csi.siac.siacbilser.integration.entity.SiacTPeriodo;
import it.csi.siac.siacfin2ser.model.Periodo;
import it.csi.siac.siacfin2ser.model.StampaIva;

// TODO: Auto-generated Javadoc
/**
 * The Class StampaIvaPeriodoConverter.
 */
@Component
public class StampaIvaPeriodoConverter extends ExtendedDozerConverter<StampaIva,SiacTIvaStampa > {

	@Autowired
	private SiacTPeriodoRepository siacTPeriodoRepository;
	
	/**
	 * Instantiates a new gruppo attivita iva registro iva converter.
	 */
	public StampaIvaPeriodoConverter() {
		super(StampaIva.class, SiacTIvaStampa.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public StampaIva convertFrom(SiacTIvaStampa src, StampaIva dest) {
		if(src.getSiacTPeriodo()!=null){
			SiacTPeriodo siacTPeriodo = src.getSiacTPeriodo();
			dest.setAnnoEsercizio(Integer.valueOf(siacTPeriodo.getAnno()));
//			dest.setAnnoEsercizio(new Integer(siacTPeriodo.getAnno()));
			Periodo periodo = Periodo.fromCodice(siacTPeriodo.getSiacDPeriodoTipo().getPeriodoTipoCode());
			dest.setPeriodo(periodo);			
		}
        return dest;

	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacTIvaStampa convertTo(StampaIva src, SiacTIvaStampa dest) {

		 SiacTPeriodo siacTPeriodo = siacTPeriodoRepository.findByAnnoAndPeriodoTipoAndEnteProprietario(src.getAnnoEsercizio().toString(),
				src.getPeriodo().getCodice(), src.getEnte().getUid());
		 dest.setSiacTPeriodo(siacTPeriodo);
		 dest.setIvastAnno(src.getAnnoEsercizio());
		 return dest;
	}



	

}
