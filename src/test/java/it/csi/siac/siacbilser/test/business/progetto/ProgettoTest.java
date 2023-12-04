/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.progetto;

import java.util.Date;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacbilser.business.service.progetto.AggiornaAnagraficaProgettoService;
import it.csi.siac.siacbilser.business.service.progetto.AnnullaProgettoService;
import it.csi.siac.siacbilser.business.service.progetto.InserisceAnagraficaProgettoService;
import it.csi.siac.siacbilser.business.service.progetto.RiattivaProgettoService;
import it.csi.siac.siacbilser.business.service.progetto.RicercaDettaglioProgettoService;
import it.csi.siac.siacbilser.business.service.progetto.RicercaPuntualeProgettoService;
import it.csi.siac.siacbilser.business.service.progetto.RicercaSinteticaProgettoService;
import it.csi.siac.siacbilser.business.service.progetto.RicercaTipiAmbitoService;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaAnagraficaProgetto;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaAnagraficaProgettoResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.AnnullaProgetto;
import it.csi.siac.siacbilser.frontend.webservice.msg.AnnullaProgettoResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceAnagraficaProgetto;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceAnagraficaProgettoResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RiattivaProgetto;
import it.csi.siac.siacbilser.frontend.webservice.msg.RiattivaProgettoResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioProgetto;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioProgettoResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeProgetto;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeProgettoResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaProgetto;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaProgettoResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaTipiAmbito;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaTipiAmbitoResponse;
import it.csi.siac.siacbilser.model.ModalitaAffidamentoProgetto;
import it.csi.siac.siacbilser.model.Progetto;
import it.csi.siac.siacbilser.model.StatoOperativoProgetto;
import it.csi.siac.siacbilser.model.TipoAmbito;
import it.csi.siac.siacbilser.model.TipoProgetto;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;

// TODO: Auto-generated Javadoc
/**
 * Classe di test per il Progetto.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 05/02/2014
 */
public class ProgettoTest extends BaseJunit4TestCase {
	
	/** The inserisce anagrafica progetto service. */
	@Autowired
	private InserisceAnagraficaProgettoService inserisceAnagraficaProgettoService;
	
	/** The aggiorna anagrafica progetto service. */
	@Autowired
	private AggiornaAnagraficaProgettoService aggiornaAnagraficaProgettoService;
	
	/** The ricerca puntuale progetto service. */
	@Autowired
	private RicercaPuntualeProgettoService ricercaPuntualeProgettoService;
	
	/** The ricerca sintetica progetto service. */
	@Autowired
	private RicercaSinteticaProgettoService ricercaSinteticaProgettoService;
	
	/** The ricerca dettaglio progetto service. */
	@Autowired
	private RicercaDettaglioProgettoService ricercaDettaglioProgettoService;
	
	/** The annulla progetto service. */
	@Autowired
	private AnnullaProgettoService annullaProgettoService;
	
	/** The riattiva progetto service. */
	@Autowired
	private RiattivaProgettoService riattivaProgettoService;
	
	/** The ricerca tipi ambito service. */
	@Autowired
	private RicercaTipiAmbitoService ricercaTipiAmbitoService;
	
