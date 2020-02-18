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
import it.csi.siac.siacbilser.integration.entity.SiacDRichiestaEconStato;
import it.csi.siac.siacbilser.integration.entity.SiacRRichiestaEconStato;
import it.csi.siac.siacbilser.integration.entity.SiacTRichiestaEcon;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDRichiestaEconStatoEnum;
import it.csi.siac.siaccecser.model.RichiestaEconomale;

/**
 * The Class DocumentoEntrataSubdocumentoIvaConverter.
 */
@Component
public class RichiestaEconomaleStatoOperativoConverter extends ExtendedDozerConverter<RichiestaEconomale, SiacTRichiestaEcon > {
	
	@Autowired
	private EnumEntityFactory eef;
	
	/**
	 * Instantiates a new subdocumento Entrata subdocumento iva converter.
	 */
	public RichiestaEconomaleStatoOperativoConverter() {
		super(RichiestaEconomale.class, SiacTRichiestaEcon.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public RichiestaEconomale convertFrom(SiacTRichiestaEcon src, RichiestaEconomale dest) {
		
		if(src.getSiacRRichiestaEconStatos() == null){
			return dest;
		}
		for(SiacRRichiestaEconStato siacRRichiestaEconStato :  src.getSiacRRichiestaEconStatos()){
			if(siacRRichiestaEconStato.getDataCancellazione() == null){
				SiacDRichiestaEconStatoEnum siacDRichiestaEconStatoEnum = SiacDRichiestaEconStatoEnum.byCodice(siacRRichiestaEconStato.getSiacDRichiestaEconStato().getRiceconStatoCode());
				dest.setStatoOperativoRichiestaEconomale(siacDRichiestaEconStatoEnum.getStatoOperativo());
			}
		}
		return dest;
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacTRichiestaEcon convertTo(RichiestaEconomale src, SiacTRichiestaEcon dest) {
		
		if(src.getStatoOperativoRichiestaEconomale() == null){
			return dest;
		}
		
		SiacDRichiestaEconStatoEnum siacDRichiestaEconStatoEnum = SiacDRichiestaEconStatoEnum.byStatoOperativo(src.getStatoOperativoRichiestaEconomale());
		SiacDRichiestaEconStato siacDRichiestaEconStato = eef.getEntity(siacDRichiestaEconStatoEnum, dest.getSiacTEnteProprietario().getUid(), SiacDRichiestaEconStato.class);
		
		
		SiacRRichiestaEconStato siacRRichiestaEconStato = new SiacRRichiestaEconStato();
		siacRRichiestaEconStato.setSiacTRichiestaEcon(dest);
		siacRRichiestaEconStato.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
		siacRRichiestaEconStato.setLoginOperazione(src.getLoginOperazione());
		siacRRichiestaEconStato.setSiacDRichiestaEconStato(siacDRichiestaEconStato);
		
		
		List<SiacRRichiestaEconStato> siacRRichiestaEconStatos = new ArrayList<SiacRRichiestaEconStato>();
		siacRRichiestaEconStatos.add(siacRRichiestaEconStato);
		dest.setSiacRRichiestaEconStatos(siacRRichiestaEconStatos);
		return dest;
		
	}



	

}
