/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacRElencoDocSubdoc;
import it.csi.siac.siacbilser.integration.entity.SiacTSubdoc;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacfin2ser.model.ElencoDocumentiAllegato;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;

/**
 * The Class SubdocumentoEntrataElencoDocumentiConverter
 */
@Component
public class SubdocumentoEntrataElencoDocumentiConverter extends ExtendedDozerConverter<SubdocumentoEntrata, SiacTSubdoc > {

	/**
	 * Instantiates a new subdocumento spesa subdocumento iva converter.
	 */
	public SubdocumentoEntrataElencoDocumentiConverter() {
		super(SubdocumentoEntrata.class, SiacTSubdoc.class);
	}

	@Override
	public SubdocumentoEntrata convertFrom(SiacTSubdoc src, SubdocumentoEntrata dest) {
		
		if(src.getSiacRElencoDocSubdocs()!=null){
			for (SiacRElencoDocSubdoc  siacRElencoDocSubdoc : src.getSiacRElencoDocSubdocs()) {
				if(siacRElencoDocSubdoc.getDataCancellazione() == null){
					ElencoDocumentiAllegato elencoDocumentiAllegato = map(siacRElencoDocSubdoc.getSiacTElencoDoc(), ElencoDocumentiAllegato.class,
							//TODO Prima qui utilizzava SiacTElencoDoc_ElencoDocumentiAllegato_Minimal (senza suffisso _AllegatoAtto) verificare prestazioni! quando tiro su tanti subdoc!!!
							BilMapId.SiacTElencoDoc_ElencoDocumentiAllegato_Minimal_AllegatoAtto); 
					dest.setElencoDocumenti(elencoDocumentiAllegato);
				}
			}
		}
		return dest;
	}

	@Override
	public SiacTSubdoc convertTo(SubdocumentoEntrata src, SiacTSubdoc dest) {
		return dest;
	}

}
