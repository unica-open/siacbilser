/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.predocumento;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacbilser.business.service.predocumentospesa.AggiornaStatoPreDocumentoDiSpesaService;
import it.csi.siac.siacbilser.business.service.predocumentospesa.AssociaImputazioniContabiliPreDocumentoSpesaService;
import it.csi.siac.siacbilser.business.service.predocumentospesa.AssociaImputazioniContabiliVariatePreDocumentoSpesaService;
import it.csi.siac.siacbilser.business.service.predocumentospesa.DefiniscePreDocumentoDiSpesaService;
import it.csi.siac.siacbilser.business.service.predocumentospesa.DefiniscePreDocumentoSpesaAsyncService;
import it.csi.siac.siacbilser.business.service.predocumentospesa.DefiniscePreDocumentoSpesaPerElencoService;
import it.csi.siac.siacbilser.business.service.predocumentospesa.DefiniscePreDocumentoSpesaService;
import it.csi.siac.siacbilser.business.service.predocumentospesa.InseriscePreDocumentoSpesaService;
import it.csi.siac.siacbilser.business.service.predocumentospesa.RicercaDettaglioPreDocumentoSpesaService;
import it.csi.siac.siacbilser.business.service.predocumentospesa.RicercaSinteticaPreDocumentoSpesaService;
import it.csi.siac.siacbilser.business.service.predocumentospesa.ValidaStatoOperativoPreDocumentoSpesaService;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;
import it.csi.siac.siaccommon.util.JAXBUtility;
import it.csi.siac.siaccorser.frontend.webservice.OperazioneAsincronaService;
import it.csi.siac.siaccorser.frontend.webservice.msg.AsyncServiceRequestWrapper;
import it.csi.siac.siaccorser.frontend.webservice.msg.AsyncServiceResponse;
import it.csi.siac.siaccorser.frontend.webservice.msg.InserisciOperazioneAsinc;
import it.csi.siac.siaccorser.frontend.webservice.msg.InserisciOperazioneAsincResponse;
import it.csi.siac.siaccorser.model.Azione;
import it.csi.siac.siaccorser.model.AzioneRichiesta;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaStatoPreDocumentoDiSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaStatoPreDocumentoDiSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AssociaImputazioniContabiliPreDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AssociaImputazioniContabiliVariatePreDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AssociaImputazioniContabiliVariatePreDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.DefiniscePreDocumentoDiSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.DefiniscePreDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.DefiniscePreDocumentoSpesaPerElenco;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.DefiniscePreDocumentoSpesaPerElencoResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.DefiniscePreDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InseriscePreDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InseriscePreDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioPreDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioPreDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaPreDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaPreDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.ValidaStatoOperativoPreDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.ValidaStatoOperativoPreDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.model.CausaleSpesa;
import it.csi.siac.siacfin2ser.model.ContoTesoreria;
import it.csi.siac.siacfin2ser.model.DatiAnagraficiPreDocumentoSpesa;
import it.csi.siac.siacfin2ser.model.ElencoDocumentiAllegato;
import it.csi.siac.siacfin2ser.model.PreDocumentoSpesa;
import it.csi.siac.siacfin2ser.model.StatoOperativoPreDocumento;
import it.csi.siac.siacfin2ser.model.TipoCausale;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto;
import it.csi.siac.siacfinser.model.soggetto.sedesecondaria.SedeSecondariaSoggetto;

// TODO: Auto-generated Javadoc
/**
 * The Class PreDocumentoSpesaDLTest.
 */
public class PreDocumentoSpesa2Test extends BaseJunit4TestCase {
	
	/** The ricerca dettaglio pre documento spesa service. */
	@Autowired
	private RicercaDettaglioPreDocumentoSpesaService ricercaDettaglioPreDocumentoSpesaService;
	
	/** The documento spesa service. */
	@Autowired
	private InseriscePreDocumentoSpesaService inseriscePreDocumentoSpesaService;
	
	/** The aggiorna stato pre documento di spesa service. */
	@Autowired
	private AggiornaStatoPreDocumentoDiSpesaService aggiornaStatoPreDocumentoDiSpesaService;
	
	/** The ricerca sintetica pre documento spesa service. */
	@Autowired
	private RicercaSinteticaPreDocumentoSpesaService ricercaSinteticaPreDocumentoSpesaService;
	
