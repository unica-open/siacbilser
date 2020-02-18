/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.SiacTSoggettoRepository;
import it.csi.siac.siacbilser.integration.entity.SiacRDocSog;
import it.csi.siac.siacbilser.integration.entity.SiacTDoc;
import it.csi.siac.siacbilser.integration.entity.SiacTSoggetto;
import it.csi.siac.siacfin2ser.model.DocumentoEntrata;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

/**
 * The Class DocumentoEntrataSoggConverter.
 */
@Component
public class DocumentoEntrataSoggConverter extends ExtendedDozerConverter<DocumentoEntrata, SiacTDoc> {
	
	@Autowired
	private SiacTSoggettoRepository siacTSoggettoRepository;
	
	/**
	 * Instantiates a new documento entrata sogg converter.
	 */
	public DocumentoEntrataSoggConverter() {
		super(DocumentoEntrata.class, SiacTDoc.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public DocumentoEntrata convertFrom(SiacTDoc src, DocumentoEntrata dest) {
		
		
		for(SiacRDocSog siacRDocSog : src.getSiacRDocSogs()){
			if(siacRDocSog.getDataCancellazione()==null) {				
			
				SiacTSoggetto siacTSoggetto = siacTSoggettoRepository.findOne(siacRDocSog.getSiacTSoggetto().getUid());
				
				Soggetto soggetto = new Soggetto();
				soggetto.setUid(siacTSoggetto.getUid());
				soggetto.setCodiceFiscale(siacTSoggetto.getCodiceFiscale());
				soggetto.setCodiceSoggetto(siacTSoggetto.getSoggettoCode());
				soggetto.setDenominazione(siacTSoggetto.getSoggettoDesc());
				soggetto.setPartitaIva(siacTSoggetto.getPartitaIva());
				
				// SIAC-6565
				soggetto.setCanalePA(siacTSoggetto.getCanalePA());
				soggetto.setEmailPec(siacTSoggetto.getEmailPec());
				soggetto.setCodDestinatario(siacTSoggetto.getCodDestinatario());
				
				dest.setSoggetto(soggetto);
				return dest;
			}
						
		}	
		
		return dest;
	}
	

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacTDoc convertTo(DocumentoEntrata src, SiacTDoc dest) {
		
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

}
