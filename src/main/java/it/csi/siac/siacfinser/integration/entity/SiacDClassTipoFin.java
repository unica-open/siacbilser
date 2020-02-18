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
 * The persistent class for the siac_d_class_tipo database table.
 * 
 */
@Entity
@Table(name="siac_d_class_tipo")
public class SiacDClassTipoFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="classif_tipo_id")
	private Integer classifTipoId;

	@Column(name="classif_tipo_code")
	private String classifTipoCode;

	@Column(name="classif_tipo_desc")
	private String classifTipoDesc;

	//bi-directional many-to-one association to SiacRAttrClassTipoFin
	@OneToMany(mappedBy="siacDClassTipo")
	private List<SiacRAttrClassTipoFin> siacRAttrClassTipos;

	//bi-directional many-to-one association to SiacTClassFin
	@OneToMany(mappedBy="siacDClassTipo")
	private List<SiacTClassFin> siacTClasses;

	public SiacDClassTipoFin() {
	}

	public Integer getClassifTipoId() {
		return this.classifTipoId;
	}

	public void setClassifTipoId(Integer classifTipoId) {
		this.classifTipoId = classifTipoId;
	}

	public String getClassifTipoCode() {
		return this.classifTipoCode;
	}

	public void setClassifTipoCode(String classifTipoCode) {
		this.classifTipoCode = classifTipoCode;
	}

	public String getClassifTipoDesc() {
		return this.classifTipoDesc;
	}

	public void setClassifTipoDesc(String classifTipoDesc) {
		this.classifTipoDesc = classifTipoDesc;
	}

	public List<SiacRAttrClassTipoFin> getSiacRAttrClassTipos() {
		return this.siacRAttrClassTipos;
	}

	public void setSiacRAttrClassTipos(List<SiacRAttrClassTipoFin> siacRAttrClassTipos) {
		this.siacRAttrClassTipos = siacRAttrClassTipos;
	}

	public SiacRAttrClassTipoFin addSiacRAttrClassTipo(SiacRAttrClassTipoFin siacRAttrClassTipo) {
		getSiacRAttrClassTipos().add(siacRAttrClassTipo);
		siacRAttrClassTipo.setSiacDClassTipo(this);

		return siacRAttrClassTipo;
	}

	public SiacRAttrClassTipoFin removeSiacRAttrClassTipo(SiacRAttrClassTipoFin siacRAttrClassTipo) {
		getSiacRAttrClassTipos().remove(siacRAttrClassTipo);
		siacRAttrClassTipo.setSiacDClassTipo(null);

		return siacRAttrClassTipo;
	}

	public List<SiacTClassFin> getSiacTClasses() {
		return this.siacTClasses;
	}

	public void setSiacTClasses(List<SiacTClassFin> siacTClasses) {
		this.siacTClasses = siacTClasses;
	}

	public SiacTClassFin addSiacTClass(SiacTClassFin siacTClass) {
		getSiacTClasses().add(siacTClass);
		siacTClass.setSiacDClassTipo(this);

		return siacTClass;
	}

	public SiacTClassFin removeSiacTClass(SiacTClassFin siacTClass) {
		getSiacTClasses().remove(siacTClass);
		siacTClass.setSiacDClassTipo(null);

		return siacTClass;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.classifTipoId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.classifTipoId = uid;
	}
}