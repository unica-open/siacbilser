/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.causale;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.business.service.causale.AggiornaCausaleService;
import it.csi.siac.siacbilser.business.service.causale.AnnullaCausaleService;
import it.csi.siac.siacbilser.business.service.causale.EliminaCausaleService;
import it.csi.siac.siacbilser.business.service.causale.InserisceCausaleService;
import it.csi.siac.siacbilser.business.service.causale.RicercaClassificatoriEPService;
import it.csi.siac.siacbilser.business.service.causale.RicercaDettaglioCausaleService;
import it.csi.siac.siacbilser.business.service.causale.RicercaSinteticaCausaleService;
import it.csi.siac.siacbilser.business.service.causale.RicercaSinteticaModulareCausaleService;
import it.csi.siac.siacbilser.business.service.causale.ValidaCausaleService;
import it.csi.siac.siacbilser.business.service.common.RicercaCodificheService;
import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaCodifiche;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaCodificheResponse;
import it.csi.siac.siacbilser.model.Ambito;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaClassificatoriEP;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaClassificatoriEPResponse;
import it.csi.siac.siaccespser.model.CategoriaCalcoloTipoCespite;
import it.csi.siac.siaccommon.util.JAXBUtility;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacgenser.frontend.webservice.msg.AggiornaCausale;
import it.csi.siac.siacgenser.frontend.webservice.msg.AggiornaCausaleResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.AnnullaCausale;
import it.csi.siac.siacgenser.frontend.webservice.msg.EliminaCausale;
import it.csi.siac.siacgenser.frontend.webservice.msg.InserisceCausale;
import it.csi.siac.siacgenser.frontend.webservice.msg.InserisceCausaleResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioCausale;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioCausaleResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaCausale;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaCausaleResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaModulareCausale;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaModulareCausaleResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.ValidaCausale;
import it.csi.siac.siacgenser.model.CausaleEP;
import it.csi.siac.siacgenser.model.CausaleEPModelDetail;
import it.csi.siac.siacgenser.model.ClasseDiConciliazione;
import it.csi.siac.siacgenser.model.Conto;
import it.csi.siac.siacgenser.model.ContoTipoOperazione;
import it.csi.siac.siacgenser.model.Evento;
import it.csi.siac.siacgenser.model.OperazioneSegnoConto;
import it.csi.siac.siacgenser.model.OperazioneTipoImporto;
import it.csi.siac.siacgenser.model.OperazioneUtilizzoConto;
import it.csi.siac.siacgenser.model.OperazioneUtilizzoImporto;
import it.csi.siac.siacgenser.model.StatoOperativoCausaleEP;
import it.csi.siac.siacgenser.model.TipoCausale;
import it.csi.siac.siacgenser.model.TipoEvento;

public class CausaleEpTest extends BaseJunit4TestCase {
	
	@Autowired
	private InserisceCausaleService inserisceCausaleService;
	
	@Autowired
	private AggiornaCausaleService aggiornaCausaleService;
	
	@Autowired
	private RicercaDettaglioCausaleService ricercaDettaglioCausaleService;
	
	@Autowired
	private AnnullaCausaleService annullaCausaleService;
	
	@Autowired
	private ValidaCausaleService validaCausaleService;

	@Autowired
	private EliminaCausaleService eliminaCausaleService;
	
	@Autowired
	private RicercaSinteticaCausaleService ricercaSinteticaCausaleService;
	
	@Autowired
	private RicercaCodificheService ricercaCodificheService;
	
//	@Autowired
//	private RicercaEventiPerTipoService ricercaEventiPerTipoService;
	
	@Autowired
	private RicercaClassificatoriEPService ricercaClassificatoriEPService;
	
