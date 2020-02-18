/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.variazionicodifica;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacbilser.business.service.capitolo.RicercaStoricoVariazioniCodificheCapitoloService;
import it.csi.siac.siacbilser.business.service.variazionicodifica.AggiornaVariazioneCodificheService;
import it.csi.siac.siacbilser.business.service.variazionicodifica.DefinisceVariazioneCodificheService;
import it.csi.siac.siacbilser.business.service.variazionicodifica.InserisceVariazioneCodificheService;
import it.csi.siac.siacbilser.business.service.variazionicodifica.RicercaDettaglioVariazioneCodificheService;
import it.csi.siac.siacbilser.business.service.variazionicodifica.RicercaVariazioneCodificheService;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaVariazioneCodifiche;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaVariazioneCodificheResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.DefinisceVariazioneCodifiche;
import it.csi.siac.siacbilser.frontend.webservice.msg.DefinisceVariazioneCodificheResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceVariazioneCodifiche;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceVariazioneCodificheResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioVariazioneCodifiche;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioVariazioneCodificheResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaStoricoVariazioniCodificheCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaStoricoVariazioniCodificheCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaVariazioneCodifiche;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaVariazioneCodificheResponse;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.CapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.model.DettaglioVariazioneCodificaCapitolo;
import it.csi.siac.siacbilser.model.ImportiCapitolo;
import it.csi.siac.siacbilser.model.StatoOperativoVariazioneDiBilancio;
import it.csi.siac.siacbilser.model.TipoCapitolo;
import it.csi.siac.siacbilser.model.TipoVariazione;
import it.csi.siac.siacbilser.model.VariazioneCodificaCapitolo;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;
import it.csi.siac.siaccommon.util.JAXBUtility;
import it.csi.siac.siaccorser.model.ClassificatoreGenerico;
import it.csi.siac.siaccorser.model.ClassificatoreGerarchico;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;

// TODO: Auto-generated Javadoc
/**
 * The Class VariazioniCodificheDLTest.
 */
public class VariazioniCodificheTest extends BaseJunit4TestCase {
		
	/** The ricerca dettaglio variazione codifiche service. */
	@Autowired
	private RicercaDettaglioVariazioneCodificheService ricercaDettaglioVariazioneCodificheService;
	
	
	/** The ricerca variazione codifiche service. */
	@Autowired
	private RicercaVariazioneCodificheService ricercaVariazioneCodificheService;
	
	/** The definisce variazione codifiche service. */
	@Autowired
	private DefinisceVariazioneCodificheService definisceVariazioneCodificheService;
	
	/** The inserisce variazione codifiche service. */
	@Autowired
	private InserisceVariazioneCodificheService inserisceVariazioneCodificheService;
	
	/** The aggiorna variazione codifiche service. */
	@Autowired
	private AggiornaVariazioneCodificheService aggiornaVariazioneCodificheService;
	
	
	/**
	 * Test ricerca dettaglio variazione codifiche.
	 */
	@Test
	public void testRicercaDettaglioVariazioneCodifiche() {
		RicercaDettaglioVariazioneCodifiche req = new RicercaDettaglioVariazioneCodifiche();
	
		req.setRichiedente(getRichiedenteByProperties("forn2", "crp"));
		req.setUidVariazione(583); //146, 84
		
		
		RicercaDettaglioVariazioneCodificheResponse resp = ricercaDettaglioVariazioneCodificheService.executeService(req);
		
		assertNotNull(resp);
		assertNotNull(resp.getVariazioneCodificaCapitolo());
		
	}
	
	
	
