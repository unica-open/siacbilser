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
 * The persistent class for the siac_r_atto_amm_class database table.
 * 
 */
@Entity
@Table(name="siac_r_atto_amm_class")
@NamedQuery(name="SiacRAttoAmmClass.findAll", query="SELECT s FROM SiacRAttoAmmClass s")
public class SiacRAttoAmmClass extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The atto amm class id. */
	@Id
	@SequenceGenerator(name="SIAC_R_ATTO_AMM_CLASS_ATTOAMMCLASSID_GENERATOR", allocationSize=1, sequenceName="siac_r_atto_amm_class_atto_amm_class_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_ATTO_AMM_CLASS_ATTOAMMCLASSID_GENERATOR")
	@Column(name="atto_amm_class_id")
	private Integer attoAmmClassId;

	//bi-directional many-to-one association to SiacTAttoAmm
	/** The siac t atto amm. */
	@ManyToOne
	@JoinColumn(name="attoamm_id")
	private SiacTAttoAmm siacTAttoAmm;

	//bi-directional many-to-one association to SiacTClass
	/** The siac t class. */
	@ManyToOne
	@JoinColumn(name="classif_id")
	private SiacTClass siacTClass;

	/**
	 * Instantiates a new siac r atto amm class.
	 */
	public SiacRAttoAmmClass() {
	}

	/**
	 * Gets the atto amm class id.
	 *
	 * @return the atto amm class id
	 */
	public Integer getAttoAmmClassId() {
		return this.attoAmmClassId;
	}

	/**
	 * Sets the atto amm class id.
	 *
	 * @param attoAmmClassId the new atto amm class id
	 */
	public void setAttoAmmClassId(Integer attoAmmClassId) {
		this.attoAmmClassId = attoAmmClassId;
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
	 * Gets the siac t class.
	 *
	 * @return the siac t class
	 */
	public SiacTClass getSiacTClass() {
		return this.siacTClass;
	}

	/**
	 * Sets the siac t class.
	 *
	 * @param siacTClass the new siac t class
	 */
	public void setSiacTClass(SiacTClass siacTClass) {
		this.siacTClass = siacTClass;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return attoAmmClassId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		attoAmmClassId = uid;
		
	}

}