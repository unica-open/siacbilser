/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.ArrayList;

import org.dozer.DozerConverter;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacRPredocSog;
import it.csi.siac.siacbilser.integration.entity.SiacTPredoc;
import it.csi.siac.siacbilser.integration.entity.SiacTSoggetto;
import it.csi.siac.siacfin2ser.model.PreDocumento;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

/**
 * The Class PreDocumentoSoggConverter.
 */
@Component
public class PreDocumentoSoggConverter extends DozerConverter<PreDocumento<?, ?>, SiacTPredoc > {

	/**
	 * Instantiates a new pre documento sogg converter.
	 */
	@SuppressWarnings("unchecked")
	public PreDocumentoSoggConverter() {
		super((Class<PreDocumento<?, ?>>)(Class<?>)PreDocumento.class, SiacTPredoc.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public PreDocumento<?, ?> convertFrom(SiacTPredoc src, PreDocumento<?, ?> dest) {
		
		if(src.getSiacRPredocSogs()!=null) {
			for(SiacRPredocSog siacRPredocSog : src.getSiacRPredocSogs()){
				if(siacRPredocSog.getDataCancellazione()!=null){
					continue;
				}
				
				SiacTSoggetto siacTSoggetto = siacRPredocSog.getSiacTSoggetto();
				
				if(siacTSoggetto!=null) {
				
					// Popolo il soggetto
					Soggetto soggetto = new Soggetto();								
					soggetto.setUid(siacTSoggetto.getUid());	
					soggetto.setCodiceFiscale(siacTSoggetto.getCodiceFiscale());
					soggetto.setCodiceSoggetto(siacTSoggetto.getSoggettoCode());
					soggetto.setDenominazione(siacTSoggetto.getSoggettoDesc());
					soggetto.setPartitaIva(siacTSoggetto.getPartitaIva());
					
					
					dest.setSoggetto(soggetto);
				
				}
							
			}
		}
		
		return dest;
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacTPredoc convertTo(PreDocumento<?, ?> src, SiacTPredoc dest) {
		
		dest.setSiacRPredocSogs(new ArrayList<SiacRPredocSog>());
		
		if(src.getSoggetto()==null || src.getSoggetto().getUid() == 0) {
			//facoltativo
			return dest;
		}
		
		SiacRPredocSog siacRPredocSog = new SiacRPredocSog();
		siacRPredocSog.setSiacTPredoc(dest);
		
		SiacTSoggetto siacTSoggetto = new SiacTSoggetto();
		siacTSoggetto.setUid(src.getSoggetto().getUid());	
		siacRPredocSog.setSiacTSoggetto(siacTSoggetto);
		
		siacRPredocSog.setLoginOperazione(dest.getLoginOperazione());
		siacRPredocSog.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
		
		dest.addSiacRPredocSog(siacRPredocSog);
		
		return dest;
	}



	

}
