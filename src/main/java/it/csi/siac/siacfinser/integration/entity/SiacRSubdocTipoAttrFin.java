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
 * The persistent class for the siac_r_subdoc_tipo_attr database table.
 * 
 */
@Entity
@Table(name="siac_r_subdoc_tipo_attr")
public class SiacRSubdocTipoAttrFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="subdoc_tipo_attr_id")
	private Integer subdocTipoAttrId;

	@Column(name="elem_code")
	private Integer elemCode;

	//bi-directional many-to-one association to SiacDSubdocTipoFin
	@ManyToOne
	@JoinColumn(name="subdoc_tipo_id")
	private SiacDSubdocTipoFin siacDSubdocTipo;

	//bi-directional many-to-one association to SiacTAttrFin
	@ManyToOne
	@JoinColumn(name="attr_id")
	private SiacTAttrFin siacTAttr;

	public SiacRSubdocTipoAttrFin() {
	}

	public Integer getSubdocTipoAttrId() {
		return this.subdocTipoAttrId;
	}

	public void setSubdocTipoAttrId(Integer subdocTipoAttrId) {
		this.subdocTipoAttrId = subdocTipoAttrId;
	}

	public Integer getElemCode() {
		return this.elemCode;
	}

	public void setElemCode(Integer elemCode) {
		this.elemCode = elemCode;
	}

	public SiacDSubdocTipoFin getSiacDSubdocTipo() {
		return this.siacDSubdocTipo;
	}

	public void setSiacDSubdocTipo(SiacDSubdocTipoFin siacDSubdocTipo) {
		this.siacDSubdocTipo = siacDSubdocTipo;
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
		return this.subdocTipoAttrId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.subdocTipoAttrId = uid;
	}
}