/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad.mapper.bilancio;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacTBil;
import it.csi.siac.siaccommon.util.number.NumberUtil;
import it.csi.siac.siaccommonser.util.mapper.SiacTBaseEntitaMapper;
import it.csi.siac.siaccorser.model.Bilancio;


@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class SiacTBilBilancioMapper extends SiacTBaseEntitaMapper<SiacTBil, Bilancio> {
	@Override
	public void map(SiacTBil a, Bilancio b) {
		super.map(a, b);
		b.setAnno(NumberUtil.safeParseInt(a.getSiacTPeriodo().getAnno()));
	}
}


