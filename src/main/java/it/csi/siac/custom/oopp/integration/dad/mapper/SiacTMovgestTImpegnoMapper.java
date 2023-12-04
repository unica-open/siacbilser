/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.custom.oopp.integration.dad.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacTMovgestT;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDMovgestTsTipoEnum;
import it.csi.siac.siacbilser.integration.entity.helper.SiacTMovgestTHelper;
import it.csi.siac.siaccommon.util.mapper.BaseMapper;
import it.csi.siac.siacintegser.model.custom.oopp.BaseMovimentoGestione;
import it.csi.siac.siacintegser.model.custom.oopp.Impegno;

@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class SiacTMovgestTImpegnoMapper extends BaseMapper<SiacTMovgestT, Impegno> {

	@Autowired private SiacTMovgestTHelper siacTMovgestTHelper;

	@Override
	public void map(SiacTMovgestT o1, Impegno o2) {
		if (o1 == null || o2 == null) {
			return;
		}
		
		new BaseMovimentoGestioneMapper<BaseMovimentoGestione>().map(o1.getSiacTMovgest(), o2);
		o2.setElencoVincoliImpegno(null);

		if (siacTMovgestTHelper.equalsSiacDMovgestTsTipo(o1, SiacDMovgestTsTipoEnum.Sub)) {
			o2.setSubNumero(o1.getMovgestTsCode());
		}
	}
	
}