	/**
	 * Test ricerca variazione codifiche.
	 */
	@Test
	public void testRicercaVariazioneCodifiche() {
		RicercaVariazioneCodifiche req = new RicercaVariazioneCodifiche();
		
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		req.setParametriPaginazione(getParametriPaginazione(0,100));
		
		//req.setTipiCapitolo(Arrays.asList(TipoCapitolo.CAPITOLO_USCITA_GESTIONE,TipoCapitolo.CAPITOLO_ENTRATA_GESTIONE));
		req.setTipiCapitolo(Arrays.asList(TipoCapitolo.CAPITOLO_USCITA_PREVISIONE,TipoCapitolo.CAPITOLO_ENTRATA_PREVISIONE, TipoCapitolo.CAPITOLO_USCITA_GESTIONE,TipoCapitolo.CAPITOLO_ENTRATA_GESTIONE));
		
		VariazioneCodificaCapitolo vic = new VariazioneCodificaCapitolo();
		
		vic.setBilancio(getBilancioTest());
		vic.setEnte(getEnteTest());	
		vic.setTipoVariazione(TipoVariazione.VARIAZIONE_CODIFICA);
		
		req.setVariazioneCodificaCapitolo(vic);
		
		
		RicercaVariazioneCodificheResponse resp = ricercaVariazioneCodificheService.executeService(req);
		
		assertNotNull(resp);
		assertNotNull(resp.getVariazioniDiBilancio());
		
	}
	
	
	
	/**
	 * Test inserisce variazione.
	 *
	 * @throws Throwable the throwable
	 */
	@Test
	public void testInserisceVariazione() throws Throwable
	{

		final String methodName = "testInserisceVariazione";
		try
		{

			
			InserisceVariazioneCodifiche req = new InserisceVariazioneCodifiche();
			
			req.setRichiedente(getRichiedenteByProperties("consip","regp"));
			req.setInvioOrganoAmministrativo(Boolean.FALSE);
			req.setInvioOrganoLegislativo(Boolean.FALSE);
			
			VariazioneCodificaCapitolo vi = new VariazioneCodificaCapitolo();
			vi.setEnte(getEnteTest());
			vi.setBilancio( getBilancioTest());	
			vi.setData(new Date());
			vi.setDescrizione("Mia Variazione Codifica3 (Desc!) "+new Date());
			vi.setNote("Mia Variazione Codifica3 (Note!) "+new Date());
			vi.setTipoVariazione(TipoVariazione.VARIAZIONE_CODIFICA);
									
			
			
			AttoAmministrativo attoAmministrativo = new AttoAmministrativo();
			attoAmministrativo.setUid(32);
			vi.setAttoAmministrativo(attoAmministrativo);
			
			List<DettaglioVariazioneCodificaCapitolo> listaDettaglioVariazioneImporto = new ArrayList<DettaglioVariazioneCodificaCapitolo>();
			
			DettaglioVariazioneCodificaCapitolo dettaglioVc = new DettaglioVariazioneCodificaCapitolo();
			CapitoloUscitaPrevisione capitoloA = new CapitoloUscitaPrevisione();
			capitoloA.setUid(40908946);
			dettaglioVc.setCapitolo(capitoloA );			
			dettaglioVc.getClassificatoriGenerici().add(newClassificatoreGenerico(5830)); ////MI 5842 giustizia > PR 5830 organi istituzionali
			dettaglioVc.getClassificatoriGenerici().add(newClassificatoreGenerico(5952)); //TitSpesa 5951 -> MACR 5952 lavoro dipendente
			
			dettaglioVc.getClassificatoriGerarchici().add(newClassificatoreGerarchico(344)); //StrutAmmCont 344 contratto e appalti 
			dettaglioVc.getClassificatoriGerarchici().add(newClassificatoreGerarchico(5995)); //PDC 5995 addizinale comunale IRPEF 
		
			listaDettaglioVariazioneImporto.add(dettaglioVc );
			
			
			
			DettaglioVariazioneCodificaCapitolo dettaglioVc2 = new DettaglioVariazioneCodificaCapitolo();
			CapitoloEntrataGestione capitoloB = new CapitoloEntrataGestione();
			capitoloB.setUid(40908784);
			dettaglioVc2.setCapitolo(capitoloB);			
			dettaglioVc2.getClassificatoriGenerici().add(newClassificatoreGenerico(5830)); ////MI 5842 giustizia > PR 5830 organi istituzionali
			dettaglioVc2.getClassificatoriGenerici().add(newClassificatoreGenerico(5952)); //TitSpesa 5951 -> MACR 5952 lavoro dipendente
			
			dettaglioVc2.getClassificatoriGerarchici().add(newClassificatoreGerarchico(344)); //StrutAmmCont 344 contratto e appalti 
			dettaglioVc2.getClassificatoriGerarchici().add(newClassificatoreGerarchico(5995)); //PDC 5995 addizinale comunale IRPEF 
			listaDettaglioVariazioneImporto.add(dettaglioVc2 );			
			
						
			vi.setListaDettaglioVariazioneCodifica(listaDettaglioVariazioneImporto);
			
			vi.setStatoOperativoVariazioneDiBilancio(StatoOperativoVariazioneDiBilancio.BOZZA);
			
			req.setVariazioneCodificaCapitolo(vi);
			
			req = JAXBUtility.unmarshall(JAXBUtility.marshall(req),InserisceVariazioneCodifiche.class);

//			InserisceStornoUEBResponse res = variazioneDiBilancioService.inserisceStornoUEBUscita(req);
			InserisceVariazioneCodificheResponse res = inserisceVariazioneCodificheService.executeService(req);
			assertNotNull(res);
			
			log.info(methodName, "esito: "+res.getEsito());
			log.info(methodName, "errori: "+res.getErrori());
			
			assertNotNull(res.getVariazioneCodificaCapitolo());
			log.info(methodName, "Numero Variazione inserita: "+ res.getVariazioneCodificaCapitolo().getNumero());

		}
		catch (Throwable e)
		{
			e.printStackTrace();
			throw e;
		}
	}



