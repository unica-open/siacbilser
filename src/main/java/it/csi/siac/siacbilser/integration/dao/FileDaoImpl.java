/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.util.Date;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.base.ExtendedJpaDao;
import it.csi.siac.siacbilser.integration.entity.SiacTFile;

/**
 * The Class FileDaoImpl.
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class FileDaoImpl extends ExtendedJpaDao<SiacTFile, Integer> implements FileDao {
	
	@Override
	public SiacTFile findById(Integer id) {
		SiacTFile siacTFile = super.findById(id);
		return siacTFile;
	}
	
	@Override
	public SiacTFile create(SiacTFile c){
		Date now = new Date();
		c.setDataModificaInserimento(now);
		
		c.setUid(null);
		super.save(c);
		return c;
	}

	@Override
	public SiacTFile update(SiacTFile d){
		Date now = new Date();
		d.setDataModificaAggiornamento(now);
		super.update(d);
		return d;
	}

}
