/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.capitolo;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.business.service.capitolo.AggiornaStanziamentiCapitoliVariatiService;
import it.csi.siac.siacbilser.business.service.capitolo.CalcolaTotaliStanziamentiDiPrevisioneService;
import it.csi.siac.siacbilser.business.service.capitolo.CalcoloDisponibilitaDiUnCapitoloService;
import it.csi.siac.siacbilser.business.service.capitolo.ControllaDisponibilitaCassaCapitoloByMovimentoService;
import it.csi.siac.siacbilser.business.service.capitolo.RicercaSinteticaVariazioniSingoloCapitoloService;
import it.csi.siac.siacbilser.business.service.capitolo.RicercaVariazioniCapitoloService;
import it.csi.siac.siacbilser.business.service.capitoloentratagestione.RicercaDisponibilitaCapitoloEntrataGestioneService;
import it.csi.siac.siacbilser.business.service.capitoloentratagestione.RicercaSinteticaCapitoloEntrataGestioneService;
import it.csi.siac.siacbilser.business.service.capitolouscitagestione.AggiornaCapitoloDiUscitaGestioneService;
import it.csi.siac.siacbilser.business.service.capitolouscitagestione.RicercaDettaglioModulareCapitoloUscitaGestioneService;
import it.csi.siac.siacbilser.business.service.capitolouscitagestione.RicercaDisponibilitaCapitoloUscitaGestioneService;
import it.csi.siac.siacbilser.business.service.capitolouscitagestione.RicercaVariazioniCapitoloPerAggiornamentoCapitoloService;
import it.csi.siac.siacbilser.business.service.capitolouscitaprevisione.AnnullaCapitoloUscitaPrevisioneService;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaCapitoloDiUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaCapitoloDiUscitaGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaStanziamentiCapitoliVariati;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaStanziamentiCapitoliVariatiResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.AnnullaCapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.AnnullaCapitoloUscitaPrevisioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.CalcolaTotaliStanziamentiDiPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.CalcolaTotaliStanziamentiDiPrevisioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.CalcoloDisponibilitaDiUnCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.CalcoloDisponibilitaDiUnCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaDisponibilitaCassaCapitoloByMovimento;
import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaDisponibilitaCassaCapitoloByMovimentoResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioModulareCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioModulareCapitoloUscitaGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDisponibilitaCapitoloEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDisponibilitaCapitoloEntrataGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDisponibilitaCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDisponibilitaCapitoloUscitaGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloEntrataGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaVariazioniSingoloCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaVariazioniSingoloCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaVariazioniCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaVariazioniCapitoloPerAggiornamentoCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaVariazioniCapitoloPerAggiornamentoCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaVariazioniCapitoloResponse;
import it.csi.siac.siacbilser.integration.dad.CapitoloDad;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.CapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.model.CategoriaCapitolo;
import it.csi.siac.siacbilser.model.ElementoPianoDeiConti;
import it.csi.siac.siacbilser.model.ImportiCapitolo;
import it.csi.siac.siacbilser.model.ImportiCapitoloEnum;
import it.csi.siac.siacbilser.model.ImportiCapitoloUG;
import it.csi.siac.siacbilser.model.Macroaggregato;
import it.csi.siac.siacbilser.model.Missione;
import it.csi.siac.siacbilser.model.Programma;
import it.csi.siac.siacbilser.model.StatoOperativoAttualeVariazioneDiBilancio;
import it.csi.siac.siacbilser.model.StatoOperativoElementoDiBilancio;
import it.csi.siac.siacbilser.model.TipoFinanziamento;
import it.csi.siac.siacbilser.model.TitoloSpesa;
import it.csi.siac.siacbilser.model.VariazioneImportoCapitolo;
import it.csi.siac.siacbilser.model.ric.RicercaSinteticaCapitoloEGest;
import it.csi.siac.siacbilser.model.ric.SegnoImporti;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;
import it.csi.siac.siaccommon.util.JAXBUtility;
import it.csi.siac.siaccorser.model.Account;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.ClassificatoreGenerico;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Entita;
import it.csi.siac.siaccorser.model.FaseEStatoAttualeBilancio;
import it.csi.siac.siaccorser.model.Operatore;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;
import it.csi.siac.siacfin2ser.model.CapitoloUscitaGestioneModelDetail;