	/** The associa imputazioni contabili pre documento spesa service. */
	@Autowired
	private AssociaImputazioniContabiliPreDocumentoSpesaService associaImputazioniContabiliPreDocumentoSpesaService;
	
	/** The definisce pre documento di spesa service. */
	@Autowired
	private DefiniscePreDocumentoDiSpesaService definiscePreDocumentoDiSpesaService;

	/** The operazione asincrona service. */
	@Autowired
	protected OperazioneAsincronaService operazioneAsincronaService;
	
	@Autowired
	private ValidaStatoOperativoPreDocumentoSpesaService validaStatoOperativoPreDocumentoSpesaService;
	
	@Autowired
	private AssociaImputazioniContabiliVariatePreDocumentoSpesaService associaImputazioniContabiliVariatePreDocumentoSpesaService;
	@Autowired
	private DefiniscePreDocumentoSpesaService definiscePreDocumentoSpesaService;
	@Autowired
	private DefiniscePreDocumentoSpesaAsyncService definiscePreDocumentoSpesaAsyncService;
	@Autowired
	private DefiniscePreDocumentoSpesaPerElencoService definiscePreDocumentoSpesaPerElencoService;
	
	/**
	 * Ricerca sintetica pre documento di spesa.
	 */
	@Test
	public void ricercaSinteticaPreDocumentoDiSpesa(){
		RicercaSinteticaPreDocumentoSpesa req = new RicercaSinteticaPreDocumentoSpesa();
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		
		PreDocumentoSpesa preDoc = new PreDocumentoSpesa();
		preDoc.setEnte(getEnteTest());
		//preDoc.setNumero("3");
		DatiAnagraficiPreDocumentoSpesa dapd = new DatiAnagraficiPreDocumentoSpesa();
		dapd.setCodiceFiscale("LSIDNC85D27L212W");
		dapd.setPartitaIva("0023423423");
		dapd.setRagioneSociale("mia ragione sociale");
		dapd.setNome("mio nome");
		dapd.setCognome("mio cognome");
		dapd.setIndirizzo("mio indirizzo");
		preDoc.setDatiAnagraficiPreDocumento(dapd);
		
		preDoc.setStatoOperativoPreDocumento(StatoOperativoPreDocumento.INCOMPLETO);
		preDoc.setImporto(new BigDecimal("106000.01"));
		
		preDoc.setDataDocumento(new Date());
		preDoc.setDataCompetenza(new Date());
		preDoc.setPeriodoCompetenza("mio periodo competenza");
		
		CausaleSpesa causale = new CausaleSpesa();
		causale.setUid(1);
		TipoCausale tipo = new TipoCausale();
		tipo.setUid(1);
		causale.setTipoCausale(tipo);
		preDoc.setCausaleSpesa(causale);
		
		ContoTesoreria contoTesoreria = new ContoTesoreria();
		//contoTesoreria.setUid(1);
		preDoc.setContoTesoreria(contoTesoreria);
		req.setContoTesoreriaMancante(true);
		
		Soggetto soggetto = new Soggetto();
		soggetto.setUid(7);
		preDoc.setSoggetto(soggetto);
		req.setSoggettoMancante(true);
		
		StrutturaAmministrativoContabile struttura = new StrutturaAmministrativoContabile();
		struttura.setUid(1);
		preDoc.setStrutturaAmministrativoContabile(struttura);
		
		Impegno impegno = new Impegno();
		impegno.setUid(3);
		preDoc.setImpegno(impegno);
		

		CapitoloUscitaGestione capitoloUscitaGestione = new CapitoloUscitaGestione();
		capitoloUscitaGestione.setUid(10);
		preDoc.setCapitoloUscitaGestione(capitoloUscitaGestione);
		
		AttoAmministrativo atto = new AttoAmministrativo();
		atto.setUid(1);
		preDoc.setAttoAmministrativo(atto);
		
		req.setDataCompetenzaA(new Date());
		req.setDataCompetenzaDa(new Date());
		
		req.setCausaleSpesaMancante(true);
		req.setProvvedimentoMancante(true);
		
		req.setPreDocumentoSpesa(preDoc);
		req.setParametriPaginazione(getParametriPaginazioneTest());
		
		RicercaSinteticaPreDocumentoSpesaResponse res = ricercaSinteticaPreDocumentoSpesaService.executeService(req);
		
		assertNotNull(res);
		
		
		
	}
	
