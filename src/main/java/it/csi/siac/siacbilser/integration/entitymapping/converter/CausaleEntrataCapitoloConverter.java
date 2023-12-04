/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.ArrayList;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacDCausale;
import it.csi.siac.siacbilser.integration.entity.SiacRCausaleBilElem;
import it.csi.siac.siacbilser.integration.entity.SiacTBilElem;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacfin2ser.model.CausaleEntrata;

/**
 * The Class CausaleEntrataCapitoloConverter.
 */
@Component
public class CausaleEntrataCapitoloConverter extends ExtendedDozerConverter<CausaleEntrata, SiacDCausale > {
	
	/**
	 * Instantiates a new causale entrata capitolo converter.
	 */
	public CausaleEntrataCapitoloConverter() {
		super(CausaleEntrata.class, SiacDCausale.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public CausaleEntrata convertFrom(SiacDCausale src, CausaleEntrata dest) {
		
		if(src.getSiacRCausaleBilElems()!=null){
			for(SiacRCausaleBilElem siacRCausaleBilElem : src.getSiacRCausaleBilElems()){
				if((src.getDateToExtract() == null && siacRCausaleBilElem.getDataCancellazione()!=null )
						|| (src.getDateToExtract() != null && !src.getDateToExtract().equals(siacRCausaleBilElem.getDataInizioValidita()))){
					continue;
				}
				
				SiacTBilElem siacTBilElem = siacRCausaleBilElem.getSiacTBilElem();
				
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
	public SiacDCausale convertTo(CausaleEntrata src, SiacDCausale dest) {
		
		dest.setSiacRCausaleBilElems(new ArrayList<SiacRCausaleBilElem>());
		
		if(src.getCapitoloEntrataGestione()==null || src.getCapitoloEntrataGestione().getUid() == 0) { //facoltativo
			return dest;
		}
		
		SiacRCausaleBilElem siacRCausaleBilElem = new SiacRCausaleBilElem();
		siacRCausaleBilElem.setSiacDCausale(dest);
		
		SiacTBilElem siacTBilElem = new SiacTBilElem();
		siacTBilElem.setUid(src.getCapitoloEntrataGestione().getUid());	
		siacRCausaleBilElem.setSiacTBilElem(siacTBilElem);
		
		siacRCausaleBilElem.setLoginOperazione(dest.getLoginOperazione());
		siacRCausaleBilElem.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
		
		dest.addSiacRCausaleBilElem(siacRCausaleBilElem);
		
				
		return dest;
	}



	

}
