/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import it.csi.siac.siacfinser.integration.entity.base.SiacTEnteBase;


/**
 * The persistent class for the siac_r_causale_class database table.
 * 
 */
@Entity
@Table(name="siac_r_causale_class")
public class SiacRCausaleClassFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="caus_classif_id")
	private Integer causClassifId;

	//bi-directional many-to-one association to SiacDCausaleFin
	@ManyToOne
	@JoinColumn(name="caus_id")
	private SiacDCausaleFin siacDCausale;

	//bi-directional many-to-one association to SiacTClassFin
	@ManyToOne
	@JoinColumn(name="classif_id")
	private SiacTClassFin siacTClass;

	public SiacRCausaleClassFin() {
	}

	public Integer getCausClassifId() {
		return this.causClassifId;
	}

	public void setCausClassifId(Integer causClassifId) {
		this.causClassifId = causClassifId;
	}

	public SiacDCausaleFin getSiacDCausale() {
		return this.siacDCausale;
	}

	public void setSiacDCausale(SiacDCausaleFin siacDCausale) {
		this.siacDCausale = siacDCausale;
	}

	public SiacTClassFin getSiacTClass() {
		return this.siacTClass;
	}

	public void setSiacTClass(SiacTClassFin siacTClass) {
		this.siacTClass = siacTClass;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.causClassifId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.causClassifId = uid;
	}

}