/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business;

import org.junit.Before;
import org.junit.Test;

import it.csi.siac.siacbilser.business.GestioneVoceDiBilancio;
import it.csi.siac.siacbilser.model.VoceDiBilancio;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;

/**
 * The Class GestioneVoceDiBilancioImplTest.
 */
public class GestioneVoceDiBilancioImplTest extends BaseJunit4TestCase {

	/** The gestione voce di bilancio. */
	private GestioneVoceDiBilancio gestioneVoceDiBilancio;

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	@Before
	public void setUp() {
		gestioneVoceDiBilancio = (GestioneVoceDiBilancio) applicationContext
				.getBean("gestioneVoceDiBilancioBean");
	}

	/**
	 * Test.
	 */
	@Test
	public void test() {
		VoceDiBilancio vdb = new VoceDiBilancio();
		vdb.setUid(1);
		gestioneVoceDiBilancio.getAndSave(vdb);
	}

}
