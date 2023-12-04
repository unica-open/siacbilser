/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacRIvaRegistroGruppo;
import it.csi.siac.siacbilser.integration.entity.SiacTIvaGruppo;
import it.csi.siac.siacbilser.integration.entity.SiacTIvaRegistro;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacfin2ser.model.GruppoAttivitaIva;
import it.csi.siac.siacfin2ser.model.RegistroIva;

// TODO: Auto-generated Javadoc
/**
 * The Class RegistroIvaGruppoAttivitaConverter.
 */
@Component
public class RegistroIvaGruppoAttivitaConverter extends ExtendedDozerConverter<RegistroIva,SiacTIvaRegistro > {

	/**
	 * Instantiates a new registro iva gruppo attivita converter.
	 */
	public RegistroIvaGruppoAttivitaConverter() {
		super(RegistroIva.class, SiacTIvaRegistro.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public RegistroIva convertFrom(SiacTIvaRegistro src, RegistroIva dest) {
		if (src.getSiacRIvaRegistroGruppos() != null) {

			for (SiacRIvaRegistroGruppo siacRIvaRegistroGruppo : src
					.getSiacRIvaRegistroGruppos()) {
				//se ho la data di cancellazione passo alla relazione successiva
				if(siacRIvaRegistroGruppo.getDataCancellazione()!=null){
					continue;
				}

				SiacTIvaGruppo siacTIvaGruppo = siacRIvaRegistroGruppo.getSiacTIvaGruppo();

				GruppoAttivitaIva gruppoAttivitaIva = map(siacTIvaGruppo, GruppoAttivitaIva.class,
						getMapIdGruppoAttivitaIva());
				dest.setGruppoAttivitaIva(gruppoAttivitaIva);
				break;
			}
			
		}
		return dest;
	}

	protected BilMapId getMapIdGruppoAttivitaIva() {
		return BilMapId.SiacTIvaGruppo_GruppoAttivitaIva;
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacTIvaRegistro convertTo(RegistroIva src, SiacTIvaRegistro dest) {
		if(dest== null || src == null || src.getGruppoAttivitaIva() == null) {
			return dest;
		}
		
		GruppoAttivitaIva gruppoAttivitaIva= src.getGruppoAttivitaIva();
		SiacTIvaGruppo siacTIvaGruppo = new SiacTIvaGruppo();
		siacTIvaGruppo.setUid(gruppoAttivitaIva.getUid());
		
		List<SiacRIvaRegistroGruppo> siacRIvaRegistroGruppos = new ArrayList<SiacRIvaRegistroGruppo>();
		SiacRIvaRegistroGruppo siacRIvaRegistroGruppo= new SiacRIvaRegistroGruppo();
		
		siacRIvaRegistroGruppo.setSiacTIvaGruppo(siacTIvaGruppo);
		siacRIvaRegistroGruppo.setSiacTIvaRegistro(dest);
		
		siacRIvaRegistroGruppo.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
		siacRIvaRegistroGruppo.setLoginOperazione(dest.getLoginOperazione());
		siacRIvaRegistroGruppo.setDataCreazione(dest.getDataCreazione());
		
		siacRIvaRegistroGruppos.add(siacRIvaRegistroGruppo);
		dest.setSiacRIvaRegistroGruppos(siacRIvaRegistroGruppos);
		
		return dest;
	}

	



	

}
