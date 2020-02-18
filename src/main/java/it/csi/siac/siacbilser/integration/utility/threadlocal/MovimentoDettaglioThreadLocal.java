/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.utility.threadlocal;

import it.csi.siac.siacgenser.model.MovimentoDettaglio;

/**
 * Thread-local for handling ModelDetails
 * @author elisa
 * @version 1.0.0 - 23-10-2018
 */
public class MovimentoDettaglioThreadLocal extends ThreadLocal<MovimentoDettaglio> {
	
	@Override
	protected MovimentoDettaglio initialValue() {
		return null;
	}
}
