/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;


/**
 * The persistent class for the sirfel_t_pagamento database table.
 * 
 */
@Entity
@Table(name="sirfel_t_pagamento")
@NamedQuery(name="SirfelTPagamento.findAll", query="SELECT s FROM SirfelTPagamento s")
public class SirfelTPagamento extends SirfelTBase<SirfelTPagamentoPK> {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	@AttributeOverrides({
        @AttributeOverride(name="enteProprietarioId",
                           column=@Column(name="ente_proprietario_id")),
        @AttributeOverride(name="idFattura",
                           column=@Column(name="id_fattura"))
    })
	private SirfelTPagamentoPK id;

	@Column(name="condizioni_pagamento")
	private String condizioniPagamento;

	//bi-directional many-to-one association to SirfelTDettaglioPagamento
	@OneToMany(mappedBy="sirfelTPagamento")
	private List<SirfelTDettaglioPagamento> sirfelTDettaglioPagamentos;

	//bi-directional many-to-one association to SirfelTFattura
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="ente_proprietario_id", referencedColumnName="ente_proprietario_id"),
		@JoinColumn(name="id_fattura", referencedColumnName="id_fattura")
		})
	@MapsId("id")
	private SirfelTFattura sirfelTFattura;

	public SirfelTPagamento() {
	}

	public SirfelTPagamentoPK getId() {
		return this.id;
	}

	public void setId(SirfelTPagamentoPK id) {
		this.id = id;
	}

	public String getCondizioniPagamento() {
		return this.condizioniPagamento;
	}

	public void setCondizioniPagamento(String condizioniPagamento) {
		this.condizioniPagamento = condizioniPagamento;
	}

	public List<SirfelTDettaglioPagamento> getSirfelTDettaglioPagamentos() {
		return this.sirfelTDettaglioPagamentos;
	}

	public void setSirfelTDettaglioPagamentos(List<SirfelTDettaglioPagamento> sirfelTDettaglioPagamentos) {
		this.sirfelTDettaglioPagamentos = sirfelTDettaglioPagamentos;
	}

	public SirfelTDettaglioPagamento addSirfelTDettaglioPagamentos(SirfelTDettaglioPagamento sirfelTDettaglioPagamentos) {
		getSirfelTDettaglioPagamentos().add(sirfelTDettaglioPagamentos);
		sirfelTDettaglioPagamentos.setSirfelTPagamento(this);

		return sirfelTDettaglioPagamentos;
	}

	public SirfelTDettaglioPagamento removeSirfelTDettaglioPagamentos(SirfelTDettaglioPagamento sirfelTDettaglioPagamentos) {
		getSirfelTDettaglioPagamentos().remove(sirfelTDettaglioPagamentos);
		sirfelTDettaglioPagamentos.setSirfelTPagamento(null);

		return sirfelTDettaglioPagamentos;
	}

	public SirfelTFattura getSirfelTFattura() {
		return this.sirfelTFattura;
	}

	public void setSirfelTFattura(SirfelTFattura sirfelTFattura) {
		this.sirfelTFattura = sirfelTFattura;
	}

	public Integer getProgressivo() {
		return getId() != null ? getId().getProgressivo() : null;
	}

}