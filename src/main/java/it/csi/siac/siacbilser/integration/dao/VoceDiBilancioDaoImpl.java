/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.util.List;

import javax.persistence.LockModeType;

import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.model.VoceDiBilancio;
import it.csi.siac.siaccorser.integration.dao.BaseDaoImpl;

// TODO: Auto-generated Javadoc
/**
 * Implementazione del DAO per la VoceDiBilancio. ATTENZIONE i Dao sollevano
 * della unchecked exceptions: per catturarle occorre catturare le
 * RuntimeException
 * 
 * @author alagna
 * @version $Id: $
 */
@Transactional
public class VoceDiBilancioDaoImpl extends BaseDaoImpl implements VoceDiBilancioDao {

	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.integration.dao.VoceDiBilancioDao#save(it.csi.siac.siacbilser.model.VoceDiBilancio)
	 */
	@Override
	public VoceDiBilancio save(VoceDiBilancio voceDiBilancio) {
		log.debug("save(VoceDiBilancio)");
		entityManager.persist(voceDiBilancio);
		log.debug(voceDiBilancio.toString());
		return voceDiBilancio;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.integration.dao.VoceDiBilancioDao#findById(int)
	 */
	@Override
	public VoceDiBilancio findById(int id) {
		log.debug("findById(int)");
		return entityManager.find(VoceDiBilancio.class, id);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.integration.dao.VoceDiBilancioDao#findByIdAndLock(int)
	 */
	@Override
	public VoceDiBilancio findByIdAndLock(int id) {
		log.debug("findById(int)");
		return entityManager
				.find(VoceDiBilancio.class, id, LockModeType.PESSIMISTIC_WRITE);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.integration.dao.VoceDiBilancioDao#list()
	 */
	@Override
	public List<VoceDiBilancio> list() {
		log.debug("list()");
		return entityManager.createQuery("select vdb from VoceDiBilancio vdb",
				VoceDiBilancio.class).getResultList();
	}

	

}
