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


/**
 * The persistent class for the siac_r_pdce_conto_attr database table.
 * 
 */
@Entity
@Table(name="siac_r_pdce_fam_class_fam")
@NamedQuery(name="SiacRPdceFamClassFam.findAll", query="SELECT s FROM SiacRPdceFamClassFam s")
public class SiacRPdceFamClassFam extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_PDCE_FAM_CLASS_FAM_GENERATOR", allocationSize=1, sequenceName="SIAC_R_PDCE_FAM_CLASS_FAM_PDCE_CONTO_CLASSIF_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_PDCE_FAM_CLASS_FAM_GENERATOR")
	@Column(name="pdce_conto_classif_id")
	private Integer pdceContoClassifId;

	//bi-directional many-to-one association to SiacTAttr
	@ManyToOne
	@JoinColumn(name="pdce_fam_id")
	private SiacDPdceFam siacDPdceFam;

	//bi-directional many-to-one association to SiacTPdceConto
	@ManyToOne
	@JoinColumn(name="classif_fam_id")
	private SiacDClassFam siacDClassFam;

	public SiacRPdceFamClassFam() {
	}

	/**
	 * @return the pdceContoClassifId
	 */
	public Integer getPdceContoClassifId() {
		return pdceContoClassifId;
	}

	/**
	 * @param pdceContoClassifId the pdceContoClassifId to set
	 */
	public void setPdceContoClassifId(Integer pdceContoClassifId) {
		this.pdceContoClassifId = pdceContoClassifId;
	}

	/**
	 * @return the siacDPdceFam
	 */
	public SiacDPdceFam getSiacDPdceFam() {
		return siacDPdceFam;
	}

	/**
	 * @param siacDPdceFam the siacDPdceFam to set
	 */
	public void setSiacDPdceFam(SiacDPdceFam siacDPdceFam) {
		this.siacDPdceFam = siacDPdceFam;
	}


	/**
	 * @return the siacDClassFam
	 */
	public SiacDClassFam getSiacDClassFam() {
		return siacDClassFam;
	}

	/**
	 * @param siacDClassFam the siacDClassFam to set
	 */
	public void setSiacDClassFam(SiacDClassFam siacDClassFam) {
		this.siacDClassFam = siacDClassFam;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return pdceContoClassifId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		pdceContoClassifId = uid;
	}

}