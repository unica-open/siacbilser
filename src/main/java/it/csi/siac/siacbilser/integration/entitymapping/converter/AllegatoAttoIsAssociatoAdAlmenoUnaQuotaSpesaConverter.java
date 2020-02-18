/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.Arrays;

import org.dozer.DozerConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.SiacTSubdocRepository;
import it.csi.siac.siacbilser.integration.entity.SiacTAttoAllegato;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDDocFamTipoEnum;
import it.csi.siac.siacfin2ser.model.AllegatoAtto;
/**
 * Converter per il boolean isAssociatoAdAlmenoUnaQuotaSpesa nell'AllegatoAtto
 * @author Elisa Chiari
 * @version 1.0.0 - 11/02/2016
 *
 */
@Component
public class AllegatoAttoIsAssociatoAdAlmenoUnaQuotaSpesaConverter extends DozerConverter<AllegatoAtto, SiacTAttoAllegato> {

	@Autowired
	private SiacTSubdocRepository siacTSubdocRepository;
	
	
	public AllegatoAttoIsAssociatoAdAlmenoUnaQuotaSpesaConverter() {
		super(AllegatoAtto.class, SiacTAttoAllegato.class);
	}

	@Override
	public AllegatoAtto convertFrom(SiacTAttoAllegato src, AllegatoAtto dest) {
		// CR-2996: controllo se esistealmeno una quota di Spesa
		Long quoteSpesa =siacTSubdocRepository.countSiacTSubdocByAttoalIdAndDocFamTipoCodeIn(src.getUid(), Arrays.asList(SiacDDocFamTipoEnum.Spesa.getCodice(), SiacDDocFamTipoEnum.IvaSpesa.getCodice()));
		dest.setIsAssociatoAdAlmenoUnaQuotaSpesa(Boolean.valueOf(quoteSpesa >= 1));				
		return dest;
	}

	@Override
	public SiacTAttoAllegato convertTo(AllegatoAtto src, SiacTAttoAllegato dest) {		
		return dest;
	}

}