	/**
	 * New classificatore generico.
	 *
	 * @param uid the uid
	 * @return the classificatore generico
	 */
	private ClassificatoreGenerico newClassificatoreGenerico(int uid) {
		ClassificatoreGenerico cg = new ClassificatoreGenerico();
		cg.setUid(uid);
		return cg;
	}
	
	/**
	 * New classificatore gerarchico.
	 *
	 * @param uid the uid
	 * @return the classificatore gerarchico
	 */
	private ClassificatoreGerarchico newClassificatoreGerarchico(int uid) {
		ClassificatoreGerarchico cg = new ClassificatoreGerarchico();
		cg.setUid(uid);
		return cg;
	}
	
	
	
	/**
	 * Test aggiorna variazione.
	 *
	 * @throws Throwable the throwable
	 */
	@Test
	public void testAggiornaVariazione() throws Throwable
	{
		final String methodName = "testAggiornaVariazione";

		try
		{

			
			AggiornaVariazioneCodifiche req = new AggiornaVariazioneCodifiche();
			
			req.setRichiedente(getRichiedenteByProperties("consip","regp"));
			req.setEvolviProcesso(Boolean.FALSE);
			req.setInvioOrganoAmministrativo(Boolean.FALSE);
			req.setInvioOrganoLegislativo(Boolean.FALSE);
			req.setAnnullaVariazione(Boolean.FALSE);
			
			VariazioneCodificaCapitolo vi = new VariazioneCodificaCapitolo();
			vi.setNumero(58);
			vi.setUid(83);
			vi.setEnte(getEnteTest());
			vi.setBilancio( getBilancioTest());	
			vi.setData(new Date());
			vi.setDescrizione("Mia Variazione Codifica (Desc) AGG "+new Date());
			vi.setNote("Mia Variazione Codifica (Note) AGG "+new Date());
			vi.setTipoVariazione(TipoVariazione.VARIAZIONE_CODIFICA);
						
			
			
			AttoAmministrativo attoAmministrativo = new AttoAmministrativo();
			attoAmministrativo.setUid(32);
			vi.setAttoAmministrativo(attoAmministrativo);
			
			List<DettaglioVariazioneCodificaCapitolo> listaDettaglioVariazioneImporto = new ArrayList<DettaglioVariazioneCodificaCapitolo>();
			
			DettaglioVariazioneCodificaCapitolo dettaglioVc = new DettaglioVariazioneCodificaCapitolo();
			CapitoloUscitaPrevisione capitoloA = new CapitoloUscitaPrevisione();
			capitoloA.setUid(40908946);
			dettaglioVc.setCapitolo(capitoloA );			
			dettaglioVc.getClassificatoriGenerici().add(newClassificatoreGenerico(5830)); ////MI 5842 giustizia > PR 5830 organi istituzionali
			dettaglioVc.getClassificatoriGenerici().add(newClassificatoreGenerico(5952)); //TitSpesa 5951 -> MACR 5952 lavoro dipendente
			
			dettaglioVc.getClassificatoriGerarchici().add(newClassificatoreGerarchico(344)); //StrutAmmCont 344 contratto e appalti 
			dettaglioVc.getClassificatoriGerarchici().add(newClassificatoreGerarchico(5995)); //PDC 5995 addizinale comunale IRPEF 
		
			listaDettaglioVariazioneImporto.add(dettaglioVc );
			
			
			
			DettaglioVariazioneCodificaCapitolo dettaglioVc2 = new DettaglioVariazioneCodificaCapitolo();
			CapitoloEntrataGestione capitoloB = new CapitoloEntrataGestione();
			capitoloB.setUid(40908784);
			dettaglioVc2.setCapitolo(capitoloB);			
			dettaglioVc2.getClassificatoriGenerici().add(newClassificatoreGenerico(5830)); ////MI 5842 giustizia > PR 5830 organi istituzionali
			dettaglioVc2.getClassificatoriGenerici().add(newClassificatoreGenerico(5952)); //TitSpesa 5951 -> MACR 5952 lavoro dipendente
			
			dettaglioVc2.getClassificatoriGerarchici().add(newClassificatoreGerarchico(344)); //StrutAmmCont 344 contratto e appalti 
			dettaglioVc2.getClassificatoriGerarchici().add(newClassificatoreGerarchico(5995)); //PDC 5995 addizinale comunale IRPEF 
			listaDettaglioVariazioneImporto.add(dettaglioVc2 );			
						
			vi.setListaDettaglioVariazioneCodifica(listaDettaglioVariazioneImporto);
			
			vi.setStatoOperativoVariazioneDiBilancio(StatoOperativoVariazioneDiBilancio.DEFINITIVA);
			
			req.setVariazioneCodificaCapitolo(vi);
			
			req = JAXBUtility.unmarshall(JAXBUtility.marshall(req),AggiornaVariazioneCodifiche.class);

//			InserisceStornoUEBResponse res = variazioneDiBilancioService.inserisceStornoUEBUscita(req);
			AggiornaVariazioneCodificheResponse res = aggiornaVariazioneCodificheService.executeService(req);
			assertNotNull(res);
			
			log.info(methodName, "esito: "+res.getEsito());
			log.info(methodName, "errori: "+res.getErrori());
			
			assertNotNull(res.getVariazioneCodificaCapitolo());
			log.info(methodName, "Numero Variazione inserita: "+ res.getVariazioneCodificaCapitolo().getNumero());

		}
		catch (Throwable e)
		{
			e.printStackTrace();
			throw e;
		}
	}
	
	
	
