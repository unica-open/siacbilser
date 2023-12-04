/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacRElencoDocPredoc;
import it.csi.siac.siacbilser.integration.entity.SiacTElencoDoc;
import it.csi.siac.siacbilser.integration.entity.SiacTPredoc;
import it.csi.siac.siacfin2ser.model.ElencoDocumentiAllegato;
import it.csi.siac.siacfin2ser.model.PreDocumento;

/**
 */
@Component
public class PreDocumentoElencoDocumentiAllegatoConverter2 extends ExtendedDozerConverter<PreDocumento<?, ?>, SiacTPredoc> {

	/**
	 * Instantiates a new pre documento spesa elenco documenti allegato converter.
 	*/
	@SuppressWarnings("unchecked")
	public PreDocumentoElencoDocumentiAllegatoConverter2() {
		super((Class<PreDocumento<?, ?>>)(Class<?>)PreDocumento.class, SiacTPredoc.class);
	}

	@Override
	public PreDocumento<?, ?> convertFrom(SiacTPredoc src, PreDocumento<?, ?> dest) {
		if(src.getSiacRElencoDocPredocs() != null) {
			for(SiacRElencoDocPredoc redp : src.getSiacRElencoDocPredocs()) {
				if(redp.getDataCancellazione() == null) {
					SiacTElencoDoc siacTElencoDoc = redp.getSiacTElencoDoc();
					ElencoDocumentiAllegato eda = new ElencoDocumentiAllegato();
					eda.setUid(siacTElencoDoc.getUid());
					eda.setAnno(siacTElencoDoc.getEldocAnno());
					eda.setNumero(siacTElencoDoc.getEldocNumero());
					
					dest.setElencoDocumentiAllegato(eda);
					break;
				}
			}
		}
		return dest;
	}

	@Override
	public SiacTPredoc convertTo(PreDocumento<?, ?> src, SiacTPredoc dest) {
		if(src.getElencoDocumentiAllegato() != null && src.getElencoDocumentiAllegato().getUid() != 0) {
			ElencoDocumentiAllegato elencoDocumentiAllegato = src.getElencoDocumentiAllegato();
			
			SiacTElencoDoc siacTElencoDoc = new SiacTElencoDoc();
			siacTElencoDoc.setUid(elencoDocumentiAllegato.getUid());
			
			SiacRElencoDocPredoc siacRElencoDocPredoc = new SiacRElencoDocPredoc();
			siacRElencoDocPredoc.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
			siacRElencoDocPredoc.setSiacTElencoDoc(siacTElencoDoc);
			siacRElencoDocPredoc.setSiacTPredoc(dest);
			siacRElencoDocPredoc.setLoginOperazione(dest.getLoginOperazione());
			
			List<SiacRElencoDocPredoc> siacRElencoDocPredocs = new ArrayList<SiacRElencoDocPredoc>();
			siacRElencoDocPredocs.add(siacRElencoDocPredoc);
			
			dest.setSiacRElencoDocPredocs(siacRElencoDocPredocs);
			
		}
		return dest;
	}

}
