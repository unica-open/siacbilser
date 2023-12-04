/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.stampa;

import java.math.BigDecimal;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.integration.dad.ProgressiviIvaDad;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;
import it.csi.siac.siaccommonser.util.log.LogSrvUtil;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siacfin2ser.model.AliquotaIva;
import it.csi.siac.siacfin2ser.model.Periodo;
import it.csi.siac.siacfin2ser.model.ProgressiviIva;
import it.csi.siac.siacfin2ser.model.RegistroIva;

/**
 * The Class ProgressiviIvaAMTest.
 */
public class ProgressiviIvaTest extends BaseJunit4TestCase {
	
	/** The ente dad. */
	@Autowired
	private ProgressiviIvaDad progressiviIvaDad;
	
	private LogSrvUtil l = new LogSrvUtil(getClass());
	
	@Test
	public void getProgressiviIvaByUid() {
		ProgressiviIva progressiviIva = progressiviIvaDad.findProgressiviIvaByUid(4);
		assertNotNull("ProgressiviIva is null", progressiviIva);
		l.logXmlTypeObject(progressiviIva, "ProgressiviIva");
	}
	
	@Test
	public void getProgressiviIvaByRegistroAliquotaPeriodoAnnoAndEnte() {
		final String methodName = "getProgressiviIvaByRegistroAliquotaPeriodoAnnoAndEnte";
		RegistroIva registroIva = new RegistroIva();
		registroIva.setUid(6);
		
		AliquotaIva aliquotaIva = new AliquotaIva();
		aliquotaIva.setUid(1);
		
		Periodo periodo = Periodo.GENNAIO_GIUGNO;
		
		Integer anno = 2013;
		
		final String progressiviIvaIdentifier = "RI" + Integer.toString(registroIva.getUid()) + "_AI" + Integer.toString(aliquotaIva.getUid()) +
				"_P" + periodo.getCodice() + "_AE" + anno;
		l.error(methodName, "Identificativo del progressivo: " + progressiviIvaIdentifier);
		
		Ente ente = getEnteTest();
		
		progressiviIvaDad.setEnte(ente);
		ProgressiviIva pi = progressiviIvaDad.findProgressiviIvaByRegistroAndAliquotaIvaAndPeriodoAndAnnoAndEnte(registroIva, aliquotaIva, periodo, anno);
		assertTrue("La lista è vuota", pi!=null);
		l.logXmlTypeObject(pi, "Lista ProgressiviIva");
	}
	
	@Test
	public void inserisceProgressiviIva() {
		final String methodName = "inserisceProgressiviIva";
		RegistroIva registroIva = new RegistroIva();
		registroIva.setUid(6);
		
		AliquotaIva aliquotaIva = new AliquotaIva();
		aliquotaIva.setUid(1);
		
		Periodo periodo = Periodo.GENNAIO_GIUGNO;
		
		Integer anno = 2013;
		
		final String progressiviIvaIdentifier = "RI" + Integer.toHexString(registroIva.getUid()) + "_AI" + Integer.toHexString(aliquotaIva.getUid()) +
				"_P" + periodo.getCodice() + "_AE" + anno;
		l.error(methodName, "Identificativo del progressivo: " + progressiviIvaIdentifier);
		
		Ente ente = getEnteTest();
		
		ProgressiviIva progressiviIva = new ProgressiviIva();
		progressiviIva.setAliquotaIva(aliquotaIva);
		progressiviIva.setAnnoEsercizio(anno);
		progressiviIva.setEnte(ente);
		progressiviIva.setPeriodo(periodo);
		progressiviIva.setRegistroIva(registroIva);
		progressiviIva.setTotaleImponibileDefinitivo(BigDecimal.TEN);
		progressiviIva.setTotaleIvaIndetraibileProvvisorio(BigDecimal.ONE);
		
		progressiviIvaDad.setLoginOperazione(getRichiedenteByProperties("consip","regp").getOperatore().getCodiceFiscale());
		progressiviIvaDad.setEnte(ente);
		progressiviIvaDad.inserisciProgressiviIva(progressiviIva);
		assertTrue("Uid non assegnato", progressiviIva.getUid() != 0);
		l.logXmlTypeObject(progressiviIva, "ProgressiviIva");
	}
	
	@Test
	public void aggiornaProgressiviIva() {
		Ente ente = getEnteTest();
		
		ProgressiviIva progressiviIva = progressiviIvaDad.findProgressiviIvaByUid(4);
		progressiviIva.setTotaleImponibileProvvisorio(new BigDecimal("250000"));
		
		progressiviIvaDad.setLoginOperazione(getRichiedenteByProperties("consip","regp").getOperatore().getCodiceFiscale());
		progressiviIvaDad.setEnte(ente);
		progressiviIvaDad.aggiornaProgressiviIva(progressiviIva);
		assertTrue("Uid non assegnato", progressiviIva.getUid() != 0);
		l.logXmlTypeObject(progressiviIva, "ProgressiviIva");
	}
	
}
