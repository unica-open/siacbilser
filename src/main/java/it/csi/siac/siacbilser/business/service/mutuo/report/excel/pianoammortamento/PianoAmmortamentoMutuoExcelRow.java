/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.mutuo.report.excel.pianoammortamento;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import javax.xml.bind.annotation.XmlType;

import it.csi.siac.siacbilser.business.service.mutuo.report.excel.base.BaseMutuoExcelRow;
import it.csi.siac.siacbilser.model.BILDataDictionary;

@XmlType(namespace = BILDataDictionary.NAMESPACE)
	
public class PianoAmmortamentoMutuoExcelRow extends BaseMutuoExcelRow {
	
	private Integer numeroRata;
	private Integer anno;
	private Integer numeroRataAnno;
	private Date dataScadenza;
	private BigDecimal importoRata;
	private BigDecimal quotaCapitale;
	private BigDecimal quotaInteressi;
	private BigDecimal quotaOneri;
	private BigDecimal debitoIniziale;
	private BigDecimal debitoResiduo;
	

	public Integer getNumeroRata() {
		return numeroRata;
	}

	public void setNumeroRata(Integer numeroRata) {
		this.numeroRata = numeroRata;
	}

	public Integer getAnno() {
		return anno;
	}

	public void setAnno(Integer anno) {
		this.anno = anno;
	}

	public Integer getNumeroRataAnno() {
		return numeroRataAnno;
	}

	public void setNumeroRataAnno(Integer numeroRataAnno) {
		this.numeroRataAnno = numeroRataAnno;
	}

	public Date getDataScadenza() {
		return dataScadenza;
	}

	public void setDataScadenza(Date dataScadenza) {
		this.dataScadenza = dataScadenza;
	}

	public BigDecimal getImportoRata() {
		return importoRata;
	}

	public void setImportoRata(BigDecimal importoRata) {
		this.importoRata = importoRata;
	}

	public BigDecimal getQuotaCapitale() {
		return quotaCapitale;
	}

	public void setQuotaCapitale(BigDecimal quotaCapitale) {
		this.quotaCapitale = quotaCapitale;
	}

	public BigDecimal getQuotaInteressi() {
		return quotaInteressi;
	}

	public void setQuotaInteressi(BigDecimal quotaInteressi) {
		this.quotaInteressi = quotaInteressi;
	}

	public BigDecimal getQuotaOneri() {
		return quotaOneri;
	}

	public void setQuotaOneri(BigDecimal quotaOneri) {
		this.quotaOneri = quotaOneri;
	}

	public BigDecimal getDebitoIniziale() {
		return debitoIniziale;
	}

	public void setDebitoIniziale(BigDecimal debitoIniziale) {
		this.debitoIniziale = debitoIniziale;
	}

	public BigDecimal getDebitoResiduo() {
		return debitoResiduo;
	}

	public void setDebitoResiduo(BigDecimal debitoResiduo) {
		this.debitoResiduo = debitoResiduo;
	}
	
		
}
