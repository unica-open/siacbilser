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
 * The persistent class for the siac_r_soggetto_onere database table.
 * 
 */
@Entity
@Table(name="siac_r_soggetto_onere")
@NamedQuery(name="SiacRSoggettoOnere.findAll", query="SELECT s FROM SiacRSoggettoOnere s")
public class SiacRSoggettoOnere extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The soggetto onere id. */
	@Id
	@SequenceGenerator(name="SIAC_R_SOGGETTO_ONERE_SOGGETTOONEREID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_SOGGETTO_ONERE_SOGGETTO_ONERE_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_SOGGETTO_ONERE_SOGGETTOONEREID_GENERATOR")
	@Column(name="soggetto_onere_id")
	private Integer soggettoOnereId;

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

	/**
	 * Instantiates a new siac r soggetto onere.
	 */
	public SiacRSoggettoOnere() {
	}

	/**
	 * Gets the soggetto onere id.
	 *
	 * @return the soggetto onere id
	 */
	public Integer getSoggettoOnereId() {
		return this.soggettoOnereId;
	}

	/**
	 * Sets the soggetto onere id.
	 *
	 * @param soggettoOnereId the new soggetto onere id
	 */
	public void setSoggettoOnereId(Integer soggettoOnereId) {
		this.soggettoOnereId = soggettoOnereId;
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

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return soggettoOnereId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.soggettoOnereId = uid;
	}

}