/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity.helper;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacTAttoAmm;
import it.csi.siac.siacbilser.integration.entity.SiacTClass;
import it.csi.siac.siacbilser.integration.entity.helper.base.SiacTEnteBaseHelper;
import it.csi.siac.siaccommonser.util.entity.EntityUtil;

@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class SiacTAttoAmmHelper extends SiacTEnteBaseHelper {

	public SiacTClass getSiacTClass(SiacTAttoAmm o1) {
		try {
			return EntityUtil.getValid(EntityUtil.findFirstValid(o1.getSiacRAttoAmmClasses()).getSiacTClass());
		}
		catch (NullPointerException npe) {
			return null;
		}
	}

}
