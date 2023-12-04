/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.SiacTQuadroEconomicoRepository;
import it.csi.siac.siacbilser.integration.entity.SiacTQuadroEconomico;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacbilser.model.QuadroEconomico;

/**
 * The Class CausaleEPContoConverter.
 *
 * @author Elisa
 */
@Component
public class QuadroEconomicoQuadroEconomicoFigliConverter extends ExtendedDozerConverter<QuadroEconomico, SiacTQuadroEconomico > {
	
	@Autowired private SiacTQuadroEconomicoRepository siacTQuadroEconomicoRepository;

	/** Costruttore vuoto di default */
	public QuadroEconomicoQuadroEconomicoFigliConverter() {
		super(QuadroEconomico.class, SiacTQuadroEconomico.class);
	}

	@Override
	public QuadroEconomico convertFrom(SiacTQuadroEconomico src, QuadroEconomico dest) {
		List<QuadroEconomico> listaQuadroeconomico = new ArrayList<QuadroEconomico>();
		List<SiacTQuadroEconomico> listSiacTQuadroEconomicoFigli = siacTQuadroEconomicoRepository.findSiacTQuadroEconomicoFigliByIdPadre(src.getUid());
		
		for (SiacTQuadroEconomico siacTQuadroEconomico : listSiacTQuadroEconomicoFigli) {
			QuadroEconomico qe = map(siacTQuadroEconomico, QuadroEconomico.class, BilMapId.SiacTQuadroEconomico_QuadroEconomico_Base);
			listaQuadroeconomico.add(qe);
		}
		dest.setListaQuadroEconomicoFigli(listaQuadroeconomico);
		return dest;
	}


	@Override
	public SiacTQuadroEconomico convertTo(QuadroEconomico src, SiacTQuadroEconomico dest) {
		// Viene inserito il padre, e non i figli
		return dest;
	}


}
