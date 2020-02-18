/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.SiacTPrimaNotaRepository;
import it.csi.siac.siacbilser.integration.entity.SiacTPdceConto;
import it.csi.siac.siacbilser.integration.entity.SiacTPrimaNota;
import it.csi.siac.siacbilser.integration.entitymapping.GenMapId;
import it.csi.siac.siacgenser.model.Conto;
import it.csi.siac.siacgenser.model.PrimaNota;

/**
 * The Class PrimaNotaContoInventarioConverter.
 * @author Marchino Alessandro
 * @version 1.0.0 - 17/10/2018
 */
@Component
public class PrimaNotaContoInventarioConverter extends ExtendedDozerConverter<PrimaNota, SiacTPrimaNota> {
	
	@Autowired
	private SiacTPrimaNotaRepository siacTPrimaNotaRepository;
	
	public PrimaNotaContoInventarioConverter() {
		super(PrimaNota.class, SiacTPrimaNota.class);
	}

	@Override
	public PrimaNota convertFrom(SiacTPrimaNota src, PrimaNota dest) {
		List<SiacTPdceConto> siacTPdceContos = siacTPrimaNotaRepository.findSiacTPdceContoInv(src.getPnotaId());
		if(siacTPdceContos != null && !siacTPdceContos.isEmpty()) {
			SiacTPdceConto siacTPdceConto = siacTPdceContos.get(0);
			dest.setContoInventario(mapNotNull(siacTPdceConto, Conto.class, GenMapId.SiacTPdceConto_Conto_Minimal));
		}
		return dest;
	}


	@Override
	public SiacTPrimaNota convertTo(PrimaNota src, SiacTPrimaNota dest) {
		// Nothing to do
		return dest;
	}

}
