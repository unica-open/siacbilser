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
 * The persistent class for the siac_r_predoc_subdoc database table.
 * 
 */
@Entity
@Table(name="siac_r_predoc_subdoc")
@NamedQuery(name="SiacRPredocSubdoc.findAll", query="SELECT s FROM SiacRPredocSubdoc s")
public class SiacRPredocSubdoc extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The predoc subdoc id. */
	@Id
	@SequenceGenerator(name="SIAC_R_PREDOC_SUBDOC_PREDOCSUBDOCID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_PREDOC_SUBDOC_PREDOC_SUBDOC_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_PREDOC_SUBDOC_PREDOCSUBDOCID_GENERATOR")
	@Column(name="predoc_subdoc_id")
	private Integer predocSubdocId;

	//bi-directional many-to-one association to SiacTPredoc
	/** The siac t predoc. */
	@ManyToOne
	@JoinColumn(name="predoc_id")
	private SiacTPredoc siacTPredoc;

	//bi-directional many-to-one association to SiacTSubdoc
	/** The siac t subdoc. */
	@ManyToOne
	@JoinColumn(name="subdoc_id")
	private SiacTSubdoc siacTSubdoc;

	/**
	 * Instantiates a new siac r predoc subdoc.
	 */
	public SiacRPredocSubdoc() {
	}

	/**
	 * Gets the predoc subdoc id.
	 *
	 * @return the predoc subdoc id
	 */
	public Integer getPredocSubdocId() {
		return this.predocSubdocId;
	}

	/**
	 * Sets the predoc subdoc id.
	 *
	 * @param predocSubdocId the new predoc subdoc id
	 */
	public void setPredocSubdocId(Integer predocSubdocId) {
		this.predocSubdocId = predocSubdocId;
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
		return predocSubdocId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.predocSubdocId = uid;
	}

}