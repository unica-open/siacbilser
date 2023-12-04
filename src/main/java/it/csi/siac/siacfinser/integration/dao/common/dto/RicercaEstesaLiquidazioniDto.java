/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.common.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;

/**
 * Classe che mappa la lettura delle liquidazioni su piu anni (quindi comprensive dei residui)
 * 
 * @author 1513
 *
 */
public class RicercaEstesaLiquidazioniDto implements Serializable {

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
	
	private String descrizioneStato;
	private String creditore;
	private Integer annoLiquidazione;
	private Integer numeroLiquidazione;
	private Integer numeroCapitolo;
	private Integer numeroArticolo;
	private Integer annoImpegno;
	private Integer numeroImpegno;
	private String codiceSubImpegno;
	private Date dataCreazione;
	private BigDecimal importoLiquidazione;
	private Integer numeroLiquidazionePrecedente;
	
	/**
	 * @return the numeroLiquidazionePrecedente
	 */
	public Integer getNumeroLiquidazionePrecedente() {
		return numeroLiquidazionePrecedente;
	}
	/**
	 * @param numeroLiquidazionePrecedente the numeroLiquidazionePrecedente to set
	 */
	public void setNumeroLiquidazionePrecedente(Integer numeroLiquidazionePrecedente) {
		this.numeroLiquidazionePrecedente = numeroLiquidazionePrecedente;
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
	 * @return the dataCreazione
	 */
	public Date getDataCreazione() {
		return dataCreazione;
	}
	/**
	 * @param dataCreazione the dataCreazione to set
	 */
	public void setDataCreazione(Date dataCreazione) {
		this.dataCreazione = dataCreazione;
	}
	/**
	 * @return the importoLiquidazione
	 */
	public BigDecimal getImportoLiquidazione() {
		return importoLiquidazione;
	}
	/**
	 * @param importoLiquidazione the importoLiquidazione to set
	 */
	public void setImportoLiquidazione(BigDecimal importoLiquidazione) {
		this.importoLiquidazione = importoLiquidazione;
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
	

}
