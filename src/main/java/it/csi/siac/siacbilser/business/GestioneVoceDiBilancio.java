/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business;

import it.csi.siac.siacbilser.model.VoceDiBilancio;

// TODO: Auto-generated Javadoc
/**
 * Servizio per la gestione della Voce di Bilancio.
 *
 * @author alagna
 * @version $Id: $
 */
public interface GestioneVoceDiBilancio {

	/**
	 * Gets the and save.
	 *
	 * @param voceDiBilancio the voce di bilancio
	 * @return the and save
	 */
	public VoceDiBilancio getAndSave(VoceDiBilancio voceDiBilancio);

}
