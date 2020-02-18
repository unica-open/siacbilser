/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.common.dto;

import java.math.BigDecimal;

public class EsitoControlliAggiornamentoAccertamentoDto extends EsitoControlliDto {

	private static final long serialVersionUID = 1L;

	private BigDecimal importoUtilizzabileNew;

	public BigDecimal getImportoUtilizzabileNew() {
		return importoUtilizzabileNew;
	}

	public void setImportoUtilizzabileNew(BigDecimal importoUtilizzabileNew) {
		this.importoUtilizzabileNew = importoUtilizzabileNew;
	}

}
