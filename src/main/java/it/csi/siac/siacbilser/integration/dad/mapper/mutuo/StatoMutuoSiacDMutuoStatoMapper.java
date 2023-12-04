/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad.mapper.mutuo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.mutuo.SiacDMutuoStatoDao;
import it.csi.siac.siacbilser.integration.entity.SiacDMutuoStato;
import it.csi.siac.siacbilser.model.mutuo.StatoMutuo;
import it.csi.siac.siaccommon.util.mapper.BaseMapper;


@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class StatoMutuoSiacDMutuoStatoMapper extends BaseMapper<StatoMutuo, SiacDMutuoStato> {
	
	@Autowired private SiacDMutuoStatoDao siacDMutuoStatoDao;
	
	@Override
	public void map(StatoMutuo s, SiacDMutuoStato d) {
	}

	public SiacDMutuoStato map(StatoMutuo s, Integer enteProprietarioId) {
		return siacDMutuoStatoDao.findByCode("mutuoStatoCode", s.getCodice(), enteProprietarioId);
	}

}


