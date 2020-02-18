/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.capitolouscita.previsione.massiva;

import java.util.Collection;
import java.util.Date;

import it.csi.siac.siacbilser.model.CapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.model.StatoOperativoElementoDiBilancio;
import it.csi.siac.siacbilser.model.ric.RicercaSinteticaCapitoloUPrev;
import it.csi.siac.siacbilser.test.business.capitolouscita.previsione.CapitoloUscitaPrevisioneMainTest;

// TODO: Auto-generated Javadoc
/**
 * The Class CapitoloUscitaPrevisioneMassivaMainTest.
 */
public class CapitoloUscitaPrevisioneMassivaMainTest extends CapitoloUscitaPrevisioneMainTest 
{
		
	/**
	 * Spreadsheet data massiva.
	 *
	 * @param sheetName the sheet name
	 * @return the collection
	 * @throws Exception the exception
	 */
	public static  Collection<Object[]> spreadsheetDataMassiva(String sheetName) throws Exception 
	{
   		return spreadsheetData("src/test/resources/capitolouscitaprevisionemassivaparametri.xls",sheetName);
   	}

	/**
	 * Gets the criteri ricercas.
	 *
	 * @param annoEser the anno eser
	 * @param descrCapitolo the descr capitolo
	 * @return the criteri ricercas
	 */
	protected RicercaSinteticaCapitoloUPrev getCriteriRicercas(Integer annoEser, String descrCapitolo)
	{
		RicercaSinteticaCapitoloUPrev criteriRicerca = null;

		criteriRicerca = new RicercaSinteticaCapitoloUPrev();

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
	protected RicercaSinteticaCapitoloUPrev getCriteriRicercas(Integer annoEser, Integer annoCapi, 
			Integer numeCapi, Integer numeArti)
	{
		RicercaSinteticaCapitoloUPrev criteriRicerca = null;

		criteriRicerca = new RicercaSinteticaCapitoloUPrev();

		criteriRicerca.setAnnoEsercizio(annoEser);
		criteriRicerca.setAnnoCapitolo(annoCapi);
		criteriRicerca.setNumeroCapitolo(numeCapi);
		criteriRicerca.setNumeroArticolo(numeArti);
		
		return criteriRicerca;
	}

	/**
	 * Gets the capitolo u prev.
	 *
	 * @return the capitolo u prev
	 */
	protected CapitoloUscitaPrevisione getCapitoloUPrev()
	{
		CapitoloUscitaPrevisione capitoloUscitaPrevisione = null;

		capitoloUscitaPrevisione = new CapitoloUscitaPrevisione();

		capitoloUscitaPrevisione.setAnnoCapitolo(annoCapi);
		capitoloUscitaPrevisione.setNumeroCapitolo(numCapi);
		capitoloUscitaPrevisione.setNumeroArticolo(numArti);
		
		try {
			capitoloUscitaPrevisione.setStatoOperativoElementoDiBilancio(StatoOperativoElementoDiBilancio.valueOf(statoCapi));
		} catch (Exception e) {
			capitoloUscitaPrevisione.setStatoOperativoElementoDiBilancio(null);
		}

		descCapi=descCapi + " " + new Date().toString();
		capitoloUscitaPrevisione.setDescrizione(descCapi);

		capitoloUscitaPrevisione.setAnnoCreazioneCapitolo(annoCrea);

		return capitoloUscitaPrevisione;
	}
}
