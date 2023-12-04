/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.allegatoatto;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacbilser.business.service.allegatoatto.AggiornaDatiSoggettoAllegatoAttoService;
import it.csi.siac.siacbilser.business.service.allegatoatto.AggiornaMassivaDatiSoggettoAllegatoAttoService;
import it.csi.siac.siacbilser.business.service.allegatoatto.AnnullaAllegatoAttoService;
import it.csi.siac.siacbilser.business.service.allegatoatto.AssociaElencoService;
import it.csi.siac.siacbilser.business.service.allegatoatto.CompletaAllegatoAttoAsyncService;
import it.csi.siac.siacbilser.business.service.allegatoatto.CompletaAllegatoAttoMultiploAsyncService;
import it.csi.siac.siacbilser.business.service.allegatoatto.CompletaAllegatoAttoMultiploService;
import it.csi.siac.siacbilser.business.service.allegatoatto.CompletaAllegatoAttoService;
import it.csi.siac.siacbilser.business.service.allegatoatto.ControlloImportiImpegniVincolatiService;
import it.csi.siac.siacbilser.business.service.allegatoatto.ConvalidaAllegatoAttoPerElenchiAsyncService;
import it.csi.siac.siacbilser.business.service.allegatoatto.ConvalidaAllegatoAttoPerElenchiMultiploAsyncService;
import it.csi.siac.siacbilser.business.service.allegatoatto.ConvalidaAllegatoAttoPerElenchiMultiploService;
import it.csi.siac.siacbilser.business.service.allegatoatto.ConvalidaAllegatoAttoPerElenchiService;
import it.csi.siac.siacbilser.business.service.allegatoatto.DisassociaElencoService;
import it.csi.siac.siacbilser.business.service.allegatoatto.EliminaQuotaDaElencoService;
import it.csi.siac.siacbilser.business.service.allegatoatto.InserisceAllegatoAttoService;
import it.csi.siac.siacbilser.business.service.allegatoatto.InserisceElencoConDocumentiConQuoteService;
import it.csi.siac.siacbilser.business.service.allegatoatto.InserisceElencoService;
import it.csi.siac.siacbilser.business.service.allegatoatto.InviaAllegatoAttoService;
import it.csi.siac.siacbilser.business.service.allegatoatto.RiCompletaAllegatoAttoPerElenchiService;
import it.csi.siac.siacbilser.business.service.allegatoatto.RicercaAllegatoAttoService;
import it.csi.siac.siacbilser.business.service.allegatoatto.RicercaDatiSospensioneAllegatoAttoService;
import it.csi.siac.siacbilser.business.service.allegatoatto.RicercaDettaglioAllegatoAttoService;
import it.csi.siac.siacbilser.business.service.allegatoatto.RicercaDettaglioElencoService;
import it.csi.siac.siacbilser.business.service.allegatoatto.RicercaElencoService;
import it.csi.siac.siacbilser.business.service.allegatoatto.RicercaSinteticaQuoteElencoService;
import it.csi.siac.siacbilser.business.service.allegatoatto.RicercaSinteticaStampaAllegatoAttoService;
import it.csi.siac.siacbilser.business.service.allegatoatto.RifiutaElenchiService;
import it.csi.siac.siacbilser.business.service.stampa.allegatoatto.InserisceStampaAllegatoAttoService;
import it.csi.siac.siacbilser.business.service.stampa.allegatoatto.StampaAllegatoAttoAsyncService;
import it.csi.siac.siacbilser.business.service.stampa.allegatoatto.StampaAllegatoAttoService;
import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.integration.dad.AllegatoAttoDad;
import it.csi.siac.siacbilser.integration.dad.ElaborazioniDad;
import it.csi.siac.siacbilser.integration.dad.ElencoDocumentiAllegatoDad;
import it.csi.siac.siacbilser.integration.dad.ImpegnoBilDad;
import it.csi.siac.siacbilser.integration.dad.SubdocumentoSpesaDad;
import it.csi.siac.siacbilser.integration.dao.SiacTAttoAllegatoRepository;
import it.csi.siac.siacbilser.integration.dao.SiacTElencoDocRepository;
import it.csi.siac.siacbilser.integration.dao.SiacTMovgestTRepository;
import it.csi.siac.siacbilser.integration.dao.SiacTSubdocRepository;
import it.csi.siac.siacbilser.integration.entity.SiacRElencoDocSubdoc;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDDocFamTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacTAttrEnum;
import it.csi.siac.siacbilser.integration.exception.ElaborazioneAttivaException;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;
import it.csi.siac.siaccecser.frontend.webservice.msg.InviaAllegatoAtto;
import it.csi.siac.siaccecser.frontend.webservice.msg.StampaAllegatoAtto;
import it.csi.siac.siaccecser.frontend.webservice.msg.StampaAllegatoAttoResponse;
import it.csi.siac.siaccommon.util.JAXBUtility;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccorser.frontend.webservice.msg.AsyncServiceRequestWrapper;
import it.csi.siac.siaccorser.frontend.webservice.msg.AsyncServiceResponse;
import it.csi.siac.siaccorser.model.Account;
import it.csi.siac.siaccorser.model.Azione;
import it.csi.siac.siaccorser.model.AzioneRichiesta;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Messaggio;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.file.File;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaDatiSoggettoAllegatoAtto;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaDatiSoggettoAllegatoAttoResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaMassivaDatiSoggettoAllegatoAtto;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaMassivaDatiSoggettoAllegatoAttoResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AnnullaAllegatoAtto;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AnnullaAllegatoAttoResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AssociaElenco;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AssociaElencoResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.CompletaAllegatoAtto;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.CompletaAllegatoAttoMultiplo;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.CompletaAllegatoAttoMultiploResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.CompletaAllegatoAttoResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.ControlloImportiImpegniVincolati;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.ControlloImportiImpegniVincolatiResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.ConvalidaAllegatoAttoPerElenchi;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.ConvalidaAllegatoAttoPerElenchiMultiplo;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.ConvalidaAllegatoAttoPerElenchiMultiploResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.ConvalidaAllegatoAttoPerElenchiResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.DisassociaElenco;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.DisassociaElencoResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EliminaQuotaDaElenco;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EliminaQuotaDaElencoResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceAllegatoAtto;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceAllegatoAttoResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceElenco;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceElencoResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceStampaAllegatoAtto;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceStampaAllegatoAttoResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RiCompletaAllegatoAttoPerElenchi;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RiCompletaAllegatoAttoPerElenchiResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaAllegatoAtto;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaAllegatoAttoResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDatiSospensioneAllegatoAtto;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDatiSospensioneAllegatoAttoResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioAllegatoAtto;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioAllegatoAttoResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioElenco;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioElencoResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaElenco;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaElencoResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaQuoteElenco;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaQuoteElencoResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaStampaAllegatoAtto;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaStampaAllegatoAttoResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RifiutaElenchi;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RifiutaElenchiResponse;
import it.csi.siac.siacfin2ser.model.AllegatoAtto;
import it.csi.siac.siacfin2ser.model.AllegatoAttoModelDetail;
import it.csi.siac.siacfin2ser.model.AllegatoAttoStampa;
import it.csi.siac.siacfin2ser.model.DatiSoggettoAllegato;
import it.csi.siac.siacfin2ser.model.Documento;
import it.csi.siac.siacfin2ser.model.ElencoDocumentiAllegato;
import it.csi.siac.siacfin2ser.model.ElencoDocumentiAllegatoModelDetail;
import it.csi.siac.siacfin2ser.model.StatoOperativoAllegatoAtto;
import it.csi.siac.siacfin2ser.model.StatoOperativoElencoDocumenti;
import it.csi.siac.siacfin2ser.model.Subdocumento;
import it.csi.siac.siacfin2ser.model.SubdocumentoIva;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfin2ser.model.TipoDocumento;
import it.csi.siac.siacfin2ser.model.TipoStampaAllegatoAtto;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.siopeplus.SiopeAssenzaMotivazione;
import it.csi.siac.siacfinser.model.siopeplus.SiopeTipoDebito;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto;

// TODO: Auto-generated Javadoc
/**
 * The Class AllegatoAttoDLTest.
 */
public class AllegatoAttoTest extends BaseJunit4TestCase {
	

	/** The inserisce AllegatoAtto service. */
	@Autowired
	private InserisceAllegatoAttoService inserisceAllegatoAttoService;
	@Autowired
	private RicercaDettaglioAllegatoAttoService ricercaDettaglioAllegatoAttoService;
	@Autowired
	private RicercaAllegatoAttoService ricercaAllegatoAttoService;
	@Autowired
	private InserisceElencoService inserisceElencoService;
	@Autowired
	private RicercaDettaglioElencoService ricercaDettaglioElencoService;
	@Autowired
	private InserisceElencoConDocumentiConQuoteService inserisceElencoConDocumentiConQuoteService;
	@Autowired
	private RicercaElencoService ricercaElencoService;
	@Autowired
	private AssociaElencoService associaElencoService;
	@Autowired
	private DisassociaElencoService disassociaElencoService;
	@Autowired
	private AggiornaDatiSoggettoAllegatoAttoService aggiornaDatiSoggettoAllegatoAttoService;
	@Autowired
	private AnnullaAllegatoAttoService annullaAllegatoAttoService;
	@Autowired
	private CompletaAllegatoAttoService completaAllegatoAttoService;
	@Autowired
	private CompletaAllegatoAttoAsyncService completaAllegatoAttoAsyncService;
	@Autowired
	private ConvalidaAllegatoAttoPerElenchiService convalidaAllegatoAttoPerElenchiService;
	@Autowired
	private ConvalidaAllegatoAttoPerElenchiAsyncService convalidaAllegatoAttoPerElenchiAsyncService;
	@Autowired
	private RiCompletaAllegatoAttoPerElenchiService riCompletaAllegatoAttoPerElenchiService;
	@Autowired
	private EliminaQuotaDaElencoService eliminaQuotaDaElencoService;
	@Autowired
	private RifiutaElenchiService rifiutaElenchiService;
	@Autowired
	private RicercaSinteticaQuoteElencoService ricercaSinteticaQuoteElencoService;
	@Autowired
	private StampaAllegatoAttoService stampaAllegatoAttoService;
	@Autowired
	private InviaAllegatoAttoService inviaAllegatoAttoService;
	@Autowired
	private StampaAllegatoAttoAsyncService stampaAllegatoAttoAsyncService;
	@Autowired
	private RicercaSinteticaStampaAllegatoAttoService ricercaSinteticaStampaAllegatoAttoService;
	@Autowired
	private InserisceStampaAllegatoAttoService inserisceStampaAllegatoAttoService;
	@Autowired
	private AggiornaMassivaDatiSoggettoAllegatoAttoService aggiornaMassivaDatiSoggettoAllegatoAttoService;
	
