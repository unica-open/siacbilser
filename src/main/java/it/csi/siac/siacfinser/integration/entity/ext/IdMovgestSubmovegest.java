/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity.ext;

import java.io.Serializable;
import java.math.BigDecimal;

public class IdMovgestSubmovegest implements Serializable {
	
	
	private static final long serialVersionUID = 1L;
	private Integer movgestId, movgestTsIdPadre, movgestTsId;
	private BigDecimal numero;
	private Integer anno;

	public IdMovgestSubmovegest(Integer movgestId, Integer movgestTsIdPadre, Integer movgestTsId,
			BigDecimal numero, Integer anno) {
		this.movgestId = movgestId;
		this.movgestTsIdPadre = movgestTsIdPadre;
		this.movgestTsId = movgestTsId;
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


	@Override
	public boolean equals(Object obj) {
		if (obj instanceof IdMovgestSubmovegest) {
			IdMovgestSubmovegest that = (IdMovgestSubmovegest) obj;
			boolean flagImpegno = this.movgestId != null
					&& that.movgestId != null
					&& this.movgestId.equals(that.movgestId);
			// boolean flagImpegnoPadre = this.idImpegno != null &&
			// that.idImpegnoPadre != null &&
			// this.idImpegno.equals(that.idImpegnoPadre) ||
			// that.idImpegno != null && this.idImpegnoPadre != null &&
			// that.idImpegno.equals(this.idImpegnoPadre);
			return flagImpegno; // || flagImpegnoPadre;
		}
		return false;
	}
	
	

	public Integer getMovgestId() {
		return movgestId;
	}

	public void setMovgestId(Integer movgestId) {
		this.movgestId = movgestId;
	}

	public Integer getMovgestTsIdPadre() {
		return movgestTsIdPadre;
	}

	public void setMovgestTsIdPadre(Integer movgestTsIdPadre) {
		this.movgestTsIdPadre = movgestTsIdPadre;
	}

	public Integer getMovgestTsId() {
		return movgestTsId;
	}

	public void setMovgestTsId(Integer movgestTsId) {
		this.movgestTsId = movgestTsId;
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}
}
