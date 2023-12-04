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
 * The persistent class for the siac_r_causale_movgest_ts database table.
 * 
 */
@Entity
@Table(name="siac_r_causale_movgest_ts")
@NamedQuery(name="SiacRCausaleMovgestT.findAll", query="SELECT s FROM SiacRCausaleMovgestT s")
public class SiacRCausaleMovgestT extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The caus movgest ts id. */
	@Id
	@SequenceGenerator(name="SIAC_R_CAUSALE_MOVGEST_TS_CAUSMOVGESTTSID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_CAUSALE_MOVGEST_TS_CAUS_MOVGEST_TS_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_CAUSALE_MOVGEST_TS_CAUSMOVGESTTSID_GENERATOR")
	@Column(name="caus_movgest_ts_id")
	private Integer causMovgestTsId;

	//bi-directional many-to-one association to SiacDCausale
	/** The siac d causale. */
	@ManyToOne
	@JoinColumn(name="caus_id")
	private SiacDCausale siacDCausale;

	//bi-directional many-to-one association to SiacTMovgestT
	/** The siac t movgest t. */
	@ManyToOne
	@JoinColumn(name="movgest_ts_id")
	private SiacTMovgestT siacTMovgestT;

	/**
	 * Instantiates a new siac r causale movgest t.
	 */
	public SiacRCausaleMovgestT() {
	}

	/**
	 * Gets the caus movgest ts id.
	 *
	 * @return the caus movgest ts id
	 */
	public Integer getCausMovgestTsId() {
		return this.causMovgestTsId;
	}

	/**
	 * Sets the caus movgest ts id.
	 *
	 * @param causMovgestTsId the new caus movgest ts id
	 */
	public void setCausMovgestTsId(Integer causMovgestTsId) {
		this.causMovgestTsId = causMovgestTsId;
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
	 * Gets the siac t movgest t.
	 *
	 * @return the siac t movgest t
	 */
	public SiacTMovgestT getSiacTMovgestT() {
		return this.siacTMovgestT;
	}

	/**
	 * Sets the siac t movgest t.
	 *
	 * @param siacTMovgestT the new siac t movgest t
	 */
	public void setSiacTMovgestT(SiacTMovgestT siacTMovgestT) {
		this.siacTMovgestT = siacTMovgestT;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return causMovgestTsId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.causMovgestTsId = uid;
	}

}