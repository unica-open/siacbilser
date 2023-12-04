/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacRDoc;
import it.csi.siac.siacbilser.integration.entity.SiacTDoc;
import it.csi.siac.siacbilser.integration.entity.SiacTSubdoc;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDDocFamTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDRelazTipoEnum;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacfin2ser.model.DocumentoEntrata;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;

/**
 * The Class SubdocumentoSpesaTipoIvaSplitReverseConverter.
 */
@Component
public class SubdocumentoEntrataDocPadreConverter extends ExtendedDozerConverter<SubdocumentoEntrata, SiacTSubdoc> {

	/**
	 * Instantiates a new subdocumento spesa tipo iva split reverse converter.
 	*/
	public SubdocumentoEntrataDocPadreConverter() {
		super(SubdocumentoEntrata.class, SiacTSubdoc.class);
	}

	@Override
	public SubdocumentoEntrata convertFrom(SiacTSubdoc src, SubdocumentoEntrata dest) {
		String methodName = "convertFrom";
		
		if(src.getSiacTDoc() == null || src.getSiacTDoc().getSiacRDocsFiglio() == null || src.getSiacTDoc().getSiacRDocsFiglio().isEmpty()){
			return dest;
		}
		
		DocumentoEntrata docEntrata =  dest.getDocumento() != null? dest.getDocumento() : new DocumentoEntrata();
		List<DocumentoEntrata> listaDocumentiEntrataPadre = new ArrayList<DocumentoEntrata>();
		List<DocumentoSpesa> listaDocumentiSpesaPadre = new ArrayList<DocumentoSpesa>();
		
		for(SiacRDoc r : src.getSiacTDoc().getSiacRDocsFiglio()){
			if(r.getDataCancellazione() == null && SiacDRelazTipoEnum.Subdocumento.getCodice().equals(r.getSiacDRelazTipo().getRelazTipoCode())){
				SiacTDoc siacTDoc = r.getSiacTDocPadre();
				
				log.debug(methodName, "trovato doc padre: " + siacTDoc.getUid());
				if(SiacDDocFamTipoEnum.Spesa.getCodice().equals(siacTDoc.getSiacDDocTipo().getSiacDDocFamTipo().getDocFamTipoCode()) || 
						SiacDDocFamTipoEnum.IvaSpesa.getCodice().equals(siacTDoc.getSiacDDocTipo().getSiacDDocFamTipo().getDocFamTipoCode())){
					DocumentoSpesa docS = map(siacTDoc, DocumentoSpesa.class, BilMapId.SiacTDoc_DocumentoSpesa_Minimal);
					log.debug(methodName, "il doc padre e' di spesa");
					listaDocumentiSpesaPadre.add(docS);
				}else if(SiacDDocFamTipoEnum.Entrata.getCodice().equals(siacTDoc.getSiacDDocTipo().getSiacDDocFamTipo().getDocFamTipoCode()) || 
								SiacDDocFamTipoEnum.IvaEntrata.getCodice().equals(siacTDoc.getSiacDDocTipo().getSiacDDocFamTipo().getDocFamTipoCode())){
					DocumentoEntrata docE = map(siacTDoc, DocumentoEntrata.class, BilMapId.SiacTDoc_DocumentoEntrata_Minimal);
					log.debug(methodName, "il doc padre e' di entrata");
					listaDocumentiEntrataPadre.add(docE);
				}
			}
			
		}
		docEntrata.setListaDocumentiEntrataPadre(listaDocumentiEntrataPadre);
		docEntrata.setListaDocumentiSpesaPadre(listaDocumentiSpesaPadre);
		dest.setDocumento(docEntrata);
		return dest;
	}

	@Override
	public SiacTSubdoc convertTo(SubdocumentoEntrata src, SiacTSubdoc dest) {
		return dest;
	}

}
