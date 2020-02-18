/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.SiacTPeriodoRepository;
import it.csi.siac.siacbilser.integration.entity.SiacTPeriodo;
import it.csi.siac.siacfin2ser.model.Periodo;
import it.csi.siac.siacfin2ser.model.ProgressiviIva;

/**
 * The Class ProgressiviIvaPeriodoConverter.
 */
@Component
public class ProgressiviIvaPeriodoConverter extends ExtendedDozerConverter<ProgressiviIva, SiacTPeriodo> {
	
	@Autowired
	private SiacTPeriodoRepository siacTPeriodoRepository;
	
	/**
	 * Instantiates a new progressivi iva periodo converter.
	 */
	public ProgressiviIvaPeriodoConverter() {
		super(ProgressiviIva.class, SiacTPeriodo.class);
	}

	@Override
	public ProgressiviIva convertFrom(SiacTPeriodo src, ProgressiviIva dest) {
		// Imposto l'anno di esercizio
		dest.setAnnoEsercizio(Integer.valueOf(src.getAnno()));
		// Imposto il periodo
		dest.setPeriodo(Periodo.fromCodice(src.getSiacDPeriodoTipo().getPeriodoTipoCode()));
		return dest;
	}

	@Override
	public SiacTPeriodo convertTo(ProgressiviIva src, SiacTPeriodo dest) {
		return siacTPeriodoRepository.findByAnnoAndPeriodoTipoAndEnteProprietario(src.getAnnoEsercizio().toString(),
				src.getPeriodo().getCodice(), src.getEnte().getUid());
	}

}
