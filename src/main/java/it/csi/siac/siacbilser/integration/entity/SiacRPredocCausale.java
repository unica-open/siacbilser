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
 * The persistent class for the siac_r_predoc_causale database table.
 * 
 */
@Entity
@Table(name="siac_r_predoc_causale")
@NamedQuery(name="SiacRPredocCausale.findAll", query="SELECT s FROM SiacRPredocCausale s")
public class SiacRPredocCausale extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The predoc caus id. */
	@Id
	@SequenceGenerator(name="SIAC_R_PREDOC_CAUSALE_PREDOCCAUSID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_PREDOC_CAUSALE_PREDOC_CAUS_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_PREDOC_CAUSALE_PREDOCCAUSID_GENERATOR")
	@Column(name="predoc_caus_id")
	private Integer predocCausId;

	//bi-directional many-to-one association to SiacDCausale
	/** The siac d causale. */
	@ManyToOne
	@JoinColumn(name="caus_id")
	private SiacDCausale siacDCausale;

	//bi-directional many-to-one association to SiacTPredoc
	/** The siac t predoc. */
	@ManyToOne
	@JoinColumn(name="predoc_id")
	private SiacTPredoc siacTPredoc;

	/**
	 * Instantiates a new siac r predoc causale.
	 */
	public SiacRPredocCausale() {
	}

	/**
	 * Gets the predoc caus id.
	 *
	 * @return the predoc caus id
	 */
	public Integer getPredocCausId() {
		return this.predocCausId;
	}

	/**
	 * Sets the predoc caus id.
	 *
	 * @param predocCausId the new predoc caus id
	 */
	public void setPredocCausId(Integer predocCausId) {
		this.predocCausId = predocCausId;
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
		return predocCausId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.predocCausId = uid;
	}

}