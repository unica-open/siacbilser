/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.integration.dao;

import java.math.BigInteger;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import it.csi.siac.siacbilser.integration.dao.VoceDiBilancioDao;
import it.csi.siac.siacbilser.model.VoceDiBilancio;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;

// TODO: Auto-generated Javadoc
/**
 * The Class VoceDiBilancioDaoImplTest.
 */
public class VoceDiBilancioDaoImplTest extends BaseJunit4TestCase {

	/** The voce di bilancio dao. */
	private VoceDiBilancioDao voceDiBilancioDao;

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	@Before
	public void setUp() {
		voceDiBilancioDao = (VoceDiBilancioDao) applicationContext
				.getBean("voceDiBilancioDaoBean");
	}

	/**
	 * Test save.
	 */
	@Test
	public void testSave() {
		try {
			VoceDiBilancio vdb = new VoceDiBilancio();
			vdb.setCodice("sss");
			vdb.setDescrizione("descrizione");
			vdb.setImporto(new BigInteger("10"));
			voceDiBilancioDao.save(vdb);
		} catch (Exception e) {
			log.error("errore in testSave():", e);
			fail();
		}
	}

	/**
	 * Test find by id.
	 */
	@Test
	public void testFindById() {
		try {
			VoceDiBilancio vdb = voceDiBilancioDao.findById(voceDiBilancioDao
					.list().get(0).getUid());
			assertNotNull("Voce di Bilancio non trovata", vdb);
		} catch (Exception e) {
			log.error("errore in testFindById():", e);
			fail();
		}
	}

	/**
	 * Test list.
	 */
	@Test
	public void testList() {
		try {
			List<VoceDiBilancio> voci = voceDiBilancioDao.list();
			log.debug("testList", voci.toString());
			assertTrue("Nessuna VoceDiBilancio trovata", voci.size() > 0);
		} catch (Exception e) {
			log.error("errore in testList():", e);
			fail();
		}
	}

}
