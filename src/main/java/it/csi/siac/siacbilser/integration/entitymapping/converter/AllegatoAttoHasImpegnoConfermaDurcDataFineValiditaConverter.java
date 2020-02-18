/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.SiacTAttoAllegatoRepository;
import it.csi.siac.siacbilser.integration.dao.SiacTSubdocRepository;
import it.csi.siac.siacbilser.integration.entity.SiacTAttoAllegato;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacTAttrEnum;
import it.csi.siac.siacfin2ser.model.AllegatoAtto;
/**
 * Converter per il boolean isAssociatoAdUnDocumento nell'AllegatoAtto
 * @author Elisa Chiari
 * @version 1.0.0 - 11/02/2016
 *
 */
@Component
public class AllegatoAttoHasImpegnoConfermaDurcDataFineValiditaConverter extends ExtendedDozerConverter<AllegatoAtto, SiacTAttoAllegato> {
	
	/** The siacTSubdocRepository */
	@Autowired
	private SiacTAttoAllegatoRepository siacTAttoAllegatoRepository;
	@Autowired
	private SiacTSubdocRepository siacTSubdocRepository;

	/**
	 *  Costruttore vuoto di default.
	 */
	public AllegatoAttoHasImpegnoConfermaDurcDataFineValiditaConverter() {
		super(AllegatoAtto.class, SiacTAttoAllegato.class);
	}

	@Override
	public AllegatoAtto convertFrom(SiacTAttoAllegato src, AllegatoAtto dest) {
		final String methodName = "convertFrom";
		List<Integer> uids = siacTAttoAllegatoRepository.getUidsSubdocWithImpegniWithBooleanAttrCodeAndValueByAttoalId(src.getUid(), SiacTAttrEnum.FlagSoggettoDurc.getCodice(), "S");
		if(uids == null || uids.isEmpty()) {
			log.debug(methodName, "non vi sono subdocumenti collegati ad impegno con flag durc = true. esco.");
			dest.setHasImpegnoConfermaDurc(Boolean.FALSE);
			return dest;
		}
		dest.setHasImpegnoConfermaDurc(Boolean.TRUE);
		List<Date> data = siacTSubdocRepository.getDataFineValiditaDurcBySubdocIds(uids);
		if(data == null || data.isEmpty()) {
			log.debug(methodName, "Non vi sono subdocumenti che richiedono conferma durc legati con data scadenza durc. esco.");
			return dest;
		}
		dest.setDataFineValiditaDurc(data.get(0));
		return dest;
	}

	@Override
	public SiacTAttoAllegato convertTo(AllegatoAtto src, SiacTAttoAllegato dest) {
		return dest;
	}
}
