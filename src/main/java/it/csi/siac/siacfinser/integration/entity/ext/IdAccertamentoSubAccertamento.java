/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity.ext;

import java.io.Serializable;
import java.math.BigDecimal;

public class IdAccertamentoSubAccertamento implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer idAccertamento, idAccertamentoPadre;
	private BigDecimal numero;
	private Integer anno;
	private String stato;
	private String codiceSoggetto;
	private String codiceSoggettoClasse;

	public IdAccertamentoSubAccertamento(Integer idAccertamento, Integer idAccertamentoPadre,
			BigDecimal numero, Integer anno, String stato, String codiceSoggetto, String codiceSoggettoClasse) {
		this.idAccertamento = idAccertamento;
		this.idAccertamentoPadre = idAccertamentoPadre;
		this.numero = numero;
		this.anno = anno;
		this.stato = stato;
		this.codiceSoggetto = codiceSoggetto;
		this.codiceSoggettoClasse = codiceSoggettoClasse;
	}

	/**
	 * @return the numero
	 */
	public BigDecimal getNumero() {
		return numero;
	}

	/**
	 * @param numero
	 *            the numero to set
	 */
	public void setNumero(BigDecimal numero) {
		this.numero = numero;
	}

	/**
	 * @return the anno
	 */
	public Integer getAnno() {
		return anno;
	}

	/**
	 * @param anno
	 *            the anno to set
	 */
	public void setAnno(Integer anno) {
		this.anno = anno;
	}

	public Integer getIdAccertamento() {
		return idAccertamento;
	}

	public void setIdAccertamento(Integer idAccertamento) {
		this.idAccertamento = idAccertamento;
	}

	public Integer getIdAccertamentoPadre() {
		return idAccertamentoPadre;
	}

	public void setIdAccertamentoPadre(Integer idAccertamentoPadre) {
		this.idAccertamentoPadre = idAccertamentoPadre;
	}
	
	/**
	 * @return the stato
	 */
	public String getStato() {
		return stato;
	}

	/**
	 * @param stato the stato to set
	 */
	public void setStato(String stato) {
		this.stato = stato;
	}
	
	/**
	 * @return the codiceSoggetto
	 */
	public String getCodiceSoggetto() {
		return codiceSoggetto;
	}

	/**
	 * @param codiceSoggetto the codiceSoggetto to set
	 */
	public void setCodiceSoggetto(String codiceSoggetto) {
		this.codiceSoggetto = codiceSoggetto;
	}
	
	/**
	 * @return the codiceSoggettoClasse
	 */
	public String getCodiceSoggettoClasse() {
		return codiceSoggettoClasse;
	}

	/**
	 * @param codiceSoggettoClasse the codiceSoggettoClasse to set
	 */
	public void setCodiceSoggettoClasse(String codiceSoggettoClasse) {
		this.codiceSoggettoClasse = codiceSoggettoClasse;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof IdAccertamentoSubAccertamento) {
			IdAccertamentoSubAccertamento that = (IdAccertamentoSubAccertamento) obj;
			boolean flagAccertamento = this.idAccertamento != null
					&& that.idAccertamento != null
					&& this.idAccertamento.equals(that.idAccertamento);
			// boolean flagAccertamentoPadre = this.idAccertamento != null &&
			// that.idAccertamentoPadre != null &&
			// this.idAccertamento.equals(that.idAccertamentoPadre) ||
			// that.idAccertamento != null && this.idAccertamentoPadre != null &&
			// that.idAccertamento.equals(this.idAccertamentoPadre);
			return flagAccertamento; // || flagAccertamentoPadre;
		}
		return false;
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

}
