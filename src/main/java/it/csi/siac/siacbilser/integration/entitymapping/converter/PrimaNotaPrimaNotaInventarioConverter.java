/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.SiacTPrimaNotaRepository;
import it.csi.siac.siacbilser.integration.entity.SiacTPrimaNota;
import it.csi.siac.siacbilser.integration.entitymapping.GenMapId;
import it.csi.siac.siacbilser.integration.entitymapping.converter.base.Converters;
import it.csi.siac.siacgenser.model.PrimaNota;
import it.csi.siac.siacgenser.model.PrimaNotaModelDetail;

/**
 * The Class PrimaNotaPrimaNotaInventarioConverter.
 * @author Marchino Alessandro
 * @version 1.0.0 - 17/10/2018
 */
@Component
public class PrimaNotaPrimaNotaInventarioConverter extends ExtendedDozerConverter<PrimaNota, SiacTPrimaNota> {
	
	@Autowired
	private SiacTPrimaNotaRepository siacTPrimaNotaRepository;
	
	public PrimaNotaPrimaNotaInventarioConverter() {
		super(PrimaNota.class, SiacTPrimaNota.class);
	}

	@Override
	public PrimaNota convertFrom(SiacTPrimaNota src, PrimaNota dest) {
		List<SiacTPrimaNota> siacTPRimaNotas = siacTPrimaNotaRepository.findSiacTPrimaNotaInv(src.getPnotaId());
		if(siacTPRimaNotas != null && !siacTPRimaNotas.isEmpty()) {
			SiacTPrimaNota siacTPrimaNota = siacTPRimaNotas.get(0);
			dest.setPrimaNotaInventario(mapNotNull(siacTPrimaNota, PrimaNota.class, GenMapId.SiacTPrimaNota_PrimaNota_ModelDetail,
					Converters.byModelDetails(PrimaNotaModelDetail.StatoAccettazionePrimaNotaDefinitiva)));
		}
		return dest;
	}


	@Override
	public SiacTPrimaNota convertTo(PrimaNota src, SiacTPrimaNota dest) {
		// Nothing to do
		return dest;
	}

}
