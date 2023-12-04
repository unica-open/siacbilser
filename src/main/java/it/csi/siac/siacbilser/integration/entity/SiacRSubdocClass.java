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
 * The persistent class for the siac_r_subdoc_class database table.
 * 
 */
@Entity
@Table(name="siac_r_subdoc_class")
@NamedQuery(name="SiacRSubdocClass.findAll", query="SELECT s FROM SiacRSubdocClass s")
public class SiacRSubdocClass extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The subdoc classif id. */
	@Id
	@SequenceGenerator(name="SIAC_R_SUBDOC_CLASS_SUBDOCCLASSIFID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_SUBDOC_CLASS_SUBDOC_CLASSIF_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_SUBDOC_CLASS_SUBDOCCLASSIFID_GENERATOR")
	@Column(name="subdoc_classif_id")
	private Integer subdocClassifId;

	//bi-directional many-to-one association to SiacTClass
	/** The siac t class. */
	@ManyToOne
	@JoinColumn(name="classif_id")
	private SiacTClass siacTClass;

	//bi-directional many-to-one association to SiacTSubdoc
	/** The siac t subdoc. */
	@ManyToOne
	@JoinColumn(name="subdoc_id")
	private SiacTSubdoc siacTSubdoc;

	/**
	 * Instantiates a new siac r subdoc class.
	 */
	public SiacRSubdocClass() {
	}

	/**
	 * Gets the subdoc classif id.
	 *
	 * @return the subdoc classif id
	 */
	public Integer getSubdocClassifId() {
		return this.subdocClassifId;
	}

	/**
	 * Sets the subdoc classif id.
	 *
	 * @param subdocClassifId the new subdoc classif id
	 */
	public void setSubdocClassifId(Integer subdocClassifId) {
		this.subdocClassifId = subdocClassifId;
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
		return subdocClassifId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.subdocClassifId = uid;
	}

}