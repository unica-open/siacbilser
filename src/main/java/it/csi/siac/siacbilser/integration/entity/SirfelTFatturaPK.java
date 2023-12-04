/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the sirfel_t_fattura database table.
 * 
 */
@Embeddable
public class SirfelTFatturaPK extends SirfelPKBase {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

//	@Column(name="ente_proprietario_id"/*, insertable=false, updatable=false*/)
//	private Integer enteProprietarioId;

	@Column(name="id_fattura")
	private Integer idFattura;

	public SirfelTFatturaPK() {
	}
//	public Integer getEnteProprietarioId() {
//		return this.enteProprietarioId;
//	}
//	public void setEnteProprietarioId(Integer enteProprietarioId) {
//		this.enteProprietarioId = enteProprietarioId;
//	}
	public Integer getIdFattura() {
		return this.idFattura;
	}
	public void setIdFattura(Integer idFattura) {
		this.idFattura = idFattura;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof SirfelTFatturaPK)) {
			return false;
		}
		SirfelTFatturaPK castOther = (SirfelTFatturaPK)other;
		return 
			this.enteProprietarioId.equals(castOther.enteProprietarioId)
			&& this.idFattura.equals(castOther.idFattura);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.enteProprietarioId.hashCode();
		hash = hash * prime + this.idFattura.hashCode();
		
		return hash;
	}
}