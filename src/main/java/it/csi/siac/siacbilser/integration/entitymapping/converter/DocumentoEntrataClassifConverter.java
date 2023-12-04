/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.ArrayList;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacRDocClass;
import it.csi.siac.siacbilser.integration.entity.SiacTDoc;
import it.csi.siac.siacfin2ser.model.DocumentoEntrata;

/**
 * The Class DocumentoEntrataClassifConverter.
 */
@Component
public class DocumentoEntrataClassifConverter extends ExtendedDozerConverter<DocumentoEntrata, SiacTDoc> {
	
	
	/**
	 * Instantiates a new documento entrata classif converter.
	 */
	public DocumentoEntrataClassifConverter() {
		super(DocumentoEntrata.class, SiacTDoc.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public DocumentoEntrata convertFrom(SiacTDoc src, DocumentoEntrata dest) {
		
		
		for(SiacRDocClass siacRDocClass : src.getSiacRDocClasses()){
			if(siacRDocClass.getDataCancellazione()!=null){
				continue;
			}
			
			//il doc di entrata non ha classificatori
				
		}	
		
		return dest;
	}
	

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacTDoc convertTo(DocumentoEntrata src, SiacTDoc dest) {
		
		dest.setSiacRDocClasses(new ArrayList<SiacRDocClass>());
		
		//addClassif(dest, src.getTipoImpresa());
		
		return dest;
	}

//	private void addClassif(SiacTDoc dest, Codifica src) {
//		if(src==null) { //facoltativo
//			return;
//		}
//		SiacRDocClass siacRDocClass = new SiacRDocClass();
//		siacRDocClass.setSiacTDoc(dest);
//		SiacTClass siacTClass = new SiacTClass();
//		siacTClass.setUid(src.getUid());	
//		siacRDocClass.setSiacTClass(siacTClass);
//		
//		siacRDocClass.setLoginOperazione(dest.getLoginOperazione());
//		siacRDocClass.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
//		
//		dest.addSiacRDocClass(siacRDocClass);
//	}

}
