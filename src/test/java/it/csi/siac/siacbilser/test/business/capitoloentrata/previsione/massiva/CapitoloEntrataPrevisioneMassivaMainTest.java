/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.capitoloentrata.previsione.massiva;

import java.util.Collection;
import java.util.Date;

import it.csi.siac.siacbilser.model.CapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.model.StatoOperativoElementoDiBilancio;
import it.csi.siac.siacbilser.model.ric.RicercaSinteticaCapitoloEPrev;
import it.csi.siac.siacbilser.test.business.capitoloentrata.previsione.CapitoloEntrataPrevisioneMainTest;

// TODO: Auto-generated Javadoc
/**
 * The Class CapitoloEntrataPrevisioneMassivaMainTest.
 */
public class CapitoloEntrataPrevisioneMassivaMainTest extends CapitoloEntrataPrevisioneMainTest 
{
			
	/**
	 * Spreadsheet data massiva.
	 *
	 * @param sheetName the sheet name
	 * @return the collection
	 * @throws Exception the exception
	 */
	public static  Collection<Object[]> spreadsheetDataMassiva(String sheetName) throws Exception {
   		return spreadsheetData("src/test/resources/capitoloentrataprevisionemassivaparametri.xls",sheetName);
   		
   	}

	/**
	 * Gets the criteri ricercas.
	 *
	 * @param annoEser the anno eser
	 * @param descrCapitolo the descr capitolo
	 * @return the criteri ricercas
	 */
	protected RicercaSinteticaCapitoloEPrev getCriteriRicercas(Integer annoEser, String descrCapitolo)
	{
		RicercaSinteticaCapitoloEPrev criteriRicerca = null;

		criteriRicerca = new RicercaSinteticaCapitoloEPrev();

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
	protected RicercaSinteticaCapitoloEPrev getCriteriRicercas(Integer annoEser, Integer annoCapi, 
			Integer numeCapi, Integer numeArti)
	{
		RicercaSinteticaCapitoloEPrev criteriRicerca = null;

		criteriRicerca = new RicercaSinteticaCapitoloEPrev();

		criteriRicerca.setAnnoEsercizio(annoEser);
		criteriRicerca.setAnnoCapitolo(annoCapi);
		criteriRicerca.setNumeroCapitolo(numeCapi);
		criteriRicerca.setNumeroArticolo(numeArti);
		
		return criteriRicerca;
	}

	/**
	 * Gets the capitolo e prev.
	 *
	 * @return the capitolo e prev
	 */
	protected CapitoloEntrataPrevisione getCapitoloEPrev()
	{
		CapitoloEntrataPrevisione capitoloEntrataPrevisione = null;

		capitoloEntrataPrevisione = new CapitoloEntrataPrevisione();

		capitoloEntrataPrevisione.setAnnoCapitolo(annoCapi);
		capitoloEntrataPrevisione.setNumeroCapitolo(numCapi);
		capitoloEntrataPrevisione.setNumeroArticolo(numArti);
		
		try {
			capitoloEntrataPrevisione.setStatoOperativoElementoDiBilancio(StatoOperativoElementoDiBilancio.valueOf(statoCapi));
		} catch (Exception e) {
			capitoloEntrataPrevisione.setStatoOperativoElementoDiBilancio(null);
		}

		descCapi=descCapi + " " + new Date().toString();
		capitoloEntrataPrevisione.setDescrizione(descCapi);

		capitoloEntrataPrevisione.setAnnoCreazioneCapitolo(annoCrea);

		return capitoloEntrataPrevisione;
	}
}