	/**
	 * Aggiorna stato pre documento di spesa.
	 */
	@Test
	public void aggiornaStatoPreDocumentoDiSpesa() {
		
		PreDocumentoSpesa preDocumentoSpesa = new PreDocumentoSpesa();
		preDocumentoSpesa.setEnte(getEnteTest());
		preDocumentoSpesa.setUid(25);
				
		AggiornaStatoPreDocumentoDiSpesa req = new AggiornaStatoPreDocumentoDiSpesa();
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		req.setPreDocumentoSpesa(preDocumentoSpesa);
						
		AggiornaStatoPreDocumentoDiSpesaResponse res = aggiornaStatoPreDocumentoDiSpesaService.executeService(req);
		
		assertNotNull(res);
	}
	
	/**
	 * Inserisci pre documento spesa.
	 */
	@Test
	public void inserisciPreDocumentoSpesa() {
		InseriscePreDocumentoSpesa req = new InseriscePreDocumentoSpesa();
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		req.setBilancio(getBilancioTest());
		PreDocumentoSpesa preDoc = new PreDocumentoSpesa();
		preDoc.setEnte(getEnteTest());
		preDoc.setNumero("3");
		preDoc.setDescrizione("pre doc di test");
		preDoc.setImporto(new BigDecimal("106000.01"));
		preDoc.setStatoOperativoPreDocumento(StatoOperativoPreDocumento.INCOMPLETO);
		preDoc.setDataDocumento(new Date());
		preDoc.setDataCompetenza(new Date());
		preDoc.setPeriodoCompetenza("mio periodo competenza");
		
		
		CausaleSpesa causale = new CausaleSpesa();
		causale.setUid(1);
		preDoc.setCausaleSpesa(causale);
		
		ContoTesoreria contoTesoreria = new ContoTesoreria();
		contoTesoreria.setUid(1);
		preDoc.setContoTesoreria(contoTesoreria);
		
		preDoc.setNote("mie note predocumento 1");
		
		DatiAnagraficiPreDocumentoSpesa dapd = new DatiAnagraficiPreDocumentoSpesa();
		dapd.setCodiceFiscale("LSIDNC85D27L212W");
		dapd.setPartitaIva("0023423423");
		dapd.setRagioneSociale("mia ragione sociale");
		dapd.setNome("mio nome");
		dapd.setCognome("mio cognome");
		dapd.setDataNascita(new Date());
		dapd.setIndirizzo("mio indirizzo");
		
		preDoc.setDatiAnagraficiPreDocumento(dapd);
				
		Soggetto soggetto = new Soggetto();
		soggetto.setUid(7);
		preDoc.setSoggetto(soggetto);
		
		CapitoloUscitaGestione capitoloUscitaGestione = new CapitoloUscitaGestione();
		capitoloUscitaGestione.setUid(10);
		preDoc.setCapitoloUscitaGestione(capitoloUscitaGestione);
		
		
		Impegno impegno = new Impegno();
		impegno.setUid(3);
		impegno.setAnnoMovimento(2013);
		impegno.setNumeroBigDecimal(new BigDecimal("3"));
		preDoc.setImpegno(impegno);
		
		SubImpegno subImpegno = new SubImpegno();
		subImpegno.setUid(4);
		preDoc.setSubImpegno(subImpegno);
		
		
		req = marshallUnmarshall(req);
		
		req.setPreDocumentoSpesa(preDoc);
		
		InseriscePreDocumentoSpesaResponse res = inseriscePreDocumentoSpesaService.executeService(req);
		
		res = marshallUnmarshall(res);

		assertNotNull(res);
	}
	
	
	
	/**
	 * Ricerca dettaglio pre documento spesa.
	 */
	@Test
	public void ricercaDettaglioPreDocumentoSpesa() {
			
		RicercaDettaglioPreDocumentoSpesa req = new RicercaDettaglioPreDocumentoSpesa();
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		PreDocumentoSpesa preDoc = new PreDocumentoSpesa();
		preDoc.setUid(10);
		req.setPreDocumentoSpesa(preDoc);
		
		RicercaDettaglioPreDocumentoSpesaResponse res = ricercaDettaglioPreDocumentoSpesaService.executeService(req);

		assertNotNull(res);
	}
	
	

