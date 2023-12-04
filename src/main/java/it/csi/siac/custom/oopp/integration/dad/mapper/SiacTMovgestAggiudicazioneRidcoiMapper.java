/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.custom.oopp.integration.dad.mapper;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacTMovgest;
import it.csi.siac.siaccommon.util.mapper.BaseMapper;
import it.csi.siac.siacintegser.model.custom.oopp.Ridcoi;

@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class SiacTMovgestAggiudicazioneRidcoiMapper extends BaseMapper<SiacTMovgest, Ridcoi> {
	
	@Override
	public void map(SiacTMovgest o1, Ridcoi o2) {
		if (o1 == null || o2 == null) {
			return;
		}
		
		o2.setAnno(o1.getMovgestAnno());
		o2.setNumero(o1.getMovgestNumero());
	}
}
