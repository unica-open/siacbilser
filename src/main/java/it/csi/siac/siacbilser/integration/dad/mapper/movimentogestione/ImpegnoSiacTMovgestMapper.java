/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad.mapper.movimentogestione;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacTMovgest;
import it.csi.siac.siacfinser.model.ImpegnoAbstract;

@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class ImpegnoSiacTMovgestMapper extends MovimentoGestioneSiacTMovgestMapper<ImpegnoAbstract> {
	
	@Override
	public void map(ImpegnoAbstract s, SiacTMovgest d) {
		super.map(s, d);
	}
}
