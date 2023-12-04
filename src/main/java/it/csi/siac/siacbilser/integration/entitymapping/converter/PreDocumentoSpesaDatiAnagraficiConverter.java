/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.ArrayList;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacTPredoc;
import it.csi.siac.siacbilser.integration.entity.SiacTPredocAnagr;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacfin2ser.model.DatiAnagraficiPreDocumentoSpesa;
import it.csi.siac.siacfin2ser.model.PreDocumentoSpesa;

/**
 * The Class PreDocumentoSpesaDatiAnagraficiConverter.
 */
@Component
public class PreDocumentoSpesaDatiAnagraficiConverter extends ExtendedDozerConverter<PreDocumentoSpesa, SiacTPredoc > {
	
	/**
	 * Instantiates a new pre documento spesa dati anagrafici converter.
	 */
	public PreDocumentoSpesaDatiAnagraficiConverter() {
		super(PreDocumentoSpesa.class, SiacTPredoc.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public PreDocumentoSpesa convertFrom(SiacTPredoc src, PreDocumentoSpesa dest) {
		
		
		for(SiacTPredocAnagr siacTPredocAnagr : src.getSiacTPredocAnagrs()){
			if(siacTPredocAnagr.getDataCancellazione()!=null){
				continue;
			}			
			
			DatiAnagraficiPreDocumentoSpesa dapd = map(siacTPredocAnagr, DatiAnagraficiPreDocumentoSpesa.class , BilMapId.SiacTPredocAnagr_DatiAnagraficiPreDocumentoSpesa);
			
			dest.setDatiAnagraficiPreDocumento(dapd);
						
		}	
		
		return dest;
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacTPredoc convertTo(PreDocumentoSpesa src, SiacTPredoc dest) {
		
		dest.setSiacTPredocAnagrs(new ArrayList<SiacTPredocAnagr>());
		
		if(src.getDatiAnagraficiPreDocumento()==null) { 
			//facoltativo ma non troppo!
			return dest;
		}
		
		SiacTPredocAnagr siacTPredocAnagr = new SiacTPredocAnagr();
		siacTPredocAnagr.setSiacTPredoc(dest);
		
		DatiAnagraficiPreDocumentoSpesa dapd = src.getDatiAnagraficiPreDocumento();
		map(dapd, siacTPredocAnagr, BilMapId.SiacTPredocAnagr_DatiAnagraficiPreDocumentoSpesa);
		
		siacTPredocAnagr.setLoginOperazione(dest.getLoginOperazione());
		siacTPredocAnagr.setLoginModifica(dest.getLoginOperazione());
		
		siacTPredocAnagr.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
		
		dest.addSiacTPredocAnagr(siacTPredocAnagr);
		
				
		return dest;
	}



}