	/**
	 * Test definisce variazione.
	 *
	 * @throws Throwable the throwable
	 */
	@Test
	public void testDefinisceVariazione() throws Throwable {

		final String methodName = "testDefinisceVariazione";
		try {
			
			DefinisceVariazioneCodifiche req = new DefinisceVariazioneCodifiche();
			
			req.setRichiedente(getRichiedenteByProperties("consip","regp"));
			req.setIdAttivita("VariazioneDiBilancio--1.0--296--VariazioneDiBilancio_AggiornamentoVariazione--itb0ec3e99-4542-4b2a-a256-40c55a1b00d0--mainActivityInstance--noLoop");
				
			
			VariazioneCodificaCapitolo vi = new VariazioneCodificaCapitolo();
			vi.setUid(381);
			vi.setEnte(getEnteTest());
			vi.setBilancio( getBilancioTest());	
			
			req.setVariazioneCodificaCapitolo(vi);
			
			req = JAXBUtility.unmarshall(JAXBUtility.marshall(req),DefinisceVariazioneCodifiche.class);

//			InserisceStornoUEBResponse res = variazioneDiBilancioService.inserisceStornoUEBUscita(req);
			DefinisceVariazioneCodificheResponse res = definisceVariazioneCodificheService.executeService(req);
			assertNotNull(res);
			
			log.info(methodName, "esito: "+res.getEsito());
			log.info(methodName, "errori: "+res.getErrori());
			
			assertNotNull(res.getVariazioneCodificaCapitolo());
			log.info(methodName, "Numero Variazione definita: "+ res.getVariazioneCodificaCapitolo().getNumero());

		}
		catch (Throwable e)
		{
			e.printStackTrace();
			throw e;
		}
	}
	
