/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad.mapper.attoamministrativo;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siacbilser.integration.entity.SiacDAttoAmmTipo;
import it.csi.siac.siaccommonser.util.mapper.SiacTBaseEntitaMapper;


@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class SiacDAttoAmmTipoTipoAttoMapper extends SiacTBaseEntitaMapper<SiacDAttoAmmTipo, TipoAtto > {

	@Override
	public void map(SiacDAttoAmmTipo a, TipoAtto b) {
		super.map(a, b);
		b.setCodice(a.getAttoammTipoCode());
		b.setDescrizione(a.getAttoammTipoDesc());
	}
}