	@Autowired
	private CompletaAllegatoAttoMultiploService completaAllegatoAttoMultiploService;
	
	@Autowired
	private CompletaAllegatoAttoMultiploAsyncService completaAllegatoAttoMultiploAsyncService;
	
	@Autowired
	private ConvalidaAllegatoAttoPerElenchiMultiploService convalidaAllegatoAttoPerElenchiMultiploService;
	@Autowired
	private ConvalidaAllegatoAttoPerElenchiMultiploAsyncService convalidaAllegatoAttoPerElenchiMultiploAsyncService;
	
	@Autowired
	private SiacTMovgestTRepository siacTMovgestTRepository;
	@Autowired
	private SiacTElencoDocRepository siacTElencoDocRepository;
	
	@Autowired
	private SiacTAttoAllegatoRepository siacTAttoAllegatoRepository;
	
	@Autowired
	private ElencoDocumentiAllegatoDad elencoDocumentiAllegatoDad;
	@Autowired
	private AllegatoAttoDad allegatoAttoDad;
	@Autowired
	private ElaborazioniDad elaborazioniDad;
	
	@Autowired
	private ImpegnoBilDad impegnoBilDad;
	@Autowired
	private ImpegnoBilDad subImpegnoBilDad;
	
	
	@Autowired
	private ControlloImportiImpegniVincolatiService controlloImportiImpegniVincolatiService;
	
	
	@Test
	public void testCountQuoteAllegato() {
		allegatoAttoDad.findSubdocumentiSpesaRilevantiIvaNonCollegatiALiquidazioneConImportoDaPagareMaggioreDiZeroNonSospesi(create(AllegatoAtto.class, 13));
	}
	
	
	@Test
	public void impegno(){
		
		List<String> ricorrenteSpesa = new ArrayList<String>();
		ricorrenteSpesa.add("RICORRENTE_SPESA");
		
		List<String> contoEconomico = new ArrayList<String>();
		contoEconomico.add("PCE_I");
		contoEconomico.add("PCE_II");
		contoEconomico.add("PCE_III");
		contoEconomico.add("PCE_IV");
		contoEconomico.add("PCE_V");
		
		List<String> pianoDeiConti = new ArrayList<String>();
		pianoDeiConti.add("PDC_I");
		pianoDeiConti.add("PDC_II");
		pianoDeiConti.add("PDC_III");
		pianoDeiConti.add("PDC_IV");
		pianoDeiConti.add("PDC_V");
		
		List<String> transazioneEuropea = new ArrayList<String>();
		transazioneEuropea.add("TRANSAZIONE_UE_SPESA");
		
//		String codice = siacTMovgestTRepository.findCodiceClassificatoreByUidImpegnoECodiceTipo(1, contoEconomico);
//		String codice = siacTMovgestTRepository.findCodiceClassificatoreByUidImpegnoECodiceTipo(31034, pianoDeiConti);
//		String codice = siacTMovgestTRepository.findCodiceClassificatoreByUidImpegnoECodiceTipo(31034, transazioneEuropea);
//		String codice = siacTMovgestTRepository.findCodiceClassificatoreByUidImpegnoECodiceTipo(31034, ricorrenteSpesa);
		
//		String codice = siacTMovgestTRepository.findCodiceClassificatoreByUidSubImpegnoECodiceTipo(31756, ricorrenteSpesa);
		String codice = siacTMovgestTRepository.findCodiceClassificatoreByUidSubImpegnoECodiceTipo(29, contoEconomico);
		//String codice = siacTMovgestTRepository.findCodiceClassificatoreByUidSubImpegnoECodiceTipo(31756, pianoDeiConti);
//		String codice = siacTMovgestTRepository.findCodiceClassificatoreByUidSubImpegnoECodiceTipo(31756, transazioneEuropea);
		
		log.debug("impegno ", "conto trovato: " + codice);
	} 
	
	@Test
	public void inserisceAllegatoAtto() {
			
		InserisceAllegatoAtto req = new InserisceAllegatoAtto();
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		AllegatoAtto aa = new AllegatoAtto();
		aa = new AllegatoAtto();
		aa.setAltriAllegati("altri allegati");
		aa.setAnnotazioni("mie annotazioni");
		
		AttoAmministrativo atto = new AttoAmministrativo();
		atto.setUid(2);
		aa.setAttoAmministrativo(atto);
		
		aa.setCausale("mia causale");
		aa.setDatiSensibili(Boolean.TRUE);
		
		List<DatiSoggettoAllegato> datiSoggettiAllegati = new ArrayList<DatiSoggettoAllegato>();
		DatiSoggettoAllegato dsa = new DatiSoggettoAllegato();
		dsa.setCausaleSospensione("mia causale sospensione");
		dsa.setDataRiattivazione(new Date());
		
		Soggetto soggetto = new Soggetto();
		soggetto.setUid(3);
		dsa.setSoggetto(soggetto);
		
		datiSoggettiAllegati.add(dsa);
		aa.setDatiSoggettiAllegati(datiSoggettiAllegati);
		
		
		List<ElencoDocumentiAllegato> elenchiDocumentiAllegato = new ArrayList<ElencoDocumentiAllegato>();
		ElencoDocumentiAllegato elenco = new ElencoDocumentiAllegato();
		elenco.setUid(4);
		elenchiDocumentiAllegato.add(elenco);
		aa.setElenchiDocumentiAllegato(elenchiDocumentiAllegato);
		
		aa.setEnte(getEnteTest());
		
		aa.setNote("miee noooteeee!!");
		
		aa.setPratica("mia pratica!");
		aa.setResponsabileAmministrativo("mio resp amm");
		aa.setResponsabileContabile("mio resp cont");
		aa.setStatoOperativoAllegatoAtto(StatoOperativoAllegatoAtto.DA_COMPLETARE);
		
		
		req.setAllegatoAtto(aa);
		InserisceAllegatoAttoResponse res = inserisceAllegatoAttoService.executeService(req);

		assertNotNull(res);
	}

	@Test
	public void ricercaDettaglioAllegatoAtto() {
		final String methodName  = "ricercaDettaglioAllegatoAtto";
		RicercaDettaglioAllegatoAtto req = new RicercaDettaglioAllegatoAtto();
		
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setDataOra(new Date());
		req.setAllegatoAtto(create(AllegatoAtto.class, 11354));
//		req.setAllegatoAttoModelDetails(AllegatoAttoModelDetail.ElencoDocumentiConPagatoIncassato);
		req.setAllegatoAttoModelDetails(AllegatoAttoModelDetail.DataInizioValiditaStato, AllegatoAttoModelDetail.DatiSoggetto, AllegatoAttoModelDetail.ElencoDocumentiModelDetail);
		req.setModelDetailsEntitaCollegate(ElencoDocumentiAllegatoModelDetail.TotaleDaPagareIncassare,ElencoDocumentiAllegatoModelDetail.TotaleQuoteSpesaEntrata,
				ElencoDocumentiAllegatoModelDetail.ContieneQuoteACopertura,ElencoDocumentiAllegatoModelDetail.SubdocumentiTotale,
				ElencoDocumentiAllegatoModelDetail.ContieneQuoteACopertura,ElencoDocumentiAllegatoModelDetail.TotaleDaConvalidareSpesaEntrata
				);
		
		RicercaDettaglioAllegatoAttoResponse res = ricercaDettaglioAllegatoAttoService.executeService(req);
		assertNotNull(res);
//		AllegatoAtto aa = res.getAllegatoAtto();
//		DatiSoggettoAllegato datiSogg = new DatiSoggettoAllegato();
//		for(DatiSoggettoAllegato datiSoggetto : aa.getDatiSoggettiAllegati()) {
//			if(datiSoggetto.getSoggetto() != null && datiSoggetto.getSoggetto().getUid() == 156520) {
//				datiSogg = datiSoggetto;
//			}
//		}
//		for (ElencoDocumentiAllegato elencoDocumentiAllegato : aa.getElenchiDocumentiAllegato()) {
//			boolean isNonConvalidabile = isNonConvalidabileElenco(elencoDocumentiAllegato);
//			StringBuilder sb = new StringBuilder()
//						.append("elenco uid [ ")
//						.append(elencoDocumentiAllegato.getUid())
//						.append("] risoluta essere convalidabile? ")
//						.append(!isNonConvalidabile)
//						.append(" .");
//			log.debug(methodName, sb.toString());
//			if(elencoDocumentiAllegato.getUid() == 12064) {
//				log.logXmlTypeObject(elencoDocumentiAllegato, "ELENCO_DOCU");
//				RicercaDettaglioElencoResponse resDettaglioElenco = ottieniResponseRicercaDettaglioElenco();
//				ElencoDocumentiAllegato elenco = resDettaglioElenco.getElencoDocumentiAllegato();
//				List<Subdocumento<?, ?>> subdocumenti = elenco.getSubdocumenti();
//				for (Subdocumento<?, ?> subdocumento : subdocumenti) {
//					boolean convalidato = subdocumento.getFlagConvalidaManuale() != null;
//					log.debug(methodName, "subdocumento con uid " + subdocumento.getUid()  + " convalidato? " + convalidato + "il flag convalida manuale risulta essere " + (convalidato? subdocumento.getFlagConvalidaManuale() : "null")); 
//				}
//			}
//			
//		}
	}
	
		
	private boolean isNonConvalidabileElenco(ElencoDocumentiAllegato eda) {
		// Stato operativo non COMPLETATO
		return !StatoOperativoElencoDocumenti.COMPLETATO.equals(eda.getStatoOperativoElencoDocumenti())
				// Importo spesa e importo entrata nulli
				|| (eda.getTotaleDaConvalidareSpesaNotNull().signum() == 0 && eda.getTotaleDaConvalidareEntrataNotNull().signum() == 0);
	}
	
	@Test
	public void ricercaDettaglioElenco() {
		final String methodName = "ricercaDettaglioElenco";
		RicercaDettaglioElencoResponse resDettaglioElenco = ottieniResponseRicercaDettaglioElenco();
		ElencoDocumentiAllegato elenco = resDettaglioElenco.getElencoDocumentiAllegato();
		List<Subdocumento<?, ?>> subdocumenti = elenco.getSubdocumenti();
		for (Subdocumento<?, ?> subdocumento : subdocumenti) {
			boolean convalidato = subdocumento.getFlagConvalidaManuale() != null;
			log.debug(methodName, "subdocumento con uid " + subdocumento.getUid()  + " convalidato? " + convalidato + "il flag convalida manuale risulta essere " + (convalidato? subdocumento.getFlagConvalidaManuale() : "null")); 
		}
	}

