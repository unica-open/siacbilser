/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the sirfel_t_prestatore database table.
 * 
 */
@Embeddable
public class SirfelTPrestatorePK extends SirfelPKBase {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

//	@Column(name="ente_proprietario_id" /*, insertable=false, updatable=false*/)
//	private Integer enteProprietarioId;

	@Column(name="id_prestatore")
	private Integer idPrestatore;

	public SirfelTPrestatorePK() {
	}
//	public Integer getEnteProprietarioId() {
//		return this.enteProprietarioId;
//	}
//	public void setEnteProprietarioId(Integer enteProprietarioId) {
//		this.enteProprietarioId = enteProprietarioId;
//	}
	public Integer getIdPrestatore() {
		return this.idPrestatore;
	}
	public void setIdPrestatore(Integer idPrestatore) {
		this.idPrestatore = idPrestatore;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof SirfelTPrestatorePK)) {
			return false;
		}
		SirfelTPrestatorePK castOther = (SirfelTPrestatorePK)other;
		return 
			this.enteProprietarioId.equals(castOther.enteProprietarioId)
			&& this.idPrestatore.equals(castOther.idPrestatore);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.enteProprietarioId.hashCode();
		hash = hash * prime + this.idPrestatore.hashCode();
		
		return hash;
	}
}