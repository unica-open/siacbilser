/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the sirfel_t_ordine_acquisto_dettaglio database table.
 * 
 */
@Embeddable
public class SirfelTOrdineAcquistoDettaglioPK extends SirfelPKBase {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="id_fattura")
	private Integer idFattura;

	@Column(name="numero_documento")
	private String numeroDocumento;

	@Column(name="numero_dettaglio")
	private Integer numeroDettaglio;

	public SirfelTOrdineAcquistoDettaglioPK() {
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
	public Integer getNumeroDettaglio() {
		return this.numeroDettaglio;
	}
	public void setNumeroDettaglio(Integer numeroDettaglio) {
		this.numeroDettaglio = numeroDettaglio;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof SirfelTOrdineAcquistoDettaglioPK)) {
			return false;
		}
		SirfelTOrdineAcquistoDettaglioPK castOther = (SirfelTOrdineAcquistoDettaglioPK)other;
		return 
			this.enteProprietarioId.equals(castOther.enteProprietarioId)
			&& this.idFattura.equals(castOther.idFattura)
			&& this.numeroDocumento.equals(castOther.numeroDocumento)
			&& this.numeroDettaglio.equals(castOther.numeroDettaglio);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.enteProprietarioId.hashCode();
		hash = hash * prime + this.idFattura.hashCode();
		hash = hash * prime + this.numeroDocumento.hashCode();
		hash = hash * prime + this.numeroDettaglio.hashCode();
		
		return hash;
	}
}