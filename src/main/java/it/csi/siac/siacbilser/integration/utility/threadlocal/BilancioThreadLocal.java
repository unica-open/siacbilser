/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.utility.threadlocal;

import it.csi.siac.siaccorser.model.Bilancio;

/**
 * Thread-local for handling ModelDetails
 * @author Marchino Alessandro
 *
 */
public class BilancioThreadLocal extends ThreadLocal<Bilancio> {
	
	@Override
	protected Bilancio initialValue() {
		return null;
	}
	
	public void initBilancio(Integer annoBilancio) {
		// se l'anno di bilancion non e' impostato ritorna
		if (annoBilancio == null || annoBilancio.intValue() == 0) {
			return;
		}
		
		Bilancio currBilancio = this.get();

		// se l'anno di bilancion non e' impostato ritorna
		if (currBilancio != null && currBilancio.getAnno() == annoBilancio) {
			return;
		}
		
		Bilancio bilancio = new Bilancio();
		bilancio.setAnno(annoBilancio);
		
		this.set(bilancio);
	}
}
