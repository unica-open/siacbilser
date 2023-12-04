/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad.mapper.progetto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dad.mapper.util.MapperDecoratorHelper;
import it.csi.siac.siacbilser.integration.dao.ProgettoDao;
import it.csi.siac.siacbilser.integration.entity.SiacRMutuoProgramma;
import it.csi.siac.siacbilser.model.mutuo.ProgettoAssociatoMutuo;
import it.csi.siac.siaccommonser.util.mapper.SiacTBaseExtEntitaExtMapper;
import it.csi.siac.siacgenser.model.ProgettoModelDetail;


@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class SiacRMutuoProgrammaProgettoAssociatoMutuoMapper extends SiacTBaseExtEntitaExtMapper<SiacRMutuoProgramma, ProgettoAssociatoMutuo> {
	
	@Autowired SiacTProgrammaProgettoMapper siacTProgrammaProgettoMapper;
	@Autowired ProgettoDao progettoDao;
	@Autowired MapperDecoratorHelper mapperDecoratorHelper;
	
	@Override
	public void map(SiacRMutuoProgramma s, ProgettoAssociatoMutuo d) {
		super.map(s, d);
		
		d.setImportoIniziale(s.getMutuoProgrammaImportoIniziale());
		d.setImportoFinale(s.getMutuoProgrammaImportoFinale());
		
		d.setProgetto(siacTProgrammaProgettoMapper.map(
				progettoDao.findById(s.getSiacTProgramma().getUid())
				,mapperDecoratorHelper.getDecoratorsFromModelDetails(new ProgettoModelDetail[]{ProgettoModelDetail.AttoAmministrativo, ProgettoModelDetail.Classificatori, ProgettoModelDetail.Attributi})
		));
	}
}
