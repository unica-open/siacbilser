/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.custom.oopp.integration.dad.mapper;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacDSiopeAssenzaMotivazione;
import it.csi.siac.siaccommon.util.mapper.BaseMapper;
import it.csi.siac.siacintegser.model.custom.oopp.MovimentoGestione;

@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class SiacDSiopeAssenzaMotivazioneMovimentoGestioneMapper extends BaseMapper<SiacDSiopeAssenzaMotivazione, MovimentoGestione> {

	@Override
	public void map(SiacDSiopeAssenzaMotivazione o1, MovimentoGestione o2) {
		if (o1 == null || o2 == null) {
			return;
		}
		
		o2.setMotivoAssenzaCig(o1.getSiopeAssenzaMotivazioneDesc());
	}

}
