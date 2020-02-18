/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.capitolouscitaprevisione;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.business.service.capitoloentrataprevisione.AggiornaCapitoloDiEntrataPrevisioneService;
import it.csi.siac.siacbilser.business.service.capitoloentrataprevisione.RicercaDettaglioCapitoloEntrataPrevisioneService;
import it.csi.siac.siacbilser.business.service.capitolouscitagestione.RicercaMovimentiCapitoloUscitaGestioneService;
import it.csi.siac.siacbilser.business.service.capitolouscitaprevisione.AggiornaCapitoloDiUscitaPrevisioneService;
import it.csi.siac.siacbilser.business.service.capitolouscitaprevisione.EliminaCapitoloUscitaPrevisioneService;
import it.csi.siac.siacbilser.business.service.capitolouscitaprevisione.InserisceCapitoloUscitaPrevisioneService;
import it.csi.siac.siacbilser.business.service.capitolouscitaprevisione.RicercaDettaglioCapitoloUscitaPrevisioneService;
import it.csi.siac.siacbilser.business.service.capitolouscitaprevisione.RicercaMovimentiCapitoloUscitaPrevisioneService;
import it.csi.siac.siacbilser.business.service.capitolouscitaprevisione.RicercaPuntualeCapitoloUscitaPrevisioneService;
import it.csi.siac.siacbilser.business.service.capitolouscitaprevisione.RicercaSinteticaCapitoloUscitaPrevisioneService;
import it.csi.siac.siacbilser.business.service.capitolouscitaprevisione.RicercaVariazioniCapitoloUscitaPrevisioneService;
import it.csi.siac.siacbilser.business.service.capitolouscitaprevisione.VerificaAnnullabilitaCapitoloUscitaPrevisioneService;
import it.csi.siac.siacbilser.business.service.capitolouscitaprevisione.VerificaEliminabilitaCapitoloUscitaPrevisioneService;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaCapitoloDiEntrataPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaCapitoloDiEntrataPrevisioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaCapitoloDiUscitaPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaCapitoloDiUscitaPrevisioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.EliminaCapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceCapitoloDiUscitaPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceCapitoloDiUscitaPrevisioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloEntrataPrevisioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloUscitaPrevisioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaMovimentiCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaMovimentiCapitoloUscitaGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaMovimentiCapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaMovimentiCapitoloUscitaPrevisioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeCapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeCapitoloUscitaPrevisioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloUscitaPrevisioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaVariazioniCapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaVariazioniCapitoloUscitaPrevisioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.VerificaAnnullabilitaCapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.VerificaAnnullabilitaCapitoloUscitaPrevisioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.VerificaEliminabilitaCapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.VerificaEliminabilitaCapitoloUscitaPrevisioneResponse;
import it.csi.siac.siacbilser.model.CapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.CapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.model.CategoriaCapitolo;
import it.csi.siac.siacbilser.model.CategoriaTipologiaTitolo;
import it.csi.siac.siacbilser.model.ElementoPianoDeiConti;
import it.csi.siac.siacbilser.model.ImportiCapitoloEP;
import it.csi.siac.siacbilser.model.ImportiCapitoloEnum;
import it.csi.siac.siacbilser.model.ImportiCapitoloUP;
import it.csi.siac.siacbilser.model.Macroaggregato;
import it.csi.siac.siacbilser.model.PerimetroSanitarioEntrata;
import it.csi.siac.siacbilser.model.Programma;
import it.csi.siac.siacbilser.model.RicorrenteEntrata;
import it.csi.siac.siacbilser.model.SiopeEntrata;
import it.csi.siac.siacbilser.model.StatoOperativoElementoDiBilancio;
import it.csi.siac.siacbilser.model.TipoCapitolo;
import it.csi.siac.siacbilser.model.TipoFinanziamento;
import it.csi.siac.siacbilser.model.TipoFondo;
import it.csi.siac.siacbilser.model.TipologiaTitolo;
import it.csi.siac.siacbilser.model.TitoloEntrata;
import it.csi.siac.siacbilser.model.TransazioneUnioneEuropeaEntrata;
import it.csi.siac.siacbilser.model.ric.RicercaDettaglioCapitoloEPrev;
import it.csi.siac.siacbilser.model.ric.RicercaDettaglioCapitoloUPrev;
import it.csi.siac.siacbilser.model.ric.RicercaPuntualeCapitoloUPrev;
import it.csi.siac.siacbilser.model.ric.RicercaSinteticaCapitoloUPrev;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;
import it.csi.siac.siaccommon.util.JAXBUtility;
import it.csi.siac.siaccorser.model.Account;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.ClassificatoreGenerico;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Operatore;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;

// TODO: Auto-generated Javadoc
/**
 * The Class GestioneCapitoloUscitaPrevisioneImplDLTest.
 */
public class GestioneCapitoloUscitaPrevisioneImpl2Test extends BaseJunit4TestCase
{
	
	
	/** The inserisce gestione capitolo uscita previsione service. */
	@Autowired
	private InserisceCapitoloUscitaPrevisioneService inserisceGestioneCapitoloUscitaPrevisioneService;
	
