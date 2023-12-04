/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad.mapper.mutuo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dad.mapper.attoamministrativo.SiacTAttoAmmAttoAmministrativoMapper;
import it.csi.siac.siacbilser.integration.dad.mapper.mutuo.decorator.MutuoPeriodoRimborsoDecorator;
import it.csi.siac.siacbilser.integration.dad.mapper.mutuo.decorator.MutuoSoggettoDecorator;
import it.csi.siac.siacbilser.integration.dad.mapper.mutuo.decorator.MutuoStatoDecorator;
import it.csi.siac.siacbilser.integration.entity.SiacTMutuo;
import it.csi.siac.siacbilser.model.mutuo.Mutuo;

@Deprecated
@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class SiacTMutuoMutuoSinteticoMapper extends SiacTMutuoMutuoBaseMapper {
	
	@Autowired private SiacTAttoAmmAttoAmministrativoMapper siacTAttoAmmAttoAmministrativoMapper;
	
	@Autowired private MutuoPeriodoRimborsoDecorator mutuoPeriodoRimborsoDecorator;
	@Autowired private MutuoStatoDecorator mutuoStatoDecorator;
	@Autowired private MutuoSoggettoDecorator mutuoSoggettoDecorator;
	
	@Override
	public void map(SiacTMutuo s, Mutuo d) {
		super.map(s, d);

		d.setAttoAmministrativo(siacTAttoAmmAttoAmministrativoMapper.map(s.getSiacTAttoAmm()));
		
		mutuoSoggettoDecorator.decorate(s, d);
		mutuoPeriodoRimborsoDecorator.decorate(s, d);
		mutuoStatoDecorator.decorate(s, d);
	}
}
