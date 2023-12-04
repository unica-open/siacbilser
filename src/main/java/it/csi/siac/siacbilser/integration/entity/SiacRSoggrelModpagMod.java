/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


// TODO: Auto-generated Javadoc
/**
 * The persistent class for the siac_r_soggrel_modpag_mod database table.
 * 
 */
@Entity
@Table(name="siac_r_soggrel_modpag_mod")
@NamedQuery(name="SiacRSoggrelModpagMod.findAll", query="SELECT s FROM SiacRSoggrelModpagMod s")
public class SiacRSoggrelModpagMod extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The sogrelmpag mod id. */
	@Id
	@SequenceGenerator(name="SIAC_R_SOGGREL_MODPAG_MOD_SOGRELMPAGMODID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_SOGGREL_MODPAG_MOD_SOGRELMPAG_MOD_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_SOGGREL_MODPAG_MOD_SOGRELMPAGMODID_GENERATOR")
	@Column(name="sogrelmpag_mod_id")
	private Integer sogrelmpagModId;

	/** The note. */
	private String note;

	/** The sog mod id. */
	@Column(name="sog_mod_id")
	private Integer sogModId;

	//bi-directional many-to-one association to SiacRSoggettoRelazMod
	/** The siac r soggetto relaz mod. */
	@ManyToOne
	@JoinColumn(name="soggetto_relaz_mod_id")
	private SiacRSoggettoRelazMod siacRSoggettoRelazMod;

	//bi-directional many-to-one association to SiacRSoggrelModpag
	/** The siac r soggrel modpag. */
	@ManyToOne
	@JoinColumn(name="soggrelmpag_id")
	private SiacRSoggrelModpag siacRSoggrelModpag;

	//bi-directional many-to-one association to SiacTModpag
	/** The siac t modpag. */
	@ManyToOne
	@JoinColumn(name="modpag_id")
	private SiacTModpag siacTModpag;

	/**
	 * Instantiates a new siac r soggrel modpag mod.
	 */
	public SiacRSoggrelModpagMod() {
	}

	/**
	 * Gets the sogrelmpag mod id.
	 *
	 * @return the sogrelmpag mod id
	 */
	public Integer getSogrelmpagModId() {
		return this.sogrelmpagModId;
	}

	/**
	 * Sets the sogrelmpag mod id.
	 *
	 * @param sogrelmpagModId the new sogrelmpag mod id
	 */
	public void setSogrelmpagModId(Integer sogrelmpagModId) {
		this.sogrelmpagModId = sogrelmpagModId;
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
	 * Gets the siac r soggetto relaz mod.
	 *
	 * @return the siac r soggetto relaz mod
	 */
	public SiacRSoggettoRelazMod getSiacRSoggettoRelazMod() {
		return this.siacRSoggettoRelazMod;
	}

	/**
	 * Sets the siac r soggetto relaz mod.
	 *
	 * @param siacRSoggettoRelazMod the new siac r soggetto relaz mod
	 */
	public void setSiacRSoggettoRelazMod(SiacRSoggettoRelazMod siacRSoggettoRelazMod) {
		this.siacRSoggettoRelazMod = siacRSoggettoRelazMod;
	}

	/**
	 * Gets the siac r soggrel modpag.
	 *
	 * @return the siac r soggrel modpag
	 */
	public SiacRSoggrelModpag getSiacRSoggrelModpag() {
		return this.siacRSoggrelModpag;
	}

	/**
	 * Sets the siac r soggrel modpag.
	 *
	 * @param siacRSoggrelModpag the new siac r soggrel modpag
	 */
	public void setSiacRSoggrelModpag(SiacRSoggrelModpag siacRSoggrelModpag) {
		this.siacRSoggrelModpag = siacRSoggrelModpag;
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

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return sogrelmpagModId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.sogrelmpagModId = uid;
	}

}