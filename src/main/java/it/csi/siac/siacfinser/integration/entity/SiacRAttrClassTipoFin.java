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
 * The persistent class for the siac_r_attr_class_tipo database table.
 * 
 */
@Entity
@Table(name="siac_r_attr_class_tipo")
public class SiacRAttrClassTipoFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="attr_class_tipo_id")
	private Integer attrClassTipoId;

	//bi-directional many-to-one association to SiacDClassTipoFin
	@ManyToOne
	@JoinColumn(name="classif_tipo_id")
	private SiacDClassTipoFin siacDClassTipo;

	//bi-directional many-to-one association to SiacTAttrFin
	@ManyToOne
	@JoinColumn(name="attr_id")
	private SiacTAttrFin siacTAttr;

	public SiacRAttrClassTipoFin() {
	}

	public Integer getAttrClassTipoId() {
		return this.attrClassTipoId;
	}

	public void setAttrClassTipoId(Integer attrClassTipoId) {
		this.attrClassTipoId = attrClassTipoId;
	}

	public SiacDClassTipoFin getSiacDClassTipo() {
		return this.siacDClassTipo;
	}

	public void setSiacDClassTipo(SiacDClassTipoFin siacDClassTipo) {
		this.siacDClassTipo = siacDClassTipo;
	}

	public SiacTAttrFin getSiacTAttr() {
		return this.siacTAttr;
	}

	public void setSiacTAttr(SiacTAttrFin siacTAttr) {
		this.siacTAttr = siacTAttr;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.attrClassTipoId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.attrClassTipoId = uid;
	}
}