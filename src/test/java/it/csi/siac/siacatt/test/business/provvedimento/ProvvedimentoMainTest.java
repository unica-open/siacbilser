/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacatt.test.business.provvedimento;

import java.util.Collection;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siacattser.model.ric.RicercaAtti;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;

// TODO: Auto-generated Javadoc
/**
 * The Class ProvvedimentoMainTest.
 */
public class ProvvedimentoMainTest extends ProvvedimentoMainTestBase {
	
	/**
	 * Spreadsheet data.
	 *
	 * @param sheetName the sheet name
	 * @return the collection
	 * @throws Exception the exception
	 */
	public static  Collection<Object[]> spreadsheetData(String sheetName) throws Exception {
   		return spreadsheetData("src/test/resources/provvedimentoparametri.xls",sheetName);
   		
   	}
	
	/**
	 * Gets the criteri ricerca.
	 *
	 * @return the criteri ricerca
	 */
	protected RicercaAtti getCriteriRicerca()
	{
		RicercaAtti criteriRicerca = null;

		criteriRicerca = new RicercaAtti();

		if (uidAttoAmministrativo != null || annoAttoAmministrativo != null || numeroAttoAmministrativo != null || uidTipoAttoAmministrativo != null 
				|| tipoAttoAmministrativo != null || strutturaContabileAttoAmministrativo != null || oggettoAttoAmministrativo != null 
				|| statoOperativoAttoAmministrativo != null || noteAttoAmministrativo != null)
		{
			if (uidAttoAmministrativo != null)
			{
				criteriRicerca.setUid(uidAttoAmministrativo);
			}

			if (annoAttoAmministrativo != null)
			{
				criteriRicerca.setAnnoAtto(annoAttoAmministrativo);
			}
			else criteriRicerca.setAnnoAtto(0);
			
			if (numeroAttoAmministrativo != null)
			{
				criteriRicerca.setNumeroAtto(numeroAttoAmministrativo);
			}
			else criteriRicerca.setNumeroAtto(0);	
			
			criteriRicerca.setTipoAtto(getTipoAtto());
			StrutturaAmministrativoContabile struttura = new StrutturaAmministrativoContabile();
			struttura.setUid(uidStrutturaContabileAtto);
			criteriRicerca.setOggetto(oggettoAttoAmministrativo);
			criteriRicerca.setStatoOperativo(statoOperativoAttoAmministrativo);
			criteriRicerca.setNote(noteAttoAmministrativo);
		}
		
		return criteriRicerca;
	}
	
	/**
	 * Gets the atto amministrativo.
	 *
	 * @return the atto amministrativo
	 */
	protected AttoAmministrativo getAttoAmministrativo()
	{
		AttoAmministrativo attoAmministrativo = null;

		/*if (uidAttoAmministrativo != null || annoAttoAmministrativo != null || numeroAttoAmministrativo != null || uidTipoAttoAmministrativo != null 
				|| tipoAttoAmministrativo != null || strutturaContabileAttoAmministrativo != null || oggettoAttoAmministrativo != null 
				|| statoOperativoAttoAmministrativo != null || noteAttoAmministrativo != null)
		{*/
			attoAmministrativo = new AttoAmministrativo();
			
			if (uidAttoAmministrativo != null)
			{
				attoAmministrativo.setUid(uidAttoAmministrativo);
			}
						
			if (annoAttoAmministrativo != null)
			{
				attoAmministrativo.setAnno(annoAttoAmministrativo);
			}
						
			if (numeroAttoAmministrativo != null)
			{
				attoAmministrativo.setNumero(numeroAttoAmministrativo);
			}
					
			attoAmministrativo.setOggetto(oggettoAttoAmministrativo);
			attoAmministrativo.setNote(noteAttoAmministrativo);
			attoAmministrativo.setStatoOperativo(statoOperativoAttoAmministrativo);
		//}
		
		return attoAmministrativo;
	}
	
	/**
	 * Gets the struttura amministrativo contabile.
	 *
	 * @return the struttura amministrativo contabile
	 */
	protected StrutturaAmministrativoContabile getStrutturaAmministrativoContabile()
	{
		StrutturaAmministrativoContabile strutturaAmministrativoContabile = null;

		strutturaAmministrativoContabile = new StrutturaAmministrativoContabile();
		strutturaAmministrativoContabile = new StrutturaAmministrativoContabile("codice", "descrizione");
		strutturaAmministrativoContabile.setUid(1);
		//strutturaAmministrativoContabile.setCodice(strutturaContabileAttoAmministrativo);

		return strutturaAmministrativoContabile;
	}
	
	/**
	 * Gets the tipo atto.
	 *
	 * @return the tipo atto
	 */
	protected TipoAtto getTipoAtto()
	{
		TipoAtto tipoAtto = null;

		if (uidTipoAttoAmministrativo != null)
		{
			tipoAtto = new TipoAtto();
			tipoAtto.setUid(uidTipoAttoAmministrativo);
			tipoAtto.setDescrizione(tipoAttoAmministrativo);
		}
		
		return tipoAtto;
	}
}
