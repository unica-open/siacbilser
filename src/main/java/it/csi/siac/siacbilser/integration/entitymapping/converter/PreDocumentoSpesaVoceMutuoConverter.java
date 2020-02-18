/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacRMutuoVocePredoc;
import it.csi.siac.siacbilser.integration.entity.SiacTMutuoVoce;
import it.csi.siac.siacbilser.integration.entity.SiacTPredoc;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacfin2ser.model.PreDocumentoSpesa;
import it.csi.siac.siacfinser.model.mutuo.VoceMutuo;

/**
 * The Class PreDocumentoSpesaMutuoConverter.
 */
@Component
public class PreDocumentoSpesaVoceMutuoConverter extends ExtendedDozerConverter<PreDocumentoSpesa, SiacTPredoc> {

	/**
	 * Instantiates a new pre documento spesa sub documento converter.
 	*/
	public PreDocumentoSpesaVoceMutuoConverter() {
		super(PreDocumentoSpesa.class, SiacTPredoc.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public PreDocumentoSpesa convertFrom(SiacTPredoc src, PreDocumentoSpesa dest) {
		
		if(src.getSiacRMutuoVocePredocs() != null) {
			for(SiacRMutuoVocePredoc siacRMutuoVocePredoc : src.getSiacRMutuoVocePredocs()){
				if(siacRMutuoVocePredoc.getDataCancellazione()!=null){
					continue;
				}
				
				SiacTMutuoVoce siacTMutuoVoce = siacRMutuoVocePredoc.getSiacTMutuoVoce();
				
				if(siacTMutuoVoce!=null) {
					
					VoceMutuo voceMutuo = map(siacTMutuoVoce, VoceMutuo.class, BilMapId.SiacTMutuoVoce_VoceMutuo_BIL);
					dest.setVoceMutuo(voceMutuo);
					break;
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
		if(src.getVoceMutuo() != null && src.getVoceMutuo().getUid() != 0) {
			VoceMutuo voceMutuo = src.getVoceMutuo();
			
			SiacTMutuoVoce siacTMutuoVoce = new SiacTMutuoVoce();
			siacTMutuoVoce.setUid(voceMutuo.getUid());
			
			SiacRMutuoVocePredoc siacRMutuoVocePredoc = new SiacRMutuoVocePredoc();
			siacRMutuoVocePredoc.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
			siacRMutuoVocePredoc.setSiacTMutuoVoce(siacTMutuoVoce);
			siacRMutuoVocePredoc.setSiacTPredoc(dest);
			siacRMutuoVocePredoc.setLoginOperazione(dest.getLoginOperazione());
			
			List<SiacRMutuoVocePredoc> siacRMutuoVocePredocs = new ArrayList<SiacRMutuoVocePredoc>();
			siacRMutuoVocePredocs.add(siacRMutuoVocePredoc);
			
			dest.setSiacRMutuoVocePredocs(siacRMutuoVocePredocs);
			
		}
		return dest;
	}

}