	/**
	 * Associa imputazioni contabili.
	 */
	@Test
	public void associaImputazioniContabili(){
		AssociaImputazioniContabiliPreDocumentoSpesa req = new AssociaImputazioniContabiliPreDocumentoSpesa();
		req.setBilancio(getBilancioTest());
		req.setEnte(getEnteTest());
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		
		List<PreDocumentoSpesa> preDocumentiSpesa = new ArrayList<PreDocumentoSpesa>();
		PreDocumentoSpesa preDoc1 = new PreDocumentoSpesa();
		preDoc1.setUid(90);
		preDocumentiSpesa.add(preDoc1);
		req.setPreDocumentiSpesa(preDocumentiSpesa);
		associaImputazioniContabiliPreDocumentoSpesaService.executeService(req);
	}
	
	
	/**
	 * Definisci pre documento spesa.
	 */
	@Test
	public void definisciPreDocumentoSpesa(){
		InserisciOperazioneAsinc reqIOS =  new InserisciOperazioneAsinc();
		reqIOS.setAccount(getRichiedenteByProperties("consip","regp").getAccount());
		AzioneRichiesta azioneRichiesta = new AzioneRichiesta();
		azioneRichiesta.setUid(66027183);//66027179
		Azione azione = new Azione();
		azione.setUid(22);
		azioneRichiesta.setAzione(azione);
		reqIOS.setAzioneRichiesta(azioneRichiesta);
		reqIOS.setEnte(getEnteTest());
		reqIOS.setRichiedente(getRichiedenteByProperties("consip","regp"));
		
		InserisciOperazioneAsincResponse resIOS = operazioneAsincronaService.inserisciOperazioneAsinc(reqIOS);
		
		Integer idOperazione=resIOS.getIdOperazione();
		
		DefiniscePreDocumentoDiSpesa req = new DefiniscePreDocumentoDiSpesa();
		req.setBilancio(getBilancio2014Test());
		req.setEnte(getEnteTest());
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		
		req.setIdOperazioneAsincrona(idOperazione);
		
		List<PreDocumentoSpesa> preDocumentiSpesa = new ArrayList<PreDocumentoSpesa>();
		
		PreDocumentoSpesa preDoc1 = new PreDocumentoSpesa();
		preDoc1.setUid(2);
		preDocumentiSpesa.add(preDoc1);		
		
//		PreDocumentoSpesa preDoc2 = new PreDocumentoSpesa();
//		preDoc2.setUid(6);
//		preDocumentiSpesa.add(preDoc2);
//		
		
		req.setPreDocumentiSpesa(preDocumentiSpesa);
		
		
		definiscePreDocumentoDiSpesaService.executeService(req);
	}
	
	@Test 
	public void validaStatoOperativo(){
		ValidaStatoOperativoPreDocumentoSpesa req = new ValidaStatoOperativoPreDocumentoSpesa();
		req.setBilancio(getBilancioTest());
		req.setEnte(getEnteTest());
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		req.setStatoOperativoPreDocumento(StatoOperativoPreDocumento.ANNULLATO);
		
		PreDocumentoSpesa predoc = new PreDocumentoSpesa();
		predoc.setUid(75);
		predoc.setStatoOperativoPreDocumento(StatoOperativoPreDocumento.ANNULLATO);
		CausaleSpesa causaleSpesa = new CausaleSpesa();
		causaleSpesa.setUid(39);
		predoc.setCausaleSpesa(causaleSpesa);
		
		req.setPreDocumentoSpesa(predoc);
		
		ValidaStatoOperativoPreDocumentoSpesaResponse res = validaStatoOperativoPreDocumentoSpesaService.executeService(req);
		assertNotNull(res);
	}
	
	
	
	
	/*
	 * 
	 
	 <associaImputazioniContabiliPreDocumentoSpesa>
    <dataOra>2014-05-07T15:12:07.384+02:00</dataOra>
    <richiedente>
        <account>
            <stato>VALIDO</stato>
            <uid>1</uid>
            <nome>Demo 21</nome>
            <descrizione>Demo 21 - CittÃÂ  di Torino</descrizione>
            <indirizzoMail>email</indirizzoMail>
            <ente>
                <stato>VALIDO</stato>
                <uid>1</uid>
                <gestioneLivelli>
                    <entry>
                        <key>LIVELLO_GESTIONE_BILANCIO</key>
                        <value>GESTIONE_UEB</value>
                    </entry>
                </gestioneLivelli>
                <nome>CittÃÂ  di Torino</nome>
            </ente>
        </account>
        <operatore>
            <stato>VALIDO</stato>
            <uid>0</uid>
            <codiceFiscale>AAAAAA00A11B000J</codiceFiscale>
            <cognome>AAAAAA00A11B000J</cognome>
            <nome>Demo</nome>
        </operatore>
    </richiedente>
    <ente>
        <stato>VALIDO</stato>
        <uid>1</uid>
        <gestioneLivelli>
            <entry>
                <key>LIVELLO_GESTIONE_BILANCIO</key>
                <value>GESTIONE_UEB</value>
            </entry>
        </gestioneLivelli>
        <nome>CittÃÂ  di Torino</nome>
    </ente>
    <idOperazioneAsincrona>22</idOperazioneAsincrona>
    <bilancio>
        <stato>VALIDO</stato>
        <uid>1</uid>
        <anno>2013</anno>
    </bilancio>
    <preDocumentiSpesa>
        <stato>VALIDO</stato>
        <uid>6</uid>
        <importo>0</importo>
    </preDocumentiSpesa>
</associaImputazioniContabiliPreDocumentoSpesa>

	 
	 
	 */
	
	
	
	
	
	
	
