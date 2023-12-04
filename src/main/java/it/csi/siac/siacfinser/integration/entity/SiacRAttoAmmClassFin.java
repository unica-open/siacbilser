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
 * The persistent class for the siac_r_atto_amm_class database table.
 * 
 */
@Entity
@Table(name="siac_r_atto_amm_class")
public class SiacRAttoAmmClassFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="atto_amm_class_id")
	private Integer attoAmmClassId;

	//bi-directional many-to-one association to SiacTAttoAmmFin
	@ManyToOne
	@JoinColumn(name="attoamm_id")
	private SiacTAttoAmmFin siacTAttoAmm;

	//bi-directional many-to-one association to SiacTClassFin
	@ManyToOne
	@JoinColumn(name="classif_id")
	private SiacTClassFin siacTClass;

	public SiacRAttoAmmClassFin() {
	}

	public Integer getAttoAmmClassId() {
		return this.attoAmmClassId;
	}

	public void setAttoAmmClassId(Integer attoAmmClassId) {
		this.attoAmmClassId = attoAmmClassId;
	}

	public SiacTAttoAmmFin getSiacTAttoAmm() {
		return this.siacTAttoAmm;
	}

	public void setSiacTAttoAmm(SiacTAttoAmmFin siacTAttoAmm) {
		this.siacTAttoAmm = siacTAttoAmm;
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
		return this.attoAmmClassId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.attoAmmClassId = uid;
	}
}