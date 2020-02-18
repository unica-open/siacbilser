/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.util.Date;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacTIvaRegistroTotale;
import it.csi.siac.siaccommonser.integration.dao.base.JpaDao;

/**
 * The Class ProgressiviIvaDaoImpl.
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ProgressiviIvaDaoImpl extends JpaDao<SiacTIvaRegistroTotale, Integer> implements ProgressiviIvaDao {
	
	@Override
	public SiacTIvaRegistroTotale create(SiacTIvaRegistroTotale c){
		Date now = new Date();
		
		c.setDataModificaInserimento(now);
		
		c.setUid(null);		
		super.save(c);
		return c;
	}

	@Override
	public SiacTIvaRegistroTotale update(SiacTIvaRegistroTotale d){
		Date now = new Date();
		d.setDataModificaAggiornamento(now);
		
		super.update(d);
		return d;
	}
}
