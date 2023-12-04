/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad.mapper.mutuo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.mutuo.SiacDMutuoVariazioneTipoDao;
import it.csi.siac.siacbilser.integration.entity.SiacDMutuoVariazioneTipo;
import it.csi.siac.siacbilser.model.mutuo.TipoVariazioneMutuo;
import it.csi.siac.siaccommon.util.mapper.BaseMapper;


@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class TipoVariazioneMutuoSiacDMutuoVariazioneTipoMapper extends BaseMapper<TipoVariazioneMutuo, SiacDMutuoVariazioneTipo> {
	
	@Autowired private SiacDMutuoVariazioneTipoDao siacDMutuoVariazioneTipoDao;
	
	@Override
	public void map(TipoVariazioneMutuo s, SiacDMutuoVariazioneTipo d) {
	}

	public SiacDMutuoVariazioneTipo map(TipoVariazioneMutuo s, Integer enteProprietarioId) {
		return siacDMutuoVariazioneTipoDao.findByCode("mutuoVariazioneTipoCode", s.getCodice(), enteProprietarioId);
	}

}


