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
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;

/**
 * The ClassSubdocumentoSpesaElencoDocumentiConverter
 */
@Component
public class SubdocumentoSpesaElencoDocumentiConverter extends ExtendedDozerConverter<SubdocumentoSpesa, SiacTSubdoc > {

	/**
	 * Instantiates a new subdocumento spesa subdocumento iva converter.
	 */
	public SubdocumentoSpesaElencoDocumentiConverter() {
		super(SubdocumentoSpesa.class, SiacTSubdoc.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SubdocumentoSpesa convertFrom(SiacTSubdoc src, SubdocumentoSpesa dest) {
		
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
	public SiacTSubdoc convertTo(SubdocumentoSpesa src, SiacTSubdoc dest) {
		
		return dest;
	}

}
