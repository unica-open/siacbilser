/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import javax.persistence.CascadeType;
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
 * The persistent class for the siac_r_ivamov database table.
 * 
 */
@Entity
@Table(name="siac_r_ivamov")
@NamedQuery(name="SiacRIvamov.findAll", query="SELECT s FROM SiacRIvamov s")
public class SiacRIvamov extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The subdocivamov id. */
	@Id
	@SequenceGenerator(name="SIAC_R_IVAMOV_SUBDOCIVAMOVID_GENERATOR", sequenceName="SIAC_R_IVAMOV_SUBDOCIVAMOV_ID_SEQ", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_IVAMOV_SUBDOCIVAMOVID_GENERATOR")
	@Column(name="subdocivamov_id")
	private Integer subdocivamovId;

	//bi-directional many-to-one association to SiacTIvamov
	/** The siac t ivamov. */
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	@JoinColumn(name="ivamov_id" )
	private SiacTIvamov siacTIvamov;

	//bi-directional many-to-one association to SiacTSubdocIva
	/** The siac t subdoc iva. */
	@ManyToOne
	@JoinColumn(name="subdociva_id")
	private SiacTSubdocIva siacTSubdocIva;

	/**
	 * Instantiates a new siac r ivamov.
	 */
	public SiacRIvamov() {
	}

	/**
	 * Gets the subdocivamov id.
	 *
	 * @return the subdocivamov id
	 */
	public Integer getSubdocivamovId() {
		return this.subdocivamovId;
	}

	/**
	 * Sets the subdocivamov id.
	 *
	 * @param subdocivamovId the new subdocivamov id
	 */
	public void setSubdocivamovId(Integer subdocivamovId) {
		this.subdocivamovId = subdocivamovId;
	}

	/**
	 * Gets the siac t ivamov.
	 *
	 * @return the siac t ivamov
	 */
	public SiacTIvamov getSiacTIvamov() {
		return this.siacTIvamov;
	}

	/**
	 * Sets the siac t ivamov.
	 *
	 * @param siacTIvamov the new siac t ivamov
	 */
	public void setSiacTIvamov(SiacTIvamov siacTIvamov) {
		this.siacTIvamov = siacTIvamov;
	}

	/**
	 * Gets the siac t subdoc iva.
	 *
	 * @return the siac t subdoc iva
	 */
	public SiacTSubdocIva getSiacTSubdocIva() {
		return this.siacTSubdocIva;
	}

	/**
	 * Sets the siac t subdoc iva.
	 *
	 * @param siacTSubdocIva the new siac t subdoc iva
	 */
	public void setSiacTSubdocIva(SiacTSubdocIva siacTSubdocIva) {
		this.siacTSubdocIva = siacTSubdocIva;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return subdocivamovId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		subdocivamovId = uid;
	}

}