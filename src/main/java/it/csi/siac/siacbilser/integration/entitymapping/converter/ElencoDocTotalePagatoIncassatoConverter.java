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
public class ElencoDocTotalePagatoIncassatoConverter extends ExtendedDozerConverter<ElencoDocumentiAllegato, SiacTElencoDoc> {
	
	@Autowired
	private SiacTElencoDocRepository siacTElencoDocRepository;

	
	public ElencoDocTotalePagatoIncassatoConverter() {
		super(ElencoDocumentiAllegato.class, SiacTElencoDoc.class);
	}

	@Override
	public ElencoDocumentiAllegato convertFrom(SiacTElencoDoc src, ElencoDocumentiAllegato dest) {
		final String methodName = "convertFrom";
	
		
		BigDecimal totalePagato = siacTElencoDocRepository.calcolaTotalePagato(src.getUid());
		dest.setTotalePagato(totalePagato);
		log.debug(methodName, "totalePagato: "+ totalePagato);
		
		BigDecimal totaleIncassato = siacTElencoDocRepository.calcolaTotaleIncassato(src.getUid());
		dest.setTotaleIncassato(totaleIncassato);
		log.debug(methodName, "totaleIncassato: "+ totaleIncassato);
		
	
		
		return dest;
	}

	@Override
	public SiacTElencoDoc convertTo(ElencoDocumentiAllegato src, SiacTElencoDoc dest) {
		
		return dest;
	}
	

}
