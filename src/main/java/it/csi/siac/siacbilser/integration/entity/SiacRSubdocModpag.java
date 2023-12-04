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
 * The persistent class for the siac_r_subdoc_modpag database table.
 * 
 */
@Entity
@Table(name="siac_r_subdoc_modpag")
@NamedQuery(name="SiacRSubdocModpag.findAll", query="SELECT s FROM SiacRSubdocModpag s")
public class SiacRSubdocModpag extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The subdoc modpag id. */
	@Id
	@SequenceGenerator(name="SIAC_R_SUBDOC_MODPAG_SUBDOCMODPAGID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_SUBDOC_MODPAG_SUBDOC_MODPAG_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_SUBDOC_MODPAG_SUBDOCMODPAGID_GENERATOR")
	@Column(name="subdoc_modpag_id")
	private Integer subdocModpagId;

	//bi-directional many-to-one association to SiacRSoggrelModpag
	/** The siac r soggrel modpag. */
	@ManyToOne
	@JoinColumn(name="soggrelmpag_id")
	private SiacRSoggrelModpag siacRSoggrelModpag;

	//bi-directional many-to-one association to SiacTModpag
	/** The siac t modpag. */
	@ManyToOne
	@JoinColumn(name="modpag_id")
	private SiacTModpag siacTModpag;

	//bi-directional many-to-one association to SiacTSubdoc
	/** The siac t subdoc. */
	@ManyToOne
	@JoinColumn(name="subdoc_id")
	private SiacTSubdoc siacTSubdoc;

	/**
	 * Instantiates a new siac r subdoc modpag.
	 */
	public SiacRSubdocModpag() {
	}

	/**
	 * Gets the subdoc modpag id.
	 *
	 * @return the subdoc modpag id
	 */
	public Integer getSubdocModpagId() {
		return this.subdocModpagId;
	}

	/**
	 * Sets the subdoc modpag id.
	 *
	 * @param subdocModpagId the new subdoc modpag id
	 */
	public void setSubdocModpagId(Integer subdocModpagId) {
		this.subdocModpagId = subdocModpagId;
	}

	/**
	 * Gets the siac r soggrel modpag.
	 *
	 * @return the siac r soggrel modpag
	 */
	public SiacRSoggrelModpag getSiacRSoggrelModpag() {
		return this.siacRSoggrelModpag;
	}

	/**
	 * Sets the siac r soggrel modpag.
	 *
	 * @param siacRSoggrelModpag the new siac r soggrel modpag
	 */
	public void setSiacRSoggrelModpag(SiacRSoggrelModpag siacRSoggrelModpag) {
		this.siacRSoggrelModpag = siacRSoggrelModpag;
	}



	/**
	 * Gets the siac t modpag.
	 *
	 * @return the siac t modpag
	 */
	public SiacTModpag getSiacTModpag() {
		return this.siacTModpag;
	}

	/**
	 * Sets the siac t modpag.
	 *
	 * @param siacTModpag the new siac t modpag
	 */
	public void setSiacTModpag(SiacTModpag siacTModpag) {
		this.siacTModpag = siacTModpag;
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
		return subdocModpagId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.subdocModpagId = uid;
	}

}