/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.Date;

import org.dozer.DozerConverter;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacDCausale;
import it.csi.siac.siacfin2ser.model.Causale;
import it.csi.siac.siacfin2ser.model.StatoOperativoCausale;

/**
 * The Class CausaleStatoConverter.
 */
@Component
public class CausaleStatoConverter extends DozerConverter<Causale, SiacDCausale > {
	
	/**
	 * Instantiates a new causale stato converter.
	 */
	public CausaleStatoConverter() {
		super(Causale.class, SiacDCausale.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public Causale convertFrom(SiacDCausale src, Causale dest) {
		
		if( src.getDataFineValidita()!=null && new Date().after(src.getDataFineValidita())){
			dest.setStatoOperativoCausale(StatoOperativoCausale.ANNULLATA);
			
			dest.setDataAnnullamento(src.getDataFineValidita()); //src.getDataCancellazione());
		}else {
			dest.setStatoOperativoCausale(StatoOperativoCausale.VALIDA);
		}
		
		
		//dest.setDataFineValidita(src.getDataFineValidita());
		
		
		// imposta anche dataCompletamento, dataDefinizione e dataAnnullamento
		 //Quali di queste devo impostare?
		/*
		dest.setDataAnnullamento(dataAnnullamento)
		dest.setDataCreazione(dataCreazione)
		
		src.getDataCancellazione()
		src.getDataCreazione()
		src.getDataInizioValidita()*/
		
		return dest;
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacDCausale convertTo(Causale src, SiacDCausale dest) {
		
		return dest;
	}



	

}
