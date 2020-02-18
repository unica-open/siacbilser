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
 * The persistent class for the siac_r_bil_elem_tipo_class_tip database table.
 * 
 */
@Entity
@Table(name="siac_r_bil_elem_tipo_class_tip")
public class SiacRBilElemTipoClassTipFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id	
	@SequenceGenerator(name="SIAC_R_BIL_ELEM_TIPO_CLASS_TIP_ELEM_TIPO_CLASSIF_TIPO_ID_GENERATOR", allocationSize=1, sequenceName="siac_r_bil_elem_tipo_class_tip_elem_tipo_classif_tipo_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_BIL_ELEM_TIPO_CLASS_TIP_ELEM_TIPO_CLASSIF_TIPO_ID_GENERATOR")
	@Column(name="elem_tipo_classif_tipo_id")
	private Integer elemTipoClassifTipoId;

	//bi-directional many-to-one association to SiacDBilElemTipoFin
	@ManyToOne
	@JoinColumn(name="elem_tipo_id")
	private SiacDBilElemTipoFin siacDBilElemTipo;

	//bi-directional many-to-one association to SiacDClassTipoFin
	@ManyToOne
	@JoinColumn(name="classif_tipo_id")
	private SiacDClassTipoFin siacDClassTipo;

	public SiacRBilElemTipoClassTipFin() {
	}

	public Integer getElemTipoClassifTipoId() {
		return this.elemTipoClassifTipoId;
	}

	public void setElemTipoClassifTipoId(Integer elemTipoClassifTipoId) {
		this.elemTipoClassifTipoId = elemTipoClassifTipoId;
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
		return this.elemTipoClassifTipoId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.elemTipoClassifTipoId = uid;
	}
}