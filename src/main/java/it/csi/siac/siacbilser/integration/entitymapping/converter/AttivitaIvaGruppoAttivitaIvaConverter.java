/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacRIvaGruppoAttivita;
import it.csi.siac.siacbilser.integration.entity.SiacTIvaAttivita;
import it.csi.siac.siacbilser.integration.entity.SiacTIvaGruppo;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacfin2ser.model.AttivitaIva;
import it.csi.siac.siacfin2ser.model.GruppoAttivitaIva;

// TODO: Auto-generated Javadoc
/**
 * The Class AttivitaIvaGruppoAttivitaIvaConverter.
 */
@Component
public class AttivitaIvaGruppoAttivitaIvaConverter extends ExtendedDozerConverter<AttivitaIva,SiacTIvaAttivita > {

	/**
	 * Instantiates a new attivita iva gruppo attivita iva converter.
	 */
	public AttivitaIvaGruppoAttivitaIvaConverter() {
		super(AttivitaIva.class, SiacTIvaAttivita.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public AttivitaIva convertFrom(SiacTIvaAttivita src, AttivitaIva dest) {
		if (src.getSiacRIvaGruppoAttivitas() != null) {
			GruppoAttivitaIva gruppoAttivitaIva = new GruppoAttivitaIva();
			for (SiacRIvaGruppoAttivita siacRIvaGruppoAttivita : src.getSiacRIvaGruppoAttivitas()) {
				//se ho la data di cancellazione passo alla relazione successiva
				if(siacRIvaGruppoAttivita.getDataCancellazione()!=null){
					continue;
				}

				SiacTIvaGruppo siacTIvaGruppo = siacRIvaGruppoAttivita.getSiacTIvaGruppo();

				gruppoAttivitaIva = map(siacTIvaGruppo, GruppoAttivitaIva.class,
						BilMapId.SiacTIvaGruppo_GruppoAttivitaIva);

			}
			dest.setGruppoAttivitaIva(gruppoAttivitaIva);
		}

		return dest;

	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacTIvaAttivita convertTo(AttivitaIva src, SiacTIvaAttivita dest) {
		if(src == null || src.getGruppoAttivitaIva() == null || dest== null) {
			return dest;
		}
		
		List<SiacRIvaGruppoAttivita> siacRIvaGruppoAttivitas = new ArrayList<SiacRIvaGruppoAttivita>();
		
		GruppoAttivitaIva gruppoAtivitaIva = src.getGruppoAttivitaIva();
			
		SiacRIvaGruppoAttivita siacRIvaGruppoAttivita = new SiacRIvaGruppoAttivita();	
		
		SiacTIvaGruppo siacTIvaGruppo = new SiacTIvaGruppo();		
		siacTIvaGruppo.setUid(gruppoAtivitaIva.getUid());
							
					
		siacRIvaGruppoAttivita.setSiacTIvaGruppo(siacTIvaGruppo);
		siacRIvaGruppoAttivita.setSiacTIvaAttivita(dest);		
			
		siacRIvaGruppoAttivita.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
		siacRIvaGruppoAttivita.setLoginOperazione(dest.getLoginOperazione());
			
		siacRIvaGruppoAttivitas.add(siacRIvaGruppoAttivita);
		
		dest.setSiacRIvaGruppoAttivitas(siacRIvaGruppoAttivitas);
		
		return dest;
	}



	

}
