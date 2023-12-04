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
 * The persistent class for the sirfel_d_natura database table.
 * 
 */
@Entity
@Table(name="sirfel_d_natura")
@NamedQuery(name="SirfelDNatura.findAll", query="SELECT s FROM SirfelDNatura s")
public class SirfelDNatura extends SirfelTBase<SirfelDNaturaPK> {
	private static final long serialVersionUID = 1L;
 
	
	//SIAC-7557 inizio FL
	@EmbeddedId
	@AttributeOverrides({
        @AttributeOverride(name="enteProprietarioId",
                           column=@Column(name="ente_proprietario_id")),
        @AttributeOverride(name="codice",
                           column=@Column(name="codice"))
    })
	private SirfelDNaturaPK id;

	private String descrizione;

	//bi-directional many-to-one association to SirfelTCassaPrevidenziale
	@OneToMany(mappedBy="sirfelDNatura")
	private List<SirfelTCassaPrevidenziale> sirfelTCassaPrevidenziales;

//	//bi-directional many-to-one association to SirfelTCassaPrevidenziale
//	@OneToMany(mappedBy="sirfelDNatura2")
//	private List<SirfelTCassaPrevidenziale> sirfelTCassaPrevidenziales2;

	//bi-directional many-to-one association to SirfelTRiepilogoBeni
	@OneToMany(mappedBy="sirfelDNatura")
	private List<SirfelTRiepilogoBeni> sirfelTRiepilogoBenis;

//	//bi-directional many-to-one association to SirfelTRiepilogoBeni
//	@OneToMany(mappedBy="sirfelDNatura2")
//	private List<SirfelTRiepilogoBeni> sirfelTRiepilogoBenis2;

	
	
	
	public SirfelDNatura() {
	}

	public SirfelDNaturaPK getId() {
		return this.id;
	}

	public void setId(SirfelDNaturaPK id) {
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
		sirfelTCassaPrevidenziales.setSirfelDNatura(this);

		return sirfelTCassaPrevidenziales;
	}

	public SirfelTCassaPrevidenziale removeSirfelTCassaPrevidenziales(SirfelTCassaPrevidenziale sirfelTCassaPrevidenziales) {
		getSirfelTCassaPrevidenziales().remove(sirfelTCassaPrevidenziales);
		sirfelTCassaPrevidenziales.setSirfelDNatura(null);

		return sirfelTCassaPrevidenziales;
	}

	public List<SirfelTRiepilogoBeni> getSirfelTRiepilogoBenis() {
		return this.sirfelTRiepilogoBenis;
	}

	public void setSirfelTRiepilogoBenis(List<SirfelTRiepilogoBeni> sirfelTRiepilogoBenis) {
		this.sirfelTRiepilogoBenis = sirfelTRiepilogoBenis;
	}

	public SirfelTRiepilogoBeni addSirfelTRiepilogoBenis(SirfelTRiepilogoBeni sirfelTRiepilogoBenis) {
		getSirfelTRiepilogoBenis().add(sirfelTRiepilogoBenis);
		sirfelTRiepilogoBenis.setSirfelDNatura(this);

		return sirfelTRiepilogoBenis;
	}

	public SirfelTRiepilogoBeni removeSirfelTRiepilogoBenis(SirfelTRiepilogoBeni sirfelTRiepilogoBenis) {
		getSirfelTRiepilogoBenis().remove(sirfelTRiepilogoBenis);
		sirfelTRiepilogoBenis.setSirfelDNatura(null);

		return sirfelTRiepilogoBenis;
	}

	public String getCodice() {
		return getId() != null ? getId().getCodice() : null;
	}

}