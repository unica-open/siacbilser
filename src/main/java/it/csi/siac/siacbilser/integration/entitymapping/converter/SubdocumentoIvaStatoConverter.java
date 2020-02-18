/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.ArrayList;
import java.util.List;

import org.dozer.DozerConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.EnumEntityFactory;
import it.csi.siac.siacbilser.integration.entity.SiacDSubdocIvaStato;
import it.csi.siac.siacbilser.integration.entity.SiacRSubdocIvaStato;
import it.csi.siac.siacbilser.integration.entity.SiacTSubdocIva;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDSubdocIvaStatoEnum;
import it.csi.siac.siaccommon.util.log.LogUtil;
import it.csi.siac.siacfin2ser.model.StatoSubdocumentoIva;

// TODO: Auto-generated Javadoc
/**
 * The Class SubdocumentoIvaStatoConverter.
 */
@Component
public class SubdocumentoIvaStatoConverter extends DozerConverter<StatoSubdocumentoIva, SiacTSubdocIva > {
	
	/** The log. */
	private LogUtil log = new LogUtil(this.getClass());
	
	/** The eef. */
	@Autowired
	private EnumEntityFactory eef;

	/**
	 * Instantiates a new subdocumento iva stato converter.
	 */
	public SubdocumentoIvaStatoConverter() {
		super(StatoSubdocumentoIva.class, SiacTSubdocIva.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public StatoSubdocumentoIva convertFrom(SiacTSubdocIva src, StatoSubdocumentoIva dest) {
		for (SiacRSubdocIvaStato siacRSubdocIvaStato : src.getSiacRSubdocIvaStatos()) {
			if(siacRSubdocIvaStato.getDataCancellazione()==null){
				return SiacDSubdocIvaStatoEnum.byCodice(siacRSubdocIvaStato.getSiacDSubdocIvaStato().getSubdocivaStatoCode()).getStatoOperativo();
			}
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacTSubdocIva convertTo(StatoSubdocumentoIva src, SiacTSubdocIva dest) {
		final String methodName = "convertTo";
		
		if(dest== null) {
			return dest;
		}
		
		List<SiacRSubdocIvaStato> siacRSubdocIvaStatos = new ArrayList<SiacRSubdocIvaStato>();
		SiacRSubdocIvaStato siacRSubdocIvaStato = new SiacRSubdocIvaStato();
	
		SiacDSubdocIvaStatoEnum stato =  SiacDSubdocIvaStatoEnum.byStatoOperativo(src);
		SiacDSubdocIvaStato siacDSubdocIvaStato = eef.getEntity(stato, dest.getSiacTEnteProprietario().getUid(), SiacDSubdocIvaStato.class); 
				
		log .debug(methodName, "setting siacDDocStato to: "+siacDSubdocIvaStato.getSubdocivaStatoCode()+ " ["+siacDSubdocIvaStato.getUid()+"]");
		siacRSubdocIvaStato.setSiacDSubdocIvaStato(siacDSubdocIvaStato);
		siacRSubdocIvaStato.setSiacTSubdocIva(dest);		
		
		siacRSubdocIvaStato.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
		siacRSubdocIvaStato.setLoginOperazione(dest.getLoginOperazione());
		
		
		siacRSubdocIvaStatos.add(siacRSubdocIvaStato);
		dest.setSiacRSubdocIvaStatos(siacRSubdocIvaStatos);
		
		return dest;
	}



	

}
