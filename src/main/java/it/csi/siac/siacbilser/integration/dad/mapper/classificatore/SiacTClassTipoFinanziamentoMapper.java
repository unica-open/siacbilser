/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad.mapper.classificatore;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacTClass;
import it.csi.siac.siacbilser.model.TipoFinanziamento;
import it.csi.siac.siaccommonser.util.mapper.SiacTBaseEntitaMapper;

@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class SiacTClassTipoFinanziamentoMapper extends SiacTBaseEntitaMapper<SiacTClass, TipoFinanziamento> {

	@Override
	public void map(SiacTClass a, TipoFinanziamento b) {
		super.map(a, b);
		b.setCodice(a.getClassifCode());
		b.setDescrizione(a.getClassifDesc());
	}
}
