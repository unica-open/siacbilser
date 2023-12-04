/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.mutuo.report.excel.movimentogestione;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlType;

import it.csi.siac.siacbilser.model.BILDataDictionary;

@XmlType(namespace = BILDataDictionary.NAMESPACE)

public class ImpegniAssociatiMutuoExcelRow extends MovimentiGestioneAssociatiMutuoExcelRow {

	private String codMissione;
	private String codProgramma;
	private String componenteBilancio;
	private String cig;
	private String cup;
	private String elencoSubImpegni;
	private BigDecimal importoLiquidato;

	public String getCodMissione() {
		return codMissione;
	}

	public void setCodMissione(String codMissione) {
		this.codMissione = codMissione;
	}

	public String getCodProgramma() {
		return codProgramma;
	}

	public void setCodProgramma(String codProgramma) {
		this.codProgramma = codProgramma;
	}

	public String getComponenteBilancio() {
		return componenteBilancio;
	}

	public void setComponenteBilancio(String componenteBilancio) {
		this.componenteBilancio = componenteBilancio;
	}

	public String getCig() {
		return cig;
	}

	public void setCig(String cig) {
		this.cig = cig;
	}

	public String getCup() {
		return cup;
	}

	public void setCup(String cup) {
		this.cup = cup;
	}

	public String getElencoSubImpegni() {
		return elencoSubImpegni;
	}

	public void setElencoSubImpegni(String elencoSubImpegni) {
		this.elencoSubImpegni = elencoSubImpegni;
	}

	public BigDecimal getImportoLiquidato() {
		return importoLiquidato;
	}

	public void setImportoLiquidato(BigDecimal importoLiquidato) {
		this.importoLiquidato = importoLiquidato;
	}

}
