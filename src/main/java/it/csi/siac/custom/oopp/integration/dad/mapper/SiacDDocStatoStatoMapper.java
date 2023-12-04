/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.custom.oopp.integration.dad.mapper;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacDDocStato;
import it.csi.siac.siaccommon.util.mapper.BaseMapper;
import it.csi.siac.siacintegser.model.base.Stato;

@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class SiacDDocStatoStatoMapper extends BaseMapper<SiacDDocStato, Stato> {

	@Override
	public void map(SiacDDocStato o1, Stato o2) {
		if (o1 == null || o2 == null) {
			return;
		}
		
		o2.setCodice(o1.getDocStatoCode());
		o2.setDescrizione(o1.getDocStatoDesc());
	}
}
