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
 * The persistent class for the siac_r_subdoc_movgest_ts database table.
 * 
 */
@Entity
@Table(name="siac_r_subdoc_movgest_ts")
@NamedQuery(name="SiacRSubdocMovgestT.findAll", query="SELECT s FROM SiacRSubdocMovgestT s")
public class SiacRSubdocMovgestT extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The subdoc movgest ts id. */
	@Id
	@SequenceGenerator(name="SIAC_R_SUBDOC_MOVGEST_TS_SUBDOCMOVGESTTSID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_SUBDOC_MOVGEST_TS_SUBDOC_MOVGEST_TS_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_SUBDOC_MOVGEST_TS_SUBDOCMOVGESTTSID_GENERATOR")
	@Column(name="subdoc_movgest_ts_id")
	private Integer subdocMovgestTsId;

	//bi-directional many-to-one association to SiacTMovgestT
	/** The siac t movgest t. */
	@ManyToOne
	@JoinColumn(name="movgest_ts_id")
	private SiacTMovgestT siacTMovgestT;

	//bi-directional many-to-one association to SiacTSubdoc
	/** The siac t subdoc. */
	@ManyToOne
	@JoinColumn(name="subdoc_id")
	private SiacTSubdoc siacTSubdoc;

	/**
	 * Instantiates a new siac r subdoc movgest t.
	 */
	public SiacRSubdocMovgestT() {
	}

	/**
	 * Gets the subdoc movgest ts id.
	 *
	 * @return the subdoc movgest ts id
	 */
	public Integer getSubdocMovgestTsId() {
		return this.subdocMovgestTsId;
	}

	/**
	 * Sets the subdoc movgest ts id.
	 *
	 * @param subdocMovgestTsId the new subdoc movgest ts id
	 */
	public void setSubdocMovgestTsId(Integer subdocMovgestTsId) {
		this.subdocMovgestTsId = subdocMovgestTsId;
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
	 * Gets the siac t subdoc.
	 *
	 * @return the siac t subdoc
	 */
	public SiacTSubdoc getSiacTSubdoc() {
		return this.siacTSubdoc;
	}

	/**
	 * Sets the siac t subdoc.
	 *
	 * @param siacTSubdoc the new siac t subdoc
	 */
	public void setSiacTSubdoc(SiacTSubdoc siacTSubdoc) {
		this.siacTSubdoc = siacTSubdoc;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return subdocMovgestTsId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.subdocMovgestTsId = uid;
	}

}