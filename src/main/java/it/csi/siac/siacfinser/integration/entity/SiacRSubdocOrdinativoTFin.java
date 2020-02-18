/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import it.csi.siac.siacfinser.integration.entity.base.SiacTEnteBase;


// TODO: Auto-generated Javadoc
/**
 * The persistent class for the siac_r_subdoc_ordinativo_ts database table.
 * 
 */
@Entity
@Table(name="siac_r_subdoc_ordinativo_ts")
public class SiacRSubdocOrdinativoTFin extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The subdoc liq id. */
	@Id
	@SequenceGenerator(name="SIAC_R_SUBDOC_ORDINATIVO_TS_SUBDOCLIQID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_SUBDOC_ORDINATIVO_TS_SUBDOC_LIQ_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_SUBDOC_ORDINATIVO_TS_SUBDOCLIQID_GENERATOR")
	@Column(name="subdoc_liq_id")
	private Integer subdocLiqId;

	//bi-directional many-to-one association to SiacTOrdinativoTS
	/** The siac t ordinativo t. */
	@ManyToOne
	@JoinColumn(name="ord_ts_id")
	private SiacTOrdinativoTFin siacTOrdinativoT;

	//bi-directional many-to-one association to SiacTSubdoc
	/** The siac t subdoc. */
	@ManyToOne
	@JoinColumn(name="subdoc_id")
	private SiacTSubdocFin siacTSubdoc;

	/**
	 * Instantiates a new siac r subdoc ordinativo t.
	 */
	public SiacRSubdocOrdinativoTFin() {
	}

	/**
	 * Gets the subdoc liq id.
	 *
	 * @return the subdoc liq id
	 */
	public Integer getSubdocLiqId() {
		return this.subdocLiqId;
	}

	/**
	 * Sets the subdoc liq id.
	 *
	 * @param subdocLiqId the new subdoc liq id
	 */
	public void setSubdocLiqId(Integer subdocLiqId) {
		this.subdocLiqId = subdocLiqId;
	}
	

	/**
	 * @return the siacTOrdinativoT
	 */
	public SiacTOrdinativoTFin getSiacTOrdinativoT() {
		return siacTOrdinativoT;
	}

	/**
	 * @param siacTOrdinativoT the siacTOrdinativoT to set
	 */
	public void setSiacTOrdinativoT(SiacTOrdinativoTFin siacTOrdinativoT) {
		this.siacTOrdinativoT = siacTOrdinativoT;
	}

	/**
	 * @return the siacTSubdoc
	 */
	public SiacTSubdocFin getSiacTSubdoc() {
		return siacTSubdoc;
	}

	/**
	 * @param siacTSubdoc the siacTSubdoc to set
	 */
	public void setSiacTSubdoc(SiacTSubdocFin siacTSubdoc) {
		this.siacTSubdoc = siacTSubdoc;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return subdocLiqId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.subdocLiqId = uid;
	}

}