	/** The aggiorna capitolo di uscita previsione service. */
	@Autowired
	private AggiornaCapitoloDiUscitaPrevisioneService aggiornaCapitoloDiUscitaPrevisioneService;
	/** The aggiorna capitolo di entrata previsione service. */
	@Autowired
	private AggiornaCapitoloDiEntrataPrevisioneService aggiornaCapitoloDiEntrataPrevisioneService;
	
	/** The ricerca puntuale capitolo uscita previsione service. */
	@Autowired
	private RicercaPuntualeCapitoloUscitaPrevisioneService ricercaPuntualeCapitoloUscitaPrevisioneService;

	/** The ricerca sintetica capitolo uscita previsione service. */
	@Autowired
	private RicercaSinteticaCapitoloUscitaPrevisioneService ricercaSinteticaCapitoloUscitaPrevisioneService;
	
	/** The ricerca dettaglio capitolo uscita previsione service. */
	@Autowired
	private RicercaDettaglioCapitoloUscitaPrevisioneService ricercaDettaglioCapitoloUscitaPrevisioneService;
	
	/** The ricerca movimenti capitolo uscita previsione service. */
	@Autowired
	private RicercaMovimentiCapitoloUscitaPrevisioneService ricercaMovimentiCapitoloUscitaPrevisioneService;
	
	/** The ricerca variazioni capitolo uscita previsione service. */
	@Autowired
	private RicercaVariazioniCapitoloUscitaPrevisioneService ricercaVariazioniCapitoloUscitaPrevisioneService;
	
	/** The verifica annullabilita capitolo uscita previsione service. */
	@Autowired
	private VerificaAnnullabilitaCapitoloUscitaPrevisioneService verificaAnnullabilitaCapitoloUscitaPrevisioneService;
	
	/** The verifica eliminabilita capitolo uscita previsione service. */
	@Autowired
	private VerificaEliminabilitaCapitoloUscitaPrevisioneService verificaEliminabilitaCapitoloUscitaPrevisioneService;
	
	/** The elimina capitolo uscita previsione service. */
	@Autowired
	private EliminaCapitoloUscitaPrevisioneService eliminaCapitoloUscitaPrevisioneService;

	/** The ricerca dettaglio capitolo entrata previsione service. */
	@Autowired
	private RicercaDettaglioCapitoloEntrataPrevisioneService ricercaDettaglioCapitoloEntrataPrevisioneService;
	
	@Autowired
	private RicercaMovimentiCapitoloUscitaGestioneService ricercaMovimentiCapitoloUscitaGestioneService;

	/**
	 * Test create.
	 */
	@Test
	public void testCreate() {

		Bilancio bilancio = getBilancioTest();

		CapitoloUscitaPrevisione capitoloUscitaPrevisione = new CapitoloUscitaPrevisione();

		capitoloUscitaPrevisione.setNumeroCapitolo(999);
		capitoloUscitaPrevisione.setNumeroArticolo(1);
		capitoloUscitaPrevisione.setNumeroUEB(2);
		capitoloUscitaPrevisione.setAnnoCapitolo(bilancio.getAnno());
		capitoloUscitaPrevisione.setStatoOperativoElementoDiBilancio(StatoOperativoElementoDiBilancio.VALIDO);
		capitoloUscitaPrevisione.setDescrizione("MIO TEST CUP " + new Date().toString() +"!!!");
		capitoloUscitaPrevisione.setAnnoCreazioneCapitolo(2011);
		
		capitoloUscitaPrevisione.setFlagAssegnabile(Boolean.TRUE);
		
		capitoloUscitaPrevisione.setNote("PIO PIIIIIIIOOOOOOOOOOOO");
		
		CategoriaCapitolo cc = new CategoriaCapitolo();
		cc.setCodice("FPV");
		capitoloUscitaPrevisione.setCategoriaCapitolo(cc);
		capitoloUscitaPrevisione.setFlagImpegnabile(Boolean.TRUE);

		Ente ente = getEnteTest();

		Richiedente richiedente = getRichiedenteByProperties("consip","regp");

		ElementoPianoDeiConti elementoPianoDeiConti = new ElementoPianoDeiConti();
		elementoPianoDeiConti.setUid(5986);

		Macroaggregato macroaggregato = new Macroaggregato();
		macroaggregato.setUid(5952);

		Programma programma = new Programma();
		programma.setUid(5829);

		StrutturaAmministrativoContabile strutturaAmministrativoContabile = new StrutturaAmministrativoContabile();
		strutturaAmministrativoContabile.setUid(406);

//		ClassificazioneCofogProgramma classificazioneCofogProgramma = new ClassificazioneCofogProgramma();
//		classificazioneCofogProgramma.setUid(5245);

		TipoFondo tipoFondo = null;
		TipoFinanziamento tipoFinanziamento = null;
		List<ClassificatoreGenerico> listaClassificatori = null;

		List<ImportiCapitoloUP> listaImporti = new ArrayList<ImportiCapitoloUP>();

		listaImporti.add(getImportiCapitolo(2013, new BigDecimal(2013)));
		listaImporti.add(getImportiCapitolo(2014, new BigDecimal(2014)));
		listaImporti.add(getImportiCapitolo(2015, new BigDecimal(2015)));
		
		
		
		
		InserisceCapitoloDiUscitaPrevisione req = new InserisceCapitoloDiUscitaPrevisione();
		req.setRichiedente(richiedente);
		req.setEnte(ente);
		req.setBilancio(bilancio);
		req.setCapitoloUscitaPrevisione(capitoloUscitaPrevisione);
		req.setTipoFondo(tipoFondo);
		req.setTipoFinanziamento(tipoFinanziamento);
		req.setClassificatoriGenerici(listaClassificatori);
		req.setElementoPianoDeiConti(elementoPianoDeiConti);
		req.setStruttAmmContabile(strutturaAmministrativoContabile);
		//req.setClassificazioneCofogProgramma(classificazioneCofogProgramma);
		req.setImportiCapitoloUP(listaImporti);
		req.setMacroaggregato(macroaggregato);
		req.setProgramma(programma);
		
//		for(int i = 0; i<55; i++){
//			int numeroCapitolo = req.getCapitoloUscitaPrevisione().getNumeroCapitolo();
//			req.getCapitoloUscitaPrevisione().setNumeroCapitolo(numeroCapitolo+1);
//			InserisceCapitoloDiUscitaPrevisioneResponse res = inserisceGestioneCapitoloUscitaPrevisioneService.executeService(req);
//			System.out.println("esito: "+res.getEsito());
//		}
		InserisceCapitoloDiUscitaPrevisioneResponse res = inserisceGestioneCapitoloUscitaPrevisioneService.executeService(req);
		
		assertNotNull(res);
		
		System.out.println("esito: "+res.getEsito());
		System.out.println("errori: "+res.getErrori());			
		CapitoloUscitaPrevisione ins = res.getCapitoloUPrevInserito();
		
		assertNotNull(ins);
		System.out.println("inserito id = " + ins.getUid());
	}