	/**
	 * @return
	 */
	private RicercaDettaglioElencoResponse ottieniResponseRicercaDettaglioElenco() {
		RicercaDettaglioElenco req = new RicercaDettaglioElenco();
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setElencoDocumentiAllegato(create(ElencoDocumentiAllegato.class, 12064));
		RicercaDettaglioElencoResponse res = ricercaDettaglioElencoService.executeService(req);
		return res;
	}
	
	
	@Test
	public void ricercaAllegatoAtto() {
		RicercaAllegatoAtto req = new RicercaAllegatoAtto();
		req.setParametriPaginazione(getParametriPaginazione(0, 10));
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setListaAttoAmministrativo(new ArrayList<AttoAmministrativo>());
		
		AllegatoAtto allegatoAtto = new AllegatoAtto();
		allegatoAtto.setEnte(req.getRichiedente().getAccount().getEnte());
		allegatoAtto.setHasImpegnoConfermaDurc(Boolean.FALSE);
//		allegatoAtto.setStatoOperativoAllegatoAtto(StatoOperativoAllegatoAtto.DA_COMPLETARE);
//		req.setStatiOperativiFiltri(Arrays.asList(StatoOperativoAllegatoAtto.PARZIALMENTE_CONVALIDATO, StatoOperativoAllegatoAtto.RIFIUTATO, StatoOperativoAllegatoAtto.CONVALIDATO));
		// Causale
//		allegatoAtto.setCausale("mi"); // Errore: la causale deve avere almeno tre caratteri
//		allegatoAtto.setCausale("per");
		// Stato
//		allegatoAtto.setStatoOperativoAllegatoAtto(StatoOperativoAllegatoAtto.DA_COMPLETARE);
		// Atto Amministrativo
//		allegatoAtto.setAttoAmministrativo(create(AttoAmministrativo.class, 44011));
//		allegatoAtto.setAttoAmministrativo(create(AttoAmministrativo.class, 528));
		// Soggetto
//		DatiSoggettoAllegato datiSoggettoAllegato = new DatiSoggettoAllegato();
//		Soggetto soggetto = new Soggetto();
//		soggetto.setUid(3);
//		datiSoggettoAllegato.setSoggetto(soggetto);
//		allegatoAtto.getDatiSoggettiAllegati().add(datiSoggettoAllegato);
//		allegatoAtto.setUid(179);
		req.setAllegatoAtto(allegatoAtto);
		
		req.setAllegatoAttoModelDetails(
				AllegatoAttoModelDetail.IsAssociatoAdAlmenoUnaQuotaSpesa,
				AllegatoAttoModelDetail.IsAssociatoAdUnDocumento,
				AllegatoAttoModelDetail.IsAssociatoAdUnSubdocumentoSospeso,
				AllegatoAttoModelDetail.IsAssociatoAdUnSubdocumentoConOrdinativo,
				AllegatoAttoModelDetail.HasImpegnoConfermaDurcDataFineValidita);
		
//		req.setStatiOperativiFiltri(Arrays.asList(StatoOperativoAllegatoAtto.COMPLETATO,StatoOperativoAllegatoAtto.PARZIALMENTE_CONVALIDATO));
		
//		req.setSoggetto(create(Soggetto.class,137876));
		// Data scadenza
		//Calendar cal = Calendar.getInstance();
		//cal.set(2014, 9, 31);
		//Date dataScadenzaDa = cal.getTime();
		//cal.set(2014, 10, 1);
		//Date dataScadenzaA = cal.getTime();
		//req.setDataScadenzaDa(dataScadenzaDa);
		//req.setDataScadenzaA(dataScadenzaA);
		
		// Elenco
//		ElencoDocumentiAllegato elencoDocumentiAllegato = new ElencoDocumentiAllegato();
//		elencoDocumentiAllegato.setUid(11622);
//		req.setElencoDocumentiAllegato(elencoDocumentiAllegato);
		
//		Soggetto soggetto = new Soggetto();
//		soggetto.setUid(3);
//		req.setSoggetto(soggetto);
//
//		Impegno imp = new Impegno();
//		imp.setUid(1);
//		req.setImpegno(imp);
		
		// SIAC-6166
		req.setBilancio(getBilancioTest(133, 2018));
		
		//req.setAllegatoAttoModelDetails(AllegatoAttoModelDetail.IsAssociatoAdUnSubdocumentoConOrdinativo);
		
		RicercaAllegatoAttoResponse res = ricercaAllegatoAttoService.executeService(req);
		assertNotNull(res);
	}
	
	
	
	@Test
	public void inserisciTantiElenchiService(){
		
//		inserisciElenco(19,20,21,109);
//		inserisciElenco(12,16,18);
//		inserisciElenco(54,56);
//		inserisciElenco(53,55,57);
//		inserisciElenco(115,116,117);
//		inserisciElenco(173,174,175);
//		inserisciElenco(200,201,202);
		inserisciElenco(47126);
	}
	
	
	
	private void inserisciElenco(int... idSubdocs){
		InserisceElenco req = new InserisceElenco();
		
		req.setRichiedente(getRichiedenteByProperties("consip", "crp"));
		req.setBilancio(getBilancioTest(143, 2017));
		
		ElencoDocumentiAllegato e = new ElencoDocumentiAllegato();
		
//		AllegatoAtto allegatoAtto = new AllegatoAtto();
//		allegatoAtto.setEnte(getEnteTest());
//		allegatoAtto.setUid(2);
//		AttoAmministrativo attoAmministrativo = new AttoAmministrativo();
//		attoAmministrativo.setUid(6);
//		allegatoAtto.setAttoAmministrativo(attoAmministrativo);
//		e.setAllegatoAtto(allegatoAtto);
		
//		e.setSysEsterno("Sono uno da sistema esterno!!");
//		e.setAnnoSysEsterno(2015);
//		
		e.setAnno(2015);
		e.setNumero(1);
//		e.setDataTrasmissione(new Date());
		e.setEnte(getEnteTest());
		
		e.setAllegatoAtto(create(AllegatoAtto.class, 9613));
		
		List<Subdocumento<?, ?>> subdocumenti = new ArrayList<Subdocumento<?, ?>>();
		
		for(int id : idSubdocs){
			SubdocumentoSpesa s1 = new SubdocumentoSpesa();
			s1.setUid(id);
			subdocumenti.add(s1);
		}
		
		e.setSubdocumenti(subdocumenti);
		e.setStatoOperativoElencoDocumenti(StatoOperativoElencoDocumenti.BOZZA);
		
		
		req.setElencoDocumentiAllegato(e);
		
		InserisceElencoResponse res = inserisceElencoService.executeService(req);
		assertNotNull(res);
	}
	
	@Test
	public void inserisceElencoService() {
		InserisceElenco req = new InserisceElenco();
	
		req.setRichiedente(getRichiedenteByProperties("consip","regp")); 
		req.setBilancio(getBilancio2015Test());
		
		ElencoDocumentiAllegato e = new ElencoDocumentiAllegato();
		
		
		
//		AllegatoAtto allegatoAtto = new AllegatoAtto();
//		allegatoAtto.setEnte(getEnteTest());
//		allegatoAtto.setUid(2);
//		AttoAmministrativo attoAmministrativo = new AttoAmministrativo();
//		attoAmministrativo.setUid(6);
//		allegatoAtto.setAttoAmministrativo(attoAmministrativo);
//		e.setAllegatoAtto(allegatoAtto);
		
		e.setSysEsterno("Sono uno da sistema esterno!!");
		e.setAnnoSysEsterno(2015);
		
		e.setAnno(2015);
		e.setNumero(160);
		e.setDataTrasmissione(new Date());
		e.setEnte(getEnteTest());
		
		
		List<Subdocumento<?, ?>> subdocumenti = new ArrayList<Subdocumento<?, ?>>();
		
		
		SubdocumentoSpesa s1 = new SubdocumentoSpesa();
		s1.setUid(28);
		subdocumenti.add(s1);
		
		SubdocumentoSpesa s2 = new SubdocumentoSpesa();
		s2.setUid(29);
		subdocumenti.add(s2);
		
		SubdocumentoSpesa s3 = new SubdocumentoSpesa();
		s3.setUid(30);
		subdocumenti.add(s3);
		
		e.setSubdocumenti(subdocumenti);
		e.setStatoOperativoElencoDocumenti(StatoOperativoElencoDocumenti.BOZZA);
		
		
		req.setElencoDocumentiAllegato(e);
		
		InserisceElencoResponse res = inserisceElencoService.executeService(req);
		assertNotNull(res);
	}
	
	@Test
	public void inserisciElencoConDocumenti() {
		InserisceElenco req = new InserisceElenco();
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setBilancio(getBilancioTest(131, 2017));
		
		ElencoDocumentiAllegato elencoDocumentiAllegato = new ElencoDocumentiAllegato();
		req.setElencoDocumentiAllegato(elencoDocumentiAllegato);
		
		elencoDocumentiAllegato.setAllegatoAtto(create(AllegatoAtto.class, 11523));
		elencoDocumentiAllegato.setAnno(Integer.valueOf(2017));
		elencoDocumentiAllegato.setEnte(req.getRichiedente().getAccount().getEnte());
		elencoDocumentiAllegato.setStatoOperativoElencoDocumenti(StatoOperativoElencoDocumenti.BOZZA);
		
		SubdocumentoSpesa ss = new SubdocumentoSpesa();
		elencoDocumentiAllegato.getSubdocumenti().add(ss);
		
		ss.setImporto(new BigDecimal("4"));
		ss.setImportoDaDedurre(BigDecimal.ZERO);
		ss.setCig("");
		ss.setCup("A77A77777777777");
		ss.setPagatoInCEC(Boolean.FALSE);
		
		ss.setSiopeTipoDebito(create(SiopeTipoDebito.class, 2));
		ss.setSubImpegno(create(SubImpegno.class, 0));
		ss.setModalitaPagamentoSoggetto(create(ModalitaPagamentoSoggetto.class, 138167));
		ss.setImpegno(create(Impegno.class, 65199));
		ss.getImpegno().setSoggetto(create(Soggetto.class, 127284));
		
		InserisceElencoResponse res = inserisceElencoConDocumentiConQuoteService.executeService(req);
		assertNotNull(res);
	}
	
	@Test
	public void inserisciElenco2(){

		String reqStr = "<inserisceElenco>" +
		"    <dataOra>2014-10-15T17:38:26.138+02:00</dataOra>" +
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
		"    <bilancio>" +
		"        <stato>VALIDO</stato>" +
		"        <uid>1</uid>" +
		"        <anno>2013</anno>" +
		"    </bilancio>" +
		"    <elencoDocumentiAllegato>" +
		"        <stato>VALIDO</stato>" +
		"        <uid>0</uid>" +
		"        <subdocumentoSpesa>" +
		"            <stato>VALIDO</stato>" +
		"            <uid>13</uid>" +
		"            <importo>0</importo>" +
		"            <importoDaDedurre>0</importoDaDedurre>" +
		"        </subdocumentoSpesa>" +
		"        <allegatoAtto>" +
		"            <stato>VALIDO</stato>" +
		"            <uid>4</uid>" +
		"        </allegatoAtto>" +
		"        <anno>2013</anno>" +
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
		"        <statoOperativoElencoDocumenti>BOZZA</statoOperativoElencoDocumenti>" +
		"    </elencoDocumentiAllegato>" +
		"</inserisceElenco>";
		
		InserisceElenco req = JAXBUtility.unmarshall(reqStr, InserisceElenco.class);

		InserisceElencoResponse res = inserisceElencoService.executeService(req);
		assertNotNull(res);

	}
	
