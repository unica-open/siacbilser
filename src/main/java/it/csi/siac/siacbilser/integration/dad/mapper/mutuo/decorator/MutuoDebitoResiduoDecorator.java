/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad.mapper.mutuo.decorator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacTMutuo;
import it.csi.siac.siacbilser.integration.entity.helper.SiacTMutuoHelper;
import it.csi.siac.siacbilser.model.mutuo.Mutuo;
import it.csi.siac.siaccommon.util.mapper.BaseMapperDecorator;

@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class MutuoDebitoResiduoDecorator extends BaseMapperDecorator<SiacTMutuo, Mutuo> {

	@Autowired private SiacTMutuoHelper siacTMutuoHelper;
	
	@Override
	public void decorate(SiacTMutuo s, Mutuo d) {
		d.setDebitoResiduo(siacTMutuoHelper.getDebitoResiduo(s));
	}
}
