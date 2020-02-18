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
 * The persistent class for the siac_r_movgest_ts_atto_amm database table.
 * 
 */
@Entity
@Table(name="siac_r_movgest_ts_atto_amm")
@NamedQuery(name="SiacRMovgestTsAttoAmm.findAll", query="SELECT s FROM SiacRMovgestTsAttoAmm s")
public class SiacRMovgestTsAttoAmm extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The movgest atto amm id. */
	@Id
	@SequenceGenerator(name="SIAC_R_MOVGEST_TS_ATTO_AMM_MOVGESTATTOAMMID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_MOVGEST_TS_ATTO_AMM_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_MOVGEST_TS_ATTO_AMM_MOVGESTATTOAMMID_GENERATOR")
	@Column(name="movgest_atto_amm_id")
	private Integer movgestAttoAmmId;

	//bi-directional many-to-one association to SiacTAttoAmm
	/** The siac t atto amm. */
	@ManyToOne
	@JoinColumn(name="attoamm_id")
	private SiacTAttoAmm siacTAttoAmm;

	//bi-directional many-to-one association to SiacTMovgestT
	/** The siac t movgest t. */
	@ManyToOne
	@JoinColumn(name="movgest_ts_id")
	private SiacTMovgestT siacTMovgestT;

	/**
	 * Instantiates a new siac r movgest ts atto amm.
	 */
	public SiacRMovgestTsAttoAmm() {
	}

	/**
	 * Gets the movgest atto amm id.
	 *
	 * @return the movgest atto amm id
	 */
	public Integer getMovgestAttoAmmId() {
		return this.movgestAttoAmmId;
	}

	/**
	 * Sets the movgest atto amm id.
	 *
	 * @param movgestAttoAmmId the new movgest atto amm id
	 */
	public void setMovgestAttoAmmId(Integer movgestAttoAmmId) {
		this.movgestAttoAmmId = movgestAttoAmmId;
	}

	/**
	 * Gets the siac t atto amm.
	 *
	 * @return the siac t atto amm
	 */
	public SiacTAttoAmm getSiacTAttoAmm() {
		return this.siacTAttoAmm;
	}

	/**
	 * Sets the siac t atto amm.
	 *
	 * @param siacTAttoAmm the new siac t atto amm
	 */
	public void setSiacTAttoAmm(SiacTAttoAmm siacTAttoAmm) {
		this.siacTAttoAmm = siacTAttoAmm;
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
		return movgestAttoAmmId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		movgestAttoAmmId = uid;
	}

}