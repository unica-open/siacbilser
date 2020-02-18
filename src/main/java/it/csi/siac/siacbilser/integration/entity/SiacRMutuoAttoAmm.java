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
 * The persistent class for the siac_r_mutuo_atto_amm database table.
 * 
 */
@Entity
@Table(name="siac_r_mutuo_atto_amm")
@NamedQuery(name="SiacRMutuoAttoAmm.findAll", query="SELECT s FROM SiacRMutuoAttoAmm s")
public class SiacRMutuoAttoAmm extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The mut atto amm id. */
	@Id
	@SequenceGenerator(name="SIAC_R_MUTUO_ATTO_AMM_MUTATTOAMMID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_MUTUO_ATTO_AMM_MUT_ATTO_AMM_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_MUTUO_ATTO_AMM_MUTATTOAMMID_GENERATOR")
	@Column(name="mut_atto_amm_id")
	private Integer mutAttoAmmId;

	//bi-directional many-to-one association to SiacTAttoAmm
	/** The siac t atto amm. */
	@ManyToOne
	@JoinColumn(name="attoamm_id")
	private SiacTAttoAmm siacTAttoAmm;

	//bi-directional many-to-one association to SiacTMutuo
	/** The siac t mutuo. */
	@ManyToOne
	@JoinColumn(name="mut_id")
	private SiacTMutuo siacTMutuo;

	/**
	 * Instantiates a new siac r mutuo atto amm.
	 */
	public SiacRMutuoAttoAmm() {
	}

	/**
	 * Gets the mut atto amm id.
	 *
	 * @return the mut atto amm id
	 */
	public Integer getMutAttoAmmId() {
		return this.mutAttoAmmId;
	}

	/**
	 * Sets the mut atto amm id.
	 *
	 * @param mutAttoAmmId the new mut atto amm id
	 */
	public void setMutAttoAmmId(Integer mutAttoAmmId) {
		this.mutAttoAmmId = mutAttoAmmId;
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

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return mutAttoAmmId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.mutAttoAmmId = uid;
	}

}