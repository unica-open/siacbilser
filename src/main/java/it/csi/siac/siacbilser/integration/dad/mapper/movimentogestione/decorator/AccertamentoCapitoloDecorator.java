/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad.mapper.movimentogestione.decorator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dad.mapper.capitolo.SiacTBilElemCapitoloEntrataGestioneMapper;
import it.csi.siac.siacbilser.integration.entity.SiacTBilElem;
import it.csi.siac.siacbilser.integration.entity.SiacTMovgest;
import it.csi.siac.siacbilser.integration.entity.helper.SiacTMovgestHelper;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siaccommon.util.mapper.BaseMapperDecorator;
import it.csi.siac.siaccommon.util.mapper.MapperDecorator;
import it.csi.siac.siacfinser.model.Accertamento;

@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class AccertamentoCapitoloDecorator extends BaseMapperDecorator<SiacTMovgest, Accertamento> {

	@Autowired private SiacTBilElemCapitoloEntrataGestioneMapper siacTBilElemCapitoloEntrataGestioneMapper;
	@Autowired private SiacTMovgestHelper siacTMovgestHelper;
	
	@Override
	public void decorate(SiacTMovgest s, Accertamento d) {
		d.setCapitoloEntrataGestione(siacTBilElemCapitoloEntrataGestioneMapper.map(siacTMovgestHelper.getSiacTBilElem(s)
				, (MapperDecorator<SiacTBilElem, CapitoloEntrataGestione>[]) getMapperDecorators()
				));
	}
}
