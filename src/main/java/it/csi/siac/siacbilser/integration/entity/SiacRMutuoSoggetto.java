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
 * The persistent class for the siac_r_mutuo_soggetto database table.
 * 
 */
@Entity
@Table(name="siac_r_mutuo_soggetto")
@NamedQuery(name="SiacRMutuoSoggetto.findAll", query="SELECT s FROM SiacRMutuoSoggetto s")
public class SiacRMutuoSoggetto extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The mut soggetto id. */
	@Id
	@SequenceGenerator(name="SIAC_R_MUTUO_SOGGETTO_MUTSOGGETTOID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_MUTUO_SOGGETTO_MUT_SOGGETTO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_MUTUO_SOGGETTO_MUTSOGGETTOID_GENERATOR")
	@Column(name="mut_soggetto_id")
	private Integer mutSoggettoId;

	//bi-directional many-to-one association to SiacTMutuo
	/** The siac t mutuo. */
	@ManyToOne
	@JoinColumn(name="mut_id")
	private SiacTMutuo siacTMutuo;

	//bi-directional many-to-one association to SiacTSoggetto
	/** The siac t soggetto. */
	@ManyToOne
	@JoinColumn(name="soggetto_id")
	private SiacTSoggetto siacTSoggetto;

	/**
	 * Instantiates a new siac r mutuo soggetto.
	 */
	public SiacRMutuoSoggetto() {
	}

	/**
	 * Gets the mut soggetto id.
	 *
	 * @return the mut soggetto id
	 */
	public Integer getMutSoggettoId() {
		return this.mutSoggettoId;
	}

	/**
	 * Sets the mut soggetto id.
	 *
	 * @param mutSoggettoId the new mut soggetto id
	 */
	public void setMutSoggettoId(Integer mutSoggettoId) {
		this.mutSoggettoId = mutSoggettoId;
	}

	/**
	 * Gets the siac t mutuo.
	 *
	 * @return the siac t mutuo
	 */
	public SiacTMutuo getSiacTMutuo() {
		return this.siacTMutuo;
	}

	/**
	 * Sets the siac t mutuo.
	 *
	 * @param siacTMutuo the new siac t mutuo
	 */
	public void setSiacTMutuo(SiacTMutuo siacTMutuo) {
		this.siacTMutuo = siacTMutuo;
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
		return mutSoggettoId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.mutSoggettoId = uid;
	}

}