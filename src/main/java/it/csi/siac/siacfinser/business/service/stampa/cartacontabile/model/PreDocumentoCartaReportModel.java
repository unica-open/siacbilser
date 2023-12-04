/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.business.service.stampa.cartacontabile.model;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlType;

import it.csi.siac.siacbilser.business.service.stampa.base.ReportInternalSvcDictionary;
import it.csi.siac.siacfinser.business.service.stampa.model.CodificaModel;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto;

@XmlType(namespace = ReportInternalSvcDictionary.NAMESPACE)
public class PreDocumentoCartaReportModel {
	
	
	private Integer numero;
	private SoggettoCartaReportModel soggetto;
	private ModalitaPagamentoSoggetto modalitaPagamentoSoggetto;
	
	private SoggettoCartaReportModel soggettoCc;
	private ModalitaPagamentoSoggetto modalitaPagamentoSoggettoCc;
	
	private MovimentoReportModel impegno;
	private SubDocumentiReportModel subDocumenti;
	
	private String valuta;
	private BigDecimal importo;
	private String importoInLettere;
	
	private CodificaModel contoTesoriere;
	
	public Integer getNumero() {
		return numero;
	}
	public void setNumero(Integer numero) {
		this.numero = numero;
	}
	public SoggettoCartaReportModel getSoggetto() {
		return soggetto;
	}
	public void setSoggetto(SoggettoCartaReportModel soggetto) {
		this.soggetto = soggetto;
	}
	public String getValuta() {
		return valuta;
	}
	public void setValuta(String valuta) {
		this.valuta = valuta;
	}
	public BigDecimal getImporto() {
		return importo;
	}
	public void setImporto(BigDecimal importo) {
		this.importo = importo;
	}
	public ModalitaPagamentoSoggetto getModalitaPagamentoSoggetto() {
		return modalitaPagamentoSoggetto;
	}
	public void setModalitaPagamentoSoggetto(
			ModalitaPagamentoSoggetto modalitaPagamentoSoggetto) {
		this.modalitaPagamentoSoggetto = modalitaPagamentoSoggetto;
	}
	
	public String getImportoInLettere() {
		return importoInLettere;
	}
	public void setImportoInLettere(String importoInLettere) {
		this.importoInLettere = importoInLettere;
	}
	public CodificaModel getContoTesoriere() {
		return contoTesoriere;
	}
	public void setContoTesoriere(CodificaModel contoTesoriere) {
		this.contoTesoriere = contoTesoriere;
	}
	public SoggettoCartaReportModel getSoggettoCc() {
		return soggettoCc;
	}
	public void setSoggettoCc(SoggettoCartaReportModel soggettoCc) {
		this.soggettoCc = soggettoCc;
	}
	public ModalitaPagamentoSoggetto getModalitaPagamentoSoggettoCc() {
		return modalitaPagamentoSoggettoCc;
	}
	public void setModalitaPagamentoSoggettoCc(
			ModalitaPagamentoSoggetto modalitaPagamentoSoggettoCc) {
		this.modalitaPagamentoSoggettoCc = modalitaPagamentoSoggettoCc;
	}
	public MovimentoReportModel getImpegno() {
		return impegno;
	}
	public void setImpegno(MovimentoReportModel impegno) {
		this.impegno = impegno;
	}
	public SubDocumentiReportModel getSubDocumenti() {
		return subDocumenti;
	}
	public void setSubDocumenti(SubDocumentiReportModel subDocumenti) {
		this.subDocumenti = subDocumenti;
	}
	
}