	@Test
	public void ricercaElenco() {
		RicercaElenco request = new RicercaElenco();
		request.setRichiedente(getRichiedenteByProperties("consip","regp"));
		request.setDataOra(new Date());
		request.setParametriPaginazione(getParametriPaginazioneTest());
		
		ElencoDocumentiAllegato elencoDocumentiAllegato = new ElencoDocumentiAllegato();
		elencoDocumentiAllegato.setEnte(getEnteTest());
		elencoDocumentiAllegato.setNumero(2);
		request.setElencoDocumentiAllegato(elencoDocumentiAllegato);
		
		request.setStatiOperativiElencoDocumenti(Arrays.asList(StatoOperativoElencoDocumenti.BOZZA));
		
		RicercaElencoResponse response = ricercaElencoService.executeService(request);
		assertNotNull(response);
	}
	
	@Test
	public void associaElenco() {
		AssociaElenco req = new AssociaElenco();
		req.setBilancio(getBilancio2015Test());
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		
		ElencoDocumentiAllegato elencoDocumentiAllegato = new ElencoDocumentiAllegato();
		elencoDocumentiAllegato.setUid(211);
		elencoDocumentiAllegato.setEnte(getEnteTest());
		
		AllegatoAtto allegatoAtto = new AllegatoAtto();
		allegatoAtto.setUid(28);
		elencoDocumentiAllegato.setAllegatoAtto(allegatoAtto);
		req.setElencoDocumentiAllegato(elencoDocumentiAllegato);
		
		AssociaElencoResponse res = associaElencoService.executeService(req);
		assertNotNull(res);
	}
	
	@Test
	public void disassociaElenco() {
		DisassociaElenco req = new DisassociaElenco();
		req.setBilancio(getBilancio2014Test());
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		
		ElencoDocumentiAllegato elencoDocumentiAllegato = new ElencoDocumentiAllegato();
		elencoDocumentiAllegato.setUid(87);
		elencoDocumentiAllegato.setEnte(getEnteTest());
		
		AllegatoAtto allegatoAtto = new AllegatoAtto();
		allegatoAtto.setUid(36);
		elencoDocumentiAllegato.setAllegatoAtto(allegatoAtto);
		req.setElencoDocumentiAllegato(elencoDocumentiAllegato);
		
		DisassociaElencoResponse res = disassociaElencoService.executeService(req);
		assertNotNull(res);
	}
	
	@Test
	public void disassociaQuotaDaElenco() {
		EliminaQuotaDaElenco req = new EliminaQuotaDaElenco();
		req.setBilancio(getBilancio2015Test());
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		
		ElencoDocumentiAllegato elencoDocumentiAllegato = new ElencoDocumentiAllegato();
		elencoDocumentiAllegato.setUid(233);
		elencoDocumentiAllegato.setEnte(getEnteTest());
		req.setElencoDocumentiAllegato(elencoDocumentiAllegato);
		
		Subdocumento<Documento<?,?>, SubdocumentoIva<?,?,?>> sub = new Subdocumento<Documento<?,?>, SubdocumentoIva<?,?,?>>();
		sub.setUid(1149);
		req.setSubdocumento(sub);
		EliminaQuotaDaElencoResponse res = eliminaQuotaDaElencoService.executeService(req);
		assertNotNull(res);
	}
	
	@Test
	public void trovaRelazione(){
		String methodName = "trovaRelazione";
		Integer idElenco = 47;
		Integer idSubdoc = 434;
		List<SiacRElencoDocSubdoc> listaRelazioni = siacTElencoDocRepository.findSiacRElencoDocSubdocsByEldocIdAndSubdocId(idElenco, idSubdoc);
		if(listaRelazioni == null){
			log.debug(methodName, "LISTA NULL");
		}else if(listaRelazioni.isEmpty()){
			log.debug(methodName, "LISTA VUOTA");
		}else{
			for(SiacRElencoDocSubdoc siacRElencoDocSubdoc : listaRelazioni){
				log.debug(methodName, "trovata relazione con uid " + siacRElencoDocSubdoc.getUid() + 
						" tra subdc con uid: " + siacRElencoDocSubdoc.getSiacTSubdoc().getUid() + 
						" e elenco con uid: " + siacRElencoDocSubdoc.getSiacTElencoDoc().getUid() );
			}
		}
		
	}
	
	
	
	@Test
	@Rollback(true)
	public void trovaRelazioneDad(){
		ElencoDocumentiAllegato elencoDocumentiAllegato = new ElencoDocumentiAllegato();
		elencoDocumentiAllegato.setUid(47);
		Subdocumento<?, ?> subdocumento = new Subdocumento<Documento<?,?>, SubdocumentoIva<?,?,?>>();
		subdocumento.setUid(434);
		elencoDocumentiAllegatoDad.disassociaQuotaDaElenco(elencoDocumentiAllegato, subdocumento);
		
	}

	
	@Test	
	public void xstQuoteSenzaLiquidazioneByElencoId(){
		final String methodName = "xstQuoteSenzaLiquidazioneByElencoId";
		boolean xstQuoteSenzaLiquidazioneByElencoId = elencoDocumentiAllegatoDad.xstQuoteSenzaLiquidazioneByElencoId(12061); 	
		log.info(methodName, "xstQuoteSenzaLiquidazioneByElencoId ?" + xstQuoteSenzaLiquidazioneByElencoId);
	}
	
	
	@Test
	public void aggiornaDatiSoggettoAllegatoAtto() {
		AggiornaDatiSoggettoAllegatoAtto req = new AggiornaDatiSoggettoAllegatoAtto();
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		
		DatiSoggettoAllegato datiSoggettoAllegato = new DatiSoggettoAllegato();
		
		AllegatoAtto allegatoAtto = new AllegatoAtto();
		allegatoAtto.setUid(3);
		datiSoggettoAllegato.setAllegatoAtto(allegatoAtto);
		
		Soggetto soggetto = new Soggetto();
		soggetto.setUid(6538);
		datiSoggettoAllegato.setSoggetto(soggetto);
		
		datiSoggettoAllegato.setEnte(getEnteTest());
		
		datiSoggettoAllegato.setCausaleSospensione("Nessuna vera, solo test");
		datiSoggettoAllegato.setDataSospensione(new Date());
//		datiSoggettoAllegato.setUid(1);
		req.setDatiSoggettoAllegato(datiSoggettoAllegato);
		
		AggiornaDatiSoggettoAllegatoAttoResponse res = aggiornaDatiSoggettoAllegatoAttoService.executeService(req);
		assertNotNull(res);
	}
	
	@Test
	public void completaAllegatoAtto(){

		
		CompletaAllegatoAtto reqCAA = new CompletaAllegatoAtto();
		reqCAA.setDataOra(new Date());
		reqCAA.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		reqCAA.setBilancio(getBilancioTest(131, 2017));
		AllegatoAtto allegato = new AllegatoAtto();
		allegato.setUid(11353);
		reqCAA.setAllegatoAtto(allegato);

		String methodName="completaAllegatoAtto";
//		log.info(methodName, "contatore " + allegatoAttoDad.countSubdocumentiSpesaRilevantiIvaNonCollegatiALiquidazioneConImportoDaPagareMaggioreDiZero(allegato));
//		log.info(methodName, "ricerca "+ allegatoAttoDad.findSubdocumentiSpesaRilevantiIvaNonCollegatiALiquidazioneConImportoDaPagareMaggioreDiZeroNonSospesi(allegato).size());
		CompletaAllegatoAttoResponse res = completaAllegatoAttoService.executeService(reqCAA); //excecutService(reqCAA);
	}
	
	@Test
	public void completaAllegatoAttoAsync() {
		CompletaAllegatoAtto req = new CompletaAllegatoAtto();
		
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setBilancio(getBilancioTest(131, 2017));
		req.setAnnoBilancio(req.getBilancio().getAnno());
		req.setAllegatoAtto(create(AllegatoAtto.class, 11248));
		//req.setAllegatoAtto(create(AllegatoAtto.class, 11245));
		req.getAllegatoAtto().setEnte(req.getRichiedente().getAccount().getEnte());
		
		AsyncServiceRequestWrapper<CompletaAllegatoAtto> wrapper = new AsyncServiceRequestWrapper<CompletaAllegatoAtto>();
		wrapper.setAccount(req.getRichiedente().getAccount());
		wrapper.setAnnoBilancio(req.getAnnoBilancio());
		wrapper.setDataOra(req.getDataOra());
		wrapper.setEnte(req.getRichiedente().getAccount().getEnte());
		wrapper.setRequest(req);
		wrapper.setRichiedente(req.getRichiedente());
		
		wrapper.setAzioneRichiesta(create(AzioneRichiesta.class, 0));
		wrapper.getAzioneRichiesta().setAzione(create(Azione.class, 4676));
		
		AsyncServiceResponse res = completaAllegatoAttoAsyncService.executeService(wrapper);
		assertNotNull(res);
	}
	
	@Test
	public void convalidaPerElenchiAllegatoAtto(){
		ConvalidaAllegatoAttoPerElenchi req = new ConvalidaAllegatoAttoPerElenchi();
		req.setRichiedente(getRichiedenteByProperties("consip", "crp"));
		
		AllegatoAtto allegatoAtto = new AllegatoAtto();
		allegatoAtto.setUid(9618);
		allegatoAtto.setEnte(getEnteTest());
		
		ElencoDocumentiAllegato elencoDoc = new ElencoDocumentiAllegato();
		elencoDoc.setUid(9992);
		List<Subdocumento<?, ?>> subdocumenti = new ArrayList<Subdocumento<?,?>>();
		SubdocumentoSpesa subdoc= new SubdocumentoSpesa();
		subdoc.setUid(72058);
		subdoc.setFlagConvalidaManuale(Boolean.TRUE);
		subdocumenti.add(subdoc);
		elencoDoc.setSubdocumenti(subdocumenti);
		List<ElencoDocumentiAllegato> elenchiDocumentiAllegato = new ArrayList<ElencoDocumentiAllegato>();
		elenchiDocumentiAllegato.add(elencoDoc);
		allegatoAtto.setElenchiDocumentiAllegato(elenchiDocumentiAllegato);
		
		req.setAllegatoAtto(allegatoAtto);
		
		ConvalidaAllegatoAttoPerElenchiResponse res = convalidaAllegatoAttoPerElenchiService.executeService(req);
		assertNotNull(res);
	}
	@Test
	public void associaElencoAhmad() {
		AssociaElenco req = new AssociaElenco();
		req.setBilancio(getBilancio2015Test());
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		
		ElencoDocumentiAllegato elencoDocumentiAllegato = new ElencoDocumentiAllegato();
		elencoDocumentiAllegato.setUid(217);
		elencoDocumentiAllegato.setEnte(getEnteTest());
		elencoDocumentiAllegato.setDataTrasmissione(new Date());
		AllegatoAtto allegatoAtto = new AllegatoAtto();
		allegatoAtto.setUid(165);
		elencoDocumentiAllegato.setAllegatoAtto(allegatoAtto);
		req.setElencoDocumentiAllegato(elencoDocumentiAllegato);
		
		AssociaElencoResponse res = associaElencoService.executeService(req);
		assertNotNull(res);
	}
	
