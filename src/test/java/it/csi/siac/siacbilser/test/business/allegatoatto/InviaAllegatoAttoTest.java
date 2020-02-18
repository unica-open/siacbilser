/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.allegatoatto;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.business.service.allegatoatto.AttiliqStartServletInvoker;
import it.csi.siac.siacbilser.business.service.allegatoatto.InviaAllegatoAttoAsyncService;
import it.csi.siac.siacbilser.business.service.allegatoatto.InviaAllegatoAttoService;
import it.csi.siac.siacbilser.integration.dad.AllegatoAttoDad;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;
import it.csi.siac.siaccecser.frontend.webservice.msg.InviaAllegatoAtto;
import it.csi.siac.siaccecser.frontend.webservice.msg.InviaAllegatoAttoResponse;
import it.csi.siac.siaccommon.util.JAXBUtility;
import it.csi.siac.siaccorser.frontend.webservice.msg.AsyncServiceRequestWrapper;
import it.csi.siac.siaccorser.frontend.webservice.msg.AsyncServiceResponse;
import it.csi.siac.siaccorser.model.Account;
import it.csi.siac.siaccorser.model.Azione;
import it.csi.siac.siaccorser.model.AzioneRichiesta;
import it.csi.siac.siaccorser.model.ServiceRequest;
import it.csi.siac.siaccorser.model.file.File;
import it.csi.siac.siacfin2ser.model.AllegatoAtto;

/**
 * The Class AllegatoAttoDLTest.
 */
public class InviaAllegatoAttoTest extends BaseJunit4TestCase {
	
	@Autowired
	private InviaAllegatoAttoService inviaAllegatoAttoService;
	
	@Autowired
	private AttiliqStartServletInvoker attiliqStartServletInvoker;
		
	@Autowired
	private AttiliqStartServletInvoker assi;
	
	@Autowired
	private AllegatoAttoDad allegatoAttoDad;
	
	
	@Test
	public void testAttiliqStartServletInvokerConfiguration() {
		// Nothing?
	}
	
	@Test
	public void testAllegatoAttoDad(){
		final String methodName = "testAllegatoAttoDad";
		AllegatoAtto allegatoAtto = new AllegatoAtto();
		allegatoAtto.setUid(11);
		
		Long countDocumentiConRitenute = allegatoAttoDad.countDocumentiConRitenute(allegatoAtto);
		Long countFattureNonALGoCCN = allegatoAttoDad.countFattureNonALGoCCN(allegatoAtto);
		Long countQuoteConSplitReverseIstituzionaleOCommerciale = allegatoAttoDad.countQuoteConSplitReverseIstituzionaleOCommerciale(allegatoAtto);
		
		log.info(methodName, ">>>>>>>> "+ countDocumentiConRitenute + " " + countFattureNonALGoCCN + " " + countQuoteConSplitReverseIstituzionaleOCommerciale);
	}
		
	@Test
	public void inviaAllegatoAtto() {
		InviaAllegatoAtto req = new InviaAllegatoAtto();
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		
		AllegatoAtto allegatoAtto = new AllegatoAtto();
		allegatoAtto.setUid(24507);
		req.setAllegatoAtto(allegatoAtto);
		
		//req.setEnte(getEnteTest());
		req.setBilancio(getBilancioByProperties("consip","regp", "2019"));
		
		//log.error("inviaAllegatoAtto", "req.getBilancio().getAnno() " + req.getBilancio().getAnno());
		req.setAnnoEsercizio(req.getBilancio().getAnno());
		
		
		InviaAllegatoAttoResponse res = inviaAllegatoAttoService.executeService(req);
		
		assertNotNull(res);
		
	}
	
	@Test
	public void attiliqStartServletInvoker() {
		Map<String, String> m = new HashMap<String, String>();
		m.put("id_ente", "A"); //trovare id ente per attiliq!
		m.put("anno", "2015");
		m.put("numero", "1");
		m.put("direzione", "D1");
		m.put("settore", "S01");
		m.put("versione", "1");
		
		m.put("codice_tipo", "ALG");
		
		m.put("anno_titolario", "2015");
		m.put("codice_titolario", "01"); //titolario //utente_scrittore
		
		m.put("importo","100");  
		m.put("dt_inserimento","26/09/2015");  
		m.put("dt_scadenza", "26/09/2015");
		
		File report = getDummyReport();
		
		attiliqStartServletInvoker.invoke(m, report);
		
	}

	private File getDummyReport() {
		File report = new File();
		byte[] contenuto = new byte[10];
		for(int i = 0; i<10; i++){
			contenuto[i] = (byte) (10+i);
		}
		report.setContenuto(contenuto);
		return report;
	}
	
	
	
