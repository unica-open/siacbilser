/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the sirfel_t_dettaglio_pagamento database table.
 * 
 */
@Embeddable
public class SirfelTDettaglioPagamentoPK extends SirfelPKBase {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

//	@Column(name="ente_proprietario_id"/*, insertable=false, updatable=false*/)
//	private Integer enteProprietarioId;

	@Column(name="id_fattura"/*, insertable=false, updatable=false*/)
	private Integer idFattura;

	@Column(name="progressivo_pagamento"/*, insertable=false, updatable=false*/)
	private Integer progressivoPagamento;

	@Column(name="progressivo_dettaglio")
	private Integer progressivoDettaglio;

	public SirfelTDettaglioPagamentoPK() {
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
	public Integer getProgressivoPagamento() {
		return this.progressivoPagamento;
	}
	public void setProgressivoPagamento(Integer progressivoPagamento) {
		this.progressivoPagamento = progressivoPagamento;
	}
	public Integer getProgressivoDettaglio() {
		return this.progressivoDettaglio;
	}
	public void setProgressivoDettaglio(Integer progressivoDettaglio) {
		this.progressivoDettaglio = progressivoDettaglio;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof SirfelTDettaglioPagamentoPK)) {
			return false;
		}
		SirfelTDettaglioPagamentoPK castOther = (SirfelTDettaglioPagamentoPK)other;
		return 
			this.enteProprietarioId.equals(castOther.enteProprietarioId)
			&& this.idFattura.equals(castOther.idFattura)
			&& this.progressivoPagamento.equals(castOther.progressivoPagamento)
			&& this.progressivoDettaglio.equals(castOther.progressivoDettaglio);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.enteProprietarioId.hashCode();
		hash = hash * prime + this.idFattura.hashCode();
		hash = hash * prime + this.progressivoPagamento.hashCode();
		hash = hash * prime + this.progressivoDettaglio.hashCode();
		
		return hash;
	}
}