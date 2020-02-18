/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.backoffice;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.business.service.backoffice.ModificaCigBackofficeService;
import it.csi.siac.siacbilser.frontend.webservice.msg.ModificaCigBackoffice;
import it.csi.siac.siacbilser.frontend.webservice.msg.ModificaCigBackofficeResponse;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.siopeplus.SiopeAssenzaMotivazione;
import it.csi.siac.siacfinser.model.siopeplus.SiopeTipoDebito;


public class ModificaCigBackofficeTest extends BaseJunit4TestCase {

	@Autowired
	private ModificaCigBackofficeService modificaCigBackofficeService;


	/*
	 *  out of memory risolto tramite opzioni JRE di Eclipse
	 *  Window>Preferences>Java/Installed JREs
	 */

	@Test
	public void testModifica() {

		ModificaCigBackoffice request = new ModificaCigBackoffice();
		request.setRichiedente(getRichiedenteByProperties("consip", "regp"));

		SiopeAssenzaMotivazione siopeAss = new SiopeAssenzaMotivazione();
		siopeAss.setUid(463);
		
		Impegno impegno = new Impegno();
		impegno.setUid(120826);
		impegno.setCig("3456789012");
//		impegno.setSiopeAssenzaMotivazione(siopeAss);
		
		/**
		 * TipoDebito 
		 * COMMERCIALE = 1
		 * NON_COMMERCIALE = 2
		 */
		SiopeTipoDebito tipoDebito = new SiopeTipoDebito();
		tipoDebito.setUid(2);
		impegno.setSiopeTipoDebito(tipoDebito);

		request.setImpegno(impegno);
//		request.setTipoModifica(ModificaCigBackoffice.TipoModificaBackofficeCig.DOCUMENTI_QUOTE);
		request.setTipoModifica(ModificaCigBackoffice.TipoModificaBackofficeCig.QUOTE_E_LIQUIDAZIONI_SENZA_ORDINATIVI_COLLEGATI);
//		request.setNumeroRemedy("INC00000000PROVA");

		ModificaCigBackofficeResponse response = modificaCigBackofficeService.executeService(request);
		
		assertNotNull(response);
//		assertTrue(response.getEsitoModifica() == 0);
		assertEquals(Esito.SUCCESSO, response.getEsito());
		
	}
	
}
