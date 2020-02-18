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
import it.csi.siac.siacbilser.integration.entity.SiacDDocStato;
import it.csi.siac.siacbilser.integration.entity.SiacRDocStato;
import it.csi.siac.siacbilser.integration.entity.SiacTDoc;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDDocStatoEnum;
import it.csi.siac.siaccommon.util.log.LogUtil;
import it.csi.siac.siacfin2ser.model.StatoOperativoDocumento;

// TODO: Auto-generated Javadoc
/**
 * The Class DocumentoEntrataCodiceBolloConverter.
 */
@Component
public class DocumentoEntrataCodiceBolloConverter extends DozerConverter<StatoOperativoDocumento, SiacTDoc > {
	
	/** The log. */
	private LogUtil log = new LogUtil(this.getClass());
	
	/** The eef. */
	@Autowired
	private EnumEntityFactory eef;

	/**
	 * Instantiates a new documento entrata codice bollo converter.
	 */
	public DocumentoEntrataCodiceBolloConverter() {
		super(StatoOperativoDocumento.class, SiacTDoc.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public StatoOperativoDocumento convertFrom(SiacTDoc src, StatoOperativoDocumento dest) {
		for (SiacRDocStato siacRDocStato : src.getSiacRDocStatos()) {
			if(siacRDocStato.getDataCancellazione()==null){
				return SiacDDocStatoEnum.byCodice(siacRDocStato.getSiacDDocStato().getDocStatoCode()).getStatoOperativo();
			}
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacTDoc convertTo(StatoOperativoDocumento src, SiacTDoc dest) {
		final String methodName = "convertTo";
		
		if(dest== null) {
			return dest;
		}
		
		List<SiacRDocStato> siacRDocStatos = new ArrayList<SiacRDocStato>();
		SiacRDocStato siacRDocStato = new SiacRDocStato();
	
		SiacDDocStatoEnum variazioneStato =  SiacDDocStatoEnum.byStatoOperativo(src);
		SiacDDocStato siacDDocStato = eef.getEntity(variazioneStato, dest.getSiacTEnteProprietario().getUid(), SiacDDocStato.class); 
				
		log .debug(methodName, "setting siacDDocStato to: "+siacDDocStato.getDocStatoCode()+ " ["+siacDDocStato.getUid()+"]");
		siacRDocStato.setSiacDDocStato(siacDDocStato);
		siacRDocStato.setSiacTDoc(dest);		
		
		siacRDocStato.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
		siacRDocStato.setLoginOperazione(dest.getLoginOperazione());
		
		
		siacRDocStatos.add(siacRDocStato);
		dest.setSiacRDocStatos(siacRDocStatos);
		
		return dest;
	}



	

}
