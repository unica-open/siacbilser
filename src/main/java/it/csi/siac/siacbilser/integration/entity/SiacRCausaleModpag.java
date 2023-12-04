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
 * The persistent class for the siac_r_causale_modpag database table.
 * 
 */
@Entity
@Table(name="siac_r_causale_modpag")
@NamedQuery(name="SiacRCausaleModpag.findAll", query="SELECT s FROM SiacRCausaleModpag s")
public class SiacRCausaleModpag extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The caus modpag id. */
	@Id
	@SequenceGenerator(name="SIAC_R_CAUSALE_MODPAG_CAUSMODPAGID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_CAUSALE_MODPAG_CAUS_MODPAG_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_CAUSALE_MODPAG_CAUSMODPAGID_GENERATOR")
	@Column(name="caus_modpag_id")
	private Integer causModpagId;
	

	//bi-directional many-to-one association to SiacDCausale
	/** The siac d causale. */
	@ManyToOne
	@JoinColumn(name="caus_id")
	private SiacDCausale siacDCausale;

	//bi-directional many-to-one association to SiacTModpag
	/** The siac t modpag. */
	@ManyToOne
	@JoinColumn(name="modpag_id")
	private SiacTModpag siacTModpag;

	/**
	 * Instantiates a new siac r causale modpag.
	 */
	public SiacRCausaleModpag() {
	}

	/**
	 * Gets the caus modpag id.
	 *
	 * @return the caus modpag id
	 */
	public Integer getCausModpagId() {
		return this.causModpagId;
	}

	/**
	 * Sets the caus modpag id.
	 *
	 * @param causModpagId the new caus modpag id
	 */
	public void setCausModpagId(Integer causModpagId) {
		this.causModpagId = causModpagId;
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

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return causModpagId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.causModpagId = uid;
	}

}