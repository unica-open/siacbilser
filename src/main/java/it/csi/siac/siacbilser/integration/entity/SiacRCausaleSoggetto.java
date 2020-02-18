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
 * The persistent class for the siac_r_causale_soggetto database table.
 * 
 */
@Entity
@Table(name="siac_r_causale_soggetto")
@NamedQuery(name="SiacRCausaleSoggetto.findAll", query="SELECT s FROM SiacRCausaleSoggetto s")
public class SiacRCausaleSoggetto extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The caus soggetto id. */
	@Id
	@SequenceGenerator(name="SIAC_R_CAUSALE_SOGGETTO_CAUSSOGGETTOID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_CAUSALE_SOGGETTO_CAUS_SOGGETTO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_CAUSALE_SOGGETTO_CAUSSOGGETTOID_GENERATOR")
	@Column(name="caus_soggetto_id")
	private Integer causSoggettoId;


	//bi-directional many-to-one association to SiacDCausale
	/** The siac d causale. */
	@ManyToOne
	@JoinColumn(name="caus_id")
	private SiacDCausale siacDCausale;

	//bi-directional many-to-one association to SiacTSoggetto
	/** The siac t soggetto. */
	@ManyToOne
	@JoinColumn(name="soggetto_id")
	private SiacTSoggetto siacTSoggetto;

	/**
	 * Instantiates a new siac r causale soggetto.
	 */
	public SiacRCausaleSoggetto() {
	}

	/**
	 * Gets the caus soggetto id.
	 *
	 * @return the caus soggetto id
	 */
	public Integer getCausSoggettoId() {
		return this.causSoggettoId;
	}

	/**
	 * Sets the caus soggetto id.
	 *
	 * @param causSoggettoId the new caus soggetto id
	 */
	public void setCausSoggettoId(Integer causSoggettoId) {
		this.causSoggettoId = causSoggettoId;
	}

	/**
	 * Gets the siac d causale.
	 *
	 * @return the siac d causale
	 */
	public SiacDCausale getSiacDCausale() {
		return this.siacDCausale;
	}

	/**
	 * Sets the siac d causale.
	 *
	 * @param siacDCausale the new siac d causale
	 */
	public void setSiacDCausale(SiacDCausale siacDCausale) {
		this.siacDCausale = siacDCausale;
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
		return causSoggettoId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.causSoggettoId = uid;
	}

}