// TODO: Auto-generated Javadoc
/**
 * The Class CapitoloTest.
 */
public class CapitoloTest extends BaseJunit4TestCase {

	/** The aggiorna stanziamenti capitoli variati service. */
	@Autowired
	private AggiornaStanziamentiCapitoliVariatiService aggiornaStanziamentiCapitoliVariatiService;
	
	/** The calcola totali stanziamenti di previsione service. */
	@Autowired
	private CalcolaTotaliStanziamentiDiPrevisioneService calcolaTotaliStanziamentiDiPrevisioneService;
	
	/** The calcolo disponibilita di un capitolo service. */
	@Autowired
	private CalcoloDisponibilitaDiUnCapitoloService calcoloDisponibilitaDiUnCapitoloService;
	
	@Autowired
	private RicercaSinteticaCapitoloEntrataGestioneService ricercaSinteticaCapitoloEntrataGestioneService;
	
	@Autowired
	private RicercaVariazioniCapitoloService ricercaVariazioniCapitoloService;
	@Autowired
	private RicercaSinteticaVariazioniSingoloCapitoloService ricercaSinteticaVariazioniSingoloCapitoloService;
	@Autowired
	private RicercaDettaglioModulareCapitoloUscitaGestioneService ricercaDettaglioModulareCapitoloUscitaGestioneService;
	@Autowired
	private RicercaDisponibilitaCapitoloUscitaGestioneService ricercaDisponibilitaCapitoloUscitaGestioneService;
	@Autowired
	private RicercaDisponibilitaCapitoloEntrataGestioneService ricercaDisponibilitaCapitoloEntrataGestioneService;
	@Autowired
	private AnnullaCapitoloUscitaPrevisioneService annullaCapitoloUscitaPrevisioneService;
	@Autowired
	private ControllaDisponibilitaCassaCapitoloByMovimentoService controllaDisponibilitaCassaCapitoloByMovimentoService;
	
	@Autowired
	private CapitoloDad capitoloDad;
	
	
	
