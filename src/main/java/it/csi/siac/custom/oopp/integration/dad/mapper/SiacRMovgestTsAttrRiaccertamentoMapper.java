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
import it.csi.siac.siaccommon.util.number.NumberUtil;
import it.csi.siac.siacfin2ser.model.TipoRiaccertamento;
import it.csi.siac.siacintegser.model.custom.oopp.Riaccertamento;

@Lazy
@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
 class SiacRMovgestTsAttrRiaccertamentoMapper extends BaseMapper<SiacRMovgestTsAttr, Riaccertamento> {

	@Autowired private SiacTAttrHelper siacTAttrHelper;

	@Override
	public void map(SiacRMovgestTsAttr o1, Riaccertamento o2) {
		if (o1 == null || o2 == null) {
			return;
		}
		
		if (siacTAttrHelper.checkAttrCode(o1.getSiacTAttr(), SiacTAttrEnum.AnnoRiaccertato)) {
			Integer anno = NumberUtil.safeParseInt(o1.getTesto());
			o2.setAnno(NumberUtil.isValidAndGreaterThanZero(anno) ? anno : null);
			return;
		}

		if (siacTAttrHelper.checkAttrCode(o1.getSiacTAttr(), SiacTAttrEnum.NumeroRiaccertato)) {
			o2.setNumero(NumberUtil.safeParseInt(o1.getTesto()));
			return;
		}

		if (siacTAttrHelper.checkAttrCode(o1.getSiacTAttr(), SiacTAttrEnum.FlagDaReanno) && siacTAttrHelper.convertAttrToBoolean(o1)) {
			o2.setCodiceTipo(TipoRiaccertamento.REANNO.name());
			return;
		}

		if (siacTAttrHelper.checkAttrCode(o1.getSiacTAttr(), SiacTAttrEnum.FlagDaRiaccertamento) && siacTAttrHelper.convertAttrToBoolean(o1)) {
			o2.setCodiceTipo(TipoRiaccertamento.REIMP.name());
			return;
		}
	}
}