	/**
	 * Test di inserimento dell'anagrafica del progetto.
	 */
	@Test
	public void inserisceAnagraficaProgetto(){
		InserisceAnagraficaProgetto req = new InserisceAnagraficaProgetto();
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		req.setBilancio(getBilancioByProperties("consip","regp", "2017"));
		
		Progetto progetto = new Progetto();
		progetto.setEnte(req.getRichiedente().getAccount().getEnte());
		progetto.setCodice("PRG-6255-0007-TST - 2");
		progetto.setDescrizione("Progetto di test per la SIAC-6255, numero 0007");
		progetto.setTipoProgetto(TipoProgetto.GESTIONE);
		//progetto.setRilevanteFPV(Boolean.TRUE);
		progetto.setNote("Note parziali per il progetto di test");
		progetto.setStatoOperativoProgetto(StatoOperativoProgetto.VALIDO);
		progetto.setAttoAmministrativo(create(AttoAmministrativo.class, 35076));
		progetto.setTipoAmbito(create(TipoAmbito.class, 41154));
		progetto.setResponsabileUnico("Responsabile unico");
		progetto.setSpaziFinanziari(Boolean.TRUE);
		progetto.setModalitaAffidamentoProgetto(create(ModalitaAffidamentoProgetto.class, 4));
		//copiato un cup a caso
//		progetto.setCup("B26G13001840000");
		progetto.setStrutturaAmministrativoContabile(create(StrutturaAmministrativoContabile.class, 1719623));
		//progetto.setValoreComplessivo(new BigDecimal("1200000"));
		
		req.setProgetto(progetto);
		
		InserisceAnagraficaProgettoResponse res = inserisceAnagraficaProgettoService.executeService(req);
		
		assertNotNull(res);
		assertNotNull(res.getProgetto());
		
		System.out.println("uid progetto: " + res.getProgetto().getUid());
	}
	
	/**
	 * Aggiorna anagrafica progetto.
	 */
	@Test
	public void aggiornaAnagraficaProgetto() {
		
		RicercaDettaglioProgetto reqRicerca = new RicercaDettaglioProgetto();
		
		reqRicerca.setRichiedente(getRichiedenteByProperties("consip","regp"));
		reqRicerca.setChiaveProgetto(64);
				
		RicercaDettaglioProgettoResponse res = ricercaDettaglioProgettoService.executeService(reqRicerca);
		
		if(res.hasErrori()) {
			System.out.println("impossibile reperire il progetto");
			return;
		}
		
		assertNotNull(res.getProgetto());
		
		AggiornaAnagraficaProgetto req = new AggiornaAnagraficaProgetto();
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		
		Progetto progetto = res.getProgetto();
		progetto.setCodice("6255-DEPLOY1");
		
//		progetto.setStrutturaAmministrativoContabile(create(StrutturaAmministrativoContabile.class, 75639151));
//		progetto.setResponsabileUnico(progetto.getResponsabileUnico() + " - aggiornato");
//		progetto.setTipoProgetto(TipoProgetto.GESTIONE);
		req.setProgetto(progetto);
		req.setAnnoBilancio(2017);
		
		AggiornaAnagraficaProgettoResponse resAggiorna = aggiornaAnagraficaProgettoService.executeService(req);
		
		assertNotNull(resAggiorna);
		assertNotNull(resAggiorna.getProgetto());
		
		System.out.println("uid progetto: " + res.getProgetto().getUid());
	}
	
	/**
	 * Ricerca puntuale progetto.
	 */
	@Test
	public void ricercaPuntualeProgetto() {
		RicercaPuntualeProgetto req = new RicercaPuntualeProgetto();
		
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		
		Progetto progetto = new Progetto();
		progetto.setEnte(req.getRichiedente().getAccount().getEnte());
		progetto.setStatoOperativoProgetto(StatoOperativoProgetto.VALIDO);
		progetto.setCodice("PRG-6255-0002-TST");
		
		req.setProgetto(progetto);
				
		RicercaPuntualeProgettoResponse res = ricercaPuntualeProgettoService.executeService(req);
		
		assertNotNull(res);
		assertNotNull(res.getProgetto());
		
		System.out.println("Progetto: " + ReflectionToStringBuilder.reflectionToString(res.getProgetto(), 
				ToStringStyle.MULTI_LINE_STYLE));
	}
	
