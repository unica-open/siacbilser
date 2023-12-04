/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity.converter;

import java.util.ArrayList;
import java.util.List;

import org.dozer.DozerConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.EnumEntityFactory;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDDocStatoEnum;
import it.csi.siac.siaccommonser.util.log.LogSrvUtil;
import it.csi.siac.siacfin2ser.model.StatoOperativoDocumento;
import it.csi.siac.siacfinser.integration.dao.ordinativo.SiacTDocFinRepository;
import it.csi.siac.siacfinser.integration.entity.SiacDDocStatoFin;
import it.csi.siac.siacfinser.integration.entity.SiacRDocStatoFin;
import it.csi.siac.siacfinser.integration.entity.SiacTDocFin;

// TODO: Auto-generated Javadoc
/**
 * The Class DocumentoSpesaStatoConverterFin.
 */
@Component
public class DocumentoSpesaStatoFinConverter extends DozerConverter<StatoOperativoDocumento, SiacTDocFin > {
	
	/** The log. */
	private LogSrvUtil log = new LogSrvUtil(this.getClass());
	
	/** The eef. */
	@Autowired
	private EnumEntityFactory eef;
	
	@Autowired
	private SiacTDocFinRepository siacTDocFinRepository;

	/**
	 * Instantiates a new documento spesa stato converter.
	 */
	public DocumentoSpesaStatoFinConverter() {
		super(StatoOperativoDocumento.class, SiacTDocFin.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public StatoOperativoDocumento convertFrom(SiacTDocFin src, StatoOperativoDocumento dest) {
		
		List<SiacRDocStatoFin> siacRDocStatos = new ArrayList<SiacRDocStatoFin>();
		if(src.getSiacRDocStatos() != null){
			siacRDocStatos = src.getSiacRDocStatos();
		}else{
			siacRDocStatos = siacTDocFinRepository.findSiacRDocStatos(src.getUid());
		}
		
		for (SiacRDocStatoFin siacRDocStato : siacRDocStatos) {
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
	public SiacTDocFin convertTo(StatoOperativoDocumento src, SiacTDocFin dest) {
		final String methodName = "convertTo";
		
		if(dest== null) {
			return dest;
		}
		
		List<SiacRDocStatoFin> siacRDocStatos = new ArrayList<SiacRDocStatoFin>();
		SiacRDocStatoFin siacRDocStato = new SiacRDocStatoFin();
	
		SiacDDocStatoEnum variazioneStato =  SiacDDocStatoEnum.byStatoOperativo(src);
		SiacDDocStatoFin siacDDocStato = eef.getEntity(variazioneStato, dest.getSiacTEnteProprietario().getUid(), SiacDDocStatoFin.class); 
				
		log .debug(methodName, "setting siacDDocStatofin to: "+siacDDocStato.getDocStatoCode()+ " ["+siacDDocStato.getUid()+"]");
		siacRDocStato.setSiacDDocStato(siacDDocStato);
		siacRDocStato.setSiacTDoc(dest);		
		
		siacRDocStato.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
		siacRDocStato.setLoginOperazione(dest.getLoginOperazione());
		
		
		siacRDocStatos.add(siacRDocStato);
		dest.setSiacRDocStatos(siacRDocStatos);
		
		return dest;
	}



	

}
