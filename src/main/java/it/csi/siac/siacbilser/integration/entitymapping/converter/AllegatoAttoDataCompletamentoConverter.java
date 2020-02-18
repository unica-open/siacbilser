/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.dozer.DozerConverter;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacRAttoAllegatoStato;
import it.csi.siac.siacbilser.integration.entity.SiacTAttoAllegato;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDAttoAllegatoStatoEnum;
import it.csi.siac.siacfin2ser.model.AllegatoAtto;

 /**
 * The Class AllegatoAttoDataCompletamentoConverter.
 */
@Component
public class AllegatoAttoDataCompletamentoConverter extends DozerConverter<AllegatoAtto, SiacTAttoAllegato> {
	
	
	
	/**
	 * Instantiates a new documento spesa data inizio validita stato converter.
	 */
	public AllegatoAttoDataCompletamentoConverter() {
		super(AllegatoAtto.class, SiacTAttoAllegato.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public AllegatoAtto convertFrom(SiacTAttoAllegato src, AllegatoAtto dest) {
		String ultimoCodice = null;
		
		for(SiacRAttoAllegatoStato raas : src.getSiacRAttoAllegatoStatos()) {
			String codice = raas.getSiacDAttoAllegatoStato().getAttoalStatoCode();
			
			if(!codice.equals(ultimoCodice)) {
				ultimoCodice = codice;
				 if(SiacDAttoAllegatoStatoEnum.COMPLETATO.getCodice().equals(codice)) {
					 dest.setDataCompletamento(raas.getDataCreazione());
					 
					 return dest;
				 }
			}
		}

		return dest;
	}
	

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacTAttoAllegato convertTo(AllegatoAtto src, SiacTAttoAllegato dest) {
		return dest;
	}


}
