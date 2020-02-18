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
 * The persistent class for the siac_r_subdoc database table.
 * 
 */
@Entity
@Table(name="siac_r_subdoc")
@NamedQuery(name="SiacRSubdoc.findAll", query="SELECT s FROM SiacRSubdoc s")
public class SiacRSubdoc extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The subdoc r id. */
	@Id
	@SequenceGenerator(name="SIAC_R_SUBDOC_SUBDOCRID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_SUBDOC_SUBDOC_R_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_SUBDOC_SUBDOCRID_GENERATOR")
	@Column(name="subdoc_r_id")
	private Integer subdocRId;

	//bi-directional many-to-one association to SiacTSubdoc
	/** The siac t subdoc b. */
	@ManyToOne
	@JoinColumn(name="subdoc_b_id")
	private SiacTSubdoc siacTSubdocB;

	//bi-directional many-to-one association to SiacTSubdoc
	/** The siac t subdoc a. */
	@ManyToOne
	@JoinColumn(name="subdoc_a_id")
	private SiacTSubdoc siacTSubdocA;

	/**
	 * Instantiates a new siac r subdoc.
	 */
	public SiacRSubdoc() {
	}

	/**
	 * Gets the subdoc r id.
	 *
	 * @return the subdoc r id
	 */
	public Integer getSubdocRId() {
		return this.subdocRId;
	}

	/**
	 * Sets the subdoc r id.
	 *
	 * @param subdocRId the new subdoc r id
	 */
	public void setSubdocRId(Integer subdocRId) {
		this.subdocRId = subdocRId;
	}


	/**
	 * Gets the siac t subdoc b.
	 *
	 * @return the siac t subdoc b
	 */
	public SiacTSubdoc getSiacTSubdocB() {
		return this.siacTSubdocB;
	}

	/**
	 * Sets the siac t subdoc b.
	 *
	 * @param siacTSubdoc1 the new siac t subdoc b
	 */
	public void setSiacTSubdocB(SiacTSubdoc siacTSubdoc1) {
		this.siacTSubdocB = siacTSubdoc1;
	}

	/**
	 * Gets the siac t subdoc a.
	 *
	 * @return the siac t subdoc a
	 */
	public SiacTSubdoc getSiacTSubdocA() {
		return this.siacTSubdocA;
	}

	/**
	 * Sets the siac t subdoc a.
	 *
	 * @param siacTSubdoc2 the new siac t subdoc a
	 */
	public void setSiacTSubdocA(SiacTSubdoc siacTSubdoc2) {
		this.siacTSubdocA = siacTSubdoc2;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return subdocRId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.subdocRId = uid;
	}

}