/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dad;
/**
 * enum con la lista delle entita' per cui vi e' la necessita'
 * di calcolare i progressivi tramite le apposite tavole
 * sul database
 * 
 * @author 
 *
 */
public enum ProgressivoType {
	SOGGETTO("sog"),
	IMPEGNO("imp"),
	ACCERTAMENTO("acc"),
	LIQUIDAZIONE("liq"),
	ORDINATIVO_DI_PAGAMENTO("ord_u"),
	ORDINATIVO_DI_INCASSO("ord_e"),
	CARTA("car");
	
	private String val;
	ProgressivoType(String val) {
		this.val = val;
	}
	
	public String getVal() {
		return val;
	}
}