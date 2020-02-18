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
import it.csi.siac.siacgenser.model.MovimentoEP;
import it.csi.siac.siacgenser.model.PrimaNota;
import it.csi.siac.siacgenser.model.PrimaNotaModelDetail;

/**
 * The Class DocumentoEntrataSubdocumentoIvaConverter.
 */
@Component
public class MovimentoEPPrimaNotaModelDetailConverter extends ExtendedDozerConverter<MovimentoEP, SiacTMovEp> {
	

	/**
	 * Instantiates a new subdocumento Entrata subdocumento iva converter.
	 */
	public MovimentoEPPrimaNotaModelDetailConverter() {
		super(MovimentoEP.class, SiacTMovEp.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public MovimentoEP convertFrom(SiacTMovEp src, MovimentoEP dest) {
		
		PrimaNota primaNota =  mapNotNull(src.getSiacTPrimaNota(), PrimaNota.class, 
				GenMapId.SiacTPrimaNota_PrimaNota_ModelDetail, 
				Converters.byModelDetails(Utility.MDTL.byModelDetailClass(PrimaNotaModelDetail.class)));
		dest.setPrimaNota(primaNota);
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
