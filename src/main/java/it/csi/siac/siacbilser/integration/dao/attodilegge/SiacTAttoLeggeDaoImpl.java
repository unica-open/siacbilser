/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao.attodilegge;

import java.util.Date;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacTAttoLegge;
import it.csi.siac.siaccommonser.integration.dao.base.JpaDao;

/**
 * The Class SiacTAttoLeggeDaoImpl.
 */
@Component
public class SiacTAttoLeggeDaoImpl extends JpaDao<SiacTAttoLegge, Integer> implements SiacTAttoLeggeDao {
	


	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.integration.dao.attodilegge.SiacTAttoLeggeDao#create(it.csi.siac.siacbilser.integration.entity.SiacTAttoLegge)
	 */
	@Override
	public SiacTAttoLegge create(SiacTAttoLegge attoLegge) {
		Date now = new Date();
		attoLegge.setDataModificaInserimento(now);
	
		super.save(attoLegge);
		
		findById(attoLegge.getUid());
		
		return attoLegge;
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.dao.base.JpaDao#update(java.lang.Object)
	 */
	@Override
	public SiacTAttoLegge update(SiacTAttoLegge attoLegge) {
		Date now = new Date();
		attoLegge.setDataModificaInserimento(now);
		
		super.update(attoLegge);
		
		return attoLegge;
	}
	
}
