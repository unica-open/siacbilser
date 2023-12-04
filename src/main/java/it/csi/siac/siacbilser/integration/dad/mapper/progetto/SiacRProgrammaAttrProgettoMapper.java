/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad.mapper.progetto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacRProgrammaAttr;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacTAttrEnum;
import it.csi.siac.siacbilser.integration.entity.helper.SiacTAttrHelper;
import it.csi.siac.siacbilser.model.Progetto;
import it.csi.siac.siaccommon.util.mapper.BaseMapper;

@Lazy
@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class SiacRProgrammaAttrProgettoMapper extends BaseMapper<SiacRProgrammaAttr, Progetto> {

	@Autowired private SiacTAttrHelper siacTAttrHelper;

	@Override
	public void map(SiacRProgrammaAttr o1, Progetto o2) {
		if (o1 == null || o2 == null) {
			return;
		}
		
		if (siacTAttrHelper.checkAttrCode(o1.getSiacTAttr(), SiacTAttrEnum.ValoreComplessivoProgetto)) {
			o2.setValoreComplessivo(o1.getNumerico());
			return;
		}
	}
}
