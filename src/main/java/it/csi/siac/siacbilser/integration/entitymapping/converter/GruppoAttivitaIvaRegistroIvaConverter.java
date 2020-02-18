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
 * The Class GruppoAttivitaIvaRegistroIvaConverter.
 */
@Component
public class GruppoAttivitaIvaRegistroIvaConverter extends ExtendedDozerConverter<GruppoAttivitaIva,SiacTIvaGruppo > {

	/**
	 * Instantiates a new gruppo attivita iva registro iva converter.
	 */
	public GruppoAttivitaIvaRegistroIvaConverter() {
		super(GruppoAttivitaIva.class, SiacTIvaGruppo.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public GruppoAttivitaIva convertFrom(SiacTIvaGruppo src,
			GruppoAttivitaIva dest) {
		if (src.getSiacRIvaRegistroGruppos() != null) {

			List<RegistroIva> listaRegistroIva = new ArrayList<RegistroIva>();
			for (SiacRIvaRegistroGruppo siacRIvaRegistroGruppo : src
					.getSiacRIvaRegistroGruppos()) {
				//se ho la data di cancellazione passo alla relazione successiva
				if(siacRIvaRegistroGruppo.getDataCancellazione()!=null){
					continue;
				}

				SiacTIvaRegistro siacTIvaRegistro = siacRIvaRegistroGruppo
						.getSiacTIvaRegistro();

				RegistroIva registroIva = map(siacTIvaRegistro, RegistroIva.class,
						BilMapId.SiacTIvaRegistro_RegistroIva_Base);
				listaRegistroIva.add(registroIva);
			}
			dest.setListaRegistroIva(listaRegistroIva);
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
		
//		List<SiacRIvaRegistroGruppo> siacRIvaRegistroGruppos = new ArrayList<SiacRIvaRegistroGruppo>();
//		for(RegistroIva registroIva: src.getListaRegistroIva()){
//			
//			SiacRIvaRegistroGruppo siacRIvaRegistroGruppo = new SiacRIvaRegistroGruppo();	
//		
//			SiacTIvaRegistro siacTIvaRegistro = new SiacTIvaRegistro();		
//			siacTIvaRegistro.setUid(registroIva.getUid());
//							
//					
//			siacRIvaRegistroGruppo.setSiacTIvaRegistro(siacTIvaRegistro);
//			siacRIvaRegistroGruppo.setSiacTIvaGruppo(dest);		
//			
//			siacRIvaRegistroGruppo.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
//			siacRIvaRegistroGruppo.setLoginOperazione(dest.getLoginOperazione());
//			
//			siacRIvaRegistroGruppos.add(siacRIvaRegistroGruppo);
//		}
//		
//		
//		dest.setSiacRIvaRegistroGruppos(siacRIvaRegistroGruppos);
//		
		return dest;
	}



	

}
