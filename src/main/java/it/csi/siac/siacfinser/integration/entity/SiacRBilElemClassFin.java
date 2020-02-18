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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import it.csi.siac.siacfinser.integration.entity.base.SiacTEnteBase;


/**
 * The persistent class for the siac_r_bil_elem_class database table.
 * 
 */
@Entity
@Table(name="siac_r_bil_elem_class")
public class SiacRBilElemClassFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_BIL_ELEM_CLASS_ELEM_CLASSIF_ID_GENERATOR", allocationSize=1, sequenceName="siac_r_bil_elem_class_elem_classif_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_BIL_ELEM_CLASS_ELEM_CLASSIF_ID_GENERATOR")
	@Column(name="elem_classif_id")
	private Integer elemClassifId;

	//bi-directional many-to-one association to SiacTBilElemFin
	@ManyToOne
	@JoinColumn(name="elem_id")
	private SiacTBilElemFin siacTBilElem;

	//bi-directional many-to-one association to SiacTClassFin
	@ManyToOne
	@JoinColumn(name="classif_id")
	private SiacTClassFin siacTClass;

	//bi-directional many-to-one association to SiacRBilElemClassVarFin
	@OneToMany(mappedBy="siacRBilElemClass")
	private List<SiacRBilElemClassVarFin> siacRBilElemClassVars;

	public SiacRBilElemClassFin() {
	}

	public Integer getElemClassifId() {
		return this.elemClassifId;
	}

	public void setElemClassifId(Integer elemClassifId) {
		this.elemClassifId = elemClassifId;
	}

	public SiacTBilElemFin getSiacTBilElem() {
		return this.siacTBilElem;
	}

	public void setSiacTBilElem(SiacTBilElemFin siacTBilElem) {
		this.siacTBilElem = siacTBilElem;
	}

	public SiacTClassFin getSiacTClass() {
		return this.siacTClass;
	}

	public void setSiacTClass(SiacTClassFin siacTClass) {
		this.siacTClass = siacTClass;
	}

	public List<SiacRBilElemClassVarFin> getSiacRBilElemClassVars() {
		return this.siacRBilElemClassVars;
	}

	public void setSiacRBilElemClassVars(List<SiacRBilElemClassVarFin> siacRBilElemClassVars) {
		this.siacRBilElemClassVars = siacRBilElemClassVars;
	}

	public SiacRBilElemClassVarFin addSiacRBilElemClassVar(SiacRBilElemClassVarFin siacRBilElemClassVar) {
		getSiacRBilElemClassVars().add(siacRBilElemClassVar);
		siacRBilElemClassVar.setSiacRBilElemClass(this);

		return siacRBilElemClassVar;
	}

	public SiacRBilElemClassVarFin removeSiacRBilElemClassVar(SiacRBilElemClassVarFin siacRBilElemClassVar) {
		getSiacRBilElemClassVars().remove(siacRBilElemClassVar);
		siacRBilElemClassVar.setSiacRBilElemClass(null);

		return siacRBilElemClassVar;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.elemClassifId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.elemClassifId = uid;
	}
}