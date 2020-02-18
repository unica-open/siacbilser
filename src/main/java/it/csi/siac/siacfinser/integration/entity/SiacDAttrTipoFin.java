/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import it.csi.siac.siacfinser.integration.entity.base.SiacTEnteBase;


/**
 * The persistent class for the siac_d_attr_tipo database table.
 * 
 */
@Entity
@Table(name="siac_d_attr_tipo")
public class SiacDAttrTipoFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="attr_tipo_id")
	private Integer attrTipoId;

	@Column(name="attr_tipo_code")
	private String attrTipoCode;

	@Column(name="attr_tipo_desc")
	private String attrTipoDesc;

	//bi-directional many-to-one association to SiacTAttrFin
	@OneToMany(mappedBy="siacDAttrTipo")
	private List<SiacTAttrFin> siacTAttrs;

	public SiacDAttrTipoFin() {
	}

	public Integer getAttrTipoId() {
		return this.attrTipoId;
	}

	public void setAttrTipoId(Integer attrTipoId) {
		this.attrTipoId = attrTipoId;
	}

	public String getAttrTipoCode() {
		return this.attrTipoCode;
	}

	public void setAttrTipoCode(String attrTipoCode) {
		this.attrTipoCode = attrTipoCode;
	}

	public String getAttrTipoDesc() {
		return this.attrTipoDesc;
	}

	public void setAttrTipoDesc(String attrTipoDesc) {
		this.attrTipoDesc = attrTipoDesc;
	}

	public List<SiacTAttrFin> getSiacTAttrs() {
		return this.siacTAttrs;
	}

	public void setSiacTAttrs(List<SiacTAttrFin> siacTAttrs) {
		this.siacTAttrs = siacTAttrs;
	}

	public SiacTAttrFin addSiacTAttr(SiacTAttrFin siacTAttr) {
		getSiacTAttrs().add(siacTAttr);
		siacTAttr.setSiacDAttrTipo(this);

		return siacTAttr;
	}

	public SiacTAttrFin removeSiacTAttr(SiacTAttrFin siacTAttr) {
		getSiacTAttrs().remove(siacTAttr);
		siacTAttr.setSiacDAttrTipo(null);

		return siacTAttr;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.attrTipoId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.attrTipoId = uid;
	}
}