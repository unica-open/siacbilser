/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.common.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Classe che mappa la lettura dei mandati per gli atti id liquidazione
 * 
 * @author 1513
 *
 */
public class RicercaEstesaOrdinativiDiPagamentoDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private Integer annoEsercizio;
	private Integer annoAtto;
	private Integer numeroAtto;
	private String codiceTipoAtto;
	private String codiceSacAtto;
	private String codiceTipoSacAtto;
	private Integer annoOrdinativo;
	private Integer numeroOrdinativo;
	private String codiceSubOrdinativo;
	private String descrizioneStato;
	private String creditore;
	private Integer annoLiquidazione;
	private Integer numeroLiquidazione;
	private Integer numeroCapitolo;
	private Integer numeroArticolo;
	private Integer annoImpegno;
	private Integer numeroImpegno;
	private String codiceSubImpegno;
	private Date dataEmissione;
	private BigDecimal importoOrdinativo;
	
	private Date dataQuietanza;
	private BigDecimal importoQuietanza;
	
	
	
	/**
	 * @return the dataQuietanza
	 */
	public Date getDataQuietanza() {
		return dataQuietanza;
	}
	/**
	 * @param dataQuietanza the dataQuietanza to set
	 */
	public void setDataQuietanza(Date dataQuietanza) {
		this.dataQuietanza = dataQuietanza;
	}
	/**
	 * @return the importoQuietanza
	 */
	public BigDecimal getImportoQuietanza() {
		return importoQuietanza;
	}
	/**
	 * @param importoQuietanza the importoQuietanza to set
	 */
	public void setImportoQuietanza(BigDecimal importoQuietanza) {
		this.importoQuietanza = importoQuietanza;
	}
	/**
	 * @return the annoEsercizio
	 */
	public Integer getAnnoEsercizio() {
		return annoEsercizio;
	}
	/**
	 * @param annoEsercizio the annoEsercizio to set
	 */
	public void setAnnoEsercizio(Integer annoEsercizio) {
		this.annoEsercizio = annoEsercizio;
	}
	/**
	 * @return the annoAtto
	 */
	public Integer getAnnoAtto() {
		return annoAtto;
	}
	/**
	 * @param annoAtto the annoAtto to set
	 */
	public void setAnnoAtto(Integer annoAtto) {
		this.annoAtto = annoAtto;
	}
	/**
	 * @return the numeroAtto
	 */
	public Integer getNumeroAtto() {
		return numeroAtto;
	}
	/**
	 * @param numeroAtto the numeroAtto to set
	 */
	public void setNumeroAtto(Integer numeroAtto) {
		this.numeroAtto = numeroAtto;
	}
	/**
	 * @return the annoOrdinativo
	 */
	public Integer getAnnoOrdinativo() {
		return annoOrdinativo;
	}
	/**
	 * @param annoOrdinativo the annoOrdinativo to set
	 */
	public void setAnnoOrdinativo(Integer annoOrdinativo) {
		this.annoOrdinativo = annoOrdinativo;
	}
	/**
	 * @return the numeroOrdinativo
	 */
	public Integer getNumeroOrdinativo() {
		return numeroOrdinativo;
	}
	/**
	 * @param numeroOrdinativo the numeroOrdinativo to set
	 */
	public void setNumeroOrdinativo(Integer numeroOrdinativo) {
		this.numeroOrdinativo = numeroOrdinativo;
	}
	/**
	 * @return the codiceSubOrdinativo
	 */
	public String getCodiceSubOrdinativo() {
		return codiceSubOrdinativo;
	}
	/**
	 * @param codiceSubOrdinativo the codiceSubOrdinativo to set
	 */
	public void setCodiceSubOrdinativo(String codiceSubOrdinativo) {
		this.codiceSubOrdinativo = codiceSubOrdinativo;
	}
	/**
	 * @return the descrizioneStato
	 */
	public String getDescrizioneStato() {
		return descrizioneStato;
	}
	/**
	 * @param descrizioneStato the descrizioneStato to set
	 */
	public void setDescrizioneStato(String descrizioneStato) {
		this.descrizioneStato = descrizioneStato;
	}
	/**
	 * @return the creditore
	 */
	public String getCreditore() {
		return creditore;
	}
	/**
	 * @param creditore the creditore to set
	 */
	public void setCreditore(String creditore) {
		this.creditore = creditore;
	}
	/**
	 * @return the annoLiquidazione
	 */
	public Integer getAnnoLiquidazione() {
		return annoLiquidazione;
	}
	/**
	 * @param annoLiquidazione the annoLiquidazione to set
	 */
	public void setAnnoLiquidazione(Integer annoLiquidazione) {
		this.annoLiquidazione = annoLiquidazione;
	}
	/**
	 * @return the numeroLiquidazione
	 */
	public Integer getNumeroLiquidazione() {
		return numeroLiquidazione;
	}
	/**
	 * @param numeroLiquidazione the numeroLiquidazione to set
	 */
	public void setNumeroLiquidazione(Integer numeroLiquidazione) {
		this.numeroLiquidazione = numeroLiquidazione;
	}
	/**
	 * @return the numeroCapitolo
	 */
	public Integer getNumeroCapitolo() {
		return numeroCapitolo;
	}
	/**
	 * @param numeroCapitolo the numeroCapitolo to set
	 */
	public void setNumeroCapitolo(Integer numeroCapitolo) {
		this.numeroCapitolo = numeroCapitolo;
	}
	/**
	 * @return the numeroArticolo
	 */
	public Integer getNumeroArticolo() {
		return numeroArticolo;
	}
	/**
	 * @param numeroArticolo the numeroArticolo to set
	 */
	public void setNumeroArticolo(Integer numeroArticolo) {
		this.numeroArticolo = numeroArticolo;
	}
	/**
	 * @return the annoImpegno
	 */
	public Integer getAnnoImpegno() {
		return annoImpegno;
	}
	/**
	 * @param annoImpegno the annoImpegno to set
	 */
	public void setAnnoImpegno(Integer annoImpegno) {
		this.annoImpegno = annoImpegno;
	}
	/**
	 * @return the numeroImpegno
	 */
	public Integer getNumeroImpegno() {
		return numeroImpegno;
	}
	/**
	 * @param numeroImpegno the numeroImpegno to set
	 */
	public void setNumeroImpegno(Integer numeroImpegno) {
		this.numeroImpegno = numeroImpegno;
	}
	/**
	 * @return the codiceSubImpegno
	 */
	public String getCodiceSubImpegno() {
		return codiceSubImpegno;
	}
	/**
	 * @param codiceSubImpegno the codiceSubImpegno to set
	 */
	public void setCodiceSubImpegno(String codiceSubImpegno) {
		this.codiceSubImpegno = codiceSubImpegno;
	}

	
	/**
	 * @return the codiceTipoAtto
	 */
	public String getCodiceTipoAtto() {
		return codiceTipoAtto;
	}
	/**
	 * @param codiceTipoAtto the codiceTipoAtto to set
	 */
	public void setCodiceTipoAtto(String codiceTipoAtto) {
		this.codiceTipoAtto = codiceTipoAtto;
	}
	/**
	 * @return the codiceSacAtto
	 */
	public String getCodiceSacAtto() {
		return codiceSacAtto;
	}
	/**
	 * @param codiceSacAtto the codiceSacAtto to set
	 */
	public void setCodiceSacAtto(String codiceSacAtto) {
		this.codiceSacAtto = codiceSacAtto;
	}
	/**
	 * @return the codiceTipoSacAtto
	 */
	public String getCodiceTipoSacAtto() {
		return codiceTipoSacAtto;
	}
	/**
	 * @param codiceTipoSacAtto the codiceTipoSacAtto to set
	 */
	public void setCodiceTipoSacAtto(String codiceTipoSacAtto) {
		this.codiceTipoSacAtto = codiceTipoSacAtto;
	}
	/**
	 * @return the dataEmissione
	 */
	public Date getDataEmissione() {
		return dataEmissione;
	}
	/**
	 * @param dataEmissione the dataEmissione to set
	 */
	public void setDataEmissione(Date dataEmissione) {
		this.dataEmissione = dataEmissione;
	}
	/**
	 * @return the importoOrdinativo
	 */
	public BigDecimal getImportoOrdinativo() {
		return importoOrdinativo;
	}
	/**
	 * @param importoOrdinativo the importoOrdinativo to set
	 */
	public void setImportoOrdinativo(BigDecimal importoOrdinativo) {
		this.importoOrdinativo = importoOrdinativo;
	}



}
