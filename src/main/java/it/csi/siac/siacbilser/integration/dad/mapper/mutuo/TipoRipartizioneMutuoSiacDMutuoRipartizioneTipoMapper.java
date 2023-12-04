/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad.mapper.mutuo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.mutuo.SiacDMutuoRipartizioneTipoDao;
import it.csi.siac.siacbilser.integration.entity.SiacDMutuoRipartizioneTipo;
import it.csi.siac.siacbilser.model.mutuo.TipoRipartizioneMutuo;
import it.csi.siac.siaccommon.util.mapper.BaseMapper;


@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class TipoRipartizioneMutuoSiacDMutuoRipartizioneTipoMapper extends BaseMapper<TipoRipartizioneMutuo, SiacDMutuoRipartizioneTipo> {
	
	@Autowired private SiacDMutuoRipartizioneTipoDao siacDMutuoRipartizioneTipoDao;
	
	@Override
	public void map(TipoRipartizioneMutuo s, SiacDMutuoRipartizioneTipo d) {
	}

	public SiacDMutuoRipartizioneTipo map(TipoRipartizioneMutuo s, Integer enteProprietarioId) {
		return siacDMutuoRipartizioneTipoDao.findByCode("mutuoRipartizioneTipoCode", s.getCodice(), enteProprietarioId);
	}

}


