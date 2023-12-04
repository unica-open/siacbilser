/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad.mapper.progetto;

import java.util.List;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacDClassTipo;
import it.csi.siac.siacbilser.integration.entity.SiacTClass;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDClassTipoEnum;
import it.csi.siac.siacbilser.model.Progetto;
import it.csi.siac.siacbilser.model.TipoAmbito;
import it.csi.siac.siaccommon.util.mapper.BaseMapper;

@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class SiacTClassProgettoMapper extends BaseMapper<SiacTClass, Progetto> {

	@Override
	public void map(List<SiacTClass> siacTClassList, Progetto progetto) {
		
		super.map(siacTClassList, progetto);

	}
	
	@Override
	public void map(SiacTClass o1, Progetto o2) {
		if (o1 == null || o2 == null) {
			return;
		}
		if (check(o1.getSiacDClassTipo(), SiacDClassTipoEnum.TipoAmbito)) {
			o2.setTipoAmbito(new TipoAmbito(o1.getClassifCode(), o1.getClassifDesc()));
			return;
		}
	}

	private boolean check(SiacDClassTipo siacDClassTipo, SiacDClassTipoEnum siacDClassTipoEnum) {
		return 
			siacDClassTipo != null && 
			siacDClassTipo.isEntitaValida() && 
			siacDClassTipoEnum.getCodice().equals(siacDClassTipo.getClassifTipoCode());
	}
}
