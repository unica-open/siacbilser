/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.integration.dao.VoceDiBilancioDao;
import it.csi.siac.siacbilser.model.VoceDiBilancio;
import it.csi.siac.siaccommon.util.log.LogUtil;
import it.csi.siac.siaccorser.business.BaseGestioneImpl;

// TODO: Auto-generated Javadoc
/**
 * Implementazione del servizio di gestione della voce di Bilancio.
 *
 * @author alagna
 * @version $Id: $
 */
@Component
@Transactional(readOnly = false)
public class GestioneVoceDiBilancioImpl extends BaseGestioneImpl implements GestioneVoceDiBilancio {
	
	/** The log. */
	private LogUtil log = new LogUtil(this.getClass());

	/** The voce di bilancio dao. */
	private VoceDiBilancioDao voceDiBilancioDao;

	/**
	 * Provo qui il lock (vd pag 466 hibernate persistence).
	 *
	 * @param voceDiBilancio the voce di bilancio
	 * @return the and save
	 */
	@Override
	public VoceDiBilancio getAndSave(VoceDiBilancio voceDiBilancio) {
		final String methodName = "getAndSave";

		VoceDiBilancio vdb = voceDiBilancioDao.findByIdAndLock(voceDiBilancio
				.getUid());
		vdb.setDescrizione(System.currentTimeMillis() + "");
		log.debug(methodName, "prima dello sleep");
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			log.error("getAndSave", "",e);
		}
		log.debug(methodName, "dopo dello sleep");
		voceDiBilancioDao.save(vdb);
		return vdb;
	}

	/**
	 * Sets the voce di bilancio dao.
	 *
	 * @param voceDiBilancioDao the new voce di bilancio dao
	 */
	public void setVoceDiBilancioDao(VoceDiBilancioDao voceDiBilancioDao) {
		this.voceDiBilancioDao = voceDiBilancioDao;
	}

}
