/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad.mapper.movimentogestione;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dad.mapper.util.MapperDecoratorHelper;
import it.csi.siac.siacbilser.integration.dao.MovimentoGestioneDao;
import it.csi.siac.siacbilser.integration.entity.SiacRMutuoMovgestT;
import it.csi.siac.siacbilser.model.mutuo.ImpegnoAssociatoMutuo;
import it.csi.siac.siaccommonser.util.mapper.SiacTBaseExtEntitaExtMapper;
import it.csi.siac.siacfin2ser.model.ImpegnoModelDetail;


@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class SiacRMutuoMovgestTImpegnoAssociatoMutuoMapper extends SiacTBaseExtEntitaExtMapper<SiacRMutuoMovgestT, ImpegnoAssociatoMutuo> {
	
	
	@Autowired SiacTMovgestImpegnoMapper siacTMovgestImpegnoMapper;
	@Autowired MapperDecoratorHelper mapperDecoratorHelper;
	@Autowired MovimentoGestioneDao movimentoGestioneDao;
	
	@Override
	public void map(SiacRMutuoMovgestT s, ImpegnoAssociatoMutuo d) {
		super.map(s, d);
		
		d.setImportoIniziale(s.getMutuoMovgestTsImportoIniziale());
		d.setImportoFinale(s.getMutuoMovgestTsImportoFinale());
		
		d.setImpegno(siacTMovgestImpegnoMapper.map(
			movimentoGestioneDao.findById(s.getSiacTMovgestT().getSiacTMovgest().getUid()),
			mapperDecoratorHelper.getDecoratorsFromModelDetails(ImpegnoModelDetail.Dettaglio, ImpegnoModelDetail.DettaglioImpegnoCapitolo)
		));
	}
}
