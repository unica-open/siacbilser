/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacintegser.business.service.test;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import it.csi.siac.siacintegser.business.service.base.IntegBaseService;
import it.csi.siac.siacintegser.frontend.webservice.msg.test.Test;
import it.csi.siac.siacintegser.frontend.webservice.msg.test.TestResponse;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class TestService extends IntegBaseService<Test, TestResponse>
{
	@Override
	protected TestResponse execute(Test ireq) {
		TestResponse ires = instantiateNewIRes();
		ires.setTestInteg(ireq);
		return ires;
	}
}
