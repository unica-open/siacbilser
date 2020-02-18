/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.capitoloentrata.gestione.massiva;

import java.util.Collection;
import java.util.Date;

import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.StatoOperativoElementoDiBilancio;
import it.csi.siac.siacbilser.model.ric.RicercaSinteticaCapitoloEGest;
import it.csi.siac.siacbilser.test.business.capitoloentrata.gestione.CapitoloEntrataGestioneMainTest;

// TODO: Auto-generated Javadoc
/**
 * The Class CapitoloEntrataGestioneMassivaMainTest.
 */
public class CapitoloEntrataGestioneMassivaMainTest extends CapitoloEntrataGestioneMainTest 
{
		
	/**
	 * Spreadsheet data massiva.
	 *
	 * @param sheetName the sheet name
	 * @return the collection
	 * @throws Exception the exception
	 */
	public static  Collection<Object[]> spreadsheetDataMassiva(String sheetName) throws Exception {
   		return spreadsheetData("src/test/resources/capitoloentratagestionemassivaparametri.xls",sheetName);
   	}

	/**
	 * Gets the criteri ricercas.
	 *
	 * @param annoEser the anno eser
	 * @param descrCapitolo the descr capitolo
	 * @return the criteri ricercas
	 */
	protected RicercaSinteticaCapitoloEGest getCriteriRicercas(Integer annoEser, String descrCapitolo)
	{
		RicercaSinteticaCapitoloEGest criteriRicerca = null;

		criteriRicerca = new RicercaSinteticaCapitoloEGest();

		criteriRicerca.setAnnoEsercizio(annoEser);
		criteriRicerca.setDescrizioneCapitolo(descrCapitolo);
		
		return criteriRicerca;
	}

	/**
	 * Gets the criteri ricercas.
	 *
	 * @param annoEser the anno eser
	 * @param annoCapi the anno capi
	 * @param numeCapi the nume capi
	 * @param numeArti the nume arti
	 * @return the criteri ricercas
	 */
	protected RicercaSinteticaCapitoloEGest getCriteriRicercas(Integer annoEser, Integer annoCapi, 
			Integer numeCapi, Integer numeArti)
	{
		RicercaSinteticaCapitoloEGest criteriRicerca = null;

		criteriRicerca = new RicercaSinteticaCapitoloEGest();

		criteriRicerca.setAnnoEsercizio(annoEser);
		criteriRicerca.setAnnoCapitolo(annoCapi);
		criteriRicerca.setNumeroCapitolo(numeCapi);
		criteriRicerca.setNumeroArticolo(numeArti);
		
		return criteriRicerca;
	}

	/**
	 * Gets the capitolo e gest.
	 *
	 * @return the capitolo e gest
	 */
	protected CapitoloEntrataGestione getCapitoloEGest()
	{
		CapitoloEntrataGestione capitoloEntrataGestione = null;
		
		if (annoCapi != null || numCapi != null || numArti != null || statoCapi != null)
		{
			capitoloEntrataGestione = new CapitoloEntrataGestione();

			capitoloEntrataGestione.setAnnoCapitolo(annoCapi);
			capitoloEntrataGestione.setNumeroCapitolo(numCapi);
			capitoloEntrataGestione.setNumeroArticolo(numArti);
		
			try {
				capitoloEntrataGestione.setStatoOperativoElementoDiBilancio(StatoOperativoElementoDiBilancio.valueOf(statoCapi));
			} catch (Exception e) {
				capitoloEntrataGestione.setStatoOperativoElementoDiBilancio(null);
			}

			descCapi=descCapi + " " + new Date().toString();
			capitoloEntrataGestione.setDescrizione(descCapi);

			capitoloEntrataGestione.setAnnoCreazioneCapitolo(annoCrea);
		}
		
		return capitoloEntrataGestione;
	}
}
