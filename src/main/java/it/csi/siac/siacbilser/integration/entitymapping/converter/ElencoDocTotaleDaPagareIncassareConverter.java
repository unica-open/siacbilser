/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.SiacTElencoDocRepository;
import it.csi.siac.siacbilser.integration.entity.SiacTElencoDoc;
import it.csi.siac.siacfin2ser.model.ElencoDocumentiAllegato;

@Component
public class ElencoDocTotaleDaPagareIncassareConverter extends ExtendedDozerConverter<ElencoDocumentiAllegato, SiacTElencoDoc> {
	
	@Autowired
	private SiacTElencoDocRepository siacTElencoDocRepository;

	
	public ElencoDocTotaleDaPagareIncassareConverter() {
		super(ElencoDocumentiAllegato.class, SiacTElencoDoc.class);
	}

	@Override
	public ElencoDocumentiAllegato convertFrom(SiacTElencoDoc src, ElencoDocumentiAllegato dest) {
		final String methodName = "convertFrom";
	
		
		BigDecimal totaleDaPagare = siacTElencoDocRepository.calcolaTotaleDaPagare(src.getUid());
		dest.setTotaleDaPagare(totaleDaPagare);
		log.debug(methodName, "totaleDaPagare: "+ totaleDaPagare);
		
		BigDecimal totaleDaIncassare = siacTElencoDocRepository.calcolaTotaleDaIncassare(src.getUid());
		dest.setTotaleDaIncassare(totaleDaIncassare);
		log.debug(methodName, "totaleDaIncassare: "+ totaleDaIncassare);
		
	
		
		return dest;
	}

	@Override
	public SiacTElencoDoc convertTo(ElencoDocumentiAllegato src, SiacTElencoDoc dest) {
		
		return dest;
	}
	

}
