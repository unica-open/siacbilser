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
 * The persistent class for the sirfel_d_tipo_cassa database table.
 * 
 */
@Entity
@Table(name="sirfel_d_tipo_cassa")
@NamedQuery(name="SirfelDTipoCassa.findAll", query="SELECT s FROM SirfelDTipoCassa s")
public class SirfelDTipoCassa extends SirfelTBase<SirfelDTipoCassaPK> {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private SirfelDTipoCassaPK id;

	private String descrizione;

	//bi-directional many-to-one association to SirfelTCassaPrevidenziale
	@OneToMany(mappedBy="sirfelDTipoCassa")
	private List<SirfelTCassaPrevidenziale> sirfelTCassaPrevidenziales;

//	//bi-directional many-to-one association to SirfelTCassaPrevidenziale
//	@OneToMany(mappedBy="sirfelDTipoCassa2")
//	private List<SirfelTCassaPrevidenziale> sirfelTCassaPrevidenziales2;

	public SirfelDTipoCassa() {
	}

	public SirfelDTipoCassaPK getId() {
		return this.id;
	}

	public void setId(SirfelDTipoCassaPK id) {
		this.id = id;
	}

	public String getDescrizione() {
		return this.descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public List<SirfelTCassaPrevidenziale> getSirfelTCassaPrevidenziales() {
		return this.sirfelTCassaPrevidenziales;
	}

	public void setSirfelTCassaPrevidenziales(List<SirfelTCassaPrevidenziale> sirfelTCassaPrevidenziales) {
		this.sirfelTCassaPrevidenziales = sirfelTCassaPrevidenziales;
	}

	public SirfelTCassaPrevidenziale addSirfelTCassaPrevidenziales(SirfelTCassaPrevidenziale sirfelTCassaPrevidenziales) {
		getSirfelTCassaPrevidenziales().add(sirfelTCassaPrevidenziales);
		sirfelTCassaPrevidenziales.setSirfelDTipoCassa(this);

		return sirfelTCassaPrevidenziales;
	}

	public SirfelTCassaPrevidenziale removeSirfelTCassaPrevidenziales(SirfelTCassaPrevidenziale sirfelTCassaPrevidenziales) {
		getSirfelTCassaPrevidenziales().remove(sirfelTCassaPrevidenziales);
		sirfelTCassaPrevidenziales.setSirfelDTipoCassa(null);

		return sirfelTCassaPrevidenziales;
	}
	
	public String getCodice() {
		return getId() != null ? getId().getCodice() : null;
	}

}