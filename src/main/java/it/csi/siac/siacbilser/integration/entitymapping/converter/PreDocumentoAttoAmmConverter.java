/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.ArrayList;

import org.springframework.stereotype.Component;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacbilser.integration.entity.SiacRPredocAttoAmm;
import it.csi.siac.siacbilser.integration.entity.SiacTAttoAmm;
import it.csi.siac.siacbilser.integration.entity.SiacTPredoc;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacfin2ser.model.PreDocumento;

/**
 * The Class PreDocumentoAttoAmmConverter.
 */
@Component
public class PreDocumentoAttoAmmConverter extends ExtendedDozerConverter<PreDocumento<?, ?>, SiacTPredoc > {
	
	/**
	 * Instantiates a new pre documento atto amm converter.
	 */
	@SuppressWarnings("unchecked")
	public PreDocumentoAttoAmmConverter() {
		super((Class<PreDocumento<?, ?>>)(Class<?>)PreDocumento.class, SiacTPredoc.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public PreDocumento<?, ?> convertFrom(SiacTPredoc src, PreDocumento<?, ?> dest) {
		
		if(src.getSiacRPredocAttoAmms()!=null) {
			for(SiacRPredocAttoAmm siacRPredocAttoAmm : src.getSiacRPredocAttoAmms()){
				if(siacRPredocAttoAmm.getDataCancellazione()!=null){
					continue;
				}
				
				SiacTAttoAmm siacTAttoAmm = siacRPredocAttoAmm.getSiacTAttoAmm();
				
				if(siacTAttoAmm!=null) {
					AttoAmministrativo attoAmm = map(siacTAttoAmm, AttoAmministrativo.class, BilMapId.SiacTAttoAmm_AttoAmministrativo);
					dest.setAttoAmministrativo(attoAmm);
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
		
		dest.setSiacRPredocAttoAmms(new ArrayList<SiacRPredocAttoAmm>());
		
		if(src.getAttoAmministrativo()==null || src.getAttoAmministrativo().getUid() == 0) {
			//facoltativo
			return dest;
		}
		
		SiacRPredocAttoAmm siacRPredocAttoAmm = new SiacRPredocAttoAmm();
		siacRPredocAttoAmm.setSiacTPredoc(dest);
		SiacTAttoAmm siacTAttoAmm = new SiacTAttoAmm();
		siacTAttoAmm.setUid(src.getAttoAmministrativo().getUid());	
		siacRPredocAttoAmm.setSiacTAttoAmm(siacTAttoAmm);
		
		siacRPredocAttoAmm.setLoginOperazione(dest.getLoginOperazione());
		siacRPredocAttoAmm.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
		
		dest.addSiacRPredocAttoAmm(siacRPredocAttoAmm);
		
				
		return dest;
	}



	

}
