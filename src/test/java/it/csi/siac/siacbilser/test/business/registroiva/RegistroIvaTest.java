/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.registroiva;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.business.service.registroiva.AggiornaRegistroIvaService;
import it.csi.siac.siacbilser.business.service.registroiva.InserisceRegistroIvaService;
import it.csi.siac.siacbilser.business.service.registroiva.RicercaDettaglioRegistroIvaService;
import it.csi.siac.siacbilser.business.service.registroiva.RicercaSinteticaRegistroIvaService;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaRegistroIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaRegistroIvaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceRegistroIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceRegistroIvaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioRegistroIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioRegistroIvaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaRegistroIva;
import it.csi.siac.siacfin2ser.model.GruppoAttivitaIva;
import it.csi.siac.siacfin2ser.model.RegistroIva;
import it.csi.siac.siacfin2ser.model.TipoRegistroIva;

// TODO: Auto-generated Javadoc
/**
 * The Class RegistroIvaVTTest.
 */
public class RegistroIvaTest extends BaseJunit4TestCase {
	
	
	/** The inserisce registro iva service. */
	@Autowired
	private InserisceRegistroIvaService inserisceRegistroIvaService;
	
	/** The aggiorna registro iva service. */
	@Autowired
	private AggiornaRegistroIvaService aggiornaRegistroIvaService;
	
	/** The ricerca sintetica registro iva service. */
	@Autowired
	private RicercaSinteticaRegistroIvaService ricercaSinteticaRegistroIvaService;
	
	@Autowired
	private RicercaDettaglioRegistroIvaService ricercaDettaglioRegistroIvaService;
	
	/**
	 * Inserisci registro iva.
	 */
	@Test
	public void inserisciRegistroIva() {
			
		InserisceRegistroIva req = new InserisceRegistroIva();
		
		//req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		
		RegistroIva registro= new RegistroIva();
		
		registro.setCodice("SIAC-6276-3");
		registro.setDescrizione("SIAC-6276");
		registro.setFlagLiquidazioneIva(Boolean.FALSE);
		//	TipoRegistroIva tipoRegistro = new TipoRegistroIva();
		//	tipoRegistro.setCodice("AD");
		//	tipoRegistro.setDescrizione("ACQUISTA IVA DIFFERITA");
		//	tipoRegistro.setTipoEsigibilitaIva(TipoEsigibilitaIva.DIFFERITA);
		//	tipoRegistro.setEnte(getEnteTest());
		//	tipoRegistro.setUid(1);
		registro.setTipoRegistroIva(TipoRegistroIva.ACQUISTI_IVA_DIFFERITA);
		registro.setEnte(req.getRichiedente().getAccount().getEnte());
		
		GruppoAttivitaIva gruppo = new GruppoAttivitaIva();
		gruppo.setUid(125);
		registro.setGruppoAttivitaIva(gruppo);
		
		req.setRegistroIva(registro);
		
		InserisceRegistroIvaResponse res = inserisceRegistroIvaService.executeService(req);
	
		assertNotNull(res);
	}
	
	/**
	 * Ricerca sintetica registro iva.
	 */
	@Test
	public void ricercaSinteticaRegistroIva(){
		
		RicercaSinteticaRegistroIva req = new RicercaSinteticaRegistroIva();
		//req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		
		req.setParametriPaginazione(getParametriPaginazioneTest());
		RegistroIva registroIva = new RegistroIva();
		registroIva.setCodice("SIAC-6276-2");
		//registroIva.setEnte(getEnteTest());
		GruppoAttivitaIva gruppo = new GruppoAttivitaIva();
		gruppo.setUid(125);
		registroIva.setGruppoAttivitaIva(gruppo);
		registroIva.setEnte(req.getRichiedente().getAccount().getEnte());
		req.setRegistroIva(registroIva);
		ricercaSinteticaRegistroIvaService.executeService(req);
	}
	
	/**
	 * Ricerca dettaglio registro iva.
	 */
	@Test
	public void ricercaDettaglioRegistroIva(){
		
		RicercaDettaglioRegistroIva req = new RicercaDettaglioRegistroIva();
		//req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		
		RegistroIva registroIva = new RegistroIva();
		registroIva.setCodice("SIAC-6276-2");
		registroIva.setUid(29);
		//registroIva.setEnte(getEnteTest());
		registroIva.setEnte(req.getRichiedente().getAccount().getEnte());
		/*GruppoAttivitaIva gruppo = new GruppoAttivitaIva();
		gruppo.setUid(125);
		registroIva.setGruppoAttivitaIva(gruppo);*/
		req.setRegistroIva(registroIva);
		ricercaDettaglioRegistroIvaService.executeService(req);
	}
	
	/**
	 * Aggiorna registro iva.
	 */
	@Test
	public void aggiornaRegistroIva() {
		
		/*RicercaSinteticaRegistroIva reqRic = new RicercaSinteticaRegistroIva();
		//req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		reqRic.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		
		reqRic.setParametriPaginazione(getParametriPaginazioneTest());
		RegistroIva registroIva = new RegistroIva();
		registroIva.setCodice("SIAC-6276-2");
		registroIva.setEnte(reqRic.getRichiedente().getAccount().getEnte());
		reqRic.setRegistroIva(registroIva);
		RicercaSinteticaRegistroIvaResponse resRic = ricercaSinteticaRegistroIvaService.executeService(reqRic);
		
		ListaPaginata<RegistroIva> resRicListaPaginata = resRic.getListaRegistroIva();
		
		RegistroIva registro = resRicListaPaginata.get(0);*/
		
		RicercaDettaglioRegistroIva reqRic = new RicercaDettaglioRegistroIva();
		//req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		reqRic.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		
		RegistroIva registroIva = new RegistroIva();
		registroIva.setCodice("SIAC-6276-2");
		registroIva.setUid(29);
		registroIva.setEnte(reqRic.getRichiedente().getAccount().getEnte());
		reqRic.setRegistroIva(registroIva);
		RicercaDettaglioRegistroIvaResponse resRic = ricercaDettaglioRegistroIvaService.executeService(reqRic);
		
		RegistroIva registro = resRic.getRegistroIva();
		
		
		AggiornaRegistroIva reqAgg = new AggiornaRegistroIva();
		
		//req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		reqAgg.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		
		//RegistroIva registro= new RegistroIva();
		
//		registro.setUid(1);
//		registro.setCodice("codice registro agg");
		registro.setDescrizione("SIAC-6276 agg");
//		registro.setTipoRegistroIva(TipoRegistroIva.VENDITE_IVA_IMMEDIATA);
//		registro.setEnte(getEnteTest());
		registro.setEnte(reqAgg.getRichiedente().getAccount().getEnte());
		
		GruppoAttivitaIva gruppo = new GruppoAttivitaIva();
		gruppo.setUid(125);
		registro.setGruppoAttivitaIva(gruppo);
		
		reqAgg.setRegistroIva(registro);
		
		AggiornaRegistroIvaResponse resAgg = aggiornaRegistroIvaService.executeService(reqAgg);

		assertNotNull(resAgg);
	}
	
		
}