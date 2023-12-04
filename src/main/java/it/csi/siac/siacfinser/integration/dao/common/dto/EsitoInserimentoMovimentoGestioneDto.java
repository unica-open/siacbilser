/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.common.dto;

import it.csi.siac.siacfinser.model.MovimentoGestione;

public class EsitoInserimentoMovimentoGestioneDto extends EsitoControlliDto {

	private static final long serialVersionUID = 1L;

	private MovimentoGestione movimentoGestione;

	public MovimentoGestione getMovimentoGestione() {
		return movimentoGestione;
	}

	public void setMovimentoGestione(MovimentoGestione movimentoGestione) {
		this.movimentoGestione = movimentoGestione;
	}
	

}
