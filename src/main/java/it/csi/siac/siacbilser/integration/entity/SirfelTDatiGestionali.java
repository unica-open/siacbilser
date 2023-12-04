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
 * The persistent class for the sirfel_t_dati_gestionali database table.
 * 
 */
@Entity
@Table(name="sirfel_t_dati_gestionali")
@NamedQuery(name="SirfelTDatiGestionali.findAll", query="SELECT s FROM SirfelTDatiGestionali s")
public class SirfelTDatiGestionali extends SirfelTBase<SirfelTDatiGestionaliPK> {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private SirfelTDatiGestionaliPK id;

	@Column(name="riferimento_data")
	private Date riferimentoData;

	@Column(name="tipo_dato")
	private String tipoDato;

	//bi-directional many-to-one association to SirfelTFattura
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="ente_proprietario_id", referencedColumnName="ente_proprietario_id"),
		@JoinColumn(name="id_fattura", referencedColumnName="id_fattura")
		})
	@MapsId("id")
	private SirfelTFattura sirfelTFattura;

	public SirfelTDatiGestionali() {
	}

	public SirfelTDatiGestionaliPK getId() {
		return this.id;
	}

	public void setId(SirfelTDatiGestionaliPK id) {
		this.id = id;
	}

	public Date getRiferimentoData() {
		return this.riferimentoData;
	}

	public void setRiferimentoData(Date riferimentoData) {
		this.riferimentoData = riferimentoData;
	}

	public String getTipoDato() {
		return this.tipoDato;
	}

	public void setTipoDato(String tipoDato) {
		this.tipoDato = tipoDato;
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