	/**
	 * Gets the capitolo u prev.
	 *
	 * @return the capitolo u prev
	 */
	protected CapitoloUscitaPrevisione getCapitoloUPrev() {
		CapitoloUscitaPrevisione getCapitoloUPrev = new CapitoloUscitaPrevisione();
		getCapitoloUPrev.setAnnoCapitolo(2013);
		getCapitoloUPrev.setNumeroCapitolo(1);
		getCapitoloUPrev.setBilancio(getBilancioTest());
		getCapitoloUPrev.setEnte(getEnteTest());
		return getCapitoloUPrev;
	}

	/**
	 * Gets the importi capitolo.
	 *
	 * @param anno the anno
	 * @param importo the importo
	 * @return the importi capitolo
	 */
	protected ImportiCapitoloUP getImportiCapitolo(Integer anno, BigDecimal importo) {
		ImportiCapitoloUP importiCapitoloUP = new ImportiCapitoloUP();

		importiCapitoloUP.setAnnoCompetenza(anno);
		importiCapitoloUP.setStanziamento(importo.add(new BigDecimal("0.1"))); 
		importiCapitoloUP.setStanziamentoCassa(importo.add(new BigDecimal("20000.2")));
		importiCapitoloUP.setStanziamentoResiduo(importo.add(new BigDecimal("10000.4")));

		return importiCapitoloUP;
	}
	
