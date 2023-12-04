/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import java.util.Date;

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
 * The persistent class for the sirfel_t_fatture_collegate database table.
 * 
 */
@Entity
@Table(name="sirfel_t_fatture_collegate")
@NamedQuery(name="SirfelTFattureCollegate.findAll", query="SELECT s FROM SirfelTFattureCollegate s")
public class SirfelTFattureCollegate extends SirfelTBase<SirfelTFattureCollegatePK> {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private SirfelTFattureCollegatePK id;

	@Column(name="codice_cig")
	private String codiceCig;

	@Column(name="codice_cup")
	private String codiceCup;

	private Date data;

	private String numero;

	//bi-directional many-to-one association to SirfelTFattura
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="ente_proprietario_id", referencedColumnName="ente_proprietario_id", insertable=false, updatable=false),
		@JoinColumn(name="id_fattura", referencedColumnName="id_fattura", insertable=false, updatable=false)
		})
	@MapsId("id")
	private SirfelTFattura sirfelTFattura;
	
	public SirfelTFattureCollegate() {
	}

	public SirfelTFattureCollegatePK getId() {
		return this.id;
	}

	public void setId(SirfelTFattureCollegatePK id) {
		this.id = id;
	}

	public String getCodiceCig() {
		return this.codiceCig;
	}

	public void setCodiceCig(String codiceCig) {
		this.codiceCig = codiceCig;
	}

	public String getCodiceCup() {
		return this.codiceCup;
	}

	public void setCodiceCup(String codiceCup) {
		this.codiceCup = codiceCup;
	}

	public Date getData() {
		return this.data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public String getNumero() {
		return this.numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
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