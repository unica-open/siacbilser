/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.integration.dao;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.integration.dao.CodificaBilDao;
import it.csi.siac.siacbilser.integration.dao.SiacTBilElemRepository;
import it.csi.siac.siacbilser.integration.entity.SiacRClassFamTree;
import it.csi.siac.siacbilser.integration.entity.SiacTClass;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;
import it.csi.siac.siaccommonser.util.log.LogSrvUtil;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;

// TODO: Auto-generated Javadoc
/**
 * The Class CodificaBilDaoImplTest.
 */
public class CodificaBilDaoImplTest extends BaseJunit4TestCase {
	
	private final LogSrvUtil l = new LogSrvUtil(getClass());
	
	/** The codifica bil dao. */
	private CodificaBilDao codificaBilDao;

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	@Before
	public void setUp() {
		codificaBilDao = (CodificaBilDao) applicationContext.getBean("codificaBilDao");
	}

	
	/**
	 * Test find codifiche by tipo elem bilancio.
	 */
	@Transactional
	@Test
	public void testFindCodificheByTipoElemBilancio() {
		List<SiacTClass> siacTClasses = codificaBilDao.findCodificheByTipoElemBilancio(1, 1, "CAP-UG");
		assertTrue("lista vuota", !siacTClasses.isEmpty());
		
		Collections.sort(siacTClasses, new Comparator<SiacTClass>() {
			@Override
			public int compare(SiacTClass o1, SiacTClass o2) {
				return o1.getSiacDClassTipo().getClassifTipoCode().compareTo(o2.getSiacDClassTipo().getClassifTipoCode());
			}
		});
		
		l.debug("testFindCodificheByTipoElemBilancio", "siacTClasses.size: " + siacTClasses.size());
		for(SiacTClass c: siacTClasses){
			l.debug("findCodificheByIdPadre", "tipo: " + TipologiaClassificatore.fromCodice(c.getSiacDClassTipo().getClassifTipoCode()).name() +
					" - codice: " + c.getClassifCode() +" - descrizione: " + c.getClassifDesc());
		}
	}
	
	/**
	 * Test find codifiche con livello by tipo elem bilancio.
	 */
	@Transactional
	@Test
	public void testFindCodificheConLivelloByTipoElemBilancio() {
		try {
			List<SiacTClass> list = codificaBilDao.findCodificheConLivelloByTipoElemBilancio(1, 1, "CAP-UP");
			assertTrue("lista vuota", !list.isEmpty());
			
			Collections.sort(list, new Comparator<SiacTClass>() {
				@Override
				public int compare(SiacTClass o1, SiacTClass o2) {
					return o1.getSiacDClassTipo().getClassifTipoCode().compareTo(o2.getSiacDClassTipo().getClassifTipoCode());
				}
			});
			
			l.debug("testFindCodificheConLivelloByTipoElemBilancio", "siacTClasses.size: " + list.size());
			for(SiacTClass c: list){
				l.debug("testFindCodificheConLivelloByTipoElemBilancio",
					"tipo: " + c.getSiacDClassTipo().getClassifTipoCode() +
						" - codice: " + c.getClassifCode() +" - descrizione: " + c.getClassifDesc());
			}
			
		} catch (Exception e) {
			log.error("errore in testFindCodificheConLivelloByTipoElemBilancio():", e);
			fail();
		}
	}
	
	/**
	 * Test find tree piano dei conti dto.
	 */
	@Transactional
	@Test
	public void testFindTreePianoDeiContiDto() {
		final String methodName = "testFindTreePianoDeiContiDto";
		try {
			
			List<SiacTClass> siacTClasses = codificaBilDao.findTreeByCodiceFamiglia(2013, 1, "00008", 5952, false);
			assertTrue("lista vuota", !siacTClasses.isEmpty());
			
			for (SiacTClass s : siacTClasses) {
				l.debug(methodName, "Uid: " + s.getUid() + " Codice: " + s.getClassifCode() + " Descrizione: " + s.getClassifDesc());
				printFigli(s.getSiacRClassFamTreesPadre());
			}			
		} catch (Exception e) {
			log.error("errore in testFindTreePianoDeiContiDto():", e);
			fail();
		}
	}
	
	private void printFigli(List<SiacRClassFamTree> figli) {
		final String methodName = "printFigli";
		for(SiacRClassFamTree r : figli) {
			SiacTClass figlio = r.getSiacTClassFiglio();
			l.debug(methodName, "Uid: " + figlio.getUid() + " Codice: " + figlio.getClassifCode() + " Descrizione: " + figlio.getClassifDesc());
			printFigli(figlio.getSiacRClassFamTreesPadre());
		}
	}


	/**
	 * Test find tree piano dei conti dto.
	 */
	@Transactional
	@Test
	public void testFindTreeSiopeSpesaDto() {
		final String methodName = "testFindTreeSiopeSpesaDto";
		try {
			
			List<SiacTClass> siacTClasses = codificaBilDao.findTreeByCodiceFamiglia(2013, 1, "00017", 7433, false);
			assertTrue("lista vuota", !siacTClasses.isEmpty());
			
			for (SiacTClass s : siacTClasses) {
				l.debug(methodName, "Uid: " + s.getUid() + " Codice: " + s.getClassifCode() + " Descrizione: " + s.getClassifDesc());
				printFigli(s.getSiacRClassFamTreesPadre());
			}			
		} catch (Exception e) {
			log.error("errore in testFindTreeSiopeSpesaDto():", e);
			fail();
		}
	}
	
	
	@Autowired
	private SiacTBilElemRepository siacTBilElemRepository;
	
	/**
	 * Test find tree piano dei conti dto.
	 */
	@Transactional
	@Test
	public void testFindSiopeSpesaDto() {
		final String methodName = "testFindSiopeSpesaDto";
		try {
			
			List<SiacTClass> list = siacTBilElemRepository.ricercaClassificatoriByClassFam(6, "00017");
			assertTrue("lista vuota", !list.isEmpty());
			
			for (SiacTClass codificaDto : list) {
				log.info(methodName, codificaDto.getUid());
				log.info(methodName, codificaDto.getClassifDesc());
//				for (CodificaDto figlio : codificaDto.getFigli()) {
//					log.info(methodName, "  " + figlio.getUid());
//					log.info(methodName, "  " + figlio.getDescrizione());
//				}
			}			
		} catch (Exception e) {
			log.error("errore in testFindSiopeSpesaDto():", e);
			fail();
		}
	}

}