	/**
	 * Test aggiorna.
	 */
	@Test
	public void testAggiorna() {

		Bilancio bilancio = getBilancioTest();

		CapitoloUscitaPrevisione capitoloUscitaPrevisione = new CapitoloUscitaPrevisione();
		capitoloUscitaPrevisione.setUid(15);

		capitoloUscitaPrevisione.setNumeroCapitolo(999);
		capitoloUscitaPrevisione.setNumeroArticolo(1);
		capitoloUscitaPrevisione.setNumeroUEB(2);
		capitoloUscitaPrevisione.setAnnoCapitolo(bilancio.getAnno());
		capitoloUscitaPrevisione.setStatoOperativoElementoDiBilancio(StatoOperativoElementoDiBilancio.VALIDO);
		capitoloUscitaPrevisione.setDescrizione("MIO TESTò CUP " + new Date().toString() +"!!!");
		capitoloUscitaPrevisione.setAnnoCreazioneCapitolo(2011);
		
		capitoloUscitaPrevisione.setFlagAssegnabile(Boolean.TRUE);
		
		capitoloUscitaPrevisione.setExAnnoCapitolo(2013);
		capitoloUscitaPrevisione.setExCapitolo(99);
		capitoloUscitaPrevisione.setExArticolo(1);
		capitoloUscitaPrevisione.setExUEB(2);

		Ente ente = getEnteTest();
		
		
		
		
		Richiedente richiedente = getRichiedenteByProperties("consip","regp");

		ElementoPianoDeiConti elementoPianoDeiConti = new ElementoPianoDeiConti();
		elementoPianoDeiConti.setUid(5986);

		Macroaggregato macroaggregato = new Macroaggregato();
		macroaggregato.setUid(5952);

		Programma programma = new Programma();
		programma.setUid(5829);
		//programma.setDataFineValidita(new Date());

		StrutturaAmministrativoContabile strutturaAmministrativoContabile = new StrutturaAmministrativoContabile();
		strutturaAmministrativoContabile.setUid(406);

//		ClassificazioneCofogProgramma classificazioneCofogProgramma = new ClassificazioneCofogProgramma();
//		classificazioneCofogProgramma.setUid(5245);

		TipoFondo tipoFondo = null;
		TipoFinanziamento tipoFinanziamento = null;
		List<ClassificatoreGenerico> listaClassificatori = null;

		List<ImportiCapitoloUP> listaImporti = new ArrayList<ImportiCapitoloUP>();

		listaImporti.add(getImportiCapitolo(2013, new BigDecimal(2013)));
		listaImporti.add(getImportiCapitolo(2014, new BigDecimal(2014)));
		listaImporti.add(getImportiCapitolo(2015, new BigDecimal(2015)));			
		
		
		AggiornaCapitoloDiUscitaPrevisione req = new AggiornaCapitoloDiUscitaPrevisione();
		req.setRichiedente(richiedente);
		req.setEnte(ente);
		req.setBilancio(bilancio);
		req.setCapitoloUscitaPrevisione(capitoloUscitaPrevisione);
		req.setTipoFondo(tipoFondo);
		req.setTipoFinanziamento(tipoFinanziamento);
		req.setClassificatoriGenerici(listaClassificatori);
		req.setElementoPianoDeiConti(elementoPianoDeiConti);
		req.setStruttAmmContabile(strutturaAmministrativoContabile);
		//req.setClassificazioneCofogProgramma(classificazioneCofogProgramma);
		req.setImportiCapitoloUP(listaImporti);
		req.setMacroaggregato(macroaggregato);
		req.setProgramma(programma);
		
		
		AggiornaCapitoloDiUscitaPrevisioneResponse res = aggiornaCapitoloDiUscitaPrevisioneService.executeService(req);
		
		assertNotNull(res);
		
		System.out.println("esito: "+res.getEsito());
		System.out.println("errori: "+res.getErrori());
		
		
		
		
		CapitoloUscitaPrevisione ins = res.getCapitoloUscitaPrevisione();
		
		//assertNotNull(ins);			

		System.out.println("inserito id = " + ins.getUid());
	}

	/**
	 * Test update.
	 */
	@Test
	public void testUpdate() {

		RicercaPuntualeCapitoloUscitaPrevisione req = new RicercaPuntualeCapitoloUscitaPrevisione();			
		req.setEnte(getEnteTest());
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));

		RicercaPuntualeCapitoloUPrev criteriRicerca = new RicercaPuntualeCapitoloUPrev();
		criteriRicerca.setNumeroCapitolo(17724);
		criteriRicerca.setAnnoEsercizio(2013);
		criteriRicerca.setAnnoCapitolo(2013);
		criteriRicerca.setNumeroArticolo(0);
		criteriRicerca.setStatoOperativoElementoDiBilancio(StatoOperativoElementoDiBilancio.VALIDO);
		req.setRicercaPuntualeCapitoloUPrev(criteriRicerca);

		RicercaPuntualeCapitoloUscitaPrevisioneResponse res = ricercaPuntualeCapitoloUscitaPrevisioneService.executeService(req);
		assertNotNull(res);
