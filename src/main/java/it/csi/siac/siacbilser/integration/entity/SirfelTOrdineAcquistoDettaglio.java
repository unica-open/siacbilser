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
 * The persistent class for the sirfel_t_ordine_acquisto_dettaglio database table.
 * 
 */
@Entity
@Table(name="sirfel_t_ordine_acquisto_dettaglio")
@NamedQuery(name="SirfelTOrdineAcquistoDettaglio.findAll", query="SELECT s FROM SirfelTOrdineAcquistoDettaglio s")
public class SirfelTOrdineAcquistoDettaglio extends SirfelTBase<SirfelTOrdineAcquistoDettaglioPK> {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	@AttributeOverrides({
		@AttributeOverride(name="enteProprietarioId", column=@Column(name="ente_proprietario_id")),
		@AttributeOverride(name="idFattura", column=@Column(name="id_fattura")),
		@AttributeOverride(name="numeroDocumento", column=@Column(name="numero_documento"))
	})
	private SirfelTOrdineAcquistoDettaglioPK id;

	//bi-directional many-to-one association to SirfelTOrdineAcquisto
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="ente_proprietario_id", referencedColumnName="ente_proprietario_id", insertable=false, updatable=false),
		@JoinColumn(name="id_fattura", referencedColumnName="id_fattura", insertable=false, updatable=false),
		@JoinColumn(name="numero_documento", referencedColumnName="numero_documento", insertable=false, updatable=false)
		})
	@MapsId("id")
	private SirfelTOrdineAcquisto sirfelTOrdineAcquisto;
	
	public SirfelTOrdineAcquistoDettaglio() {
	}

	public SirfelTOrdineAcquistoDettaglioPK getId() {
		return this.id;
	}

	public void setId(SirfelTOrdineAcquistoDettaglioPK id) {
		this.id = id;
	}

	public SirfelTOrdineAcquisto getSirfelTOrdineAcquisto() {
		return this.sirfelTOrdineAcquisto;
	}

	public void setSirfelTOrdineAcquisto(SirfelTOrdineAcquisto sirfelTOrdineAcquisto) {
		this.sirfelTOrdineAcquisto = sirfelTOrdineAcquisto;
	}
	
	public Integer getNumeroDettaglio() {
		return getId() != null ? getId().getNumeroDettaglio() : null;
	}

}