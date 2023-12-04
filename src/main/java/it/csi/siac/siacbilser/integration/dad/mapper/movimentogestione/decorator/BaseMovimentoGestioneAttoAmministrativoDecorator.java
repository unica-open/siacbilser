/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad.mapper.movimentogestione.decorator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dad.mapper.attoamministrativo.SiacTAttoAmmAttoAmministrativoMapper;
import it.csi.siac.siacbilser.integration.entity.SiacTMovgest;
import it.csi.siac.siacbilser.integration.entity.helper.SiacTMovgestHelper;
import it.csi.siac.siaccommon.util.mapper.BaseMapperDecorator;
import it.csi.siac.siacfinser.model.MovimentoGestione;

public abstract class BaseMovimentoGestioneAttoAmministrativoDecorator<MG extends MovimentoGestione> extends BaseMapperDecorator<SiacTMovgest, MG> {
	
	@Autowired private SiacTAttoAmmAttoAmministrativoMapper siacTAttoAmmAttoAmministrativoMapper;
	@Autowired private SiacTMovgestHelper siacTMovgestHelper;
	
	@Override
	public void decorate(SiacTMovgest s, MG d) {
		d.setAttoAmministrativo(siacTAttoAmmAttoAmministrativoMapper.map(siacTMovgestHelper.getSiacTAttoAmm(s)));
	}
}
