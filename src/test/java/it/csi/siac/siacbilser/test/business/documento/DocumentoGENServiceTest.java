/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.documento;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.business.service.documentospesa.AggiornaStatoDocumentoDiSpesaService;
import it.csi.siac.siacbilser.integration.dad.CapitoloUscitaGestioneDad;
import it.csi.siac.siacbilser.integration.dad.RegistrazioneMovFinDad;
import it.csi.siac.siacbilser.model.Ambito;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.ElementoPianoDeiConti;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaStatoDocumentoDiSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaStatoDocumentoDiSpesaResponse;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacgenser.model.Evento;
import it.csi.siac.siacgenser.model.RegistrazioneMovFin;
import it.csi.siac.siacgenser.model.StatoOperativoRegistrazioneMovFin;
import it.csi.siac.siacgenser.model.TipoCollegamento;

public class DocumentoGENServiceTest extends BaseJunit4TestCase {
	
	
	@Autowired
	private RegistrazioneMovFinDad registrazioneMovFinDad;
	@Autowired
	private CapitoloUscitaGestioneDad capitoloUscitaGestioneDad;
	
	@Autowired
	private AggiornaStatoDocumentoDiSpesaService aggiornaStatoDocumentoDiSpesaService;
	
	@Test
	public void inserisciRegistrazioneMovFin() {
		
		registrazioneMovFinDad.setEnte(getEnteTest());
		registrazioneMovFinDad.setLoginOperazione("TEST");
		
		
		SubdocumentoSpesa subdoc = new SubdocumentoSpesa();
		subdoc.setUid(127);
		
		Evento evento = registrazioneMovFinDad.ricercaEventoCensito(TipoCollegamento.SUBDOCUMENTO_SPESA, false, false, false,false);
		
		RegistrazioneMovFin registrazioneMovFin = new RegistrazioneMovFin();
		registrazioneMovFin.setEnte(getEnteTest());
		registrazioneMovFin.setBilancio(getBilancio2015Test());
		
		registrazioneMovFin.setMovimento(subdoc);
		registrazioneMovFin.setEvento(evento);
		registrazioneMovFin.setStatoOperativoRegistrazioneMovFin(StatoOperativoRegistrazioneMovFin.NOTIFICATO); //O Registrato?
		
		//registrazioneMovFin.setConto(conto);									//popolato quando la prima nota passerà allo stato DEFINITA!
		//registrazioneMovFin.setListaMovimentiEP(listaMovimentiEP);			//popolato quando creo la prima nota!
		
		CapitoloUscitaGestione cap = new CapitoloUscitaGestione();
		cap.setUid(12349);
		ElementoPianoDeiConti elementoPianoDeiConti = capitoloUscitaGestioneDad.ricercaClassificatoreElementoPianoDeiContiCapitolo(cap);
		registrazioneMovFin.setElementoPianoDeiContiIniziale(elementoPianoDeiConti);
		registrazioneMovFin.setElementoPianoDeiContiAggiornato(elementoPianoDeiConti);
		//registrazioneMovFin.setElementoPianoDeiContiAggiornato(elementoPianoDeiContiAggiornato);	//viene modificato per particolari attività utente.
		
		
		registrazioneMovFinDad.inserisciRegistrazioneMovFin(registrazioneMovFin);
		
		System.out.println("uid inserimento registrazione: "+registrazioneMovFin.getUid());
		
		
		RegistrazioneMovFin registrazioneMovFinInserita = registrazioneMovFinDad.ricercaDettaglioRegistrazioneMovFin(registrazioneMovFin.getUid());
		log.logXmlTypeObject(registrazioneMovFinInserita, "registrazioneMovFinInserita: ");
		
	}
	
	
	
	@Test
	public void aggiornaStatoDocumentoDiSpesa() {
		AggiornaStatoDocumentoDiSpesa req = new AggiornaStatoDocumentoDiSpesa();
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setBilancio(getBilancioTest(131, 2017));
		DocumentoSpesa docSpesa = new DocumentoSpesa();
		docSpesa.setUid(32095); //38418
		/*
		 	217
			219
			215
			218
			221
			222
			223
			445
		 */
		req.setDocumentoSpesa(docSpesa);
		
		AggiornaStatoDocumentoDiSpesaResponse res = aggiornaStatoDocumentoDiSpesaService.executeService(req);
	}
	@Test
	public void testRicercaSintetica() {
		
		registrazioneMovFinDad.setEnte(getRichiedenteByProperties("consip", "regp").getAccount().getEnte());
		registrazioneMovFinDad.setLoginOperazione("tst-junit");
		
		RegistrazioneMovFin registrazioneMovFin = new RegistrazioneMovFin();
		registrazioneMovFin.setBilancio(getBilancioTest(131, 2017)); //Serve Davvero??? :(
		registrazioneMovFin.setAmbito(Ambito.AMBITO_FIN);
		
		List<StatoOperativoRegistrazioneMovFin> statiOperativiRegistrazioneMovFinDaEscludere = Arrays.asList(StatoOperativoRegistrazioneMovFin.ANNULLATO);
		
		List<Integer> idMovimento = new ArrayList<Integer>();
		Integer docId;
			docId =32095;
			idMovimento = null;
		ListaPaginata<RegistrazioneMovFin> registrazioniMovFin = registrazioneMovFinDad.ricercaSinteticaRegistrazioneMovFin(
				registrazioneMovFin,
				TipoCollegamento.SUBDOCUMENTO_SPESA, //ad esempio: TipoCollegamento.SUBDOCUMENTO_SPESA,
				docId,  //doc.getUid(), //movimento.getUid()
				idMovimento,
				statiOperativiRegistrazioneMovFinDaEscludere,
				new ParametriPaginazione(0, Integer.MAX_VALUE) 
				);
		if(registrazioniMovFin != null && !registrazioniMovFin.isEmpty()) {
			System.out.println("trovate "  + registrazioniMovFin.size());
			for (RegistrazioneMovFin reg : registrazioniMovFin) {
				SubdocumentoSpesa ss = (SubdocumentoSpesa) reg.getMovimento();
				System.out.println("trovata registrazione " + reg.getUid() + " in stato " + reg.getStatoOperativoRegistrazioneMovFin().getDescrizione() + " per la quota " + ss.getUid() );
				 
			}
		}
	}
	
	
}
