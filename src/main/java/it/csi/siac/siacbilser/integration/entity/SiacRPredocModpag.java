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
 * The persistent class for the siac_r_predoc_modpag database table.
 * 
 */
@Entity
@Table(name="siac_r_predoc_modpag")
@NamedQuery(name="SiacRPredocModpag.findAll", query="SELECT s FROM SiacRPredocModpag s")
public class SiacRPredocModpag extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The predoc modpag id. */
	@Id
	@SequenceGenerator(name="SIAC_R_PREDOC_MODPAG_PREDOCMODPAGID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_PREDOC_MODPAG_PREDOC_MODPAG_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_PREDOC_MODPAG_PREDOCMODPAGID_GENERATOR")
	@Column(name="predoc_modpag_id")
	private Integer predocModpagId;

	//bi-directional many-to-one association to SiacTModpag
	/** The siac t modpag. */
	@ManyToOne
	@JoinColumn(name="modpag_id")
	private SiacTModpag siacTModpag;

	//bi-directional many-to-one association to SiacTPredoc
	/** The siac t predoc. */
	@ManyToOne
	@JoinColumn(name="predoc_id")
	private SiacTPredoc siacTPredoc;

	/**
	 * Instantiates a new siac r predoc modpag.
	 */
	public SiacRPredocModpag() {
	}

	/**
	 * Gets the predoc modpag id.
	 *
	 * @return the predoc modpag id
	 */
	public Integer getPredocModpagId() {
		return this.predocModpagId;
	}

	/**
	 * Sets the predoc modpag id.
	 *
	 * @param predocModpagId the new predoc modpag id
	 */
	public void setPredocModpagId(Integer predocModpagId) {
		this.predocModpagId = predocModpagId;
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

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return predocModpagId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.predocModpagId = uid;
	}

}