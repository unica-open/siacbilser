/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacRIvaStampaRegistro;
import it.csi.siac.siacbilser.integration.entity.SiacTIvaRegistro;
import it.csi.siac.siacbilser.integration.entity.SiacTIvaStampa;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacfin2ser.model.RegistroIva;
import it.csi.siac.siacfin2ser.model.StampaIva;

// TODO: Auto-generated Javadoc
/**
 * The Class GruppoAttivitaIvaAttivitaIvaConverter.
 */
@Component
public class StampaIvaRegistroIvaConverter extends ExtendedDozerConverter<StampaIva,SiacTIvaStampa > {
	
	

	/**
	 * Instantiates a new gruppo attivita iva attivita iva converter.
	 */
	public StampaIvaRegistroIvaConverter() {
		super(StampaIva.class, SiacTIvaStampa.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public StampaIva convertFrom(SiacTIvaStampa src, StampaIva dest) {
		if (src.getSiacRIvaStampaRegistros() != null) {
			List<RegistroIva> listaRegistri = new ArrayList<RegistroIva>();
			for (SiacRIvaStampaRegistro siacRIvaStampaRegistro : src.getSiacRIvaStampaRegistros()) {
				//se ho la data di cancellazione passo alla relazione successiva
				if(siacRIvaStampaRegistro.getDataCancellazione()!=null){
					continue;
				}

				SiacTIvaRegistro siacTIvaRegistro =siacRIvaStampaRegistro.getSiacTIvaRegistro();

				RegistroIva registroIva = map(siacTIvaRegistro, RegistroIva.class, getMapIdRegistroIva());
				
				listaRegistri.add(registroIva);
			}
			dest.setListaRegistroIva(listaRegistri);
		}

		return dest;

	}

	protected BilMapId getMapIdRegistroIva() {
		
		return BilMapId.SiacTIvaRegistro_RegistroIva_Base;
		
		/*
		try{
			String parameter = getParameter(); //sono componenti singletone!!! questo crea casini! 
			return BilMapId.valueOf(parameter);
		} catch(RuntimeException e){
			return BilMapId.SiacTIvaRegistro_RegistroIva_Base;
		}	
		*/	
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacTIvaStampa convertTo(StampaIva src, SiacTIvaStampa dest) {
		
		List<SiacRIvaStampaRegistro> siacRIvaStampaRegistros = new ArrayList<SiacRIvaStampaRegistro>();
		
		if(src.getListaRegistroIva()!=null){
			for(RegistroIva registro : src.getListaRegistroIva()){
				
				SiacRIvaStampaRegistro siacRIvaStampaRegistro = new SiacRIvaStampaRegistro();	
				
				SiacTIvaRegistro siacTIvaRegistro = map(registro,SiacTIvaRegistro.class, BilMapId.SiacTIvaRegistro_RegistroIva_Base);	
					
				siacRIvaStampaRegistro.setSiacTIvaRegistro(siacTIvaRegistro);
				siacRIvaStampaRegistro.setSiacTIvaStampa(dest);
				siacRIvaStampaRegistro.setLoginOperazione(dest.getLoginOperazione());
				siacRIvaStampaRegistro.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
						
				siacRIvaStampaRegistros.add(siacRIvaStampaRegistro);
			}
			
				
			dest.setSiacRIvaStampaRegistros(siacRIvaStampaRegistros);
		}
		return dest;
	}



	

}
