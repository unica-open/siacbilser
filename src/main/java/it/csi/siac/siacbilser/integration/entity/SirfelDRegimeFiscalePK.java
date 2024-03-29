/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import javax.persistence.Embeddable;

/**
 * The primary key class for the sirfel_d_regime_fiscale database table.
 * 
 */
@Embeddable
public class SirfelDRegimeFiscalePK extends SirfelPKBase {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

//	@Column(name="ente_proprietario_id"/*, insertable=false, updatable=false*/)
//	private Integer enteProprietarioId;

	private String codice;

	public SirfelDRegimeFiscalePK() {
	}
//	public Integer getEnteProprietarioId() {
//		return this.enteProprietarioId;
//	}
//	public void setEnteProprietarioId(Integer enteProprietarioId) {
//		this.enteProprietarioId = enteProprietarioId;
//	}
	public String getCodice() {
		return this.codice;
	}
	public void setCodice(String codice) {
		this.codice = codice;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof SirfelDRegimeFiscalePK)) {
			return false;
		}
		SirfelDRegimeFiscalePK castOther = (SirfelDRegimeFiscalePK)other;
		return 
			this.enteProprietarioId.equals(castOther.enteProprietarioId)
			&& this.codice.equals(castOther.codice);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.enteProprietarioId.hashCode();
		hash = hash * prime + this.codice.hashCode();
		
		return hash;
	}
}