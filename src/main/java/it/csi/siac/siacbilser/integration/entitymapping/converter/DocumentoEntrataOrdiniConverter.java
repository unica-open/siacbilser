/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacRDocOrdine;
import it.csi.siac.siacbilser.integration.entity.SiacTDoc;
import it.csi.siac.siacbilser.integration.entity.SiacTOrdine;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacfin2ser.model.DocumentoEntrata;
import it.csi.siac.siacfin2ser.model.Ordine;

/**
 * The Class DocumentoSpesaOrdiniConverter.
 */
@Component
public class DocumentoEntrataOrdiniConverter extends ExtendedDozerConverter<DocumentoEntrata, SiacTDoc > {

	/**
	 * Instantiates a new documento spesa ordini converter.
	 */
	public DocumentoEntrataOrdiniConverter() {
		super(DocumentoEntrata.class, SiacTDoc.class);
	}

	@Override
	public DocumentoEntrata convertFrom(SiacTDoc src, DocumentoEntrata dest) {
		
		if(src.getSiacRDocOrdines()!=null){
			List<Ordine> ordini = new ArrayList<Ordine>();
			for (SiacRDocOrdine r : src.getSiacRDocOrdines()) {
				if (r.getDataCancellazione() != null 
						|| r.getSiacTOrdine().getDataCancellazione() != null) {
					continue;
				}
				
				Ordine ordine = map(r.getSiacTOrdine(), Ordine.class, BilMapId.SiacTOrdine_Ordine);
				ordini.add(ordine);
			}
			
			dest.setOrdini(ordini);
		}
		return dest;
	}

	@Override
	public SiacTDoc convertTo(DocumentoEntrata src, SiacTDoc dest) {
		if(src.getOrdini() == null || src.getOrdini().isEmpty()){
			return dest;
		}
		List<SiacRDocOrdine> siacRDocOrdines = new ArrayList<SiacRDocOrdine>();
		for(Ordine ordine : src.getOrdini()){
			SiacRDocOrdine siacRDocOrdine = new SiacRDocOrdine();
			SiacTOrdine siacTOrdine = map(ordine, SiacTOrdine.class, BilMapId.SiacTOrdine_Ordine);
			siacRDocOrdine.setSiacTDoc(dest);
			siacRDocOrdine.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
			siacRDocOrdine.setSiacTOrdine(siacTOrdine);
			siacRDocOrdine.setLoginOperazione(src.getLoginOperazione());
			siacRDocOrdines.add(siacRDocOrdine);
		}
		dest.setSiacRDocOrdines(siacRDocOrdines);
		return dest;
	}



	

}
