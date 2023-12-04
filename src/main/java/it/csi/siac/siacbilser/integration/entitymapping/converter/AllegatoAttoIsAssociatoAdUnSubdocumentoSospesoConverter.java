/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.dozer.DozerConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.SiacTSubdocRepository;
import it.csi.siac.siacbilser.integration.entity.SiacTAttoAllegato;
import it.csi.siac.siacfin2ser.model.AllegatoAtto;
/**
 * Converter per il boolean isAssociatoAdUnSubdocumentoSospeso nell'AllegatoAtto
 * @author Marchino Alessandro
 * @version 1.0.0 - 12/07/2017
 *
 */
@Component
public class AllegatoAttoIsAssociatoAdUnSubdocumentoSospesoConverter extends DozerConverter<AllegatoAtto, SiacTAttoAllegato> {
	
	/** The siacTSubdocRepository */
	@Autowired
	private SiacTSubdocRepository siacTSubdocRepository;

	/**
	 *  Costruttore vuoto di default.
	 */
	public AllegatoAttoIsAssociatoAdUnSubdocumentoSospesoConverter() {
		super(AllegatoAtto.class, SiacTAttoAllegato.class);
	}

	@Override
	public AllegatoAtto convertFrom(SiacTAttoAllegato src, AllegatoAtto dest) {
		// Per ogni elenco, lo aggiungo all'allegato con la ricerca minima
		Long quote = siacTSubdocRepository.countSiacTSubdocSospesiByAttoalId(src.getUid());
		dest.setIsAssociatoAdUnSubdocumentoSospeso(Boolean.valueOf(quote != null && quote.longValue() >= 1L));
		return dest;
	}

	@Override
	public SiacTAttoAllegato convertTo(AllegatoAtto src, SiacTAttoAllegato dest) {
		return dest;
	}
}
