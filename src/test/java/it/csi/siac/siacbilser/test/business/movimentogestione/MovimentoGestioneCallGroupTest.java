/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.movimentogestione;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.business.service.base.ServiceExecutor;
import it.csi.siac.siacbilser.business.service.documento.MovimentoGestioneServiceCallGroup;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;

// TODO: Auto-generated Javadoc
/**
 * The Class GruppoAttivitaIvaVTTest.
 */
public class MovimentoGestioneCallGroupTest extends BaseJunit4TestCase {
	
	@Autowired
	protected ServiceExecutor serviceExecutor;
	protected MovimentoGestioneServiceCallGroup mgscg =  new MovimentoGestioneServiceCallGroup(serviceExecutor,getRichiedenteByProperties("consip","regp"),getEnteTest(), getBilancio2015Test());;
	
	
	
	
	/**
	 * Inserisci gruppo attivita.
	 */
	@Test
	public void ricercaImpegnoPerChiaveCached() {
			
//		serviceExecutor.setServiceName(mgscg.getClass().getSimpleName());
//		
//		Impegno impegno = new Impegno();
//		impegno.setAnnoMovimento(2015);
//		impegno.setNumero(BigDecimal.TEN);
//		
//		RicercaImpegnoPerChiaveResponse resRIPC = mgscg.ricercaImpegnoPerChiaveCached(impegno);
	}
		
}