	@Autowired
	private RicercaSinteticaModulareCausaleService ricercaSinteticaModulareCausaleService;
	
/****
 * 
 *  JUNITS 	MULTIPLI
 * 
 * 
 * 	*/
	@Test
	public void inserisciAggiornaSuVariAnniBilancio() {
		final String methodName="inserisciAggiornaSuVariAnniBilancio";
		
		Richiedente richiedente = getRichiedenteByProperties("consip", "regp");
		//i vari bilanci che vado a gestire
		
		Bilancio[] bilancios = new Bilancio[] {
				getBilancioByProperties("consip","regp", "2015"),
				getBilancioByProperties("consip","regp", "2016"),
				getBilancioByProperties("consip","regp", "2017")
		};
		
		
		Evento[] eventos = new Evento[] {
				create(Evento.class, 1209),
				create(Evento.class, 1222),
				create(Evento.class, 1235)		
		};
		
		
		Conto[] contosAvere = new Conto[] {
				create(Conto.class, 124634),
				create(Conto.class, 124615),
				create(Conto.class, 124494)
		};
		Conto[] contosDare = new Conto[] {
				create(Conto.class, 126107),
				create(Conto.class, 125398),
				create(Conto.class, 124141)
		};

		ClasseDiConciliazione[] classiDiConciliazione = ClasseDiConciliazione.values();
		
		Soggetto[] soggettos = new Soggetto[] {
				create(Soggetto.class, 162353),
				create(Soggetto.class, 162332),
				create(Soggetto.class, 162350),
		};
		
		StringBuilder riepilogoOperazione = new StringBuilder();
		
		
		//creo l'oggettoDaInserire
		CausaleEP causaleEPDaInserire = new CausaleEP();
		causaleEPDaInserire.setCodice("SIAC-6871-XXVIII");
		causaleEPDaInserire.setDescrizione("inserimento tramite junit nel 2015");
		
		causaleEPDaInserire.setTipoCausale(TipoCausale.Libera);
		
		causaleEPDaInserire.setAmbito(Ambito.AMBITO_GSA);
		
		causaleEPDaInserire.addEvento(eventos[0]);
		
//		causaleEPDaInserire.setSoggetto(soggettos[0]);
		
//		
		ContoTipoOperazione contoTipoOperazioneDare = new ContoTipoOperazione();
//		contoTipoOperazioneDare.setConto(contosDare[0]);
		contoTipoOperazioneDare.setClasseDiConciliazione(classiDiConciliazione[0]);
		contoTipoOperazioneDare.setOperazioneSegnoConto(OperazioneSegnoConto.DARE);
		contoTipoOperazioneDare.setOperazioneTipoImporto(OperazioneTipoImporto.LORDO);
		contoTipoOperazioneDare.setOperazioneUtilizzoConto(OperazioneUtilizzoConto.OBBLIGATORIO);
		contoTipoOperazioneDare.setOperazioneUtilizzoImporto(OperazioneUtilizzoImporto.NON_MODIFICABILE);
		
		
		ContoTipoOperazione contoTipoOperazioneAvere = new ContoTipoOperazione();
		contoTipoOperazioneAvere.setConto(contosAvere[0]);
		contoTipoOperazioneAvere.setOperazioneSegnoConto(OperazioneSegnoConto.AVERE);
		contoTipoOperazioneAvere.setOperazioneTipoImporto(OperazioneTipoImporto.LORDO);
		contoTipoOperazioneAvere.setOperazioneUtilizzoConto(OperazioneUtilizzoConto.OBBLIGATORIO);
		contoTipoOperazioneAvere.setOperazioneUtilizzoImporto(OperazioneUtilizzoImporto.NON_MODIFICABILE);
//		
//		
//		
		causaleEPDaInserire.addContoTipoOperazione(contoTipoOperazioneDare);
		causaleEPDaInserire.addContoTipoOperazione(contoTipoOperazioneAvere);
		
		
		riepilogoOperazione.append("inserisco la causale nell'anno: " + bilancios[0].getAnno() + ". ");
		InserisceCausaleResponse inserisceCausale = inserisceCausale(richiedente, bilancios[0], causaleEPDaInserire);
		if(inserisceCausale.hasErrori()) {
			
			riepilogoOperazione.append("Impossibile inserire la causale. Esco.");
			log.warn("RIEPILOGO", riepilogoOperazione.toString());
			return;
		}
		CausaleEP causaleInserita = inserisceCausale.getCausaleEP();
		riepilogoOperazione.append(". Inserita causale con uid " + causaleInserita.getUid());
		
//		causaleInserita.setEventi(new ArrayList<Evento>());
//		causaleInserita.addEvento(eventos[1]);
//		Soggetto soggetto1 = soggettos[1];
//		riepilogoOperazione.append("Aggiorno il soggetto a " + soggetto1.getUid());
//		causaleInserita.setSoggetto(soggetto1);
		
		String aggiornaContoConciliazioneRiepilogo = aggiornaContoConciliazione(causaleInserita,OperazioneSegnoConto.DARE,classiDiConciliazione[1]);
		riepilogoOperazione.append(aggiornaContoConciliazioneRiepilogo);
//		
//		aggiornaContiCausale(causaleInserita, contosDare[1], contosAvere[1]);
//		String resultAggiornaSegnoImportoCausale = aggiornaSegnoImportoCausale(causaleInserita,OperazioneSegnoConto.DARE, OperazioneSegnoConto.DARE);
//		riepilogoOperazione.append(resultAggiornaSegnoImportoCausale);
//		String resultAggiornaTipoImportoCausale = aggiornaTipoImportoCausale(causaleInserita, OperazioneSegnoConto.DARE, OperazioneTipoImporto.IMPOSTA);
//		riepilogoOperazione.append(resultAggiornaTipoImportoCausale);
//		String resultAggiornaUtilizzoContoCausale = aggiornaUtilizzoContoCausale(causaleInserita,OperazioneSegnoConto.DARE, OperazioneUtilizzoConto.PROPOSTO);
//		riepilogoOperazione.append(resultAggiornaUtilizzoContoCausale);
//		String resultAggiornaUtilizzoImportoCausale = aggiornaUtilizzoImportoCausale(causaleInserita,OperazioneSegnoConto.DARE, OperazioneUtilizzoImporto.PROPOSTO);
//		riepilogoOperazione.append(resultAggiornaUtilizzoImportoCausale);
		
		riepilogoOperazione.append(". Aggiorno la  causale con uid " + causaleInserita.getUid() + "nell'anno " + bilancios[1].getAnno());
		AggiornaCausaleResponse aggiornamentoCausaleEp = aggiornamentoCausaleEp(bilancios[1], richiedente, causaleInserita);
		
		if(aggiornamentoCausaleEp.hasErrori()) {
			riepilogoOperazione.append("Impossibile aggiornare la causale. Esco.");
			log.warn("RIEPILOGO", riepilogoOperazione.toString());
			return;
		}
//		if(!aggiornamentoCausaleEp.isFallimento()) {
//			log.error("RIEPILOGODIQUANTOFATTO", riepilogoOperazione.toString() + " Esco dopo il primo aggiornamento.");
//			return;
//		}
		CausaleEP causaleAggiornata = aggiornamentoCausaleEp.getCausaleEP();
		
		Soggetto soggetto2 = soggettos[2];
		riepilogoOperazione.append("Aggiorno il soggetto a " + soggetto2.getUid());
		causaleAggiornata.setSoggetto(soggetto2);
		
		String aggiornaContoConciliazioneRiepilogo2 = aggiornaContoConciliazione(causaleAggiornata,OperazioneSegnoConto.DARE,classiDiConciliazione[2]);
		riepilogoOperazione.append(aggiornaContoConciliazioneRiepilogo2);
//		
//		causaleAggiornata.setEventi(new ArrayList<Evento>());
//		causaleAggiornata.addEvento(eventos[2]);
		
//		aggiornaContiCausale(causaleAggiornata, contosDare[2], contosAvere[2]);
//		String resultAggiornaSegnoImportoCausale2 = aggiornaSegnoImportoCausale(causaleAggiornata,OperazioneSegnoConto.DARE, OperazioneSegnoConto.DARE);
//		riepilogoOperazione.append(resultAggiornaSegnoImportoCausale2);
//		String resultAggiornaTipoImportoCausale2 = aggiornaTipoImportoCausale(causaleAggiornata, OperazioneSegnoConto.DARE, OperazioneTipoImporto.IMPONIBILE);
//		riepilogoOperazione.append(resultAggiornaTipoImportoCausale2);		
//		String resultAggiornaUtilizzoContoCausale2 = aggiornaUtilizzoContoCausale(causaleAggiornata,OperazioneSegnoConto.DARE, OperazioneUtilizzoConto.OBBLIGATORIO);
//		riepilogoOperazione.append(resultAggiornaUtilizzoContoCausale2);
//		String resultAggiornaUtilizzoImportoCausale2 = aggiornaUtilizzoImportoCausale(causaleAggiornata,OperazioneSegnoConto.DARE, OperazioneUtilizzoImporto.NON_MODIFICABILE);
//		riepilogoOperazione.append(resultAggiornaUtilizzoImportoCausale2);
		
		
		
		
		riepilogoOperazione.append(". Aggiorno la  causale con uid " + causaleAggiornata.getUid() + " nell'anno " + bilancios[2].getAnno());
		AggiornaCausaleResponse aggiornamentoCausaleEp2= aggiornamentoCausaleEp(bilancios[2], richiedente, causaleAggiornata);
		
		if(aggiornamentoCausaleEp2.hasErrori()) {
			riepilogoOperazione.append("Impossibile aggiornare la causale. Esco.");
		}
		
		log.error("RIEPILOGODIQUANTOFATTO", riepilogoOperazione.toString());
	}
	/****
	 * 
	 *  JUNITS 	SINGOLI
	 * 
	 * 
	 * 	*/	@Test
	public void inserisciCausaleEP() {
		Richiedente richiedente = getRichiedenteByProperties("consip","regp");
		Bilancio bilancio = getBilancioByProperties("consip","regp", "2015");
		
		CausaleEP causaleEP = new CausaleEP();
		causaleEP.setCodice("SIAC-6871-IIV");
		causaleEP.setDescrizione("inserimento tramite junit nel 2015");
		
		causaleEP.setTipoCausale(TipoCausale.Libera);
		
		causaleEP.setAmbito(Ambito.AMBITO_FIN);
		
		
		Evento evento = new Evento();
		evento.setUid(1235);
		causaleEP.addEvento(evento);
		
//		
		ContoTipoOperazione contoTipoOperazioneDare = new ContoTipoOperazione();
		
		Conto contoDare = new Conto();
		contoDare.setUid(125473); //124634, 124615, 124494
		contoTipoOperazioneDare.setConto(contoDare);		
		contoTipoOperazioneDare.setOperazioneSegnoConto(OperazioneSegnoConto.DARE);
		contoTipoOperazioneDare.setOperazioneTipoImporto(OperazioneTipoImporto.LORDO);
		contoTipoOperazioneDare.setOperazioneUtilizzoConto(OperazioneUtilizzoConto.PROPOSTO);
		contoTipoOperazioneDare.setOperazioneUtilizzoImporto(OperazioneUtilizzoImporto.NON_MODIFICABILE);
		
		
		ContoTipoOperazione contoTipoOperazioneAvere = new ContoTipoOperazione();
		
		Conto contoAvere = new Conto();
		contoAvere.setUid(126480); //126107, 125398, 124141
		contoTipoOperazioneAvere.setConto(contoAvere);
		contoTipoOperazioneAvere.setOperazioneSegnoConto(OperazioneSegnoConto.AVERE);
		contoTipoOperazioneAvere.setOperazioneTipoImporto(OperazioneTipoImporto.LORDO);
		contoTipoOperazioneAvere.setOperazioneUtilizzoConto(OperazioneUtilizzoConto.PROPOSTO);
		contoTipoOperazioneAvere.setOperazioneUtilizzoImporto(OperazioneUtilizzoImporto.NON_MODIFICABILE);
//		
//		
//		
		causaleEP.addContoTipoOperazione(contoTipoOperazioneDare);
		causaleEP.addContoTipoOperazione(contoTipoOperazioneAvere);
		
		inserisceCausale(richiedente, bilancio, causaleEP);
		
		//Provo a ricercare la causale appena inserita!
//		ricercaDettaglioCausaleEP(bilancio, res.getCausaleEP().getUid());
	}

