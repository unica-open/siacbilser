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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**
 * The persistent class for the siac_r_mov_ep_det_class database table.
 * 
 */
@Entity
@Table(name="siac_r_mov_ep_det_class")
public class SiacRMovEpDetClass extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The elem classif id. */
	@Id
	@SequenceGenerator(name="SIAC_R_MOV_EP_DET_CLASS_MOVEPDETCLASSIFID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_MOV_EP_DET_CLASS_MOVEP_DET_CLASSIF_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_MOV_EP_DET_CLASS_MOVEPDETCLASSIFID_GENERATOR")
	@Column(name="movep_det_classif_id")
	private Integer movepDetClassifId;

	//bi-directional many-to-one association to SiacTBilElem
	/** The siac t bil elem. */
	@ManyToOne
	@JoinColumn(name="movep_det_id")
	private SiacTMovEpDet siacTMovEpDet;

	//bi-directional many-to-one association to SiacTClass
	/** The siac t class. */
	@ManyToOne
	@JoinColumn(name="classif_id")
	private SiacTClass siacTClass;

	/**
	 * Instantiates a new siac r bil elem class.
	 */
	public SiacRMovEpDetClass() {
	}

	/**
	 * Gets the movep det classif id.
	 * 
	 * @return the movep det classif id
	 */
	public Integer getMovepDetClassifId() {
		return movepDetClassifId;
	}

	/**
	 * Sets the movep det classif id
	 * 
	 * @param movepDetClassifId the new movep det classif id
	 */
	public void setMovepDetClassifId(Integer movepDetClassifId) {
		this.movepDetClassifId = movepDetClassifId;
	}

	/**
	 * Gets the siac t mov ep det.
	 * 
	 * @return the siac t mov ep det
	 */
	public SiacTMovEpDet getSiacTMovEpDet() {
		return siacTMovEpDet;
	}

	/**
	 * Sets the siac t mov ep det.
	 * 
	 * @param siacTMovEpDet the siac t mov ep det to set
	 */
	public void setSiacTMovEpDet(SiacTMovEpDet siacTMovEpDet) {
		this.siacTMovEpDet = siacTMovEpDet;
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

	@Override
	public Integer getUid() {
		return movepDetClassifId;
	}

	@Override
	public void setUid(Integer uid) {
		this.movepDetClassifId = uid;
	}

}