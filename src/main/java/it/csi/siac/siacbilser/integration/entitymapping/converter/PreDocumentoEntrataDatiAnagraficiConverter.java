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
import it.csi.siac.siacfin2ser.model.DatiAnagraficiPreDocumento;
import it.csi.siac.siacfin2ser.model.PreDocumentoEntrata;

// TODO: Auto-generated Javadoc
/**
 * The Class PreDocumentoEntrataDatiAnagraficiConverter.
 */
@Component
public class PreDocumentoEntrataDatiAnagraficiConverter extends ExtendedDozerConverter<PreDocumentoEntrata, SiacTPredoc > {
	
	/**
	 * Instantiates a new pre documento entrata dati anagrafici converter.
	 */
	public PreDocumentoEntrataDatiAnagraficiConverter() {
		super(PreDocumentoEntrata.class, SiacTPredoc.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public PreDocumentoEntrata convertFrom(SiacTPredoc src, PreDocumentoEntrata dest) {
		
		
		for(SiacTPredocAnagr siacTPredocAnagr : src.getSiacTPredocAnagrs()){
			if(siacTPredocAnagr.getDataCancellazione()!=null){
				continue;
			}			
			
			DatiAnagraficiPreDocumento dapd = map(siacTPredocAnagr, DatiAnagraficiPreDocumento.class , BilMapId.SiacTPredocAnagr_DatiAnagraficiPreDocumentoEntrata);
			
			dest.setDatiAnagraficiPreDocumento(dapd);
						
		}	
		
		return dest;
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacTPredoc convertTo(PreDocumentoEntrata src, SiacTPredoc dest) {
		
		dest.setSiacTPredocAnagrs(new ArrayList<SiacTPredocAnagr>());
		
		if(src.getDatiAnagraficiPreDocumento()==null) { //facoltativo ma non troppo!
			return dest;
		}
		
		SiacTPredocAnagr siacTPredocAnagr = new SiacTPredocAnagr();
		siacTPredocAnagr.setSiacTPredoc(dest);
		
		DatiAnagraficiPreDocumento dapd = src.getDatiAnagraficiPreDocumento();
		map(dapd, siacTPredocAnagr, BilMapId.SiacTPredocAnagr_DatiAnagraficiPreDocumentoEntrata);
		
		siacTPredocAnagr.setLoginOperazione(dest.getLoginOperazione());
		siacTPredocAnagr.setLoginModifica(dest.getLoginOperazione());
		
		siacTPredocAnagr.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
		
		dest.addSiacTPredocAnagr(siacTPredocAnagr);
		
				
		return dest;
	}



	

}