	@Test
	public void aggiornaCausaleEP() {
		Bilancio bilancio = getBilancioByProperties("consip","regp", "2016");
		Richiedente richiedente = getRichiedenteByProperties("consip","regp");
		RicercaDettaglioCausaleResponse responseDettaglio = ricercaDettaglioCausaleEP(bilancio, richiedente,create(CausaleEP.class, 539761));
		
		CausaleEP causaleEP = responseDettaglio.getCausaleEP();
		
//		causaleEP.setEventi(new ArrayList<Evento>());
//		
//		Evento evento = new Evento();
//		evento.setUid(1313);
//		causaleEP.addEvento(evento);
		
		Soggetto soggetto = new Soggetto();
		soggetto.setUid(162325);
		causaleEP.setSoggetto(soggetto);
//		
//		String aggiornaContoConciliazioneRiepilogo = aggiornaContoConciliazione(causaleEP,OperazioneSegnoConto.DARE,ClasseDiConciliazione.CONTI);
		
//		aggiornaContiCausale(causaleEP,new Conto, new Conto());
		
//		String resultAggiornaTipoImportoCausale = aggiornaTipoImportoCausale(causaleEP, OperazioneSegnoConto.DARE, OperazioneTipoImporto.IMPONIBILE);
		
		
		aggiornamentoCausaleEp(bilancio, richiedente, causaleEP);
		
		//Provo a ricercare la causale appena aggiornata!
		
	}
	
	
	@Test
	public void annullaCausaleEP() {
		AnnullaCausale req = new AnnullaCausale();
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		req.setBilancio(getBilancio2015Test());
		
		CausaleEP causaleEP = new CausaleEP();
		causaleEP.setUid(12);
		req.setCausaleEP(causaleEP);
		annullaCausaleService.executeService(req);
	}
	
	@Test
	public void validaCausaleEP() {
		ValidaCausale req = new ValidaCausale();
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		req.setBilancio(getBilancio2015Test());
		
		CausaleEP causaleEP = new CausaleEP();
		causaleEP.setUid(12);
		req.setCausaleEP(causaleEP);
		validaCausaleService.executeService(req);
	}
	
	@Test
	public void eliminaCausaleEP() {
		EliminaCausale req = new EliminaCausale();
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		req.setBilancio(getBilancio2015Test());
		
		CausaleEP causaleEP = new CausaleEP();
		causaleEP.setUid(12);
		req.setCausaleEP(causaleEP);
		eliminaCausaleService.executeService(req);
	}
	
	
	
	@Test
	public void riceraDettaglioCausaleEP() {
//		ricercaDettaglioCausaleEP(getBilancio2015Test(), 14);
//		ricercaDettaglioCausaleEP(getBilancio2016Test(), 6);
	}

	@Test
	public void riceraSinteticaCausaleEP() {
		RicercaSinteticaCausale req = new RicercaSinteticaCausale();
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setParametriPaginazione(new ParametriPaginazione(0,1));
		req.setBilancio(getBilancioTest(155, 2017));
		req.setBilancio(getBilancioTest(48, 2016));
		
		
		
		CausaleEP causaleEP = new CausaleEP();
		// Codice
		//causaleEP.setCodice("RA-U.2.02.01.07.001");
		//causaleEP.setDescrizione("RATEI");
		
		// Descrizione
//		causaleEP.setDescrizione("1");
		
		// Tipo Causale
		causaleEP.setTipoCausale(TipoCausale.Integrata);
		
		causaleEP.setAmbito(Ambito.AMBITO_GSA);
		
		// Stato Operativo Causale EP
		causaleEP.setStatoOperativoCausaleEP(StatoOperativoCausaleEP.VALIDO);
		
		// Conto
//		Conto conto = new Conto();
//		conto.setUid(7);
//		ContoTipoOperazione contoTipoOperazione = new ContoTipoOperazione();
//		contoTipoOperazione.setConto(conto);
//		causaleEP.addContoTipoOperazione(contoTipoOperazione);
		
		// Tipo Evento
		TipoEvento tipoEvento = new TipoEvento();
		tipoEvento.setUid(496);
		req.setTipoEvento(tipoEvento);
		
		
		// Evento
		Evento evento = new Evento();
		evento.setUid(1352);
		evento.setTipoEvento(tipoEvento);
		causaleEP.addEvento(evento);
		
		// Elemento Piano Dei Conti
		// TODO: non ho ancora legami su DB
		
		// Soggetto
//		Soggetto soggetto = new Soggetto();
//		soggetto.setUid(1);
//		causaleEP.setSoggetto(soggetto);
		
//		ElementoPianoDeiConti epdc = new ElementoPianoDeiConti();
//		epdc.setUid(8796004);		
//		causaleEP.setElementoPianoDeiConti(epdc);
//		causaleEP.setStatoOperativoCausaleEP(StatoOperativoCausaleEP.VALIDO);
		
		req.setCausaleEP(causaleEP);
		RicercaSinteticaCausaleResponse res = ricercaSinteticaCausaleService.executeService(req);
		assertNotNull(res);
	}
	
	
	@SuppressWarnings("unchecked")
	@Test
	public void ricercaCodificheTipoEvento(){
		RicercaCodifiche req = new RicercaCodifiche();
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.addTipiCodifica(CategoriaCalcoloTipoCespite.class/*, Evento.class*/);
		req.setAnnoBilancio(2017);
		
		RicercaCodificheResponse res = ricercaCodificheService.executeService(req);
		assertNotNull(res);
		
//		List<TipoEvento> tipiEvento = res.getCodifiche(TipoEvento.class);
//		
//		assertNotNull(tipiEvento);
//		assertTrue(tipiEvento.size()>0);
		
//		TipoEvento tipoEvento = tipiEvento.get(0);
//		
//		RicercaEventiPerTipo reqE = new RicercaEventiPerTipo();
//		reqE.setRichiedente(getRichiedenteByProperties("consip","regp"));
//		reqE.setTipoEvento(tipoEvento);
//		
//		RicercaEventiPerTipoResponse resE = ricercaEventiPerTipoService.executeService(reqE);
//		assertNotNull(resE);
		
		
		
	}
	
