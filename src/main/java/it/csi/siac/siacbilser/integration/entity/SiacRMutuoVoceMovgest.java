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
 * The persistent class for the siac_r_mutuo_voce_movgest database table.
 * 
 */
@Entity
@Table(name="siac_r_mutuo_voce_movgest")
@NamedQuery(name="SiacRMutuoVoceMovgest.findAll", query="SELECT s FROM SiacRMutuoVoceMovgest s")
public class SiacRMutuoVoceMovgest extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The mut voce movgest id. */
	@Id
	@SequenceGenerator(name="SIAC_R_MUTUO_VOCE_MOVGEST_MUTVOCEMOVGESTID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_MUTUO_VOCE_MOVGEST_MUT_VOCE_MOVGEST_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_MUTUO_VOCE_MOVGEST_MUTVOCEMOVGESTID_GENERATOR")
	@Column(name="mut_voce_movgest_id")
	private Integer mutVoceMovgestId;

	//bi-directional many-to-one association to SiacTMovgestT
	/** The siac t movgest t. */
	@ManyToOne
	@JoinColumn(name="movgest_ts_id")
	private SiacTMovgestT siacTMovgestT;

	//bi-directional many-to-one association to SiacTMutuoVoce
	/** The siac t mutuo voce. */
	@ManyToOne
	@JoinColumn(name="mut_voce_id")
	private SiacTMutuoVoce siacTMutuoVoce;

	/**
	 * Instantiates a new siac r mutuo voce movgest.
	 */
	public SiacRMutuoVoceMovgest() {
	}

	/**
	 * Gets the mut voce movgest id.
	 *
	 * @return the mut voce movgest id
	 */
	public Integer getMutVoceMovgestId() {
		return this.mutVoceMovgestId;
	}

	/**
	 * Sets the mut voce movgest id.
	 *
	 * @param mutVoceMovgestId the new mut voce movgest id
	 */
	public void setMutVoceMovgestId(Integer mutVoceMovgestId) {
		this.mutVoceMovgestId = mutVoceMovgestId;
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
	 * Gets the siac t mutuo voce.
	 *
	 * @return the siac t mutuo voce
	 */
	public SiacTMutuoVoce getSiacTMutuoVoce() {
		return this.siacTMutuoVoce;
	}

	/**
	 * Sets the siac t mutuo voce.
	 *
	 * @param siacTMutuoVoce the new siac t mutuo voce
	 */
	public void setSiacTMutuoVoce(SiacTMutuoVoce siacTMutuoVoce) {
		this.siacTMutuoVoce = siacTMutuoVoce;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return mutVoceMovgestId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.mutVoceMovgestId = uid;
	}

}