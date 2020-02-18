/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacRSubdocIva;
import it.csi.siac.siacbilser.integration.entity.SiacTSubdocIva;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDRelazTipoEnum;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacfin2ser.model.SubdocumentoIvaEntrata;
import it.csi.siac.siacfin2ser.model.SubdocumentoIvaSpesa;


 
@Component
public class SubdocumentoIvaSpesaControregistrazioneConverter extends ExtendedDozerConverter<SubdocumentoIvaSpesa, SiacTSubdocIva > {
	
	/**
	 * Instantiates a new subdocumento iva subdocumenti iva collegati converter.
	 */
	public SubdocumentoIvaSpesaControregistrazioneConverter() {
		super(SubdocumentoIvaSpesa.class, SiacTSubdocIva.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SubdocumentoIvaSpesa convertFrom(SiacTSubdocIva siacTSubdocIva, SubdocumentoIvaSpesa dest) {
		final String methodName = "convertFrom";
		
		log.debug(methodName, "dest.uid: "+ dest.getUid());
		
		if(siacTSubdocIva.getSiacRSubdocIvasFiglio()!=null) {
			for(SiacRSubdocIva r: siacTSubdocIva.getSiacRSubdocIvasFiglio()) {			
				if(r.getDataCancellazione()==null) {
					
					SiacTSubdocIva siacTSubdocIvaPadre = r.getSiacTSubdocIvaPadre();						
					SubdocumentoIvaSpesa subdocIva = mapNotNull(siacTSubdocIvaPadre, SubdocumentoIvaSpesa.class, BilMapId.SiacTSubdocIva_SubdocumentoIvaSpesa_Base);
					dest.setSubdocumentoIvaPadre(subdocIva);					
					
					dest.setTipoRelazione(SiacDRelazTipoEnum.byCodice(r.getSiacDRelazTipo().getRelazTipoCode()).getTipoRelazione());	
					
					break;
					
				}
				
			}
		}
		
		
		
		if(siacTSubdocIva.getSiacRSubdocIvasPadre()!=null) {
			for(SiacRSubdocIva r: siacTSubdocIva.getSiacRSubdocIvasPadre()) {			
				if(r.getDataCancellazione()==null && SiacDRelazTipoEnum.ControregistrazioneIntrastat.getCodice().equals(r.getSiacDRelazTipo().getRelazTipoCode())) {
						SiacTSubdocIva siacTSubdocIvaFiglio = r.getSiacTSubdocIvaFiglio();						
						SubdocumentoIvaEntrata controregistrazione = mapNotNull(siacTSubdocIvaFiglio, SubdocumentoIvaEntrata.class, BilMapId.SiacTSubdocIva_SubdocumentoIvaEntrata_Minimal);						
						dest.setSubdocumentoIvaEntrata(controregistrazione);					
				}
				
			}
		}
		
		
		log.debug(methodName, "fine");
		
		return dest;
	}



	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacTSubdocIva convertTo(SubdocumentoIvaSpesa subdoc, SiacTSubdocIva dest) {	
				
//		dest.setSiacRSubdocIvasPadre(new ArrayList<SiacRSubdocIva>());
//		dest.setSiacRSubdocIvasFiglio(new ArrayList<SiacRSubdocIva>());
//		
//		if(subdoc.getSubdocumentoIvaPadre() != null && subdoc.getTipoRelazione()!=null) {			
//			addPadre(dest, subdoc.getSubdocumentoIvaPadre(), subdoc.getTipoRelazione());
//		}
//		
//		for(SubdocumentoIva<?, ?> s : subdoc.getListaNoteDiCredito()){
//			addFiglio(dest, s, TipoRelazione.NOTA_CREDITO_IVA);
//		}
//		
//		for(SubdocumentoIva<?, ?> s : subdoc.getListaQuoteIvaDifferita()){
//			addFiglio(dest, s, TipoRelazione.QUOTE_PER_IVA_DIFFERITA);
//		}
		
		return dest;	
	}
	

}
