/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.progetto;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Locale;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacbilser.business.service.progetto.AggiornaAnagraficaCronoprogrammaService;
import it.csi.siac.siacbilser.business.service.progetto.AggiornaRigaEntrataService;
import it.csi.siac.siacbilser.business.service.progetto.AggiornaRigaSpesaService;
import it.csi.siac.siacbilser.business.service.progetto.AnnullaCronoprogrammaService;
import it.csi.siac.siacbilser.business.service.progetto.InserisceCronoprogrammaService;
import it.csi.siac.siacbilser.business.service.progetto.InserisceRigaEntrataService;
import it.csi.siac.siacbilser.business.service.progetto.InserisceRigaSpesaService;
import it.csi.siac.siacbilser.business.service.progetto.RicercaCronoprogrammaService;
import it.csi.siac.siacbilser.business.service.progetto.RicercaDeiCronoprogrammiCollegatiAlProgettoPerBilancioService;
import it.csi.siac.siacbilser.business.service.progetto.RicercaDeiCronoprogrammiCollegatiAlProgettoService;
import it.csi.siac.siacbilser.business.service.progetto.RicercaDettaglioCronoprogrammaService;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaAnagraficaCronoprogramma;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaAnagraficaCronoprogrammaResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaRigaEntrata;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaRigaEntrataResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaRigaSpesa;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaRigaSpesaResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.AnnullaCronoprogramma;
import it.csi.siac.siacbilser.frontend.webservice.msg.AnnullaCronoprogrammaResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceCronoprogramma;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceCronoprogrammaResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceRigaEntrata;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceRigaSpesa;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaCronoprogramma;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaCronoprogrammaResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDeiCronoprogrammiCollegatiAlProgetto;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDeiCronoprogrammiCollegatiAlProgettoPerBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDeiCronoprogrammiCollegatiAlProgettoPerBilancioResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDeiCronoprogrammiCollegatiAlProgettoResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCronoprogramma;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCronoprogrammaResponse;
import it.csi.siac.siacbilser.model.CapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.model.Cronoprogramma;
import it.csi.siac.siacbilser.model.DettaglioEntrataCronoprogramma;
import it.csi.siac.siacbilser.model.DettaglioUscitaCronoprogramma;
import it.csi.siac.siacbilser.model.Missione;
import it.csi.siac.siacbilser.model.Progetto;
import it.csi.siac.siacbilser.model.Programma;
import it.csi.siac.siacbilser.model.QuadroEconomico;
import it.csi.siac.siacbilser.model.StatoOperativoCronoprogramma;
import it.csi.siac.siacbilser.model.TipoProgetto;
import it.csi.siac.siacbilser.model.TipologiaTitolo;
import it.csi.siac.siacbilser.model.TitoloEntrata;
import it.csi.siac.siacbilser.model.TitoloSpesa;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Ente;

// TODO: Auto-generated Javadoc
/**
 * The Class CronoprogrammaDLTest.
 */
public class CronoprogrammaTest extends BaseJunit4TestCase {
	
	
	/** The inserisce cronoprogramma service. */
	@Autowired
	private InserisceCronoprogrammaService inserisceCronoprogrammaService;
	
	/** The aggiorna anagrafica cronoprogramma service. */
	@Autowired
	private AggiornaAnagraficaCronoprogrammaService aggiornaAnagraficaCronoprogrammaService;
	
	/** The annulla cronoprogramma service. */
	@Autowired
	private AnnullaCronoprogrammaService annullaCronoprogrammaService;
	
	/** The ricerca cronoprogramma service. */
	@Autowired
	private RicercaCronoprogrammaService ricercaCronoprogrammaService;
	
	/** The ricerca dettaglio cronoprogramma service. */
	@Autowired
	private RicercaDettaglioCronoprogrammaService ricercaDettaglioCronoprogrammaService;
	
	/** The ricerca dei cronoprogrammi collegati al progetto service. */
	@Autowired
	private RicercaDeiCronoprogrammiCollegatiAlProgettoService ricercaDeiCronoprogrammiCollegatiAlProgettoService;
		
	/** The inserisce riga entrata service. */
	@Autowired
	private InserisceRigaEntrataService inserisceRigaEntrataService;
	
