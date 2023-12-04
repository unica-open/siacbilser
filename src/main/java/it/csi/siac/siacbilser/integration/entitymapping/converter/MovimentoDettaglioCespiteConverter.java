/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.integration.entity.SiacRCespitiMovEpDet;
import it.csi.siac.siacbilser.integration.entity.SiacTMovEpDet;
import it.csi.siac.siacbilser.integration.entitymapping.CespMapId;
import it.csi.siac.siacbilser.integration.entitymapping.converter.base.Converters;
import it.csi.siac.siaccespser.model.Cespite;
import it.csi.siac.siaccespser.model.CespiteModelDetail;
import it.csi.siac.siacgenser.model.MovimentoDettaglio;

/**
 * The Class DocumentoEntrataSubdocumentoIvaConverter.
 */
@Component
public class MovimentoDettaglioCespiteConverter extends ExtendedDozerConverter<MovimentoDettaglio, SiacTMovEpDet> {
	

	/**
	 * Instantiates a new subdocumento Entrata subdocumento iva converter.
	 */
	public MovimentoDettaglioCespiteConverter() {
		super(MovimentoDettaglio.class, SiacTMovEpDet.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public MovimentoDettaglio convertFrom(SiacTMovEpDet src, MovimentoDettaglio dest) {
		
		if(src.getSiacRCespitiMovEpDets() == null || src.getSiacRCespitiMovEpDets().isEmpty()){
			return dest;
		}
		Utility.MDETTL.set(dest);
		List<Cespite> cespiti = new ArrayList<Cespite>();
		for (SiacRCespitiMovEpDet siacRCespitiMovEpDet : src.getSiacRCespitiMovEpDets()) {
			if(siacRCespitiMovEpDet.getDataCancellazione() != null) {
				continue;
			}
			Cespite cespite = mapNotNull(siacRCespitiMovEpDet.getSiacTCespiti(), Cespite.class, CespMapId.SiacTCespiti_Cespite_ModelDetail, Converters.byModelDetails(Utility.MDTL.byModelDetailClass(CespiteModelDetail.class)));
			cespiti.add(cespite);
		}
		dest.setCespiti(cespiti);
		Utility.MDETTL.set(null);
		return dest;
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacTMovEpDet convertTo(MovimentoDettaglio src, SiacTMovEpDet dest) {
		//questa relazione non va salvata
		return dest;
	}



	

}
