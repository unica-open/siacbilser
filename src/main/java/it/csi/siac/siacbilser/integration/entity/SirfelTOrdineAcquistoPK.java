/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the sirfel_t_ordine_acquisto database table.
 * 
 */
@Embeddable
public class SirfelTOrdineAcquistoPK extends SirfelPKBase {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="id_fattura")
	private Integer idFattura;

	@Column(name="numero_documento")
	private String numeroDocumento;

	public SirfelTOrdineAcquistoPK() {
	}
	public Integer getIdFattura() {
		return this.idFattura;
	}
	public void setIdFattura(Integer idFattura) {
		this.idFattura = idFattura;
	}
	public String getNumeroDocumento() {
		return this.numeroDocumento;
	}
	public void setNumeroDocumento(String numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof SirfelTOrdineAcquistoPK)) {
			return false;
		}
		SirfelTOrdineAcquistoPK castOther = (SirfelTOrdineAcquistoPK)other;
		return 
			this.enteProprietarioId.equals(castOther.enteProprietarioId)
			&& this.idFattura.equals(castOther.idFattura)
			&& this.numeroDocumento.equals(castOther.numeroDocumento);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.enteProprietarioId.hashCode();
		hash = hash * prime + this.idFattura.hashCode();
		hash = hash * prime + this.numeroDocumento.hashCode();
		
		return hash;
	}
}