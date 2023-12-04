/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.custom.oopp.integration.dad.mapper;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacDSoggettoClasse;
import it.csi.siac.siaccommon.util.mapper.BaseMapper;
import it.csi.siac.siacintegser.model.integ.ClasseSoggetto;

@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class SiacDSoggettoClasseClasseSoggettoMapper extends BaseMapper<SiacDSoggettoClasse, ClasseSoggetto> {

	@Override
	public void map(SiacDSoggettoClasse o1, ClasseSoggetto o2) {
		if (o1 == null || o2 == null) {
			return;
		}
		
		o2.setCodice(o1.getSoggettoClasseCode());
		o2.setDescrizione(o1.getSoggettoClasseDesc());
	}

}
