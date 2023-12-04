/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.custom.oopp.integration.dad.mapper;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacTOrdinativo;
import it.csi.siac.siaccommon.util.mapper.BaseMapper;
import it.csi.siac.siacintegser.model.custom.oopp.Mandato;


@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class SiacTOrdinativoMandatoMapper extends BaseMapper<SiacTOrdinativo, Mandato> {

	@Override
	public void map(SiacTOrdinativo o1, Mandato o2) {
		if (o1 == null || o2 == null) {
			return;
		}
		
		o2.setAnno(o1.getOrdAnno());
		o2.setNumero(o1.getOrdNumero());
		o2.setDescrizione(o1.getOrdDesc());
	}
}

