/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacintegser.business.service.stipendi.model;

import javax.xml.bind.annotation.XmlType;

import it.csi.siac.siacattser.model.ATTDataDictionary;

/**
 * TipoVoce 
 * @author Nazha Ahmad
 * @date 14/07/2016
 */
@XmlType(namespace = ATTDataDictionary.NAMESPACE)
public enum TipoVoce {

	RITENUTE_ONERI("E"),
	ENTRATA_SPESE_SINGOLE("D");

	private String codice;

	TipoVoce(String codice) {
		this.codice = codice;
	}

	public String getCodice() {
		return this.codice;
	}

	public static TipoVoce fromString(String tipoVoce) {
		for (TipoVoce tipo : TipoVoce.values()) {
	        if (tipo.codice.equals(tipoVoce)) {
	          return tipo;
	        }
	    }
		return null;
	}
}