	@Test
	public void aggiornaCusaleEP2(){
//		StringBuilder sb = new StringBuilder();
//
//		sb.append("<aggiornaCausale>");
//		sb.append("    <dataOra>2015-04-01T14:27:55.738+02:00</dataOra>");
//		sb.append("    <richiedente>");
//		sb.append("        <account>");
//		sb.append("            <stato>VALIDO</stato>");
//		sb.append("            <uid>1</uid>");
//		sb.append("            <nome>Demo 21</nome>");
//		sb.append("            <descrizione>Demo 21 - CittÃ  di Torino</descrizione>");
//		sb.append("            <indirizzoMail>email</indirizzoMail>");
//		sb.append("            <ente>");
//		sb.append("                <stato>VALIDO</stato>");
//		sb.append("                <uid>1</uid>");
//		sb.append("                <gestioneLivelli>");
//		sb.append("                    <entry>");
//		sb.append("                        <key>LIVELLO_GESTIONE_BILANCIO</key>");
//		sb.append("                        <value>GESTIONE_UEB</value>");
//		sb.append("                    </entry>");
//		sb.append("                </gestioneLivelli>");
//		sb.append("                <nome>CittÃ  di Torino</nome>");
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
//		sb.append("    <bilancio>");
//		sb.append("        <stato>VALIDO</stato>");
//		sb.append("        <uid>16</uid>");
//		sb.append("        <anno>2015</anno>");
//		sb.append("    </bilancio>");
//		sb.append("    <causaleEP>");
//		sb.append("        <stato>VALIDO</stato>");
//		sb.append("        <uid>7</uid>");
//		sb.append("        <codice>001</codice>");
//		sb.append("        <contiTipoOperazione>");
//		sb.append("            <conto>");
//		sb.append("                <dataCreazione>2015-03-30T15:58:31.734+02:00</dataCreazione>");
//		sb.append("                <dataInizioValidita>2015-01-01T00:00:00+01:00</dataInizioValidita>");
//		sb.append("                <loginOperazione>Demo 21 - CittÃ  di Torino</loginOperazione>");
//		sb.append("                <stato>VALIDO</stato>");
//		sb.append("                <uid>134</uid>");
//		sb.append("                <loginCreazione>Demo 21 - CittÃ  di Torino</loginCreazione>");
//		sb.append("                <attivo>true</attivo>");
//		sb.append("                <codice>RE001002003-4-21</codice>");
//		sb.append("                <codiceInterno>13421</codiceInterno>");
//		sb.append("                <contoAPartite>false</contoAPartite>");
//		sb.append("                <contoDiLegge>false</contoDiLegge>");
//		sb.append("                <contoFoglia>true</contoFoglia>");
//		sb.append("                <contoPadre>");
//		sb.append("                    <dataCreazione>2015-03-26T09:16:01.926+01:00</dataCreazione>");
//		sb.append("                    <dataInizioValidita>2015-01-01T00:00:00+01:00</dataInizioValidita>");
//		sb.append("                    <loginOperazione>Demo 21 - CittÃ  di Torino</loginOperazione>");
//		sb.append("                    <stato>VALIDO</stato>");
//		sb.append("                    <uid>129</uid>");
//		sb.append("                    <loginCreazione>Demo 21 - CittÃ  di Torino</loginCreazione>");
//		sb.append("                    <codice>RE001002003-4-2</codice>");
//		sb.append("                    <contoAPartite>false</contoAPartite>");
//		sb.append("                    <contoPadre>");
//		sb.append("                        <dataCreazione>2015-03-19T17:13:53.436+01:00</dataCreazione>");
//		sb.append("                        <dataInizioValidita>2015-03-19T17:13:53.436+01:00</dataInizioValidita>");
//		sb.append("                        <loginOperazione>Demo 21 - CittÃ  di Torino</loginOperazione>");
//		sb.append("                        <stato>VALIDO</stato>");
//		sb.append("                        <uid>115</uid>");
//		sb.append("                        <loginCreazione>Demo 21 - CittÃ  di Torino</loginCreazione>");
//		sb.append("                        <codice>RE001002003</codice>");
//		sb.append("                        <contoAPartite>false</contoAPartite>");
//		sb.append("                        <contoPadre>");
//		sb.append("                            <dataCreazione>2015-03-19T17:05:31.359+01:00</dataCreazione>");
//		sb.append("                            <dataInizioValidita>2015-03-19T17:05:31.359+01:00</dataInizioValidita>");
//		sb.append("                            <loginOperazione>Demo 21 - CittÃ  di Torino</loginOperazione>");
//		sb.append("                            <stato>VALIDO</stato>");
//		sb.append("                            <uid>114</uid>");
//		sb.append("                            <loginCreazione>Demo 21 - CittÃ  di Torino</loginCreazione>");
//		sb.append("                            <codice>RE001002</codice>");
//		sb.append("                            <contoAPartite>false</contoAPartite>");
//		sb.append("                            <contoPadre>");
//		sb.append("<dataCreazione>2015-03-19T16:58:51.691+01:00</dataCreazione>");
//		sb.append("<dataInizioValidita>2015-03-19T16:58:51.691+01:00</dataInizioValidita>");
//		sb.append("<loginOperazione>Demo 21 - CittÃ  di Torino</loginOperazione>");
//		sb.append("<stato>VALIDO</stato>");
//		sb.append("<uid>113</uid>");
//		sb.append("<loginCreazione>Demo 21 - CittÃ  di Torino</loginCreazione>");
//		sb.append("<codice>RE001</codice>");
//		sb.append("<contoAPartite>false</contoAPartite>");
//		sb.append("<contoPadre>");
//		sb.append("    <dataCreazione>2015-03-16T12:16:06.247+01:00</dataCreazione>");
//		sb.append("    <dataInizioValidita>2014-01-01T00:00:00+01:00</dataInizioValidita>");
//		sb.append("    <loginOperazione>admin</loginOperazione>");
//		sb.append("    <stato>VALIDO</stato>");
//		sb.append("    <uid>12</uid>");
//		sb.append("    <loginCreazione>admin</loginCreazione>");
//		sb.append("    <codice>RE</codice>");
//		sb.append("    <contoAPartite>false</contoAPartite>");
//		sb.append("    <descrizione>Ricavi di Esercizio</descrizione>");
//		sb.append("    <ente>");
//		sb.append("        <dataCreazione>2013-05-20T11:21:26.827+02:00</dataCreazione>");
//		sb.append("        <dataInizioValidita>2013-01-01T00:00:00+01:00</dataInizioValidita>");
//		sb.append("        <loginOperazione>admin</loginOperazione>");
//		sb.append("        <stato>VALIDO</stato>");
//		sb.append("        <uid>1</uid>");
//		sb.append("        <codiceFiscale>aaaa</codiceFiscale>");
//		sb.append("        <gestioneLivelli/>");
//		sb.append("        <nome>CittÃ  di Torino</nome>");
//		sb.append("    </ente>");
//		sb.append("    <livello>0</livello>");
//		sb.append("    <ordine>RE</ordine>");
//		sb.append("</contoPadre>");
//		sb.append("<descrizione>descrizione primo figlio re</descrizione>");
//		sb.append("<ente>");
//		sb.append("    <dataCreazione>2013-05-20T11:21:26.827+02:00</dataCreazione>");
//		sb.append("    <dataInizioValidita>2013-01-01T00:00:00+01:00</dataInizioValidita>");
//		sb.append("    <loginOperazione>admin</loginOperazione>");
//		sb.append("    <stato>VALIDO</stato>");
//		sb.append("    <uid>1</uid>");
//		sb.append("    <codiceFiscale>aaaa</codiceFiscale>");
//		sb.append("    <gestioneLivelli/>");
//		sb.append("    <nome>CittÃ  di Torino</nome>");
//		sb.append("</ente>");
//		sb.append("<livello>1</livello>");
//		sb.append("                            </contoPadre>");
//		sb.append("                            <descrizione>secondo figlio</descrizione>");
//		sb.append("                            <ente>");
//		sb.append("<dataCreazione>2013-05-20T11:21:26.827+02:00</dataCreazione>");
//		sb.append("<dataInizioValidita>2013-01-01T00:00:00+01:00</dataInizioValidita>");
//		sb.append("<loginOperazione>admin</loginOperazione>");
//		sb.append("<stato>VALIDO</stato>");
//		sb.append("<uid>1</uid>");
//		sb.append("<codiceFiscale>aaaa</codiceFiscale>");
//		sb.append("<gestioneLivelli/>");
//		sb.append("<nome>CittÃ  di Torino</nome>");
//		sb.append("                            </ente>");
//		sb.append("                            <livello>2</livello>");
//		sb.append("                        </contoPadre>");
//		sb.append("                        <descrizione>terzo figlio</descrizione>");
//		sb.append("                        <ente>");
//		sb.append("                            <dataCreazione>2013-05-20T11:21:26.827+02:00</dataCreazione>");
//		sb.append("                            <dataInizioValidita>2013-01-01T00:00:00+01:00</dataInizioValidita>");
//		sb.append("                            <loginOperazione>admin</loginOperazione>");
//		sb.append("                            <stato>VALIDO</stato>");
//		sb.append("                            <uid>1</uid>");
//		sb.append("                            <codiceFiscale>aaaa</codiceFiscale>");
//		sb.append("                            <gestioneLivelli/>");
//		sb.append("                            <nome>CittÃ  di Torino</nome>");
//		sb.append("                        </ente>");
//		sb.append("                        <livello>3</livello>");
//		sb.append("                    </contoPadre>");
//		sb.append("                    <descrizione>prova per campo hidden</descrizione>");
//		sb.append("                    <ente>");
//		sb.append("                        <dataCreazione>2013-05-20T11:21:26.827+02:00</dataCreazione>");
//		sb.append("                        <dataInizioValidita>2013-01-01T00:00:00+01:00</dataInizioValidita>");
//		sb.append("                        <loginOperazione>admin</loginOperazione>");
//		sb.append("                        <stato>VALIDO</stato>");
//		sb.append("                        <uid>1</uid>");
//		sb.append("                        <codiceFiscale>aaaa</codiceFiscale>");
//		sb.append("                        <gestioneLivelli/>");
//		sb.append("                        <nome>CittÃ  di Torino</nome>");
//		sb.append("                    </ente>");
//		sb.append("                    <livello>4</livello>");
//		sb.append("                    <ordine>RE001002003-4-2</ordine>");
//		sb.append("                </contoPadre>");
//		sb.append("                <descrizione>Test livello 5</descrizione>");
//		sb.append("                <ente>");
//		sb.append("                    <dataCreazione>2013-05-20T11:21:26.827+02:00</dataCreazione>");
//		sb.append("                    <dataInizioValidita>2013-01-01T00:00:00+01:00</dataInizioValidita>");
//		sb.append("                    <loginOperazione>admin</loginOperazione>");
//		sb.append("                    <stato>VALIDO</stato>");
//		sb.append("                    <uid>1</uid>");
//		sb.append("                    <codiceFiscale>aaaa</codiceFiscale>");
//		sb.append("                    <gestioneLivelli/>");
//		sb.append("                    <nome>CittÃ  di Torino</nome>");
//		sb.append("                </ente>");
//		sb.append("                <livello>5</livello>");
//		sb.append("                <ordine>RE001002003-4-21</ordine>");
//		sb.append("                <pianoDeiConti>");
//		sb.append("                    <dataCancellazione>2015-03-16T12:16:06.168+01:00</dataCancellazione>");
//		sb.append("                    <dataCreazione>2015-03-16T12:16:06.168+01:00</dataCreazione>");
//		sb.append("                    <dataInizioValidita>2014-01-01T00:00:00+01:00</dataInizioValidita>");
//		sb.append("                    <loginOperazione>admin</loginOperazione>");
//		sb.append("                    <stato>VALIDO</stato>");
//		sb.append("                    <uid>2</uid>");
//		sb.append("                    <codice>RE</codice>");
//		sb.append("                    <descrizione>Ricavi di Esercizio</descrizione>");
//		sb.append("                    <classePiano>");
//		sb.append("                        <dataCreazione>2015-03-11T10:33:00.378+01:00</dataCreazione>");
//		sb.append("                        <dataInizioValidita>2014-01-01T00:00:00+01:00</dataInizioValidita>");
//		sb.append("                        <loginOperazione>admin</loginOperazione>");
//		sb.append("                        <stato>VALIDO</stato>");
//		sb.append("                        <uid>16</uid>");
//		sb.append("                        <codice>RE</codice>");
//		sb.append("                        <descrizione>Ricavi di Esercizio</descrizione>");
//		sb.append("                        <ente>");
//		sb.append("                            <dataCreazione>2013-05-20T11:21:26.827+02:00</dataCreazione>");
//		sb.append("                            <dataInizioValidita>2013-01-01T00:00:00+01:00</dataInizioValidita>");
//		sb.append("                            <loginOperazione>admin</loginOperazione>");
//		sb.append("                            <stato>VALIDO</stato>");
//		sb.append("                            <uid>1</uid>");
//		sb.append("                            <codiceFiscale>aaaa</codiceFiscale>");
//		sb.append("                            <gestioneLivelli/>");
//		sb.append("                            <nome>CittÃ  di Torino</nome>");
//		sb.append("                        </ente>");
//		sb.append("                        <segnoConto>AVERE</segnoConto>");
//		sb.append("                        <segnoContoString>AVERE</segnoContoString>");
//		sb.append("                    </classePiano>");
//		sb.append("                </pianoDeiConti>");
//		sb.append("                <tipoConto>");
//		sb.append("                    <dataCreazione>2015-03-11T10:33:00.217+01:00</dataCreazione>");
//		sb.append("                    <dataInizioValidita>2014-01-01T00:00:00+01:00</dataInizioValidita>");
//		sb.append("                    <loginOperazione>admin</loginOperazione>");
//		sb.append("                    <stato>VALIDO</stato>");
//		sb.append("                    <uid>58</uid>");
//		sb.append("                    <codice>GE</codice>");
//		sb.append("                    <descrizione>Generico</descrizione>");
//		sb.append("                    <ente>");
//		sb.append("                        <dataCreazione>2013-05-20T11:21:26.827+02:00</dataCreazione>");
//		sb.append("                        <dataInizioValidita>2013-01-01T00:00:00+01:00</dataInizioValidita>");
//		sb.append("                        <loginOperazione>admin</loginOperazione>");
//		sb.append("                        <stato>VALIDO</stato>");
//		sb.append("                        <uid>1</uid>");
//		sb.append("                        <codiceFiscale>aaaa</codiceFiscale>");
//		sb.append("                        <gestioneLivelli/>");
//		sb.append("                        <nome>CittÃ  di Torino</nome>");
//		sb.append("                    </ente>");
//		sb.append("                </tipoConto>");
//		sb.append("            </conto>");
//		sb.append("            <operazioneSegnoConto>DARE</operazioneSegnoConto>");
//		sb.append("            <operazioneTipoImporto>LORDO</operazioneTipoImporto>");
//		sb.append("            <operazioneUtilizzoConto>PROPOSTO</operazioneUtilizzoConto>");
//		sb.append("            <operazioneUtilizzoImporto>LIBERO</operazioneUtilizzoImporto>");
//		sb.append("        </contiTipoOperazione>");
//		sb.append("        <descrizione>Test 001</descrizione>");
//		sb.append("        <ente>");
//		sb.append("            <stato>VALIDO</stato>");
//		sb.append("            <uid>1</uid>");
//		sb.append("            <gestioneLivelli>");
//		sb.append("                <entry>");
//		sb.append("                    <key>LIVELLO_GESTIONE_BILANCIO</key>");
//		sb.append("                    <value>GESTIONE_UEB</value>");
//		sb.append("                </entry>");
//		sb.append("            </gestioneLivelli>");
//		sb.append("            <nome>CittÃ  di Torino</nome>");
//		sb.append("        </ente>");
//		sb.append("        <eventi>");
//		sb.append("            <dataCreazione>2015-03-26T15:52:04.591+01:00</dataCreazione>");
//		sb.append("            <dataInizioValidita>2015-03-26T15:52:04.591+01:00</dataInizioValidita>");
//		sb.append("            <loginOperazione>Dome</loginOperazione>");
//		sb.append("            <stato>VALIDO</stato>");
//		sb.append("            <uid>1</uid>");
//		sb.append("            <codice>CEN</codice>");
//		sb.append("            <descrizione>Cenone sotto il Sole</descrizione>");
//		sb.append("            <tipoEvento>");
//		sb.append("                <dataCreazione>2015-03-11T10:32:59.224+01:00</dataCreazione>");
//		sb.append("                <dataInizioValidita>2014-01-01T00:00:00+01:00</dataInizioValidita>");
//		sb.append("                <loginOperazione>admin</loginOperazione>");
//		sb.append("                <stato>VALIDO</stato>");
//		sb.append("                <uid>2</uid>");
//		sb.append("                <codice>I</codice>");
//		sb.append("                <descrizione>Impegno</descrizione>");
//		sb.append("            </tipoEvento>");
//		sb.append("        </eventi>");
//		sb.append("        <eventi>");
//		sb.append("            <dataCreazione>2015-03-30T09:10:01.180+02:00</dataCreazione>");
//		sb.append("            <dataInizioValidita>2014-01-01T00:00:00+01:00</dataInizioValidita>");
//		sb.append("            <loginOperazione>admin</loginOperazione>");
//		sb.append("            <stato>VALIDO</stato>");
//		sb.append("            <uid>2</uid>");
//		sb.append("            <codice>I_EV</codice>");
//		sb.append("            <descrizione>Impegno Evento</descrizione>");
//		sb.append("            <tipoEvento>");
//		sb.append("                <dataCreazione>2015-03-11T10:32:59.224+01:00</dataCreazione>");
//		sb.append("                <dataInizioValidita>2014-01-01T00:00:00+01:00</dataInizioValidita>");
//		sb.append("                <loginOperazione>admin</loginOperazione>");
//		sb.append("                <stato>VALIDO</stato>");
//		sb.append("                <uid>2</uid>");
//		sb.append("                <codice>I</codice>");
//		sb.append("                <descrizione>Impegno</descrizione>");
//		sb.append("            </tipoEvento>");
//		sb.append("        </eventi>");
//		sb.append("        <eventi>");
//		sb.append("            <dataCreazione>2015-03-30T09:10:01.180+02:00</dataCreazione>");
//		sb.append("            <dataInizioValidita>2014-01-01T00:00:00+01:00</dataInizioValidita>");
//		sb.append("            <loginOperazione>admin</loginOperazione>");
//		sb.append("            <stato>VALIDO</stato>");
//		sb.append("            <uid>12</uid>");
//		sb.append("            <codice>I_EV2</codice>");
//		sb.append("            <descrizione>Impegno Evento 2</descrizione>");
//		sb.append("            <tipoEvento>");
//		sb.append("                <dataCreazione>2015-03-11T10:32:59.224+01:00</dataCreazione>");
//		sb.append("                <dataInizioValidita>2014-01-01T00:00:00+01:00</dataInizioValidita>");
//		sb.append("                <loginOperazione>admin</loginOperazione>");
//		sb.append("                <stato>VALIDO</stato>");
//		sb.append("                <uid>2</uid>");
//		sb.append("                <codice>I</codice>");
//		sb.append("                <descrizione>Impegno</descrizione>");
//		sb.append("            </tipoEvento>");
//		sb.append("        </eventi>");
//		sb.append("        <tipoCausale>Libera</tipoCausale>");
//		sb.append("    </causaleEP>");
//		sb.append("</aggiornaCausale>");
		
		
		 // BuildMyString.com generated code. Please enjoy your StringBuilder responsibly.

		StringBuilder sb = new StringBuilder();

		sb.append("<aggiornaCausale>");
		sb.append("    <dataOra>2015-04-03T17:09:57.334+02:00</dataOra>");
		sb.append("    <richiedente>");
		sb.append("        <account>");
		sb.append("            <stato>VALIDO</stato>");
		sb.append("            <uid>1</uid>");
		sb.append("            <nome>Demo 21</nome>");
		sb.append("            <descrizione>Demo 21 - CittÃƒÂ  di Torino</descrizione>");
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
		sb.append("                <nome>CittÃƒÂ  di Torino</nome>");
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
		sb.append("    <bilancio>");
		sb.append("        <stato>VALIDO</stato>");
		sb.append("        <uid>16</uid>");
		sb.append("        <anno>2015</anno>");
		sb.append("    </bilancio>");
		sb.append("    <causaleEP>");
		sb.append("        <stato>VALIDO</stato>");
		sb.append("        <uid>13</uid>");
		sb.append("        <codice>004</codice>");
		sb.append("        <descrizione>Test numero 004 - date</descrizione>");
		sb.append("        <ente>");
		sb.append("            <stato>VALIDO</stato>");
		sb.append("            <uid>1</uid>");
		sb.append("            <gestioneLivelli>");
		sb.append("                <entry>");
		sb.append("                    <key>LIVELLO_GESTIONE_BILANCIO</key>");
		sb.append("                    <value>GESTIONE_UEB</value>");
		sb.append("                </entry>");
		sb.append("            </gestioneLivelli>");
		sb.append("            <nome>CittÃƒÂ  di Torino</nome>");
		sb.append("        </ente>");
		sb.append("        <eventi>");
		sb.append("            <dataCreazione>2015-03-30T09:10:01.180+02:00</dataCreazione>");
		sb.append("            <dataInizioValidita>2014-01-01T00:00:00+01:00</dataInizioValidita>");
		sb.append("            <loginOperazione>admin</loginOperazione>");
		sb.append("            <stato>VALIDO</stato>");
		sb.append("            <uid>3</uid>");
		sb.append("            <codice>A_EV</codice>");
		sb.append("            <descrizione>Accertamento Evento</descrizione>");
		sb.append("            <tipoEvento>");
		sb.append("                <dataCreazione>2015-03-11T10:32:59.326+01:00</dataCreazione>");
		sb.append("                <dataInizioValidita>2014-01-01T00:00:00+01:00</dataInizioValidita>");
		sb.append("                <loginOperazione>admin</loginOperazione>");
		sb.append("                <stato>VALIDO</stato>");
		sb.append("                <uid>16</uid>");
		sb.append("                <codice>A</codice>");
		sb.append("                <descrizione>Accertamento</descrizione>");
		sb.append("            </tipoEvento>");
		sb.append("        </eventi>");
		sb.append("        <tipoCausale>Libera</tipoCausale>");
		sb.append("    </causaleEP>");
		sb.append("</aggiornaCausale>");
				

		aggiornaCausaleService.executeService(JAXBUtility.unmarshall(sb.toString(), AggiornaCausale.class));
	}
	
