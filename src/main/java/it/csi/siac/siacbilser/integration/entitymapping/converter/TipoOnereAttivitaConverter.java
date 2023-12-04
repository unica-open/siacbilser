/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacDOnere;
import it.csi.siac.siacbilser.integration.entity.SiacDOnereAttivita;
import it.csi.siac.siacbilser.integration.entity.SiacROnereAttivita;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacfin2ser.model.AttivitaOnere;
import it.csi.siac.siacfin2ser.model.TipoOnere;


/**
 * The Class OnereAttrConverter.
 */
@Component
public class TipoOnereAttivitaConverter extends ExtendedDozerConverter<TipoOnere, SiacDOnere > {
	
	/**
	 * Instantiates a new onere attr converter.
	 */
	public TipoOnereAttivitaConverter() {
		super(TipoOnere.class, SiacDOnere.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public TipoOnere convertFrom(SiacDOnere src, TipoOnere dest) {
		
		if(src.getSiacROnereAttivitas() == null){
			return dest;
		}
		List<AttivitaOnere> listaAttivitaOnere = new ArrayList<AttivitaOnere>();
		for(SiacROnereAttivita siacROnereAttivita : src.getSiacROnereAttivitas()){
			
			if((src.getDateToExtract() == null && siacROnereAttivita.getDataCancellazione()!=null )
					|| (src.getDateToExtract() != null && !src.getDateToExtract().equals(siacROnereAttivita.getDataInizioValidita()))){
				continue;
			}			
			
			AttivitaOnere attivitaOnere = new AttivitaOnere();
			map(siacROnereAttivita.getSiacDOnereAttivita(),attivitaOnere, BilMapId.SiacDOnereAttivita_AttivitaOnere);
			listaAttivitaOnere.add(attivitaOnere);
		}
		dest.setAttivitaOnere(listaAttivitaOnere);
		return dest;
	}



	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacDOnere convertTo(TipoOnere tipoOnere, SiacDOnere dest) {	
		
		List<SiacROnereAttivita> siacROnereAttivitas = new ArrayList<SiacROnereAttivita>();
		
		for(AttivitaOnere attivitaOnere : tipoOnere.getAttivitaOnere()){
			SiacROnereAttivita siacROnereAttivita = new SiacROnereAttivita();
			
			SiacDOnereAttivita siacDOnereAttivita = new SiacDOnereAttivita();
			siacDOnereAttivita.setUid(attivitaOnere.getUid());
			siacROnereAttivita.setSiacDOnereAttivita(siacDOnereAttivita);
			
			siacROnereAttivita.setLoginOperazione(dest.getLoginOperazione());
			siacROnereAttivita.setSiacDOnere(dest);
			siacROnereAttivita.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
			
			siacROnereAttivitas.add(siacROnereAttivita);
		}
		
		dest.setSiacROnereAttivitas(siacROnereAttivitas);
		return dest;	
		
	}
	
}
