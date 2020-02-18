/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity.ext;

import java.io.Serializable;
import java.math.BigDecimal;

public class IdImpegnoSubimpegno implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer idImpegno, idImpegnoPadre;
	private BigDecimal numero;
	private Integer anno;

	public IdImpegnoSubimpegno(Integer idImpegno, Integer idImpegnoPadre,
			BigDecimal numero, Integer anno) {
		this.idImpegno = idImpegno;
		this.idImpegnoPadre = idImpegnoPadre;
		this.numero = numero;
		this.anno = anno;
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

	public Integer getIdImpegno() {
		return idImpegno;
	}

	public void setIdImpegno(Integer idImpegno) {
		this.idImpegno = idImpegno;
	}

	public Integer getIdImpegnoPadre() {
		return idImpegnoPadre;
	}

	public void setIdImpegnoPadre(Integer idImpegnoPadre) {
		this.idImpegnoPadre = idImpegnoPadre;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof IdImpegnoSubimpegno) {
			IdImpegnoSubimpegno that = (IdImpegnoSubimpegno) obj;
			boolean flagImpegno = this.idImpegno != null
					&& that.idImpegno != null
					&& this.idImpegno.equals(that.idImpegno);
			// boolean flagImpegnoPadre = this.idImpegno != null &&
			// that.idImpegnoPadre != null &&
			// this.idImpegno.equals(that.idImpegnoPadre) ||
			// that.idImpegno != null && this.idImpegnoPadre != null &&
			// that.idImpegno.equals(this.idImpegnoPadre);
			return flagImpegno; // || flagImpegnoPadre;
		}
		return false;
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}
}