	@Test
	public void ricercaClassificatoriEP() {
		RicercaClassificatoriEP req = new RicercaClassificatoriEP();
		
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		
		RicercaClassificatoriEPResponse res = ricercaClassificatoriEPService.executeService(req);
		
		assertNotNull(res);
	}

	@Test
	public void ricercaSinteticaModulareCausale() {
		RicercaSinteticaModulareCausale req = new RicercaSinteticaModulareCausale();
		
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setParametriPaginazione(getParametriPaginazione(0, 10));
		req.setBilancio(getBilancioTest(131, 2017));
//		req.setBilancio(getBilancioTest(133, 2018));
		req.setCausaleEPModelDetails(new CausaleEPModelDetail[] {CausaleEPModelDetail.Classif, CausaleEPModelDetail.Conto, CausaleEPModelDetail.Soggetto/*,CausaleEPModelDetail.ClasseConciliazione*/});
		
//		TipoEvento tipoEvento = new TipoEvento();
//		tipoEvento.setUid(2);
//		req.setTipoEvento(tipoEvento);
		
		CausaleEP causaleEP = new CausaleEP();
		causaleEP.setAmbito(Ambito.AMBITO_INV);
		req.setCausaleEP(causaleEP);
		
		List<Evento> eventi = new ArrayList<Evento>();
		causaleEP.setEventi(eventi);
		
		Evento evento = new Evento();
		evento.setUid(4787);
////		evento.setTipoEvento(tipoEvento);
		eventi.add(evento);
		
		RicercaSinteticaModulareCausaleResponse res = ricercaSinteticaModulareCausaleService.executeService(req);
		assertNotNull(res);
	}

