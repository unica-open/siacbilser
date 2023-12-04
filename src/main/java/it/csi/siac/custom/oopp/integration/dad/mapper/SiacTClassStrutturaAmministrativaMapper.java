/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.custom.oopp.integration.dad.mapper;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacTClass;
import it.csi.siac.siaccommon.util.mapper.BaseMapper;
import it.csi.siac.siacintegser.model.integ.StrutturaAmministrativa;

@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class SiacTClassStrutturaAmministrativaMapper extends BaseMapper<SiacTClass, StrutturaAmministrativa> {

	@Override
	public void map(SiacTClass o1, StrutturaAmministrativa o2) {
		if (o1 == null || o2 == null) {
			return;
		}

		o2.setCodice(o1.getClassifCode());
		o2.setDescrizione(o1.getClassifDesc());
		o2.setCodiceTipoStruttura(o1.getSiacDClassTipo().getClassifTipoCode());
	}

}
