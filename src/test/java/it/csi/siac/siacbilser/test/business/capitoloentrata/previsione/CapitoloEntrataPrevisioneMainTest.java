/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.capitoloentrata.previsione;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;

import it.csi.siac.siacbilser.frontend.webservice.CapitoloEntrataPrevisioneService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeCapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeCapitoloEntrataPrevisioneResponse;
import it.csi.siac.siacbilser.model.CapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.model.ImportiCapitoloEP;
import it.csi.siac.siacbilser.model.StatoOperativoElementoDiBilancio;
import it.csi.siac.siacbilser.model.ric.RicercaDettaglioCapitoloEPrev;
import it.csi.siac.siacbilser.model.ric.RicercaPuntualeCapitoloEPrev;
import it.csi.siac.siacbilser.model.ric.RicercaSinteticaCapitoloEPrev;
import it.csi.siac.siacbilser.test.business.capitolo.CapitoloMainTestBase;

// TODO: Auto-generated Javadoc
/**
 * The Class CapitoloEntrataPrevisioneMainTest.
 */
public class CapitoloEntrataPrevisioneMainTest extends CapitoloMainTestBase {
	
	
	
	/**
	 * Spreadsheet data.
	 *
	 * @param sheetName the sheet name
	 * @return the collection
	 * @throws Exception the exception
	 */
	public static  Collection<Object[]> spreadsheetData(String sheetName) throws Exception {
   		return spreadsheetData("src/test/resources/capitoloentrataprevisioneparametri.xls",sheetName);
   		
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
	protected RicercaPuntualeCapitoloEPrev getCriteriRicerca(Integer annoEser, Integer annoCapi, 
																Integer numeCapi, Integer numeArti, 
																Integer numUEB, String StatoCapi)
 {
		RicercaPuntualeCapitoloEPrev criteriRicerca = null;

		criteriRicerca = new RicercaPuntualeCapitoloEPrev();
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
	protected RicercaSinteticaCapitoloEPrev getCriteriRicercas(Integer annoEser, Integer annoCapi, 
			Integer numeCapi, Integer numeArti, 
			Integer numUEB, String StatoCapi, 
			                                                  String descCapi, String CodPdC,
			                                                  String CodStrutt, String CodTipStrutt,
			                                                  String CodMiss, String CodMacro)
 {
		RicercaSinteticaCapitoloEPrev criteriRicerca = null;

		criteriRicerca = new RicercaSinteticaCapitoloEPrev();

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
		//criteriRicerca.setCodiceMissione(CodMiss);
		//criteriRicerca.setCodiceTitoloEntrata(CodMacro);
//		criteriRicerca.setCodiceCategoria(codiceCategoria)
//		criteriRicerca.setCodiceTipologia(codiceTipologia)
		return criteriRicerca;
	}

	/**
	 * Gets the criteri ricerca dettaglio.
	 *
	 * @param chiaveCapi the chiave capi
	 * @return the criteri ricerca dettaglio
	 */
	protected RicercaDettaglioCapitoloEPrev getCriteriRicercaDettaglio(Integer chiaveCapi)
 {
		RicercaDettaglioCapitoloEPrev criteriRicerca = null;

		criteriRicerca = new RicercaDettaglioCapitoloEPrev();
		try {
			criteriRicerca.setChiaveCapitolo(chiaveCapi);
		} catch (NullPointerException npe) {
			criteriRicerca.setChiaveCapitolo(0);
		}

		return criteriRicerca;
	}

	/**
	 * Gets the capitolo entrata previsione.
	 *
	 * @return the capitolo entrata previsione
	 */
	protected CapitoloEntrataPrevisione getCapitoloEntrataPrevisione(/*int annoCapi, int numCapi,
			                                                       int numArti, int numUEB, 
			                                                       String statoCapi, String descCapi,
			                                                       int annoCrea*/)
 {
		CapitoloEntrataPrevisione capitoloEntrataPrevisione = null;

		capitoloEntrataPrevisione = new CapitoloEntrataPrevisione();

		capitoloEntrataPrevisione.setAnnoCapitolo(annoCapi);
		capitoloEntrataPrevisione.setNumeroCapitolo(numCapi);
		capitoloEntrataPrevisione.setNumeroArticolo(numArti);
		capitoloEntrataPrevisione.setNumeroUEB(numUEB);

		try {
			capitoloEntrataPrevisione.setStatoOperativoElementoDiBilancio(StatoOperativoElementoDiBilancio.valueOf(statoCapi));
		} catch (Exception e) {
			capitoloEntrataPrevisione.setStatoOperativoElementoDiBilancio(null);
		}

		capitoloEntrataPrevisione.setDescrizione(descCapi + " " + new Date().toString());

		return capitoloEntrataPrevisione;
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
	protected ImportiCapitoloEP getImportiCapitolo(Integer deltaAnno, 
			BigDecimal impStanz, BigDecimal impCassa, BigDecimal impResiduo)
	{
		
		ImportiCapitoloEP importiCapitoloEP = new ImportiCapitoloEP();
		if(annoBil!=null){
			importiCapitoloEP.setAnnoCompetenza(annoBil+deltaAnno);
		}
		importiCapitoloEP.setStanziamento(impStanz); 
		importiCapitoloEP.setStanziamentoCassa(impCassa);
		importiCapitoloEP.setStanziamentoResiduo(impResiduo);

		return importiCapitoloEP;
	}
	
	/**
	 * Ricerca puntuale.
	 *
	 * @param capitoloEntrataPrevisioneService the capitolo entrata previsione service
	 * @return the ricerca puntuale capitolo entrata previsione response
	 */
	protected RicercaPuntualeCapitoloEntrataPrevisioneResponse ricercaPuntuale(CapitoloEntrataPrevisioneService capitoloEntrataPrevisioneService) {
	       
		log.debug("Ricerco il capitolo: "+ annoEser+"|"+annoCapi+"|"+numCapi+"|"+numArti+"|"+numUEB+"|"+statoCapi);
             
		RicercaPuntualeCapitoloEPrev criteriRicerca = 
					getCriteriRicerca(annoEser, annoCapi, numCapi, numArti, numUEB, statoCapi);

		RicercaPuntualeCapitoloEntrataPrevisione req = new RicercaPuntualeCapitoloEntrataPrevisione();
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		req.setEnte(getEnteTest());
		req.setRicercaPuntualeCapitoloEPrev(criteriRicerca);
			
		RicercaPuntualeCapitoloEntrataPrevisioneResponse res = capitoloEntrataPrevisioneService
					.ricercaPuntualeCapitoloEntrataPrevisione(req);
		return res;
	}
	



}