	/**
	 * Test aggiorna stanziamenti capitoli variati.
	 *
	 * @throws Throwable the throwable
	 */
	@Test
	public void testAggiornaStanziamentiCapitoliVariati()
			throws Throwable {
		try
		{
			AggiornaStanziamentiCapitoliVariati req = new AggiornaStanziamentiCapitoliVariati();
			
			//INFO parametri provvisori
			req.setVariazioneImportoCapitolo(new VariazioneImportoCapitolo());
			req.setStatoVariazionePrecedente(new StatoOperativoAttualeVariazioneDiBilancio());
			req.setStatoVariazioneSuccessivo(new StatoOperativoAttualeVariazioneDiBilancio());
			
			req.setRichiedente(createRichiedente());
			
			AggiornaStanziamentiCapitoliVariatiResponse res = aggiornaStanziamentiCapitoliVariatiService.executeService(req);
			
			Assert.assertNotNull("AggiornaStanziamentiCapitoliVariati", res);
		}
		catch (Throwable e)
		{
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * Test calcola totali stanziamenti di previsione.
	 */
	@Test
	public void calcolaTotaliStanziamentiDiPrevisione() {
		CalcolaTotaliStanziamentiDiPrevisione req = new CalcolaTotaliStanziamentiDiPrevisione();
		req.setAnnoBilancio(Integer.valueOf(2020));
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		
		req.setCalcolaComponenti(true);
		req.setAnnoEsercizio(2020);
		req.setBilancio(getBilancioByProperties("consip", "regp", 2020));
		
		CalcolaTotaliStanziamentiDiPrevisioneResponse res = calcolaTotaliStanziamentiDiPrevisioneService.executeService(req);
		assertNotNull(res);
	}
	
	/**
	 * Test calcolo disponibilita di un capitolo.
	 *
	 * @throws Throwable the throwable
	 */
	@Test
	public void testCalcoloDisponibilitaDiUnCapitolo()
			throws Throwable {
		try
		{
			CalcoloDisponibilitaDiUnCapitolo req = new CalcoloDisponibilitaDiUnCapitolo();
			
			//INFO parametri provvisori
			req.setEnte(new Ente());
			req.setBilancio(new Bilancio());
			req.setFase(new FaseEStatoAttualeBilancio());
			req.setAnnoCapitolo(1);
			req.setNumroCapitolo(1);
			req.setTipoDisponibilitaRichiesta("String tipoDisponibilitaRichiesta");
			
			req.setRichiedente(createRichiedente());
			
			CalcoloDisponibilitaDiUnCapitoloResponse res = calcoloDisponibilitaDiUnCapitoloService.executeService(req);
			
			Assert.assertNotNull("CalcoloDisponibilitaDiUnCapitolo", res);
		}
		catch (Throwable e)
		{
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * Creates the richiedente.
	 *
	 * @return the richiedente
	 */
	protected Richiedente createRichiedente() 
	{
		Ente ente = new Ente();
		ente.setUid(1);
				
		Operatore operatore = new Operatore();
		operatore.setCodiceFiscale("PROLOGIC");
				
		Account account = new Account();
		account.setEnte(ente);
		account.setOperatore(operatore);
				
		Richiedente richiedente = new Richiedente();
		richiedente.setOperatore(operatore);
		richiedente.setAccount(account);
		return richiedente;
	}
	
	/**
	 * Effettua un test per il servizio di RicercaSinteticaCapitoloEntrataGestioneService.
	 */
	@Test
	public void ricercaSinteticaEG(){
		RicercaSinteticaCapitoloEntrataGestione req = new RicercaSinteticaCapitoloEntrataGestione();
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setEnte(req.getRichiedente().getAccount().getEnte());
		req.setParametriPaginazione(getParametriPaginazioneTest());
		
		RicercaSinteticaCapitoloEGest ricercaSinteticaCapitoloEntrata = new RicercaSinteticaCapitoloEGest();
		ricercaSinteticaCapitoloEntrata.setAnnoEsercizio(2016);
		ricercaSinteticaCapitoloEntrata.setAnnoCapitolo(2016);
		ricercaSinteticaCapitoloEntrata.setNumeroCapitolo(5937);
		ricercaSinteticaCapitoloEntrata.setNumeroArticolo(1);
		ricercaSinteticaCapitoloEntrata.setNumeroUEB(1);
		ricercaSinteticaCapitoloEntrata.setStatoOperativo(StatoOperativoElementoDiBilancio.VALIDO);
		req.setRicercaSinteticaCapitoloEntrata(ricercaSinteticaCapitoloEntrata );
		
		RicercaSinteticaCapitoloEntrataGestioneResponse res = ricercaSinteticaCapitoloEntrataGestioneService.executeService(req);
		assertNotNull(res);
	}

	/**
	 * Effettua un test per il servizio di RicercaVariazioniCapitolo
	 */
	@Test
	public void ricercaVariazioniCapitolo() {
		RicercaVariazioniCapitolo req = new RicercaVariazioniCapitolo();
		
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("dev", "ente1"));
		
		Bilancio bilancio = new Bilancio();
		bilancio.setUid(16);
		req.setBilancio(bilancio);
		
		Capitolo<?, ?> capitolo = new CapitoloUscitaGestione();
		capitolo.setUid(31201);
		req.setCapitolo(capitolo);
		
		RicercaVariazioniCapitoloResponse res = ricercaVariazioniCapitoloService.executeService(req);
		assertNotNull(res);
	}
	
	/**
	 * Effettua un test per il servizio di RicercaVariazioniCapitolo
	 */
	@Test
	public void ricercaSinteticaVariazioniSingoloCapitolo() {
		RicercaSinteticaVariazioniSingoloCapitolo req = new RicercaSinteticaVariazioniSingoloCapitolo();
		
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setBilancio(getBilancioByProperties("consip", "regp", 2019));
		req.setCapitolo(create(CapitoloUscitaGestione.class, 108389));
		
		req.setEnte(req.getRichiedente().getAccount().getEnte());
		req.setSegnoImportiVariazione(SegnoImporti.POSITIVO);
		//req.setSegnoImportiVariazione(SegnoImporti.NEGATIVO);
		
		req.setParametriPaginazione(getParametriPaginazioneTest());
		
		RicercaSinteticaVariazioniSingoloCapitoloResponse res = ricercaSinteticaVariazioniSingoloCapitoloService.executeService(req);
		assertNotNull(res);
	}
	
	@Autowired
	private RicercaVariazioniCapitoloPerAggiornamentoCapitoloService ricercaVariazioniCapitoloPerAggiornamentoCapitolo;
	
	@Test
	public void ricercaVariazioniCapitoloPerAggiornamentoCapitolo() {
		RicercaVariazioniCapitoloPerAggiornamentoCapitolo req = new RicercaVariazioniCapitoloPerAggiornamentoCapitolo();
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("dev", "ente1"));
		req.setUidCapitolo(31012);
		
		RicercaVariazioniCapitoloPerAggiornamentoCapitoloResponse res = ricercaVariazioniCapitoloPerAggiornamentoCapitolo.executeService(req);
		assertNotNull(res);
	}
	
	
	@Autowired
	private AggiornaCapitoloDiUscitaGestioneService aggiornaCapitoloDiUscitaGestioneService;
	
	@Test
	public void aggiornaCapitoloDiUscitaGestione() {
		AggiornaCapitoloDiUscitaGestione req = new AggiornaCapitoloDiUscitaGestione();
		
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("dev", "ente1"));
		
		CapitoloUscitaGestione cug = new CapitoloUscitaGestione();
		
		cug.setUid(12509);
		cug.setAnnoCapitolo(2015);
		cug.setDescrizione("MAGAZZIN0 SERVIZI ECONOMALI - SPESE GENERALI DI FUNZIONAMENTO");
		cug.setExAnnoCapitolo(2014);
		cug.setExCapitolo(100);
		cug.setExArticolo(1);
		cug.setExUEB(1);
		cug.setFlagImpegnabile(Boolean.FALSE);
		cug.setNumeroCapitolo(10000);
		cug.setNumeroArticolo(0);
		cug.setNumeroUEB(100642004);
		cug.setStatoOperativoElementoDiBilancio(StatoOperativoElementoDiBilancio.VALIDO);
		
		cug.setBilancio(getBilancio2015Test());
		cug.setEnte(req.getRichiedente().getAccount().getEnte());
		
		cug.setCategoriaCapitolo(createEntita(CategoriaCapitolo.class, 2));
		cug.getClassificatoriGenerici().add(createEntita(ClassificatoreGenerico.class, 453446));
		cug.getClassificatoriGenerici().add(createEntita(ClassificatoreGenerico.class, 29));
		cug.getClassificatoriGenerici().add(createEntita(ClassificatoreGenerico.class, 453447));
		cug.getClassificatoriGenerici().add(createEntita(ClassificatoreGenerico.class, 453448));
		cug.getClassificatoriGenerici().add(createEntita(ClassificatoreGenerico.class, 453573));
		cug.getClassificatoriGenerici().add(createEntita(ClassificatoreGenerico.class, 453791));
		cug.getClassificatoriGenerici().add(createEntita(ClassificatoreGenerico.class, 453825));
		cug.getClassificatoriGenerici().add(null);
		cug.getClassificatoriGenerici().add(null);
		cug.getClassificatoriGenerici().add(null);
		cug.getClassificatoriGenerici().add(null);
		cug.getClassificatoriGenerici().add(null);
		cug.getClassificatoriGenerici().add(null);
		cug.getClassificatoriGenerici().add(null);
		cug.getClassificatoriGenerici().add(null);
		cug.setElementoPianoDeiConti(createEntita(ElementoPianoDeiConti.class, 125692));
		cug.setStrutturaAmministrativoContabile(createEntita(StrutturaAmministrativoContabile.class, 404));
		cug.setTipoFinanziamento(createEntita(TipoFinanziamento.class, 453445));
		cug.setMacroaggregato(createEntita(Macroaggregato.class, 118919));
		cug.setMissione(createEntita(Missione.class, 118306));
		cug.setProgramma(createEntita(Programma.class, 118309));
		cug.setTitoloSpesa(createEntita(TitoloSpesa.class, 118916));
		
		cug.setImportiCapitoloUG(createImportiCapitolo(ImportiCapitoloUG.class, 2015, new BigDecimal(6499), new BigDecimal("7089.88"), new BigDecimal("13588.88")));
		
		cug.getListaImportiCapitoloUG().add(createImportiCapitolo(ImportiCapitoloUG.class, 2015, new BigDecimal(6499), new BigDecimal("7089.88"), new BigDecimal("13588.88")));
		cug.getListaImportiCapitoloUG().add(createImportiCapitolo(ImportiCapitoloUG.class, 2016, new BigDecimal(6499), BigDecimal.ZERO, BigDecimal.ZERO));
		cug.getListaImportiCapitoloUG().add(createImportiCapitolo(ImportiCapitoloUG.class, 2017, new BigDecimal(6499), BigDecimal.ZERO, BigDecimal.ZERO));
		
		cug.getListaImportiCapitolo().add(createImportiCapitolo(ImportiCapitoloUG.class, 2015, new BigDecimal(6499), new BigDecimal("7089.88"), new BigDecimal("13588.88")));
		cug.getListaImportiCapitolo().add(createImportiCapitolo(ImportiCapitoloUG.class, 2016, new BigDecimal(6499), BigDecimal.ZERO, BigDecimal.ZERO));
		cug.getListaImportiCapitolo().add(createImportiCapitolo(ImportiCapitoloUG.class, 2017, new BigDecimal(6499), BigDecimal.ZERO, BigDecimal.ZERO));
		
		req.setCapitoloUscitaGestione(cug);
		AggiornaCapitoloDiUscitaGestioneResponse res = aggiornaCapitoloDiUscitaGestioneService.executeService(req);
		assertNotNull(res);
	}
	
	@Test
	public void aggiornaCapitoloDiUscitaGestione2() {
		RicercaDettaglioModulareCapitoloUscitaGestione req1 = new RicercaDettaglioModulareCapitoloUscitaGestione();
		req1.setDataOra(new Date());
		req1.setRichiedente(getRichiedenteByProperties("dev", "ente1"));
		req1.setImportiDerivatiRichiesti(EnumSet.allOf(ImportiCapitoloEnum.class));
		req1.setModelDetails(CapitoloUscitaGestioneModelDetail.values());
		req1.setTipologieClassificatoriRichiesti(TipologiaClassificatore.values());
		
		CapitoloUscitaGestione capitoloUscitaGestione = new CapitoloUscitaGestione();
		capitoloUscitaGestione.setUid(12349);
		req1.setCapitoloUscitaGestione(capitoloUscitaGestione);
		
		RicercaDettaglioModulareCapitoloUscitaGestioneResponse res1 = ricercaDettaglioModulareCapitoloUscitaGestioneService.executeService(req1);
		
		capitoloUscitaGestione = res1.getCapitoloUscitaGestione();
		
		capitoloUscitaGestione.setExAnnoCapitolo(2014);
		capitoloUscitaGestione.setExCapitolo(100);
		capitoloUscitaGestione.setExArticolo(1);
		capitoloUscitaGestione.setExUEB(1);
		
		AggiornaCapitoloDiUscitaGestione req = new AggiornaCapitoloDiUscitaGestione();
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("dev", "ente1"));
		req.setCapitoloUscitaGestione(capitoloUscitaGestione);
		
		AggiornaCapitoloDiUscitaGestioneResponse res = aggiornaCapitoloDiUscitaGestioneService.executeService(req);
		assertNotNull(res);
	}
	
