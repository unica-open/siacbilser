/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import it.csi.siac.siacfinser.integration.entity.base.SiacTEnteBase;


/**
 * The persistent class for the siac_d_bil_elem_tipo database table.
 * 
 */
@Entity
@Table(name="siac_d_bil_elem_tipo")
public class SiacDBilElemTipoFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_D_BIL_ELEM_TIPO_ELEM_TIPO_ID_GENERATOR", allocationSize=1, sequenceName="siac_d_bil_elem_tipo_elem_tipo_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_BIL_ELEM_TIPO_ELEM_TIPO_ID_GENERATOR")
	@Column(name="elem_tipo_id")
	private Integer elemTipoId;

	@Column(name="elem_tipo_code")
	private String elemTipoCode;

	@Column(name="elem_tipo_desc")
	private String elemTipoDesc;

	//bi-directional many-to-one association to SiacRAttrBilElemTipoFin
	@OneToMany(mappedBy="siacDBilElemTipo")
	private List<SiacRAttrBilElemTipoFin> siacRAttrBilElemTipos;

	//bi-directional many-to-one association to SiacRBilElemTipoClassTipFin
	@OneToMany(mappedBy="siacDBilElemTipo")
	private List<SiacRBilElemTipoClassTipFin> siacRBilElemTipoClassTips;

	//bi-directional many-to-one association to SiacTBilElemFin
	@OneToMany(mappedBy="siacDBilElemTipo")
	private List<SiacTBilElemFin> siacTBilElems;

	//bi-directional many-to-one association to SiacRBilElemTipoClassTipElemCodeFin
	@OneToMany(mappedBy="siacDBilElemTipo")
	private List<SiacRBilElemTipoClassTipElemCodeFin> siacRBilElemTipoClassTipElemCodes;

	public SiacDBilElemTipoFin() {
	}

	public Integer getElemTipoId() {
		return this.elemTipoId;
	}

	public void setElemTipoId(Integer elemTipoId) {
		this.elemTipoId = elemTipoId;
	}

	public String getElemTipoCode() {
		return this.elemTipoCode;
	}

	public void setElemTipoCode(String elemTipoCode) {
		this.elemTipoCode = elemTipoCode;
	}

	public String getElemTipoDesc() {
		return this.elemTipoDesc;
	}

	public void setElemTipoDesc(String elemTipoDesc) {
		this.elemTipoDesc = elemTipoDesc;
	}

	public List<SiacRAttrBilElemTipoFin> getSiacRAttrBilElemTipos() {
		return this.siacRAttrBilElemTipos;
	}

	public void setSiacRAttrBilElemTipos(List<SiacRAttrBilElemTipoFin> siacRAttrBilElemTipos) {
		this.siacRAttrBilElemTipos = siacRAttrBilElemTipos;
	}

	public SiacRAttrBilElemTipoFin addSiacRAttrBilElemTipo(SiacRAttrBilElemTipoFin siacRAttrBilElemTipo) {
		getSiacRAttrBilElemTipos().add(siacRAttrBilElemTipo);
		siacRAttrBilElemTipo.setSiacDBilElemTipo(this);

		return siacRAttrBilElemTipo;
	}

	public SiacRAttrBilElemTipoFin removeSiacRAttrBilElemTipo(SiacRAttrBilElemTipoFin siacRAttrBilElemTipo) {
		getSiacRAttrBilElemTipos().remove(siacRAttrBilElemTipo);
		siacRAttrBilElemTipo.setSiacDBilElemTipo(null);

		return siacRAttrBilElemTipo;
	}

	public List<SiacRBilElemTipoClassTipFin> getSiacRBilElemTipoClassTips() {
		return this.siacRBilElemTipoClassTips;
	}

	public void setSiacRBilElemTipoClassTips(List<SiacRBilElemTipoClassTipFin> siacRBilElemTipoClassTips) {
		this.siacRBilElemTipoClassTips = siacRBilElemTipoClassTips;
	}

	public SiacRBilElemTipoClassTipFin addSiacRBilElemTipoClassTip(SiacRBilElemTipoClassTipFin siacRBilElemTipoClassTip) {
		getSiacRBilElemTipoClassTips().add(siacRBilElemTipoClassTip);
		siacRBilElemTipoClassTip.setSiacDBilElemTipo(this);

		return siacRBilElemTipoClassTip;
	}

	public SiacRBilElemTipoClassTipFin removeSiacRBilElemTipoClassTip(SiacRBilElemTipoClassTipFin siacRBilElemTipoClassTip) {
		getSiacRBilElemTipoClassTips().remove(siacRBilElemTipoClassTip);
		siacRBilElemTipoClassTip.setSiacDBilElemTipo(null);

		return siacRBilElemTipoClassTip;
	}

	public List<SiacTBilElemFin> getSiacTBilElems() {
		return this.siacTBilElems;
	}

	public void setSiacTBilElems(List<SiacTBilElemFin> siacTBilElems) {
		this.siacTBilElems = siacTBilElems;
	}

	public SiacTBilElemFin addSiacTBilElem(SiacTBilElemFin siacTBilElem) {
		getSiacTBilElems().add(siacTBilElem);
		siacTBilElem.setSiacDBilElemTipo(this);

		return siacTBilElem;
	}

	public SiacTBilElemFin removeSiacTBilElem(SiacTBilElemFin siacTBilElem) {
		getSiacTBilElems().remove(siacTBilElem);
		siacTBilElem.setSiacDBilElemTipo(null);

		return siacTBilElem;
	}

	public List<SiacRBilElemTipoClassTipElemCodeFin> getSiacRBilElemTipoClassTipElemCodes() {
		return this.siacRBilElemTipoClassTipElemCodes;
	}

	public void setSiacRBilElemTipoClassTipElemCodes(List<SiacRBilElemTipoClassTipElemCodeFin> siacRBilElemTipoClassTipElemCodes) {
		this.siacRBilElemTipoClassTipElemCodes = siacRBilElemTipoClassTipElemCodes;
	}

	public SiacRBilElemTipoClassTipElemCodeFin addSiacRBilElemTipoClassTipElemCode(SiacRBilElemTipoClassTipElemCodeFin siacRBilElemTipoClassTipElemCode) {
		getSiacRBilElemTipoClassTipElemCodes().add(siacRBilElemTipoClassTipElemCode);
		siacRBilElemTipoClassTipElemCode.setSiacDBilElemTipo(this);

		return siacRBilElemTipoClassTipElemCode;
	}

	public SiacRBilElemTipoClassTipElemCodeFin removeSiacRBilElemTipoClassTipElemCode(SiacRBilElemTipoClassTipElemCodeFin siacRBilElemTipoClassTipElemCode) {
		getSiacRBilElemTipoClassTipElemCodes().remove(siacRBilElemTipoClassTipElemCode);
		siacRBilElemTipoClassTipElemCode.setSiacDBilElemTipo(null);

		return siacRBilElemTipoClassTipElemCode;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.elemTipoId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.elemTipoId = uid;
	}
}