//
//		CapitoloUscitaPrevisione capitoloUscitaPrevisione = res.getCapitoloUscitaPrevisione();
//
//		capitoloUscitaPrevisione.setDescrizione(StringUtils.reverse(capitoloUscitaPrevisione
//				.getDescrizione()));
//
//		Ente ente = getEnteTest();
//
//		Richiedente richiedente = getRichiedenteByProperties("consip","regp");
//
//		Bilancio bilancio = getBilancioTest();
	}
	
	
	/**
	 * Test ricerca sintetica descrizione.
	 */
	@Test 
	public void testRicercaSinteticaDescrizione() {

		RicercaSinteticaCapitoloUscitaPrevisione req = new RicercaSinteticaCapitoloUscitaPrevisione();

		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		Ente ente = getEnteTest();
		req.setEnte(ente);

		RicercaSinteticaCapitoloUPrev criteriRicerca = new RicercaSinteticaCapitoloUPrev();
		
		//criteriRicerca.setDescrizioneCapitolo("test"); //Trova TESTà
//		criteriRicerca.setAnnoAttoDilegge(2013);
//		criteriRicerca.setNumeroAttoDilegge(1);

		criteriRicerca.setAnnoEsercizio(2015);
		criteriRicerca.setAnnoCapitolo(2015);
//		criteriRicerca.setNumeroCapitolo(9999);
//		criteriRicerca.setNumeroArticolo(1);
//		criteriRicerca.setNumeroUEB(1);
		
//		criteriRicerca.setStatoOperativo(StatoOperativoElementoDiBilancio.ANNULLATO);
		
		
//		criteriRicerca.setCodicePianoDeiConti("E.9.02.03.00.000"); //trova E.9.02.03.04.000
		
//		criteriRicerca.setCodiceStrutturaAmmCont("005"); //troverà "004" CDC
//		criteriRicerca.setCodiceTipoStrutturaAmmCont("CDR");
		
 		//criteriRicerca.setCodiceMissione("01"); //troverà codice programma "11"
 		//criteriRicerca.setCodiceProgramma("01");
// 		criteriRicerca.setCodiceTitoloUscita("01");
// 		criteriRicerca.setCodiceMacroaggregato("01");
// 		criteriRicerca.setCodicePianoDeiConti("U.1.01.01.01.001");
		
		//criteriRicerca.setCodiceTitoloUscita("1"); //troverà codice Macroaggregato "06"
		
		req.setRicercaSinteticaCapitoloUPrev(criteriRicerca);
		ParametriPaginazione pp = new ParametriPaginazione();
		pp.setNumeroPagina(0);
		pp.setElementiPerPagina(100);

		req.setParametriPaginazione(pp);
		/*gestioneCapitoloUscitaPrevisione.ricercaSinteticaCapitoloUscitaPrevisione(richiedente,
				ente, criteriRicerca, null);*/
		
		req.setTipologieClassificatoriRichiesti(TipologiaClassificatore.SIOPE_SPESA);
		
		RicercaSinteticaCapitoloUscitaPrevisioneResponse res = ricercaSinteticaCapitoloUscitaPrevisioneService.executeService(req);
		res = JAXBUtility.unmarshall(JAXBUtility.marshall(res),RicercaSinteticaCapitoloUscitaPrevisioneResponse.class);
		
		ListaPaginata<CapitoloUscitaPrevisione> list = res.getCapitoli();
		
		log.debug("testRicercaSinteticaDescrizione", "testRicercaSinteticaDescrizione - " + "PaginaCorrente: "+list.getPaginaCorrente() + //Numero pagina restituita
				" TotaleElementi: " + list.getTotaleElementi() + //Numero elementi trovati
				" TotalePagine: "+list.getTotalePagine()		 //Numero pagine trovate			
			);
		
		for(CapitoloUscitaPrevisione cap : list){
			System.out.println("ImportiCapitoloUP expected! -> " + cap.getImportiCapitoloUP().getClass());
			
			System.out.println("SiopeSpesa -> " + cap.getSiopeSpesa());
			
		}
	}
	
	/**
	 * Test ricerca sintetica.
	 */
	@Test
	public void testRicercaSintetica() {

		RicercaSinteticaCapitoloUscitaPrevisione req = new RicercaSinteticaCapitoloUscitaPrevisione();

		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setEnte(req.getRichiedente().getAccount().getEnte());
		req.setParametriPaginazione(new ParametriPaginazione(0, 10));
		req.setCalcolaTotaleImporti(Boolean.TRUE);

		RicercaSinteticaCapitoloUPrev criteriRicerca = new RicercaSinteticaCapitoloUPrev();
		req.setRicercaSinteticaCapitoloUPrev(criteriRicerca);

		criteriRicerca.setAnnoEsercizio(Integer.valueOf(2018));
		criteriRicerca.setAnnoCapitolo(Integer.valueOf(2018));
		criteriRicerca.setCodiceRicorrenteSpesa("3");
//		criteriRicerca.setNumeroCapitolo(Integer.valueOf(1));
//		criteriRicerca.setNumeroArticolo(1);
//		
//		
//		criteriRicerca.setCodicePianoDeiConti("E.9.02.03.00.000"); //trova E.9.02.03.04.000
//		
//		criteriRicerca.setCodiceStrutturaAmmCont("005"); //troverà "004" CDC
//		criteriRicerca.setCodiceTipoStrutturaAmmCont("CDR");
//		
//		criteriRicerca.setCodiceMissione("01"); //troverà codice programma "11"
//		
//		criteriRicerca.setCodiceTitoloUscita("1"); //troverà codice Macroaggregato "06"
		

		/*gestioneCapitoloUscitaPrevisione.ricercaSinteticaCapitoloUscitaPrevisione(richiedente,
				ente, criteriRicerca, null);*/
		
		RicercaSinteticaCapitoloUscitaPrevisioneResponse res = ricercaSinteticaCapitoloUscitaPrevisioneService.executeService(req);
		assertNotNull(res);
	}

	/**
	 * Test ricerca puntuale.
	 */
	@Test
	public void testRicercaPuntuale() {
		RicercaPuntualeCapitoloUscitaPrevisione req = new RicercaPuntualeCapitoloUscitaPrevisione();

		
		Richiedente richiedente = new Richiedente();
		Operatore operatore = new Operatore();
		operatore.setCodiceFiscale("LSIDNC32242");
		richiedente.setOperatore(operatore);
		Account a = new Account();
		a.setUid(1);
		richiedente.setAccount(a);
		req.setRichiedente(richiedente);
		
		
		Ente ente = getEnteTest();
		req.setEnte(ente);
		

		RicercaPuntualeCapitoloUPrev criteriRicerca = new RicercaPuntualeCapitoloUPrev();

		/*criteriRicerca.setAnnoEsercizio(2013);
		criteriRicerca.setAnnoCapitolo(2013);
		criteriRicerca.setNumeroCapitolo(1234567);
		criteriRicerca.setNumeroArticolo(1234567);*/
		
		criteriRicerca.setAnnoEsercizio(2013);
		criteriRicerca.setAnnoCapitolo(2013);
		criteriRicerca.setNumeroCapitolo(17657);
		criteriRicerca.setNumeroArticolo(1);
		criteriRicerca.setNumeroUEB(1);
		
		/*
		 * capitoloUscitaPrevisione.setNumeroCapitolo(17643);
		capitoloUscitaPrevisione.setNumeroArticolo(0);
		capitoloUscitaPrevisione.setAnnoCapitolo(bilancio.getAnno());
		capitoloUscitaPrevisione.setStatoOperativoElementoDiBilancio(StatoOperativoElementoDiBilancio.VALIDO);
		capitoloUscitaPrevisione.setDescrizione("TEST CUP " + new Date().toString());
		capitoloUscitaPrevisione.setAnnoCreazioneCapitolo(2011);
		 */

		criteriRicerca.setStatoOperativoElementoDiBilancio(StatoOperativoElementoDiBilancio.VALIDO);
		req.setRicercaPuntualeCapitoloUPrev(criteriRicerca);
		

		/*gestioneCapitoloUscitaPrevisione.ricercaPuntualeCapitoloUscitaPrevisione(richiedente,
				ente, criteriRicerca);*/
				
				
		RicercaPuntualeCapitoloUscitaPrevisioneResponse res = ricercaPuntualeCapitoloUscitaPrevisioneService.executeService(req);
		
		System.out.println("test result: "+res.getCapitoloUscitaPrevisione());
	}

	/**
	 * Test ricerca dettaglio.
	 */
	@Test
	public void testRicercaDettaglio() {

		RicercaDettaglioCapitoloUscitaPrevisione req = new RicercaDettaglioCapitoloUscitaPrevisione();

		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		req.setEnte(getEnteTest());

		RicercaDettaglioCapitoloUPrev criteriRicerca = new RicercaDettaglioCapitoloUPrev();

		//criteriRicerca.setChiaveCapitolo(817950);
		//criteriRicerca.setChiaveCapitolo(2045426850);
		
		//criteriRicerca.setChiaveCapitolo(2045425050);
		
		//criteriRicerca.setChiaveCapitolo(40908935);//40908523 //40908670
		
		criteriRicerca.setChiaveCapitolo(5067); //5067//87);

		req.setRicercaDettaglioCapitoloUPrev(criteriRicerca);
		
		
		RicercaDettaglioCapitoloUscitaPrevisioneResponse res = ricercaDettaglioCapitoloUscitaPrevisioneService.executeService(req);
		
		System.out.println("lista classificatori: "+res.getListaClassificatori());
		
		System.out.println("res.getListaClassificatori()!=null? " +(res.getListaClassificatori()!=null));
	}
	
	/**
	 * Test ricerca dettaglio entrata previsione.
	 */
	@Test
	public void testRicercaDettaglioEntrataPrevisione() {
		RicercaDettaglioCapitoloEntrataPrevisione req = new RicercaDettaglioCapitoloEntrataPrevisione();
		
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setAnnoBilancio(Integer.valueOf(2017));
		req.setEnte(req.getRichiedente().getAccount().getEnte());
		req.setImportiDerivatiRichiesti(EnumSet.allOf(ImportiCapitoloEnum.class));
		
		RicercaDettaglioCapitoloEPrev ricercaDettaglioCapitoloEPrev = new RicercaDettaglioCapitoloEPrev();
		ricercaDettaglioCapitoloEPrev.setChiaveCapitolo(87048);
		req.setRicercaDettaglioCapitoloEPrev(ricercaDettaglioCapitoloEPrev);
		
		RicercaDettaglioCapitoloEntrataPrevisioneResponse res = ricercaDettaglioCapitoloEntrataPrevisioneService.executeService(req);
		assertNotNull(res);
	}
	
	@Test
	public void aggiornaCapitoloDiEntrataPrevisione() {
		AggiornaCapitoloDiEntrataPrevisione req = new AggiornaCapitoloDiEntrataPrevisione();
		
		req.setAnnoBilancio(Integer.valueOf(2017));
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		
		CapitoloEntrataPrevisione c = create(CapitoloEntrataPrevisione.class, 87048);
		c.setAnnoCapitolo(Integer.valueOf(2017));
		c.setAnnoCreazioneCapitolo(Integer.valueOf(2017));
		c.setBilancio(getBilancioTest(131, 2017));
		c.setCategoriaCapitolo(create(CategoriaCapitolo.class, 3));
		c.getClassificatoriGenerici().add(create(ClassificatoreGenerico.class, 75602933));
		c.getClassificatoriGenerici().add(create(ClassificatoreGenerico.class, 75602950));
		c.getClassificatoriGenerici().add(create(ClassificatoreGenerico.class, 75644385));
		c.setDescrizione("TRASFERIMENTO DI FONDI PER IL FINANZIAMENTO DEL PROGETTO &quot;RURBANCE&quot;, NELL'AMBITO DEL PROGRAMMA ALPINE SPACE(REG.CE N.1083/2006) - FONDI STATALI");
		c.setDescrizioneArticolo("");
		c.setElementoPianoDeiConti(create(ElementoPianoDeiConti.class, 127581));
		c.setEnte(req.getRichiedente().getAccount().getEnte());
		c.setFlagImpegnabile(Boolean.TRUE);
		c.setFlagRilevanteIva(Boolean.TRUE);
		c.setImportiCapitolo(creaImportiCapitoloEP(Integer.valueOf(2017), new BigDecimal("5000")));
		c.getListaImportiCapitolo().add(creaImportiCapitoloEP(Integer.valueOf(2017), new BigDecimal("5000")));
		c.getListaImportiCapitolo().add(creaImportiCapitoloEP(Integer.valueOf(2018), new BigDecimal("0")));
		c.getListaImportiCapitolo().add(creaImportiCapitoloEP(Integer.valueOf(2019), new BigDecimal("0")));
		c.setNote("");
		c.setNumeroArticolo(Integer.valueOf(0));
		c.setNumeroCapitolo(Integer.valueOf(22042));
		c.setNumeroUEB(Integer.valueOf(1));
		c.setStatoOperativoElementoDiBilancio(StatoOperativoElementoDiBilancio.VALIDO);
		c.setStrutturaAmministrativoContabile(create(StrutturaAmministrativoContabile.class, 1719663));
		c.setTipoCapitolo(TipoCapitolo.CAPITOLO_ENTRATA_PREVISIONE);
		c.setTipoFinanziamento(create(TipoFinanziamento.class, 75602594));
		c.setTipoFondo(create(TipoFondo.class, 75602541));
		c.setUidCapitoloEquivalente(59627);
		c.setUidExCapitolo(0);
		c.setCategoriaTipologiaTitolo(create(CategoriaTipologiaTitolo.class, 119680));
		c.setFlagEntrateRicorrenti(Boolean.FALSE);
		c.setFlagPerMemoria(Boolean.FALSE);
		c.setPerimetroSanitarioEntrata(create(PerimetroSanitarioEntrata.class, 22682));
		c.setRicorrenteEntrata(create(RicorrenteEntrata.class, 22747));
		c.setSiopeEntrata(create(SiopeEntrata.class, 75639294));
		c.setTipologiaTitolo(create(TipologiaTitolo.class, 119679));
		c.setTitoloEntrata(create(TitoloEntrata.class, 119678));
		c.setTransazioneUnioneEuropeaEntrata(create(TransazioneUnioneEuropeaEntrata.class, 22707));
		c.setFlagAccertatoPerCassa(Boolean.TRUE);
		req.setCapitoloEntrataPrevisione(c);
		
		AggiornaCapitoloDiEntrataPrevisioneResponse res = aggiornaCapitoloDiEntrataPrevisioneService.executeService(req);
		assertNotNull(res);
		
		if(!res.hasErrori()) {
			testRicercaDettaglioEntrataPrevisione();
		}
	}
	
	private ImportiCapitoloEP creaImportiCapitoloEP(Integer annoCompetenza, BigDecimal impegnatoPlur) {
		ImportiCapitoloEP i = new ImportiCapitoloEP();
		i.setAnnoCompetenza(annoCompetenza);
		i.setImpegnatoPlur(impegnatoPlur);
		return i;
	}

	/**
	 * Test ricerca movimenti capitolo uscita previsione.
	 */
	@Test
	public void testRicercaMovimentiCapitoloUscitaPrevisione() {
		RicercaMovimentiCapitoloUscitaPrevisione req = new RicercaMovimentiCapitoloUscitaPrevisione();

		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		req.setEnte(getEnteTest());
		req.setBilancio(getBilancioTest());
		req.setCapitoloUPrev(getCapitoloUPrev());
				
		RicercaMovimentiCapitoloUscitaPrevisioneResponse res = ricercaMovimentiCapitoloUscitaPrevisioneService.executeService(req);
		assertNotNull(res);
	}
	
	/**
	 * Test ricerca variazioni capitolo uscita previsione.
	 */
	@Test
	public void testRicercaVariazioniCapitoloUscitaPrevisione() {
		RicercaVariazioniCapitoloUscitaPrevisione req = new RicercaVariazioniCapitoloUscitaPrevisione();

		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		CapitoloUscitaPrevisione cap = new CapitoloUscitaPrevisione();
		cap.setEnte(getEnteTest());
		cap.setBilancio(getBilancioTest());
		cap.setUid(40908516);
		req.setCapitoloUscitaPrev(cap);
					
		RicercaVariazioniCapitoloUscitaPrevisioneResponse res = ricercaVariazioniCapitoloUscitaPrevisioneService.executeService(req);
		assertNotNull(res);
	}
	
	/**
	 * Test verifica annullabilita capitolo uscita previsione.
	 */
	@Test
	public void testVerificaAnnullabilitaCapitoloUscitaPrevisione() {
		VerificaAnnullabilitaCapitoloUscitaPrevisione req = new VerificaAnnullabilitaCapitoloUscitaPrevisione();

		req.setDataOra(new Date());
		//req.setRichiedente(getRichiedenteByProperties("forn2", "fittizio"));
		req.setRichiedente(getRichiedenteByProperties("dev", "ente1"));
		req.setEnte(req.getRichiedente().getAccount().getEnte());
		//req.setBilancio(getBilancioTest(164, 2017));
		req.setBilancio(getBilancioTest(16, 2015));
		
		CapitoloUscitaPrevisione cup = getCapitoloUPrev();
		cup.setAnnoCapitolo(2015);
		cup.setNumeroCapitolo(18052017);
		cup.setNumeroArticolo(0);
		cup.setNumeroUEB(1);
		cup.setStatoOperativoElementoDiBilancio(StatoOperativoElementoDiBilancio.VALIDO);
		
		req.setCapitolo(cup);
		req.setDataOra(new Date());
		
		VerificaAnnullabilitaCapitoloUscitaPrevisioneResponse res = verificaAnnullabilitaCapitoloUscitaPrevisioneService.executeService(req);

		assertNotNull(res);
	}
	
	/**
	 * Test verifica eliminabilita capitolo uscita gestione.
	 */
	@Test
	public void testVerificaEliminabilitaCapitoloUscitaGestione() {
		VerificaEliminabilitaCapitoloUscitaPrevisione req = new VerificaEliminabilitaCapitoloUscitaPrevisione();

		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		req.setEnte(getEnteTest());
		req.setBilancio(getBilancioTest());
		CapitoloUscitaPrevisione cup = getCapitoloUPrev();
		cup.setAnnoCapitolo(2013);
		cup.setNumeroCapitolo(17716);
		cup.setNumeroArticolo(1);
		cup.setStatoOperativoElementoDiBilancio(StatoOperativoElementoDiBilancio.VALIDO);
		
		req.setCapitoloUscitaPrev(cup);
		req.setDataOra(new Date());
				
		VerificaEliminabilitaCapitoloUscitaPrevisioneResponse res = verificaEliminabilitaCapitoloUscitaPrevisioneService.executeService(req);

		assertFalse("Esito Negativo", res.isFallimento());
		assertTrue("Il capitolo non è annullabile!", res.isEliminabilitaCapitolo());
	}
	
	/**
	 * Test elimina.
	 */
	@Test
	public void testElimina() {
		EliminaCapitoloUscitaPrevisione serviceRequest = new EliminaCapitoloUscitaPrevisione();
		serviceRequest.setRichiedente(getRichiedenteByProperties("consip","regp"));
		serviceRequest.setEnte(getEnteTest());
		serviceRequest.setBilancio(getBilancioTest());
		CapitoloUscitaPrevisione capitoloUscitaPrevisione = new CapitoloUscitaPrevisione();
		//capitoloUscitaPrevisione.setUid(40908554);
		serviceRequest.setCapitoloUscitaPrev(capitoloUscitaPrevisione);	
		
		capitoloUscitaPrevisione.setNumeroCapitolo(17719);
		capitoloUscitaPrevisione.setNumeroArticolo(1);
		capitoloUscitaPrevisione.setNumeroUEB(1);
		capitoloUscitaPrevisione.setAnnoCapitolo(2013);
		capitoloUscitaPrevisione.setStatoOperativoElementoDiBilancio(StatoOperativoElementoDiBilancio.VALIDO);
		//capitoloUscitaPrevisione.setDescrizione("MIO TEST CUP " + new Date().toString() +"!!!");
		//capitoloUscitaPrevisione.setAnnoCreazioneCapitolo(2011);
		
		
		eliminaCapitoloUscitaPrevisioneService.executeService(serviceRequest);
	}
	
	
	
	@Test
	public void ricercaMovimentiCapitoloUscitaGestioneService(){
		RicercaMovimentiCapitoloUscitaGestione req = new RicercaMovimentiCapitoloUscitaGestione();
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		req.setBilancio(getBilancioTest());
		req.setEnte(getEnteTest());
		CapitoloUscitaGestione capitoloUscitaGestione = new CapitoloUscitaGestione();
		capitoloUscitaGestione.setUid(2);
		req.setCapitoloUscitaGestione(capitoloUscitaGestione);
		RicercaMovimentiCapitoloUscitaGestioneResponse res = ricercaMovimentiCapitoloUscitaGestioneService.executeService(req);
		assertNotNull(res);
		
	}
	
}
