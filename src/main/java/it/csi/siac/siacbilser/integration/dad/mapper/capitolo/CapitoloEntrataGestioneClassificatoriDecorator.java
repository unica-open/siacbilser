/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad.mapper.capitolo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacTBilElem;
import it.csi.siac.siacbilser.integration.entity.helper.SiacTBilElemHelper;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siaccommon.util.mapper.BaseMapperDecorator;

@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class CapitoloEntrataGestioneClassificatoriDecorator extends BaseMapperDecorator<SiacTBilElem, CapitoloEntrataGestione> {

	@Autowired private SiacTClassCapitoloEntrataGestioneMapper siacTClassCapitoloEntrataGestioneMapper;
	@Autowired private SiacTBilElemHelper siacTBilElemHelper;
	
	@Override
	public void decorate(SiacTBilElem s, CapitoloEntrataGestione d) {
		siacTClassCapitoloEntrataGestioneMapper.map(siacTBilElemHelper.getSiacTClassList(s), d);
	}
}
