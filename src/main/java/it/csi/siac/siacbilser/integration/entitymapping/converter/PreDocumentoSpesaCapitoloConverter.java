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
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacfin2ser.model.PreDocumentoSpesa;

/**
 * The Class PreDocumentoSpesaCapitoloConverter.
 */
@Component
public class PreDocumentoSpesaCapitoloConverter extends ExtendedDozerConverter<PreDocumentoSpesa, SiacTPredoc > {
	
	/**
	 * Instantiates a new pre documento spesa capitolo converter.
	 */
	public PreDocumentoSpesaCapitoloConverter() {
		super(PreDocumentoSpesa.class, SiacTPredoc.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public PreDocumentoSpesa convertFrom(SiacTPredoc src, PreDocumentoSpesa dest) {
		
		
		for(SiacRPredocBilElem siacRPredocBilElem : src.getSiacRPredocBilElems()){
			if(siacRPredocBilElem.getDataCancellazione()!=null){
				continue;
			}
			
			SiacTBilElem siacTBilElem = siacRPredocBilElem.getSiacTBilElem();
			
			CapitoloUscitaGestione capitolo = new CapitoloUscitaGestione();
				
			mapNotNull(siacTBilElem,capitolo, BilMapId.SiacTBilElem_Capitolo_Base);		
			
			dest.setCapitoloUscitaGestione(capitolo);			
			
						
		}	
		
		return dest;
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacTPredoc convertTo(PreDocumentoSpesa src, SiacTPredoc dest) {
		
		dest.setSiacRPredocBilElems(new ArrayList<SiacRPredocBilElem>());
		
		if(src.getCapitoloUscitaGestione()==null || src.getCapitoloUscitaGestione().getUid() == 0) { //facoltativo
			return dest;
		}
		
		SiacRPredocBilElem siacRPredocBilElem = new SiacRPredocBilElem();
		siacRPredocBilElem.setSiacTPredoc(dest);
		
		SiacTBilElem siacTBilElem = new SiacTBilElem();
		siacTBilElem.setUid(src.getCapitoloUscitaGestione().getUid());	
		siacRPredocBilElem.setSiacTBilElem(siacTBilElem);
		
		siacRPredocBilElem.setLoginOperazione(dest.getLoginOperazione());
		siacRPredocBilElem.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
		
		dest.addSiacRPredocBilElem(siacRPredocBilElem);
		
				
		return dest;
	}



	

}
