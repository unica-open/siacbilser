/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.integration.dao;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.integration.entity.SiacTClass;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;
import it.csi.siac.siaccommonser.util.log.LogSrvUtil;
import it.csi.siac.siaccorser.integration.dao.CodificaDao;

// TODO: Auto-generated Javadoc
/**
 * The Class CodificaBilDaoImplTest.
 */
public class CodificaDaoImplTest extends BaseJunit4TestCase {
	
	private final LogSrvUtil l = new LogSrvUtil(getClass());
	
	// XXX: Per eseguire il test è necessario aggiungere l'annotazione @Component sulla classe CodificaDaoImpl
	/** The codifica bil dao. */
	@Autowired
	@Qualifier("codificaDaoImpl")
	private CodificaDao codificaDao;
	
	/**
	 * Test find codifiche by tipo elem bilancio.
	 */
	@Transactional
	@Test
	public void findCodifiche() {
		List<SiacTClass> siacTClasses = codificaDao.findCodifiche(2013, 1);
		l.debug("findCodifiche", "siacTClasses.size(): " + siacTClasses.size());
	}
	
	@Transactional
	@Test
	public void findCodificheByIdPadre() {
		List<SiacTClass> siacTClasses = codificaDao.findCodificheByIdPadre(2013, 1, 5829);
		l.debug("findCodificheByIdPadre", "siacTClasses.size(): " + siacTClasses.size());
		for(SiacTClass c: siacTClasses){
			l.debug("findCodificheByIdPadre", "codice: " + c.getClassifCode() +" - descrizione: " + c.getClassifDesc());
		}
	}
	
	@Transactional
	@Test
	public void findCodificaFamigliaTreeDto() {
		List<SiacTClass> siacTClasses = codificaDao.findCodificaFamigliaTreeDto(2013, 1, "00005");
		l.debug("findCodificaFamigliaTreeDto", "siacTClasses.size(): " + siacTClasses.size());
		for(SiacTClass c: siacTClasses){
			l.debug("findCodificaFamigliaTreeDto", "codice: " + c.getClassifCode() +" - descrizione: " + c.getClassifDesc());
		}
	}

}
