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
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;


/**
 * The persistent class for the sirfel_d_modalita_pagamento database table.
 * 
 */
@Entity
@Table(name="sirfel_d_modalita_pagamento")
@NamedQuery(name="SirfelDModalitaPagamento.findAll", query="SELECT s FROM SirfelDModalitaPagamento s")
public class SirfelDModalitaPagamento extends SirfelTBase<SirfelDModalitaPagamentoPK> {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	@AttributeOverrides({
        @AttributeOverride(name="enteProprietarioId",
                           column=@Column(name="ente_proprietario_id")),
        @AttributeOverride(name="codice",
                           column=@Column(name="codice"))
    })
	private SirfelDModalitaPagamentoPK id;

	private String descrizione;

	//bi-directional many-to-one association to SirfelTDettaglioPagamento
	@OneToMany(mappedBy="sirfelDModalitaPagamento")
	private List<SirfelTDettaglioPagamento> sirfelTDettaglioPagamentos;

//	//bi-directional many-to-one association to SirfelTDettaglioPagamento
//	@OneToMany(mappedBy="sirfelDModalitaPagamento2")
//	private List<SirfelTDettaglioPagamento> sirfelTDettaglioPagamentos2;

	public SirfelDModalitaPagamento() {
	}

	public SirfelDModalitaPagamentoPK getId() {
		return this.id;
	}

	public void setId(SirfelDModalitaPagamentoPK id) {
		this.id = id;
	}

	public String getDescrizione() {
		return this.descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public List<SirfelTDettaglioPagamento> getSirfelTDettaglioPagamentos() {
		return this.sirfelTDettaglioPagamentos;
	}

	public void setSirfelTDettaglioPagamentos(List<SirfelTDettaglioPagamento> sirfelTDettaglioPagamentos) {
		this.sirfelTDettaglioPagamentos = sirfelTDettaglioPagamentos;
	}

	public SirfelTDettaglioPagamento addSirfelTDettaglioPagamentos(SirfelTDettaglioPagamento sirfelTDettaglioPagamentos) {
		getSirfelTDettaglioPagamentos().add(sirfelTDettaglioPagamentos);
		sirfelTDettaglioPagamentos.setSirfelDModalitaPagamento(this);

		return sirfelTDettaglioPagamentos;
	}

	public SirfelTDettaglioPagamento removeSirfelTDettaglioPagamentos(SirfelTDettaglioPagamento sirfelTDettaglioPagamentos) {
		getSirfelTDettaglioPagamentos().remove(sirfelTDettaglioPagamentos);
		sirfelTDettaglioPagamentos.setSirfelDModalitaPagamento(null);

		return sirfelTDettaglioPagamentos;
	}
	
	public String getCodice() {
		return getId() != null ? getId().getCodice() : null;
	}

}