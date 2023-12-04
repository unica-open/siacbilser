/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity.helper;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacRMovgestTsAttr;
import it.csi.siac.siacbilser.integration.entity.SiacTAttr;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacTAttrEnum;
import it.csi.siac.siacbilser.integration.entity.helper.base.SiacTEnteBaseHelper;


@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class SiacTAttrHelper extends SiacTEnteBaseHelper {


	public boolean checkAttrCode(SiacTAttr siacTAttr, SiacTAttrEnum siacTAttrEnum) {
		return 
			siacTAttr != null &&
			siacTAttr.isEntitaValida() &&
			StringUtils.isNotBlank(siacTAttr.getAttrCode()) && 
			siacTAttrEnum.getCodice().equals(siacTAttr.getAttrCode());
	}
	
	public boolean convertAttrToBoolean(SiacRMovgestTsAttr o1) {
		return o1.getBoolean_() != null && "S".equals(o1.getBoolean_());
	}
}
