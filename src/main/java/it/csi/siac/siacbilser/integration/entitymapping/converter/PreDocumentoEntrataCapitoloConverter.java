/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.ArrayList;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacRPredocBilElem;
import it.csi.siac.siacbilser.integration.entity.SiacTBilElem;
import it.csi.siac.siacbilser.integration.entity.SiacTPredoc;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacfin2ser.model.PreDocumentoEntrata;

/**
 * The Class PreDocumentoEntrataCapitoloConverter.
 */
@Component
public class PreDocumentoEntrataCapitoloConverter extends ExtendedDozerConverter<PreDocumentoEntrata, SiacTPredoc > {

	/**
	 * Instantiates a new pre documento entrata capitolo converter.
	 */
	public PreDocumentoEntrataCapitoloConverter() {
		super(PreDocumentoEntrata.class, SiacTPredoc.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public PreDocumentoEntrata convertFrom(SiacTPredoc src, PreDocumentoEntrata dest) {
		
		if(src.getSiacRPredocBilElems()!=null) {
			for(SiacRPredocBilElem siacRPredocBilElem : src.getSiacRPredocBilElems()){
				if(siacRPredocBilElem.getDataCancellazione()!=null){
					continue;
				}
				
				SiacTBilElem siacTBilElem = siacRPredocBilElem.getSiacTBilElem();
				
				CapitoloEntrataGestione capitolo = new CapitoloEntrataGestione();
				
				mapNotNull(siacTBilElem,capitolo, BilMapId.SiacTBilElem_Capitolo_Base);		
				
				dest.setCapitoloEntrataGestione(capitolo);			
				
							
			}
		}
		
		
		return dest;
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacTPredoc convertTo(PreDocumentoEntrata src, SiacTPredoc dest) {
		
		dest.setSiacRPredocBilElems(new ArrayList<SiacRPredocBilElem>());
		
		if(src.getCapitoloEntrataGestione()==null || src.getCapitoloEntrataGestione().getUid() == 0) { 
			//facoltativo
			return dest;
		}
		
		SiacRPredocBilElem siacRPredocBilElem = new SiacRPredocBilElem();
		siacRPredocBilElem.setSiacTPredoc(dest);
		
		SiacTBilElem siacTBilElem = new SiacTBilElem();
		siacTBilElem.setUid(src.getCapitoloEntrataGestione().getUid());	
		siacRPredocBilElem.setSiacTBilElem(siacTBilElem);
		
		siacRPredocBilElem.setLoginOperazione(dest.getLoginOperazione());
		siacRPredocBilElem.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
		
		dest.addSiacRPredocBilElem(siacRPredocBilElem);
		
				
		return dest;
	}



	

}
