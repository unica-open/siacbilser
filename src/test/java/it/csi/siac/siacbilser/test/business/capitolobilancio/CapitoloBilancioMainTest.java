/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.capitolobilancio;

import java.util.Collection;

import it.csi.siac.siacbilser.model.StatoOperativoAttualeVariazioneDiBilancio;
import it.csi.siac.siacbilser.model.StatoOperativoVariazioneBilancio;
import it.csi.siac.siacbilser.test.business.capitolo.CapitoloMainTestBase;
import it.csi.siac.siaccorser.model.FaseEStatoAttualeBilancio;
import it.csi.siac.siaccorser.model.FaseBilancio;
import it.csi.siac.siaccorser.model.StatoBilancio;

// TODO: Auto-generated Javadoc
/**
 * The Class CapitoloBilancioMainTest.
 */
public class CapitoloBilancioMainTest  extends CapitoloMainTestBase {
	
	/**
	 * Spreadsheet data.
	 *
	 * @param sheetName the sheet name
	 * @return the collection
	 * @throws Exception the exception
	 */
	public static  Collection<Object[]> spreadsheetData(String sheetName) throws Exception {
   		return spreadsheetData("src/test/resources/capitolobilancioparametri.xls",sheetName);
   		
   	}
    
    /**
     * Gets the fase stato.
     *
     * @param faseBil the fase bil
     * @param statoBil the stato bil
     * @return the fase stato
     */
    protected FaseEStatoAttualeBilancio getFaseStato(String faseBil, String statoBil)
	{
    	FaseEStatoAttualeBilancio fasestato = new FaseEStatoAttualeBilancio();
    	if(uidBil == null) {
    		fasestato.setUid(0);
    	} else {
    		fasestato.setUid(uidBil);
    	}

		try {
			fasestato.setFaseBilancio(FaseBilancio.valueOf(faseBil));
		} catch (Exception e) {
			fasestato.setFaseBilancio(null);
		}

		try {
			fasestato.setStatoBilancio(StatoBilancio.valueOf(statoBil));
		} catch (Exception e) {
			fasestato.setStatoBilancio(null);
		}

    	return fasestato;
	}

	/**
	 * Gets the stato operativo variazione bilancio.
	 *
	 * @param statoVar the stato var
	 * @return the stato operativo variazione bilancio
	 */
	protected StatoOperativoAttualeVariazioneDiBilancio getStatoOperativoVariazioneBilancio(String statoVar)
	{
		StatoOperativoAttualeVariazioneDiBilancio statoper = new StatoOperativoAttualeVariazioneDiBilancio();

		try {
			statoper.setStatoOperativoVariazioneDiBilancio(StatoOperativoVariazioneBilancio.valueOf(statoVar));
		} catch (Exception e) {
		}

    	return statoper;
	}

}