	@Test
	public void inviaAllegatoAtto1(){

		StringBuilder sb = new StringBuilder();

		sb.append("<inviaAllegatoAtto>");
		sb.append("    <dataOra>2015-09-21T10:24:08.193+02:00</dataOra>");
		sb.append("    <richiedente>");
		sb.append("        <account>");
		sb.append("            <stato>VALIDO</stato>");
		sb.append("            <uid>1</uid>");
		sb.append("            <nome>Demo 21</nome>");
		sb.append("            <descrizione>Demo 21 - CittÃ  di Torino</descrizione>");
		sb.append("            <indirizzoMail>email</indirizzoMail>");
		sb.append("            <ente>");
		sb.append("                <stato>VALIDO</stato>");
		sb.append("                <uid>1</uid>");
		sb.append("                <gestioneLivelli>");
		sb.append("                    <entry>");
		sb.append("                        <key>LIVELLO_GESTIONE_BILANCIO</key>");
		sb.append("                        <value>GESTIONE_UEB</value>");
		sb.append("                    </entry>");
		sb.append("                </gestioneLivelli>");
		sb.append("                <nome>CittÃ  di Torino</nome>");
		sb.append("            </ente>");
		sb.append("        </account>");
		sb.append("        <operatore>");
		sb.append("            <stato>VALIDO</stato>");
		sb.append("            <uid>0</uid>");
		sb.append("            <codiceFiscale>AAAAAA00A11B000J</codiceFiscale>");
		sb.append("            <cognome>AAAAAA00A11B000J</cognome>");
		sb.append("            <nome>Demo</nome>");
		sb.append("        </operatore>");
		sb.append("    </richiedente>");
		sb.append("    <allegatoAtto>");
		sb.append("        <stato>VALIDO</stato>");
		sb.append("        <uid>139</uid>");
		sb.append("        <ente>");
		sb.append("            <stato>VALIDO</stato>");
		sb.append("            <uid>1</uid>");
		sb.append("            <gestioneLivelli>");
		sb.append("                <entry>");
		sb.append("                    <key>LIVELLO_GESTIONE_BILANCIO</key>");
		sb.append("                    <value>GESTIONE_UEB</value>");
		sb.append("                </entry>");
		sb.append("            </gestioneLivelli>");
		sb.append("            <nome>CittÃ  di Torino</nome>");
		sb.append("        </ente>");
		sb.append("    </allegatoAtto>");
		sb.append("</inviaAllegatoAtto>");
				
		InviaAllegatoAtto req = JAXBUtility.unmarshall(sb.toString(), InviaAllegatoAtto.class);
		inviaAllegatoAttoService.executeService(req);
				
	}
	
	@Autowired
	private InviaAllegatoAttoAsyncService inviaAllegatoAttoAsyncService;
	
	@Test
	public void inviaAllegatoAttoAsync() {
		InviaAllegatoAtto req = new InviaAllegatoAtto();
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		
		AllegatoAtto allegatoAtto = new AllegatoAtto();
		allegatoAtto.setUid(183);
		req.setAllegatoAtto(allegatoAtto);
		//req.setEnte(getEnteTest());
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));

		req.setAnnoEsercizio(getBilancio2015Test().getAnno());
		req.setBilancio(getBilancio2015Test());
		Azione azione= new Azione();
		AzioneRichiesta azioneRichiesta = new AzioneRichiesta();
		azione.setNome("OP-COM-inviaAttoAllegato");
		azione.setUid(6201);
		azioneRichiesta.setAzione(azione);
		AsyncServiceResponse response = inviaAllegatoAttoAsyncService.executeService(wrapRequestToAsync(req, azioneRichiesta));
		assertNotNull(response);
		
		try {
			Thread.sleep(2 * 60 * 1000);
		} catch (InterruptedException e) {
			log.error("stampaAllegatoAtto", "InterruptedException during test", e);
			throw new RuntimeException("Test failure", e);
		}
		inviaAllegatoAttoService.executeService(req);

	}
	
	protected <REQ extends ServiceRequest> AsyncServiceRequestWrapper<REQ> wrapRequestToAsync(REQ request, AzioneRichiesta azioneRichiesta) {
		AsyncServiceRequestWrapper<REQ> result = new AsyncServiceRequestWrapper<REQ>();
		
		// Mappatura dei campi
		result.setAzioneRichiesta(azioneRichiesta);
		result.setDataOra(request.getDataOra());
		result.setEnte(getEnteTest());
		result.setRequest(request);
		result.setRichiedente(getRichiedenteByProperties("consip","regp"));
		Account account = new Account();
		account.setUid(1);
		account.setEnte(getEnteTest(getEnteTest().getUid()));
		result.setAccount(account);		
		return result;
	}
	
	
}
