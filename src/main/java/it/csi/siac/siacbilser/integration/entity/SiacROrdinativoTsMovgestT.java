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
 * The persistent class for the siac_r_ordinativo_ts_movgest_ts database table.
 * 
 */
@Entity
@Table(name="siac_r_ordinativo_ts_movgest_ts")
@NamedQuery(name="SiacROrdinativoTsMovgestT.findAll", query="SELECT s FROM SiacROrdinativoTsMovgestT s")
public class SiacROrdinativoTsMovgestT extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The ord movgest ts id. */
	@Id
	@SequenceGenerator(name="SIAC_R_ORDINATIVO_TS_MOVGEST_TS_ORDMOVGESTTSID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_ORDINATIVO_TS_MOVGEST_TS_ORD_MOVGEST_TS_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_ORDINATIVO_TS_MOVGEST_TS_ORDMOVGESTTSID_GENERATOR")
	@Column(name="ord_movgest_ts_id")
	private Integer ordMovgestTsId;

	//bi-directional many-to-one association to SiacTMovgestT
	/** The siac t movgest t. */
	@ManyToOne
	@JoinColumn(name="movgest_ts_id")
	private SiacTMovgestT siacTMovgestT;

	//bi-directional many-to-one association to SiacTOrdinativoT
	/** The siac t ordinativo t. */
	@ManyToOne
	@JoinColumn(name="ord_ts_id")
	private SiacTOrdinativoT siacTOrdinativoT;

	/**
	 * Instantiates a new siac r ordinativo ts movgest t.
	 */
	public SiacROrdinativoTsMovgestT() {
	}

	/**
	 * Gets the ord movgest ts id.
	 *
	 * @return the ord movgest ts id
	 */
	public Integer getOrdMovgestTsId() {
		return this.ordMovgestTsId;
	}

	/**
	 * Sets the ord movgest ts id.
	 *
	 * @param ordMovgestTsId the new ord movgest ts id
	 */
	public void setOrdMovgestTsId(Integer ordMovgestTsId) {
		this.ordMovgestTsId = ordMovgestTsId;
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
	 * Gets the siac t ordinativo t.
	 *
	 * @return the siac t ordinativo t
	 */
	public SiacTOrdinativoT getSiacTOrdinativoT() {
		return this.siacTOrdinativoT;
	}

	/**
	 * Sets the siac t ordinativo t.
	 *
	 * @param siacTOrdinativoT the new siac t ordinativo t
	 */
	public void setSiacTOrdinativoT(SiacTOrdinativoT siacTOrdinativoT) {
		this.siacTOrdinativoT = siacTOrdinativoT;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return ordMovgestTsId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.ordMovgestTsId = uid;
	}

}