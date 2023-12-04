/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacDGiustificativo;
import it.csi.siac.siacbilser.integration.entity.SiacTGiustificativoDet;
import it.csi.siac.siacbilser.integration.entitymapping.CecMapId;
import it.csi.siac.siaccecser.model.Giustificativo;
import it.csi.siac.siaccecser.model.TipoGiustificativo;

/**
 * The Class GiustificativoStatoConverter.
 */
@Component
public class GiustificativoTipoGiustificativoConverter extends ExtendedDozerConverter<Giustificativo, SiacTGiustificativoDet> {
	

	/**
	 * Instantiates a new giustificativo stato converter.
	 */
	public GiustificativoTipoGiustificativoConverter() {
		super(Giustificativo.class, SiacTGiustificativoDet.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public Giustificativo convertFrom(SiacTGiustificativoDet src, Giustificativo dest) {
		
		if(src.getSiacDGiustificativo() == null){
			return dest;
		}
		TipoGiustificativo tipoGiustificativo = map(src.getSiacDGiustificativo(),TipoGiustificativo.class, CecMapId.SiacDGiustificativo_TipoGiustificativo);
		dest.setTipoGiustificativo(tipoGiustificativo);
		return dest;
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacTGiustificativoDet convertTo(Giustificativo src, SiacTGiustificativoDet dest) {
		
		if(src.getTipoGiustificativo() == null){
			return dest;
		}
		SiacDGiustificativo siacDGiustificativo = new SiacDGiustificativo();
		siacDGiustificativo.setUid(src.getTipoGiustificativo().getUid());
		dest.setSiacDGiustificativo(siacDGiustificativo );
		return dest;
		
	}



	

}
