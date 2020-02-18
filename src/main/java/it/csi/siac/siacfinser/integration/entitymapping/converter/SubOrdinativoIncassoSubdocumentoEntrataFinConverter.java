/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entitymapping.converter;

import java.util.ArrayList;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entitymapping.converter.ExtendedDozerConverter;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;
import it.csi.siac.siacfinser.integration.entity.SiacRSubdocOrdinativoTFin;
import it.csi.siac.siacfinser.integration.entity.SiacTOrdinativoTFin;
import it.csi.siac.siacfinser.integration.entity.SiacTSubdocFin;
import it.csi.siac.siacfinser.integration.entity.mapping.FinMapId;
import it.csi.siac.siacfinser.model.ordinativo.SubOrdinativoIncasso;

@Component
public class SubOrdinativoIncassoSubdocumentoEntrataFinConverter extends ExtendedDozerConverter<SubOrdinativoIncasso, SiacTOrdinativoTFin> {
	
	/**
	 * Instantiates a new sub ordinativo incasso Subdocumento entrata fin converter.
	 */
	public SubOrdinativoIncassoSubdocumentoEntrataFinConverter() {
		super(SubOrdinativoIncasso.class, SiacTOrdinativoTFin.class);
	}

	@Override
	public SubOrdinativoIncasso convertFrom(SiacTOrdinativoTFin src, SubOrdinativoIncasso dest) {
		if(src.getSiacRSubdocOrdinativoTs() != null) {
			for(SiacRSubdocOrdinativoTFin siacRSubdocOrdinativoTFin: src.getSiacRSubdocOrdinativoTs()) {
				if(siacRSubdocOrdinativoTFin.getDataCancellazione() == null) {
					SiacTSubdocFin siacTSubDocFin = siacRSubdocOrdinativoTFin.getSiacTSubdoc();
					
					SubdocumentoEntrata subDocumentoEntrata = mapNotNull(siacTSubDocFin,SubdocumentoEntrata.class, FinMapId.SiacTSubdocFin_SubdocumentoEntrata);
					dest.setSubDocumentoEntrata(subDocumentoEntrata);
					break;
				}
			}
		}
		return dest;
	}

	@Override
	public SiacTOrdinativoTFin convertTo(SubOrdinativoIncasso src, SiacTOrdinativoTFin dest) {
		if(src.getSubDocumentoEntrata() == null || src.getSubDocumentoEntrata().getUid() == 0) {
			return dest;
		}
		
		SiacRSubdocOrdinativoTFin siacRSubdocOrdinativoTs = new SiacRSubdocOrdinativoTFin();
		
		SiacTSubdocFin siacTSubdocFin = new SiacTSubdocFin();
		siacTSubdocFin.setUid(src.getSubDocumentoEntrata().getUid());
		siacRSubdocOrdinativoTs.setSiacTSubdoc(siacTSubdocFin);
		siacRSubdocOrdinativoTs.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
		siacRSubdocOrdinativoTs.setSiacTOrdinativoT(dest);
		
		dest.setSiacRSubdocOrdinativoTs(new ArrayList<SiacRSubdocOrdinativoTFin>());
		dest.getSiacRSubdocOrdinativoTs().add(siacRSubdocOrdinativoTs);
		return dest;
	}

}