	/***
	 * --------------------------------------------------------------------------------
	 *                                    CHIAMATE AI   SERVIZI	
	 * -------------------------------------------------------------------------------
	 */
	
	/**
	 * @param richiedente
	 * @param bilancio
	 * @param causaleEP
	 * @return 
	 */
	private InserisceCausaleResponse inserisceCausale(Richiedente richiedente, Bilancio bilancio, CausaleEP causaleEP) {
		InserisceCausale req = new InserisceCausale();
		req.setRichiedente(richiedente);		
		req.setBilancio(bilancio);
		
		
		
		req.setCausaleEP(causaleEP);
		
		InserisceCausaleResponse res = inserisceCausaleService.executeService(req);
		assertNotNull(res);
		assertNotNull(res.getCausaleEP());
		assertTrue(res.getCausaleEP().getUid()>0);
		return res;
	}
	
	

	/**
	 * @param bilancio
	 * @param richiedente
	 * @param causaleEP
	 * @return 
	 */
	private AggiornaCausaleResponse aggiornamentoCausaleEp(Bilancio bilancio, Richiedente richiedente, CausaleEP causaleEP) {
		AggiornaCausale req = new AggiornaCausale();
		req.setRichiedente(richiedente);
		req.setBilancio(bilancio);
		
		
		req.setCausaleEP(causaleEP);
		
		AggiornaCausaleResponse res = aggiornaCausaleService.executeService(req);
		assertNotNull(res);
		assertNotNull(res.getCausaleEP());
		return res;
	}


