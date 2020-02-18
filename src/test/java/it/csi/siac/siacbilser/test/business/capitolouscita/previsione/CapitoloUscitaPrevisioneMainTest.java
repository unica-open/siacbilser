/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.capitolouscita.previsione;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;

import it.csi.siac.siacbilser.frontend.webservice.CapitoloUscitaPrevisioneService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeCapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeCapitoloUscitaPrevisioneResponse;
import it.csi.siac.siacbilser.model.CapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.model.ImportiCapitoloUP;
import it.csi.siac.siacbilser.model.StatoOperativoElementoDiBilancio;
import it.csi.siac.siacbilser.model.ric.RicercaDettaglioCapitoloUPrev;
import it.csi.siac.siacbilser.model.ric.RicercaPuntualeCapitoloUPrev;
import it.csi.siac.siacbilser.model.ric.RicercaSinteticaCapitoloUPrev;
import it.csi.siac.siacbilser.test.business.capitolo.CapitoloMainTestBase;

// TODO: Auto-generated Javadoc
/**
 * The Class CapitoloUscitaPrevisioneMainTest.
 */
public class CapitoloUscitaPrevisioneMainTest extends CapitoloMainTestBase {
	
	
	
	/**
	 * Spreadsheet data.
	 *
	 * @param sheetName the sheet name
	 * @return the collection
	 * @throws Exception the exception
	 */
	public static  Collection<Object[]> spreadsheetData(String sheetName) throws Exception {
   		return spreadsheetData("src/test/resources/capitolouscitaprevisioneparametri.xls",sheetName);
   		
   	}

	

	/**
	 * Gets the criteri ricerca.
	 *
	 * @param annoEser the anno eser
	 * @param annoCapi the anno capi
	 * @param numeCapi the nume capi
	 * @param numeArti the nume arti
	 * @param numUEB the num ueb
	 * @param StatoCapi the stato capi
	 * @return the criteri ricerca
	 */
	protected RicercaPuntualeCapitoloUPrev getCriteriRicerca(Integer annoEser, Integer annoCapi, 
																Integer numeCapi, Integer numeArti, 
																Integer numUEB, String StatoCapi)
 {
		RicercaPuntualeCapitoloUPrev criteriRicerca = null;

		criteriRicerca = new RicercaPuntualeCapitoloUPrev();
		criteriRicerca.setAnnoEsercizio(annoEser);
		criteriRicerca.setAnnoCapitolo(annoCapi);
		criteriRicerca.setNumeroCapitolo(numeCapi);
		criteriRicerca.setNumeroArticolo(numeArti);
		criteriRicerca.setNumeroUEB(numUEB);

		try {
			criteriRicerca.setStatoOperativoElementoDiBilancio(StatoOperativoElementoDiBilancio.valueOf(StatoCapi));
		} catch (NullPointerException npe) {
			criteriRicerca.setStatoOperativoElementoDiBilancio(null);
		}

		return criteriRicerca;
	}

	/**
	 * Gets the criteri ricercas.
	 *
	 * @param annoEser the anno eser
	 * @param annoCapi the anno capi
	 * @param numeCapi the nume capi
	 * @param numeArti the nume arti
	 * @param numUEB the num ueb
	 * @param StatoCapi the stato capi
	 * @param descCapi the desc capi
	 * @param CodPdC the cod pd c
	 * @param CodStrutt the cod strutt
	 * @param CodTipStrutt the cod tip strutt
	 * @param CodMiss the cod miss
	 * @param CodMacro the cod macro
	 * @return the criteri ricercas
	 */
	protected RicercaSinteticaCapitoloUPrev getCriteriRicercas(Integer annoEser, Integer annoCapi, 
			Integer numeCapi, Integer numeArti, 
			Integer numUEB, String StatoCapi, 
			                                                  String descCapi, String CodPdC,
			                                                  String CodStrutt, String CodTipStrutt,
			                                                  String CodMiss, String CodMacro)
 {
		RicercaSinteticaCapitoloUPrev criteriRicerca = null;

		criteriRicerca = new RicercaSinteticaCapitoloUPrev();

		criteriRicerca.setAnnoEsercizio(annoEser);
		criteriRicerca.setAnnoCapitolo(annoCapi);
		criteriRicerca.setNumeroCapitolo(numeCapi);
		criteriRicerca.setNumeroArticolo(numeArti);
		criteriRicerca.setNumeroUEB(numUEB);

		try {
			criteriRicerca.setStatoOperativo(StatoOperativoElementoDiBilancio.valueOf(StatoCapi));
		} catch (Exception e) {
			criteriRicerca.setStatoOperativo(null);
		}

		criteriRicerca.setDescrizioneCapitolo(descCapi);
		criteriRicerca.setCodicePianoDeiConti(CodPdC);
		criteriRicerca.setCodiceStrutturaAmmCont(CodStrutt);
		criteriRicerca.setCodiceTipoStrutturaAmmCont(CodTipStrutt);
		criteriRicerca.setCodiceMissione(CodMiss);
		criteriRicerca.setCodiceTitoloUscita(CodMacro);
		return criteriRicerca;
	}

