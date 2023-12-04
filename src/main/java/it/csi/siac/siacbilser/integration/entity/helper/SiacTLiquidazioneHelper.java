/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity.helper;

import java.util.List;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacRLiquidazioneAttr;
import it.csi.siac.siacbilser.integration.entity.SiacTLiquidazione;
import it.csi.siac.siacbilser.integration.entity.helper.base.SiacTEnteBaseHelper;
import it.csi.siac.siaccommonser.util.entity.EntityUtil;

@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class SiacTLiquidazioneHelper extends SiacTEnteBaseHelper {

	public List<SiacRLiquidazioneAttr> getSiacRSubdocAttrList(SiacTLiquidazione siacTLiquidazione) {
		try {
			return EntityUtil.getAllValid(siacTLiquidazione.getSiacRLiquidazioneAttrs());
		}
		catch (NullPointerException npe) {
			return null;
		}
	}


}
