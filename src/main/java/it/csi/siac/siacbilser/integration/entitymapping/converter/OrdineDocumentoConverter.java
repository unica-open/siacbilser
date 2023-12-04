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
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.Ordine;

/**
 * The Class OrdineDocumentoConverter.
 */
@Component
public class OrdineDocumentoConverter extends ExtendedDozerConverter<Ordine, SiacTOrdine> {
	
	/**
	 * Instantiates a new documento entrata sogg converter.
	 */
	public OrdineDocumentoConverter() {
		super(Ordine.class, SiacTOrdine.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public Ordine convertFrom(SiacTOrdine src, Ordine dest) {
		if(src.getSiacRDocOrdines() == null){
			return dest;
		}
		for(SiacRDocOrdine r : src.getSiacRDocOrdines()){
			if(r.getDataCancellazione() == null && r.getSiacTDoc().getDataCancellazione() == null){
				DocumentoSpesa docSpesa = new DocumentoSpesa();
				docSpesa.setUid(r.getSiacTDoc().getUid());
				dest.setDocumento(docSpesa);
				return dest;
			}
		}
		return dest;
	}
	

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacTOrdine convertTo(Ordine src, SiacTOrdine dest) {
		
		if(src.getDocumento() == null || src.getDocumento().getUid() == 0){
			
			return dest;
		}
		SiacTDoc siacTDoc = new SiacTDoc();
		siacTDoc.setUid(src.getDocumento().getUid());
		List<SiacRDocOrdine> siacRDocOrdines = new ArrayList<SiacRDocOrdine>();
		SiacRDocOrdine siacRDocOrdine = new SiacRDocOrdine();
		siacRDocOrdine.setSiacTDoc(siacTDoc);
		siacRDocOrdine.setSiacTOrdine(dest);
		siacRDocOrdine.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
		siacRDocOrdine.setLoginOperazione(dest.getLoginOperazione());
		siacRDocOrdines.add(siacRDocOrdine);
		dest.setSiacRDocOrdines(siacRDocOrdines);
		return dest;
	}

}
