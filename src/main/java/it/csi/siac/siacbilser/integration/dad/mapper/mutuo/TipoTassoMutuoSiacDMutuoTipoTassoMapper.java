/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad.mapper.mutuo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.mutuo.SiacDMutuoTipoTassoDao;
import it.csi.siac.siacbilser.integration.entity.SiacDMutuoTipoTasso;
import it.csi.siac.siacbilser.model.mutuo.TipoTassoMutuo;
import it.csi.siac.siaccommon.util.mapper.BaseMapper;


@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class TipoTassoMutuoSiacDMutuoTipoTassoMapper extends BaseMapper<TipoTassoMutuo, SiacDMutuoTipoTasso> {
	
	@Autowired private SiacDMutuoTipoTassoDao siacDMutuoTipoTassoDao;
	
	@Override
	public void map(TipoTassoMutuo s, SiacDMutuoTipoTasso d) {
	}

	public SiacDMutuoTipoTasso map(TipoTassoMutuo s, Integer enteProprietarioId) {
		return siacDMutuoTipoTassoDao.findByCode("mutuoTipoTassoCode", s.getCodice(), enteProprietarioId);
	}

}


