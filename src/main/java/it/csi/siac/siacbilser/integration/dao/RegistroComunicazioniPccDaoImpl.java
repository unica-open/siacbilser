/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.util.Date;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacTRegistroPcc;
import it.csi.siac.siaccommonser.integration.dao.base.JpaDao;


/**
 * The Class RegistroComunicazioniPccDaoImpl.
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RegistroComunicazioniPccDaoImpl extends JpaDao<SiacTRegistroPcc, Integer> implements RegistroComunicazioniPCCDao {
	
	public SiacTRegistroPcc create(SiacTRegistroPcc r){
		Date now = new Date();
		r.setDataModificaInserimento(now);
		
		r.setUid(null);
		super.save(r);
		return r;
	}
	
	@Override
	public SiacTRegistroPcc update(SiacTRegistroPcc r) {
		Date now = new Date();
		r.setDataModificaAggiornamento(now);
		return super.update(r);
	}
	
	@Override
	public void delete(SiacTRegistroPcc entity) {
		Date now = new Date();
		
		SiacTRegistroPcc siacTRegistroPcc = findById(entity.getUid());
		siacTRegistroPcc.setDataCancellazioneIfNotSet(now);
	}

}
