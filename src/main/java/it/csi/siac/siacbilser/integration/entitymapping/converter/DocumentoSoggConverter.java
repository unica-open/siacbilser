/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.SiacTDocRepository;
import it.csi.siac.siacbilser.integration.dao.SiacTSoggettoRepository;
import it.csi.siac.siacbilser.integration.entity.SiacRDocSog;
import it.csi.siac.siacbilser.integration.entity.SiacTDoc;
import it.csi.siac.siacbilser.integration.entity.SiacTSoggetto;
import it.csi.siac.siacfin2ser.model.Documento;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

/**
 * The Class DocumentoSoggConverter.
 */
@Component
public class DocumentoSoggConverter extends ExtendedDozerConverter<Documento<?, ?>, SiacTDoc> {
	
	@Autowired
	private SiacTSoggettoRepository siacTSoggettoRepository;
	@Autowired
	private SiacTDocRepository siacTDocRepository;
	
	/**
	 * Instantiates a new documento entrata sogg converter.
	 */
	@SuppressWarnings("unchecked")
	public DocumentoSoggConverter() {
		super((Class<Documento<?, ?>>)(Class<?>)Documento.class, SiacTDoc.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public Documento<?, ?> convertFrom(SiacTDoc src, Documento<?, ?> dest) {
		
		src = siacTDocRepository.findOne(src.getUid());
		
		if(src.getSiacRDocSogs()!=null){
			for(SiacRDocSog siacRDocSog : src.getSiacRDocSogs()){
				if(siacRDocSog.getDataCancellazione()==null) {				
				
					SiacTSoggetto siacTSoggetto = siacTSoggettoRepository.findOne(siacRDocSog.getSiacTSoggetto().getUid());
					
					
					Soggetto soggetto = new Soggetto();
					soggetto.setUid(siacTSoggetto.getUid());
					soggetto.setCodiceFiscale(siacTSoggetto.getCodiceFiscale());
					soggetto.setCodiceSoggetto(siacTSoggetto.getSoggettoCode());
					soggetto.setDenominazione(siacTSoggetto.getSoggettoDesc());
					soggetto.setPartitaIva(siacTSoggetto.getPartitaIva());
					//SIAC-6261
					soggetto.setDataFineValiditaDurc(siacTSoggetto.getDataFineValiditaDurc());
					
					
					dest.setSoggetto(soggetto);
					return dest;
				}
							
			}	
		}
		
		return dest;
	}
	

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacTDoc convertTo(Documento<?, ?> src, SiacTDoc dest) {
		
		if(src.getSoggetto()!=null){
			dest.setSiacRDocSogs(new ArrayList<SiacRDocSog>());
					
			SiacRDocSog siacRDocSog = new SiacRDocSog();
			siacRDocSog.setSiacTDoc(dest);
			SiacTSoggetto siacTSoggetto = new SiacTSoggetto();
			siacTSoggetto.setUid(src.getSoggetto().getUid());	
			siacRDocSog.setSiacTSoggetto(siacTSoggetto);
			
			siacRDocSog.setLoginOperazione(dest.getLoginOperazione());
			siacRDocSog.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
			
			dest.addSiacRDocSog(siacRDocSog);
		}
		
		return dest;
	}

//	/**
//	 * Adds the sogg.
//	 *
//	 * @param dest the dest
//	 * @param src the src
//	 */
//	private void addSogg(SiacTDoc dest, Soggetto src) {
//		
//	}

}
