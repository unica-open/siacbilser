/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad.mapper.mutuo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dad.mapper.util.MapperDecoratorHelper;
import it.csi.siac.siacbilser.integration.entity.SiacRMutuoProgramma;
import it.csi.siac.siacbilser.model.mutuo.MutuoModelDetail;
import it.csi.siac.siacbilser.model.progetto.MutuoAssociatoProgetto;
import it.csi.siac.siaccommonser.util.mapper.SiacTBaseExtEntitaExtMapper;


@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class SiacRMutuoProgrammaMutuoAssociatoProgettoMapper extends SiacTBaseExtEntitaExtMapper<SiacRMutuoProgramma, MutuoAssociatoProgetto> {
	
	
	@Autowired SiacTMutuoMutuoMapper siacTMutuoMutuoMapper;
	@Autowired MapperDecoratorHelper mapperDecoratorHelper;
	
	@Override
	public void map(SiacRMutuoProgramma s, MutuoAssociatoProgetto d) {
		super.map(s, d);
		
		d.setImportoIniziale(s.getMutuoProgrammaImportoIniziale());
		d.setImportoFinale(s.getMutuoProgrammaImportoFinale());

		d.setMutuo(siacTMutuoMutuoMapper.map(
				s.getSiacTMutuo(), mapperDecoratorHelper.getDecoratorsFromModelDetails(MutuoModelDetail.AssociatoAEntita)
				));
	}
}