	/** The inserisce riga spesa service. */
	@Autowired
	private InserisceRigaSpesaService inserisceRigaSpesaService;
	
	
	/** The aggiorna riga entrata service. */
	@Autowired
	private AggiornaRigaEntrataService aggiornaRigaEntrataService;
	
	/** The aggiorna riga spesa service. */
	@Autowired
	private AggiornaRigaSpesaService aggiornaRigaSpesaService;
	
	@Autowired
	private RicercaDeiCronoprogrammiCollegatiAlProgettoPerBilancioService ricercaDeiCronoprogrammiCollegatiAlProgettoPerBilancioService;
	
//	/** The calcolo fondo pluriennale vincolato cronoprogramma service. */
//	@Autowired
//	private CalcoloFondoPluriennaleVincolatoCronoprogrammaService calcoloFondoPluriennaleVincolatoCronoprogrammaService;
	
	
	/** The df. */
	private static DecimalFormat df;
	
	static {
		df = (DecimalFormat) NumberFormat.getInstance(Locale.ITALY);
		df.setMinimumFractionDigits(2);
		df.setMaximumFractionDigits(2);
	}
	
	/**
	 * Inserisci anagrafica cronoprogramma.
	 */
	@Test
	public void inserisciAnagraficaCronoprogramma() {
			
		InserisceCronoprogramma req = new InserisceCronoprogramma();
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		req.setAnnoBilancio(2017);
		Cronoprogramma c = new Cronoprogramma();
		c = new Cronoprogramma();
		c.setBilancio(getBilancioByProperties("consip","regp", "2017"));
		c.setEnte(req.getRichiedente().getAccount().getEnte());
		c.setCodice("codice 018");
		c.setDescrizione("mia descrizione");
		c.setNote("mie note cronoprogramma");
		c.setStatoOperativoCronoprogramma(StatoOperativoCronoprogramma.VALIDO);
		c.setCronoprogrammaDaDefinire(Boolean.TRUE);
		Date now = new Date();
		
		c.setDataAggiudicazioneLavori(now);
		c.setDataApprovazioneFattibilita(now);
		c.setDataApprovazioneProgettoDefinitivo(now);
		c.setDataApprovazioneProgettoEsecutivo(now);
		c.setDataAvvioProcedura(now);
		c.setDataCollaudo(now);
		c.setDataFineLavori(now);
		c.setDataInizioLavori(now);
		c.setGestioneQuadroEconomico(Boolean.FALSE);
//		c.setAttoAmministrativo(create(AttoAmministrativo.class, 35076));
		c.setAttoAmministrativo(create(AttoAmministrativo.class, 44002));
		c.setDurataInGiorni(Integer.valueOf(13));
		
		Progetto progetto = new Progetto();
		progetto.setUid(57);
		c.setProgetto(progetto);
		
		req.setCronoprogramma(c);
		

		DettaglioUscitaCronoprogramma duc = new DettaglioUscitaCronoprogramma();
		c.getCapitoliUscita().add(duc);
		duc.setAnnoCompetenza(2017);
		duc.setDescrizioneCapitolo("Test spesa");
		duc.setNumeroArticolo(2);
		duc.setNumeroCapitolo(5211);
		duc.setStanziamento(new BigDecimal("40"));
		duc.setAnnoEntrata(2017);
		duc.setMissione(create(Missione.class, 118441));
		duc.setProgramma(create(Programma.class, 118522));
		duc.setTitoloSpesa(create(TitoloSpesa.class, 118959));
		duc.setQuadroEconomico(create(QuadroEconomico.class, 0));
//		duc.setImportoQuadroEconomico(new BigDecimal("5"));
		
		
		DettaglioEntrataCronoprogramma dec = new DettaglioEntrataCronoprogramma();
		c.getCapitoliEntrata().add(dec);
		dec.setAnnoCompetenza(2017);
		dec.setDescrizioneCapitolo("Test entrata");
		dec.setNumeroArticolo(2);
		dec.setNumeroCapitolo(5211);
		dec.setStanziamento(new BigDecimal("40"));
		dec.setTitoloEntrata(create(TitoloEntrata.class, 119613));
		dec.setTipologiaTitolo(create(TipologiaTitolo.class, 119614));
		dec.setIsAvanzoAmministrazione(Boolean.TRUE);
		
		
		InserisceCronoprogrammaResponse res = inserisceCronoprogrammaService.executeService(req);

		assertNotNull(res);
	}
	
