/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.registrocomunicazionipcc;

import java.util.Calendar;
import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.business.service.registrocomunicazionipcc.AggiornaRegistroComunicazioniPCCService;
import it.csi.siac.siacbilser.business.service.registrocomunicazionipcc.EliminaRegistroComunicazioniPCCService;
import it.csi.siac.siacbilser.business.service.registrocomunicazionipcc.InserisciRegistroComunicazioniPCCService;
import it.csi.siac.siacbilser.business.service.registrocomunicazionipcc.RicercaRegistriComunicazioniPCCSubdocumentoService;
import it.csi.siac.siacbilser.integration.dad.RegistroComunicazioniPCCDad;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaRegistroComunicazioniPCC;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaRegistroComunicazioniPCCResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EliminaRegistroComunicazioniPCC;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EliminaRegistroComunicazioniPCCResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisciRegistroComunicazioniPCC;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisciRegistroComunicazioniPCCResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaRegistriComunicazioniPCCSubdocumento;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaRegistriComunicazioniPCCSubdocumentoResponse;
import it.csi.siac.siacfin2ser.model.CausalePCC;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.RegistroComunicazioniPCC;
import it.csi.siac.siacfin2ser.model.StatoDebito;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfin2ser.model.TipoOperazionePCC;

/**
 * The Class RegistroComunicazioniPCCTest.
 */
public class RegistroComunicazioniPCCTest extends BaseJunit4TestCase {
	
	@Autowired
	private RicercaRegistriComunicazioniPCCSubdocumentoService ricercaRegistriComunicazioniPCCSubdocumentoService;
	@Autowired
	private InserisciRegistroComunicazioniPCCService inserisciRegistroComunicazioniPCCService;
	@Autowired
	private AggiornaRegistroComunicazioniPCCService aggiornaRegistroComunicazioniPCCService;
	@Autowired
	private EliminaRegistroComunicazioniPCCService eliminaRegistroComunicazioniPCCService;
	
	@Autowired
	private RegistroComunicazioniPCCDad registroComunicazioniPCCDad;
	
	@Test
	public void ricercaRegistriComunicazioniPCCSubdocumento() {
		RicercaRegistriComunicazioniPCCSubdocumento req = new RicercaRegistriComunicazioniPCCSubdocumento();
		
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		
		SubdocumentoSpesa subdocumentoSpesa = new SubdocumentoSpesa();
		subdocumentoSpesa.setUid(799);
		req.setSubdocumentoSpesa(subdocumentoSpesa);
		
		RicercaRegistriComunicazioniPCCSubdocumentoResponse res = ricercaRegistriComunicazioniPCCSubdocumentoService.executeService(req);
		
		assertNotNull(res);
	}
	
	@Test
	public void inserisciRegistroComunicazioniPCC() {
		InserisciRegistroComunicazioniPCC req = new InserisciRegistroComunicazioniPCC();
		
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		
		RegistroComunicazioniPCC registroComunicazioniPCC = new RegistroComunicazioniPCC();
		req.setRegistroComunicazioniPCC(registroComunicazioniPCC);
		
		registroComunicazioniPCC.setEnte(req.getRichiedente().getAccount().getEnte());
		registroComunicazioniPCC.setSubdocumentoSpesa(create(SubdocumentoSpesa.class, 37579));
		registroComunicazioniPCC.setDocumentoSpesa(create(DocumentoSpesa.class, 32489));
		// CO
//		registroComunicazioniPCC.setTipoOperazionePCC(create(TipoOperazionePCC.class, 8));
		// CS
		registroComunicazioniPCC.setTipoOperazionePCC(create(TipoOperazionePCC.class, 17));
//		registroComunicazioniPCC.setCausalePCC(create(CausalePCC.class, 53));
//		registroComunicazioniPCC.setStatoDebito(create(StatoDebito.class, 17));
		
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 2017);
		cal.set(Calendar.MONTH, Calendar.OCTOBER);
		cal.set(Calendar.DAY_OF_MONTH, 2);
//		
//		registroComunicazioniPCC.setAnnoProvvisorioCassa(2015);
//		registroComunicazioniPCC.setDataEmissioneOrdinativo(cal.getTime());
//		
//		cal.set(Calendar.DAY_OF_MONTH, 31);
//		registroComunicazioniPCC.setDataInvio(cal.getTime());
		
		registroComunicazioniPCC.setDataScadenza(cal.getTime());
		
//		registroComunicazioniPCC.setNumeroOrdinativo(1);
//		registroComunicazioniPCC.setNumeroProvvisorioCassa(1);
		
		InserisciRegistroComunicazioniPCCResponse res = inserisciRegistroComunicazioniPCCService.executeService(req);
		
