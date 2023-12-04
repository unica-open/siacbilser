/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.movimentogestione;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.integration.dad.ModificaMovimentoGestioneBilDad;
import it.csi.siac.siacbilser.model.ImportiCapitoloEnum;
import it.csi.siac.siacbilser.model.MacrotipoComponenteImportiCapitolo;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;
import it.csi.siac.siaccommon.util.JAXBUtility;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.Messaggio;
import it.csi.siac.siacfinser.business.service.movgest.AnnullaMovimentoSpesaService;
import it.csi.siac.siacfinser.business.service.movgest.InserisceImpegniService;
import it.csi.siac.siacfinser.business.service.movgest.RicercaAccertamentoPerChiaveOttimizzatoService;
import it.csi.siac.siacfinser.business.service.movgest.RicercaImpegnoPerChiaveOttimizzatoService;
import it.csi.siac.siacfinser.business.service.movgest.RicercaSinteticaImpegniSubimpegniService;
import it.csi.siac.siacfinser.frontend.webservice.msg.AnnullaMovimentoSpesa;
import it.csi.siac.siacfinser.frontend.webservice.msg.AnnullaMovimentoSpesaResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.DatiOpzionaliCapitoli;
import it.csi.siac.siacfinser.frontend.webservice.msg.DatiOpzionaliElencoSubTuttiConSoloGliIds;
import it.csi.siac.siacfinser.frontend.webservice.msg.InserisceImpegni;
import it.csi.siac.siacfinser.frontend.webservice.msg.InserisceImpegniResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentoPerChiaveOttimizzatoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzatoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSinteticaImpegniSubImpegni;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSinteticaImpegniSubimpegniResponse;
import it.csi.siac.siacfinser.integration.dao.movgest.AccertamentoDao;
import it.csi.siac.siacfinser.integration.dao.movgest.SiacTCronopElemFinRepository;
import it.csi.siac.siacfinser.integration.entity.SiacTCronopElemFin;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneEntrata;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneSpesa;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaImpSub;
import it.csi.siac.siacfinser.model.ric.RicercaAccertamentoK;
import it.csi.siac.siacfinser.model.ric.RicercaImpegnoK;

// TODO: Auto-generated Javadoc
/**
 * The Class GruppoAttivitaIvaVTTest.
 */
public class MovimentoGestioneTest extends BaseJunit4TestCase {
	
	@Autowired private SiacTCronopElemFinRepository siacTCronopElemFinRepository;
	@Autowired private InserisceImpegniService inserisceImpegniService;
	@Autowired private RicercaAccertamentoPerChiaveOttimizzatoService ricercaAccertamentoPerChiaveOttimizzatoService;
	@Autowired private RicercaImpegnoPerChiaveOttimizzatoService ricercaImpegnoPerChiaveOttimizzatoService;
	@Autowired private RicercaSinteticaImpegniSubimpegniService ricercaSinteticaImpegniSubimpegniService;
	@Autowired private AnnullaMovimentoSpesaService annullaMovimentoSpesaService;
	/**
	 * Inserisci gruppo attivita.*/
	@Test
	public void testRicercaAccertamentoPerChiaveOttimizzato() {
		
		Bilancio bilancio = getBilancioTest(131, 2017);
		
		RicercaAccertamentoPerChiaveOttimizzato reqRAPCO = new RicercaAccertamentoPerChiaveOttimizzato();
		reqRAPCO.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		reqRAPCO.setEnte(reqRAPCO.getRichiedente().getAccount().getEnte());
		
		DatiOpzionaliElencoSubTuttiConSoloGliIds parametriElencoIds = new DatiOpzionaliElencoSubTuttiConSoloGliIds();
		parametriElencoIds.setEscludiAnnullati(true);
		
		DatiOpzionaliCapitoli datiOpzionaliCapitoli = new DatiOpzionaliCapitoli();
		datiOpzionaliCapitoli.setImportiDerivatiRichiesti(EnumSet.noneOf(ImportiCapitoloEnum.class));
		
		RicercaAccertamentoK pRicercaAccertamentoK = new RicercaAccertamentoK();
		pRicercaAccertamentoK.setAnnoEsercizio(bilancio.getAnno());
		pRicercaAccertamentoK.setAnnoAccertamento(2017);
		pRicercaAccertamentoK.setNumeroAccertamento(new BigDecimal(1));
		
		reqRAPCO.setpRicercaAccertamentoK(pRicercaAccertamentoK);
		reqRAPCO.setDatiOpzionaliElencoSubTuttiConSoloGliIds(parametriElencoIds);
		reqRAPCO.setDatiOpzionaliCapitoli(datiOpzionaliCapitoli);
		reqRAPCO.setEscludiSubAnnullati(true);
		reqRAPCO.setCaricaFlagPresenteStoricizzazioneNelBilancio(true);
		
		RicercaAccertamentoPerChiaveOttimizzatoResponse response = ricercaAccertamentoPerChiaveOttimizzatoService.executeService(reqRAPCO);
		assertNotNull(response);
	}
	
