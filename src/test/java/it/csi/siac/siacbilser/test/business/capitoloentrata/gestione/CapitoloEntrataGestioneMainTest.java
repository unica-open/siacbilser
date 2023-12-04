/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.capitoloentrata.gestione;

import java.math.BigDecimal;
import java.util.Collection;

import it.csi.siac.siacbilser.frontend.webservice.CapitoloEntrataGestioneService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeCapitoloEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeCapitoloEntrataGestioneResponse;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.ImportiCapitoloEG;
import it.csi.siac.siacbilser.model.StatoOperativoElementoDiBilancio;
import it.csi.siac.siacbilser.model.ric.RicercaDettaglioCapitoloEGest;
import it.csi.siac.siacbilser.model.ric.RicercaPuntualeCapitoloEGest;
import it.csi.siac.siacbilser.model.ric.RicercaSinteticaCapitoloEGest;
import it.csi.siac.siacbilser.test.business.capitolo.CapitoloMainTestBase;

// TODO: Auto-generated Javadoc
/**
 * The Class CapitoloEntrataGestioneMainTest.
 */
public class CapitoloEntrataGestioneMainTest extends CapitoloMainTestBase {
	
	
	
	/**
	 * Spreadsheet data.
	 *
	 * @param sheetName the sheet name
	 * @return the collection
	 * @throws Exception the exception
	 */
	public static  Collection<Object[]> spreadsheetData(String sheetName) throws Exception {
   		return spreadsheetData("src/test/resources/capitoloentratagestioneparametri.xls",sheetName);
   		
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
	protected RicercaPuntualeCapitoloEGest getCriteriRicerca(Integer annoEser, Integer annoCapi, 
																Integer numeCapi, Integer numeArti, 
																Integer numUEB, String StatoCapi)
 {
		RicercaPuntualeCapitoloEGest criteriRicerca = null;

		criteriRicerca = new RicercaPuntualeCapitoloEGest();
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
	protected RicercaSinteticaCapitoloEGest getCriteriRicercas(Integer annoEser, Integer annoCapi, 
			Integer numeCapi, Integer numeArti, 
			Integer numUEB, String StatoCapi, 
			                                                  String descCapi, String CodPdC,
			                                                  String CodStrutt, String CodTipStrutt,
			                                                  String CodMiss, String CodMacro)
 {
		RicercaSinteticaCapitoloEGest criteriRicerca = null;

		criteriRicerca = new RicercaSinteticaCapitoloEGest();

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
	protected RicercaDettaglioCapitoloEGest getCriteriRicercaDettaglio(Integer chiaveCapi)
 {
		RicercaDettaglioCapitoloEGest criteriRicerca = null;

		criteriRicerca = new RicercaDettaglioCapitoloEGest();
		try {
			criteriRicerca.setChiaveCapitolo(chiaveCapi);
		} catch (NullPointerException npe) {
			criteriRicerca.setChiaveCapitolo(0);
		}

		return criteriRicerca;
	}

	/**
	 * Gets the capitolo entrata gestione.
	 *
	 * @return the capitolo entrata gestione
	 */
	protected CapitoloEntrataGestione getCapitoloEntrataGestione(/*int annoCapi, int numCapi,
			                                                       int numArti, int numUEB, 
			                                                       String statoCapi, String descCapi,
			                                                       int annoCrea*/)
 {
		CapitoloEntrataGestione capitoloEntrataGestione = null;

		capitoloEntrataGestione = new CapitoloEntrataGestione();
		capitoloEntrataGestione.setUid(31036);
		
		capitoloEntrataGestione.setAnnoCapitolo(2015);
		capitoloEntrataGestione.setNumeroCapitolo(3355);
		capitoloEntrataGestione.setNumeroArticolo(1);
		capitoloEntrataGestione.setNumeroUEB(1);

		try {
			capitoloEntrataGestione.setStatoOperativoElementoDiBilancio(StatoOperativoElementoDiBilancio.VALIDO);
		} catch (Exception e) {
			capitoloEntrataGestione.setStatoOperativoElementoDiBilancio(null);
		}

		//capitoloEntrataGestione.setDescrizione(descCapi + " " + new Date().toString());

		return capitoloEntrataGestione;
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
	protected ImportiCapitoloEG getImportiCapitolo(Integer deltaAnno, 
			BigDecimal impStanz, BigDecimal impCassa, BigDecimal impResiduo)
	{
		
		ImportiCapitoloEG importiCapitoloEG = new ImportiCapitoloEG();
		if(annoBil!=null){
			importiCapitoloEG.setAnnoCompetenza(annoBil+deltaAnno);
		}
		importiCapitoloEG.setStanziamento(impStanz); 
		importiCapitoloEG.setStanziamentoCassa(impCassa);
		importiCapitoloEG.setStanziamentoResiduo(impResiduo);

		return importiCapitoloEG;
	}
	
	/**
	 * Ricerca puntuale.
	 *
	 * @param capitoloEntrataGestioneService the capitolo entrata gestione service
	 * @return the ricerca puntuale capitolo entrata gestione response
	 */
	protected RicercaPuntualeCapitoloEntrataGestioneResponse ricercaPuntuale(CapitoloEntrataGestioneService capitoloEntrataGestioneService) {
	       
		log.debug("ricercaPuntuale", "Ricerco il capitolo: "+ annoEser+"|"+annoCapi+"|"+numCapi+"|"+numArti+"|"+numUEB+"|"+statoCapi);
             
		RicercaPuntualeCapitoloEGest criteriRicerca = 
					getCriteriRicerca(annoEser, annoCapi, numCapi, numArti, numUEB, statoCapi);

		RicercaPuntualeCapitoloEntrataGestione req = new RicercaPuntualeCapitoloEntrataGestione();
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		req.setEnte(getEnteTest());
		req.setRicercaPuntualeCapitoloEGest(criteriRicerca);
			
		RicercaPuntualeCapitoloEntrataGestioneResponse res = capitoloEntrataGestioneService
					.ricercaPuntualeCapitoloEntrataGestione(req);
		return res;
	}
	



}
