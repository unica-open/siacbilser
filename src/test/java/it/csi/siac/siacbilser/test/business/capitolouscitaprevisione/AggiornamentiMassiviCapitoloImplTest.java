/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.capitolouscitaprevisione;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.business.service.capitolouscitaprevisione.AggiornamentoMassivoCapitoloUscitaPrevisioneService;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaMassivoCapitoloDiUscitaPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaMassivoCapitoloDiUscitaPrevisioneResponse;
import it.csi.siac.siacbilser.model.CapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.model.ClassificazioneCofogProgramma;
import it.csi.siac.siacbilser.model.ElementoPianoDeiConti;
import it.csi.siac.siacbilser.model.ImportiCapitoloUP;
import it.csi.siac.siacbilser.model.Macroaggregato;
import it.csi.siac.siacbilser.model.Programma;
import it.csi.siac.siacbilser.model.StatoOperativoElementoDiBilancio;
import it.csi.siac.siacbilser.model.TipoFinanziamento;
import it.csi.siac.siacbilser.model.TipoFondo;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.ClassificatoreGenerico;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;

// TODO: Auto-generated Javadoc
/**
 * The Class AggiornamentiMassiviCapitoloImplDLTest.
 */
public class AggiornamentiMassiviCapitoloImplTest extends BaseJunit4TestCase
{
	
	/** The agg uscita previsione. */
	@Autowired
	private AggiornamentoMassivoCapitoloUscitaPrevisioneService aggUscitaPrevisione;
	
	
	/**
	 * Test update massivo uscita previsione.
	 */
	@Test
	public void testUpdateMassivoUscitaPrevisione () {
		
		Bilancio bilancio = getBilancioTest();

		CapitoloUscitaPrevisione capitoloUscitaPrevisione = new CapitoloUscitaPrevisione();
		
		capitoloUscitaPrevisione.setNumeroCapitolo(9999);
		capitoloUscitaPrevisione.setNumeroArticolo(1);
//		capitoloUscitaPrevisione.setNumeroUEB(1);
		capitoloUscitaPrevisione.setAnnoCapitolo(bilancio.getAnno());
		capitoloUscitaPrevisione.setStatoOperativoElementoDiBilancio(StatoOperativoElementoDiBilancio.VALIDO);
		capitoloUscitaPrevisione.setDescrizione("MIO TESTò CUP " + new Date().toString() +"!!!");
		capitoloUscitaPrevisione.setAnnoCreazioneCapitolo(2013);
		
		capitoloUscitaPrevisione.setFlagFondoPluriennaleVinc(Boolean.TRUE);
		capitoloUscitaPrevisione.setFlagAssegnabile(Boolean.TRUE);

		Ente ente = getEnteTest();

		Richiedente richiedente = getRichiedenteByProperties("consip","regp");

		ElementoPianoDeiConti elementoPianoDeiConti = new ElementoPianoDeiConti();
		elementoPianoDeiConti.setUid(5255);

		Macroaggregato macroaggregato = new Macroaggregato();
		macroaggregato.setUid(124);

		Programma programma = new Programma();
		programma.setUid(25); //era 24
		//programma.setDataFineValidita(new Date());

		StrutturaAmministrativoContabile strutturaAmministrativoContabile = new StrutturaAmministrativoContabile();
		strutturaAmministrativoContabile.setUid(345);

		ClassificazioneCofogProgramma classificazioneCofogProgramma = new ClassificazioneCofogProgramma();
		classificazioneCofogProgramma.setUid(5245);

		TipoFondo tipoFondo = null;
		TipoFinanziamento tipoFinanziamento = null;
		List<ClassificatoreGenerico> listaClassificatori = null;
/*
		List<ImportiCapitoloUP> listaImporti = new ArrayList<ImportiCapitoloUP>();

		listaImporti.add(getImportiCapitolo(2013, new BigDecimal(2013)));
		listaImporti.add(getImportiCapitolo(2014, new BigDecimal(2014)));
		listaImporti.add(getImportiCapitolo(2015, new BigDecimal(2015)));			
		
		*/
		AggiornaMassivoCapitoloDiUscitaPrevisione req = new AggiornaMassivoCapitoloDiUscitaPrevisione();
		req.setRichiedente(richiedente);
		req.setEnte(ente);
		req.setBilancio(bilancio);
		req.setCapitoloUscitaPrevisione(capitoloUscitaPrevisione);
		req.setTipoFondo(tipoFondo);
		req.setTipoFinanziamento(tipoFinanziamento);
		req.setClassificatoriGenerici(listaClassificatori);
		req.setElementoPianoDeiConti(elementoPianoDeiConti);
		req.setStruttAmmContabile(strutturaAmministrativoContabile);
		req.setClassificazioneCofogProgramma(classificazioneCofogProgramma);
//		req.setImportiCapitoloUP(listaImporti);
		req.setMacroaggregato(macroaggregato);
		req.setProgramma(programma);
		
		
		AggiornaMassivoCapitoloDiUscitaPrevisioneResponse res = aggUscitaPrevisione.executeService(req);
		
		assertNotNull(res);
		
		System.out.println("esito: "+res.getEsito());
		System.out.println("errori: "+res.getErrori());
		
		
		
		
		CapitoloUscitaPrevisione ins = res.getCapitoloUscitaPrevisione();
		
		//assertNotNull(ins);			

		System.out.println("inserito id = " + ins.getUid());
		
	}
	
	

	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.test.BaseJunit4TestCase#getBilancioTest()
	 */
	protected Bilancio getBilancioTest()
	{
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

	/**
	 * Gets the capitolo u prev.
	 *
	 * @return the capitolo u prev
	 */
	protected CapitoloUscitaPrevisione getCapitoloUPrev() {
		CapitoloUscitaPrevisione getCapitoloUPrev = new CapitoloUscitaPrevisione();
		getCapitoloUPrev.setAnnoCapitolo(2013);
		getCapitoloUPrev.setNumeroCapitolo(1);
		return getCapitoloUPrev;
	}

	/**
	 * Gets the importi capitolo.
	 *
	 * @param anno the anno
	 * @param importo the importo
	 * @return the importi capitolo
	 */
	protected ImportiCapitoloUP getImportiCapitolo(Integer anno, BigDecimal importo)
	{
		ImportiCapitoloUP importiCapitoloUP = new ImportiCapitoloUP();

		importiCapitoloUP.setAnnoCompetenza(anno);
		importiCapitoloUP.setStanziamento(importo.add(new BigDecimal("0.1"))); 
		importiCapitoloUP.setStanziamentoCassa(importo.add(new BigDecimal("20000.2")));
		importiCapitoloUP.setStanziamentoResiduo(importo.add(new BigDecimal("10000.4")));

		return importiCapitoloUP;
	}
	
	
	
}
