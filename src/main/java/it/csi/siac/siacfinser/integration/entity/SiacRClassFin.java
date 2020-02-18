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
 * The persistent class for the siac_r_class database table.
 * 
 */
@Entity
@Table(name="siac_r_class")
public class SiacRClassFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="classif_classif_id")
	private Integer classifClassifId;

	//bi-directional many-to-one association to SiacTClassFin
	@ManyToOne
	@JoinColumn(name="classif_b_id")
	private SiacTClassFin siacTClass1;

	//bi-directional many-to-one association to SiacTClassFin
	@ManyToOne
	@JoinColumn(name="classif_a_id")
	private SiacTClassFin siacTClass2;

	public SiacRClassFin() {
	}

	public Integer getClassifClassifId() {
		return this.classifClassifId;
	}

	public void setClassifClassifId(Integer classifClassifId) {
		this.classifClassifId = classifClassifId;
	}

	public SiacTClassFin getSiacTClass1() {
		return this.siacTClass1;
	}

	public void setSiacTClass1(SiacTClassFin siacTClass1) {
		this.siacTClass1 = siacTClass1;
	}

	public SiacTClassFin getSiacTClass2() {
		return this.siacTClass2;
	}

	public void setSiacTClass2(SiacTClassFin siacTClass2) {
		this.siacTClass2 = siacTClass2;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.classifClassifId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.classifClassifId = uid;
	}
}