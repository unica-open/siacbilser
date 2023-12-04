/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.util.Date;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.integration.entity.SiacRDocOnere;
import it.csi.siac.siaccommonser.integration.dao.base.JpaDao;

// TODO: Auto-generated Javadoc
/**
 * The Class DettaglioOnereDaoImpl.
 */
@Component
@Transactional
public class DettaglioOnereDaoImpl extends JpaDao<SiacRDocOnere, Integer> implements DettaglioOnereDao {

	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.integration.dao.DettaglioOnereDao#create(it.csi.siac.siacbilser.integration.entity.SiacRDocOnere)
	 */
	public SiacRDocOnere create(SiacRDocOnere c) {
		Date now = new Date();
		c.setDataModificaInserimento(now);
		
		c.setUid(null);		
		super.save(c);
		return c;
		
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.dao.base.JpaDao#update(java.lang.Object)
	 */
	public SiacRDocOnere update(SiacRDocOnere c) {
		
		Date now = new Date();
		c.setDataModificaAggiornamento(now);		
		
		super.update(c);
		return c;
		
	}
	
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.dao.base.JpaDao#delete(java.lang.Object)
	 */
	@Override
	public void delete(SiacRDocOnere c) {
		Date now = new Date();
		c.setDataCancellazioneIfNotSet(now);
		
		super.update(c);		
	}



}
