/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.integration.dao;

import org.junit.Before;
import org.junit.Test;

import it.csi.siac.siacbilser.test.BaseJunit4TestCase;

// TODO: Auto-generated Javadoc
/**
 * The Class CapitoloUscitaGestoneDaoImplTest.
 */
public class CapitoloUscitaGestoneDaoImplTest extends BaseJunit4TestCase {
	//private CapitoloUscitaGestoneDao capitoloUscitaGestoneDao;

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	@Before
	public void setUp() {
		//capitoloUscitaGestoneDao = (CapitoloUscitaGestoneDao) applicationContext
		//		.getBean("capitoloUscitaGestoneDao");
	}

	/**
	 * Test insert.
	 */
	@Test
	public void testInsert() {
		try {
			//CapitoloUscitaGestoneDto cup = new CapitoloUscitaGestoneDto();

			
			//cup = capitoloUscitaGestoneDao.create(cup);

			//assertNotNull("CapitoloUscitaGestone non inserito", cup.getUid());
		} catch (Exception e) {
			log.error("errore in testFindById():", e);
			fail();
		}
	}

	/**
	 * Test update.
	 */
	@Test
	public void testUpdate() {
		try {
//			CapitoloUscitaGestoneDto cup = capitoloUscitaGestoneDao					.findById(9458);
//			
//			cup.setDescrizione("TEST 2");
//
//			capitoloUscitaGestoneDao.create(cup);
//
//			assertNotNull("CapitoloUscitaGestone non trovata", cup);
		} catch (Exception e) {
			log.error("errore in testFindById():", e);
			fail();
		}
	}

	/**
	 * Test find by id.
	 */
	@Test
	public void testFindById() {
		try {

//			CapitoloUscitaGestoneDto cup = capitoloUscitaGestoneDao
//					.findById(9458);
//
//			assertNotNull("CapitoloUscitaGestone non trovata", cup);
		} catch (Exception e) {
			log.error("errore in testFindById():", e);
			fail();
		}
	}

	// @Test
	// public void testSave()
	// {
	// try
	// {
	// CapitoloUscitaGestone cup = new CapitoloUscitaGestone();
	//
	// cup.setAnnoCapitolo(2013);
	// cup.setAnnoCreazioneCapitolo(2012);
	// cup.setDataAnnullamento(new Date());
	// cup.setDataCreazione(new Date());
	// cup.setDataModifica(new Date());
	// cup.setDescrizione("descrizione sdsdasd");
	// cup.setDisponibilitaVariare(9.22);
	// cup.setExAnnoCapitolo(2010);
	// cup.setExArticolo(3);
	// cup.setExCapitolo(1);
	// cup.setFlagAssegnabile(true);
	// cup.setFlagFondoSvalutazioneCred(true);
	// cup.setFlagPerMemoria(true);
	// cup.setFlagRilevanteIva(true);
	// cup.setFlagTrasferimentiOrgComunitari(true);
	// cup.setFondoPluriennaleVinc(9983.4);
	// cup.setFondoPluriennaleVincPrec(734.32);
	// cup.setFunzDelegateRegione(true);
	// cup.setNote("note asdfsd");
	// cup.setNumeroArticolo(88);
	// cup.setNumeroCapitolo(65);
	// cup.setStanziamento(88.34);
	// cup.setStanziamentoAnnoPrec(743.21);
	// cup.setStanziamentoCassa(6.3);
	// cup.setStanziamentoCassaAnnoPrec(6.4);
	// cup.setStanziamentoCassaIniziale(6.5);
	// cup.setStanziamentoIniziale(7.0);
	// cup.setStanziamentoProposto(8.3);
	// cup.setStanziamentoResAnnoPrec(1.22);
	// cup.setStanziamentoResiduo(5.33);
	// cup.setStanziamentoResiduoIniziale(8.03);
	//
	// capitoloUscitaGestoneDao.save(cup);
	// }
	// catch (Exception e)
	// {
	// log.error("errore in testSave():", e);
	// fail();
	// }
	// }
}