	@Test
	public void inserisciElenchiAhmad() {
		inserisciElenco(109,109,109);
	}
	@Test
	public void riCompletaPerElenchiAllegatoAtto(){
		RiCompletaAllegatoAttoPerElenchi req = new RiCompletaAllegatoAttoPerElenchi();
		
		req.setRichiedente(getRichiedenteByProperties("consip","coal"));
		req.setAnnoBilancio(Integer.valueOf(2018));
		req.setDataOra(new Date());
		
		req.setAllegatoAtto(create(AllegatoAtto.class, 16887));
		req.getAllegatoAtto().setEnte(req.getRichiedente().getAccount().getEnte());
		
		req.getAllegatoAtto().getElenchiDocumentiAllegato().add(create(ElencoDocumentiAllegato.class, 17989));
		req.getAllegatoAtto().getElenchiDocumentiAllegato().add(create(ElencoDocumentiAllegato.class, 17990));
		req.getAllegatoAtto().getElenchiDocumentiAllegato().add(create(ElencoDocumentiAllegato.class, 17991));
		
		
		
		RiCompletaAllegatoAttoPerElenchiResponse res = riCompletaAllegatoAttoPerElenchiService.executeService(req);
		assertNotNull(res);
	}
	
	@Test
	public void rifiutaElenchi(){
		RifiutaElenchi req = new RifiutaElenchi();
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		req.setBilancio(getBilancio2014Test());
		
		AllegatoAtto allegatoAtto = new AllegatoAtto();
		allegatoAtto.setUid(57);
		allegatoAtto.setEnte(getEnteTest());
		
		ElencoDocumentiAllegato elencoDoc = new ElencoDocumentiAllegato();
		elencoDoc.setUid(82);
		List<Subdocumento<?, ?>> subdocumenti = new ArrayList<Subdocumento<?,?>>();
		SubdocumentoSpesa subdoc= new SubdocumentoSpesa();
		subdoc.setUid(494);
		subdoc.setFlagConvalidaManuale(Boolean.FALSE);
		subdocumenti.add(subdoc);
		elencoDoc.setSubdocumenti(subdocumenti);
		List<ElencoDocumentiAllegato> elenchiDocumentiAllegato = new ArrayList<ElencoDocumentiAllegato>();
		elenchiDocumentiAllegato.add(elencoDoc);
		allegatoAtto.setElenchiDocumentiAllegato(elenchiDocumentiAllegato);
		
		req.setAllegatoAtto(allegatoAtto);
		
		RifiutaElenchiResponse res = rifiutaElenchiService.executeService(req);
		assertNotNull(res);
	}
	