	/**
	 * Gets the criteri ricerca dettaglio.
	 *
	 * @param chiaveCapi the chiave capi
	 * @return the criteri ricerca dettaglio
	 */
	protected RicercaDettaglioCapitoloUPrev getCriteriRicercaDettaglio(Integer chiaveCapi)
 {
		RicercaDettaglioCapitoloUPrev criteriRicerca = null;

		criteriRicerca = new RicercaDettaglioCapitoloUPrev();
		try {
			criteriRicerca.setChiaveCapitolo(chiaveCapi);
		} catch (NullPointerException npe) {
			criteriRicerca.setChiaveCapitolo(0);
		}

		return criteriRicerca;
	}

	/**
	 * Gets the capitolo uscita previsione.
	 *
	 * @return the capitolo uscita previsione
	 */
	protected CapitoloUscitaPrevisione getCapitoloUscitaPrevisione(/*int annoCapi, int numCapi,
			                                                       int numArti, int numUEB, 
			                                                       String statoCapi, String descCapi,
			                                                       int annoCrea*/)
 {
		CapitoloUscitaPrevisione capitoloUscitaPrevisione = null;

		capitoloUscitaPrevisione = new CapitoloUscitaPrevisione();

		capitoloUscitaPrevisione.setAnnoCapitolo(annoCapi);
		capitoloUscitaPrevisione.setNumeroCapitolo(numCapi);
		capitoloUscitaPrevisione.setNumeroArticolo(numArti);
		capitoloUscitaPrevisione.setNumeroUEB(numUEB);

		try {
			capitoloUscitaPrevisione.setStatoOperativoElementoDiBilancio(StatoOperativoElementoDiBilancio.valueOf(statoCapi));
		} catch (Exception e) {
			capitoloUscitaPrevisione.setStatoOperativoElementoDiBilancio(null);
		}

		capitoloUscitaPrevisione.setDescrizione(descCapi + " " + new Date().toString());

		return capitoloUscitaPrevisione;
	}
	
	/**
	 * Gets the importi capitolo.
	 *
	 * @param deltaAnno the delta anno
	 * @param impStanz the imp stanz
	 * @param impCassa the imp cassa
	 * @param impResiduo the imp residuo
	 * @return the importi capitolo
	 */
	protected ImportiCapitoloUP getImportiCapitolo(Integer deltaAnno, 
			BigDecimal impStanz, BigDecimal impCassa, BigDecimal impResiduo)
	{
		
		ImportiCapitoloUP importiCapitoloUP = new ImportiCapitoloUP();
		if(annoBil!=null){
			importiCapitoloUP.setAnnoCompetenza(annoBil+deltaAnno);
		}
		importiCapitoloUP.setStanziamento(impStanz); 
		importiCapitoloUP.setStanziamentoCassa(impCassa);
		importiCapitoloUP.setStanziamentoResiduo(impResiduo);

		return importiCapitoloUP;
	}
	
	/**
	 * Ricerca puntuale.
	 *
	 * @param capitoloUscitaPrevisioneService the capitolo uscita previsione service
	 * @return the ricerca puntuale capitolo uscita previsione response
	 */
	protected RicercaPuntualeCapitoloUscitaPrevisioneResponse ricercaPuntuale(CapitoloUscitaPrevisioneService capitoloUscitaPrevisioneService) {
	       
		log.debug("Ricerco il capitolo: "+ annoEser+"|"+annoCapi+"|"+numCapi+"|"+numArti+"|"+numUEB+"|"+statoCapi);
             
		RicercaPuntualeCapitoloUPrev criteriRicerca = 
					getCriteriRicerca(annoEser, annoCapi, numCapi, numArti, numUEB, statoCapi);

		RicercaPuntualeCapitoloUscitaPrevisione req = new RicercaPuntualeCapitoloUscitaPrevisione();
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		req.setEnte(getEnteTest());
		req.setRicercaPuntualeCapitoloUPrev(criteriRicerca);
			
		RicercaPuntualeCapitoloUscitaPrevisioneResponse res = capitoloUscitaPrevisioneService
					.ricercaPuntualeCapitoloUscitaPrevisione(req);
		return res;
	}
	



}
