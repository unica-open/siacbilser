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
 * The persistent class for the siac_r_causale_bil_elem database table.
 * 
 */
@Entity
@Table(name="siac_r_causale_bil_elem")
@NamedQuery(name="SiacRCausaleBilElem.findAll", query="SELECT s FROM SiacRCausaleBilElem s")
public class SiacRCausaleBilElem extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The caus elem id. */
	@Id
	@SequenceGenerator(name="SIAC_R_CAUSALE_BIL_ELEM_CAUSELEMID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_CAUSALE_BIL_ELEM_CAUS_ELEM_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_CAUSALE_BIL_ELEM_CAUSELEMID_GENERATOR")
	@Column(name="caus_elem_id")
	private Integer causElemId;

	//bi-directional many-to-one association to SiacDCausale
	/** The siac d causale. */
	@ManyToOne
	@JoinColumn(name="caus_id")
	private SiacDCausale siacDCausale;

	//bi-directional many-to-one association to SiacTBilElem
	/** The siac t bil elem. */
	@ManyToOne
	@JoinColumn(name="elem_id")
	private SiacTBilElem siacTBilElem;

	/**
	 * Instantiates a new siac r causale bil elem.
	 */
	public SiacRCausaleBilElem() {
	}

	/**
	 * Gets the caus elem id.
	 *
	 * @return the caus elem id
	 */
	public Integer getCausElemId() {
		return this.causElemId;
	}

	/**
	 * Sets the caus elem id.
	 *
	 * @param causElemId the new caus elem id
	 */
	public void setCausElemId(Integer causElemId) {
		this.causElemId = causElemId;
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
	 * Gets the siac t bil elem.
	 *
	 * @return the siac t bil elem
	 */
	public SiacTBilElem getSiacTBilElem() {
		return this.siacTBilElem;
	}

	/**
	 * Sets the siac t bil elem.
	 *
	 * @param siacTBilElem the new siac t bil elem
	 */
	public void setSiacTBilElem(SiacTBilElem siacTBilElem) {
		this.siacTBilElem = siacTBilElem;
	}



	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return causElemId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.causElemId = uid;
	}

}