	private RicercaDettaglioCausaleResponse ricercaDettaglioCausaleEP(Bilancio bilancio, Richiedente richiedente, CausaleEP causaleEP) {
		RicercaDettaglioCausale req = new RicercaDettaglioCausale();
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		req.setBilancio(bilancio);
		req.setCausaleEP(causaleEP);
		return ricercaDettaglioCausaleService.executeService(req);
	}

/***
 * --------------------------------------------------------------------------------
 *                                      UTILITIES	
 * -------------------------------------------------------------------------------
 */

	
	private String aggiornaTipoImportoCausale(CausaleEP causaleEP, OperazioneSegnoConto operazioneSegnoContoDelContoDaAggiornare, OperazioneTipoImporto operazioneUtilizzoContoAggiornata ) {
		if(operazioneSegnoContoDelContoDaAggiornare == null && operazioneUtilizzoContoAggiornata == null) {
			return "";
		}
		for (ContoTipoOperazione cto : causaleEP.getContiTipoOperazione()) {
			if(operazioneSegnoContoDelContoDaAggiornare != null && operazioneSegnoContoDelContoDaAggiornare.equals(cto.getOperazioneSegnoConto())) {
				OperazioneTipoImporto operazioneUtilizzoContoOld = cto.getOperazioneTipoImporto();
				cto.setOperazioneTipoImporto(operazioneUtilizzoContoAggiornata);
				return "conto " + cto.getConto().getCodice() + " passa da tipo importo " + operazioneUtilizzoContoOld.name()  + " a "  + cto.getOperazioneTipoImporto().name()  + ". ";
			}
		}
		return "non e' stato aggiornato il tipo importo perche' non e' stato trovato un conto modificabile. ";
	}
	
	private String aggiornaUtilizzoImportoCausale(CausaleEP causaleEP, OperazioneSegnoConto operazioneSegnoContoDelContoDaAggiornare, OperazioneUtilizzoImporto operazioneUtilizzoContoAggiornata ) {
		if(operazioneSegnoContoDelContoDaAggiornare == null && operazioneUtilizzoContoAggiornata == null) {
			return "";
		}
		for (ContoTipoOperazione cto : causaleEP.getContiTipoOperazione()) {
			if(operazioneSegnoContoDelContoDaAggiornare != null && operazioneSegnoContoDelContoDaAggiornare.equals(cto.getOperazioneSegnoConto())) {
				OperazioneUtilizzoImporto operazioneUtilizzoContoOld = cto.getOperazioneUtilizzoImporto();
				cto.setOperazioneUtilizzoImporto(operazioneUtilizzoContoAggiornata);
				return "conto " + cto.getConto().getCodice() + " passa da ultilizzo importo " + operazioneUtilizzoContoOld.name()  + " a "  + cto.getOperazioneUtilizzoImporto().name() + ". ";
			}
		}
		return "non e' stato aggiornato l'utilizzo importo perche' non e' stato trovato un conto modificabile";
	}
	
	private String aggiornaUtilizzoContoCausale(CausaleEP causaleEP, OperazioneSegnoConto operazioneSegnoContoDelContoDaAggiornare, OperazioneUtilizzoConto operazioneUtilizzoContoAggiornata ) {
		if(operazioneSegnoContoDelContoDaAggiornare == null && operazioneUtilizzoContoAggiornata == null) {
			return "";
		}
		for (ContoTipoOperazione cto : causaleEP.getContiTipoOperazione()) {
			if(operazioneSegnoContoDelContoDaAggiornare != null && operazioneSegnoContoDelContoDaAggiornare.equals(cto.getOperazioneSegnoConto())) {
				OperazioneUtilizzoConto operazioneUtilizzoContoOld = cto.getOperazioneUtilizzoConto();
				cto.setOperazioneUtilizzoConto(operazioneUtilizzoContoAggiornata);
				return "conto " + cto.getConto().getCodice() + " passa da ultilizzo conto " + operazioneUtilizzoContoOld.name()  + " a "  + cto.getOperazioneUtilizzoConto().name() + ". ";
			}
		}
		return "non e' stato aggiornato l'utilizzo conto perche' non e' stato trovato un conto modificabile. ";
	}
	
	private String aggiornaSegnoImportoCausale(CausaleEP causaleEP, OperazioneSegnoConto operazioneSegnoContoPrecedente, OperazioneSegnoConto operazioneSegnoContoSuccessiva ) {
		if(operazioneSegnoContoPrecedente == null && operazioneSegnoContoSuccessiva == null) {
			return "";
		}
		for (ContoTipoOperazione cto : causaleEP.getContiTipoOperazione()) {
			if(operazioneSegnoContoPrecedente != null && operazioneSegnoContoPrecedente.equals(cto.getOperazioneSegnoConto())) {
				cto.setOperazioneSegnoConto(operazioneSegnoContoSuccessiva);
				return "conto " + cto.getConto().getCodice() + " passa da segno conto conto " + operazioneSegnoContoPrecedente.name()  + " a "  + operazioneSegnoContoSuccessiva.name() + ". ";
			}
		}
		return "non e' stato aggiornato il segno del conto perche' non e' stato trovato un conto modificabile. ";
	}


	/**
	 * @param causaleEP
	 */
	private void aggiornaContiCausale(CausaleEP causaleEP, Conto contoDare, Conto contoAvere) {
		if(contoDare == null && contoAvere == null) {
			return;
		}
		for (ContoTipoOperazione cto : causaleEP.getContiTipoOperazione()) {
			if(OperazioneSegnoConto.DARE.equals(cto.getOperazioneSegnoConto())) {
				cto.setConto(contoDare);
			}
			if(OperazioneSegnoConto.AVERE.equals(cto.getOperazioneSegnoConto())) {
				cto.setConto(contoAvere);
			}
		}
	}
	
	private String aggiornaContoConciliazione(CausaleEP causaleEP, OperazioneSegnoConto operazioneSegnoConto, ClasseDiConciliazione classeDiConciliazione){
		if(classeDiConciliazione == null){
			return "non e' stato aggiornata la classe di conciliazione perche' non e' stato trovato un conto modificabile. ";
		}
		for (ContoTipoOperazione cto : causaleEP.getContiTipoOperazione()) {
			if(operazioneSegnoConto != null && operazioneSegnoConto.equals(cto.getOperazioneSegnoConto())) {
				String conciliazioneOld =cto.getClasseDiConciliazione() != null?  cto.getClasseDiConciliazione().name() : "null";
				cto.setClasseDiConciliazione(classeDiConciliazione);
				cto.setConto(null);
				return "il conto  passa da segno conto conto " + conciliazioneOld  + " a "  + classeDiConciliazione.name() + ". ";
			}
		}
		return "non e' stato aggiornata la classe di conciliazione perche' non e' stato trovato un conto modificabile. ";
	}
	
