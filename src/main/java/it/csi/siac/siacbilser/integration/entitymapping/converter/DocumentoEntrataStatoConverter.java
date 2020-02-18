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
import it.csi.siac.siacbilser.integration.dao.SiacTDocRepository;
import it.csi.siac.siacbilser.integration.entity.SiacDDocStato;
import it.csi.siac.siacbilser.integration.entity.SiacRDocStato;
import it.csi.siac.siacbilser.integration.entity.SiacTDoc;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDDocStatoEnum;
import it.csi.siac.siaccommon.util.log.LogUtil;
import it.csi.siac.siacfin2ser.model.DocumentoEntrata;
import it.csi.siac.siacfin2ser.model.StatoOperativoDocumento;

/**
 * The Class DocumentoEntrataStatoConverter.
 */
@Component
public class DocumentoEntrataStatoConverter extends DozerConverter<DocumentoEntrata, SiacTDoc > {
	
	/** The log. */
	private LogUtil log = new LogUtil(this.getClass());
	
	/** The eef. */
	@Autowired
	private EnumEntityFactory eef;
	
	@Autowired
	private SiacTDocRepository siacTDocRepository;

	/**
	 * Instantiates a new documento entrata stato converter.
	 */
	public DocumentoEntrataStatoConverter() {
		super(DocumentoEntrata.class, SiacTDoc.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public DocumentoEntrata convertFrom(SiacTDoc src, DocumentoEntrata dest) {
		String methodName = "convertFrom";
		log.debug(methodName, "src: " + src);
		log.debug(methodName, "src.uid: " + (src!=null?src.getUid():"null"));
		log.debug(methodName, "src.siacRDocStatos: " + (src!=null?src.getSiacRDocStatos():"null"));
		
		if(src==null){
			return dest;
		}
		
		List<SiacRDocStato> siacRDocStatos;
		if(src.getSiacRDocStatos() != null){
			siacRDocStatos = src.getSiacRDocStatos();
		}else{
			siacRDocStatos = siacTDocRepository.findSiacRDocStatos(src.getUid());
		}
		
		for (SiacRDocStato siacRDocStato : siacRDocStatos) {
			if(siacRDocStato.getDataCancellazione()==null){
				StatoOperativoDocumento statoOperativoDocumento = SiacDDocStatoEnum.byCodice(siacRDocStato.getSiacDDocStato().getDocStatoCode()).getStatoOperativo();
				dest.setStatoOperativoDocumento(statoOperativoDocumento);
			}
		}
		
		return dest;
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacTDoc convertTo(DocumentoEntrata src, SiacTDoc dest) {
		final String methodName = "convertTo";
		
		if(dest== null) {
			return dest;
		}
		
		List<SiacRDocStato> siacRDocStatos = new ArrayList<SiacRDocStato>();
		SiacRDocStato siacRDocStato = new SiacRDocStato();
	
		SiacDDocStatoEnum variazioneStato =  SiacDDocStatoEnum.byStatoOperativo(src.getStatoOperativoDocumento());
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
