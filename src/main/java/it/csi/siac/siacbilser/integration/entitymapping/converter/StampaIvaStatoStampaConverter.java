/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.EnumEntityFactory;
import it.csi.siac.siacbilser.integration.entity.SiacDIvaStampaStato;
import it.csi.siac.siacbilser.integration.entity.SiacRIvaStampaStato;
import it.csi.siac.siacbilser.integration.entity.SiacTIvaStampa;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDIvaStampaStatoEnum;
import it.csi.siac.siacfin2ser.model.StampaIva;
import it.csi.siac.siacfin2ser.model.TipoStampa;

// TODO: Auto-generated Javadoc
/**
 * The Class GruppoAttivitaIvaRegistroIvaConverter.
 */
@Component
public class StampaIvaStatoStampaConverter extends ExtendedDozerConverter<StampaIva,SiacTIvaStampa > {

	@Autowired
	private EnumEntityFactory eef;
	
	/**
	 * Instantiates a new gruppo attivita iva registro iva converter.
	 */
	public StampaIvaStatoStampaConverter() {
		super(StampaIva.class, SiacTIvaStampa.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public StampaIva convertFrom(SiacTIvaStampa src, StampaIva dest) {
		
		if(src.getSiacRIvaStampaStatos()!=null){
			for(SiacRIvaStampaStato siacRIvaStampaStato: src.getSiacRIvaStampaStatos()){
				if(siacRIvaStampaStato.getDataCancellazione()==null){
					TipoStampa tipoStampa = TipoStampa.fromCodice(siacRIvaStampaStato.getSiacDIvaStampaStato().getIvastStatoCode());
					dest.setTipoStampa(tipoStampa);
				}
			}
		}
        return dest;

	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacTIvaStampa convertTo(StampaIva src, SiacTIvaStampa dest) {
			
		List<SiacRIvaStampaStato> siacRIvaStampaStatos = new ArrayList<SiacRIvaStampaStato>();
		SiacRIvaStampaStato siacRIvaStampaStato = new SiacRIvaStampaStato();
		
		SiacDIvaStampaStatoEnum siacDIvaStampaStatoEnum = SiacDIvaStampaStatoEnum.byCodice(src.getTipoStampa().getCodice());
		
		SiacDIvaStampaStato siacDIvaStampaStato = eef.getEntity(siacDIvaStampaStatoEnum, dest.getSiacTEnteProprietario().getUid(), SiacDIvaStampaStato.class);
		siacRIvaStampaStato.setSiacDIvaStampaStato(siacDIvaStampaStato);
		siacRIvaStampaStato.setSiacTIvaStampa(dest);
		siacRIvaStampaStato.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
		siacRIvaStampaStato.setLoginOperazione(dest.getLoginOperazione());
		siacRIvaStampaStatos.add(siacRIvaStampaStato );
		dest.setSiacRIvaStampaStatos(siacRIvaStampaStatos);

		return dest;
	}



	

}