	@Test
	public void convalidaPerElenchiAllegatoAttoAsync(){
		final String methodName = "convalidaPerElenchiAllegatoAttoAsync";
		ConvalidaAllegatoAttoPerElenchi req = new ConvalidaAllegatoAttoPerElenchi();
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		
		AllegatoAtto allegatoAtto = new AllegatoAtto();
		allegatoAtto.setUid(37);
		allegatoAtto.setEnte(getEnteTest());
		
		ElencoDocumentiAllegato elencoDoc = new ElencoDocumentiAllegato();
		elencoDoc.setUid(46);
		List<Subdocumento<?, ?>> subdocumenti = new ArrayList<Subdocumento<?,?>>();
		SubdocumentoSpesa subdoc= new SubdocumentoSpesa();
		subdoc.setUid(433);
		subdoc.setFlagConvalidaManuale(Boolean.FALSE);
		subdocumenti.add(subdoc);
		elencoDoc.setSubdocumenti(subdocumenti);
		List<ElencoDocumentiAllegato> elenchiDocumentiAllegato = new ArrayList<ElencoDocumentiAllegato>();
		elenchiDocumentiAllegato.add(elencoDoc);
		allegatoAtto.setElenchiDocumentiAllegato(elenchiDocumentiAllegato );
		
		req.setAllegatoAtto(allegatoAtto);
		
		AsyncServiceRequestWrapper<ConvalidaAllegatoAttoPerElenchi> reqAsync = new AsyncServiceRequestWrapper<ConvalidaAllegatoAttoPerElenchi>();
		reqAsync.setRequest(req);
		
		reqAsync.setEnte(getEnteTest());
		Account account = new Account();
		account.setUid(1);
		reqAsync.setAccount(account);
		AzioneRichiesta azioneRichiesta = new AzioneRichiesta();
		azioneRichiesta.setUid(66024200);
		Azione azione = new Azione();
		azione.setUid(58);
		azioneRichiesta.setAzione(azione);
		reqAsync.setAzioneRichiesta(azioneRichiesta);
		
		AsyncServiceResponse res = convalidaAllegatoAttoPerElenchiAsyncService.executeService(reqAsync);
		assertNotNull(res);
		
		log.info(methodName, ">>>>>>>>>>>>>>>>>>> Mi metto in attesa per 20 secondi...");
		
		try {
			Thread.sleep(20000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			log.info(methodName, ">>>>>>>>>>>>>>>> sleep finito!");
		}
	}
	
	@Test
	public void checkSubordinatiPerConvalida(){
		
		ElencoDocumentiAllegato elencoDocumentiAllegato = new ElencoDocumentiAllegato();
		List<Subdocumento<?, ?>> subdocumenti = new ArrayList<Subdocumento<?,?>>();
//		Subdocumento<?, ?> subdocumento1 = new Subdocumento();
//		subdocumento1.setUid(362);
//		subdocumenti.add(subdocumento1);
//		Subdocumento<?, ?> subdocumento2 = new Subdocumento();
//		subdocumento2.setUid(363);
//		subdocumenti.add(subdocumento2);
		Subdocumento<?, ?> subdocumento3 = new SubdocumentoSpesa();
		subdocumento3.setUid(364);
		subdocumenti.add(subdocumento3);
		Subdocumento<?, ?> subdocumento4 = new SubdocumentoSpesa();
		subdocumento4.setUid(366);
		subdocumenti.add(subdocumento4);
		elencoDocumentiAllegato.setSubdocumenti(subdocumenti);
		List<Subdocumento<?,?>> listaSubdoc = elencoDocumentiAllegatoDad.findSubdocDocSubordinati(subdocumenti);
		
		for(Subdocumento<?,?> subdocSubordinato : listaSubdoc){
			boolean subdocContenuto = false;
			for(Subdocumento<?,?> subdocElenco : elencoDocumentiAllegato.getSubdocumenti()){
				if(subdocSubordinato.getUid() == subdocElenco.getUid()){
					subdocContenuto = true;
					break;
				}
			}
			if(!subdocContenuto){
				log.debug("checkSubordinatiPerConvalida", "nell'elenco manca il subdoc: " + subdocSubordinato.getUid());
			}else{
				log.debug("checkSubordinatiPerConvalida", "Il subdoc: " + subdocSubordinato.getUid() + " è presente nell'elenco");
			}
		}
		
	}
	
	
	@Test
	public void annullaAllegatoAtto(){

//		String sb = "<annullaAllegatoAtto>" +
//		"    <dataOra>2014-10-29T15:32:58.613+01:00</dataOra>" +
//		"    <richiedente>" +
//		"        <account>" +
//		"            <stato>VALIDO</stato>" +
//		"            <uid>1</uid>" +
//		"            <nome>Demo 21</nome>" +
//		"            <descrizione>Demo 21 - Città di Torino</descrizione>" +
//		"            <indirizzoMail>email</indirizzoMail>" +
//		"            <ente>" +
//		"                <stato>VALIDO</stato>" +
//		"                <uid>1</uid>" +
//		"                <gestioneLivelli>" +
//		"                    <entry>" +
//		"                        <key>LIVELLO_GESTIONE_BILANCIO</key>" +
//		"                        <value>GESTIONE_UEB</value>" +
//		"                    </entry>" +
//		"                </gestioneLivelli>" +
//		"                <nome>Città di Torino</nome>" +
//		"            </ente>" +
//		"        </account>" +
//		"        <operatore>" +
//		"            <stato>VALIDO</stato>" +
//		"            <uid>0</uid>" +
//		"            <codiceFiscale>AAAAAA00A11B000J</codiceFiscale>" +
//		"            <cognome>AAAAAA00A11B000J</cognome>" +
//		"            <nome>Demo</nome>" +
//		"        </operatore>" +
//		"    </richiedente>" +
//		"    <allegatoAtto>" +
//		"        <stato>VALIDO</stato>" +
//		"        <uid>27</uid>" +
//		"        <ente>" +
//		"            <stato>VALIDO</stato>" +
//		"            <uid>1</uid>" +
//		"            <gestioneLivelli>" +
//		"                <entry>" +
//		"                    <key>LIVELLO_GESTIONE_BILANCIO</key>" +
//		"                    <value>GESTIONE_UEB</value>" +
//		"                </entry>" +
//		"            </gestioneLivelli>" +
//		"            <nome>Città di Torino</nome>" +
//		"        </ente>" +
//		"    </allegatoAtto>" +
//		"</annullaAllegatoAtto>";
				
		AnnullaAllegatoAtto req = new AnnullaAllegatoAtto();
		AllegatoAtto allegatoAtto = new AllegatoAtto();
		allegatoAtto.setUid(47);
		allegatoAtto.setEnte(getEnteTest());
		req.setAllegatoAtto(allegatoAtto);
		req.setBilancio(getBilancio2014Test());
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		
		AnnullaAllegatoAttoResponse res = annullaAllegatoAttoService.executeService(req);
		assertNotNull(res);
				
	}
	
	@Test
	public void stampaAllegatoAtto() {
		StampaAllegatoAtto req = new StampaAllegatoAtto();
		
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setEnte(req.getRichiedente().getAccount().getEnte());
		req.setBilancio(getBilancioTest(131, 2017));
		req.setAnnoEsercizio(req.getBilancio().getAnno());
		
		req.setAllegatoAtto(create(AllegatoAtto.class, 11349)); 
		
		StampaAllegatoAttoResponse res = stampaAllegatoAttoService.executeService(req);
		assertNotNull(res);
	}
	
	@Test
	public void inviaAllegatoAtto() {
		InviaAllegatoAtto req = new InviaAllegatoAtto();
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		
		AllegatoAtto allegatoAtto = new AllegatoAtto();
		allegatoAtto.setUid(199);
		req.setAllegatoAtto(allegatoAtto);
		//req.setEnte(getEnteTest());
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		req.setAnnoEsercizio(getBilancio2015Test().getAnno());
		req.setBilancio(getBilancio2015Test());
		
		
		inviaAllegatoAttoService.executeService(req);
		
	}
//									@Autowired
//	private InviaAllegatoAttoAsyncService inviaAllegatoAttoAsyncService;
//	@Test
//	public void inviaAllegatoAttoAsync() {
//		StringBuilder sb = new StringBuilder();
//		sb.append("<asyncServiceRequestWrapper>");
//		sb.append("    <dataOra>2016-04-13T10:04:26.249+02:00</dataOra>");
//		sb.append("   <richiedente>");
//		sb.append("        <account>");
//		sb.append(" <stato>VALIDO</stato>");
//		sb.append("           <uid>1</uid>");
//		sb.append("            <nome>Demo 21</nome>");
//		sb.append("            <descrizione>Demo 21 - Città di Torino</descrizione>");
//		sb.append("            <indirizzoMail>email</indirizzoMail>");
//		sb.append("            <ente>");
//		sb.append("                <stato>VALIDO</stato>");
//		sb.append("                <uid>1</uid>");
//		sb.append("                <gestioneLivelli>");
//		sb.append("                    <entry>");
//		sb.append("                        <key>LIVELLO_GESTIONE_BILANCIO</key>");
//		sb.append("                        <value>GESTIONE_UEB</value>");
//		sb.append("                    </entry>");
//		sb.append("                    <entry>");
//		sb.append("                        <key>CARICA_MISSIONE_DA_ESTERNO</key>");
//		sb.append("                        <value>CARICA_MISSIONE_DA_ESTERNO_ATTIVA</value>");
//		sb.append("                    </entry>");
//		sb.append("                   <entry>");
//		sb.append("                        <key>INTEGRAZIONE_HR</key>");
//		sb.append("                        <value>INTEGRAZIONE_HR_ATTIVA</value>");
//		sb.append("                    </entry>");
//		sb.append("                </gestioneLivelli>");
//		sb.append("                <nome>Città di Torino</nome>");
//		sb.append("            </ente>");
//		sb.append("        </account>");
//		sb.append("        <operatore>");
//		sb.append("            <stato>VALIDO</stato>");
//		sb.append("            <uid>0</uid>");
//		sb.append("            <codiceFiscale>AAAAAA00A11B000J</codiceFiscale>");
//		sb.append("            <cognome>AAAAAA00A11B000J</cognome>");
//		sb.append("            <nome>Demo</nome>");
//		sb.append("        </operatore>");
//		sb.append("    </richiedente>");
//		sb.append("    <account>");
//		sb.append("        <stato>VALIDO</stato>");
//		sb.append("        <uid>1</uid>");
//		sb.append("        <nome>Demo 21</nome>");
//		sb.append("        <descrizione>Demo 21 - Città di Torino</descrizione>");
//		sb.append("        <indirizzoMail>email</indirizzoMail>");
//		sb.append("        <ente>");
//		sb.append("            <stato>VALIDO</stato>");
//		sb.append("            <uid>1</uid>");
//		sb.append("            <gestioneLivelli>");
//		sb.append("                <entry>");
//		sb.append("                    <key>LIVELLO_GESTIONE_BILANCIO</key>");
//		sb.append("                    <value>GESTIONE_UEB</value>");
//		sb.append("                </entry>");
//		sb.append("                <entry>");
//		sb.append("                    <key>CARICA_MISSIONE_DA_ESTERNO</key>");
//		sb.append("                    <value>CARICA_MISSIONE_DA_ESTERNO_ATTIVA</value>");
//		sb.append("                </entry>");
//		sb.append("                <entry>");
//		sb.append("                    <key>INTEGRAZIONE_HR</key>");
//		sb.append("                    <value>INTEGRAZIONE_HR_ATTIVA</value>");
//		sb.append("                </entry>");
//		sb.append("            </gestioneLivelli>");
//		sb.append("            <nome>Città di Torino</nome>");
//		sb.append("        </ente>");
//		sb.append("    </account>");
//		sb.append("    <azioneRichiesta>");
//		sb.append("        <stato>VALIDO</stato>");
//		sb.append("        <uid>0</uid>");
//		sb.append("        <azione>");
//		sb.append("            <stato>VALIDO</stato>");
//		sb.append("            <uid>6201</uid>");
//		sb.append("            <nome>OP-COM-inviaAttoAllegato</nome>");
//		sb.append("            <titolo>Invia Allegato Atto</titolo>");
//		sb.append("            <tipo>AZIONE_SECONDARIA</tipo>");
//		sb.append("            <urlApplicazione>/../siacbilapp/azioneRichiesta.do</urlApplicazione>");
//		sb.append("            <flagVerificaSac>false</flagVerificaSac>");
//		sb.append("        </azione>");
//		sb.append("        <daCruscotto>false</daCruscotto>");
//		sb.append("    </azioneRichiesta>");
//		sb.append("    <ente>");
//		sb.append("        <stato>VALIDO</stato>");
//		sb.append("        <uid>1</uid>");
//		sb.append("<gestioneLivelli>");
//		sb.append("            <entry>");
//		sb.append("                <key>LIVELLO_GESTIONE_BILANCIO</key>");
//		sb.append("                <value>GESTIONE_UEB</value>");
//		sb.append("            </entry>");
//		sb.append("            <entry>");
//		sb.append("                <key>CARICA_MISSIONE_DA_ESTERNO</key>");
//		sb.append("                <value>CARICA_MISSIONE_DA_ESTERNO_ATTIVA</value>");
//		sb.append("            </entry>");
//		sb.append("            <entry>");
//		sb.append("                <key>INTEGRAZIONE_HR</key>");
//		sb.append("                <value>INTEGRAZIONE_HR_ATTIVA</value>");
//		sb.append("            </entry>");
//		sb.append("        </gestioneLivelli>");
//		sb.append("        <nome>Città di Torino</nome>");
//		sb.append("    </ente>");
//		sb.append("    <request>");
//		sb.append("        <dataOra>2016-04-13T10:04:26.249+02:00</dataOra>");
//		sb.append("        <richiedente>");
//		sb.append("            <account>");
//		sb.append("                <stato>VALIDO</stato>");
//		sb.append("                <uid>1</uid>");
//		sb.append("                <nome>Demo 21</nome>");
//		sb.append("                <descrizione>Demo 21 - Città di Torino</descrizione>");
//		sb.append("                <indirizzoMail>email</indirizzoMail>");
//		sb.append("                <ente>");
//		sb.append("                    <stato>VALIDO</stato>");
//		sb.append("                    <uid>1</uid>");
//		sb.append("                    <gestioneLivelli>");
//		sb.append("                       <entry>");
//		sb.append("                            <key>LIVELLO_GESTIONE_BILANCIO</key>");
//		sb.append("                            <value>GESTIONE_UEB</value>");
//		sb.append("                        </entry>");
//		sb.append("                        <entry>");
//		sb.append("                           <key>CARICA_MISSIONE_DA_ESTERNO</key>");
//		sb.append("                           <value>CARICA_MISSIONE_DA_ESTERNO_ATTIVA</value>");
//		sb.append("                       </entry>");
//		sb.append("                        <entry>");
//		sb.append("                            <key>INTEGRAZIONE_HR</key>");
//		sb.append("                            <value>INTEGRAZIONE_HR_ATTIVA</value>");
//		sb.append("                        </entry>");
//		sb.append("                    </gestioneLivelli>");
//		sb.append("                    <nome>Città di Torino</nome>");
//		sb.append("                </ente>");
//		sb.append("            </account>");
//		sb.append("            <operatore>");
//		sb.append("                <stato>VALIDO</stato>");
//		sb.append("                <uid>0</uid>");
//		sb.append("                <codiceFiscale>AAAAAA00A11B000J</codiceFiscale>");
//		sb.append("                <cognome>AAAAAA00A11B000J</cognome>");
//		sb.append("                <nome>Demo</nome>");
//		sb.append("            </operatore>");
//		sb.append("       </richiedente>");
//		sb.append("   </request>");
//		sb.append("</asyncServiceRequestWrapper>");
//		AsyncServiceRequestWrapper req = JAXBUtility.unmarshall(sb.toString(), AsyncServiceRequestWrapper.class);
//		InviaAllegatoAtto req = new InviaAllegatoAtto();
//		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
//		req.setDataOra(new Date());//
//		req.setRichiedente(getRichiedenteByProperties("consip","regp"));//
//		req.setAnnoEsercizio(getBilancio2015Test().getAnno());//
//		req.setBilancio(getBilancio2015Test());//
//		AllegatoAtto allegatoAtto = new AllegatoAtto();
//		allegatoAtto.setUid(199);
//		AttoAmministrativo amm= new AttoAmministrativo();
//		amm.setNumero(86);
//		amm.setAnno(2015);
//		allegatoAtto.setAttoAmministrativo(amm);
//		req.setAllegatoAtto(allegatoAtto);
//		//req.setEnte(getEnteTest());
//		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
//		req.setAnnoEsercizio(getBilancio2015Test().getAnno());
//		req.setBilancio(getBilancio2015Test());
//		
//		
//		InviaAllegatoAttoResponse res = inviaAllegatoAttoService.inviaAllegatoAttoAsync(req);
//		
//	}
	
	@Test
	public void stampaAllegatoAttoAsync() {
		StampaAllegatoAtto req = new StampaAllegatoAtto();
		req.setDataOra(new Date());//
		req.setEnte(getEnteTest());//
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));//
		req.setAnnoEsercizio(getBilancio2015Test().getAnno());//
		req.setBilancio(getBilancio2015Test());//
		
		AllegatoAtto allegatoAtto = new AllegatoAtto();
		allegatoAtto.setUid(199);
		req.setAllegatoAtto(allegatoAtto);
		
		StampaAllegatoAttoResponse res = stampaAllegatoAttoAsyncService.executeService(req);
		assertNotNull(res);
		
