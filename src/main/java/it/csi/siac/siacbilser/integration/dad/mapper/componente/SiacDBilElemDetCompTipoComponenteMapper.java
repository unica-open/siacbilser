/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad.mapper.componente;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dad.mapper.base.SiacTEnteBaseEntitaEnteMapper;
import it.csi.siac.siacbilser.integration.entity.SiacDBilElemDetCompTipo;
import it.csi.siac.siacfinser.model.movgest.ComponenteBilancioImpegno;


@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class SiacDBilElemDetCompTipoComponenteMapper extends SiacTEnteBaseEntitaEnteMapper<SiacDBilElemDetCompTipo, ComponenteBilancioImpegno> {
	@Override
	public void map(SiacDBilElemDetCompTipo a, ComponenteBilancioImpegno b) {
		super.map(a, b);
		b.setUid(0);
		b.setIdTipoComponente(a.getUid());
		b.setDescrizioneTipoComponente(a.getElemDetCompTipoDesc());
	}
}