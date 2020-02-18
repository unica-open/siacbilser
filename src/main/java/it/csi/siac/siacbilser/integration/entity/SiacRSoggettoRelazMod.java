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
 * The persistent class for the siac_r_soggetto_relaz_mod database table.
 * 
 */
@Entity
@Table(name="siac_r_soggetto_relaz_mod")
@NamedQuery(name="SiacRSoggettoRelazMod.findAll", query="SELECT s FROM SiacRSoggettoRelazMod s")
public class SiacRSoggettoRelazMod extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The soggetto relaz mod id. */
	@Id
	@SequenceGenerator(name="SIAC_R_SOGGETTO_RELAZ_MOD_SOGGETTORELAZMODID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_SOGGETTO_RELAZ_MOD_SOGGETTO_RELAZ_MOD_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_SOGGETTO_RELAZ_MOD_SOGGETTORELAZMODID_GENERATOR")
	@Column(name="soggetto_relaz_mod_id")
	private Integer soggettoRelazModId;

	/** The sog mod id. */
	@Column(name="sog_mod_id")
	private Integer sogModId;

	//bi-directional many-to-one association to SiacDRelazTipo
	/** The siac d relaz tipo. */
	@ManyToOne
	@JoinColumn(name="relaz_tipo_id")
	private SiacDRelazTipo siacDRelazTipo;

	//bi-directional many-to-one association to SiacDRuolo
	/** The siac d ruolo1. */
	@ManyToOne
	@JoinColumn(name="ruolo_id_a")
	private SiacDRuolo siacDRuolo1;

	//bi-directional many-to-one association to SiacDRuolo
	/** The siac d ruolo2. */
	@ManyToOne
	@JoinColumn(name="ruolo_id_da")
	private SiacDRuolo siacDRuolo2;

	//bi-directional many-to-one association to SiacRSoggettoRelaz
	/** The siac r soggetto relaz. */
	@ManyToOne
	@JoinColumn(name="soggetto_relaz_id")
	private SiacRSoggettoRelaz siacRSoggettoRelaz;

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

	//bi-directional many-to-one association to SiacRSoggrelModpagMod
	/** The siac r soggrel modpag mods. */
	@OneToMany(mappedBy="siacRSoggettoRelazMod")
	private List<SiacRSoggrelModpagMod> siacRSoggrelModpagMods;

	/**
	 * Instantiates a new siac r soggetto relaz mod.
	 */
	public SiacRSoggettoRelazMod() {
	}

	/**
	 * Gets the soggetto relaz mod id.
	 *
	 * @return the soggetto relaz mod id
	 */
	public Integer getSoggettoRelazModId() {
		return this.soggettoRelazModId;
	}

	/**
	 * Sets the soggetto relaz mod id.
	 *
	 * @param soggettoRelazModId the new soggetto relaz mod id
	 */
	public void setSoggettoRelazModId(Integer soggettoRelazModId) {
		this.soggettoRelazModId = soggettoRelazModId;
	}

	/**
	 * Gets the sog mod id.
	 *
	 * @return the sog mod id
	 */
	public Integer getSogModId() {
		return this.sogModId;
	}

	/**
	 * Sets the sog mod id.
	 *
	 * @param sogModId the new sog mod id
	 */
	public void setSogModId(Integer sogModId) {
		this.sogModId = sogModId;
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
	 * Gets the siac r soggetto relaz.
	 *
	 * @return the siac r soggetto relaz
	 */
	public SiacRSoggettoRelaz getSiacRSoggettoRelaz() {
		return this.siacRSoggettoRelaz;
	}

	/**
	 * Sets the siac r soggetto relaz.
	 *
	 * @param siacRSoggettoRelaz the new siac r soggetto relaz
	 */
	public void setSiacRSoggettoRelaz(SiacRSoggettoRelaz siacRSoggettoRelaz) {
		this.siacRSoggettoRelaz = siacRSoggettoRelaz;
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
	 * Gets the siac r soggrel modpag mods.
	 *
	 * @return the siac r soggrel modpag mods
	 */
	public List<SiacRSoggrelModpagMod> getSiacRSoggrelModpagMods() {
		return this.siacRSoggrelModpagMods;
	}

	/**
	 * Sets the siac r soggrel modpag mods.
	 *
	 * @param siacRSoggrelModpagMods the new siac r soggrel modpag mods
	 */
	public void setSiacRSoggrelModpagMods(List<SiacRSoggrelModpagMod> siacRSoggrelModpagMods) {
		this.siacRSoggrelModpagMods = siacRSoggrelModpagMods;
	}

	/**
	 * Adds the siac r soggrel modpag mod.
	 *
	 * @param siacRSoggrelModpagMod the siac r soggrel modpag mod
	 * @return the siac r soggrel modpag mod
	 */
	public SiacRSoggrelModpagMod addSiacRSoggrelModpagMod(SiacRSoggrelModpagMod siacRSoggrelModpagMod) {
		getSiacRSoggrelModpagMods().add(siacRSoggrelModpagMod);
		siacRSoggrelModpagMod.setSiacRSoggettoRelazMod(this);

		return siacRSoggrelModpagMod;
	}

	/**
	 * Removes the siac r soggrel modpag mod.
	 *
	 * @param siacRSoggrelModpagMod the siac r soggrel modpag mod
	 * @return the siac r soggrel modpag mod
	 */
	public SiacRSoggrelModpagMod removeSiacRSoggrelModpagMod(SiacRSoggrelModpagMod siacRSoggrelModpagMod) {
		getSiacRSoggrelModpagMods().remove(siacRSoggrelModpagMod);
		siacRSoggrelModpagMod.setSiacRSoggettoRelazMod(null);

		return siacRSoggrelModpagMod;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return soggettoRelazModId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.soggettoRelazModId = uid;
	}

}