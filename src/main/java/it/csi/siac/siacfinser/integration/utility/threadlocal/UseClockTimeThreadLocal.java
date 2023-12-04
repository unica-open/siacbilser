/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.utility.threadlocal;

/**
 * 
 * Per dire al metodo AbstractFinDad.getCurrentMilliseconds di utilizzare
 * CLOCK_TIMESTAMP() al posto di CURRENT_TIMESTAMP
 * 
 * @author claudio.picco
 *
 */
public class UseClockTimeThreadLocal extends ThreadLocal<Boolean> {
	
	@Override
	protected Boolean initialValue() {
		return null;
	}
}
