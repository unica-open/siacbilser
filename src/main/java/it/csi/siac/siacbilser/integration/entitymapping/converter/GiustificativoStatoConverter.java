/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.EnumEntityFactory;
import it.csi.siac.siacbilser.integration.entity.SiacDGiustificativoStato;
import it.csi.siac.siacbilser.integration.entity.SiacRGiustificativoStato;
import it.csi.siac.siacbilser.integration.entity.SiacTEnteProprietario;
import it.csi.siac.siacbilser.integration.entity.SiacTGiustificativoDet;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDGiustificativoStatoEnum;
import it.csi.siac.siaccecser.model.Giustificativo;

/**
 * The Class GiustificativoStatoConverter.
 */
@Component
public class GiustificativoStatoConverter extends ExtendedDozerConverter<Giustificativo, SiacTGiustificativoDet> {
	
	@Autowired
	private EnumEntityFactory eef;
	

	/**
	 * Instantiates a new giustificativo stato converter.
	 */
	public GiustificativoStatoConverter() {
		super(Giustificativo.class, SiacTGiustificativoDet.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public Giustificativo convertFrom(SiacTGiustificativoDet src, Giustificativo dest) {
		
		if(src.getSiacRGiustificativoStatos() == null){
			return dest;
		}
		for(SiacRGiustificativoStato siacRRichiestaEconStato :  src.getSiacRGiustificativoStatos()){
			if(siacRRichiestaEconStato.getDataCancellazione() != null){
				continue;
			}
			
			String giustStatoCode = siacRRichiestaEconStato.getSiacDGiustificativoStato().getGiustStatoCode();
			SiacDGiustificativoStatoEnum siacDGiustificativoStatoEnum = SiacDGiustificativoStatoEnum.byCodice(giustStatoCode);
			dest.setStatoOperativoGiustificativi(siacDGiustificativoStatoEnum.getStatoOperativo());
		}
		
		return dest;
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacTGiustificativoDet convertTo(Giustificativo src, SiacTGiustificativoDet dest) {
		
		if(src.getStatoOperativoGiustificativi() == null){
			return dest;
		}
		
		SiacRGiustificativoStato siacRGiustificativoStato = new SiacRGiustificativoStato();
		
		SiacDGiustificativoStatoEnum siacDGiustificativoStatoEnum = SiacDGiustificativoStatoEnum.byStatoOperativo(src.getStatoOperativoGiustificativi());
		SiacDGiustificativoStato siacDGiustificativoStato = eef.getEntity(siacDGiustificativoStatoEnum, dest.getSiacTEnteProprietario().getUid(), SiacDGiustificativoStato.class);
		
		siacRGiustificativoStato.setSiacTGiustificativoDet(dest);
		siacRGiustificativoStato.setSiacDGiustificativoStato(siacDGiustificativoStato);
		//siacRGiustificativoStato.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
		siacRGiustificativoStato.setLoginOperazione(src.getLoginOperazione());
		SiacTEnteProprietario siacTEnteProprietario = new SiacTEnteProprietario();
		siacTEnteProprietario.setUid(src.getEnte().getUid());
		siacRGiustificativoStato.setSiacTEnteProprietario(siacTEnteProprietario);
		
		dest.setSiacRGiustificativoStatos(new ArrayList<SiacRGiustificativoStato>());
		dest.addSiacRGiustificativoStato(siacRGiustificativoStato);
		return dest;
		
	}



	

}
