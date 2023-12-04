/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the sirfel_t_fattura_contabile database table.
 * 
 */
@Entity
@Table(name="sirfel_t_fattura_contabile")
@NamedQuery(name="SirfelTFatturaContabile.findAll", query="SELECT s FROM SirfelTFatturaContabile s")
public class SirfelTFatturaContabile extends SirfelTBase<SirfelTFatturaContabilePK> {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private SirfelTFatturaContabilePK id;

	//bi-directional many-to-one association to SirfelTFattura
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="ente_proprietario_id", referencedColumnName="ente_proprietario_id"),
		@JoinColumn(name="id_fattura", referencedColumnName="id_fattura")
		})
	@MapsId("id")
	private SirfelTFattura sirfelTFattura;

	public SirfelTFatturaContabile() {
	}

	public SirfelTFatturaContabilePK getId() {
		return this.id;
	}

	public void setId(SirfelTFatturaContabilePK id) {
		this.id = id;
	}

	public SirfelTFattura getSirfelTFattura() {
		return this.sirfelTFattura;
	}

	public void setSirfelTFattura(SirfelTFattura sirfelTFattura) {
		this.sirfelTFattura = sirfelTFattura;
	}

	public String getEu() {
		return getId() != null ? getId().getEu() : null;
	}
	
	public Long getCodben() {
		return getId() != null ? getId().getCodben() : null;
	}
	
	public String getAnnofat() {
		return getId() != null ? getId().getAnnofat() : null;
	}
	
	public String getNfatt() {
		return getId() != null ? getId().getNfatt() : null;
	}
	
	public String getTipofatt() {
		return getId() != null ? getId().getTipofatt() : null;
	}

}