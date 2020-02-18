/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacTPrimaNota;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacgenser.model.PrimaNota;

/**
 * The Class DocumentoEntrataSubdocumentoIvaConverter.
 */
@Component
public class PrimaNotaSoggettoConverter extends ExtendedDozerConverter<PrimaNota, SiacTPrimaNota> {
	

	/**
	 * Instantiates a new subdocumento Entrata subdocumento iva converter.
	 */
	public PrimaNotaSoggettoConverter() {
		super(PrimaNota.class, SiacTPrimaNota.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public PrimaNota convertFrom(SiacTPrimaNota src, PrimaNota dest) {
		//SiacTSoggetto_Soggetto
		if(src.getSiacTSoggetto() == null){
			return dest;
		}
		Soggetto soggetto = mapNotNull(src.getSiacTSoggetto(), Soggetto.class, BilMapId.SiacTSoggetto_Soggetto);
		dest.setSoggetto(soggetto);
		return dest;
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacTPrimaNota convertTo(PrimaNota src, SiacTPrimaNota dest) {
		return dest;
	}



	

}
