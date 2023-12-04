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
 * The persistent class for the siac_r_bil_elem_tipo_class_tip_elem_code database table.
 * 
 */
@Entity
@Table(name="siac_r_bil_elem_tipo_class_tip_elem_code")
public class SiacRBilElemTipoClassTipElemCodeFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_BIL_ELEM_TIPO_CLASS_TI_ELEM_TIPO_CLASS_TIP_ELEM_CODE_ID_GENERATOR", allocationSize=1, sequenceName="siac_r_bil_elem_tipo_class_ti_elem_tipo_class_tip_elem_code_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_BIL_ELEM_TIPO_CLASS_TI_ELEM_TIPO_CLASS_TIP_ELEM_CODE_ID_GENERATOR")
	@Column(name="siac_r_bil_elem_tipo_class_tip_elem_code")
	private Integer siacRBilElemTipoClassTipElemCode;

	@Column(name="elem_code")
	private String elemCode;

	//bi-directional many-to-one association to SiacDBilElemTipoFin
	@ManyToOne
	@JoinColumn(name="elem_tipo_id")
	private SiacDBilElemTipoFin siacDBilElemTipo;

	//bi-directional many-to-one association to SiacDClassTipoFin
	@ManyToOne
	@JoinColumn(name="classif_tipo_id")
	private SiacDClassTipoFin siacDClassTipo;

	public SiacRBilElemTipoClassTipElemCodeFin() {
	}

	public Integer getSiacRBilElemTipoClassTipElemCode() {
		return this.siacRBilElemTipoClassTipElemCode;
	}

	public void setSiacRBilElemTipoClassTipElemCode(Integer siacRBilElemTipoClassTipElemCode) {
		this.siacRBilElemTipoClassTipElemCode = siacRBilElemTipoClassTipElemCode;
	}

	public String getElemCode() {
		return this.elemCode;
	}

	public void setElemCode(String elemCode) {
		this.elemCode = elemCode;
	}

	public SiacDBilElemTipoFin getSiacDBilElemTipo() {
		return this.siacDBilElemTipo;
	}

	public void setSiacDBilElemTipo(SiacDBilElemTipoFin siacDBilElemTipo) {
		this.siacDBilElemTipo = siacDBilElemTipo;
	}

	public SiacDClassTipoFin getSiacDClassTipo() {
		return this.siacDClassTipo;
	}

	public void setSiacDClassTipo(SiacDClassTipoFin siacDClassTipo) {
		this.siacDClassTipo = siacDClassTipo;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.siacRBilElemTipoClassTipElemCode;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.siacRBilElemTipoClassTipElemCode = uid;
	}
}