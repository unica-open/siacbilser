/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacatt.test.business.attodilegge;

import java.util.Collection;

import it.csi.siac.siacattser.model.AttoDiLegge;
import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siacattser.model.ric.RicercaLeggi;

// TODO: Auto-generated Javadoc
/**
 * The Class AttodiLeggeMainTest.
 */
public class AttodiLeggeMainTest extends AttodiLeggeMainTestBase {
	
	/**
	 * Spreadsheet data.
	 *
	 * @param sheetName the sheet name
	 * @return the collection
	 * @throws Exception the exception
	 */
	public static  Collection<Object[]> spreadsheetData(String sheetName) throws Exception {
   		return spreadsheetData("src/test/resources/attodileggeparametri.xls",sheetName);
   		
   	}
	
	/**
	 * Gets the criteri ricerca.
	 *
	 * @return the criteri ricerca
	 */
	protected RicercaLeggi getCriteriRicerca()
	{
		RicercaLeggi criteriRicerca = null;
		
		criteriRicerca = new RicercaLeggi();
				
		if (uidAttoLegge != null || annoAttoLegge != null || numeroAttoLegge != null || uidTipoAttoLegge != null 
				|| descrTipoAttoLegge != null || articoloAttoLegge != null || commaAttoLegge != null 
				|| puntoAttoLegge != null)
		{
			if (uidAttoLegge != null)
			{
				criteriRicerca.setUid(uidAttoLegge);
			}
		
			criteriRicerca.setAnno(annoAttoLegge);
			criteriRicerca.setNumero(numeroAttoLegge);
			criteriRicerca.setTipoAtto(getTipoAtto());
			criteriRicerca.setArticolo(articoloAttoLegge);
			criteriRicerca.setComma(commaAttoLegge);
			criteriRicerca.setPunto(puntoAttoLegge);
		}
		
		return criteriRicerca;
	}
		
	/**
	 * Gets the atto di legge.
	 *
	 * @return the atto di legge
	 */
	protected AttoDiLegge getAttoDiLegge()
	{
		AttoDiLegge attoDiLegge = null;

		if (uidAttoLegge != null || annoAttoLegge != null || numeroAttoLegge != null || uidTipoAttoLegge != null 
				|| descrTipoAttoLegge != null || articoloAttoLegge != null || commaAttoLegge != null 
				|| puntoAttoLegge != null)
		{
			attoDiLegge = new AttoDiLegge();
			if (uidAttoLegge != null)
			{
				attoDiLegge.setUid(uidAttoLegge);
			}
			
			attoDiLegge.setAnno(annoAttoLegge);
			attoDiLegge.setNumero(numeroAttoLegge);
			attoDiLegge.setTipoAtto(getTipoAtto());
			attoDiLegge.setArticolo(articoloAttoLegge);
			attoDiLegge.setComma(commaAttoLegge);
			attoDiLegge.setPunto(puntoAttoLegge);
		}
		
		return attoDiLegge;
	}

	
	/**
	 * Gets the tipo atto.
	 *
	 * @return the tipo atto
	 */
	protected TipoAtto getTipoAtto()
	{
		TipoAtto tipoAtto = null;
	
		if (uidTipoAttoLegge != null)
		{
			tipoAtto = new TipoAtto();

			tipoAtto.setUid(uidTipoAttoLegge);
			tipoAtto.setDescrizione(descrTipoAttoLegge);
		}

		return tipoAtto;
	}
}