	@Test
	public void test() {
		StringBuilder sb = new StringBuilder()
			.append("<inseriscePreDocumentoSpesa>")
			.append("    <dataOra>2014-10-31T15:30:55.918+01:00</dataOra>")
			.append("    <richiedente>")
			.append("        <account>")
			.append("            <uid>1</uid>")
			.append("            <ente>")
			.append("                <uid>1</uid>")
			.append("            </ente>")
			.append("        </account>")
			.append("        <operatore>")
			.append("            <codiceFiscale>AAAAAA00A11B000J</codiceFiscale>")
			.append("            <cognome>AAAAAA00A11B000J</cognome>")
			.append("            <nome>Demo</nome>")
			.append("        </operatore>")
			.append("    </richiedente>")
			.append("    <bilancio>")
			.append("        <stato>VALIDO</stato>")
			.append("        <uid>6</uid>")
			.append("        <anno>2014</anno>")
			.append("    </bilancio>")
			.append("    <preDocumentoSpesa>")
			.append("        <stato>VALIDO</stato>")
			.append("        <uid>0</uid>")
			.append("        <datiAnagraficiPreDocumentoSpesa>")
			.append("            <stato>VALIDO</stato>")
			.append("            <uid>0</uid>")
			.append("            <codiceFiscale></codiceFiscale>")
			.append("            <cognome></cognome>")
			.append("            <comuneIndirizzo>, </comuneIndirizzo>")
			.append("            <indirizzo></indirizzo>")
			.append("            <indirizzoEmail></indirizzoEmail>")
			.append("            <nazioneIndirizzo>ITALIA</nazioneIndirizzo>")
			.append("            <nazioneNascita>ITALIA</nazioneNascita>")
			.append("            <nome></nome>")
			.append("            <numTelefono></numTelefono>")
			.append("            <partitaIva></partitaIva>")
			.append("            <ragioneSociale></ragioneSociale>")
			.append("            <codiceABI></codiceABI>")
			.append("            <codiceBic></codiceBic>")
			.append("            <codiceCAB></codiceCAB>")
			.append("            <codiceFiscaleQuietanzante></codiceFiscaleQuietanzante>")
			.append("            <codiceIban></codiceIban>")
			.append("            <contoCorrente></contoCorrente>")
			.append("            <intestazioneConto></intestazioneConto>")
			.append("            <soggettoQuietanzante></soggettoQuietanzante>")
			.append("        </datiAnagraficiPreDocumentoSpesa>")
			.append("        <dataCompetenza>2014-10-15T00:00:00+02:00</dataCompetenza>")
			.append("        <dataDocumento>2014-10-31T00:00:00+01:00</dataDocumento>")
			.append("        <descrizione></descrizione>")
			.append("        <ente>")
			.append("            <uid>1</uid>")
			.append("        </ente>")
			.append("        <flagManuale>true</flagManuale>")
			.append("        <importo>10.00</importo>")
			.append("        <note></note>")
			.append("        <periodoCompetenza>201410</periodoCompetenza>")
			.append("        <soggetto>")
			.append("            <stato>VALIDO</stato>")
			.append("            <uid>71979</uid>")
			.append("            <codiceSoggetto>95</codiceSoggetto>")
			.append("        </soggetto>")
			.append("        <statoOperativoPreDocumento>INCOMPLETO</statoOperativoPreDocumento>")
			.append("        <causaleSpesa>")
			.append("            <stato>VALIDO</stato>")
			.append("            <uid>59</uid>")
			.append("            <annoCatalogazione>0</annoCatalogazione>")
			.append("        </causaleSpesa>")
			.append("        <contoTesoreria>")
			.append("            <stato>VALIDO</stato>")
			.append("            <uid>10</uid>")
			.append("            <annoCatalogazione>0</annoCatalogazione>")
			.append("        </contoTesoreria>")
			.append("        <impegno>")
			.append("            <uid>36920</uid>")
			.append("            <annoMovimento>2014</annoMovimento>")
			.append("            <numero>153</numero>")
			.append("        </impegno>")
			.append("    </preDocumentoSpesa>")
			.append("</inseriscePreDocumentoSpesa>");
		
		InseriscePreDocumentoSpesa request = JAXBUtility.unmarshall(sb.toString(), InseriscePreDocumentoSpesa.class);
		InseriscePreDocumentoSpesaResponse response = inseriscePreDocumentoSpesaService.executeService(request);
		assertNotNull(response);
	}

