/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.cassaeconomale;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.business.service.cassaeconomale.AnnullaRichiestaEconomaleService;
import it.csi.siac.siacbilser.business.service.cassaeconomale.RicercaSinteticaRichiestaEconomaleService;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;
import it.csi.siac.siaccecser.frontend.webservice.msg.AnnullaRichiestaEconomale;
import it.csi.siac.siaccecser.frontend.webservice.msg.AnnullaRichiestaEconomaleResponse;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaSinteticaRichiestaEconomale;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaSinteticaRichiestaEconomaleResponse;
import it.csi.siac.siaccecser.model.CassaEconomale;
import it.csi.siac.siaccecser.model.RichiestaEconomale;
import it.csi.siac.siaccorser.model.ClassificatoreGenerico;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

// TODO: Auto-generated Javadoc
/**
 * The Class CassaEconomaleVTTest.
 */
public class RicercaRichiestaEconomaleTest extends BaseJunit4TestCase {

	@Autowired
	private RicercaSinteticaRichiestaEconomaleService ricercaSinteticaRichiestaEconomaleService;
	
	@Autowired
	private AnnullaRichiestaEconomaleService annullaRichiestaEconomaleService;

	@Test
	public void ricercaSinteticaRichiestaEconomale(){
		RicercaSinteticaRichiestaEconomale req = new RicercaSinteticaRichiestaEconomale();
		
		RichiestaEconomale richiesta = new RichiestaEconomale();
//		richiesta.setSoggetto(getSoggetto());
		richiesta.setCassaEconomale(getCassa());
//		TipoRichiestaEconomale tipoRichiestaEconomale = new TipoRichiestaEconomale();
//		tipoRichiestaEconomale.setUid(3);
//		richiesta.setTipoRichiestaEconomale(tipoRichiestaEconomale);
		
//		Sospeso s = new Sospeso();
//		s.setNumeroSospeso(8);
//		richiesta.setSospeso(s);
		
//		Movimento m = new Movimento();
//		m.setNumeroMovimento(2);
//		richiesta.setMovimento(m);
		
//		richiesta.setNumeroRichiesta(2);
		
//		richiesta.setDescrizioneDellaRichiesta("a");
		
//		richiesta.setStatoOperativoRichiestaEconomale(StatoOperativoRichiestaEconomale.PRENOTATA);
		
		ClassificatoreGenerico class1 = new ClassificatoreGenerico();
		class1.setUid(453436);
		ClassificatoreGenerico class2 = new ClassificatoreGenerico();
		class2.setUid(453440);
		ClassificatoreGenerico class3 = new ClassificatoreGenerico();
		class3.setUid(453444);
		
		List<ClassificatoreGenerico> listaClassificatori = new ArrayList<ClassificatoreGenerico>();
		listaClassificatori .add(class1);
		listaClassificatori.add(class2);
		listaClassificatori.add(class3);
		
		richiesta.setClassificatoriGenerici(listaClassificatori);
		
		
		
		

		
		req.setRichiestaEconomale(richiesta);
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		req.setParametriPaginazione(getParametriPaginazioneTest());
		
		RicercaSinteticaRichiestaEconomaleResponse res = ricercaSinteticaRichiestaEconomaleService.executeService(req);
		assertNotNull(res);
	}
	
	
	@Test
	public void annullaRichiestaEconomale(){
		AnnullaRichiestaEconomale req = new AnnullaRichiestaEconomale();
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		RichiestaEconomale richiestaEconomale = new RichiestaEconomale();
		richiestaEconomale.setUid(36);
		req.setRichiestaEconomale(richiestaEconomale );
		AnnullaRichiestaEconomaleResponse res = annullaRichiestaEconomaleService.executeService(req);
		assertNotNull(res);
	}
	

	private CassaEconomale getCassa() {
		CassaEconomale cassa = new CassaEconomale();
		cassa.setUid(8);
		return cassa;
	}

	private Impegno getImpegno() {
		Impegno impegno = new Impegno();
		impegno.setUid(1);
		return impegno;
	}

	private Soggetto getSoggetto() {
		Soggetto soggetto = new Soggetto();
		soggetto.setUid(7);
		return soggetto;
	}
	
	
}
