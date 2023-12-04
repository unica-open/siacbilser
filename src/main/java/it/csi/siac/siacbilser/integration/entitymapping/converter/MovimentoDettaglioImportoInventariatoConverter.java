/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacRCespitiMovEpDet;
import it.csi.siac.siacbilser.integration.entity.SiacTMovEpDet;
import it.csi.siac.siacgenser.model.MovimentoDettaglio;

/**
 * The Class DocumentoEntrataSubdocumentoIvaConverter.
 */
@Component
public class MovimentoDettaglioImportoInventariatoConverter extends ExtendedDozerConverter<MovimentoDettaglio, SiacTMovEpDet> {
	

	/**
	 * Instantiates a new subdocumento Entrata subdocumento iva converter.
	 */
	public MovimentoDettaglioImportoInventariatoConverter() {
		super(MovimentoDettaglio.class, SiacTMovEpDet.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public MovimentoDettaglio convertFrom(SiacTMovEpDet src, MovimentoDettaglio dest) {
		
		BigDecimal importoInventariato = BigDecimal.ZERO;
		if(src.getSiacRCespitiMovEpDets() == null || src.getSiacRCespitiMovEpDets().isEmpty()){
			dest.setImportoInventariato(importoInventariato);
			return dest;
		}
		
		for (SiacRCespitiMovEpDet siacRCespitiMovEpDet : src.getSiacRCespitiMovEpDets()) {
			if(siacRCespitiMovEpDet.getDataCancellazione() != null) {
				continue;
			}
			importoInventariato = importoInventariato.add(siacRCespitiMovEpDet.getImportoSuPrimaNota());
		}
		dest.setImportoInventariato(importoInventariato);
		return dest;
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacTMovEpDet convertTo(MovimentoDettaglio src, SiacTMovEpDet dest) {
		return dest;
	}



	

}
