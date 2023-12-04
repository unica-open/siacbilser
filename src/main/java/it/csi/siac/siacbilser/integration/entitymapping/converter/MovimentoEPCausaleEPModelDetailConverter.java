/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.integration.entity.SiacTMovEp;
import it.csi.siac.siacbilser.integration.entitymapping.GenMapId;
import it.csi.siac.siacbilser.integration.entitymapping.converter.base.Converters;
import it.csi.siac.siacgenser.model.CausaleEP;
import it.csi.siac.siacgenser.model.CausaleEPModelDetail;
import it.csi.siac.siacgenser.model.MovimentoEP;

/**
 * The Class DocumentoEntrataSubdocumentoIvaConverter.
 */
@Component
public class MovimentoEPCausaleEPModelDetailConverter extends ExtendedDozerConverter<MovimentoEP, SiacTMovEp> {
	

	/**
	 * Instantiates a new subdocumento Entrata subdocumento iva converter.
	 */
	public MovimentoEPCausaleEPModelDetailConverter() {
		super(MovimentoEP.class, SiacTMovEp.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public MovimentoEP convertFrom(SiacTMovEp src, MovimentoEP dest) {
		CausaleEP causaleEP = mapNotNull(src.getSiacTCausaleEp(), CausaleEP.class, GenMapId.SiacTCausaleEp_CausaleEP_Minimal_Without_Ente, Converters.byModelDetails(Utility.MDTL.byModelDetailClass(CausaleEPModelDetail.class)));
		dest.setCausaleEP(causaleEP);
		return dest;
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacTMovEp convertTo(MovimentoEP src, SiacTMovEp dest) {
		return dest;
	}



	

}
