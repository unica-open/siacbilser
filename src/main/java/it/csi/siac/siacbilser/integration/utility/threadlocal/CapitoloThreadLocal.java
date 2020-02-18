/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.utility.threadlocal;

import it.csi.siac.siacbilser.model.Capitolo;

/**
 * Thread-local for handling Capitolo
 * @author Marchino Alessandro
 *
 */
public class CapitoloThreadLocal extends ThreadLocal<Capitolo<?, ?>> {
	
	@Override
	protected Capitolo<?, ?> initialValue() {
		return null;
	}
	
}
