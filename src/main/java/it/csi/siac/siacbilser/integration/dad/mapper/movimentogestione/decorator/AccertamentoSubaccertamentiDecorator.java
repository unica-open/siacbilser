/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad.mapper.movimentogestione.decorator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacTMovgest;
import it.csi.siac.siacbilser.integration.entity.helper.SiacTMovgestHelper;
import it.csi.siac.siaccommon.util.collections.CollectionUtil;
import it.csi.siac.siaccommon.util.mapper.BaseMapperDecorator;
import it.csi.siac.siaccommon.util.number.NumberUtil;
import it.csi.siac.siacfinser.model.Accertamento;

@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class AccertamentoSubaccertamentiDecorator extends BaseMapperDecorator<SiacTMovgest, Accertamento> {

	@Autowired private SiacTMovgestHelper siacTMovgestHelper;

	@Override
	public void decorate(SiacTMovgest s, Accertamento d) {
		d.setTotaleSubAccertamentiBigDecimal(NumberUtil.toBigDecimal(CollectionUtil.getSize(siacTMovgestHelper.getSiacTMovgestSubList(s))));
	}
}
