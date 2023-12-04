/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad.mapper.soggetto;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacTSoggetto;
import it.csi.siac.siaccommonser.util.mapper.SiacTBaseEntitaMapper;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;


@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class SiacTSoggettoSoggettoMapper extends SiacTBaseEntitaMapper<SiacTSoggetto, Soggetto> {

	@Override
	public void map(SiacTSoggetto a, Soggetto b) {
		super.map(a, b);
		b.setCodiceSoggetto(a.getSoggettoCode());
		b.setDenominazione(a.getSoggettoDesc());
		b.setFlagIstitutoDiCredito(a.getIstitutoDiCredito());
	}
}