	//STORICIZZAZIONE
	
	
	public static void main(String[] args) {
		List<RecordRelazione> recordRelazione = new ArrayList<RecordRelazione>();
		// inserimento
//		recordRelazione.add(new RecordRelazione("01/01/2015",null, null));
		//aggiornamento seuqenziale nel 2016:
//		recordRelazione.add(new RecordRelazione("01/01/2015","31/12/2015", null));
//		recordRelazione.add(new RecordRelazione("01/01/2016",null, null));
		//aggiornamento sequenziale nel 2016:
		recordRelazione.add(new RecordRelazione("01/01/2015","31/12/2016", null));
		recordRelazione.add(new RecordRelazione("01/01/2017",null, null));
		
		int annoBilancioDellaModifica = 2016;
		aggiornaRecord(recordRelazione, annoBilancioDellaModifica);
		recordRelazione.add(new RecordRelazione(Utility.primoGiornoDellAnno(annoBilancioDellaModifica),null, null));
		
//		System.out.println("*********");
//		recordRelazione.add(new RecordRelazione(Utility.primoGiornoDellAnno(annoBilancioDellaModifica),null, null));
//		annoBilancioDellaModifica = 2017;
//		recordRelazione.add(new RecordRelazione(Utility.primoGiornoDellAnno(annoBilancioDellaModifica),null, null));
//		aggiornaRecord(recordRelazione, annoBilancioDellaModifica);		
	}
	/**
	 * @param recordRelazione
	 * @param annoBilancioDellaModifica
	 */
	private static void aggiornaRecord(List<RecordRelazione> recordRelazione, int annoBilancioDellaModifica) {
		int annoBilancioPrecedenteAllaModifica = annoBilancioDellaModifica -1;
		
		for (RecordRelazione record : recordRelazione) {
			elaboraRecord(record, annoBilancioDellaModifica, Utility.ultimoGiornoDellAnno(annoBilancioPrecedenteAllaModifica), Utility.primoGiornoDellAnno(annoBilancioDellaModifica), new Date());
			System.out.println(record.getDescrizioneValiditaFine());
		}
	}
	/**
	 * @param annoBilancioDellaModifica
	 * @param ultimoGiornoAnnoBilancioPrecedente
	 * @param record
	 */
	private static void elaboraRecord(RecordRelazione record, int annoBilancioDellaModifica, Date ultimoGiornoAnnoBilancioPrecedente, Date primoGiornoAnnoBilancio, Date dataCancellazioneDaImpostare) {
		Date ultimoGiornoDellannoDellaValiditaInizioDellEntityAttuale = Utility.ultimoGiornoDellAnno(record.getDataInizioValidita()); // alpha
		int annoInizioValidita = Utility.getAnno(record.getDataInizioValidita());
		boolean validitaFineNellAnnoDiInizio = record.getDataFineValidita() != null;
		int deltaBilancioInizioValidita = annoBilancioDellaModifica - annoInizioValidita;
//			int deltaAnnoModificaAnnoInizioValiditaRecord = annoBilancioDellaModifica - annoInizioValidita;
		boolean esistonoRecordSuccessiviNonCoinvoltiDaModifica = validitaFineNellAnnoDiInizio && (deltaBilancioInizioValidita > 1);
//			boolean modificaNelloStessoAnnoDelRecord = deltaAnnoModificaAnnoInizioValiditaRecord == 0;
//			boolean modificaAnnoImmediatamenteSuccessivoAlRecord = deltaAnnoModificaAnnoInizioValiditaRecord == 1;
//			Date dataFineValiditaDaImpostare = modificaNelloStessoAnnoDelRecord || modificaAnnoImmediatamenteSuccessivoAlRecord ?
//		Date dataFineValiditaDaImpostare = annoInizioValidita <= annoBilancioDellaModifica ?
		Date dataFineValiditaDaImpostare = annoInizioValidita <= annoBilancioDellaModifica  && !esistonoRecordSuccessiviNonCoinvoltiDaModifica ?
				//ho inserito/aggiornato una relazione nel 2017 ma ho relazioni nel 2018, non modifico successiva
				Utility.dataPiuRecente(ultimoGiornoDellannoDellaValiditaInizioDellEntityAttuale, ultimoGiornoAnnoBilancioPrecedente) :
				//ho inserito/aggiornato una relazione nel 2019 e sto aggiornando una relazione nel 2018
				record.getDataFineValidita();
		System.out.println( 
				new StringBuilder().append(" dataFineValiditaDaImpostare: ")
				.append(formattaData(dataFineValiditaDaImpostare))
				.append(" A partire dall' ultimoGiornoDellannoDellaValiditaInizioDellEntityAttuale: ") 
				.append(formattaData(ultimoGiornoDellannoDellaValiditaInizioDellEntityAttuale))
				.append("e ultimoGiornoAnnoBilancioPrecedente: ")
				.append(ultimoGiornoAnnoBilancioPrecedente != null? ultimoGiornoAnnoBilancioPrecedente : "null")
				.append("la data piu' recente e': ")
				.append(formattaData(Utility.dataPiuRecente(ultimoGiornoDellannoDellaValiditaInizioDellEntityAttuale, ultimoGiornoAnnoBilancioPrecedente)))
				.append("e la data validita fine pregressa e' ")
				.append(formattaData(record.getDataFineValidita()))
				.toString());
		
		record.impostaDate(dataCancellazioneDaImpostare, primoGiornoAnnoBilancio, dataFineValiditaDaImpostare);
		
	}

	
	private static String formattaData(Date dataDaFormattare) {
		if(dataDaFormattare == null) {
			return "<null>";
		}
		return getDateFormat().format(dataDaFormattare);
	}
	
	private static SimpleDateFormat getDateFormat() {
		return new SimpleDateFormat("dd/MM/yyyy", Locale.ITALY);
	}
	
	private static Date parsificaData(String dataDaParsificare){
		
		Date  data = null;
		
		if(StringUtils.isBlank(dataDaParsificare)) {
			return data;
		}
		
		try {
			data = getDateFormat().parse(dataDaParsificare);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			System.out.println("impossibile parsificare la data: " + dataDaParsificare);
		}
		
		return data;
	}
	
	public static class RecordRelazione {
		private Date dataInizioValidita;
		private Date dataFineValidita;
		private Date dataCancellazione;
		
		final String intestazione = "val_inizio \t valid_fine \t cancellazione";
		
		public RecordRelazione(Date dataInizioValidita, Date dataFineValidita, Date dataCancellazione){
			this.dataInizioValidita = dataInizioValidita; 
			this.dataFineValidita = dataFineValidita;
			this.dataCancellazione = dataCancellazione;
		}
		
		public RecordRelazione(String dataInizioValidita, String dataFineValidita, String dataCancellazione){
			this.dataInizioValidita = parsificaData(dataInizioValidita); 
			this.dataFineValidita = parsificaData(dataFineValidita);
			this.dataCancellazione = parsificaData(dataCancellazione);
		}
		
		/**
		 * @return the dataInizioValidita
		 */
		public Date getDataInizioValidita() {
			return dataInizioValidita;
		}

		/**
		 * @return the dataFineValidita
		 */
		public Date getDataFineValidita() {
			return dataFineValidita;
		}


		/**
		 * @return the dataCancellazione
		 */
		public Date getDataCancellazione() {
			return dataCancellazione;
		}

		/**
		 * @param dataCancellazione the dataCancellazione to set
		 */
		public void setDataCancellazione(Date dataCancellazione) {
			this.dataCancellazione = dataCancellazione;
		}

		/**
		 * @return
		 */
		
		private boolean isDataValiditaCompresa(Date data) {
			
			boolean result = this.getDataInizioValidita().compareTo(data) <= 0
			&& (this.getDataFineValidita()==null || this.getDataFineValidita().compareTo(data)>=0);
			
			return result;
		}

		
		public void impostaDate(Date dataCancellazioneDaImpostare, Date dataNelPeriodoDiValidita, Date dataFineValiditaDaImpostare) {
			if(getDataCancellazione()==null) {
				this.dataFineValidita = dataFineValiditaDaImpostare;
				
				//posso impostare la data cancellazione solo se del mio range di validita' di competenza 
				if(isDataValiditaCompresa(dataNelPeriodoDiValidita)) { 
					this.dataCancellazione = dataCancellazioneDaImpostare;
				} 
				
				
			}

		}
		
		public String getDescrizioneValiditaFine() {
			return "validita_fine: " + formattaData(this.dataFineValidita);
		}
		
		public String getDescrizioneRecord() {
			return new StringBuilder()
					.append(intestazione)
					.append("\n")
					.append(formattaData(this.dataInizioValidita))
					.append("\t")
					.append(formattaData(this.dataFineValidita))
					.append("\t")
					.append(formattaData(this.dataCancellazione))
					.toString();
		}
		
	}
	
}
