/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;


/**
 * The persistent class for the sirfel_d_tipo_documento database table.
 * 
 */
@Entity
@Table(name="sirfel_d_tipo_documento")
@NamedQuery(name="SirfelDTipoDocumento.findAll", query="SELECT s FROM SirfelDTipoDocumento s")
public class SirfelDTipoDocumento extends SirfelTBase<SirfelDTipoDocumentoPK> {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private SirfelDTipoDocumentoPK id;

	private String descrizione;

	@Column(name="flag_bilancio")
	private String flagBilancio;

	//bi-directional many-to-one association to SirfelTFattura
	@OneToMany(mappedBy="sirfelDTipoDocumento")
	private List<SirfelTFattura> sirfelTFatturas;

//	//bi-directional many-to-one association to SirfelTFattura
//	@OneToMany(mappedBy="sirfelDTipoDocumento2")
//	private List<SirfelTFattura> sirfelTFatturas2;

	public SirfelDTipoDocumento() {
	}

	public SirfelDTipoDocumentoPK getId() {
		return this.id;
	}

	public void setId(SirfelDTipoDocumentoPK id) {
		this.id = id;
	}

	public String getDescrizione() {
		return this.descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public String getFlagBilancio() {
		return this.flagBilancio;
	}

	public void setFlagBilancio(String flagBilancio) {
		this.flagBilancio = flagBilancio;
	}

	public List<SirfelTFattura> getSirfelTFatturas() {
		return this.sirfelTFatturas;
	}

	public void setSirfelTFatturas(List<SirfelTFattura> sirfelTFatturas) {
		this.sirfelTFatturas = sirfelTFatturas;
	}

	public SirfelTFattura addSirfelTFatturas(SirfelTFattura sirfelTFatturas) {
		getSirfelTFatturas().add(sirfelTFatturas);
		sirfelTFatturas.setSirfelDTipoDocumento(this);

		return sirfelTFatturas;
	}

	public SirfelTFattura removeSirfelTFatturas(SirfelTFattura sirfelTFatturas) {
		getSirfelTFatturas().remove(sirfelTFatturas);
		sirfelTFatturas.setSirfelDTipoDocumento(null);

		return sirfelTFatturas;
	}
	
	public String getCodice() {
		return getId() != null ? getId().getCodice() : null;
	}

}