/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacRMutuoVoceSubdoc;
import it.csi.siac.siacbilser.integration.entity.SiacTMutuoVoce;
import it.csi.siac.siacbilser.integration.entity.SiacTSubdoc;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfinser.model.mutuo.VoceMutuo;

/**
 * The Class SubdocumentoSpesaMutuoConverter.
 */
@Component
public class SubdocumentoSpesaVoceMutuoConverter extends ExtendedDozerConverter<SubdocumentoSpesa, SiacTSubdoc> {

	/**
	 * Instantiates a new pre documento spesa sub documento converter.
 	*/
	public SubdocumentoSpesaVoceMutuoConverter() {
		super(SubdocumentoSpesa.class, SiacTSubdoc.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SubdocumentoSpesa convertFrom(SiacTSubdoc src, SubdocumentoSpesa dest) {
		
		if(src.getSiacRMutuoVoceSubdocs() != null) {
			for(SiacRMutuoVoceSubdoc siacRMutuoVoceSubdoc : src.getSiacRMutuoVoceSubdocs()){
				if(siacRMutuoVoceSubdoc.getDataCancellazione()!=null){
					continue;
				}
				
				SiacTMutuoVoce siacTMutuoVoce = siacRMutuoVoceSubdoc.getSiacTMutuoVoce();
				
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
	public SiacTSubdoc convertTo(SubdocumentoSpesa src, SiacTSubdoc dest) {
		if(src.getVoceMutuo() != null && src.getVoceMutuo().getUid() != 0) {
			VoceMutuo voceMutuo = src.getVoceMutuo();
			
			SiacTMutuoVoce siacTMutuoVoce = new SiacTMutuoVoce();
			siacTMutuoVoce.setUid(voceMutuo.getUid());
			
			SiacRMutuoVoceSubdoc siacRMutuoVoceSubdoc = new SiacRMutuoVoceSubdoc();
			siacRMutuoVoceSubdoc.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
			siacRMutuoVoceSubdoc.setSiacTMutuoVoce(siacTMutuoVoce);
			siacRMutuoVoceSubdoc.setSiacTSubdoc(dest);
			siacRMutuoVoceSubdoc.setLoginOperazione(dest.getLoginOperazione());
			
			List<SiacRMutuoVoceSubdoc> siacRMutuoVoceSubdocs = new ArrayList<SiacRMutuoVoceSubdoc>();
			siacRMutuoVoceSubdocs.add(siacRMutuoVoceSubdoc);
			
			dest.setSiacRMutuoVoceSubdocs(siacRMutuoVoceSubdocs);
			
		}
		return dest;
	}

}
