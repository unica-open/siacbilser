/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.math.BigDecimal;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.SiacTElencoDocRepository;
import it.csi.siac.siacbilser.integration.entity.SiacTElencoDoc;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDDocFamTipoEnum;
import it.csi.siac.siacfin2ser.model.ElencoDocumentiAllegato;

/**
 * Converter per i totali quote spesa ed entrata.
 * 
 * @author Domenico
 *
 */
@Component
public class ElencoDocTotaleQuoteSpesaEntrataConverter extends ExtendedDozerConverter<ElencoDocumentiAllegato, SiacTElencoDoc> {
	
	@Autowired
	private SiacTElencoDocRepository siacTElencoDocRepository;

	public ElencoDocTotaleQuoteSpesaEntrataConverter() {
		super(ElencoDocumentiAllegato.class, SiacTElencoDoc.class);
	}

	@Override
	public ElencoDocumentiAllegato convertFrom(SiacTElencoDoc src, ElencoDocumentiAllegato dest) {
		final String methodName = "convertFrom";
	
		
		BigDecimal totaleQuoteSpese = siacTElencoDocRepository.calcolaTotaleQuote(src.getUid(),
				Arrays.asList(SiacDDocFamTipoEnum.Spesa.getCodice(), SiacDDocFamTipoEnum.IvaSpesa.getCodice()));
		dest.setTotaleQuoteSpese(totaleQuoteSpese);
		log.debug(methodName, "totaleQuoteSpese: "+ totaleQuoteSpese);
		
		BigDecimal totaleQuoteEntrate = siacTElencoDocRepository.calcolaTotaleQuote(src.getUid(),
				Arrays.asList(SiacDDocFamTipoEnum.Entrata.getCodice(), SiacDDocFamTipoEnum.IvaEntrata.getCodice()));
		dest.setTotaleQuoteEntrate(totaleQuoteEntrate);
		log.debug(methodName, "totaleQuoteEntrate: "+ totaleQuoteEntrate);
		
	
		
		return dest;
	}

	@Override
	public SiacTElencoDoc convertTo(ElencoDocumentiAllegato src, SiacTElencoDoc dest) {
		
		return dest;
	}
	

}
