/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.ArrayList;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacDCausale;
import it.csi.siac.siacbilser.integration.entity.SiacRPredocCausale;
import it.csi.siac.siacbilser.integration.entity.SiacTPredoc;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacfin2ser.model.CausaleEntrata;
import it.csi.siac.siacfin2ser.model.PreDocumentoEntrata;

/**
 * The Class PreDocumentoEntrataCausaleConverter.
 */
@Component
public class PreDocumentoEntrataCausaleConverter extends ExtendedDozerConverter<PreDocumentoEntrata, SiacTPredoc > {
	

	/**
 * Instantiates a new pre documento entrata causale converter.
 */
	public PreDocumentoEntrataCausaleConverter() {
		super(PreDocumentoEntrata.class, SiacTPredoc.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public PreDocumentoEntrata convertFrom(SiacTPredoc src, PreDocumentoEntrata dest) {
		
		if(src.getSiacRPredocCausales()!=null) {
			for(SiacRPredocCausale siacRPredocCausale : src.getSiacRPredocCausales()){
				if(siacRPredocCausale.getDataCancellazione()!=null){
					continue;
				}
				
				SiacDCausale siacDCausale = siacRPredocCausale.getSiacDCausale();
				
				if(siacDCausale!=null) {			
					
					CausaleEntrata causale = map(siacDCausale, CausaleEntrata.class, BilMapId.SiacDCausale_CausaleEntrata_Base);					
					dest.setCausaleEntrata(causale);
					
				}				
							
			}	
		}
		
		return dest;
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacTPredoc convertTo(PreDocumentoEntrata src, SiacTPredoc dest) {
		
		dest.setSiacRPredocCausales(new ArrayList<SiacRPredocCausale>());
		
		if(src.getCausaleEntrata()==null || src.getCausaleEntrata().getUid() == 0) {
			//facoltativo
			return dest;
		}
		
		SiacRPredocCausale siacRPredocCausale = new SiacRPredocCausale();
		siacRPredocCausale.setSiacTPredoc(dest);
		
		SiacDCausale siacDCausale = new SiacDCausale();
		siacDCausale.setUid(src.getCausaleEntrata().getUid());	
		siacRPredocCausale.setSiacDCausale(siacDCausale);
		
		siacRPredocCausale.setLoginOperazione(dest.getLoginOperazione());
		siacRPredocCausale.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
		
		dest.addSiacRPredocCausale(siacRPredocCausale);
		
		return dest;
	}



	

}
