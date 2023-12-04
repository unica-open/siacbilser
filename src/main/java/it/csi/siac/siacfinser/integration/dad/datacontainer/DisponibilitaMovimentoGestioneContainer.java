/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dad.datacontainer;

import java.math.BigDecimal;

public class DisponibilitaMovimentoGestioneContainer {
	private final BigDecimal disponibilita;
	private final String motivazione;
	
	public DisponibilitaMovimentoGestioneContainer(BigDecimal disponibilita, String motivazione) {
		this.disponibilita = disponibilita;
		this.motivazione = motivazione;
	}

	/**
	 * @return the disponibilita
	 */
	public BigDecimal getDisponibilita() {
		return this.disponibilita;
	}

	/**
	 * @return the motivazione
	 */
	public String getMotivazione() {
		return this.motivazione;
	}
	
}