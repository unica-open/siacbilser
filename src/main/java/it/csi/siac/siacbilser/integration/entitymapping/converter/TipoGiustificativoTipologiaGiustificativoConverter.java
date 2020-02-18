/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.EnumEntityFactory;
import it.csi.siac.siacbilser.integration.entity.SiacDGiustificativo;
import it.csi.siac.siacbilser.integration.entity.SiacDGiustificativoTipo;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDGiustificativoTipoEnum;
import it.csi.siac.siaccecser.model.TipoGiustificativo;
import it.csi.siac.siaccecser.model.TipologiaGiustificativo;

// TODO: Auto-generated Javadoc
/**
 * The Class GruppoAttivitaIvaRegistroIvaConverter.
 */
@Component
public class TipoGiustificativoTipologiaGiustificativoConverter extends ExtendedDozerConverter<TipoGiustificativo,SiacDGiustificativo> {

	@Autowired
	private EnumEntityFactory eef;
	/**
	 * Instantiates a new gruppo attivita iva registro iva converter.
	 */
	public TipoGiustificativoTipologiaGiustificativoConverter() {
		super(TipoGiustificativo.class, SiacDGiustificativo.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public TipoGiustificativo convertFrom(SiacDGiustificativo src, TipoGiustificativo dest) {
		if(src.getSiacDGiustificativoTipo()!=null){
			TipologiaGiustificativo tipologiaGiustificativo = TipologiaGiustificativo.byCodice(src.getSiacDGiustificativoTipo().getGiustTipoCode());
			dest.setTipologiaGiustificativo(tipologiaGiustificativo);
		}
        return dest;

	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacDGiustificativo convertTo(TipoGiustificativo src, SiacDGiustificativo dest) {
		if(src.getTipologiaGiustificativo() == null){
			return dest;
		}
		SiacDGiustificativoTipoEnum siacDGiustificativoTipoEnum = SiacDGiustificativoTipoEnum.byCodice(src.getTipologiaGiustificativo().getCodice());
		SiacDGiustificativoTipo siacDGiustificativoTipo = eef.getEntity(siacDGiustificativoTipoEnum, dest.getSiacTEnteProprietario().getUid(), SiacDGiustificativoTipo.class);
		dest.setSiacDGiustificativoTipo(siacDGiustificativoTipo);
		
		return dest;
	}



	

}
