/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.dozer.DozerConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.EnumEntityFactory;
import it.csi.siac.siacbilser.integration.dao.SiacDCausaleTipoRepository;
import it.csi.siac.siacbilser.integration.entity.SiacDCausaleFamTipo;
import it.csi.siac.siacbilser.integration.entity.SiacDCausaleTipo;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDCausaleFamTipoEnum;
import it.csi.siac.siacfin2ser.model.TipoFamigliaCausale;

// TODO: Auto-generated Javadoc
/**
 * The Class TipoFamigliaCausaleConverter.
 */
@Component
public class TipoFamigliaCausaleConverter extends DozerConverter<TipoFamigliaCausale, SiacDCausaleTipo > {
	//private LogSrvUtil log = new LogSrvUtil(this.getClass());
	
	/** The eef. */
	@Autowired
	private EnumEntityFactory eef;
	@Autowired
	private SiacDCausaleTipoRepository siacDCausaleTipoRepository;

	/**
	 * Instantiates a new tipo causale converter.
	 */
	public TipoFamigliaCausaleConverter() {
		super(TipoFamigliaCausale.class, SiacDCausaleTipo.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public TipoFamigliaCausale convertFrom(SiacDCausaleTipo src, TipoFamigliaCausale dest) {
		//final String methodName = "convertFrom";
		SiacDCausaleTipo sdct = siacDCausaleTipoRepository.findOne(src.getUid());
		return SiacDCausaleFamTipoEnum.byCodice(sdct.getSiacDCausaleFamTipo().getCausFamTipoCode()).getTipoFamigliaCausale();
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacDCausaleTipo convertTo(TipoFamigliaCausale src, SiacDCausaleTipo dest) {
		//final String methodName = "convertTo";
		
		if(dest == null) {
			return null;
		}
		
		SiacDCausaleFamTipoEnum causaleFamTipo = SiacDCausaleFamTipoEnum.byTipoFamigliaCausale(src);
		SiacDCausaleFamTipo siacDCausaleFamTipo = eef.getEntity(causaleFamTipo, dest.getSiacTEnteProprietario().getUid(), SiacDCausaleFamTipo.class);
		
		dest.setSiacDCausaleFamTipo(siacDCausaleFamTipo);
		
		return dest;
	}



	

}