	@Test
	public void testSiacTCronopElemFinRepo() {
		String methodName = "testSiacTCronopElemFinRepo";
		List<SiacTCronopElemFin> listCronopElem = siacTCronopElemFinRepository.findSiacTCronopElemByProgettoIdAndTipoCapitolo(2, "CAP-UG", 2);

		assertNotNull(listCronopElem);

		for (SiacTCronopElemFin siacTCronopElemFin : listCronopElem) {
			log.debug(methodName, siacTCronopElemFin.toString());
			assertNotNull(siacTCronopElemFin.getUid());
			log.debug("[[[ THE UID ]]] =========================>>>>>>>>>>>>>>>>>>>>>>>>>>>>> ", siacTCronopElemFin.toString());			
		}
		
	
	}
	
	@Test
	public void testInserisciImpegnoPerCronoprogramma() {
		
		String xml = "";
		
		InserisceImpegni serviceRequest = JAXBUtility.unmarshall(xml, InserisceImpegni.class);
		
		InserisceImpegniResponse response = inserisceImpegniService.executeService(serviceRequest);
		
		assertNotNull(response);
		assertTrue(Esito.SUCCESSO.equals(response.getEsito()));
		assertTrue(response.getElencoImpegniInseriti().size() > 0);
		assertTrue(response.getElencoImpegniInseriti().get(0).getIdCronoprogramma() != null);
	}
	
	@Test
	public void testRicercaImpegno() {
		//VALORI DA PASSARE A RICHIESTA
		String numImpegno = "4628";
		String annoImp = "2020";
		String annoEs = "2020";
		String enteUid = "2";
		///////////////////////////////
		
		
		//istanzio valori dell'impegno da cercare (valori da passare dalla ricerca)
		RicercaImpegnoPerChiaveOttimizzato parametroRicercaPerChiave = new RicercaImpegnoPerChiaveOttimizzato();
		RicercaImpegnoK impegnoDaCercare = new RicercaImpegnoK();
		BigDecimal numeroImpegno = new BigDecimal(numImpegno);
		
		impegnoDaCercare.setAnnoEsercizio(Integer.valueOf(annoEs));
		impegnoDaCercare.setNumeroImpegno(numeroImpegno);
		impegnoDaCercare.setAnnoImpegno(new Integer(annoImp));
		Ente enteProva = getEnteTest(new Integer(enteUid));
			
		parametroRicercaPerChiave.setRichiedente(getRichiedenteTest("AAAAAA00A11C000K", 52, 2));
		parametroRicercaPerChiave.setEnte(enteProva);
		parametroRicercaPerChiave.setpRicercaImpegnoK(impegnoDaCercare);
		
		//PER SIAC-5785 - serve caricare anche il cig dato che viene controllato nei controlli SIOPE PLUS:
		DatiOpzionaliElencoSubTuttiConSoloGliIds datiOpzionaliSubs = new DatiOpzionaliElencoSubTuttiConSoloGliIds();
		//serve caricare anche il cig dato che viene controllato nei controlli SIOPE PLUS:
		datiOpzionaliSubs.setCaricaCig(true);
		//il cup viene gratis assieme al cig (sono attr entrambi vedi funzonamento servizio)
		//quindi tanto vale farlo caricare:
		datiOpzionaliSubs.setCaricaCup(true);
		parametroRicercaPerChiave.setDatiOpzionaliElencoSubTuttiConSoloGliIds(datiOpzionaliSubs);
		parametroRicercaPerChiave.setCaricaSub(false);
		
		
		RicercaImpegnoPerChiaveOttimizzatoResponse response = ricercaImpegnoPerChiaveOttimizzatoService.executeService(parametroRicercaPerChiave);
		
		assertNotNull(response);
		assertTrue(Esito.SUCCESSO.equals(response.getEsito()));
		assertTrue(response.getImpegno().getProgetto().getCronoprogrammi().size() > 0);
		assertTrue(response.getImpegno().getIdCronoprogramma() != null);
		
		
	}
	
