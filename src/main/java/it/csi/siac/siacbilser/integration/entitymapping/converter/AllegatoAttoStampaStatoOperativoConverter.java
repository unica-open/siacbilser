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
import it.csi.siac.siacbilser.integration.entity.SiacDAllegatoAttoStampaStato;
import it.csi.siac.siacbilser.integration.entity.SiacRAllegatoAttoStampaStato;
import it.csi.siac.siacbilser.integration.entity.SiacTAttoAllegatoStampa;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDAllegatoAttoStampaStatoEnum;
import it.csi.siac.siacfin2ser.model.AllegatoAttoStampa;
import it.csi.siac.siacfin2ser.model.StatoOperativoStampaAllegatoAtto;

/**
 * The Class StampaAllegatoAttoStatoConverter
 */
@Component
public class AllegatoAttoStampaStatoOperativoConverter extends ExtendedDozerConverter<AllegatoAttoStampa, SiacTAttoAllegatoStampa> {

	
	@Autowired
	private EnumEntityFactory eef;
	/**
	 * Instantiates a new gruppo attivita iva registro iva converter.
	 */
	public AllegatoAttoStampaStatoOperativoConverter() {
		super(AllegatoAttoStampa.class, SiacTAttoAllegatoStampa.class);
	}

	@Override
	public AllegatoAttoStampa convertFrom(SiacTAttoAllegatoStampa src, AllegatoAttoStampa dest) {
		
		if(src.getSiacRAllegatoAttoStampaStatos()!=null){
			for(SiacRAllegatoAttoStampaStato siacRAllegatoAttoStampaStato: src.getSiacRAllegatoAttoStampaStatos()){
				if(siacRAllegatoAttoStampaStato.getDataCancellazione()==null){
					
					StatoOperativoStampaAllegatoAtto statoStampa = SiacDAllegatoAttoStampaStatoEnum.byCodice(siacRAllegatoAttoStampaStato.getSiacDAllegatoAttoStampaStato().getAttoalstStatoCode())
							.getStatoOperativoStampaAllegatoAtto();
					dest.setStatoOperativoStampa(statoStampa);
				}
			}
		}
        return dest;
	}
	
	@Override
	public SiacTAttoAllegatoStampa convertTo(AllegatoAttoStampa src, SiacTAttoAllegatoStampa dest) {
		
		if(src.getStatoOperativoStampa() == null) {
			return dest;
		}		
		
		List<SiacRAllegatoAttoStampaStato> siacRAttoAllegatoStampaStatos = new ArrayList<SiacRAllegatoAttoStampaStato>();
		SiacRAllegatoAttoStampaStato siacRAttoAllegatoStampaStato = new SiacRAllegatoAttoStampaStato();
		
		SiacDAllegatoAttoStampaStatoEnum siacDAttoAllegatoStampaStatoEnum = SiacDAllegatoAttoStampaStatoEnum.byStatoStampa(src.getStatoOperativoStampa());
		
		SiacDAllegatoAttoStampaStato siacDAllegatoAttoStampaStato = eef.getEntity(siacDAttoAllegatoStampaStatoEnum, dest.getSiacTEnteProprietario().getUid(), SiacDAllegatoAttoStampaStato.class);
		siacRAttoAllegatoStampaStato.setSiacDAllegatoAttoStampaStato(siacDAllegatoAttoStampaStato);
		siacRAttoAllegatoStampaStato.setSiacTAttoAllegatoStampa(dest);
		siacRAttoAllegatoStampaStato.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
		siacRAttoAllegatoStampaStato.setLoginOperazione(dest.getLoginOperazione());
		siacRAttoAllegatoStampaStatos.add(siacRAttoAllegatoStampaStato);
		dest.setSiacRAllegatoAttoStampaStatos(siacRAttoAllegatoStampaStatos);

		return dest;
	}

}
