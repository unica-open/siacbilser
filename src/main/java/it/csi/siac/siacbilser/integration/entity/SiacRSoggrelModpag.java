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
 * The persistent class for the siac_r_soggrel_modpag database table.
 * 
 */
@Entity
@Table(name="siac_r_soggrel_modpag")
@NamedQuery(name="SiacRSoggrelModpag.findAll", query="SELECT s FROM SiacRSoggrelModpag s")
public class SiacRSoggrelModpag extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The soggrelmpag id. */
	@Id
	@SequenceGenerator(name="SIAC_R_SOGGREL_MODPAG_SOGGRELMPAGID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_SOGGREL_MODPAG_SOGGRELMPAG_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_SOGGREL_MODPAG_SOGGRELMPAGID_GENERATOR")
	@Column(name="soggrelmpag_id")
	private Integer soggrelmpagId;

	/** The note. */
	private String note;

	//bi-directional many-to-one association to SiacRModpagOrdine
	@OneToMany(mappedBy="siacRSoggrelModpag")
	private List<SiacRModpagOrdine> siacRModpagOrdines;

	//bi-directional many-to-one association to SiacRSoggettoRelaz
	/** The siac r soggetto relaz. */
	@ManyToOne
	@JoinColumn(name="soggetto_relaz_id")
	private SiacRSoggettoRelaz siacRSoggettoRelaz;

	//bi-directional many-to-one association to SiacTModpag
	/** The siac t modpag. */
	@ManyToOne
	@JoinColumn(name="modpag_id")
	private SiacTModpag siacTModpag;

	//bi-directional many-to-one association to SiacRSoggrelModpagMod
	/** The siac r soggrel modpag mods. */
	@OneToMany(mappedBy="siacRSoggrelModpag")
	private List<SiacRSoggrelModpagMod> siacRSoggrelModpagMods;

	//bi-directional many-to-one association to SiacRSubdocModpag
	/** The siac r subdoc modpags. */
	@OneToMany(mappedBy="siacRSoggrelModpag")
	private List<SiacRSubdocModpag> siacRSubdocModpags;

	/**
	 * Instantiates a new siac r soggrel modpag.
	 */
	public SiacRSoggrelModpag() {
	}

	/**
	 * Gets the soggrelmpag id.
	 *
	 * @return the soggrelmpag id
	 */
	public Integer getSoggrelmpagId() {
		return this.soggrelmpagId;
	}

	/**
	 * Sets the soggrelmpag id.
	 *
	 * @param soggrelmpagId the new soggrelmpag id
	 */
	public void setSoggrelmpagId(Integer soggrelmpagId) {
		this.soggrelmpagId = soggrelmpagId;
	}

	

	/**
	 * Gets the note.
	 *
	 * @return the note
	 */
	public String getNote() {
		return this.note;
	}

	/**
	 * Sets the note.
	 *
	 * @param note the new note
	 */
	public void setNote(String note) {
		this.note = note;
	}

	/**
	 * Gets the siac r soggetto relaz.
	 *
	 * @return the siac r soggetto relaz
	 */
	public List<SiacRModpagOrdine> getSiacRModpagOrdines() {
		return this.siacRModpagOrdines;
	}

	public void setSiacRModpagOrdines(List<SiacRModpagOrdine> siacRModpagOrdines) {
		this.siacRModpagOrdines = siacRModpagOrdines;
	}

	public SiacRModpagOrdine addSiacRModpagOrdine(SiacRModpagOrdine siacRModpagOrdine) {
		getSiacRModpagOrdines().add(siacRModpagOrdine);
		siacRModpagOrdine.setSiacRSoggrelModpag(this);

		return siacRModpagOrdine;
	}

	public SiacRModpagOrdine removeSiacRModpagOrdine(SiacRModpagOrdine siacRModpagOrdine) {
		getSiacRModpagOrdines().remove(siacRModpagOrdine);
		siacRModpagOrdine.setSiacRSoggrelModpag(null);

		return siacRModpagOrdine;
	}

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
	 * Gets the siac t modpag.
	 *
	 * @return the siac t modpag
	 */
	public SiacTModpag getSiacTModpag() {
		return this.siacTModpag;
	}

	/**
	 * Sets the siac t modpag.
	 *
	 * @param siacTModpag the new siac t modpag
	 */
	public void setSiacTModpag(SiacTModpag siacTModpag) {
		this.siacTModpag = siacTModpag;
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
		siacRSoggrelModpagMod.setSiacRSoggrelModpag(this);

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
		siacRSoggrelModpagMod.setSiacRSoggrelModpag(null);

		return siacRSoggrelModpagMod;
	}

	/**
	 * Gets the siac r subdoc modpags.
	 *
	 * @return the siac r subdoc modpags
	 */
	public List<SiacRSubdocModpag> getSiacRSubdocModpags() {
		return this.siacRSubdocModpags;
	}

	/**
	 * Sets the siac r subdoc modpags.
	 *
	 * @param siacRSubdocModpags the new siac r subdoc modpags
	 */
	public void setSiacRSubdocModpags(List<SiacRSubdocModpag> siacRSubdocModpags) {
		this.siacRSubdocModpags = siacRSubdocModpags;
	}

	/**
	 * Adds the siac r subdoc modpag.
	 *
	 * @param siacRSubdocModpag the siac r subdoc modpag
	 * @return the siac r subdoc modpag
	 */
	public SiacRSubdocModpag addSiacRSubdocModpag(SiacRSubdocModpag siacRSubdocModpag) {
		getSiacRSubdocModpags().add(siacRSubdocModpag);
		siacRSubdocModpag.setSiacRSoggrelModpag(this);

		return siacRSubdocModpag;
	}

	/**
	 * Removes the siac r subdoc modpag.
	 *
	 * @param siacRSubdocModpag the siac r subdoc modpag
	 * @return the siac r subdoc modpag
	 */
	public SiacRSubdocModpag removeSiacRSubdocModpag(SiacRSubdocModpag siacRSubdocModpag) {
		getSiacRSubdocModpags().remove(siacRSubdocModpag);
		siacRSubdocModpag.setSiacRSoggrelModpag(null);

		return siacRSubdocModpag;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return soggrelmpagId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.soggrelmpagId = uid;
	}

}