/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad.mapper.movimentogestione.decorator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dad.mapper.movimentogestione.SiacRMovgestTsAttrImpegnoMapper;
import it.csi.siac.siacbilser.integration.entity.SiacTMovgest;
import it.csi.siac.siacbilser.integration.entity.helper.SiacTMovgestHelper;
import it.csi.siac.siacbilser.integration.utility.function.SimpleJDBCFunctionInvoker;
import it.csi.siac.siaccommon.util.mapper.BaseMapperDecorator;
import it.csi.siac.siacfinser.model.Impegno;

@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class ImpegnoAttributiDecorator extends BaseMapperDecorator<SiacTMovgest, Impegno> {
	
	@Autowired private SiacRMovgestTsAttrImpegnoMapper siacRMovgestTsAttrImpegnoMapper;
	@Autowired private SiacTMovgestHelper siacTMovgestHelper;
	

	@Autowired
	protected SimpleJDBCFunctionInvoker fi;
	
	
	@Override
	public void decorate(SiacTMovgest s, Impegno d) {
		siacRMovgestTsAttrImpegnoMapper.map(siacTMovgestHelper.getSiacRattrList(s), d);
	}
}
