/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad.mapper.movimentogestione.decorator;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacTMovgest;
import it.csi.siac.siacbilser.integration.entity.helper.SiacTMovgestHelper;
import it.csi.siac.siacbilser.integration.utility.function.SimpleJDBCFunctionInvoker;
import it.csi.siac.siaccommon.util.collections.CollectionUtil;
import it.csi.siac.siaccommon.util.mapper.BaseMapperDecorator;
import it.csi.siac.siacfinser.model.Accertamento;

@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class AccertamentoDettaglioImportiDecorator extends BaseMapperDecorator<SiacTMovgest, Accertamento> {
	
	@Autowired
	protected SimpleJDBCFunctionInvoker fi;
	@Autowired private SiacTMovgestHelper siacTMovgestHelper;
	
	
	@Override
	public void decorate(SiacTMovgest s, Accertamento d) {
		
		List<Object[]> ris = fi.invokeFunctionToObjectArray("fnc_siac_consultadettaglioaccertamento", siacTMovgestHelper.getSiacTMovgestTestata(s).getUid());
		
		if (CollectionUtil.isNotEmpty(ris)) {
			d.setImportoIncassato((BigDecimal)ris.get(0)[0]);
		}
	}
}