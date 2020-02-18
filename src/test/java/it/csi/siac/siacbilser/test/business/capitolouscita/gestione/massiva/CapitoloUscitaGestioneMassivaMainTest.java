/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.capitolouscita.gestione.massiva;

import java.util.Collection;
import java.util.Date;

import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.StatoOperativoElementoDiBilancio;
import it.csi.siac.siacbilser.model.ric.RicercaSinteticaCapitoloUGest;
import it.csi.siac.siacbilser.test.business.capitolouscita.gestione.CapitoloUscitaGestioneMainTest;

// TODO: Auto-generated Javadoc
/**
 * The Class CapitoloUscitaGestioneMassivaMainTest.
 */
public class CapitoloUscitaGestioneMassivaMainTest extends CapitoloUscitaGestioneMainTest 
{
	
	/**
	 * Spreadsheet data massiva.
	 *
	 * @param sheetName the sheet name
	 * @return the collection
	 * @throws Exception the exception
	 */
	public static  Collection<Object[]> spreadsheetDataMassiva(String sheetName) throws Exception {
   		return spreadsheetData("src/test/resources/capitolouscitagestionemassivaparametri.xls",sheetName);
   	}

	/**
	 * Gets the criteri ricercas.
	 *
	 * @param annoEser the anno eser
	 * @param descrCapitolo the descr capitolo
	 * @return the criteri ricercas
	 */
	protected RicercaSinteticaCapitoloUGest getCriteriRicercas(Integer annoEser, String descrCapitolo)
	{
		RicercaSinteticaCapitoloUGest criteriRicerca = null;

		criteriRicerca = new RicercaSinteticaCapitoloUGest();

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
	protected RicercaSinteticaCapitoloUGest getCriteriRicercas(Integer annoEser, Integer annoCapi, 
			Integer numeCapi, Integer numeArti)
	{
		RicercaSinteticaCapitoloUGest criteriRicerca = null;

		criteriRicerca = new RicercaSinteticaCapitoloUGest();

		criteriRicerca.setAnnoEsercizio(annoEser);
		criteriRicerca.setAnnoCapitolo(annoCapi);
		criteriRicerca.setNumeroCapitolo(numeCapi);
		criteriRicerca.setNumeroArticolo(numeArti);
		
		return criteriRicerca;
	}

	/**
	 * Gets the capitolo u gest.
	 *
	 * @return the capitolo u gest
	 */
	protected CapitoloUscitaGestione getCapitoloUGest()
	{
		CapitoloUscitaGestione capitoloUscitaGestione = null;

		capitoloUscitaGestione = new CapitoloUscitaGestione();

		capitoloUscitaGestione.setAnnoCapitolo(annoCapi);
		capitoloUscitaGestione.setNumeroCapitolo(numCapi);
		capitoloUscitaGestione.setNumeroArticolo(numArti);
		
		try {
			capitoloUscitaGestione.setStatoOperativoElementoDiBilancio(StatoOperativoElementoDiBilancio.valueOf(statoCapi));
		} catch (Exception e) {
			capitoloUscitaGestione.setStatoOperativoElementoDiBilancio(null);
		}

		descCapi=descCapi + " " + new Date().toString();
		capitoloUscitaGestione.setDescrizione(descCapi);

		capitoloUscitaGestione.setAnnoCreazioneCapitolo(annoCrea);

		return capitoloUscitaGestione;
	}
}