	private <T extends Entita> T createEntita(Class<T> clazz, int uid) {
		T instance;
		try {
			instance = clazz.newInstance();
		} catch (Exception e) {
			throw new IllegalArgumentException("Impossibile instanziare l'entita' richiesta (clazz: " + clazz.getSimpleName() + ", uid: " + uid + ")", e);
		}
		instance.setUid(uid);
		return instance;
	}
	
	private <I extends ImportiCapitolo> I createImportiCapitolo(Class<I> clazz, Integer annoCompetenza, BigDecimal stanziamento, BigDecimal residuo, BigDecimal cassa) {
		I instance;
		try {
			instance = clazz.newInstance();
		} catch (Exception e) {
			throw new IllegalArgumentException("Impossibile instanziare l'importo richiesto (clazz: " + clazz.getSimpleName() + ", annoCompetenza: " + annoCompetenza + ", stanziamento: " + stanziamento + ", residuo: " + residuo + ", cassa: " + cassa + ")", e);
		}
		instance.setAnnoCompetenza(annoCompetenza);
		instance.setStanziamento(stanziamento);
		instance.setStanziamentoResiduo(residuo);
		instance.setStanziamentoCassa(cassa);
		return instance;
	}
	
	@Test
	public void ricercaDettaglioModulareCapitoloUscitaGestione() {
		RicercaDettaglioModulareCapitoloUscitaGestione req = new RicercaDettaglioModulareCapitoloUscitaGestione();
		
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("dev", "ente1"));
		
