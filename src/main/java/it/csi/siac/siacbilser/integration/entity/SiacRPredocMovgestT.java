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
 * The persistent class for the siac_r_predoc_movgest_ts database table.
 * 
 */
@Entity
@Table(name="siac_r_predoc_movgest_ts")
@NamedQuery(name="SiacRPredocMovgestT.findAll", query="SELECT s FROM SiacRPredocMovgestT s")
public class SiacRPredocMovgestT extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The predoc movgest ts id. */
	@Id
	@SequenceGenerator(name="SIAC_R_PREDOC_MOVGEST_TS_PREDOCMOVGESTTSID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_PREDOC_MOVGEST_TS_PREDOC_MOVGEST_TS_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_PREDOC_MOVGEST_TS_PREDOCMOVGESTTSID_GENERATOR")
	@Column(name="predoc_movgest_ts_id")
	private Integer predocMovgestTsId;

	//bi-directional many-to-one association to SiacTMovgestT
	/** The siac t movgest t. */
	@ManyToOne
	@JoinColumn(name="movgest_ts_id")
	private SiacTMovgestT siacTMovgestT;

	//bi-directional many-to-one association to SiacTPredoc
	/** The siac t predoc. */
	@ManyToOne
	@JoinColumn(name="predoc_id")
	private SiacTPredoc siacTPredoc;

	/**
	 * Instantiates a new siac r predoc movgest t.
	 */
	public SiacRPredocMovgestT() {
	}

	/**
	 * Gets the predoc movgest ts id.
	 *
	 * @return the predoc movgest ts id
	 */
	public Integer getPredocMovgestTsId() {
		return this.predocMovgestTsId;
	}

	/**
	 * Sets the predoc movgest ts id.
	 *
	 * @param predocMovgestTsId the new predoc movgest ts id
	 */
	public void setPredocMovgestTsId(Integer predocMovgestTsId) {
		this.predocMovgestTsId = predocMovgestTsId;
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

	/**
	 * Gets the siac t predoc.
	 *
	 * @return the siac t predoc
	 */
	public SiacTPredoc getSiacTPredoc() {
		return this.siacTPredoc;
	}

	/**
	 * Sets the siac t predoc.
	 *
	 * @param siacTPredoc the new siac t predoc
	 */
	public void setSiacTPredoc(SiacTPredoc siacTPredoc) {
		this.siacTPredoc = siacTPredoc;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return predocMovgestTsId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.predocMovgestTsId = uid;
	}

}