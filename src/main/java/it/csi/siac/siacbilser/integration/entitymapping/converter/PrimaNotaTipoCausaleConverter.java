/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.EnumEntityFactory;
import it.csi.siac.siacbilser.integration.entity.SiacDCausaleEpTipo;
import it.csi.siac.siacbilser.integration.entity.SiacTPrimaNota;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDCausaleEpTipoEnum;
import it.csi.siac.siacgenser.model.PrimaNota;
import it.csi.siac.siacgenser.model.TipoCausale;

/**
 * The Class DocumentoEntrataSubdocumentoIvaConverter.
 */
@Component
public class PrimaNotaTipoCausaleConverter extends ExtendedDozerConverter<PrimaNota, SiacTPrimaNota> {
	
	@Autowired
	private EnumEntityFactory eef;

	/**
	 * Instantiates a new subdocumento Entrata subdocumento iva converter.
	 */
	public PrimaNotaTipoCausaleConverter() {
		super(PrimaNota.class, SiacTPrimaNota.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public PrimaNota convertFrom(SiacTPrimaNota src, PrimaNota dest) {
		if(src.getSiacDCausaleEpTipo() == null){
			return dest;
		}
		SiacDCausaleEpTipoEnum siacDCausaleEpTipoEnum = SiacDCausaleEpTipoEnum.byCodice(src.getSiacDCausaleEpTipo().getCausaleEpTipoCode());
		TipoCausale tipoCausale = siacDCausaleEpTipoEnum.getTipoCausale();
		dest.setTipoCausale(tipoCausale);
		return dest;
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacTPrimaNota convertTo(PrimaNota src, SiacTPrimaNota dest) {
		if(src.getTipoCausale() == null){
			return dest;
		}
		SiacDCausaleEpTipoEnum siacDCausaleEpTipoEnum = SiacDCausaleEpTipoEnum.byTipoCausale(src.getTipoCausale());
		SiacDCausaleEpTipo siacDCausaleEpTipo = eef.getEntity(siacDCausaleEpTipoEnum, src.getEnte().getUid(), SiacDCausaleEpTipo.class);
		dest.setSiacDCausaleEpTipo(siacDCausaleEpTipo);
		return dest;
	}



	

}
