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
import it.csi.siac.siacgenser.model.MovimentoDettaglio;
import it.csi.siac.siacgenser.model.MovimentoEP;
import it.csi.siac.siacgenser.model.MovimentoEPModelDetail;

/**
 * The Class MovimentoDettaglioClassifConverter.
 */
@Component
public class MovimentoDettaglioMovimentoEPModelDetailConverter extends ExtendedDozerConverter<MovimentoDettaglio, SiacTMovEpDet> {
	

	/**
	 * Instantiates a new movimento dettaglio classif converter.
	 */
	public MovimentoDettaglioMovimentoEPModelDetailConverter() {
		super(MovimentoDettaglio.class, SiacTMovEpDet.class);
	}

	@Override
	public MovimentoDettaglio convertFrom(SiacTMovEpDet src, MovimentoDettaglio dest) {
		
		if(src.getSiacTMovEp() == null ) {
			return dest;
		}
		
		MovimentoEP movimentoEP = mapNotNull(src.getSiacTMovEp(), MovimentoEP.class, GenMapId.SiacTMovEp_MovimentoEP_Base, Converters.byModelDetails(Utility.MDTL.byModelDetailClass(MovimentoEPModelDetail.class)));
		dest.setMovimentoEP(movimentoEP);
		return dest;
	}
	

	@Override
	public SiacTMovEpDet convertTo(MovimentoDettaglio src, SiacTMovEpDet dest) {
		
		return dest;
	}
	
	
}
