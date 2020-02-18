/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.integration.entity.SiacTMovEpDet;
import it.csi.siac.siacbilser.integration.entitymapping.GenMapId;
import it.csi.siac.siacbilser.integration.entitymapping.converter.base.Converters;
import it.csi.siac.siacgenser.model.Conto;
import it.csi.siac.siacgenser.model.ContoModelDetail;
import it.csi.siac.siacgenser.model.MovimentoDettaglio;

/**
 * The Class MovimentoDettaglioClassifConverter.
 */
@Component
public class MovimentoDettaglioContoModelDetailConverter extends ExtendedDozerConverter<MovimentoDettaglio, SiacTMovEpDet> {
	

	/**
	 * Instantiates a new movimento dettaglio classif converter.
	 */
	public MovimentoDettaglioContoModelDetailConverter() {
		super(MovimentoDettaglio.class, SiacTMovEpDet.class);
	}

	@Override
	public MovimentoDettaglio convertFrom(SiacTMovEpDet src, MovimentoDettaglio dest) {
		
		
		if(src.getSiacTPdceConto() == null) {
			return dest;
		}
		Conto conto = mapNotNull(src.getSiacTPdceConto(), Conto.class, GenMapId.SiacTPdceConto_Conto_Minimal, Converters.byModelDetails(Utility.MDTL.byModelDetailClass(ContoModelDetail.class)));
		dest.setConto(conto);
		return dest;
	}
	

	@Override
	public SiacTMovEpDet convertTo(MovimentoDettaglio src, SiacTMovEpDet dest) {
		
		return dest;
	}
	
	
}
