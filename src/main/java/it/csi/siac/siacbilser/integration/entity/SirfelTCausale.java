/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

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
import javax.persistence.Table;


/**
 * The persistent class for the sirfel_t_causale database table.
 * 
 */
@Entity
@Table(name="sirfel_t_causale")
@NamedQuery(name="SirfelTCausale.findAll", query="SELECT s FROM SirfelTCausale s")
public class SirfelTCausale extends SirfelTBase<SirfelTCausalePK> {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	@AttributeOverrides({
        @AttributeOverride(name="enteProprietarioId", column=@Column(name="ente_proprietario_id")),
        @AttributeOverride(name="idFattura", column=@Column(name="id_fattura")),
        @AttributeOverride(name="progressivo", column=@Column(name="progressivo"))
    })
	private SirfelTCausalePK id;

	private String causale;

	//bi-directional many-to-one association to SirfelTFattura
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="ente_proprietario_id", referencedColumnName="ente_proprietario_id", insertable=false, updatable=false),
		@JoinColumn(name="id_fattura", referencedColumnName="id_fattura", insertable=false, updatable=false)
		})
	@MapsId("id")
	private SirfelTFattura sirfelTFattura;
	
	public SirfelTCausale() {
	}

	public SirfelTCausalePK getId() {
		return this.id;
	}

	public void setId(SirfelTCausalePK id) {
		this.id = id;
	}

	public String getCausale() {
		return this.causale;
	}

	public void setCausale(String causale) {
		this.causale = causale;
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