/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import javax.persistence.Embeddable;

/**
 * The primary key class for the sirfel_t_fattura_contabile database table.
 * 
 */
@Embeddable
public class SirfelTFatturaContabilePK extends SirfelPKBase {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

//	@Column(name="ente_proprietario_id"/*, insertable=false, updatable=false*/)
//	private Integer enteProprietarioId;

	private String eu;

	private Long codben;

	private String annofat;

	private String nfatt;

	private String tipofatt;

	public SirfelTFatturaContabilePK() {
	}
//	public Integer getEnteProprietarioId() {
//		return this.enteProprietarioId;
//	}
//	public void setEnteProprietarioId(Integer enteProprietarioId) {
//		this.enteProprietarioId = enteProprietarioId;
//	}
	public String getEu() {
		return this.eu;
	}
	public void setEu(String eu) {
		this.eu = eu;
	}
	public Long getCodben() {
		return this.codben;
	}
	public void setCodben(Long codben) {
		this.codben = codben;
	}
	public String getAnnofat() {
		return this.annofat;
	}
	public void setAnnofat(String annofat) {
		this.annofat = annofat;
	}
	public String getNfatt() {
		return this.nfatt;
	}
	public void setNfatt(String nfatt) {
		this.nfatt = nfatt;
	}
	public String getTipofatt() {
		return this.tipofatt;
	}
	public void setTipofatt(String tipofatt) {
		this.tipofatt = tipofatt;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof SirfelTFatturaContabilePK)) {
			return false;
		}
		SirfelTFatturaContabilePK castOther = (SirfelTFatturaContabilePK)other;
		return 
			this.enteProprietarioId.equals(castOther.enteProprietarioId)
			&& this.eu.equals(castOther.eu)
			&& this.codben.equals(castOther.codben)
			&& this.annofat.equals(castOther.annofat)
			&& this.nfatt.equals(castOther.nfatt)
			&& this.tipofatt.equals(castOther.tipofatt);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.enteProprietarioId.hashCode();
		hash = hash * prime + this.eu.hashCode();
		hash = hash * prime + ((int) (this.codben ^ (this.codben >>> 32)));
		hash = hash * prime + this.annofat.hashCode();
		hash = hash * prime + this.nfatt.hashCode();
		hash = hash * prime + this.tipofatt.hashCode();
		
		return hash;
	}
}