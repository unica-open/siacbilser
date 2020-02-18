/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import it.csi.siac.siacbilser.integration.entity.SiacDCausale;
import it.csi.siac.siacbilser.integration.entity.SiacDDistinta;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacfin2ser.model.CausaleEntrata;
import it.csi.siac.siacfinser.model.Distinta;

/**
 * The Class CausaleEntrataEntrataDistintaConverter.
 */
public class CausaleEntrataDistintaConverter extends ExtendedDozerConverter<CausaleEntrata, SiacDCausale > {

	/**
	 * Instantiates a new tipo CausaleEntrata converter.
	 */
	public CausaleEntrataDistintaConverter() {
		super(CausaleEntrata.class, SiacDCausale.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public CausaleEntrata convertFrom(SiacDCausale src, CausaleEntrata dest) {
		
		SiacDDistinta siacDDistinta = src.getSiacDDistinta();
		Distinta dist = mapNotNull(siacDDistinta, Distinta.class, BilMapId.SiacDDistinta_Distinta);
		dest.setDistinta(dist);
		return dest;
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacDCausale convertTo(CausaleEntrata src, SiacDCausale dest) {
		Distinta distinta = src.getDistinta();
		if( distinta == null || distinta.getUid() == 0){
			return dest;
		}
		SiacDDistinta siacDDistinta  = new SiacDDistinta();
		siacDDistinta.setUid(distinta.getUid());
		dest.setSiacDDistinta(siacDDistinta);
		return dest;
	}



	

}
