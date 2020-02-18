/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacRIvamov;
import it.csi.siac.siacbilser.integration.entity.SiacTIvamov;
import it.csi.siac.siacbilser.integration.entity.SiacTSubdocIva;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacfin2ser.model.AliquotaSubdocumentoIva;
import it.csi.siac.siacfin2ser.model.SubdocumentoIva;


/**
 * The Class SubdocumentoIvaRegistroIvaConverter.
 */
@Component
public class SubdocumentoIvaAliquoteConverter extends ExtendedDozerConverter<SubdocumentoIva<?, ?, ?>, SiacTSubdocIva > {
	
	/**
	 * Instantiates a new subdocumento iva registro iva converter.
	 */
	@SuppressWarnings("unchecked")
	public SubdocumentoIvaAliquoteConverter() {
		super((Class<SubdocumentoIva<?, ?, ?>>)(Class<?>)SubdocumentoIva.class, SiacTSubdocIva.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SubdocumentoIva<?, ?, ?> convertFrom(SiacTSubdocIva siacTSubdocIva, SubdocumentoIva<?, ?, ?> dest) {
		final String methodName = "convertFrom";
		
		log.debug(methodName, "dest.uid: "+ dest.getUid());
		
		List<AliquotaSubdocumentoIva> list = new ArrayList<AliquotaSubdocumentoIva>();
		
		if(siacTSubdocIva.getSiacRIvamovs()!=null) {
			for(SiacRIvamov r : siacTSubdocIva.getSiacRIvamovs()){
				if(r.getDataCancellazione()!=null){
					continue;
				}
				
				SiacTIvamov siacTIvamov = r.getSiacTIvamov();
				
				AliquotaSubdocumentoIva asi = mapNotNull(siacTIvamov, AliquotaSubdocumentoIva.class, BilMapId.SiacTIvamov_AliquotaSubdocumentoIva);				
				list.add(asi);				
			}			
		}
		
		dest.setListaAliquotaSubdocumentoIva(list);
		dest.calcolaTotaleMovimentiIva();
		return dest;
	}



	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacTSubdocIva convertTo(SubdocumentoIva<?, ?, ?> subdoc, SiacTSubdocIva dest) {	
		
		if(subdoc.getListaAliquotaSubdocumentoIva()!=null) {		
			
			dest.setSiacRIvamovs(new ArrayList<SiacRIvamov>());
			
			for(AliquotaSubdocumentoIva asi : subdoc.getListaAliquotaSubdocumentoIva()) {				
				
				
				SiacTIvamov siacTIvamov = new SiacTIvamov();				
				map(asi, siacTIvamov, BilMapId.SiacTIvamov_AliquotaSubdocumentoIva);				
				siacTIvamov.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
				siacTIvamov.setLoginOperazione(dest.getLoginOperazione());
				
				SiacRIvamov siacRIvamov = new SiacRIvamov();
				siacRIvamov.setSiacTIvamov(siacTIvamov);
				siacRIvamov.setSiacTSubdocIva(dest);
				siacRIvamov.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
				siacRIvamov.setLoginOperazione(dest.getLoginOperazione());
				
				dest.addSiacRIvamov(siacRIvamov);
			}
				
		}		
		
		return dest;	
	}

}
	

	
	
	



	