		try {
			Thread.sleep(2 * 60 * 1000);
		} catch (InterruptedException e) {
			log.error("stampaAllegatoAtto", "InterruptedException during test", e);
			throw new RuntimeException("Test failure", e);
		}
	}
	
	@Test
	public void ricercaStampaAllegatoAtto() {
		
		RicercaSinteticaStampaAllegatoAtto req = new RicercaSinteticaStampaAllegatoAtto();		
		AllegatoAttoStampa stampa = new AllegatoAttoStampa();

		AllegatoAtto aa = new AllegatoAtto();
		AttoAmministrativo atto = new AttoAmministrativo();
		atto.setAnno(2015);
		atto.setNumero(50005);
		atto.setUid(37728);
		aa.setAttoAmministrativo(atto);
		stampa.setAllegatoAtto(aa);

		/*Calendar c = new GregorianCalendar();
		c.set(2016, 0, 13);
		stampa.setDataCreazione(c.getTime());*/

		
		stampa.setEnte(getEnteTest());
		
//		
		stampa.setTipoStampa(TipoStampaAllegatoAtto.ALLEGATO);
		stampa.setAnnoEsercizio(getBilancio2015Test().getAnno());		
		stampa.setBilancio(getBilancio2014Test());

		req.setAllegatoAttoStampa(stampa);
//

		req.setDataOra(new Date());
		req.setEnte(getEnteTest());
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
//		
		req.setParametriPaginazione(getParametriPaginazioneTest());

		RicercaSinteticaStampaAllegatoAttoResponse res = ricercaSinteticaStampaAllegatoAttoService.executeService(req);
		assertNotNull(res.getListaAllegatoAttoStampa());
		
	}
	
	@Test
	public void inserisceStampaAllegatoAtto() {
		 InserisceStampaAllegatoAtto req = new InserisceStampaAllegatoAtto();
		 
		 AllegatoAttoStampa stampa= new AllegatoAttoStampa();
		 AllegatoAtto al = new AllegatoAtto();
		 AttoAmministrativo am = new AttoAmministrativo();
		 am.setUid(79);
		 File file = new File();
		 file.setUid(798); //uid a caso presente su DB
		 al.setUid(29);
		 al.setAttoAmministrativo(am);//uid a caso presente su DB
		 stampa.setAllegatoAtto(al);
		 stampa.setTipoStampa(TipoStampaAllegatoAtto.ALLEGATO);
		 stampa.setAnnoEsercizio(getBilancio2015Test().getAnno());
		 stampa.setFiles(Arrays.asList(file));
		 stampa.setVersioneInvioFirma(0);
		 stampa.setCodice("codice");
		 stampa.setEnte(getEnteTest());
		 stampa.setBilancio(getBilancio2015Test());
		 
		 req.setAllegatoAttoStampa(stampa);
		 req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		 req.setDataOra(new Date());
		//	req.setEnte(getEnteTest());
		 
		InserisceStampaAllegatoAttoResponse response = inserisceStampaAllegatoAttoService.executeService(req);			
		assertNotNull(response);
	}

	@Test
	public void stampaDettaglioOperazione() {
		StringBuilder sb = new StringBuilder();
		AllegatoAtto temp = allegatoAttoDad.findAllegatoAttoById(199);
		if (temp == null) {
			throw new BusinessException(ErroreCore.ENTITA_INESISTENTE.getErrore("allegato atto", "uid 199" ));
		}
		sb.append("Elaborazione Invio per Firma ");
		sb.append("Atto ");
		AllegatoAtto aa = temp;

		sb.append((aa.getAttoAmministrativo() != null && aa.getAttoAmministrativo().getAnno() != 0) ? aa.getAttoAmministrativo().getAnno() : " ");
		sb.append("/");
		sb.append((aa.getAttoAmministrativo() != null && aa.getAttoAmministrativo().getNumero() != 0) ? aa.getAttoAmministrativo().getNumero() : " ");
		sb.append("-");
		sb.append(aa.getVersioneInvioFirmaNotNull());
		log.debug("stampaDettaglioOperazione", sb.toString());
	}

	 protected Ente getEnteForn2() {
		return getEnteTest(5);
	}
	
	protected Bilancio getBilancioForn2(){
		Bilancio bilancio = new Bilancio();
		bilancio.setUid(51);
		bilancio.setAnno(2016);
		return bilancio;
	}
	
	
	
	
	@Test
	public void testElaborazioniDad(){
		elaborazioniDad.setEnte(getEnteTest());
		elaborazioniDad.setLoginOperazione("blablalba");
		
		try {
			elaborazioniDad.startElaborazione("test1", "testKey");
		} catch (ElaborazioneAttivaException e) {
			e.printStackTrace();
		}
		try {
			elaborazioniDad.startElaborazione("test1", "testKey");
		} catch (ElaborazioneAttivaException e) {
			e.printStackTrace();
		}
		elaborazioniDad.endElaborazione("test1", "testKey");
		
	}
	
	/**
	 * RicercaSinteticaQuoteElenco
	 */
	@Test
	public void ricercaSinteticaQuoteElenco() {
		final String methodName = "ricercaSinteticaQuoteElenco";
		RicercaSinteticaQuoteElenco req = new RicercaSinteticaQuoteElenco();
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip","edisu"));
		
		ElencoDocumentiAllegato elencoDocumentiAllegato = new ElencoDocumentiAllegato();
		//elencoDocumentiAllegato.setUid(355);
		elencoDocumentiAllegato.setUid(9333);
		req.setElencoDocumentiAllegato(elencoDocumentiAllegato);

		List<Subdocumento<?, ?>> subdocs = new ArrayList<Subdocumento<?,?>>();
		for(int i = 0; i < 3; i++) {
			req.setParametriPaginazione(new ParametriPaginazione(i, 10));
			
			RicercaSinteticaQuoteElencoResponse res = ricercaSinteticaQuoteElencoService.executeService(req);
			assertNotNull(res);
			subdocs.addAll(res.getSubdocumenti());
		}
		
		for(Subdocumento<?, ?> s : subdocs) {
			log.warn(methodName, s.getUid() + " - " + s.getDocumento().getAnno() + "/" + s.getDocumento().getNumero() + "/" + s.getNumero());
		}
		
		
	}
	
	@Test
	public void aggiornaMassivaDatiSoggettoAllegatoAtto() {
		AggiornaMassivaDatiSoggettoAllegatoAtto req = new AggiornaMassivaDatiSoggettoAllegatoAtto();
		
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("dev", "ente1"));
		req.setElencoDocumentiAllegato(create(ElencoDocumentiAllegato.class, 0));
		req.setAllegatoAtto(create(AllegatoAtto.class, 222));
		
		DatiSoggettoAllegato dsa = new DatiSoggettoAllegato();
		dsa.setDataSospensione(parseDate("31/07/2017"));
		dsa.setCausaleSospensione("Prova via JIRA");
		dsa.setDataRiattivazione(parseDate("01/08/2017"));
		req.setDatiSoggettoAllegato(dsa);
		
		AggiornaMassivaDatiSoggettoAllegatoAttoResponse res = aggiornaMassivaDatiSoggettoAllegatoAttoService.executeService(req);
		assertNotNull(res);
	}
	
	@Autowired RicercaDatiSospensioneAllegatoAttoService ricercaDatiSospensioneAllegatoAttoService;
	
	@Test
	public void testDatiSospensioneAllegato() {
		 
		RicercaDatiSospensioneAllegatoAtto req = new RicercaDatiSospensioneAllegatoAtto();
		req.setRichiedente(getRichiedenteByProperties("dev", "ente1"));
		AllegatoAtto aa = new AllegatoAtto();
		aa.setUid(204);
		
		ElencoDocumentiAllegato ead = new ElencoDocumentiAllegato();
		ead.setUid(0);

		req.setAllegatoAtto(aa);
		req.setElencoDocumentiAllegato(ead);
		
		RicercaDatiSospensioneAllegatoAttoResponse res = ricercaDatiSospensioneAllegatoAttoService.executeService(req);

	}
	
	@Test
	public void completaAllegatoAttoMultiplo() {
		CompletaAllegatoAttoMultiplo req = new CompletaAllegatoAttoMultiplo();
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setBilancio(getBilancioTest(131, 2017));
		Integer[] uids = new Integer[] {
				Integer.valueOf(11297)
				};
		req.setUidsAllegatiAtto(Arrays.asList(uids));
		
		RicercaAllegatoAtto reqInterna = new RicercaAllegatoAtto();
		reqInterna.setDataOra(new Date());
		reqInterna.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		
		AllegatoAtto aa = new AllegatoAtto();
		aa.setStatoOperativoAllegatoAtto(StatoOperativoAllegatoAtto.DA_COMPLETARE);
		AttoAmministrativo atto = new AttoAmministrativo();
		atto.setAnno(2017);
		atto.setUid(44040);
//		atto.setNumero();
		aa.setAttoAmministrativo(atto);
		aa.setEnte(req.getRichiedente().getAccount().getEnte());
		reqInterna.setAllegatoAtto(aa);
		
		List<AttoAmministrativo> uidAtti = new ArrayList<AttoAmministrativo>();
		uidAtti.add(atto);
		reqInterna.setListaAttoAmministrativo(uidAtti);
		
//		req.setRicercaAllegatoAtto(reqInterna);
		CompletaAllegatoAttoMultiploResponse res = completaAllegatoAttoMultiploService.executeService(req);
		
		assertNotNull(res);
	}
	
	@Test
	public void completaAllegatoAttoMultiploAsync() {
		final String methodName = "completaAllegatoAttoMultiploAsync";
		CompletaAllegatoAttoMultiplo req = new CompletaAllegatoAttoMultiplo();
		
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setBilancio(getBilancioTest(131, 2017));
		req.setAnnoBilancio(req.getBilancio().getAnno());
		Integer[] uids = new Integer[] {
				Integer.valueOf(11298)
				};
		req.setUidsAllegatiAtto(Arrays.asList(uids));
		//req.setAllegatoAtto(create(AllegatoAtto.class, 11245));
		
		AsyncServiceRequestWrapper<CompletaAllegatoAttoMultiplo> wrapper = new AsyncServiceRequestWrapper<CompletaAllegatoAttoMultiplo>();
		wrapper.setAccount(req.getRichiedente().getAccount());
		wrapper.setAnnoBilancio(req.getAnnoBilancio());
		wrapper.setDataOra(req.getDataOra());
		wrapper.setEnte(req.getRichiedente().getAccount().getEnte());
		wrapper.setRequest(req);
		wrapper.setRichiedente(req.getRichiedente());
		
		wrapper.setAzioneRichiesta(create(AzioneRichiesta.class, 0));
		wrapper.getAzioneRichiesta().setAzione(create(Azione.class, 13025));
		
		AsyncServiceResponse res = completaAllegatoAttoMultiploAsyncService.executeService(wrapper);
		assertNotNull(res);
		
		try {
			Thread.sleep(10*60*1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			log.info(methodName, ">>>>>>>>>>>>>>>> sleep finito!");
		}
	}	

	@Test
	public void convalidaAllegatoAttoMultiplo() {
		ConvalidaAllegatoAttoPerElenchiMultiplo req = new ConvalidaAllegatoAttoPerElenchiMultiplo();
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		Integer[] uids = new Integer[] {
				Integer.valueOf(11289)
				};
		req.setUidsAllegatiAtto(Arrays.asList(uids));
		req.setFlagConvalidaManuale(Boolean.TRUE);
		
		RicercaAllegatoAtto reqInterna = new RicercaAllegatoAtto();
		reqInterna.setDataOra(new Date());
		reqInterna.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		AllegatoAtto aa = new AllegatoAtto();
		aa.setStatoOperativoAllegatoAtto(StatoOperativoAllegatoAtto.DA_COMPLETARE);
		AttoAmministrativo atto = new AttoAmministrativo();
		atto.setAnno(2017);
		atto.setUid(44040);
//		atto.setNumero();
		aa.setAttoAmministrativo(atto);
		aa.setEnte(req.getRichiedente().getAccount().getEnte());
		List<AttoAmministrativo> uidAtti = new ArrayList<AttoAmministrativo>();
		uidAtti.add(atto);
		reqInterna.setListaAttoAmministrativo(uidAtti);
		reqInterna.setAllegatoAtto(aa);
		
		ConvalidaAllegatoAttoPerElenchiMultiploResponse response = convalidaAllegatoAttoPerElenchiMultiploService.executeService(req);
		assertNotNull(response);
	}
	
	@Test
	public void convalidaAllegatoAttoMultiploAsync() {
		final String methodName = "convalidaAllegatoAttoMultiploAsync";
		ConvalidaAllegatoAttoPerElenchiMultiplo req = new ConvalidaAllegatoAttoPerElenchiMultiplo();
		
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setAnnoBilancio(getBilancioTest(131, 2017).getAnno());
		req.setFlagConvalidaManuale(Boolean.TRUE);
		Integer[] uids = new Integer[] {
				Integer.valueOf(11294)
				};
		req.setUidsAllegatiAtto(Arrays.asList(uids));
		//req.setAllegatoAtto(create(AllegatoAtto.class, 11245));
		
		AsyncServiceRequestWrapper<ConvalidaAllegatoAttoPerElenchiMultiplo> wrapper = new AsyncServiceRequestWrapper<ConvalidaAllegatoAttoPerElenchiMultiplo>();
		wrapper.setAccount(req.getRichiedente().getAccount());
		wrapper.setAnnoBilancio(req.getAnnoBilancio());
		wrapper.setDataOra(req.getDataOra());
		wrapper.setEnte(req.getRichiedente().getAccount().getEnte());
		wrapper.setRequest(req);
		wrapper.setRichiedente(req.getRichiedente());
		
		wrapper.setAzioneRichiesta(create(AzioneRichiesta.class, 0));
		wrapper.getAzioneRichiesta().setAzione(create(Azione.class, 13025));
		
		AsyncServiceResponse res = convalidaAllegatoAttoPerElenchiMultiploAsyncService.executeService(wrapper);
		assertNotNull(res);
		
		try {
			Thread.sleep(10*60*1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			log.info(methodName, ">>>>>>>>>>>>>>>> sleep finito!");
		}
	}
	
	@Test
	public void testCaricaAssenzaMotivazioneSiope() {
		final String methodName = "testCaricaAssenzaMotivazioneSiope";
		SiopeAssenzaMotivazione siopeAssenzaMotivazione = impegnoBilDad.getSiopeAssenzaMotivazione(64686);
		log.debug(methodName, "trovata siope assenza motivazione " + (siopeAssenzaMotivazione != null? (" codice " + siopeAssenzaMotivazione.getCodice()) : "null"));
	}
	
	@Test
	public void testCaricaAssenzaMotivazioneSiopePerSub() {
		final String methodName = "testCaricaAssenzaMotivazioneSiopePerSub";
		SiopeAssenzaMotivazione siopeAssenzaMotivazione = subImpegnoBilDad.getSiopeAssenzaMotivazione(67227);
		log.debug(methodName, "trovata siope assenza motivazione " + (siopeAssenzaMotivazione != null? (" codice " + siopeAssenzaMotivazione.getCodice()) : "null"));
	}
	
	
	@Test
	public void getSubDocIdsWithBooleanAttrCodeAndValueByEldocIdsss() {
		siacTElencoDocRepository.calcolaTotaleDaConvalidare(27740, Arrays.asList(SiacDDocFamTipoEnum.Spesa.getCodice(), SiacDDocFamTipoEnum.IvaSpesa.getCodice()));
	}
	
	@Test
	public void getSubDocIdsWithBooleanAttrCodeAndValueByEldocId() {
		List<Integer> uids = siacTElencoDocRepository.getSubDocIdsWihtMovgestWithBooleanAttrCodeAndValueByEldocId(27740, SiacTAttrEnum.FlagSoggettoDurc.getCodice(), "S");
		for (Integer uid : uids) {
			System.out.println("count:" + uid);
		}
	
	}
	
	@Test
	public void testGetDataFineValiditaDurcCessione() {
		final String methodName = "testGetDataFineValiditaDurcCessione";
		Integer uidElenco = Integer.valueOf(11620);
		List<Integer> uids = siacTElencoDocRepository.getSubDocIdsWihtMovgestWithBooleanAttrCodeAndValueByEldocId(uidElenco, SiacTAttrEnum.FlagSoggettoDurc.getCodice(), "S");
		if(uids == null || uids.isEmpty()) {
			log.debug(methodName, "non vi sono subdocumenti collegati ad impegno con flag durc = true. esco.");
			return;
		}
		List<Date> data = siacTElencoDocRepository.getDataFineValiditaDurcByEldocIdSubdocIds(uidElenco, uids);
		if(data == null || data.isEmpty()) {
			log.debug(methodName, "Non vi sono subdocumenti che richiedono conferma durc legati con data scadenza durc. esco.");
			return;
		}
		Date dataPiuProssima = Utility.getDataPiuProssima(data);
		System.out.println("data:" + data);
		System.out.println("dataPiuProssima:" + dataPiuProssima);
	}
	
	@Test
	public void testContaImpegniConFlagDurcAllegato() {
		Long count = siacTAttoAllegatoRepository.countImpegniWithBooleanAttrCodeAndValueByAttoalId(528, SiacTAttrEnum.FlagSoggettoDurc.getCodice(), "S");
		System.out.println("count:" + count);
	}
	
	@Test
	public void testGetDataFineValiditaDurcAllegato() {
		List<Date> data = siacTAttoAllegatoRepository.getDataFineValiditaDurcByAttoalId(528);
		Date dataPiuProssima = Utility.getDataPiuProssima(data);
		System.out.println("data:" + data);
		System.out.println("dataPiuProssima:" + dataPiuProssima);
	}
	
	@Autowired SiacTSubdocRepository siacTSubdocRepository;
	@Test
	public void testConversioneDatiDurc() {
		final String methodName = "testConversioneDatiDurc";
		List<Integer> uids = siacTAttoAllegatoRepository.getUidsSubdocWithImpegniWithBooleanAttrCodeAndValueByAttoalId(11351, SiacTAttrEnum.FlagSoggettoDurc.getCodice(), "S");
		if(uids == null || uids.isEmpty()) {
			log.debug(methodName, "non vi sono subdocumenti collegati ad impegno con flag durc = true. esco.");
		}else {
			StringBuilder uidstring = new StringBuilder();
			for(Integer uid : uids){
				uidstring.append(uid).append(" , ");
			}
			
			log.debug(methodName, "uids: " + uidstring);
			List<Date> data = siacTSubdocRepository.getDataFineValiditaDurcBySubdocIds(uids);
			if(data == null || data.isEmpty()) {
				log.debug(methodName, "Non vi sono subdocumenti che richiedono conferma durc legati con data scadenza durc. esco.");
			}
			Date datas = data.get(0);
			System.out.println("data fine validita durc:" + datas);
		}
	}
	
	@Autowired SubdocumentoSpesaDad subdocumentoSpesaDad;
	@Test
	public void testCheckDatiDurc() {
		int uidAllegatoAtto = 24885;
		List<Integer> uidsSubdocConConfermaDurc = allegatoAttoDad.getUidsSubdocWithImpegnoConfermaDurc(create(AllegatoAtto.class, uidAllegatoAtto));
		if(uidsSubdocConConfermaDurc == null || uidsSubdocConConfermaDurc == null) {
			System.out.println("nessun subdocumento collegato ad un impegno con richeista conferma durc");
			return;
		}
		Map<String, Date> mappaSoggettoData = subdocumentoSpesaDad.getDataFineValiditaDurcAndSoggettoCodePiuRecenteBySubdocIds(uidsSubdocConConfermaDurc);
		Date now = new Date();
		
		//SIAC-7143
		String dateNow = new SimpleDateFormat("yyyy-MM-dd").format(now);
		String dateFineDurc = null;
		
		for (String soggettoCode : mappaSoggettoData.keySet()) {
			Date dataFineValiditaDurc = mappaSoggettoData.get(soggettoCode);
			//SIAC-7143
			dateFineDurc = new SimpleDateFormat("yyyy-MM-dd").format(dataFineValiditaDurc);		
			if(dataFineValiditaDurc == null  || dateNow.compareTo(dateFineDurc) > 0){
				throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("Il soggetto " + soggettoCode + " presenta dati Durc non validi."));
			}
		}
		System.out.println("Check Superato");
		
		assertTrue(dateNow.compareTo(dateFineDurc) > 0 == false);
		assertNotNull(mappaSoggettoData);
	}
	
	@Test
	public void testCaricamentoDati() {
		subdocumentoSpesaDad.setEnte(create(Ente.class, 2));
		subdocumentoSpesaDad.ricercaSubdocumentiSpesaPerProvvisorio(
				create(TipoDocumento.class, 72),
				Integer.valueOf(2020),
				"28/C",
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				getParametriPaginazioneTest()
				);
	}
	
	
	@Test
	public void controlloImportiImpegniVincolati() {
		ControlloImportiImpegniVincolati controlloImportiImpegniVincolati = new ControlloImportiImpegniVincolati();
		controlloImportiImpegniVincolati.setDataOra(new Date());
		controlloImportiImpegniVincolati.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		controlloImportiImpegniVincolati.setAnnoBilancio(2017);
		controlloImportiImpegniVincolati.setListaAllegatoAttoId(Arrays.asList(Integer.valueOf(24572)));
		ControlloImportiImpegniVincolatiResponse res = controlloImportiImpegniVincolatiService.executeService(controlloImportiImpegniVincolati);
		assertNotNull(res);
		
	}
	
	@Test
	public void controlloImportiVincolati() {
		Integer[] uids = new Integer[] {
				Integer.valueOf(24582),
				Integer.valueOf(24583),
				Integer.valueOf(24584),
				Integer.valueOf(24585),
				Integer.valueOf(13696)
				
		};
		for (Integer integer : uids) {
			List<Messaggio> controlloImportiImpegniVincolati = allegatoAttoDad.controlloImportiImpegniVincolati(Arrays.asList(integer));
			System.out.println("Sto valutando l'atto allegato: " + integer.toString());
			for (Messaggio messaggio : controlloImportiImpegniVincolati) {
				System.out.println(messaggio.getDescrizione());
			}
		}
		
	}
	
	
	
}
