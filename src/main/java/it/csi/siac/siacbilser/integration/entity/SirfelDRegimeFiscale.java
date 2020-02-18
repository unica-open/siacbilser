/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import java.util.List;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;


/**
 * The persistent class for the sirfel_d_regime_fiscale database table.
 * 
 */
@Entity
@Table(name="sirfel_d_regime_fiscale")
@NamedQuery(name="SirfelDRegimeFiscale.findAll", query="SELECT s FROM SirfelDRegimeFiscale s")
public class SirfelDRegimeFiscale extends SirfelTBase<SirfelDRegimeFiscalePK> {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private SirfelDRegimeFiscalePK id;

	private String descrizione;

	//bi-directional many-to-one association to SirfelTPrestatore
	@OneToMany(mappedBy="sirfelDRegimeFiscale")
	private List<SirfelTPrestatore> sirfelTPrestatores;

//	//bi-directional many-to-one association to SirfelTPrestatore
//	@OneToMany(mappedBy="sirfelDRegimeFiscale2")
//	private List<SirfelTPrestatore> sirfelTPrestatores2;

	public SirfelDRegimeFiscale() {
	}

	public SirfelDRegimeFiscalePK getId() {
		return this.id;
	}

	public void setId(SirfelDRegimeFiscalePK id) {
		this.id = id;
	}

	public String getDescrizione() {
		return this.descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public List<SirfelTPrestatore> getSirfelTPrestatores() {
		return this.sirfelTPrestatores;
	}

	public void setSirfelTPrestatores(List<SirfelTPrestatore> sirfelTPrestatores) {
		this.sirfelTPrestatores = sirfelTPrestatores;
	}

	public SirfelTPrestatore addSirfelTPrestatores(SirfelTPrestatore sirfelTPrestatores) {
		getSirfelTPrestatores().add(sirfelTPrestatores);
		sirfelTPrestatores.setSirfelDRegimeFiscale(this);

		return sirfelTPrestatores;
	}

	public SirfelTPrestatore removeSirfelTPrestatores(SirfelTPrestatore sirfelTPrestatores) {
		getSirfelTPrestatores().remove(sirfelTPrestatores);
		sirfelTPrestatores.setSirfelDRegimeFiscale(null);

		return sirfelTPrestatores;
	}
	
	public String getCodice() {
		return getId() != null ? getId().getCodice() : null;
	}
}