	@Test
	public void associaImputazioniContabiliVariatePreDocumentoSpesa() {
		AssociaImputazioniContabiliVariatePreDocumentoSpesa req = new AssociaImputazioniContabiliVariatePreDocumentoSpesa();
		
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("dev", "ente1"));
		req.setBilancio(getBilancioTest(16, 2015));
		
		req.setCapitoloUscitaGestione(create(CapitoloUscitaGestione.class, 0));
		req.setImpegno(create(Impegno.class, 46018));
		req.setSubImpegno(create(SubImpegno.class, 87080));
		req.setSoggetto(create(Soggetto.class, 114431));
		req.setAttoAmministrativo(create(AttoAmministrativo.class, 0));
		req.setSedeSecondariaSoggetto(create(SedeSecondariaSoggetto.class, 0));
		req.setModalitaPagamentoSoggetto(create(ModalitaPagamentoSoggetto.class, 0));
		
		req.getPreDocumentiSpesa().add(create(PreDocumentoSpesa.class, 571));
		req.getPreDocumentiSpesa().add(create(PreDocumentoSpesa.class, 570));
		
		AssociaImputazioniContabiliVariatePreDocumentoSpesaResponse res = associaImputazioniContabiliVariatePreDocumentoSpesaService.executeService(req);
		assertNotNull(res);
		
	}
	
	@Test
	public void definiscePreDocumentoSpesa() {
		DefiniscePreDocumentoSpesa req = new DefiniscePreDocumentoSpesa();
		
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "crp"));
		req.setBilancio(getBilancioTest(143, 2017));

		// Saltato