	/**
	 * Inserisci cronoprogramma.
	 */
	@Test
	public void inserisciCronoprogramma() {
		InserisceCronoprogramma req = new InserisceCronoprogramma();
		
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "crp"));
		
		Cronoprogramma c = new Cronoprogramma();
		req.setCronoprogramma(c);
		c.setBilancio(getBilancioTest(143, 2017));
		c.setCodice("V01");
		c.setCronoprogrammaDaDefinire(Boolean.FALSE);
		c.setDescrizione("Test 3");
		c.setEnte(req.getRichiedente().getAccount().getEnte());
		c.setProgetto(create(Progetto.class, 237));
		c.setStatoOperativoCronoprogramma(StatoOperativoCronoprogramma.VALIDO);
		c.setUsatoPerFpv(Boolean.FALSE);
		
		DettaglioUscitaCronoprogramma duc = new DettaglioUscitaCronoprogramma();
		c.getCapitoliUscita().add(duc);
		duc.setAnnoCompetenza(2017);
		duc.setDescrizioneCapitolo("Test spesa");
		duc.setNumeroArticolo(2);
		duc.setNumeroCapitolo(5211);
		duc.setStanziamento(new BigDecimal("100"));
		duc.setAnnoEntrata(2017);
		duc.setMissione(create(Missione.class, 118807));
		duc.setProgramma(create(Programma.class, 118808));
		duc.setTitoloSpesa(create(TitoloSpesa.class, 119044));
		
		DettaglioEntrataCronoprogramma dec = new DettaglioEntrataCronoprogramma();
		c.getCapitoliEntrata().add(dec);
		dec.setAnnoCompetenza(2017);
		dec.setDescrizioneCapitolo("Test entrata");
		dec.setNumeroArticolo(2);
		dec.setNumeroCapitolo(5211);
		dec.setStanziamento(new BigDecimal("100"));
		dec.setTitoloEntrata(create(TitoloEntrata.class, 120204));
		dec.setTipologiaTitolo(create(TipologiaTitolo.class, 120205));
		
		InserisceCronoprogrammaResponse res = inserisceCronoprogrammaService.executeService(req);

		assertNotNull(res);
	}
	
	/**
	 * Inserisce riga entrata.
	 */
	@Test
	public void inserisceRigaEntrata() {
	
		InserisceRigaEntrata req = new InserisceRigaEntrata();
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		
		DettaglioEntrataCronoprogramma dec = new DettaglioEntrataCronoprogramma();		
		dec.setEnte(getEnteTest());
		
		dec.setAnnoCompetenza(2013);
		dec.setNumeroCapitolo(null);
		dec.setDescrizioneCapitolo("mia desc capitolooo con classif!");
		dec.setDescrizioneArticolo("mia desc articolo con classif! ");
		dec.setNumeroArticolo(10);
		dec.setNumeroUEB(1);
		dec.setStanziamento(new BigDecimal("1992.02"));
		dec.setCronoprogramma(new Cronoprogramma());
		dec.getCronoprogramma().setUid(1);
		
		TipologiaTitolo tipologiaTitolo = new TipologiaTitolo();
		tipologiaTitolo.setUid(5452);
		dec.setTipologiaTitolo(tipologiaTitolo);
		
		CapitoloEntrataPrevisione cap = new CapitoloEntrataPrevisione();
		cap.setUid(40908817);
		dec.setCapitolo(cap);
		
		/*
		 Per ricercare un capitolo entrata previsione:
		 
		 	select * from siac_t_bil_elem e, siac_d_bil_elem_tipo t
			where e.elem_tipo_id = t.elem_tipo_id 
			and elem_tipo_code = 'CAP-EP'
		 */
		
		req.setDettaglioEntrataCronoprogramma(dec);
		inserisceRigaEntrataService.executeService(req);
	}
	
	/**
	 * Inserisce riga spesa.
	 */
	@Test
	public void inserisceRigaSpesa() {
	
		InserisceRigaSpesa req = new InserisceRigaSpesa();
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		
		DettaglioUscitaCronoprogramma dec = new DettaglioUscitaCronoprogramma();		
		dec.setEnte(getEnteTest());
		
		dec.setAnnoEntrata(2014);
		dec.setAnnoCompetenza(2013);
		dec.setNumeroCapitolo(100);
		dec.setDescrizioneCapitolo("mia desc capitolooo spesa ;-)");
		dec.setDescrizioneArticolo("mia desc articolo spesa ;-)");
		dec.setNumeroArticolo(10);
		dec.setNumeroUEB(1);
		dec.setStanziamento(new BigDecimal("1990.99"));
		dec.setCronoprogramma(new Cronoprogramma());
		dec.getCronoprogramma().setUid(1);
		
		req.setDettaglioUscitaCronoprogramma(dec);
		inserisceRigaSpesaService.executeService(req);
	}
	
	/**
	 * Aggiorna riga spesa.
	 */
	@Test
	public void aggiornaRigaSpesa() {
	
		AggiornaRigaSpesa req = new AggiornaRigaSpesa();
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		
		DettaglioUscitaCronoprogramma dec = new DettaglioUscitaCronoprogramma();	
		dec.setUid(8);
		dec.setEnte(getEnteTest());
		
		dec.setAnnoEntrata(2014);
		dec.setAnnoCompetenza(2013);
		dec.setNumeroCapitolo(101);
		dec.setDescrizioneCapitolo("mia desc capitolooo spesa AGG ;-)");
		dec.setDescrizioneArticolo("mia desc articolo spesa AGG ;-)");
		dec.setNumeroArticolo(11);
		dec.setNumeroUEB(2);
		dec.setStanziamento(new BigDecimal("1991.99"));
		dec.setCronoprogramma(new Cronoprogramma());
		dec.getCronoprogramma().setUid(1);
		
		req.setDettaglioUscitaCronoprogramma(dec);
		
		AggiornaRigaSpesaResponse res = aggiornaRigaSpesaService.executeService(req);
		assertNotNull(res);
	}
	
	
	/**
	 * Aggiorna riga entrata.
	 */
	@Test
	public void aggiornaRigaEntrata() {
	
		AggiornaRigaEntrata req = new AggiornaRigaEntrata();
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		
		DettaglioEntrataCronoprogramma dec = new DettaglioEntrataCronoprogramma();	
		dec.setUid(11);
		dec.setEnte(getEnteTest());
		
		//dec.setAnnoEntrata(2014);
		dec.setAnnoCompetenza(2013);
		dec.setNumeroCapitolo(101);
		dec.setDescrizioneCapitolo("mia desc capitolooo con classif! AGG");
		dec.setDescrizioneArticolo("mia desc articolo con classif! AGG");
		dec.setNumeroArticolo(11);
		dec.setNumeroUEB(2);
		dec.setStanziamento(new BigDecimal("1999.99"));
		dec.setCronoprogramma(new Cronoprogramma());
		dec.getCronoprogramma().setUid(1);
		
		TipologiaTitolo tipologiaTitolo = new TipologiaTitolo();
		tipologiaTitolo.setUid(5474);
		dec.setTipologiaTitolo(tipologiaTitolo);
		
		CapitoloEntrataPrevisione cap = new CapitoloEntrataPrevisione();
		cap.setUid(40908819);
		dec.setCapitolo(cap);
		
		req.setDettaglioEntrataCronoprogramma(dec);
		
		AggiornaRigaEntrataResponse res = aggiornaRigaEntrataService.executeService(req);
		assertNotNull(res);
	}
	
	
	
	/**
	 * Aggiorna anagrafica cronoprogramma.
	 */
	@Test
	public void aggiornaAnagraficaCronoprogramma() {
		
		RicercaDettaglioCronoprogramma reqRicerca = new RicercaDettaglioCronoprogramma();
		reqRicerca.setRichiedente(getRichiedenteByProperties("consip","regp"));
		Cronoprogramma cr = new Cronoprogramma();		
		cr.setUid(905);
		
		reqRicerca.setCronoprogramma(cr);

		RicercaDettaglioCronoprogrammaResponse resRicerca = ricercaDettaglioCronoprogrammaService.executeService(reqRicerca);

		assertNotNull(resRicerca);
		assertNotNull(resRicerca.getCronoprogramma());
			
		AggiornaAnagraficaCronoprogramma req = new AggiornaAnagraficaCronoprogramma();
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		Cronoprogramma c = new Cronoprogramma();
		c = resRicerca.getCronoprogramma();
		c.setCodice(c.getCodice() + " -2");
//		c.setDescrizione( c.getDescrizione() + " - aggiornata");
//		c.setAttoAmministrativo(create(AttoAmministrativo.class, 35081));
//       c.setUsatoPerFpv(Boolean.TRUE);
       c.setStatoOperativoCronoprogramma(StatoOperativoCronoprogramma.VALIDO);
		req.setCronoprogramma(c);
		AggiornaAnagraficaCronoprogrammaResponse res = aggiornaAnagraficaCronoprogrammaService.executeService(req);

		assertNotNull(res);
	}
	
	
	/**
	 * Annulla cronoprogramma.
	 */
	@Test
	public void annullaCronoprogramma() {
			
		AnnullaCronoprogramma req = new AnnullaCronoprogramma();
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		
		Cronoprogramma c = new Cronoprogramma();
		c.setUid(1);
		req.setCronoprogramma(c);

		AnnullaCronoprogrammaResponse res = annullaCronoprogrammaService.executeService(req);

		assertNotNull(res);
	}
	
	
	/**
	 * Ricerca cronoprogramma.
	 */
	@Test
	public void ricercaCronoprogramma() {
		final String methodName = "ricercaCronoprogramma";
			
		RicercaCronoprogramma req = new RicercaCronoprogramma();
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		Cronoprogramma c = new Cronoprogramma();		
		c.setBilancio(getBilancioByProperties("consip", "regp", "2017"));
		c.setEnte(req.getRichiedente().getAccount().getEnte());;
		
		//c.setUid(1);
		
		c.setCodice("04");
		c.setStatoOperativoCronoprogramma(StatoOperativoCronoprogramma.VALIDO);
		
		c.setProgetto(new Progetto());
		c.getProgetto().setUid(60);
//		c.getProgetto().setCodice("EL-5954");
//		c.getProgetto().setStatoOperativoProgetto(StatoOperativoProgetto.VALIDO);
		
		
		req.setCronoprogramma(c);

		RicercaCronoprogrammaResponse res = ricercaCronoprogrammaService.executeService(req);
		boolean cronoTrovati = res.getCronoprogramma() != null && !res.getCronoprogramma().isEmpty();
		log.debug(methodName, "trovati " + (cronoTrovati ? ( "" + res.getCronoprogramma().size()) : "0") + "cronoprogrammi." );

		assertNotNull(res);
	}
	
	
	
	/**
	 * Ricerca dettaglio cronoprogramma.
	 */
	@Test
	public void ricercaDettaglioCronoprogramma() {
			
		RicercaDettaglioCronoprogramma req = new RicercaDettaglioCronoprogramma();
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		Cronoprogramma c = new Cronoprogramma();		
//		c.setBilancio(getBilancioTest());
//		c.setEnte(getEnteTest());
		
//		c.setUid(42);
		c.setUid(51);
		
//		//c.setUid(1);
//		
//		c.setCodice("ciao codice AGG2");
//		c.setStatoOperativoCronoprogramma(StatoOperativoCronoprogramma.VALIDO);
//		
//		c.setProgetto(new Progetto());
//		c.getProgetto().setUid(1);;
//		c.getProgetto().setCodice("PRG-0001-TST");
//		c.getProgetto().setStatoOperativoProgetto(StatoOperativoProgetto.VALIDO);
		
		
		req.setCronoprogramma(c);

		RicercaDettaglioCronoprogrammaResponse res = ricercaDettaglioCronoprogrammaService.executeService(req);

		assertNotNull(res);
	}
	
	/**
	 * Ricerca dei cronoprogrammi collegati al progetto.
	 */
	@Test
	public void ricercaDeiCronoprogrammiCollegatiAlProgettoPerBilancio() {
			
		RicercaDeiCronoprogrammiCollegatiAlProgettoPerBilancio req = new RicercaDeiCronoprogrammiCollegatiAlProgettoPerBilancio();
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		req.setProgetto(create(Progetto.class, 60));
		req.setBilancio(getBilancioByProperties("consip","regp", "2017"));
		req.setAnnoBilancio(req.getBilancio().getAnno());

		RicercaDeiCronoprogrammiCollegatiAlProgettoPerBilancioResponse res = ricercaDeiCronoprogrammiCollegatiAlProgettoPerBilancioService.executeService(req);

		assertNotNull(res);
	}

	
	/**
	 * Ricerca dei cronoprogrammi collegati al progetto.
	 */
	@Test
	public void ricercaDeiCronoprogrammiCollegatiAlProgetto() {
			
		RicercaDeiCronoprogrammiCollegatiAlProgetto req = new RicercaDeiCronoprogrammiCollegatiAlProgetto();
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
//		req.setProgetto(create(Progetto.class, 60));
		req.setProgetto(create(Progetto.class, 0));
		req.getProgetto().setCodice("6255-GEST");
		req.getProgetto().setTipoProgetto(TipoProgetto.GESTIONE);
		req.setAnnoBilancioCronoprogrammi(2017);
		

		RicercaDeiCronoprogrammiCollegatiAlProgettoResponse res = ricercaDeiCronoprogrammiCollegatiAlProgettoService.executeService(req);

		assertNotNull(res);
	}
	
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.test.BaseJunit4TestCase#getBilancioTest()
	 */
	protected Bilancio getBilancioTest() {
		Bilancio bilancio = new Bilancio();
		bilancio.setUid(1);
		bilancio.setAnno(2013);
		return bilancio;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.test.BaseJunit4TestCase#getEnteTest()
	 */
	protected Ente getEnteTest()
	{
		Ente ente = new Ente();
		ente.setUid(1);
		//ente.setNome("mio nome di prova");
		return ente;
	}


	
	
	
	
	
	
	
	
	
	
	
	
	
	

//	/**
//	 * Test calcolo fpv.
//	 */
//	@Test
//	public void testCalcoloFPV() {
//		CalcoloFondoPluriennaleVincolatoCronoprogramma req = new CalcoloFondoPluriennaleVincolatoCronoprogramma();
//		req.setDataOra(new Date());
//		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
//		
//		Cronoprogramma c = new Cronoprogramma();
//		c.setUid(24);
//		
//		req.setCronoprogramma(c);
//		
//		CalcoloFondoPluriennaleVincolatoCronoprogrammaResponse res =calcoloFondoPluriennaleVincolatoCronoprogrammaService.executeService(req);
//		
//		assertNotNull(res);
//		
//		printToSpreadSheet(res);
//	}
	
	
	
	
	
//	/**
//	 * Prints the to spread sheet.
//	 *
//	 * @param res the res
//	 */
//	private void printToSpreadSheet(CalcoloFondoPluriennaleVincolatoCronoprogrammaResponse res) {
//		try {
//			FileOutputStream fos = new FileOutputStream("docs/test.xlsx");
//			Workbook wb = new XSSFWorkbook();
//			
//			Sheet s1 = wb.createSheet("Entrata Old");
//			Sheet s1b = wb.createSheet("Entrata New");
//			Sheet s2 = wb.createSheet("Spesa");
//			Sheet s3 = wb.createSheet("Globale");
//			
//			CellStyle cs = wb.createCellStyle();
//			cs.setAlignment(CellStyle.ALIGN_RIGHT);
//			
//			printOnSpreadSheetEntrata(res.getListaFondoPluriennaleVincolatoEntrataCronoprogramma(), fos, s1, cs);			
//			printOnSpreadSheetEntrataB(res.raggruppaFondoPluriennaleVincolatoPerEntrate(), fos, s1b, cs);
////			printOnSpreadSheetUscita(res.getListaFondoPluriennaleVincolatoUscitaCronoprogramma(), fos, s2, cs);
//			printOnSpreadSheetGenerale(res.raggruppaFondoPluriennaleVincolatoPerAnno(), fos, s3, cs);
//			
//			wb.write(fos);
//			
//			fos.close();
//		} catch(Exception e) {
//			System.out.println(e.getMessage());
//		}
//	}

//	/**
//	 * Prints the on spread sheet entrata.
//	 *
//	 * @param list the list
//	 * @param fos the fos
//	 * @param s the s
//	 * @param cs the cs
//	 * @throws IOException Signals that an I/O exception has occurred.
//	 */
//	private void printOnSpreadSheetEntrata(List<FondoPluriennaleVincolatoEntrataCronoprogramma> list, FileOutputStream fos, Sheet s, CellStyle cs) throws IOException {
//		Row r = null;
//		Cell c0 = null;
//		Cell c1 = null;
//		Cell c2 = null;
//		Cell c3 = null;
//		
//		//wb.setSheetName(0, "Entrata");
//		
//		int i = 0;
//		
//		r = s.createRow(i++);
//		c0 = r.createCell(0);
//		c1 = r.createCell(1);
//		c2 = r.createCell(2);
//		c3 = r.createCell(3);
//		
//		c0.setCellValue("ANNO");
//		c1.setCellValue("TITOLO");
//		c2.setCellValue("IMPORTO");
//		c3.setCellValue("IMPORTO FPV");
//		
//		for (FondoPluriennaleVincolatoEntrataCronoprogramma fpve : list) {
//			r = s.createRow(i++);
//			c0 = r.createCell(0);
//			c1 = r.createCell(1);
//			c2 = r.createCell(2);
//			c3 = r.createCell(3);
//			
//			c0.setCellValue(fpve.getAnno());
//			c1.setCellValue(fpve.getTitoloSpesa().getCodice());
//			c2.setCellValue(df.format(fpve.getImporto()));
//			c3.setCellValue(df.format(fpve.getImportoFPV()));
//			
//			c2.setCellStyle(cs);
//			c3.setCellStyle(cs);
//		}
//		
//		s.autoSizeColumn(0);
//		s.autoSizeColumn(1);
//		s.autoSizeColumn(2);
//		s.autoSizeColumn(3);
//	}
	
	
//	/**
//	 * Prints the on spread sheet entrata b.
//	 *
//	 * @param list the list
//	 * @param fos the fos
//	 * @param s the s
//	 * @param cs the cs
//	 * @throws IOException Signals that an I/O exception has occurred.
//	 */
//	private void printOnSpreadSheetEntrataB(List<RiepilogoFondoPluriennaleVincolatoEntrataCronoprogramma> list, FileOutputStream fos, Sheet s, CellStyle cs) throws IOException {
//		//wb.setSheetName(0, "Entrata");
//		
//		int i = 0;
//		
//		Row r = s.createRow(i++);
//		Cell c0 = r.createCell(0);
//		Cell  c1 = r.createCell(1);
//		Cell  c2 = r.createCell(2);
//		Cell  c3 = r.createCell(3);
//		Cell  c4 = r.createCell(4);
//		Cell  c5 = r.createCell(5);
//		
//		c0.setCellValue("Anno");
//		c1.setCellValue("Entrata prevista");
//		c2.setCellValue("FPV Entrata per spesa corrente");
//		c3.setCellValue("FPV entrata per spesa c/capitale");
//		c4.setCellValue("Totale");
//		c5.setCellValue("FPV Entrata complessivo");
//		
//		for (RiepilogoFondoPluriennaleVincolatoEntrataCronoprogramma fpve : list) {
//			r = s.createRow(i++);
//			c0 = r.createCell(0);
//			c1 = r.createCell(1);
//			c2 = r.createCell(2);
//			c3 = r.createCell(3);
//			c4 = r.createCell(4);
//			c5 = r.createCell(5);
//			
//			c0.setCellValue(fpve.getAnno());
//			c1.setCellValue(df.format(fpve.getImportoEntrata()));
//			c2.setCellValue(df.format(fpve.getImportoTitolo1()));
//			c3.setCellValue(df.format(fpve.getImportoTitolo2()));
//			c4.setCellValue(df.format(fpve.getImportoTotaleEntrata()));
//			c5.setCellValue(df.format(fpve.getImportoTotaleEntrataFPV()));
//			
//			c1.setCellStyle(cs);
//			c2.setCellStyle(cs);
//			c3.setCellStyle(cs);
//			c4.setCellStyle(cs);
//			c5.setCellStyle(cs);
//		}
//		
//		s.autoSizeColumn(0);
//		s.autoSizeColumn(1);
//		s.autoSizeColumn(2);
//		s.autoSizeColumn(3);
//		s.autoSizeColumn(4);
//		s.autoSizeColumn(5);
//	}
	
//	/**
//	 * Prints the on spread sheet uscita.
//	 *
//	 * @param list the list
//	 * @param fos the fos
//	 * @param s the s
//	 * @param cs the cs
//	 * @throws IOException Signals that an I/O exception has occurred.
//	 */
//	private void printOnSpreadSheetUscita(List<FondoPluriennaleVincolatoUscitaCronoprogramma> list, FileOutputStream fos, Sheet s, CellStyle cs) throws IOException {
//		Row r = null;
//		Cell c0 = null;
//		Cell c1 = null;
//		Cell c2 = null;
//		Cell c3 = null;
//		Cell c4 = null;
//		Cell c5 = null;
//		
//		//wb.setSheetName(1, "Uscita");
//		
//		int i = 0;
//		
//		r = s.createRow(i++);
//		c0 = r.createCell(0);
//		c1 = r.createCell(1);
//		c2 = r.createCell(2);
//		c3 = r.createCell(3);
//		c4 = r.createCell(4);
//		c5 = r.createCell(5);
//		
//		c0.setCellValue("ANNO");
//		c1.setCellValue("MISSIONE");
//		c2.setCellValue("PROGRAMMA");
//		c3.setCellValue("TITOLO");
//		c4.setCellValue("IMPORTO");
//		c5.setCellValue("IMPORTO FPV");
//		
//		for (FondoPluriennaleVincolatoUscitaCronoprogramma fpvu : list) {
//			r = s.createRow(i++);
//			c0 = r.createCell(0);
//			c1 = r.createCell(1);
//			c2 = r.createCell(2);
//			c3 = r.createCell(3);
//			c4 = r.createCell(4);
//			c5 = r.createCell(5);
//			
//			c0.setCellValue(fpvu.getAnno());
//			c1.setCellValue(fpvu.getMissione().getCodice());
//			c2.setCellValue(fpvu.getProgramma().getCodice());
//			c3.setCellValue(fpvu.getTitoloSpesa().getCodice());
//			c4.setCellValue(df.format(fpvu.getImporto()));
//			c5.setCellValue(df.format(fpvu.getImportoFPV()));
//			
//			c4.setCellStyle(cs);
//			c5.setCellStyle(cs);
//		}
//		
//		s.autoSizeColumn(0);
//		s.autoSizeColumn(1);
//		s.autoSizeColumn(2);
//		s.autoSizeColumn(3);
//		s.autoSizeColumn(4);
//		s.autoSizeColumn(5);
//	}
	
//	/**
//	 * Prints the on spread sheet generale.
//	 *
//	 * @param map the map
//	 * @param fos the fos
//	 * @param s the s
//	 * @param cs the cs
//	 * @throws IOException Signals that an I/O exception has occurred.
//	 */
//	private void printOnSpreadSheetGenerale(Map<Integer, FondoPluriennaleVincolatoCronoprogramma[]> map, FileOutputStream fos, Sheet s, CellStyle cs) throws IOException {
//		Row r = null;
//		Cell c0 = null;
//		Cell c1 = null;
//		Cell c2 = null;
//		Cell c3 = null;
//		Cell c4 = null;
//		
//		//wb.setSheetName(2, "Generale");
//		
//		int i = 0;
//		
//		r = s.createRow(i++);
//		c0 = r.createCell(0);
//		c1 = r.createCell(1);
//		c2 = r.createCell(2);
//		c3 = r.createCell(3);
//		c4 = r.createCell(4);
//		
//		c0.setCellValue("ANNO");
//		c1.setCellValue("ENTRATA PREVISTA");
//		c2.setCellValue("FPV ENTRATA");
//		c3.setCellValue("SPESA PREVISTA");
//		c4.setCellValue("FPV SPESA");
//		
//		for (Entry<Integer, FondoPluriennaleVincolatoCronoprogramma[]> entry : map.entrySet()) {
//			r = s.createRow(i++);
//			c0 = r.createCell(0);
//			c1 = r.createCell(1);
//			c2 = r.createCell(2);
//			c3 = r.createCell(3);
//			c4 = r.createCell(4);
//			
//			c0.setCellValue(entry.getKey());
//			c1.setCellValue(df.format(entry.getValue()[0].getImporto()));
//			c2.setCellValue(df.format(entry.getValue()[0].getImportoFPV()));
//			c3.setCellValue(df.format(entry.getValue()[1].getImporto()));
//			c4.setCellValue(df.format(entry.getValue()[1].getImportoFPV()));
//			
//			c1.setCellStyle(cs);
//			c2.setCellStyle(cs);
//			c3.setCellStyle(cs);
//			c4.setCellStyle(cs);
//		}
//		
//		s.autoSizeColumn(0);
//		s.autoSizeColumn(1);
//		s.autoSizeColumn(2);
//		s.autoSizeColumn(3);
//		s.autoSizeColumn(4);
//	}

	

}
