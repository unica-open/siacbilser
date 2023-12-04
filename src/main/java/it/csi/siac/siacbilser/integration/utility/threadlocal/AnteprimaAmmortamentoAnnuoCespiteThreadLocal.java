/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.utility.threadlocal;

import it.csi.siac.siaccespser.model.AnteprimaAmmortamentoAnnuoCespite;

/**
 * Thread-local for handling ModelDetails
 * @author elisa
 * @version 1.0.0 - 23-10-2018
 */
public class AnteprimaAmmortamentoAnnuoCespiteThreadLocal extends ThreadLocal<AnteprimaAmmortamentoAnnuoCespite> {
	
	@Override
	protected AnteprimaAmmortamentoAnnuoCespite initialValue() {
		return null;
	}
}
