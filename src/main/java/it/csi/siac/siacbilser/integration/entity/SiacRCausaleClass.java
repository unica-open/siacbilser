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
 * The persistent class for the siac_r_causale_class database table.
 * 
 */
@Entity
@Table(name="siac_r_causale_class")
@NamedQuery(name="SiacRCausaleClass.findAll", query="SELECT s FROM SiacRCausaleClass s")
public class SiacRCausaleClass extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The caus classif id. */
	@Id
	@SequenceGenerator(name="SIAC_R_CAUSALE_CLASS_CAUSCLASSIFID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_CAUSALE_CLASS_CAUS_CLASSIF_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_CAUSALE_CLASS_CAUSCLASSIFID_GENERATOR")
	@Column(name="caus_classif_id")
	private Integer causClassifId;


	//bi-directional many-to-one association to SiacDCausale
	/** The siac d causale. */
	@ManyToOne
	@JoinColumn(name="caus_id")
	private SiacDCausale siacDCausale;

	//bi-directional many-to-one association to SiacTClass
	/** The siac t class. */
	@ManyToOne
	@JoinColumn(name="classif_id")
	private SiacTClass siacTClass;

	

	/**
	 * Instantiates a new siac r causale class.
	 */
	public SiacRCausaleClass() {
	}

	/**
	 * Gets the caus classif id.
	 *
	 * @return the caus classif id
	 */
	public Integer getCausClassifId() {
		return this.causClassifId;
	}

	/**
	 * Sets the caus classif id.
	 *
	 * @param causClassifId the new caus classif id
	 */
	public void setCausClassifId(Integer causClassifId) {
		this.causClassifId = causClassifId;
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

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return causClassifId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.causClassifId = uid;
	}

	

}