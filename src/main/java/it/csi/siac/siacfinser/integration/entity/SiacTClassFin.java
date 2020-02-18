/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import it.csi.siac.siacfinser.integration.entity.base.SiacTEnteBase;


/**
 * The persistent class for the siac_t_class database table.
 * 
 */
@Entity
@Table(name="siac_t_class")
public class SiacTClassFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="classif_id")
	private Integer classifId;

	@Column(name="classif_code")
	private String classifCode;

	@Column(name="classif_desc")
	private String classifDesc;

	//bi-directional many-to-one association to SiacRAttoAmmClassFin
	@OneToMany(mappedBy="siacTClass")
	private List<SiacRAttoAmmClassFin> siacRAttoAmmClasses;

	//bi-directional many-to-one association to SiacRClassFin
	@OneToMany(mappedBy="siacTClass1")
	private List<SiacRClassFin> siacRClasses1;

	

	//bi-directional many-to-one association to SiacRClassAttrFin
	@OneToMany(mappedBy="siacTClass")
	private List<SiacRClassAttrFin> siacRClassAttrs;

	

	//bi-directional many-to-one association to SiacRGruppoRuoloOpFin
	@OneToMany(mappedBy="siacTClass")
	private List<SiacRGruppoRuoloOpFin> siacRGruppoRuoloOps;

	//bi-directional many-to-one association to SiacDClassTipoFin
	@ManyToOne
	@JoinColumn(name="classif_tipo_id")
	private SiacDClassTipoFin siacDClassTipo;

	public SiacTClassFin(Integer uid) {
		setUid(uid);
	}
	
	public SiacTClassFin() {
	}

	public Integer getClassifId() {
		return this.classifId;
	}

	public void setClassifId(Integer classifId) {
		this.classifId = classifId;
	}

	public String getClassifCode() {
		return this.classifCode;
	}

	public void setClassifCode(String classifCode) {
		this.classifCode = classifCode;
	}

	public String getClassifDesc() {
		return this.classifDesc;
	}

	public void setClassifDesc(String classifDesc) {
		this.classifDesc = classifDesc;
	}

	public List<SiacRAttoAmmClassFin> getSiacRAttoAmmClasses() {
		return this.siacRAttoAmmClasses;
	}

	public void setSiacRAttoAmmClasses(List<SiacRAttoAmmClassFin> siacRAttoAmmClasses) {
		this.siacRAttoAmmClasses = siacRAttoAmmClasses;
	}

	public SiacRAttoAmmClassFin addSiacRAttoAmmClass(SiacRAttoAmmClassFin siacRAttoAmmClass) {
		getSiacRAttoAmmClasses().add(siacRAttoAmmClass);
		siacRAttoAmmClass.setSiacTClass(this);

		return siacRAttoAmmClass;
	}

	public SiacRAttoAmmClassFin removeSiacRAttoAmmClass(SiacRAttoAmmClassFin siacRAttoAmmClass) {
		getSiacRAttoAmmClasses().remove(siacRAttoAmmClass);
		siacRAttoAmmClass.setSiacTClass(null);

		return siacRAttoAmmClass;
	}

	public List<SiacRClassFin> getSiacRClasses1() {
		return this.siacRClasses1;
	}

	public void setSiacRClasses1(List<SiacRClassFin> siacRClasses1) {
		this.siacRClasses1 = siacRClasses1;
	}

	public SiacRClassFin addSiacRClasses1(SiacRClassFin siacRClasses1) {
		getSiacRClasses1().add(siacRClasses1);
		siacRClasses1.setSiacTClass1(this);

		return siacRClasses1;
	}

	public SiacRClassFin removeSiacRClasses1(SiacRClassFin siacRClasses1) {
		getSiacRClasses1().remove(siacRClasses1);
		siacRClasses1.setSiacTClass1(null);

		return siacRClasses1;
	}

	


	public List<SiacRClassAttrFin> getSiacRClassAttrs() {
		return this.siacRClassAttrs;
	}

	public void setSiacRClassAttrs(List<SiacRClassAttrFin> siacRClassAttrs) {
		this.siacRClassAttrs = siacRClassAttrs;
	}

	public SiacRClassAttrFin addSiacRClassAttr(SiacRClassAttrFin siacRClassAttr) {
		getSiacRClassAttrs().add(siacRClassAttr);
		siacRClassAttr.setSiacTClass(this);

		return siacRClassAttr;
	}

	public SiacRClassAttrFin removeSiacRClassAttr(SiacRClassAttrFin siacRClassAttr) {
		getSiacRClassAttrs().remove(siacRClassAttr);
		siacRClassAttr.setSiacTClass(null);

		return siacRClassAttr;
	}

	public List<SiacRGruppoRuoloOpFin> getSiacRGruppoRuoloOps() {
		return this.siacRGruppoRuoloOps;
	}

	public void setSiacRGruppoRuoloOps(List<SiacRGruppoRuoloOpFin> siacRGruppoRuoloOps) {
		this.siacRGruppoRuoloOps = siacRGruppoRuoloOps;
	}

	public SiacRGruppoRuoloOpFin addSiacRGruppoRuoloOp(SiacRGruppoRuoloOpFin siacRGruppoRuoloOp) {
		getSiacRGruppoRuoloOps().add(siacRGruppoRuoloOp);
		siacRGruppoRuoloOp.setSiacTClass(this);

		return siacRGruppoRuoloOp;
	}

	public SiacRGruppoRuoloOpFin removeSiacRGruppoRuoloOp(SiacRGruppoRuoloOpFin siacRGruppoRuoloOp) {
		getSiacRGruppoRuoloOps().remove(siacRGruppoRuoloOp);
		siacRGruppoRuoloOp.setSiacTClass(null);

		return siacRGruppoRuoloOp;
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
		return this.classifId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.classifId = uid;
	}
}