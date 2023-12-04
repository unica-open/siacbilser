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
 * The persistent class for the siac_r_predoc_atto_amm database table.
 * 
 */
@Entity
@Table(name="siac_r_predoc_atto_amm")
@NamedQuery(name="SiacRPredocAttoAmm.findAll", query="SELECT s FROM SiacRPredocAttoAmm s")
public class SiacRPredocAttoAmm extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The predoc attoamm id. */
	@Id
	@SequenceGenerator(name="SIAC_R_PREDOC_ATTO_AMM_PREDOCATTOAMMID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_PREDOC_ATTO_AMM_PREDOC_ATTOAMM_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_PREDOC_ATTO_AMM_PREDOCATTOAMMID_GENERATOR")
	@Column(name="predoc_attoamm_id")
	private Integer predocAttoammId;

	//bi-directional many-to-one association to SiacTAttoAmm
	/** The siac t atto amm. */
	@ManyToOne
	@JoinColumn(name="attoamm_id")
	private SiacTAttoAmm siacTAttoAmm;

	//bi-directional many-to-one association to SiacTPredoc
	/** The siac t predoc. */
	@ManyToOne
	@JoinColumn(name="predoc_id")
	private SiacTPredoc siacTPredoc;

	/**
	 * Instantiates a new siac r predoc atto amm.
	 */
	public SiacRPredocAttoAmm() {
	}

	/**
	 * Gets the predoc attoamm id.
	 *
	 * @return the predoc attoamm id
	 */
	public Integer getPredocAttoammId() {
		return this.predocAttoammId;
	}

	/**
	 * Sets the predoc attoamm id.
	 *
	 * @param predocAttoammId the new predoc attoamm id
	 */
	public void setPredocAttoammId(Integer predocAttoammId) {
		this.predocAttoammId = predocAttoammId;
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
		return predocAttoammId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.predocAttoammId = uid;
	}

}