/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacRDocIva;
import it.csi.siac.siacbilser.integration.entity.SiacTDoc;
import it.csi.siac.siacbilser.integration.entity.SiacTSubdocIva;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDDocFamTipoEnum;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacfin2ser.model.DocumentoEntrata;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.SubdocumentoIva;


/**
 * The Class SubdocumentoIvaDocumentoConverter.
 */
@Component
public class SubdocumentoIvaDocumentoConverter extends ExtendedDozerConverter<SubdocumentoIva, SiacTSubdocIva > {
	
	/**
	 * Instantiates a new subdocumento iva documento converter.
	 */
	public SubdocumentoIvaDocumentoConverter() {
		super(SubdocumentoIva.class, SiacTSubdocIva.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SubdocumentoIva convertFrom(SiacTSubdocIva siacTSubdocIva, SubdocumentoIva dest) {
		final String methodName = "convertFrom";
		
		log.debug(methodName, "dest.uid: "+ dest.getUid());
		
		
		SiacRDocIva siacRDocIva = siacTSubdocIva.getSiacRDocIva();
		if(siacRDocIva==null || siacRDocIva.getDataCancellazione()!=null){
			return dest;
		}
		
		SiacTDoc siacTDoc = siacRDocIva.getSiacTDoc();
		
		
		String famTipoCode = siacTDoc.getSiacDDocTipo().getSiacDDocFamTipo().getDocFamTipoCode();
		if(SiacDDocFamTipoEnum.Spesa.getCodice().equals(famTipoCode) || SiacDDocFamTipoEnum.IvaSpesa.getCodice().equals(famTipoCode)){
			DocumentoSpesa doc = mapNotNull(siacTDoc, DocumentoSpesa.class, BilMapId.SiacTDoc_DocumentoSpesa_Base);
			dest.setDocumento(doc);
		} else if(SiacDDocFamTipoEnum.Entrata.getCodice().equals(famTipoCode) || SiacDDocFamTipoEnum.IvaEntrata.getCodice().equals(famTipoCode)){
			DocumentoEntrata doc = mapNotNull(siacTDoc, DocumentoEntrata.class, BilMapId.SiacTDoc_DocumentoEntrata_Base);
			dest.setDocumento(doc);
		} else {
			log.warn(methodName, "tipo documento con codice tipo famiglia " + famTipoCode + " non supportato");
		}		
		
		log.debug(methodName, "fine");
		
		return dest;
	}



	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacTSubdocIva convertTo(SubdocumentoIva subdoc, SiacTSubdocIva dest) {	
					
		if(subdoc.getDocumento()!=null && subdoc.getDocumento().getUid()!=0) {
			
			SiacRDocIva siacRDocIva = new SiacRDocIva();
			SiacTDoc siacTDoc = new SiacTDoc();
			siacTDoc.setUid(subdoc.getDocumento().getUid());
			siacRDocIva.setSiacTDoc(siacTDoc);
			
			siacRDocIva.setLoginOperazione(dest.getLoginOperazione());
			siacRDocIva.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
			
			dest.setSiacRDocIva(siacRDocIva);		
		}		
		
		return dest;	
	}

}
	

	
	
	



	


