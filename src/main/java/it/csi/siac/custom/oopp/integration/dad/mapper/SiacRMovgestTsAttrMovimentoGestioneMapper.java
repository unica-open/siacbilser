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

import it.csi.siac.siacbilser.integration.entity.SiacRMovgestTsAttr;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacTAttrEnum;
import it.csi.siac.siacbilser.integration.entity.helper.SiacTAttrHelper;
import it.csi.siac.siaccommon.util.mapper.BaseMapper;
import it.csi.siac.siacintegser.model.custom.oopp.MovimentoGestione;

@Lazy
@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
 class SiacRMovgestTsAttrMovimentoGestioneMapper extends BaseMapper<SiacRMovgestTsAttr, MovimentoGestione> {

	@Autowired private SiacTAttrHelper siacTAttrHelper;

	@Override
	public void map(SiacRMovgestTsAttr o1, MovimentoGestione o2) {
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

		if (siacTAttrHelper.checkAttrCode(o1.getSiacTAttr(), SiacTAttrEnum.FlagPrenotazione)) {
			o2.setPrenotazioneImpegno(siacTAttrHelper.convertAttrToBoolean(o1));
			return;
		}

		if (siacTAttrHelper.checkAttrCode(o1.getSiacTAttr(), SiacTAttrEnum.FlagAttivaGsa)) {
			o2.setRilevanteGsa(siacTAttrHelper.convertAttrToBoolean(o1));
			return;
		}
	}


}
