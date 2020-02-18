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
 * The persistent class for the siac_r_subdoc_liquidazione database table.
 * 
 */
@Entity
@Table(name="siac_r_subdoc_liquidazione")
@NamedQuery(name="SiacRSubdocLiquidazione.findAll", query="SELECT s FROM SiacRSubdocLiquidazione s")
public class SiacRSubdocLiquidazione extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The subdoc liq id. */
	@Id
	@SequenceGenerator(name="SIAC_R_SUBDOC_LIQUIDAZIONE_SUBDOCLIQID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_SUBDOC_LIQUIDAZIONE_SUBDOC_LIQ_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_SUBDOC_LIQUIDAZIONE_SUBDOCLIQID_GENERATOR")
	@Column(name="subdoc_liq_id")
	private Integer subdocLiqId;


	//bi-directional many-to-one association to SiacTLiquidazione
	/** The siac t liquidazione. */
	@ManyToOne
	@JoinColumn(name="liq_id")
	private SiacTLiquidazione siacTLiquidazione;

	//bi-directional many-to-one association to SiacTSubdoc
	/** The siac t subdoc. */
	@ManyToOne
	@JoinColumn(name="subdoc_id")
	private SiacTSubdoc siacTSubdoc;

	/**
	 * Instantiates a new siac r subdoc liquidazione.
	 */
	public SiacRSubdocLiquidazione() {
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
	 * Gets the siac t liquidazione.
	 *
	 * @return the siac t liquidazione
	 */
	public SiacTLiquidazione getSiacTLiquidazione() {
		return this.siacTLiquidazione;
	}

	/**
	 * Sets the siac t liquidazione.
	 *
	 * @param siacTLiquidazione the new siac t liquidazione
	 */
	public void setSiacTLiquidazione(SiacTLiquidazione siacTLiquidazione) {
		this.siacTLiquidazione = siacTLiquidazione;
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