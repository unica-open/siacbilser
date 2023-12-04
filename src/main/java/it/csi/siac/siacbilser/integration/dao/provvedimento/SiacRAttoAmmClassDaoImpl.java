/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao.provvedimento;

import java.util.Date;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacRAttoAmmClass;
import it.csi.siac.siaccommonser.integration.dao.base.JpaDao;

/**
 * The Class SiacRAttoAmmClassDaoImpl.
 */
@Component
public class SiacRAttoAmmClassDaoImpl extends JpaDao<SiacRAttoAmmClass, Integer> implements SiacRAttoAmmClassDao {

	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.integration.dao.provvedimento.SiacRAttoAmmClassDao#create(it.csi.siac.siacbilser.integration.entity.SiacRAttoAmmClass)
	 */
	@Override
	public SiacRAttoAmmClass create(SiacRAttoAmmClass attoLegge) {
		Date now = new Date();
		attoLegge.setDataModificaInserimento(now);
		attoLegge.setUid(null);
	
		super.save(attoLegge);
		
		findById(attoLegge.getUid());
		
		return attoLegge;
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.dao.base.JpaDao#update(java.lang.Object)
	 */
	@Override
	public SiacRAttoAmmClass update(SiacRAttoAmmClass attoLegge) {
		Date now = new Date();
		attoLegge.setDataModificaInserimento(now);
		
		super.update(attoLegge);
		
		return attoLegge;
	}
	
}
