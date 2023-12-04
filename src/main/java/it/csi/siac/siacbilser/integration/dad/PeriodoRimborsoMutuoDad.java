/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.integration.dad.mapper.mutuo.SiacDMutuoPeriodoRimborsoPeriodoRimborsoMutuoMapper;
import it.csi.siac.siacbilser.integration.dao.mutuo.SiacDMutuoPeriodoRimborsoRepository;
import it.csi.siac.siacbilser.integration.entity.SiacDMutuoPeriodoRimborso;
import it.csi.siac.siacbilser.model.mutuo.PeriodoRimborsoMutuo;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class PeriodoRimborsoMutuoDad extends ExtendedBaseDadImpl {

	@Autowired private SiacDMutuoPeriodoRimborsoRepository siacDMutuoPeriodoRimborsoRepository;
	@Autowired private SiacDMutuoPeriodoRimborsoPeriodoRimborsoMutuoMapper siacDMutuoPeriodoRimborsoPeriodoRimborsoMutuoMapper;

	/**
	 * Ricerca il periodo rimborso a partire dal codice
	 * 
	 * @param periodoRimborso 
	 * 
	 * @return periodoRimborso 
	 */
	public PeriodoRimborsoMutuo findPeriodoRimborsoMutuoByLogicKey(PeriodoRimborsoMutuo periodoRimborso){
		
		SiacDMutuoPeriodoRimborso siacDMutuoPeriodoRimborso = siacDMutuoPeriodoRimborsoRepository.findByCodice(periodoRimborso.getCodice());
		
		if(siacDMutuoPeriodoRimborso == null) {
			return null;
		}
		
		return siacDMutuoPeriodoRimborsoPeriodoRimborsoMutuoMapper.map(siacDMutuoPeriodoRimborso);
		
	}
	
	public PeriodoRimborsoMutuo findPeriodoRimborsoByKey(PeriodoRimborsoMutuo periodoRimborso) {
		
		if (periodoRimborso.getUid()!=0) {
			return siacDMutuoPeriodoRimborsoPeriodoRimborsoMutuoMapper.map(siacDMutuoPeriodoRimborsoRepository.findOne(periodoRimborso.getUid()));
		}
		
		return findPeriodoRimborsoMutuoByLogicKey(periodoRimborso);
	}	
}