	/**
	 * Ricerca sintetica progetto.
	 */
	@Test
	public void ricercaSinteticaProgetto() {
		RicercaSinteticaProgetto req = new RicercaSinteticaProgetto();
		req.setDataOra(new Date());
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		req.setParametriPaginazione(getParametriPaginazioneTest());
		
		Progetto progetto = new Progetto();
		progetto.setTipoAmbito(create(TipoAmbito.class, 41139));
		//progetto.setDescrizione("t");
		//progetto.setRilevanteFPV(Boolean.TRUE);
//		progetto.setStatoOperativoProgetto(StatoOperativoProgetto.VALIDO);
//		//progetto.setAttoAmministrativo(getAttoAmministrativoTest());
		progetto.setEnte(req.getRichiedente().getAccount().getEnte());
//		
//		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
//		String dataIndizione = "10-03-2015 00:00:00";//2015-03-10T00:00:00+01:00  
//		                                             //2015-03-10T00:00:00+01:00
//		Date date =new Date();
//				try {
//					date=sdf.parse(dataIndizione);
//				} catch (ParseException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//		progetto.setDataIndizioneGara(date);
	//	progetto.setCodice("A102M");
		req.setProgetto(progetto);
		
		RicercaSinteticaProgettoResponse res = ricercaSinteticaProgettoService.executeService(req);
		
		assertNotNull(res);
	}
	
	/**
	 * Ricerca dettaglio progetto.
	 */
	@Test
	public void ricercaDettaglioProgetto() {
		RicercaDettaglioProgetto req = new RicercaDettaglioProgetto();
		
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		req.setChiaveProgetto(51);
				
		RicercaDettaglioProgettoResponse res = ricercaDettaglioProgettoService.executeService(req);
		
		assertNotNull(res);
		assertNotNull(res.getProgetto());
//		
//		System.out.println("#Cronoprogrammi: " + res.getProgetto().getCronoprogrammi().size());
//		for(Cronoprogramma c : res.getProgetto().getCronoprogrammi()) {
//			System.out.println("Cronoprogramma " + c.getUid() + " - #Entrata: " + c.getCapitoliEntrata().size()
//				+ " | #Uscita: " + c.getCapitoliUscita().size());
//		}
	}
	
	/**
	 * Annulla progetto.
	 */
	@Test
	public void annullaProgetto() {
		AnnullaProgetto req = new AnnullaProgetto();
		
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		req.setDataOra(new Date());
		
		Progetto progetto = new Progetto();
		progetto.setUid(4);
		progetto.setEnte(getEnteTest());
		
		req.setProgetto(progetto);
				
		AnnullaProgettoResponse res = annullaProgettoService.executeService(req);
		
		assertNotNull(res);
		assertNotNull(res.getProgetto());
	}
	
	/**
	 * Riattiva progetto.
	 */
	@Test
	public void riattivaProgetto() {
		RiattivaProgetto req = new RiattivaProgetto();
		
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		req.setDataOra(new Date());
		
		Progetto progetto = new Progetto();
		progetto.setUid(4);
		progetto.setEnte(getEnteTest());
		
		req.setProgetto(progetto);
				
		RiattivaProgettoResponse res = riattivaProgettoService.executeService(req);
		
		assertNotNull(res);
		assertNotNull(res.getProgetto());
	}
	
	/**
	 * Ricerca tipi ambito.
	 */
	@Test
	public void ricercaTipiAmbito() {
		RicercaTipiAmbito req = new RicercaTipiAmbito();
		
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		req.setDataOra(new Date());
		
		req.setAnno(2013);
		req.setEnte(getEnteTest());
		
		RicercaTipiAmbitoResponse res = ricercaTipiAmbitoService.executeService(req);
		
		assertNotNull(res);
		assertNotNull(res.getTipiAmbito());
	}
	
	
	
	/**
	 * Ottiene un Atto Amministrativo di test.
	 * 
	 * @return l'atto
	 */
	private AttoAmministrativo getAttoAmministrativoTest() {
		AttoAmministrativo attoAmministrativo = new AttoAmministrativo();
		attoAmministrativo.setUid(34); // Cfr. siac_t_atto_amm, DB
		return attoAmministrativo;
	}
	
	/**
	 * Ottiene un Tipo Ambito di test.
	 * 
	 * @return il tipo
	 */
	private TipoAmbito getTipoAmbitoTest() {
		TipoAmbito tipoAmbito = new TipoAmbito();
		tipoAmbito.setUid(41138); // Cfr. siac_t_class, DB
		return tipoAmbito;
	}
	

	
}
