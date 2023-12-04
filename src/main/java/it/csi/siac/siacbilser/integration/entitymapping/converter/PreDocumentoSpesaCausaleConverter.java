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
import it.csi.siac.siacfin2ser.model.CausaleSpesa;
import it.csi.siac.siacfin2ser.model.PreDocumentoSpesa;

// TODO: Auto-generated Javadoc
/**
 * The Class PreDocumentoSpesaCausaleConverter.
 */
@Component
public class PreDocumentoSpesaCausaleConverter extends ExtendedDozerConverter<PreDocumentoSpesa, SiacTPredoc > {
	

/**
 * Instantiates a new pre documento spesa causale converter.
 */
public PreDocumentoSpesaCausaleConverter() {
		super(PreDocumentoSpesa.class, SiacTPredoc.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public PreDocumentoSpesa convertFrom(SiacTPredoc src, PreDocumentoSpesa dest) {
		
		if(src.getSiacRPredocCausales()!=null) {
			for(SiacRPredocCausale siacRPredocCausale : src.getSiacRPredocCausales()){
				if(siacRPredocCausale.getDataCancellazione()!=null){
					continue;
				}
				
				SiacDCausale siacDCausale = siacRPredocCausale.getSiacDCausale();
				
				if(siacDCausale!=null) {					
					CausaleSpesa causale = map(siacDCausale, CausaleSpesa.class, BilMapId.SiacDCausale_CausaleSpesa_Base);					
					dest.setCausaleSpesa(causale);
				}				
							
			}	
		}
		
		return dest;
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacTPredoc convertTo(PreDocumentoSpesa src, SiacTPredoc dest) {
		
		dest.setSiacRPredocCausales(new ArrayList<SiacRPredocCausale>());
		
		if(src.getCausaleSpesa()==null || src.getCausaleSpesa().getUid() == 0) { //facoltativo
			return dest;
		}
		
		SiacRPredocCausale siacRPredocCausale = new SiacRPredocCausale();
		siacRPredocCausale.setSiacTPredoc(dest);
		
		SiacDCausale siacDCausale = new SiacDCausale();
		siacDCausale.setUid(src.getCausaleSpesa().getUid());	
		siacRPredocCausale.setSiacDCausale(siacDCausale);
		
		siacRPredocCausale.setLoginOperazione(dest.getLoginOperazione());
		siacRPredocCausale.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
		
		dest.addSiacRPredocCausale(siacRPredocCausale);
		
				
		return dest;
	}



	

}
