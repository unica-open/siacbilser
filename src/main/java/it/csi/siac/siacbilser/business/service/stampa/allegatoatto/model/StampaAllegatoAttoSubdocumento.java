/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.stampa.allegatoatto.model;

import java.util.Date;

import javax.xml.bind.annotation.XmlType;

import it.csi.siac.siacbilser.business.service.stampa.base.ReportInternalSvcDictionary;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;

@XmlType(namespace = ReportInternalSvcDictionary.NAMESPACE)
public class StampaAllegatoAttoSubdocumento {
	
	private SubdocumentoSpesa subdocumentoSpesa;
	
	private String estremiNoteCredito;
	private String estremiPenali;
	private String estremiCartaContabile;
	private String estremiProvvisorioCassa;
	private String estremiImpegno;
	private String estremiLiquidazione;
	private String estremiRitenute;
	// SIAC-5271
	private Date dataScadenza;
	
	/**
	 * @return the subdocumentoSpesa
	 */
	public SubdocumentoSpesa getSubdocumentoSpesa() {
		return subdocumentoSpesa;
	}
	/**
	 * @param subdocumentoSpesa the subdocumentoSpesa to set
	 */
	public void setSubdocumentoSpesa(SubdocumentoSpesa subdocumentoSpesa) {
		this.subdocumentoSpesa = subdocumentoSpesa;
	}
	/**
	 * @return the estremiNoteCredito
	 */
	public String getEstremiNoteCredito() {
		return estremiNoteCredito;
	}
	/**
	 * @param estremiNoteCredito the estremiNoteCredito to set
	 */
	public void setEstremiNoteCredito(String estremiNoteCredito) {
		this.estremiNoteCredito = estremiNoteCredito;
	}
	/**
	 * @return the estremiPenali
	 */
	public String getEstremiPenali() {
		return estremiPenali;
	}
	/**
	 * @param estremiPenali the estremiPenali to set
	 */
	public void setEstremiPenali(String estremiPenali) {
		this.estremiPenali = estremiPenali;
	}
	/**
	 * @return the estremiCartaContabile
	 */
	public String getEstremiCartaContabile() {
		return estremiCartaContabile;
	}
	/**
	 * @param estremiCartaContabile the estremiCartaContabile to set
	 */
	public void setEstremiCartaContabile(String estremiCartaContabile) {
		this.estremiCartaContabile = estremiCartaContabile;
	}
	/**
	 * @return the estremiProvvisorioCassa
	 */
	public String getEstremiProvvisorioCassa() {
		return estremiProvvisorioCassa;
	}
	/**
	 * @param estremiProvvisorioCassa the estremiProvvisorioCassa to set
	 */
	public void setEstremiProvvisorioCassa(String estremiProvvisorioCassa) {
		this.estremiProvvisorioCassa = estremiProvvisorioCassa;
	}
	/**
	 * @return the estremiImpegno
	 */
	public String getEstremiImpegno() {
		return estremiImpegno;
	}
	/**
	 * @param estremiImpegno the estremiImpegno to set
	 */
	public void setEstremiImpegno(String estremiImpegno) {
		this.estremiImpegno = estremiImpegno;
	}
	/**
	 * @return the estremiLiquidazione
	 */
	public String getEstremiLiquidazione() {
		return estremiLiquidazione;
	}
	/**
	 * @param estremiLiquidazione the estremiLiquidazione to set
	 */
	public void setEstremiLiquidazione(String estremiLiquidazione) {
		this.estremiLiquidazione = estremiLiquidazione;
	}
	/**
	 * @return the estremiRitenute
	 */
	public String getEstremiRitenute() {
		return estremiRitenute;
	}
	/**
	 * @param estremiRitenute the estremiRitenute to set
	 */
	public void setEstremiRitenute(String estremiRitenute) {
		this.estremiRitenute = estremiRitenute;
	}
	/**
	 * @return the dataScadenza
	 */
	public Date getDataScadenza() {
		return dataScadenza;
	}
	/**
	 * @param dataScadenza the dataScadenza to set
	 */
	public void setDataScadenza(Date dataScadenza) {
		this.dataScadenza = dataScadenza;
	}
	
}
