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
import it.csi.siac.siaccommonser.util.log.LogSrvUtil;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.StatoOperativoDocumento;

/**
 * The Class DocumentoSpesaStatoConverter.
 */
@Component
public class DocumentoSpesaStatoConverter extends DozerConverter<DocumentoSpesa, SiacTDoc > {
	
	/** The log. */
	private LogSrvUtil log = new LogSrvUtil(this.getClass());
	
	/** The eef. */
	@Autowired
	private EnumEntityFactory eef;
	
	@Autowired
	private SiacTDocRepository siacTDocRepository;

	/**
	 * Instantiates a new documento spesa stato converter.
	 */
	public DocumentoSpesaStatoConverter() {
		super(DocumentoSpesa.class, SiacTDoc.class);
	}

	@Override
	public DocumentoSpesa convertFrom(SiacTDoc src, DocumentoSpesa dest) {
		
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

	@Override
	public SiacTDoc convertTo(DocumentoSpesa src, SiacTDoc dest) {
		final String methodName = "convertTo";
		
		if(dest == null) {
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
