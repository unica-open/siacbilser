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
import it.csi.siac.siacbilser.integration.entity.SiacDCassaEconStampaStato;
import it.csi.siac.siacbilser.integration.entity.SiacRCassaEconStampaStato;
import it.csi.siac.siacbilser.integration.entity.SiacTCassaEconStampa;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDCassaEconStampaStatoEnum;
import it.csi.siac.siaccecser.model.StampeCassaFile;
import it.csi.siac.siaccecser.model.TipoStampa;
/**
 * the class   StampeCassaFileStatoStampaConverter
 * @author Paggio Simona
 * @version 1.0.0 - 11/03/2015
 *
 */
@Component
public class StampeCassaFileStatoStampaConverter extends ExtendedDozerConverter<StampeCassaFile,SiacTCassaEconStampa > {
	@Autowired
	private EnumEntityFactory eef;
	/**
	 * Instantiates a new gruppo attivita iva registro iva converter.
	 */
	public StampeCassaFileStatoStampaConverter() {
		super(StampeCassaFile.class, SiacTCassaEconStampa.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public StampeCassaFile convertFrom(SiacTCassaEconStampa src, StampeCassaFile dest) {
		
		if(src.getSiacRCassaEconStampaStatos()!=null){
			for(SiacRCassaEconStampaStato siacRCassaEconStampaStato: src.getSiacRCassaEconStampaStatos()){
				if(siacRCassaEconStampaStato.getDataCancellazione()==null){
					TipoStampa tipoStampa = TipoStampa.fromCodice(siacRCassaEconStampaStato.getSiacDCassaEconStampaStato().getCestStatoCode());
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
	public SiacTCassaEconStampa convertTo(StampeCassaFile src, SiacTCassaEconStampa dest) {
			
		List<SiacRCassaEconStampaStato> siacRCassaEconStampaStatos = new ArrayList<SiacRCassaEconStampaStato>();
		SiacRCassaEconStampaStato siacRCassaEconStampaStato = new SiacRCassaEconStampaStato();
		
		SiacDCassaEconStampaStatoEnum siacDCassaEconStampaStatoEnum = SiacDCassaEconStampaStatoEnum.byCodice(src.getTipoStampa().getCodice());
		
		SiacDCassaEconStampaStato siacDCassaEconStampaStato = eef.getEntity(siacDCassaEconStampaStatoEnum, dest.getSiacTEnteProprietario().getUid(), SiacDCassaEconStampaStato.class);
		siacRCassaEconStampaStato.setSiacDCassaEconStampaStato(siacDCassaEconStampaStato);
		siacRCassaEconStampaStato.setSiacTCassaEconStampa(dest);
		siacRCassaEconStampaStato.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
		siacRCassaEconStampaStato.setLoginOperazione(dest.getLoginOperazione());
		siacRCassaEconStampaStatos.add(siacRCassaEconStampaStato );
		dest.setSiacRCassaEconStampaStatos(siacRCassaEconStampaStatos);

		return dest;
	}

}
