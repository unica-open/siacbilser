/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.SiacTElencoDocRepository;
import it.csi.siac.siacbilser.integration.entity.SiacTElencoDoc;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacTAttrEnum;
import it.csi.siac.siacfin2ser.model.ElencoDocumentiAllegato;

@Component
public class ElencoDocImpegnoConfermaDataFineValiditaDurcConverter extends ExtendedDozerConverter<ElencoDocumentiAllegato, SiacTElencoDoc> {
	
	@Autowired
	private SiacTElencoDocRepository  siacTElencoDocRepository;

	
	public ElencoDocImpegnoConfermaDataFineValiditaDurcConverter() {
		super(ElencoDocumentiAllegato.class, SiacTElencoDoc.class);
	}

	@Override
	public ElencoDocumentiAllegato convertFrom(SiacTElencoDoc src, ElencoDocumentiAllegato dest) {
		final String methodName = "convertFrom";
		List<Integer> uids = siacTElencoDocRepository.getSubDocIdsWihtMovgestWithBooleanAttrCodeAndValueByEldocId(src.getUid(), SiacTAttrEnum.FlagSoggettoDurc.getCodice(), "S");
		if(uids == null || uids.isEmpty()) {
			log.debug(methodName, "non vi sono subdocumenti collegati ad impegno con flag durc = true. esco.");
			dest.setHasImpegnoConfermaDurc(Boolean.FALSE);
			return dest;
		}
		dest.setHasImpegnoConfermaDurc(Boolean.TRUE);
		List<Date> data = siacTElencoDocRepository.getDataFineValiditaDurcByEldocIdSubdocIds(src.getUid(), uids);
		if(data == null || data.isEmpty()) {
			log.debug(methodName, "Non vi sono subdocumenti che richiedono conferma durc legati con data scadenza durc. esco.");
			return dest;
		}
		dest.setDataFineValiditaDurc(data.get(0));
		return dest;
	}

	@Override
	public SiacTElencoDoc convertTo(ElencoDocumentiAllegato src, SiacTElencoDoc dest) {
		return dest;
	}
	

}
