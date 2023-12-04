/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacRDocOnere;
import it.csi.siac.siacbilser.integration.entity.SiacTDoc;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacfin2ser.model.DettaglioOnere;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.RitenuteDocumento;

/**
 * The Class DocumentoSpesaOnereConverter.
 */
@Component
public class DocumentoSpesaOnereConverter extends ExtendedDozerConverter<DocumentoSpesa, SiacTDoc > {

	/**
	 * Instantiates a new subdocumento spesa subdocumento iva converter.
	 */
	public DocumentoSpesaOnereConverter() {
		super(DocumentoSpesa.class, SiacTDoc.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public DocumentoSpesa convertFrom(SiacTDoc src, DocumentoSpesa dest) {
		
		if(src.getSiacRDocOneres()!=null){
			RitenuteDocumento ritenuteDocumento = dest.getRitenuteDocumento();
			if(ritenuteDocumento == null) {
				ritenuteDocumento = new RitenuteDocumento();
				dest.setRitenuteDocumento(ritenuteDocumento);
			}
			
			List<DettaglioOnere> listaOnere = new ArrayList<DettaglioOnere>();
			
			for (SiacRDocOnere r : src.getSiacRDocOneres()) {
				if(r.getDataCancellazione()==null) {
					DettaglioOnere dettaglioOnere = map(r, DettaglioOnere.class, BilMapId.SiacRDocOnere_DettaglioOnere);
					listaOnere.add(dettaglioOnere);
				}
			}
			
			ritenuteDocumento.setListaOnere(listaOnere);
		}
		
		return dest;
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacTDoc convertTo(DocumentoSpesa src, SiacTDoc dest) {
		//non dovrebbe servirmi
		return dest;
	}



	

}
