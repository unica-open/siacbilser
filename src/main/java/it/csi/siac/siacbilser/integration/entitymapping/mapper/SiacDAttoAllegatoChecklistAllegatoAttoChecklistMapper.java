/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.mapper;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacDAttoAllegatoChecklist;
import it.csi.siac.siaccommon.util.ReflectionUtil;
import it.csi.siac.siaccommon.util.mapper.BaseMapper;
import it.csi.siac.siacfin2ser.model.allegatoattochecklist.Checklist;
import it.csi.siac.siacfin2ser.model.allegatoattochecklist.ChecklistDelegate;

@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class SiacDAttoAllegatoChecklistAllegatoAttoChecklistMapper extends BaseMapper<SiacDAttoAllegatoChecklist, Checklist> {

	@Override
	public void map(SiacDAttoAllegatoChecklist o1, Checklist o2) {
		if (o1 == null || o2 == null) {
			return;
		}
		ReflectionUtil.invokeSetterMethodWithFieldName(new ChecklistDelegate(o2), o1.getAttoAllegatoChecklistCode(), String.class, o1.getAttoAllegatoChecklistDesc());
	}

}
