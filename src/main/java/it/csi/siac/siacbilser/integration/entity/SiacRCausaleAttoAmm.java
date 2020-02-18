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
 * The persistent class for the siac_r_causale_atto_amm database table.
 * 
 */
@Entity
@Table(name="siac_r_causale_atto_amm")
@NamedQuery(name="SiacRCausaleAttoAmm.findAll", query="SELECT s FROM SiacRCausaleAttoAmm s")
public class SiacRCausaleAttoAmm extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The caus attoamm id. */
	@Id
	@SequenceGenerator(name="SIAC_R_CAUSALE_ATTO_AMM_CAUSATTOAMMID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_CAUSALE_ATTO_AMM_CAUS_ATTOAMM_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_CAUSALE_ATTO_AMM_CAUSATTOAMMID_GENERATOR")
	@Column(name="caus_attoamm_id")
	private Integer causAttoammId;

	//bi-directional many-to-one association to SiacDCausale
	/** The siac d causale. */
	@ManyToOne
	@JoinColumn(name="caus_id")
	private SiacDCausale siacDCausale;

	//bi-directional many-to-one association to SiacTAttoAmm
	/** The siac t atto amm. */
	@ManyToOne
	@JoinColumn(name="attoamm_id")
	private SiacTAttoAmm siacTAttoAmm;



	/**
	 * Instantiates a new siac r causale atto amm.
	 */
	public SiacRCausaleAttoAmm() {
	}

	/**
	 * Gets the caus attoamm id.
	 *
	 * @return the caus attoamm id
	 */
	public Integer getCausAttoammId() {
		return this.causAttoammId;
	}

	/**
	 * Sets the caus attoamm id.
	 *
	 * @param causAttoammId the new caus attoamm id
	 */
	public void setCausAttoammId(Integer causAttoammId) {
		this.causAttoammId = causAttoammId;
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

	

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return causAttoammId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.causAttoammId = uid;
	}

}