/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad.mapper.progetto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dad.mapper.base.SiacTEnteBaseEntitaEnteMapper;
import it.csi.siac.siacbilser.integration.dad.mapper.bilancio.SiacTBilBilancioMapper;
import it.csi.siac.siacbilser.integration.entity.SiacTProgramma;
import it.csi.siac.siacbilser.model.Progetto;

@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class SiacTProgrammaProgettoMapper extends SiacTEnteBaseEntitaEnteMapper<SiacTProgramma, Progetto> {
	
	private @Autowired SiacTBilBilancioMapper siacTBilBilancioMapper;
	
	@Override
	public void map(SiacTProgramma s,Progetto d) {
		super.map(s, d);
		
		d.setCodice(s.getProgrammaCode());
		d.setBilancio(siacTBilBilancioMapper.map(s.getSiacTBil()));
	}
}
