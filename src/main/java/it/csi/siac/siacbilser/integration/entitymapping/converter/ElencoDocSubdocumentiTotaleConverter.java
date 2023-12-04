/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.SiacTElencoDocRepository;
import it.csi.siac.siacbilser.integration.entity.SiacTElencoDoc;
import it.csi.siac.siacfin2ser.model.ElencoDocumentiAllegato;

@Component
public class ElencoDocSubdocumentiTotaleConverter extends ExtendedDozerConverter<ElencoDocumentiAllegato, SiacTElencoDoc> {
	
	@Autowired
	private SiacTElencoDocRepository siacTElencoDocRepository;

	/**
	 * Instantiates a new ElencoDocSubdocumentiTotale converter.
	 */
	public ElencoDocSubdocumentiTotaleConverter() {
		super(ElencoDocumentiAllegato.class, SiacTElencoDoc.class);
	}

	@Override
	public ElencoDocumentiAllegato convertFrom(SiacTElencoDoc src, ElencoDocumentiAllegato dest) {
		String methodName = "convertFrom";

		Long numeroQuoteInElenco = siacTElencoDocRepository.calcolaNumeroQuoteLegateAdElenco(src.getUid());
		log.debug(methodName, "numero di quote in elenco: " + numeroQuoteInElenco);
		dest.setNumeroQuoteInElenco(numeroQuoteInElenco);
		return dest;
	}

	@Override
	public SiacTElencoDoc convertTo(ElencoDocumentiAllegato src, SiacTElencoDoc dest) {
		return dest;
	}
	

}
