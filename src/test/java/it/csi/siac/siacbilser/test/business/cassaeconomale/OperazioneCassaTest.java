/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.cassaeconomale;

import java.util.Arrays;
import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.integration.dad.OperazioneDiCassaDad;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;
import it.csi.siac.siaccecser.model.CassaEconomale;
import it.csi.siac.siaccecser.model.OperazioneCassa;
import it.csi.siac.siaccecser.model.StatoOperativoOperazioneCassa;
import it.csi.siac.siaccecser.model.TipoOperazioneCassa;
import it.csi.siac.siaccommonser.util.log.LogSrvUtil;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Richiedente;

public class OperazioneCassaTest extends BaseJunit4TestCase {
	@Autowired
	private OperazioneDiCassaDad operazioneDiCassaDad;
	
	
	private LogSrvUtil log = new LogSrvUtil(getClass());
	/**
	 * Ottiene il bilancio 2014 con id 6.
	 *
	 * @return the bilancio test
	 */
	protected Bilancio getBilancio2014Test() {
		Bilancio bilancio = new Bilancio();
		bilancio.setUid(6);
		bilancio.setAnno(2014);
		return bilancio;
	}
	
	/**
	 * Ottiene il bilancio 2015 con id 16.
	 *
	 * @return the bilancio test
	 */
	protected Bilancio getBilancio2015Test() {
		Bilancio bilancio = new Bilancio();
		bilancio.setUid(16);
		bilancio.setAnno(2015);
		return bilancio;
	}
	
	private Richiedente getRichiedenteByEnvironment (boolean isSviluppo) {
		return  isSviluppo? getRichiedenteByProperties("consip","regp") : getRichiedenteTest("AAAAAA00A11E000M",71,15);
	}
	
	private Bilancio getBilancioByEnvironment (boolean isSviluppo) {
		return  isSviluppo? getBilancio2015Test() : getBilancioTest(46, 2015);
	}
	
	private Ente getEnteByEnvironment (boolean isSviluppo) {
		return  isSviluppo? getEnteTest() : getEnteTest(15);
	}
	
	private CassaEconomale getCassaEconomaleByEnvironment (boolean isSviluppo) {
		CassaEconomale ce = new CassaEconomale();
		ce.setUid((isSviluppo ? 2:13));
		return ce;
		
	}
	
	@Test
	public void ricercaSintetica() {
		Richiedente rich = getRichiedenteByProperties("consip","regp");
		operazioneDiCassaDad.setEnte(rich.getAccount().getEnte());
		operazioneDiCassaDad.setLoginOperazione(rich.getOperatore().getCodiceFiscale());
		operazioneDiCassaDad.ricercaSinteticaOperazioneDiCassaPerPeriodo(getBilancioByProperties("consip", "regp", 2019), 
				create(CassaEconomale.class, 16), 
				new Date(), 
				new Date(), 
				create(TipoOperazioneCassa.class, 87), 
				null,
				Arrays.asList(StatoOperativoOperazioneCassa.ANNULLATO), 
				getParametriPaginazioneTest());
		
		
	}
	
	
	@Test
	public void aggStatoOpCassa() {
		operazioneDiCassaDad.setEnte(getEnteTest());
		operazioneDiCassaDad.setLoginOperazione(getRichiedenteByProperties("consip","regp").getOperatore().getCodiceFiscale());
		OperazioneCassa op = new OperazioneCassa();
	
		op.setUid(11);
		op.setStatoOperativoOperazioneCassa(StatoOperativoOperazioneCassa.DEFINITIVO);
		operazioneDiCassaDad.aggiornaStatoOperazioneCassa(op);
		
		
	}
}