		CapitoloUscitaGestione capitoloUscitaGestione = new CapitoloUscitaGestione();
		capitoloUscitaGestione.setUid(12349);
		req.setCapitoloUscitaGestione(capitoloUscitaGestione);
		
		req.setImportiDerivatiRichiesti(EnumSet.allOf(ImportiCapitoloEnum.class));
		req.setModelDetails(CapitoloUscitaGestioneModelDetail.Importi);
		
		RicercaDettaglioModulareCapitoloUscitaGestioneResponse res = ricercaDettaglioModulareCapitoloUscitaGestioneService.executeService(req);
		assertNotNull(res);
	}
	
	@Test
	public void ricercaDisponibilitaCapitoloUscitaGestione() {
		final String methodName = "ricercaDisponibilitaCapitoloUscitaGestione";
		int[] anni = new int[] {2017};
		Map<Integer, RicercaDisponibilitaCapitoloUscitaGestioneResponse> responses = new HashMap<Integer, RicercaDisponibilitaCapitoloUscitaGestioneResponse>();
		
		RicercaDisponibilitaCapitoloUscitaGestione req = new RicercaDisponibilitaCapitoloUscitaGestione();
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("forn2", "regp"));
		
		CapitoloUscitaGestione capitoloUscitaGestione = new CapitoloUscitaGestione();
		capitoloUscitaGestione.setUid(58866);
		req.setCapitoloUscitaGestione(capitoloUscitaGestione);
		
		for(int anno : anni) {
			req.setAnnoBilancio(anno);
			
			RicercaDisponibilitaCapitoloUscitaGestioneResponse res = ricercaDisponibilitaCapitoloUscitaGestioneService.executeService(req);
			assertNotNull(res);
			responses.put(anno, res);
		}
		for(Entry<Integer, RicercaDisponibilitaCapitoloUscitaGestioneResponse> entry : responses.entrySet()) {
			log.info(methodName, "Anno " + entry.getKey() + " - VALUE: " + JAXBUtility.marshall(entry.getValue()));
		}
	}
	
	@Test
	public void ricercaDisponibilitaCapitoloEntrataGestione() {
		final String methodName = "ricercaDisponibilitaCapitoloEntrataGestione";
		//int[] anni = new int[] {2013, 2014, 2015, 2016};
		int[] anni = new int[] {2015};
		Map<Integer, RicercaDisponibilitaCapitoloEntrataGestioneResponse> responses = new HashMap<Integer, RicercaDisponibilitaCapitoloEntrataGestioneResponse>();
		
		RicercaDisponibilitaCapitoloEntrataGestione req = new RicercaDisponibilitaCapitoloEntrataGestione();
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("dev", "ente1"));
		
		CapitoloEntrataGestione capitoloEntrataGestione = new CapitoloEntrataGestione();
		capitoloEntrataGestione.setUid(18085);
		req.setCapitoloEntrataGestione(capitoloEntrataGestione);
		
		for(int anno : anni) {
			req.setAnnoBilancio(anno);
			
			RicercaDisponibilitaCapitoloEntrataGestioneResponse res = ricercaDisponibilitaCapitoloEntrataGestioneService.executeService(req);
			assertNotNull(res);
			responses.put(anno, res);
		}
		for(Entry<Integer, RicercaDisponibilitaCapitoloEntrataGestioneResponse> entry : responses.entrySet()) {
			log.info(methodName, "Anno " + entry.getKey() + " - VALUE: " + JAXBUtility.marshall(entry.getValue()));
		}
	}
	
	@Test
	public void annullaCapitoloUscitaPrevisione() {
		AnnullaCapitoloUscitaPrevisione req = new AnnullaCapitoloUscitaPrevisione();
		
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("dev", "ente1"));
		req.setEnte(req.getRichiedente().getAccount().getEnte());
		req.setBilancio(getBilancioTest(16, 2015));
		
		CapitoloUscitaPrevisione cap = create(CapitoloUscitaPrevisione.class, 31275);
		cap.setAnnoCapitolo(req.getBilancio().getAnno());
		cap.setNumeroCapitolo(4958);
		cap.setNumeroArticolo(1);
		cap.setNumeroUEB(1);
		cap.setStatoOperativoElementoDiBilancio(StatoOperativoElementoDiBilancio.VALIDO);
		req.setCapitoloUscitaPrev(cap);
		
		AnnullaCapitoloUscitaPrevisioneResponse res = annullaCapitoloUscitaPrevisioneService.executeService(req);
		assertNotNull(res);
	}
	
	@Test
	public void testQueryDisponibileCapitolo() {
		Integer[] uidSubdoc = new Integer[] {
				4537
				,1564
				,2120
				,17
				,8919
				,7895
				,53480
				,53806
				,55076
				,55073
		};
		//capitoloDad.findCapitoliBySubdoc(Arrays.asList(uidSubdoc));
		capitoloDad.findCapitoliByElenco(Arrays.asList(2));
	}
	
	@Test
	public void testCapitoloFondino() {
		//capitoloDad.findCapitoliBySubdoc(Arrays.asList(uidSubdoc));
		boolean capitoloFondino = capitoloDad.isCapitoloFondino(202174);
		System.out.println(capitoloFondino);
	}
	
	@Test
	public void testControllaDisponibilitaCassaCapitoloByMovimentoService() {
		Integer[] uidSubdocs = new Integer[] {
				
				68082
				,68944
				,70716
				,58919
				,70626
				,58913
				,58916
				,60596
				,59593
				,64778
				,61227
				
				/*STESSO CAPITOLO
				 64635
				,64634
				,62123
				,61418
				,62531
				,64589
				,61411
				,60235
				,64640
				,64638*/
		};
		Integer[] uidsElenco = new Integer[] {9420};
		ControllaDisponibilitaCassaCapitoloByMovimento req = new ControllaDisponibilitaCassaCapitoloByMovimento();
		req.setRichiedente(getRichiedenteByProperties("forn2", "crp"));
		req.setBilancio(getBilancioTest(143, 2017));
		//req.setIdsSubdocumentiSpesa(Arrays.asList(uidSubdocs));
		req.setIdsElenchi(Arrays.asList(uidsElenco));
		ControllaDisponibilitaCassaCapitoloByMovimentoResponse executeService = controllaDisponibilitaCassaCapitoloByMovimentoService.executeService(req);
		/*select siactsubdo8_.subdoc_id, 
		 siacrmovge0_.elem_id as col_0_0_,
		 cast(siactperio4_.anno as int4) as col_1_0_,
		 cast(siactbilel2_.elem_code as int4) as col_2_0_,
		 cast(siactbilel2_.elem_code2 as int4) as col_3_0_,
		 cast(siactbilel2_.elem_code3 as int4) as col_4_0_,
		 siacrsubdo1_.subdoc_id as col_5_0_,
		 siactdoc9_.doc_anno as col_6_0_,
		 siactdoc9_.doc_numero as col_7_0_,
		 siactsubdo8_.subdoc_numero as col_8_0_,
		 siactsubdo8_.subdoc_importo as col_9_0_ from siac_r_movgest_bil_elem siacrmovge0_,
		 siac_t_bil_elem siactbilel2_,
		 siac_t_bil siactbil3_,
		 siac_t_periodo siactperio4_ 
		cross join siac_r_subdoc_movgest_ts siacrsubdo1_,
		 siac_t_subdoc siactsubdo8_,
		 siac_t_doc siactdoc9_ 
		cross join siac_t_movgest_ts siactmovge14_ 
		where siacrmovge0_.elem_id=siactbilel2_.elem_id 
		and siactbilel2_.bil_id=siactbil3_.bil_id 
		and siactbil3_.periodo_id=siactperio4_.periodo_id 
		and siacrsubdo1_.subdoc_id=siactsubdo8_.subdoc_id 
		and siactsubdo8_.doc_id=siactdoc9_.doc_id 
		and siacrsubdo1_.movgest_ts_id=siactmovge14_.movgest_ts_id 
		and (siacrmovge0_.data_cancellazione is null) 
		and siactmovge14_.movgest_id=siacrmovge0_.movgest_id 
		and (siacrsubdo1_.data_cancellazione is null) 
		--and (siacrsubdo1_.subdoc_id in (72025))
		order by siacrmovge0_.elem_id
		limit 10
			 * */
	}
}
