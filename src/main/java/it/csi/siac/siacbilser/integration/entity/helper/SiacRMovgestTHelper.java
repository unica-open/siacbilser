/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity.helper;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacRMovgestT;
import it.csi.siac.siacbilser.integration.entity.SiacTMovgest;
import it.csi.siac.siacbilser.integration.entity.helper.base.SiacTEnteBaseHelper;

@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class SiacRMovgestTHelper extends SiacTEnteBaseHelper {

	public SiacTMovgest getSiacTMovgest(SiacRMovgestT siacRMovgestT) {
		try {
			return 
				siacRMovgestT.isEntitaValida() && 
				siacRMovgestT.getSiacTMovgestT2().isEntitaValida() &&
				siacRMovgestT.getSiacTMovgestT2().getSiacTMovgest().isEntitaValida() ?
					siacRMovgestT.getSiacTMovgestT2().getSiacTMovgest() : 
					null;
		}
		catch (NullPointerException npe) {
			return null;
		}
	}

}
