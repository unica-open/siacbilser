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
 * The Class GruppoAttivitaIvaAttivitaIvaConverter.
 */
@Component
public class GruppoAttivitaIvaAttivitaIvaConverter extends ExtendedDozerConverter<GruppoAttivitaIva,SiacTIvaGruppo > {

	/**
	 * Instantiates a new gruppo attivita iva attivita iva converter.
	 */
	public GruppoAttivitaIvaAttivitaIvaConverter() {
		super(GruppoAttivitaIva.class, SiacTIvaGruppo.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public GruppoAttivitaIva convertFrom(SiacTIvaGruppo src,
			GruppoAttivitaIva dest) {
		if (src.getSiacRIvaGruppoAttivitas() != null) {

			List<AttivitaIva> listaAttivitaIva = new ArrayList<AttivitaIva>();
			for (SiacRIvaGruppoAttivita siacRIvaGruppoAttivita : src
					.getSiacRIvaGruppoAttivitas()) {
				//se ho la data di cancellazione passo alla relazione successiva
				if(siacRIvaGruppoAttivita.getDataCancellazione()!=null){
					continue;
				}

				SiacTIvaAttivita siacTIvaAttivita = siacRIvaGruppoAttivita
						.getSiacTIvaAttivita();

				AttivitaIva attivitaIva = map(siacTIvaAttivita, AttivitaIva.class,
						BilMapId.SiacTIvaAttivita_AttivitaIva_Minimal);
				listaAttivitaIva.add(attivitaIva);
			}
			dest.setListaAttivitaIva(listaAttivitaIva);
		}

		return dest;

	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacTIvaGruppo convertTo(GruppoAttivitaIva src, SiacTIvaGruppo dest) {
		if(dest== null) {
			return dest;
		}
		
		List<SiacRIvaGruppoAttivita> siacRIvaGruppoAttivitas = new ArrayList<SiacRIvaGruppoAttivita>();
		for(AttivitaIva attivitaIva: src.getListaAttivitaIva()){
			
			SiacRIvaGruppoAttivita siacRIvaGruppoAttivita = new SiacRIvaGruppoAttivita();
		
			SiacTIvaAttivita siacTIvaAttivita = new SiacTIvaAttivita();
			siacTIvaAttivita.setUid(attivitaIva.getUid());
			
			siacRIvaGruppoAttivita.setSiacTIvaAttivita(siacTIvaAttivita);
			siacRIvaGruppoAttivita.setSiacTIvaGruppo(dest);
			
			siacRIvaGruppoAttivita.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
			siacRIvaGruppoAttivita.setLoginOperazione(dest.getLoginOperazione());
			
			siacRIvaGruppoAttivitas.add(siacRIvaGruppoAttivita);
		}
		
		dest.setSiacRIvaGruppoAttivitas(siacRIvaGruppoAttivitas);
		
		return dest;
	}



	

}
