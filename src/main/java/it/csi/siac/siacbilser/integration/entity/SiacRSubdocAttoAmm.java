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
 * The persistent class for the siac_r_subdoc_atto_amm database table.
 * 
 */
@Entity
@Table(name="siac_r_subdoc_atto_amm")
@NamedQuery(name="SiacRSubdocAttoAmm.findAll", query="SELECT s FROM SiacRSubdocAttoAmm s")
public class SiacRSubdocAttoAmm extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The subdoc atto amm id. */
	@Id
	@SequenceGenerator(name="SIAC_R_SUBDOC_ATTO_AMM_SUBDOCATTOAMMID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_SUBDOC_ATTO_AMM_SUBDOC_ATTO_AMM_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_SUBDOC_ATTO_AMM_SUBDOCATTOAMMID_GENERATOR")
	@Column(name="subdoc_atto_amm_id")
	private Integer subdocAttoAmmId;

	

	//bi-directional many-to-one association to SiacTAttoAmm
	/** The siac t atto amm. */
	@ManyToOne
	@JoinColumn(name="attoamm_id")
	private SiacTAttoAmm siacTAttoAmm;

	//bi-directional many-to-one association to SiacTSubdoc
	/** The siac t subdoc. */
	@ManyToOne
	@JoinColumn(name="subdoc_id")
	private SiacTSubdoc siacTSubdoc;

	/**
	 * Instantiates a new siac r subdoc atto amm.
	 */
	public SiacRSubdocAttoAmm() {
	}

	/**
	 * Gets the subdoc atto amm id.
	 *
	 * @return the subdoc atto amm id
	 */
	public Integer getSubdocAttoAmmId() {
		return this.subdocAttoAmmId;
	}

	/**
	 * Sets the subdoc atto amm id.
	 *
	 * @param subdocAttoAmmId the new subdoc atto amm id
	 */
	public void setSubdocAttoAmmId(Integer subdocAttoAmmId) {
		this.subdocAttoAmmId = subdocAttoAmmId;
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
		return subdocAttoAmmId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.subdocAttoAmmId = uid;
		
	}

}