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
 * The persistent class for the siac_r_soggetto_onere_mod database table.
 * 
 */
@Entity
@Table(name="siac_r_soggetto_onere_mod")
@NamedQuery(name="SiacRSoggettoOnereMod.findAll", query="SELECT s FROM SiacRSoggettoOnereMod s")
public class SiacRSoggettoOnereMod extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The soggetto onere mod id. */
	@Id
	@SequenceGenerator(name="SIAC_R_SOGGETTO_ONERE_MOD_SOGGETTOONEREMODID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_SOGGETTO_ONERE_MOD_SOGGETTO_ONERE_MOD_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_SOGGETTO_ONERE_MOD_SOGGETTOONEREMODID_GENERATOR")
	@Column(name="soggetto_onere_mod_id")
	private Integer soggettoOnereModId;

	

	//bi-directional many-to-one association to SiacDOnere
	/** The siac d onere. */
	@ManyToOne
	@JoinColumn(name="onere_id")
	private SiacDOnere siacDOnere;

	//bi-directional many-to-one association to SiacTSoggetto
	/** The siac t soggetto. */
	@ManyToOne
	@JoinColumn(name="soggetto_id")
	private SiacTSoggetto siacTSoggetto;

	//bi-directional many-to-one association to SiacTSoggettoMod
	/** The siac t soggetto mod. */
	@ManyToOne
	@JoinColumn(name="sog_mod_id")
	private SiacTSoggettoMod siacTSoggettoMod;

	/**
	 * Instantiates a new siac r soggetto onere mod.
	 */
	public SiacRSoggettoOnereMod() {
	}

	/**
	 * Gets the soggetto onere mod id.
	 *
	 * @return the soggetto onere mod id
	 */
	public Integer getSoggettoOnereModId() {
		return this.soggettoOnereModId;
	}

	/**
	 * Sets the soggetto onere mod id.
	 *
	 * @param soggettoOnereModId the new soggetto onere mod id
	 */
	public void setSoggettoOnereModId(Integer soggettoOnereModId) {
		this.soggettoOnereModId = soggettoOnereModId;
	}

	

	/**
	 * Gets the siac d onere.
	 *
	 * @return the siac d onere
	 */
	public SiacDOnere getSiacDOnere() {
		return this.siacDOnere;
	}

	/**
	 * Sets the siac d onere.
	 *
	 * @param siacDOnere the new siac d onere
	 */
	public void setSiacDOnere(SiacDOnere siacDOnere) {
		this.siacDOnere = siacDOnere;
	}

	

	/**
	 * Gets the siac t soggetto.
	 *
	 * @return the siac t soggetto
	 */
	public SiacTSoggetto getSiacTSoggetto() {
		return this.siacTSoggetto;
	}

	/**
	 * Sets the siac t soggetto.
	 *
	 * @param siacTSoggetto the new siac t soggetto
	 */
	public void setSiacTSoggetto(SiacTSoggetto siacTSoggetto) {
		this.siacTSoggetto = siacTSoggetto;
	}

	/**
	 * Gets the siac t soggetto mod.
	 *
	 * @return the siac t soggetto mod
	 */
	public SiacTSoggettoMod getSiacTSoggettoMod() {
		return this.siacTSoggettoMod;
	}

	/**
	 * Sets the siac t soggetto mod.
	 *
	 * @param siacTSoggettoMod the new siac t soggetto mod
	 */
	public void setSiacTSoggettoMod(SiacTSoggettoMod siacTSoggettoMod) {
		this.siacTSoggettoMod = siacTSoggettoMod;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return soggettoOnereModId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.soggettoOnereModId = uid;
	}
	
	

}