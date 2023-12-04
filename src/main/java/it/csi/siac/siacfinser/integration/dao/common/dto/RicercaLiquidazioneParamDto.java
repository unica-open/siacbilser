/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.common.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class RicercaLiquidazioneParamDto implements Serializable {

	private static final long serialVersionUID = -1L;

	private Integer annoBilancio;
	private Integer annoEsercizio;
	private Integer annoCapitolo;
	private Integer numeroUEB;
	private BigDecimal numeroCapitolo;
	private BigDecimal numeroArticolo;
	private Integer uidCapitolo;
	private Integer annoImpegno;
	private BigDecimal numeroImpegno;
	private BigDecimal numeroSubImpegno;
	private String codiceCreditore;
	private String tipoProvvedimento;
	private Integer annoProvvedimento;
	private BigDecimal numeroProvvedimento;
	private Integer uidProvvedimento;
	private Integer annoLiquidazione;
	private BigDecimal numeroLiquidazione;
	private String tipoRicerca;
	
	private Integer uidStrutturaAmministrativaProvvedimento;
	
	//SIAC-5253 itroduciamo la possibilita' di escludere stati precisi:
	private List<String> statiDaEscludere;

	/**
	 * @return the tipoRicerca
	 */
	public String getTipoRicerca() {
		return tipoRicerca;
	}

	/**
	 * @param tipoRicerca
	 *            the tipoRicerca to set
	 */
	public void setTipoRicerca(String tipoRicerca) {
		this.tipoRicerca = tipoRicerca;
	}

	public Integer getAnnoBilancio() {
		return annoBilancio;
	}

	public void setAnnoBilancio(Integer annoBilancio) {
		this.annoBilancio = annoBilancio;
	}

	public Integer getAnnoEsercizio() {
		return annoEsercizio;
	}

	public void setAnnoEsercizio(Integer annoEsercizio) {
		this.annoEsercizio = annoEsercizio;
	}

	public Integer getAnnoCompetenza() {
		return annoCapitolo;
	}

	public void setAnnoCompetenza(Integer annoCompetenza) {
		this.annoCapitolo = annoCompetenza;
	}

	public BigDecimal getNumeroCapitolo() {
		return numeroCapitolo;
	}

	public void setNumeroCapitolo(BigDecimal numeroCapitolo) {
		this.numeroCapitolo = numeroCapitolo;
	}

	public BigDecimal getNumeroArticolo() {
		return numeroArticolo;
	}

	public void setNumeroArticolo(BigDecimal numeroArticolo) {
		this.numeroArticolo = numeroArticolo;
	}

	public Integer getAnnoImpegno() {
		return annoImpegno;
	}

	public void setAnnoImpegno(Integer annoImpegno) {
		this.annoImpegno = annoImpegno;
	}

	public BigDecimal getNumeroImpegno() {
		return numeroImpegno;
	}

	public void setNumeroImpegno(BigDecimal numeroImpegno) {
		this.numeroImpegno = numeroImpegno;
	}

	public String getCodiceCreditore() {
		return codiceCreditore;
	}

	public void setCodiceCreditore(String codiceCreditore) {
		this.codiceCreditore = codiceCreditore;
	}

	public String getTipoProvvedimento() {
		return tipoProvvedimento;
	}

	public void setTipoProvvedimento(String tipoProvvedimento) {
		this.tipoProvvedimento = tipoProvvedimento;
	}

	public Integer getAnnoProvvedimento() {
		return annoProvvedimento;
	}

	public void setAnnoProvvedimento(Integer annoProvvedimento) {
		this.annoProvvedimento = annoProvvedimento;
	}

	public BigDecimal getNumeroProvvedimento() {
		return numeroProvvedimento;
	}

	public void setNumeroProvvedimento(BigDecimal numeroProvvedimento) {
		this.numeroProvvedimento = numeroProvvedimento;
	}

	public Integer getAnnoLiquidazione() {
		return annoLiquidazione;
	}

	public void setAnnoLiquidazione(Integer annoLiquidazione) {
		this.annoLiquidazione = annoLiquidazione;
	}

	public BigDecimal getNumeroLiquidazione() {
		return numeroLiquidazione;
	}

	public void setNumeroLiquidazione(BigDecimal numeroLiquidazione) {
		this.numeroLiquidazione = numeroLiquidazione;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Integer getNumeroUEB() {
		return numeroUEB;
	}

	public void setNumeroUEB(Integer numeroUEB) {
		this.numeroUEB = numeroUEB;
	}

	public Integer getUidCapitolo() {
		return uidCapitolo;
	}

	public void setUidCapitolo(Integer uidCapitolo) {
		this.uidCapitolo = uidCapitolo;
	}

	public Integer getUidProvvedimento() {
		return uidProvvedimento;
	}

	public void setUidProvvedimento(Integer uidProvvedimento) {
		this.uidProvvedimento = uidProvvedimento;
	}

	public Integer getAnnoCapitolo() {
		return annoCapitolo;
	}

	public void setAnnoCapitolo(Integer annoCapitolo) {
		this.annoCapitolo = annoCapitolo;
	}

	public BigDecimal getNumeroSubImpegno() {
		return numeroSubImpegno;
	}

	public void setNumeroSubImpegno(BigDecimal numeroSubImpegno) {
		this.numeroSubImpegno = numeroSubImpegno;
	}

	public Integer getUidStrutturaAmministrativaProvvedimento() {
		return uidStrutturaAmministrativaProvvedimento;
	}

	public void setUidStrutturaAmministrativaProvvedimento(
			Integer uidStrutturaAmministrativaProvvedimento) {
		this.uidStrutturaAmministrativaProvvedimento = uidStrutturaAmministrativaProvvedimento;
	}

	public List<String> getStatiDaEscludere() {
		return statiDaEscludere;
	}

	public void setStatiDaEscludere(List<String> statiDaEscludere) {
		this.statiDaEscludere = statiDaEscludere;
	}

}
