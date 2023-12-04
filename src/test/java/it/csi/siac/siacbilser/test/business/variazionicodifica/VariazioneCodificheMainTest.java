/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.variazionicodifica;

import java.util.Collection;
import java.util.Date;

import it.csi.siac.siacattser.model.AttoDiLegge;
import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siacbilser.model.AttoDiLeggeCapitolo;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.ric.RicercaAttiDiLeggeCapitolo;
import it.csi.siac.siacbilser.test.business.attodileggecapitolo.RelazioneAttodiLeggeMainTestBase;

// TODO: Auto-generated Javadoc
/**
 * The Class VariazioneCodificheMainTest.
 */
public class VariazioneCodificheMainTest extends RelazioneAttodiLeggeMainTestBase 
{	
	
	/**
	 * Spreadsheet data.
	 *
	 * @param sheetName the sheet name
	 * @return the collection
	 * @throws Exception the exception
	 */
	public static  Collection<Object[]> spreadsheetData(String sheetName) throws Exception {
   		return spreadsheetData("src/test/resources/relazioneattodileggeparametri.xls",sheetName);
   	}
	
	/**
	 * Gets the atto di legge.
	 *
	 * @return the atto di legge
	 */
	protected AttoDiLegge getAttoDiLegge()
	{
		AttoDiLegge attolegge = null;

		if (uidAttoLegge != null || annoAttoLegge != null || numeroAttoLegge != null || uidTipoAttoLegge != null 
				|| descrTipoAttoLegge != null)
		{
			attolegge = new AttoDiLegge();
			if (uidAttoLegge != null)
			{
				attolegge.setUid(uidAttoLegge);
			}
			
			attolegge.setAnno(annoAttoLegge);
			attolegge.setNumero(numeroAttoLegge);
			attolegge.setTipoAtto(getTipoAtto());
		}
		
		return attolegge;
	}
	
	/**
	 * Gets the tipo atto.
	 *
	 * @return the tipo atto
	 */
	private TipoAtto getTipoAtto() {
		
		TipoAtto tipoAtto = new TipoAtto();
		
		if (uidTipoAttoLegge != null)
		{
			tipoAtto = new TipoAtto();

			tipoAtto.setUid(uidTipoAttoLegge);
			tipoAtto.setDescrizione(descrTipoAttoLegge);
		}
		
		return tipoAtto;
	}
	
	/**
	 * Gets the capitolo.
	 *
	 * @return the capitolo
	 */
	protected Capitolo getCapitolo()
	{
		Capitolo capitoloass = null;
		
		if (uidCapitoloEntrata != null) 
		{
			capitoloass = new CapitoloEntrataGestione();
			capitoloass.setUid(uidCapitoloEntrata);
		}

		if (uidCapitoloUscita != null) 
		{	
			capitoloass = new CapitoloUscitaGestione();
			capitoloass.setUid(uidCapitoloUscita);
		}
	
		return capitoloass;
	}
	
	/**
	 * Gets the atto di legge capitolo.
	 *
	 * @return the atto di legge capitolo
	 */
	protected AttoDiLeggeCapitolo getAttoDiLeggeCapitolo()
	{
		AttoDiLeggeCapitolo attoleggecapi = new AttoDiLeggeCapitolo();

		attoleggecapi.setDataFineFinanz(new Date());
		attoleggecapi.setDataInizioFinanz(new Date());
		attoleggecapi.setGerarchia(attoCapiGerarchia);
		attoleggecapi.setDescrizione(attoCapiDescrizione);
		return attoleggecapi;
	}

	/**
	 * Gets the ricerca atti di legge capitolo.
	 *
	 * @return the ricerca atti di legge capitolo
	 */
	protected RicercaAttiDiLeggeCapitolo getRicercaAttiDiLeggeCapitolo()
	{
		RicercaAttiDiLeggeCapitolo ricercaAttiDiLeggeCapitolo = new RicercaAttiDiLeggeCapitolo();
		
		ricercaAttiDiLeggeCapitolo.setAttoDiLegge(getAttoDiLegge());
		ricercaAttiDiLeggeCapitolo.setBilancio(getBilancioTest());
		
		Capitolo capi = null;
		if (uidCapitoloEntrata != null) 
		{
			capi = new CapitoloEntrataGestione();
			capi.setUid(uidCapitoloEntrata);
		}
		
		if (uidCapitoloUscita != null) 
		{

			capi = new CapitoloUscitaGestione();
			capi.setUid(uidCapitoloUscita);
		}
		
		ricercaAttiDiLeggeCapitolo.setCapitolo(capi);
		
		return ricercaAttiDiLeggeCapitolo;
	}
}
