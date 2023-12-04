/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad.mapper.progetto.decorator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dad.mapper.progetto.SiacRProgrammaAttrProgettoMapper;
import it.csi.siac.siacbilser.integration.entity.SiacTProgramma;
import it.csi.siac.siacbilser.integration.entity.helper.SiacTProgrammaHelper;
import it.csi.siac.siacbilser.integration.utility.function.SimpleJDBCFunctionInvoker;
import it.csi.siac.siacbilser.model.Progetto;
import it.csi.siac.siaccommon.util.mapper.BaseMapperDecorator;

@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class ProgettoAttributiDecorator extends BaseMapperDecorator<SiacTProgramma, Progetto> {
	
	@Autowired private SiacRProgrammaAttrProgettoMapper siacRProgrammaAttrProgettoMapper;
	@Autowired private SiacTProgrammaHelper siacTProgrammaHelper;
	

	@Autowired
	protected SimpleJDBCFunctionInvoker fi;
	
	
	@Override
	public void decorate(SiacTProgramma s, Progetto d) {
		siacRProgrammaAttrProgettoMapper.map(siacTProgrammaHelper.getSiacRattrList(s), d);
	}
}
