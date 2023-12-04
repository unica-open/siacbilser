/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.fattureelettroniche;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.business.service.fatturaelettronica.RicercaDettaglioFatturaElettronicaService;
import it.csi.siac.siacbilser.business.service.fatturaelettronica.RicercaSinteticaFatturaElettronicaService;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;
import it.csi.siac.sirfelser.frontend.webservice.msg.RicercaDettaglioFatturaElettronica;
import it.csi.siac.sirfelser.frontend.webservice.msg.RicercaDettaglioFatturaElettronicaResponse;
import it.csi.siac.sirfelser.frontend.webservice.msg.RicercaSinteticaFatturaElettronica;
import it.csi.siac.sirfelser.frontend.webservice.msg.RicercaSinteticaFatturaElettronicaResponse;
import it.csi.siac.sirfelser.model.FatturaFEL;

// TODO: Auto-generated Javadoc
/**
 * The Class FatturaFelTest.
 */
public class FatturaFelTest extends BaseJunit4TestCase {
	
	@Autowired
	private RicercaDettaglioFatturaElettronicaService ricercaDettaglioFatturaElettronicaService;
	
	@Autowired
	private RicercaSinteticaFatturaElettronicaService ricercaSinteticaFatturaElettronicaService;
	
	
	@Test
	public void ricercaDettaglioFattura(){
		
		RicercaDettaglioFatturaElettronica req = new RicercaDettaglioFatturaElettronica();
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		
		FatturaFEL fat = new FatturaFEL();
		fat.setIdFattura(556);
//		fat.setIdFattura(5);
		fat.setEnte(getEnteTest());
		req.setFatturaFEL(fat);
		req = marshallUnmarshall(req);
		
		RicercaDettaglioFatturaElettronicaResponse res = ricercaDettaglioFatturaElettronicaService.executeService(req);
		
		res = marshallUnmarshall(res);
		
		assertNotNull(res);
	}
	
	
	/**
	 * Ricerca dettaglio documento spesa.
	 */
	@Test
	public void ricercaSinteticaFattura(){
		
		RicercaSinteticaFatturaElettronica req = new RicercaSinteticaFatturaElettronica();
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		req.setParametriPaginazione(getParametriPaginazioneTest());
		
		
		FatturaFEL fat = new FatturaFEL();
		fat.setEnte(getEnteTest());
		fat.setNumero("0010114978201404");
//		fat.setTipoDocumentoFEL(TipoDocumentoFEL.FATTURA);
//		fat.setCodiceDestinatario("001");
//		PrestatoreFEL prestatore = new PrestatoreFEL();
//		prestatore.setCodicePrestatore("STRSFO75T54L219Q");
//		fat.setPrestatore(prestatore);
//		
		
//		Date dataFatturaDa = new GregorianCalendar(2015, 2, 1).getTime();
//		req.setDataFatturaDa(dataFatturaDa);
//		Date dataFatturaA  = new GregorianCalendar(2015, 11, 31).getTime();
//		req.setDataFatturaA(dataFatturaA);
//		fat.setStatoAcquisizioneFEL(StatoAcquisizioneFEL.DA_ACQUISIRE);
		req.setFatturaFEL(fat);
//		req = marshallUnmarshall(req);
		
		
		RicercaSinteticaFatturaElettronicaResponse res = ricercaSinteticaFatturaElettronicaService.executeService(req);
		
//		res = marshallUnmarshall(res);
		
		assertNotNull(res);
	}
	
}