	@Test
	public void testInserisceVariazione2() throws Throwable {
		final String methodName = "testInserisceVariazione2";
			InserisceVariazioneCodifiche req = new InserisceVariazioneCodifiche();
			String reqXml =  "<inserisceVariazioneCodifiche>" +
"    <dataOra>2015-01-15T17:53:08.861+01:00</dataOra>" +
"    <richiedente>" +
"        <account>" +
"            <stato>VALIDO</stato>" +
"            <uid>1</uid>" +
"            <nome>Demo 21</nome>" +
"            <descrizione>Demo 21 - CittÃ  di Torino</descrizione>" +
"            <indirizzoMail>email</indirizzoMail>" +
"            <ente>" +
"                <stato>VALIDO</stato>" +
"                <uid>1</uid>" +
"                <gestioneLivelli>" +
"                    <entry>" +
"                        <key>LIVELLO_GESTIONE_BILANCIO</key>" +
"                        <value>GESTIONE_UEB</value>" +
"                    </entry>" +
"                </gestioneLivelli>" +
"                <nome>CittÃ  di Torino</nome>" +
"            </ente>" +
"        </account>" +
"        <operatore>" +
"            <stato>VALIDO</stato>" +
"            <uid>0</uid>" +
"            <codiceFiscale>AAAAAA00A11B000J</codiceFiscale>" +
"            <cognome>AAAAAA00A11B000J</cognome>" +
"            <nome>Demo</nome>" +
"        </operatore>" +
"    </richiedente>" +
"    <invioOrganoAmministrativo>false</invioOrganoAmministrativo>" +
"    <invioOrganoLegislativo>false</invioOrganoLegislativo>" +
"    <variazioneCodificaCapitolo>" +
"        <stato>VALIDO</stato>" +
"        <uid>0</uid>" +
"        <bilancio>" +
"            <stato>VALIDO</stato>" +
"            <uid>10</uid>" +
"            <anno>2015</anno>" +
"        </bilancio>" +
"        <data>2015-01-15T17:53:08.861+01:00</data>" +
"        <descrizione>SIAC-1218 --- 1 UEB, gestione</descrizione>" +
"        <ente>" +
"            <stato>VALIDO</stato>" +
"            <uid>1</uid>" +
"            <gestioneLivelli>" +
"                <entry>" +
"                    <key>LIVELLO_GESTIONE_BILANCIO</key>" +
"                    <value>GESTIONE_UEB</value>" +
"                </entry>" +
"            </gestioneLivelli>" +
"            <nome>CittÃ  di Torino</nome>" +
"        </ente>" +
"        <note></note>" +
"        <statoOperativoVariazioneDiBilancio>BOZZA</statoOperativoVariazioneDiBilancio>" +
"        <tipoVariazione>VARIAZIONE_CODIFICA</tipoVariazione>" +
"        <listaDettaglioVariazioneCodifica>" +
"            <stato>VALIDO</stato>" +
"            <uid>0</uid>" +
"            <capitolo xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:ns3=\"http://siac.csi.it/bil/svc/1.0\" xsi:type=\"ns3:capitoloUscitaGestione\">" +
"                <stato>VALIDO</stato>" +
"                <uid>10568</uid>" +
"                <categoriaCapitolo>STANDARD</categoriaCapitolo>" +
"                <descrizione>Il primo classificatore generico Ã¨ stato impostato come informazione del capitolo e quindi viene presentato correttament nella maschera delle informazioni da variare; il sistema perÃ² non lo salva.</descrizione>" +
"                <descrizioneArticolo>Facendo la prova con 3 ueb il sistema ha candcellato la classificazione di bilancio.&#xD;" +
"Facendo la prova con una ueb sola, arrivata alla definizione il sistema visualizza il seguente mssaggio \"COR_ERR_0001 - Errore di sistema: null\" &#xD;" +
"Commento di Irene</descrizioneArticolo>" +
"                <note></note>" +
"                <numeroArticolo>1</numeroArticolo>" +
"                <numeroCapitolo>1218</numeroCapitolo>" +
"                <tipoCapitolo>CAPITOLO_USCITA_GESTIONE</tipoCapitolo>" +
"                <uidCapitoloEquivalente>0</uidCapitoloEquivalente>" +
"                <uidExCapitolo>0</uidExCapitolo>" +
"            </capitolo>" +
"            <classificatoriGerarchici xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:ns3=\"http://siac.csi.it/bil/data/1.0\" xsi:type=\"ns3:programma\">" +
"                <loginOperazione>admin_mod_class</loginOperazione>" +
"                <stato>VALIDO</stato>" +
"                <uid>5830</uid>" +
"                <codice>0101</codice>" +
"                <descrizione>Organi istituzionali</descrizione>" +
"                <livello>2</livello>" +
"                <tipoClassificatore>" +
"                    <stato>VALIDO</stato>" +
"                    <uid>0</uid>" +
"                    <codice>PROGRAMMA</codice>" +
"                    <descrizione>Programma</descrizione>" +
"                </tipoClassificatore>" +
"            </classificatoriGerarchici>" +
"            <classificatoriGerarchici xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:ns3=\"http://siac.csi.it/bil/data/1.0\" xsi:type=\"ns3:macroaggregato\">" +
"                <loginOperazione>admin</loginOperazione>" +
"                <stato>VALIDO</stato>" +
"                <uid>5952</uid>" +
"                <codice>01</codice>" +
"                <descrizione>Redditi da lavoro dipendente</descrizione>" +
"                <livello>2</livello>" +
"                <tipoClassificatore>" +
"                    <stato>VALIDO</stato>" +
"                    <uid>0</uid>" +
"                    <codice>MACROAGGREGATO</codice>" +
"                    <descrizione>Macroaggregato</descrizione>" +
"                </tipoClassificatore>" +
"            </classificatoriGerarchici>" +
"            <classificatoriGerarchici xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:ns3=\"http://siac.csi.it/bil/data/1.0\" xsi:type=\"ns3:elementoPianoDeiConti\">" +
"                <stato>VALIDO</stato>" +
"                <uid>7433</uid>" +
"                <codice>U.1.01.01.01.001</codice>" +
"                <descrizione>Arretrati per anni precedenti corrisposti al personale a tempo indeterminato</descrizione>" +
"                <livello>0</livello>" +
"                <tipoClassificatore>" +
"                    <stato>VALIDO</stato>" +
"                    <uid>25</uid>" +
"                    <codice>PDC_V</codice>" +
"                    <descrizione>Quinto livello PDC</descrizione>" +
"                </tipoClassificatore>" +
"            </classificatoriGerarchici>" +
"            <classificatoriGerarchici xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:ns3=\"http://siac.csi.it/cor/data/1.0\" xsi:type=\"ns3:strutturaAmministrativoContabile\">" +
"                <stato>VALIDO</stato>" +
"                <uid>502</uid>" +
"                <codice>001</codice>" +
"                <descrizione>DIREZIONE GENERALE</descrizione>" +
"                <livello>0</livello>" +
"                <tipoClassificatore>" +
"                    <loginOperazione>admin</loginOperazione>" +
"                    <stato>VALIDO</stato>" +
"                    <uid>10</uid>" +
"                    <codice>CDR</codice>" +
"                    <descrizione>Centro di RespondabilitÃ Â (Direzione)</descrizione>" +
"                </tipoClassificatore>" +
"                <subStrutture>" +
"                    <stato>VALIDO</stato>" +
"                    <uid>342</uid>" +
"                    <codice>00X</codice>" +
"                    <descrizione>GABINETTO DEL SINDACO</descrizione>" +
"                    <livello>0</livello>" +
"                    <tipoClassificatore>" +
"                        <loginOperazione>admin</loginOperazione>" +
"                        <stato>VALIDO</stato>" +
"                        <uid>8</uid>" +
"                        <codice>CDC</codice>" +
"                        <descrizione>Cdc(Settore)</descrizione>" +
"                    </tipoClassificatore>" +
"                    <assessorato>Campo Note (capitolo)</assessorato>" +
"                </subStrutture>" +
"                <subStrutture>" +
"                    <stato>VALIDO</stato>" +
"                    <uid>381</uid>" +
"                    <codice>040</codice>" +
"                    <descrizione>SERVIZI CIMITERIALI</descrizione>" +
"                    <livello>0</livello>" +
"                    <tipoClassificatore>" +
"                        <loginOperazione>admin</loginOperazione>" +
"                        <stato>VALIDO</stato>" +
"                        <uid>8</uid>" +
"                        <codice>CDC</codice>" +
"                        <descrizione>Cdc(Settore)</descrizione>" +
"                    </tipoClassificatore>" +
"                    <assessorato>Campo Note (capitolo)</assessorato>" +
"                </subStrutture>" +
"                <subStrutture>" +
"                    <stato>VALIDO</stato>" +
"                    <uid>404</uid>" +
"                    <codice>064</codice>" +
"                    <descrizione>PARTECIPAZIONI COMUNALI</descrizione>" +
"                    <livello>0</livello>" +
"                    <tipoClassificatore>" +
"                        <loginOperazione>admin</loginOperazione>" +
"                        <stato>VALIDO</stato>" +
"                        <uid>8</uid>" +
"                        <codice>CDC</codice>" +
"                        <descrizione>Cdc(Settore)</descrizione>" +
"                    </tipoClassificatore>" +
"                    <assessorato>Campo Note (capitolo)</assessorato>" +
"                </subStrutture>" +
"                <subStrutture>" +
"                    <stato>VALIDO</stato>" +
"                    <uid>406</uid>" +
"                    <codice>066</codice>" +
"                    <descrizione>DIREZIONE GENERALE (CDC)</descrizione>" +
"                    <livello>0</livello>" +
"                    <tipoClassificatore>" +
"                        <loginOperazione>admin</loginOperazione>" +
"                        <stato>VALIDO</stato>" +
"                        <uid>8</uid>" +
"                        <codice>CDC</codice>" +
"                        <descrizione>Cdc(Settore)</descrizione>" +
"                    </tipoClassificatore>" +
"                    <assessorato>Campo Note (capitolo)</assessorato>" +
"                </subStrutture>" +
"                <subStrutture>" +
"                    <stato>VALIDO</stato>" +
"                    <uid>465</uid>" +
"                    <codice>128</codice>" +
"                    <descrizione>CONTROLLO STRATEGICO E DIREZIONALE</descrizione>" +
"                    <livello>0</livello>" +
"                    <tipoClassificatore>" +
"                        <loginOperazione>admin</loginOperazione>" +
"                        <stato>VALIDO</stato>" +
"                        <uid>8</uid>" +
"                        <codice>CDC</codice>" +
"                        <descrizione>Cdc(Settore)</descrizione>" +
"                    </tipoClassificatore>" +
"                    <assessorato>Campo Note (capitolo)</assessorato>" +
"                </subStrutture>" +
"                <subStrutture>" +
"                    <stato>VALIDO</stato>" +
"                    <uid>466</uid>" +
"                    <codice>129</codice>" +
"                    <descrizione>URP E RELAZIONI CON IL CITTADINO</descrizione>" +
"                    <livello>0</livello>" +
"                    <tipoClassificatore>" +
"                        <loginOperazione>admin</loginOperazione>" +
"                        <stato>VALIDO</stato>" +
"                        <uid>8</uid>" +
"                        <codice>CDC</codice>" +
"                        <descrizione>Cdc(Settore)</descrizione>" +
"                    </tipoClassificatore>" +
"                    <assessorato>Campo Note (capitolo)</assessorato>" +
"                </subStrutture>" +
"                <subStrutture>" +
"                    <stato>VALIDO</stato>" +
"                    <uid>470</uid>" +
"                    <codice>133</codice>" +
"                    <descrizione>PIANIFICAZIONE STRATEGICA DELLA CITTA' DI TORINO</descrizione>" +
"                    <livello>0</livello>" +
"                    <tipoClassificatore>" +
"                        <loginOperazione>admin</loginOperazione>" +
"                        <stato>VALIDO</stato>" +
"                        <uid>8</uid>" +
"                        <codice>CDC</codice>" +
"                        <descrizione>Cdc(Settore)</descrizione>" +
"                    </tipoClassificatore>" +
"                    <assessorato>Campo Note (capitolo)</assessorato>" +
"                </subStrutture>" +
"                <subStrutture>" +
"                    <stato>VALIDO</stato>" +
"                    <uid>471</uid>" +
"                    <codice>134</codice>" +
"                    <descrizione>GRANDI PROGETTI URBANI</descrizione>" +
"                    <livello>0</livello>" +
"                    <tipoClassificatore>" +
"                        <loginOperazione>admin</loginOperazione>" +
"                        <stato>VALIDO</stato>" +
"                        <uid>8</uid>" +
"                        <codice>CDC</codice>" +
"                        <descrizione>Cdc(Settore)</descrizione>" +
"                    </tipoClassificatore>" +
"                    <assessorato>Campo Note (capitolo)</assessorato>" +
"                </subStrutture>" +
"            </classificatoriGerarchici>" +
"        </listaDettaglioVariazioneCodifica>" +
"    </variazioneCodificaCapitolo>" +
"</inserisceVariazioneCodifiche>";
		

		
			
			
			req = JAXBUtility.unmarshall(reqXml,InserisceVariazioneCodifiche.class);

//			InserisceStornoUEBResponse res = variazioneDiBilancioService.inserisceStornoUEBUscita(req);
			InserisceVariazioneCodificheResponse res = inserisceVariazioneCodificheService.executeService(req);
			assertNotNull(res);
			
			log.info(methodName, "esito: "+res.getEsito());
			log.info(methodName, "errori: "+res.getErrori());
			
			assertNotNull(res.getVariazioneCodificaCapitolo());
			log.info(methodName, "Numero Variazione inserita: "+ res.getVariazioneCodificaCapitolo().getNumero());

	}
	
	
	@Test
	public void testRicercaStoricoVariazioniCapitolo() {
		RicercaStoricoVariazioniCodificheCapitolo req = new RicercaStoricoVariazioniCodificheCapitolo();
		req.setRichiedente(getRichiedenteTest("AAAAAA00A11C000K", 52, 2));
		req.setParametriPaginazione(new ParametriPaginazione(0, 100)); 
		Capitolo<?, ?> capitolo = new Capitolo<ImportiCapitolo,ImportiCapitolo>();
		capitolo.setUid(25221); //capitolo di entrata (25221)
		req.setCapitolo(capitolo);
		RicercaStoricoVariazioniCodificheCapitoloResponse res = se.executeService(RicercaStoricoVariazioniCodificheCapitoloService.class, req);
		assertFalse(res.hasErrori());

	}

}
