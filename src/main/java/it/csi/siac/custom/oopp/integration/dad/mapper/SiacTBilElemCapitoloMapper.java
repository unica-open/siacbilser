/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.custom.oopp.integration.dad.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacTBilElem;
import it.csi.siac.siacbilser.integration.entity.helper.SiacTBilElemHelper;
import it.csi.siac.siaccommon.util.mapper.BaseMapper;
import it.csi.siac.siaccommon.util.number.NumberUtil;
import it.csi.siac.siacintegser.model.integ.Capitolo;

@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class SiacTBilElemCapitoloMapper extends BaseMapper<SiacTBilElem, Capitolo> {

	@Autowired private SiacTClassCapitoloMapper siacTClassCapitoloMapper;

	@Autowired private SiacTBilElemHelper siacTBilElemHelper;
	
	
	@Override
	public void map(SiacTBilElem o1, Capitolo o2) {
		if (o1 == null || o2 == null) {
			return;
		}
		
		o2.setNumeroCapitolo(NumberUtil.safeParseInt(o1.getElemCode()));
		o2.setNumeroArticolo(NumberUtil.safeParseInt(o1.getElemCode2()));
		o2.setNumeroUEB(NumberUtil.safeParseInt(o1.getElemCode3()));
		o2.setDescrizione(o1.getElemDesc());
		
		siacTClassCapitoloMapper.map(siacTBilElemHelper.getSiacTClassList(o1), o2);
	}

}
