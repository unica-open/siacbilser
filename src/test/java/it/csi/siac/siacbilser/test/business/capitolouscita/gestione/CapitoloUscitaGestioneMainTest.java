/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.capitolouscita.gestione;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;

import it.csi.siac.siacbilser.frontend.webservice.CapitoloUscitaGestioneService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeCapitoloUscitaGestioneResponse;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.ImportiCapitoloUG;
import it.csi.siac.siacbilser.model.StatoOperativoElementoDiBilancio;
import it.csi.siac.siacbilser.model.ric.RicercaDettaglioCapitoloUGest;
import it.csi.siac.siacbilser.model.ric.RicercaPuntualeCapitoloUGest;
import it.csi.siac.siacbilser.model.ric.RicercaSinteticaCapitoloUGest;
import it.csi.siac.siacbilser.test.business.capitolo.CapitoloMainTestBase;

// TODO: Auto-generated Javadoc
/**
 * The Class CapitoloUscitaGestioneMainTest.
 */
public class CapitoloUscitaGestioneMainTest extends CapitoloMainTestBase
{
	
		
	
 	/**
	  * Spreadsheet data.
	  *
	  * @param sheetName the sheet name
	  * @return the collection
	  * @throws Exception the exception
	  */
	 public static  Collection<Object[]> spreadsheetData(String sheetName) throws Exception {
   		return spreadsheetData("src/test/resources/capitolouscitagestioneparametri.xls",sheetName);
   	}
	
	
	/**
	 * Ricerca puntuale.
	 *
	 * @param capitoloUscitaGestioneService the capitolo uscita gestione service
	 * @return the ricerca puntuale capitolo uscita gestione response
	 */
	protected RicercaPuntualeCapitoloUscitaGestioneResponse ricercaPuntuale(CapitoloUscitaGestioneService capitoloUscitaGestioneService) {
	       
		log.debug("Ricerco il capitolo: "+ annoEser+"|"+annoCapi+"|"+numCapi+"|"+numArti+"|"+numUEB+"|"+statoCapi);
             
		RicercaPuntualeCapitoloUGest criteriRicerca = 
					getCriteriRicerca(annoEser, annoCapi, numCapi, numArti, numUEB, statoCapi);

		RicercaPuntualeCapitoloUscitaGestione req = new RicercaPuntualeCapitoloUscitaGestione();
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		req.setEnte(getEnteTest());
		req.setRicercaPuntualeCapitoloUGest(criteriRicerca);
			
		RicercaPuntualeCapitoloUscitaGestioneResponse res = capitoloUscitaGestioneService
					.ricercaPuntualeCapitoloUscitaGestione(req);
		return res;
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
	protected ImportiCapitoloUG getImportiCapitolo(Integer deltaAnno, 
			BigDecimal impStanz, BigDecimal impCassa, BigDecimal impResiduo)
	{
		
		ImportiCapitoloUG importiCapitoloUG = new ImportiCapitoloUG();
		if(annoBil!=null){
			importiCapitoloUG.setAnnoCompetenza(annoBil+deltaAnno);
		}
		importiCapitoloUG.setStanziamento(impStanz); 
		importiCapitoloUG.setStanziamentoCassa(impCassa);
		importiCapitoloUG.setStanziamentoResiduo(impResiduo);

		return importiCapitoloUG;
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
	protected RicercaPuntualeCapitoloUGest getCriteriRicerca(Integer annoEser, Integer annoCapi, Integer numeCapi, Integer numeArti, Integer numUEB,
			String StatoCapi) {
		RicercaPuntualeCapitoloUGest criteriRicerca = null;
		criteriRicerca = new RicercaPuntualeCapitoloUGest();
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
	protected RicercaSinteticaCapitoloUGest getCriteriRicercas(Integer annoEser, Integer annoCapi, Integer numeCapi, Integer numeArti,
			Integer numUEB, String StatoCapi, String descCapi, String CodPdC, String CodStrutt, String CodTipStrutt, String CodMiss, String CodMacro) {
		RicercaSinteticaCapitoloUGest criteriRicerca = null;

		// if (annoEser > 0 || annoCapi > 0 || numeCapi > 0 || numeArti <
		// 999999999 || StatoCapi.length() > 3)
		// {
		criteriRicerca = new RicercaSinteticaCapitoloUGest();

		// if (annoEser > 0)
		criteriRicerca.setAnnoEsercizio(annoEser);
		// if (annoCapi > 0)
		criteriRicerca.setAnnoCapitolo(annoCapi);
		// if (numeCapi > 0)
		criteriRicerca.setNumeroCapitolo(numeCapi);
		// if (numeArti < 999999999)
		criteriRicerca.setNumeroArticolo(numeArti);
		criteriRicerca.setNumeroUEB(numUEB);

		// if (StatoCapi.length() > 3)
		// {
		// criteriRicerca.setStatoOperativo(StatoOperativoElementoDiBilancio.valueOf(StatoCapi));

		try {
			criteriRicerca.setStatoOperativo(StatoOperativoElementoDiBilancio.valueOf(StatoCapi));
		} catch (Exception e) {
			criteriRicerca.setStatoOperativo(null);
		}
		// }

		// if (descCapi.length() > 3)
		criteriRicerca.setDescrizioneCapitolo(descCapi);
		// if (CodPdC.length() > 0)
		criteriRicerca.setCodicePianoDeiConti(CodPdC);
		// if (CodStrutt.length() > 0)
		criteriRicerca.setCodiceStrutturaAmmCont(CodStrutt);
		// if (CodTipStrutt.length() > 0)
		criteriRicerca.setCodiceTipoStrutturaAmmCont(CodTipStrutt);
		// if (CodMiss.length() > 0)
		criteriRicerca.setCodiceMissione(CodMiss);
		// if (CodMacro.length() > 0)
		criteriRicerca.setCodiceTitoloUscita(CodMacro);
		// }
		return criteriRicerca;
	}

	/**
	 * Gets the criteri ricerca dettaglio.
	 *
	 * @param chiaveCapi the chiave capi
	 * @return the criteri ricerca dettaglio
	 */
	protected RicercaDettaglioCapitoloUGest getCriteriRicercaDettaglio(Integer chiaveCapi) {
		RicercaDettaglioCapitoloUGest criteriRicerca = null;

		
		criteriRicerca = new RicercaDettaglioCapitoloUGest();
		try {
			criteriRicerca.setChiaveCapitolo(chiaveCapi);
		} catch (NullPointerException npe) {
			criteriRicerca.setChiaveCapitolo(0);
		}

		return criteriRicerca;
	}

	/**
	 * Gets the capitolo uscita gestione.
	 *
	 * @return the capitolo uscita gestione
	 */
	protected CapitoloUscitaGestione getCapitoloUscitaGestione() {
		CapitoloUscitaGestione capitoloUscitaGestione = null;

		capitoloUscitaGestione = new CapitoloUscitaGestione();

		capitoloUscitaGestione.setAnnoCapitolo(annoCapi);
		capitoloUscitaGestione.setNumeroCapitolo(numCapi);
		capitoloUscitaGestione.setNumeroArticolo(numArti);
		capitoloUscitaGestione.setNumeroUEB(numUEB);
		
		try {
			capitoloUscitaGestione.setStatoOperativoElementoDiBilancio(StatoOperativoElementoDiBilancio.valueOf(statoCapi));
		} catch (Exception e) {
			capitoloUscitaGestione.setStatoOperativoElementoDiBilancio(null);
		}
		capitoloUscitaGestione.setDescrizione(descCapi + " " + new Date().toString());		

		return capitoloUscitaGestione;
	}

	

}
