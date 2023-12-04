/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


// TODO: Auto-generated Javadoc
/**
 * The persistent class for the siac_r_soggetto_relaz database table.
 * 
 */
@Entity
@Table(name="siac_r_soggetto_relaz")
@NamedQuery(name="SiacRSoggettoRelaz.findAll", query="SELECT s FROM SiacRSoggettoRelaz s")
public class SiacRSoggettoRelaz extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The soggetto relaz id. */
	@Id
	@SequenceGenerator(name="SIAC_R_SOGGETTO_RELAZ_SOGGETTORELAZID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_SOGGETTO_RELAZ_SOGGETTO_RELAZ_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_SOGGETTO_RELAZ_SOGGETTORELAZID_GENERATOR")
	@Column(name="soggetto_relaz_id")
	private Integer soggettoRelazId;

	//bi-directional many-to-one association to SiacDRelazTipo
	/** The siac d relaz tipo. */
	@ManyToOne
	@JoinColumn(name="relaz_tipo_id")
	private SiacDRelazTipo siacDRelazTipo;

	//bi-directional many-to-one association to SiacDRuolo
	/** The siac d ruolo1. */
	@ManyToOne
	@JoinColumn(name="ruolo_id_da")
	private SiacDRuolo siacDRuolo1;

	//bi-directional many-to-one association to SiacDRuolo
	/** The siac d ruolo2. */
	@ManyToOne
	@JoinColumn(name="ruolo_id_a")
	private SiacDRuolo siacDRuolo2;

	//bi-directional many-to-one association to SiacTSoggetto
	/** The siac t soggetto1. */
	@ManyToOne
	@JoinColumn(name="soggetto_id_da")
	private SiacTSoggetto siacTSoggetto1;

	//bi-directional many-to-one association to SiacTSoggetto
	/** The siac t soggetto2. */
	@ManyToOne
	@JoinColumn(name="soggetto_id_a")
	private SiacTSoggetto siacTSoggetto2;

	//bi-directional many-to-one association to SiacRSoggettoRelazMod
	/** The siac r soggetto relaz mods. */
	@OneToMany(mappedBy="siacRSoggettoRelaz")
	private List<SiacRSoggettoRelazMod> siacRSoggettoRelazMods;

	//bi-directional many-to-one association to SiacRSoggettoRelazStato
	/** The siac r soggetto relaz statos. */
	@OneToMany(mappedBy="siacRSoggettoRelaz")
	private List<SiacRSoggettoRelazStato> siacRSoggettoRelazStatos;

	//bi-directional many-to-one association to SiacRSoggrelModpag
	/** The siac r soggrel modpags. */
	@OneToMany(mappedBy="siacRSoggettoRelaz")
	private List<SiacRSoggrelModpag> siacRSoggrelModpags;

	/**
	 * Instantiates a new siac r soggetto relaz.
	 */
	public SiacRSoggettoRelaz() {
	}

	/**
	 * Gets the soggetto relaz id.
	 *
	 * @return the soggetto relaz id
	 */
	public Integer getSoggettoRelazId() {
		return this.soggettoRelazId;
	}

	/**
	 * Sets the soggetto relaz id.
	 *
	 * @param soggettoRelazId the new soggetto relaz id
	 */
	public void setSoggettoRelazId(Integer soggettoRelazId) {
		this.soggettoRelazId = soggettoRelazId;
	}

	/**
	 * Gets the siac d relaz tipo.
	 *
	 * @return the siac d relaz tipo
	 */
	public SiacDRelazTipo getSiacDRelazTipo() {
		return this.siacDRelazTipo;
	}

	/**
	 * Sets the siac d relaz tipo.
	 *
	 * @param siacDRelazTipo the new siac d relaz tipo
	 */
	public void setSiacDRelazTipo(SiacDRelazTipo siacDRelazTipo) {
		this.siacDRelazTipo = siacDRelazTipo;
	}

	/**
	 * Gets the siac d ruolo1.
	 *
	 * @return the siac d ruolo1
	 */
	public SiacDRuolo getSiacDRuolo1() {
		return this.siacDRuolo1;
	}

	/**
	 * Sets the siac d ruolo1.
	 *
	 * @param siacDRuolo1 the new siac d ruolo1
	 */
	public void setSiacDRuolo1(SiacDRuolo siacDRuolo1) {
		this.siacDRuolo1 = siacDRuolo1;
	}

	/**
	 * Gets the siac d ruolo2.
	 *
	 * @return the siac d ruolo2
	 */
	public SiacDRuolo getSiacDRuolo2() {
		return this.siacDRuolo2;
	}

	/**
	 * Sets the siac d ruolo2.
	 *
	 * @param siacDRuolo2 the new siac d ruolo2
	 */
	public void setSiacDRuolo2(SiacDRuolo siacDRuolo2) {
		this.siacDRuolo2 = siacDRuolo2;
	}

	/**
	 * Gets the siac t soggetto1.
	 *
	 * @return the siac t soggetto1
	 */
	public SiacTSoggetto getSiacTSoggetto1() {
		return this.siacTSoggetto1;
	}

	/**
	 * Sets the siac t soggetto1.
	 *
	 * @param siacTSoggetto1 the new siac t soggetto1
	 */
	public void setSiacTSoggetto1(SiacTSoggetto siacTSoggetto1) {
		this.siacTSoggetto1 = siacTSoggetto1;
	}

	/**
	 * Gets the siac t soggetto2.
	 *
	 * @return the siac t soggetto2
	 */
	public SiacTSoggetto getSiacTSoggetto2() {
		return this.siacTSoggetto2;
	}

	/**
	 * Sets the siac t soggetto2.
	 *
	 * @param siacTSoggetto2 the new siac t soggetto2
	 */
	public void setSiacTSoggetto2(SiacTSoggetto siacTSoggetto2) {
		this.siacTSoggetto2 = siacTSoggetto2;
	}

	/**
	 * Gets the siac r soggetto relaz mods.
	 *
	 * @return the siac r soggetto relaz mods
	 */
	public List<SiacRSoggettoRelazMod> getSiacRSoggettoRelazMods() {
		return this.siacRSoggettoRelazMods;
	}

	/**
	 * Sets the siac r soggetto relaz mods.
	 *
	 * @param siacRSoggettoRelazMods the new siac r soggetto relaz mods
	 */
	public void setSiacRSoggettoRelazMods(List<SiacRSoggettoRelazMod> siacRSoggettoRelazMods) {
		this.siacRSoggettoRelazMods = siacRSoggettoRelazMods;
	}

	/**
	 * Adds the siac r soggetto relaz mod.
	 *
	 * @param siacRSoggettoRelazMod the siac r soggetto relaz mod
	 * @return the siac r soggetto relaz mod
	 */
	public SiacRSoggettoRelazMod addSiacRSoggettoRelazMod(SiacRSoggettoRelazMod siacRSoggettoRelazMod) {
		getSiacRSoggettoRelazMods().add(siacRSoggettoRelazMod);
		siacRSoggettoRelazMod.setSiacRSoggettoRelaz(this);

		return siacRSoggettoRelazMod;
	}

	/**
	 * Removes the siac r soggetto relaz mod.
	 *
	 * @param siacRSoggettoRelazMod the siac r soggetto relaz mod
	 * @return the siac r soggetto relaz mod
	 */
	public SiacRSoggettoRelazMod removeSiacRSoggettoRelazMod(SiacRSoggettoRelazMod siacRSoggettoRelazMod) {
		getSiacRSoggettoRelazMods().remove(siacRSoggettoRelazMod);
		siacRSoggettoRelazMod.setSiacRSoggettoRelaz(null);

		return siacRSoggettoRelazMod;
	}

	/**
	 * Gets the siac r soggetto relaz statos.
	 *
	 * @return the siac r soggetto relaz statos
	 */
	public List<SiacRSoggettoRelazStato> getSiacRSoggettoRelazStatos() {
		return this.siacRSoggettoRelazStatos;
	}

	/**
	 * Sets the siac r soggetto relaz statos.
	 *
	 * @param siacRSoggettoRelazStatos the new siac r soggetto relaz statos
	 */
	public void setSiacRSoggettoRelazStatos(List<SiacRSoggettoRelazStato> siacRSoggettoRelazStatos) {
		this.siacRSoggettoRelazStatos = siacRSoggettoRelazStatos;
	}

	/**
	 * Adds the siac r soggetto relaz stato.
	 *
	 * @param siacRSoggettoRelazStato the siac r soggetto relaz stato
	 * @return the siac r soggetto relaz stato
	 */
	public SiacRSoggettoRelazStato addSiacRSoggettoRelazStato(SiacRSoggettoRelazStato siacRSoggettoRelazStato) {
		getSiacRSoggettoRelazStatos().add(siacRSoggettoRelazStato);
		siacRSoggettoRelazStato.setSiacRSoggettoRelaz(this);

		return siacRSoggettoRelazStato;
	}

	/**
	 * Removes the siac r soggetto relaz stato.
	 *
	 * @param siacRSoggettoRelazStato the siac r soggetto relaz stato
	 * @return the siac r soggetto relaz stato
	 */
	public SiacRSoggettoRelazStato removeSiacRSoggettoRelazStato(SiacRSoggettoRelazStato siacRSoggettoRelazStato) {
		getSiacRSoggettoRelazStatos().remove(siacRSoggettoRelazStato);
		siacRSoggettoRelazStato.setSiacRSoggettoRelaz(null);

		return siacRSoggettoRelazStato;
	}

	/**
	 * Gets the siac r soggrel modpags.
	 *
	 * @return the siac r soggrel modpags
	 */
	public List<SiacRSoggrelModpag> getSiacRSoggrelModpags() {
		return this.siacRSoggrelModpags;
	}

	/**
	 * Sets the siac r soggrel modpags.
	 *
	 * @param siacRSoggrelModpags the new siac r soggrel modpags
	 */
	public void setSiacRSoggrelModpags(List<SiacRSoggrelModpag> siacRSoggrelModpags) {
		this.siacRSoggrelModpags = siacRSoggrelModpags;
	}

	/**
	 * Adds the siac r soggrel modpag.
	 *
	 * @param siacRSoggrelModpag the siac r soggrel modpag
	 * @return the siac r soggrel modpag
	 */
	public SiacRSoggrelModpag addSiacRSoggrelModpag(SiacRSoggrelModpag siacRSoggrelModpag) {
		getSiacRSoggrelModpags().add(siacRSoggrelModpag);
		siacRSoggrelModpag.setSiacRSoggettoRelaz(this);

		return siacRSoggrelModpag;
	}

	/**
	 * Removes the siac r soggrel modpag.
	 *
	 * @param siacRSoggrelModpag the siac r soggrel modpag
	 * @return the siac r soggrel modpag
	 */
	public SiacRSoggrelModpag removeSiacRSoggrelModpag(SiacRSoggrelModpag siacRSoggrelModpag) {
		getSiacRSoggrelModpags().remove(siacRSoggrelModpag);
		siacRSoggrelModpag.setSiacRSoggettoRelaz(null);

		return siacRSoggrelModpag;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return soggettoRelazId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.soggettoRelazId = uid;
	}

}