	@Test
	public void ricercaSinteticaImpegniSubImpegniTEST() {
		
		String methodName = "ricercaSinteticaImpegniSubImpegniTEST";

		RicercaSinteticaImpegniSubImpegni request = new RicercaSinteticaImpegniSubImpegni();
		request.setNumPagina(1);
		request.setNumRisultatiPerPagina(100);
		request.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		request.setEnte(request.getRichiedente().getAccount().getEnte());
		
		ParametroRicercaImpSub pr = new ParametroRicercaImpSub();
		pr.setAnnoEsercizio(Integer.valueOf(2019));
		
		/* ***PROGETTO*** */
		pr.setCodiceProgetto("mas-2019 01");
		pr.setProgetto("mas-2019 01");
//		pr.setUidCronoprogramma(869);
		
		/* ***PROVVEDIMENTO*** */
//		pr.setAnnoProvvedimento(Integer.valueOf(2017));
//		pr.setNumeroProvvedimento(Integer.valueOf(11));
		//pr.setTipoProvvedimento(impostaEntitaFacoltativa(attoAmministrativo.getTipoAtto()));
		
		request.setParametroRicercaImpSub(pr);
		
		RicercaSinteticaImpegniSubimpegniResponse response = ricercaSinteticaImpegniSubimpegniService.executeService(request);

		assertNotNull(response);
		assertTrue(response.getListaImpegni().size() > 0);
		
		for (Impegno imp : response.getListaImpegni()) {
			log.debug(methodName, imp.getAnnoMovimento() + "/" + imp.getNumeroBigDecimal());
		}
		
		System.out.println("--- TEST FINITO ---");
	}
	
	/**
	 * Test
	 */
	@Test
	public void annullaMovimentoSpesa() {
		
		AnnullaMovimentoSpesa req = new AnnullaMovimentoSpesa();
		
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setEnte(req.getRichiedente().getAccount().getEnte());
		
		req.setBilancio(create(Bilancio.class, 133));
		req.getBilancio().setAnno(2018);
		
		Impegno impegno = create(Impegno.class, 120614);
		
		List<ModificaMovimentoGestioneSpesa> listaModifiche = new ArrayList<ModificaMovimentoGestioneSpesa>();
		ModificaMovimentoGestioneSpesa modDaAnnullare = new ModificaMovimentoGestioneSpesa();
		modDaAnnullare.setUid(29478);
		listaModifiche.add(modDaAnnullare);
		
		impegno.setListaModificheMovimentoGestioneSpesa(listaModifiche);
		
		req.setImpegno(impegno);
		
		AnnullaMovimentoSpesaResponse response = annullaMovimentoSpesaService.executeService(req);
		assertNotNull(response);
	}
	
	@Autowired ModificaMovimentoGestioneBilDad modificaMovimentoGestioneBilDad;
	
	@Test
	public void getSoggettoModifica() {
		modificaMovimentoGestioneBilDad.setEnte(create(Ente.class,2));
		modificaMovimentoGestioneBilDad.estraiSoggettoDellaModifica(create(ModificaMovimentoGestioneEntrata.class, 4));
	}	
	
	@Autowired private AccertamentoDao accertamentoDao;
	
	@Test
	public void testVincoliImpliciti() {
		accertamentoDao.ricercaSiacTMovgestTsFinInVincoloImplicitosulCapitolo(180653, 
				Arrays.asList(MacrotipoComponenteImportiCapitolo.FPV.getCodice(),MacrotipoComponenteImportiCapitolo.AVANZO.getCodice()), 
				2);
	}
	
	
	public static void main(String[] args) {
		boolean[] aggiudicazioneSoggettoValues = new boolean[] {true, false};
		boolean[] origineSoggettoValues = new boolean[] {true, false};
		boolean[] flagPrenotazioneImpegnoOrigine = new boolean[] {true, false};
		
		for (boolean aggiudicazioneSoggetto : aggiudicazioneSoggettoValues) {
			for (boolean origineSoggetto : origineSoggettoValues) {
				for (boolean ereditato : flagPrenotazioneImpegnoOrigine) {
					StringBuilder sb = new StringBuilder().append(" aggiudicazioneSoggetto: ").append(aggiudicazioneSoggetto).append(" origineSoggetto: ").append(origineSoggetto)
							.append(" ereditato: ").append(ereditato);
					boolean flagCheck = false;
					try {
						flagCheck = getValoreFlagLiquidabileAggiudicazioneCheck(origineSoggetto,aggiudicazioneSoggetto, ereditato);
					} catch (Exception e) {
						System.err.println("si e' verificato un errore brutto con questi parametri:" + sb.toString());
					}
					
					boolean flag = getValoreFlagLiquidabileAggiudicazione(origineSoggetto, !aggiudicazioneSoggetto,ereditato);
					if(flagCheck != flag) {
						System.err.println("Risultati diversi per questi parametri: " + sb.toString() + ",  flagCheck " + flagCheck + ", flag " + flag);
					}
				}
			}
		}
		
		System.out.println("ciclo terminato");
		
		
	}
	
	
	
private static boolean getValoreFlagLiquidabileAggiudicazioneCheck(boolean origineSoggetto,boolean aggiudicazioneSoggetto, boolean ereditato) throws Exception {
		
		boolean aggiudicazioneSoggettoSi = aggiudicazioneSoggetto == true;
		boolean aggiudicazioneSoggettoNo = aggiudicazioneSoggetto == false;
		
		boolean origineSoggettoSi = origineSoggetto == true;
		boolean origineSoggettoNo = origineSoggetto == false;
		
		if(aggiudicazioneSoggettoNo && origineSoggettoSi) {
			return false;
		}
		
		if(aggiudicazioneSoggettoNo && origineSoggettoNo) {
			return ereditato;
		}
		
		if(aggiudicazioneSoggettoSi && origineSoggettoSi) {
			return false;
		}
		
		if(aggiudicazioneSoggettoSi && origineSoggettoNo) {
			return false;
		}
			
		throw new Exception("Non sono caduta in uno dei casi, impossiibile!!");
	}
	
//SIAC-7838
	private static boolean getValoreFlagLiquidabileAggiudicazione(boolean impegnoDiOrigineConSoggettoOClasse,boolean aggiudicazioneSenzaSoggetto, boolean flagLiquidabileImpegnoOrigine) {
		
		return !aggiudicazioneSenzaSoggetto || impegnoDiOrigineConSoggettoOClasse? false : flagLiquidabileImpegnoOrigine;
	}
	
