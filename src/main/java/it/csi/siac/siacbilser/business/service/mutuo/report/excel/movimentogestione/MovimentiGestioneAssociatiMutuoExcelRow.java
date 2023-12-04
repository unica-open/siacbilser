/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.mutuo.report.excel.movimentogestione;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlType;

import it.csi.siac.siacbilser.business.service.mutuo.report.excel.base.BaseMutuoExcelRow;
import it.csi.siac.siacbilser.model.BILDataDictionary;

@XmlType(namespace = BILDataDictionary.NAMESPACE)
public abstract class MovimentiGestioneAssociatiMutuoExcelRow extends BaseMutuoExcelRow {

	private Integer anno;
	private Integer numero;
	private String statoOperativo;
	private String capitolo;
	private String descTipoFinanziamento;
	private String attoAmministrativo;
	private String soggetto;
	private String altriMutui;
	private BigDecimal importoAttuale;
//	private BigDecimal importoFinale;

	public Integer getAnno() {
		return anno;
	}

	public void setAnno(Integer anno) {
		this.anno = anno;
	}

	public Integer getNumero() {
		return numero;
	}

	public void setNumero(Integer numero) {
		this.numero = numero;
	}

	public String getCapitolo() {
		return capitolo;
	}

	public void setCapitolo(String capitolo) {
		this.capitolo = capitolo;
	}

	public String getDescTipoFinanziamento() {
		return descTipoFinanziamento;
	}

	public void setDescTipoFinanziamento(String descTipoFinanziamento) {
		this.descTipoFinanziamento = descTipoFinanziamento;
	}

	public String getAttoAmministrativo() {
		return attoAmministrativo;
	}

	public void setAttoAmministrativo(String attoAmministrativo) {
		this.attoAmministrativo = attoAmministrativo;
	}

	public String getSoggetto() {
		return soggetto;
	}

	public void setSoggetto(String soggetto) {
		this.soggetto = soggetto;
	}

	public String getAltriMutui() {
		return altriMutui;
	}

	public void setAltriMutui(String altriMutui) {
		this.altriMutui = altriMutui;
	}

	public BigDecimal getImportoAttuale() {
		return importoAttuale;
	}

	public void setImportoAttuale(BigDecimal importoAttuale) {
		this.importoAttuale = importoAttuale;
	}

//	public BigDecimal getImportoFinale() {
//		return importoFinale;
//	}
//
//	public void setImportoFinale(BigDecimal importoFinale) {
//		this.importoFinale = importoFinale;
//	}

	public String getStatoOperativo() {
		return statoOperativo;
	}

	public void setStatoOperativo(String statoOperativo) {
		this.statoOperativo = statoOperativo;
	}

}
