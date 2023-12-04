/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.business.service.stampa.cartacontabile.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.xml.bind.annotation.XmlType;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacbilser.business.service.stampa.base.ReportInternalSvcDictionary;

@XmlType(namespace = ReportInternalSvcDictionary.NAMESPACE)
public class CartaContabileReportModel {
	
	private Integer anno;
	private Integer numero;
	private String numRegistrazione;
	private Date dataCreazione;
	private Date dataScadenza;
	private Date dataEsecuzionePagamento;
	private String oggetto;
	
	
	private String divisa;
	
	
	private BigDecimal importo;
	private String importoInLettere;
	
	
	private AttoAmministrativo attoAmministrativo;
	
	private String causale;
	
	private String motivoUrgenza;
	
	private String note;
	
	private String titolareFirmaUno;
	private String titolareFirmaDue;
	
	private ModuloEsteroCartaContabileReportModel moduloEstero;
	
	private PreDocumentiCartaReportModel preDocumentiCarta;
	
	private String firma1;
	private String firma2;
	
	public Integer getNumero() {
		return numero;
	}

	public void setNumero(Integer numero) {
		this.numero = numero;
	}

	public String getNumRegistrazione() {
		return numRegistrazione;
	}

	public void setNumRegistrazione(String numRegistrazione) {
		this.numRegistrazione = numRegistrazione;
	}

	public Date getDataScadenza() {
		return dataScadenza;
	}

	public void setDataScadenza(Date dataScadenza) {
		this.dataScadenza = dataScadenza;
	}

	public Date getDataEsecuzionePagamento() {
		return dataEsecuzionePagamento;
	}

	public void setDataEsecuzionePagamento(Date dataEsecuzionePagamento) {
		this.dataEsecuzionePagamento = dataEsecuzionePagamento;
	}

	public String getOggetto() {
		return oggetto;
	}

	public void setOggetto(String oggetto) {
		this.oggetto = oggetto;
	}

	public String getDivisa() {
		return divisa;
	}

	public void setDivisa(String divisa) {
		this.divisa = divisa;
	}

	public BigDecimal getImporto() {
		return importo;
	}

	public void setImporto(BigDecimal importo) {
		this.importo = importo;
	}

	public String getCausale() {
		return causale;
	}

	public void setCausale(String causale) {
		this.causale = causale;
	}

	public String getMotivoUrgenza() {
		return motivoUrgenza;
	}

	public void setMotivoUrgenza(String motivoUrgenza) {
		this.motivoUrgenza = motivoUrgenza;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getTitolareFirmaUno() {
		return titolareFirmaUno;
	}

	public void setTitolareFirmaUno(String titolareFirmaUno) {
		this.titolareFirmaUno = titolareFirmaUno;
	}

	public String getTitolareFirmaDue() {
		return titolareFirmaDue;
	}

	public void setTitolareFirmaDue(String titolareFirmaDue) {
		this.titolareFirmaDue = titolareFirmaDue;
	}

	public AttoAmministrativo getAttoAmministrativo() {
		return attoAmministrativo;
	}

	public void setAttoAmministrativo(AttoAmministrativo attoAmministrativo) {
		this.attoAmministrativo = attoAmministrativo;
	}

	public ModuloEsteroCartaContabileReportModel getModuloEstero() {
		return moduloEstero;
	}

	public void setModuloEstero(ModuloEsteroCartaContabileReportModel moduloEstero) {
		this.moduloEstero = moduloEstero;
	}

	public Date getDataCreazione() {
		return dataCreazione;
	}

	public void setDataCreazione(Date dataCreazione) {
		this.dataCreazione = dataCreazione;
	}

	public PreDocumentiCartaReportModel getPreDocumentiCarta() {
		return preDocumentiCarta;
	}

	public void setPreDocumentiCarta(PreDocumentiCartaReportModel preDocumentiCarta) {
		this.preDocumentiCarta = preDocumentiCarta;
	}

	public String getImportoInLettere() {
		return importoInLettere;
	}

	public void setImportoInLettere(String importoInLettere) {
		this.importoInLettere = importoInLettere;
	}

	public String getFirma1() {
		return firma1;
	}

	public void setFirma1(String firma1) {
		this.firma1 = firma1;
	}

	public String getFirma2() {
		return firma2;
	}

	public void setFirma2(String firma2) {
		this.firma2 = firma2;
	}

	public Integer getAnno() {
		return anno;
	}

	public void setAnno(Integer anno) {
		this.anno = anno;
	}
	
}
