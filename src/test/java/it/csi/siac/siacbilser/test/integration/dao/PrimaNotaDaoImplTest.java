/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.integration.dao;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.integration.dad.PrimaNotaDad;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;
import it.csi.siac.siacgenser.model.PrimaNota;
import it.csi.siac.siacgenser.model.StatoOperativoPrimaNota;
import it.csi.siac.siacgenser.model.TipoCausale;

public class PrimaNotaDaoImplTest extends BaseJunit4TestCase {
	@Autowired
	private PrimaNotaDad primaNotaDad;
	@Test
	public void ricercaSinteticaPrimaNotaTest(){
		PrimaNota primaNota = new PrimaNota();
		primaNota.setStatoOperativoPrimaNota(StatoOperativoPrimaNota.PROVVISORIO);
		primaNota.setTipoCausale(TipoCausale.Libera);
		//aggiungere Ente come primo parametro prima di far partire il test
//		ListaPaginata<PrimaNota> ricercaSinteticaPrimeNote = primaNotaDad.ricercaSinteticaPrimeNote(getEnteTest(), getBilancio2015Test(), primaNota , null, null, null, null, null, null, null, new BigDecimal(1.00), getParametriPaginazioneTest());
//		assertNotNull(ricercaSinteticaPrimeNote);
	}
}