		assertNotNull(res);
	}
	
	@Test
	public void aggiornaRegistroComunicazioniPCC() {
		AggiornaRegistroComunicazioniPCC req = new AggiornaRegistroComunicazioniPCC();
		
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		
		RegistroComunicazioniPCC registroComunicazioniPCC = new RegistroComunicazioniPCC();
		registroComunicazioniPCC.setUid(1);
		
		// Subdocumento
		SubdocumentoSpesa subdocumentoSpesa = new SubdocumentoSpesa();
		subdocumentoSpesa.setUid(537);
		registroComunicazioniPCC.setSubdocumentoSpesa(subdocumentoSpesa);
		
		// Documento
		DocumentoSpesa documentoSpesa = new DocumentoSpesa();
		documentoSpesa.setUid(453);
		registroComunicazioniPCC.setDocumentoSpesa(documentoSpesa);
		
		// TipoOperazionePCC
		TipoOperazionePCC tipoOperazionePCC = new TipoOperazionePCC();
		tipoOperazionePCC.setUid(1);
		registroComunicazioniPCC.setTipoOperazionePCC(tipoOperazionePCC);
		
		registroComunicazioniPCC.setEnte(getEnteTest());
		
		req.setRegistroComunicazioniPCC(registroComunicazioniPCC);
		
		AggiornaRegistroComunicazioniPCCResponse res = aggiornaRegistroComunicazioniPCCService.executeService(req);
		
		assertNotNull(res);
	}
	
	@Test
	public void eliminaRegistroComunicazioniPCC() {
		EliminaRegistroComunicazioniPCC req = new EliminaRegistroComunicazioniPCC();
		
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		
		RegistroComunicazioniPCC registroComunicazioniPCC = new RegistroComunicazioniPCC();
		registroComunicazioniPCC.setUid(1);
		req.setRegistroComunicazioniPCC(registroComunicazioniPCC);
		
		EliminaRegistroComunicazioniPCCResponse res = eliminaRegistroComunicazioniPCCService.executeService(req);
		
		assertNotNull(res);
	}
	
	@Test
	public void registroComunicazioniPCCDad(){
		registroComunicazioniPCCDad.setEnte(getEnteTest());
		registroComunicazioniPCCDad.setLoginOperazione("TEST");
		
		for(CausalePCC.Value value : CausalePCC.Value.values()){
			CausalePCC causalePCC = registroComunicazioniPCCDad.findCausalePCCByValue(value);
			System.out.println(">>>>>>>>>> CausalePCC "+value + ": " + causalePCC.getCodice() + " [uid: "+causalePCC.getUid()+"]");
		}
		
		for(StatoDebito.Value value : StatoDebito.Value.values()){
			StatoDebito statoDebito = registroComunicazioniPCCDad.findStatoDebitoByValue(value);
			System.out.println(">>>>>>>>>> StatoDebito "+value + ": " + statoDebito.getCodice() + " [uid: "+statoDebito.getUid()+"]");
		}
		
		for(TipoOperazionePCC.Value value : TipoOperazionePCC.Value.values()){
			TipoOperazionePCC tipoOperazione = registroComunicazioniPCCDad.findTipoOperazionePCCByValue(value);
			System.out.println(">>>>>>>>>> TipoOperazionePCC "+value + ": " + tipoOperazione.getCodice() + " [uid: "+tipoOperazione.getUid()+"]");
		}
		
		System.out.println(">>>>>>>>>> Cached!!!!! ");
		
		for(CausalePCC.Value value : CausalePCC.Value.values()){
			CausalePCC causalePCC = registroComunicazioniPCCDad.findCausalePCCByValue(value);
			System.out.println(">>>>>>>>>> CausalePCC "+value + ": " + causalePCC.getCodice() + " [uid: "+causalePCC.getUid()+"]");
		}
		
		for(StatoDebito.Value value : StatoDebito.Value.values()){
			StatoDebito statoDebito = registroComunicazioniPCCDad.findStatoDebitoByValue(value);
			System.out.println(">>>>>>>>>> StatoDebito "+value + ": " + statoDebito.getCodice() + " [uid: "+statoDebito.getUid()+"]");
		}
		
		for(TipoOperazionePCC.Value value : TipoOperazionePCC.Value.values()){
			TipoOperazionePCC tipoOperazione = registroComunicazioniPCCDad.findTipoOperazionePCCByValue(value);
			System.out.println(">>>>>>>>>> TipoOperazionePCC "+value + ": " + tipoOperazione.getCodice() + " [uid: "+tipoOperazione.getUid()+"]");
		}
		
		
	}
	
	
	
}