//		req.getPreDocumentiSpesa().add(create(PreDocumentoSpesa.class, 571));
		// Da elaborare
		req.getPreDocumentiSpesa().add(create(PreDocumentoSpesa.class, 12));
		
		DefiniscePreDocumentoSpesaResponse res = definiscePreDocumentoSpesaService.executeService(req);
		assertNotNull(res);
	}
	
	@Test
	public void definiscePreDocumentoSpesaAsync() {
		final String methodName = "definiscePreDocumentoSpesaAsync";
		DefiniscePreDocumentoSpesa req = new DefiniscePreDocumentoSpesa();
		
		Date now = new Date();
		Richiedente richiedente = getRichiedenteByProperties("dev", "ente1");
		
		req.setDataOra(now);
		req.setRichiedente(richiedente);
		req.setBilancio(getBilancioTest(16, 2015));

		// Saltato
		req.getPreDocumentiSpesa().add(create(PreDocumentoSpesa.class, 571));
		// Da elaborare
		req.getPreDocumentiSpesa().add(create(PreDocumentoSpesa.class, 55));
		
		
		AsyncServiceRequestWrapper<DefiniscePreDocumentoSpesa> areq = new AsyncServiceRequestWrapper<DefiniscePreDocumentoSpesa>();
		areq.setAccount(richiedente.getAccount());
		areq.setDataOra(now);
		areq.setAzioneRichiesta(create(AzioneRichiesta.class, 66058900));
		areq.getAzioneRichiesta().setAzione(create(Azione.class, 4402));
		areq.setRichiedente(richiedente);
		areq.setEnte(richiedente.getAccount().getEnte());
		areq.setRequest(req);
		
		AsyncServiceResponse res = definiscePreDocumentoSpesaAsyncService.executeService(areq);
		assertNotNull(res);
		
		try {
			Thread.sleep(5 * 60 * 1000);
		} catch (InterruptedException e) {
			// Ignoro
			log.error(methodName, "Interrupted exception", e);
		}
	}

	@Test
	public void definiscePreDocumentoSpesaPerElenco() {
		DefiniscePreDocumentoSpesaPerElenco req = new DefiniscePreDocumentoSpesaPerElenco();
		
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("dev", "ente1"));
		req.setElencoDocumentiAllegato(create(ElencoDocumentiAllegato.class, 418));
		req.setBilancio(getBilancioTest(16, 2015));
		req.setImpegno(create(Impegno.class, 95613));
		
		DefiniscePreDocumentoSpesaPerElencoResponse res = definiscePreDocumentoSpesaPerElencoService.executeService(req);
		assertNotNull(res);
	}
	
	/**
	 * Inserimento del predocumento di spesa
	 */
	@Test
	public void inseriscePreDocumentoSpesa() {
		final String methodName = "inseriscePreDocumentoSpesa";
		
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 2015);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		Date now = cal.getTime();
		
		InseriscePreDocumentoSpesa req = new InseriscePreDocumentoSpesa();
		
		req.setDataOra(now);
		req.setRichiedente(getRichiedenteByProperties("dev", "ente1"));
		
		req.setBilancio(getBilancioTest(16, 2015));
		req.setInserisciElenco(true);
		
		PreDocumentoSpesa preDocumentoSpesa = new PreDocumentoSpesa();
		req.setPreDocumentoSpesa(preDocumentoSpesa);
		
		preDocumentoSpesa.setEnte(req.getRichiedente().getAccount().getEnte());
		preDocumentoSpesa.setDataCompetenza(new Date());
		preDocumentoSpesa.setPeriodoCompetenza("201507");
		preDocumentoSpesa.setCausaleSpesa(create(CausaleSpesa.class, 53));
		preDocumentoSpesa.setContoTesoreria(create(ContoTesoreria.class, 5));
		preDocumentoSpesa.setDataDocumento(now);
		preDocumentoSpesa.setImporto(BigDecimal.TEN);
		preDocumentoSpesa.setStatoOperativoPreDocumento(StatoOperativoPreDocumento.INCOMPLETO);
		
		ElencoDocumentiAllegato eda = create(ElencoDocumentiAllegato.class, 418);
		
		Soggetto[] soggetti = new Soggetto[] {create(Soggetto.class, 7), create(Soggetto.class, 69)};
		ModalitaPagamentoSoggetto[] modalitaPagamentoSoggettos = new ModalitaPagamentoSoggetto[] {create(ModalitaPagamentoSoggetto.class, 23), create(ModalitaPagamentoSoggetto.class, 300900)};
		
		List<String> results = new ArrayList<String>();
		
		for(int i = 0; i < 10; i++) {
			preDocumentoSpesa.setDescrizione("PreDocumento spesa 03/07/2017, numero " + i + " per test batch");
			preDocumentoSpesa.setElencoDocumentiAllegato(eda);
			preDocumentoSpesa.setSoggetto(soggetti[i % soggetti.length]);
			preDocumentoSpesa.setModalitaPagamentoSoggetto(modalitaPagamentoSoggettos[i % modalitaPagamentoSoggettos.length]);
			
			InseriscePreDocumentoSpesaResponse res = inseriscePreDocumentoSpesaService.executeService(req);
			assertNotNull(res);
			assertTrue(res.getErrori().isEmpty());
			
			results.add("Inserito predoc con uid " + res.getPreDocumentoSpesa().getUid());
		}
		results.add("Elenco con uid " + eda.getUid());
		
		for(String str : results) {
			log.info(methodName, str);
		}
	}
}
