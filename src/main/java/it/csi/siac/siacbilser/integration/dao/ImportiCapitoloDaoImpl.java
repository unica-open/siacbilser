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
import it.csi.siac.siacbilser.integration.entity.SiacTBilElemDet;

/**
 * The Class ImportiCapitoloDaoImpl.
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ImportiCapitoloDaoImpl extends ExtendedJpaDao<SiacTBilElemDet, Integer> implements ImportiCapitoloDao {
	
	@Override
	public SiacTBilElemDet create(SiacTBilElemDet c) {
		Date now = new Date();
		
		c.setDataModificaInserimento(now);
		c.setUid(null);
		
		super.save(c);
		return c;
	}

	@Override
	public SiacTBilElemDet update(SiacTBilElemDet d){
		Date now = new Date();
		d.setDataModificaAggiornamento(now);
		
		super.update(d);
		return d;
	}
}
