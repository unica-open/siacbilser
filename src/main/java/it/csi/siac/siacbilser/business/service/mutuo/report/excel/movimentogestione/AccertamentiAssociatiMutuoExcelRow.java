/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.mutuo.report.excel.movimentogestione;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlType;

import it.csi.siac.siacbilser.model.BILDataDictionary;

@XmlType(namespace = BILDataDictionary.NAMESPACE)

public class AccertamentiAssociatiMutuoExcelRow extends MovimentiGestioneAssociatiMutuoExcelRow {

	private String codTitolo;
	private String elencoSubAccertamenti;
	private BigDecimal importoIncassato;

	public String getCodTitolo() {
		return codTitolo;
	}

	public void setCodTitolo(String codTitolo) {
		this.codTitolo = codTitolo;
	}

	public String getElencoSubAccertamenti() {
		return elencoSubAccertamenti;
	}

	public void setElencoSubAccertamenti(String elencoSubAccertamenti) {
		this.elencoSubAccertamenti = elencoSubAccertamenti;
	}

	public BigDecimal getImportoIncassato() {
		return importoIncassato;
	}

	public void setImportoIncassato(BigDecimal importoIncassato) {
		this.importoIncassato = importoIncassato;
	}
	
	
	
}
