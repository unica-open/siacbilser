/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.common.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class RicercaCartaContabileParamDto implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8661132634536322922L;

	// bilancio
	private Integer annoEsercizio;
	
	// carta contabile
	private Integer numeroCartaContabileDa;
	private Integer numeroCartaContabileA;
	private Date dataScadenzaDa;
	private Date dataScadenzaA;
	private String statoOperativo;
	private String oggetto;
	
	// capitolo
	private Integer annoCapitolo;
	private BigDecimal numeroCapitolo;
	private BigDecimal numeroArticolo;
	private Integer numeroUEB;
	
	// impegno
	private Integer annoImpegno;
	private BigDecimal numeroImpegno;
	private BigDecimal numeroSubImpegno;
	
	// provvedimento
	private Integer annoProvvedimento;
	private Integer numeroProvvedimento;
	private String tipoProvvedimento;
	private String strutturaAmministrativaProvvedimento;
	
	// soggetto
	private String codiceCreditore;
	
	// sub-documento
	private Integer subDocumentoSpesaId;
		
	public Integer getAnnoEsercizio() {
		return annoEsercizio;
	}

	public void setAnnoEsercizio(Integer annoEsercizio) {
		this.annoEsercizio = annoEsercizio;
	}

	public Integer getNumeroCartaContabileDa() {
		return numeroCartaContabileDa;
	}

	public void setNumeroCartaContabileDa(Integer numeroCartaContabileDa) {
		this.numeroCartaContabileDa = numeroCartaContabileDa;
	}

	public Integer getNumeroCartaContabileA() {
		return numeroCartaContabileA;
	}

	public void setNumeroCartaContabileA(Integer numeroCartaContabileA) {
		this.numeroCartaContabileA = numeroCartaContabileA;
	}

	public Date getDataScadenzaDa() {
		return dataScadenzaDa;
	}

	public void setDataScadenzaDa(Date dataScadenzaDa) {
		this.dataScadenzaDa = dataScadenzaDa;
	}

	public Date getDataScadenzaA() {
		return dataScadenzaA;
	}

	public void setDataScadenzaA(Date dataScadenzaA) {
		this.dataScadenzaA = dataScadenzaA;
	}

	public String getStatoOperativo() {
		return statoOperativo;
	}

	public void setStatoOperativo(String statoOperativo) {
		this.statoOperativo = statoOperativo;
	}

	public String getOggetto() {
		return oggetto;
	}

	public void setOggetto(String oggetto) {
		this.oggetto = oggetto;
	}

	public Integer getAnnoCapitolo() {
		return annoCapitolo;
	}

	public void setAnnoCapitolo(Integer annoCapitolo) {
		this.annoCapitolo = annoCapitolo;
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

	public Integer getNumeroUEB() {
		return numeroUEB;
	}

	public void setNumeroUEB(Integer numeroUEB) {
		this.numeroUEB = numeroUEB;
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

	public BigDecimal getNumeroSubImpegno() {
		return numeroSubImpegno;
	}

	public void setNumeroSubImpegno(BigDecimal numeroSubImpegno) {
		this.numeroSubImpegno = numeroSubImpegno;
	}

	public Integer getAnnoProvvedimento() {
		return annoProvvedimento;
	}

	public void setAnnoProvvedimento(Integer annoProvvedimento) {
		this.annoProvvedimento = annoProvvedimento;
	}

	public Integer getNumeroProvvedimento() {
		return numeroProvvedimento;
	}

	public void setNumeroProvvedimento(Integer numeroProvvedimento) {
		this.numeroProvvedimento = numeroProvvedimento;
	}

	public String getTipoProvvedimento() {
		return tipoProvvedimento;
	}

	public void setTipoProvvedimento(String tipoProvvedimento) {
		this.tipoProvvedimento = tipoProvvedimento;
	}

	public String getStrutturaAmministrativaProvvedimento() {
		return strutturaAmministrativaProvvedimento;
	}

	public void setStrutturaAmministrativaProvvedimento(
			String strutturaAmministrativaProvvedimento) {
		this.strutturaAmministrativaProvvedimento = strutturaAmministrativaProvvedimento;
	}

	public String getCodiceCreditore() {
		return codiceCreditore;
	}

	public void setCodiceCreditore(String codiceCreditore) {
		this.codiceCreditore = codiceCreditore;
	}

	public Integer getSubDocumentoSpesaId() {
		return subDocumentoSpesaId;
	}

	public void setSubDocumentoSpesaId(Integer subDocumentoSpesaId) {
		this.subDocumentoSpesaId = subDocumentoSpesaId;
	}
}
