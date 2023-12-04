/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import it.csi.siac.siacfinser.integration.entity.base.SiacTEnteBase;


/**
 * The persistent class for the siac_r_attr_bil_elem_tipo database table.
 * 
 */
@Entity
@Table(name="siac_r_attr_bil_elem_tipo")
public class SiacRAttrBilElemTipoFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id	
	@SequenceGenerator(name="SIAC_R_ATTR_BIL_ELEM_TIPO_ATTR_BIL_ELEM_TIPO_ID_GENERATOR", allocationSize=1, sequenceName="siac_r_attr_bil_elem_tipo_attr_bil_elem_tipo_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_ATTR_BIL_ELEM_TIPO_ATTR_BIL_ELEM_TIPO_ID_GENERATOR")
	@Column(name="attr_bil_elem_tipo_id")
	private Integer attrBilElemTipoId;

	//bi-directional many-to-one association to SiacDBilElemTipoFin
	@ManyToOne
	@JoinColumn(name="elem_tipo_id")
	private SiacDBilElemTipoFin siacDBilElemTipo;

	//bi-directional many-to-one association to SiacTAttrFin
	@ManyToOne
	@JoinColumn(name="attr_id")
	private SiacTAttrFin siacTAttr;

	public SiacRAttrBilElemTipoFin() {
	}

	public Integer getAttrBilElemTipoId() {
		return this.attrBilElemTipoId;
	}

	public void setAttrBilElemTipoId(Integer attrBilElemTipoId) {
		this.attrBilElemTipoId = attrBilElemTipoId;
	}

	public SiacDBilElemTipoFin getSiacDBilElemTipo() {
		return this.siacDBilElemTipo;
	}

	public void setSiacDBilElemTipo(SiacDBilElemTipoFin siacDBilElemTipo) {
		this.siacDBilElemTipo = siacDBilElemTipo;
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
		return this.attrBilElemTipoId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.attrBilElemTipoId = uid;
	}
}