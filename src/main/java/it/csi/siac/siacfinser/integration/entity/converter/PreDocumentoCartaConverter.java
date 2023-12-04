/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity.converter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siaccommonser.util.log.LogSrvUtil;
import it.csi.siac.siacfinser.integration.dao.EnumEntityFinFactory;
import it.csi.siac.siacfinser.integration.entity.SiacRCartacontDetSubdocFin;
import it.csi.siac.siacfinser.integration.entity.SiacTCartacontDetFin;
import it.csi.siac.siacfinser.model.carta.PreDocumentoCarta;

@Component
public class PreDocumentoCartaConverter extends FinExtendedDozerConverter<PreDocumentoCarta, SiacTCartacontDetFin> { 


	private LogSrvUtil log = new LogSrvUtil(this.getClass());
	
	@Autowired
	private EnumEntityFinFactory eef;
	
	public PreDocumentoCartaConverter() {
		super(PreDocumentoCarta.class, SiacTCartacontDetFin.class);
		
	}

	@Override
	public PreDocumentoCarta convertFrom(SiacTCartacontDetFin src,PreDocumentoCarta dest) {

		List<SiacRCartacontDetSubdocFin> elencoSiacRCartacontDetSubdoc = src.getSiacRCartacontDetSubdocs();
		List<Integer> listaNumSubdoc = new ArrayList<Integer>();
		BigDecimal totaleDaRegolarizzare = BigDecimal.ZERO;
		BigDecimal importoDaRegolarizzare = BigDecimal.ZERO;
		if(elencoSiacRCartacontDetSubdoc!=null && elencoSiacRCartacontDetSubdoc.size()>0){
			for(SiacRCartacontDetSubdocFin siacRCartacontDetSubdoc : elencoSiacRCartacontDetSubdoc){
				if(siacRCartacontDetSubdoc!=null && siacRCartacontDetSubdoc.getDataFineValidita()==null){
					// listaNumSubdoc.add(siacRCartacontDetSubdoc.getSiacTSubdoc().getSubdocNumero());
					listaNumSubdoc.add(siacRCartacontDetSubdoc.getSiacTSubdoc().getSubdocId());
					totaleDaRegolarizzare = totaleDaRegolarizzare.add(siacRCartacontDetSubdoc.getSiacTSubdoc().getSubdocImporto());
				}
			}
			
			dest.setListaIdSubDocumentiCollegati(listaNumSubdoc);
		}

		importoDaRegolarizzare = dest.getImporto().subtract(totaleDaRegolarizzare);
		dest.setImportoDaRegolarizzare(importoDaRegolarizzare);
		
		
		return dest;
	}

	@Override
	public SiacTCartacontDetFin convertTo(PreDocumentoCarta src, SiacTCartacontDetFin dest) {

		
		return null;
	}
	
	
}