	private static boolean getValoreFlagPrenotazioneAggiudicazioneCheck(boolean origineSoggetto,boolean aggiudicazioneSoggetto, boolean ereditato) throws Exception {
		
		boolean aggiudicazioneSoggettoSi = aggiudicazioneSoggetto == true;
		boolean aggiudicazioneSoggettoNo = aggiudicazioneSoggetto == false;
		
		boolean origineSoggettoSi = origineSoggetto == true;
		boolean origineSoggettoNo = origineSoggetto == false;
		
		if(aggiudicazioneSoggettoNo && origineSoggettoSi) {
			return true;
		}
		
		if(aggiudicazioneSoggettoNo && origineSoggettoNo) {
			return ereditato;
		}
		
		if(aggiudicazioneSoggettoSi && origineSoggettoSi) {
			return false;
		}
		
		if(aggiudicazioneSoggettoSi && origineSoggettoNo) {
			return false;
		}
			
		throw new Exception("Non sono caduta in uno dei casi, impossiibile!!");
	}
	
	private static boolean getValoreFlagPrenotazioneAggiudicazione(boolean impegnoDiOrigineConSoggettoOClasse,boolean aggiudicazioneSenzaSoggetto, boolean flagPrenotazioneImpegnoOrigine) {
		
		if(!aggiudicazioneSenzaSoggetto) {
			return false; 
		}
		return impegnoDiOrigineConSoggettoOClasse?  true : flagPrenotazioneImpegnoOrigine;
	}
	
	
	
//	public static void main(String[] args) {
//		List<Integer> maxValues = new ArrayList<Integer>();
//		
//		Integer maxValueModificheDiImporto = null; //Integer.valueOf(12);
//		maxValues.add(maxValueModificheDiImporto != null? maxValueModificheDiImporto : Integer.valueOf(0));
//		
//		Integer maxValueModificheDiSoggetto = null; //Integer.valueOf(6);
//		if(maxValueModificheDiSoggetto != null) {
//			maxValues.add(maxValueModificheDiSoggetto);
//		}
//		
//		Integer maxValueModificheDiClasseSoggetto = null;
//		if(maxValueModificheDiClasseSoggetto != null) {
//			maxValues.add(maxValueModificheDiClasseSoggetto);
//		}
//		
//		
//		Integer max = Collections.max(maxValues);
//		
//		System.out.println( new StringBuilder()
//							.append(" Massimo numero presente delle modifiche di importo: ")
//							.append(maxValueModificheDiImporto != null? maxValueModificheDiImporto : Integer.valueOf(0))
//							.append(" Massimo numero presente delle modifiche di soggetto: ")
//							.append(maxValueModificheDiSoggetto != null? maxValueModificheDiSoggetto : "null")
//							.append(" Massimo numero presente delle modifiche di classe: ")
//							.append(maxValueModificheDiClasseSoggetto != null? maxValueModificheDiClasseSoggetto : "null")
//							.append(" . Massimo tra i valori precedenti: ")
//							.append(max != null? max : "null")
//							.append(", inserirei una modifica con numero: ")
//							.append(max != null? (max.intValue() +1) : Integer.valueOf(0))
//				.toString());
//	}
}