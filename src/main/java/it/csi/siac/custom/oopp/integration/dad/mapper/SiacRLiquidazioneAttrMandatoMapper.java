/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.custom.oopp.integration.dad.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacRLiquidazioneAttr;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacTAttrEnum;
import it.csi.siac.siacbilser.integration.entity.helper.SiacTAttrHelper;
import it.csi.siac.siaccommon.util.mapper.BaseMapper;
import it.csi.siac.siacintegser.model.custom.oopp.Mandato;

@Lazy
@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
 class SiacRLiquidazioneAttrMandatoMapper extends BaseMapper<SiacRLiquidazioneAttr, Mandato> {

	@Autowired private SiacTAttrHelper siacTAttrHelper;

	@Override
	public void map(SiacRLiquidazioneAttr o1, Mandato o2) {
		if (o1 == null || o2 == null) {
			return;
		}
		
		if (siacTAttrHelper.checkAttrCode(o1.getSiacTAttr(), SiacTAttrEnum.Cig)) {
			o2.setCig(o1.getTesto());
			return;
		}

		if (siacTAttrHelper.checkAttrCode(o1.getSiacTAttr(), SiacTAttrEnum.Cup)) {
			o2.setCup(o1.getTesto());
			return;
		}
	}


}
