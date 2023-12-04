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
 * The persistent class for the siac_d_class_fam database table.
 * 
 */
@Entity
@Table(name="siac_d_class_fam")
public class SiacDClassFamFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="classif_fam_id")
	private Integer classifFamId;

	@Column(name="classif_fam_code")
	private String classifFamCode;

	@Column(name="classif_fam_desc")
	private String classifFamDesc;

	//bi-directional many-to-one association to SiacTClassFamTreeFin
	@OneToMany(mappedBy="siacDClassFam")
	private List<SiacTClassFamTreeFin> siacTClassFamTrees;

	public SiacDClassFamFin() {
	}

	public Integer getClassifFamId() {
		return this.classifFamId;
	}

	public void setClassifFamId(Integer classifFamId) {
		this.classifFamId = classifFamId;
	}

	public String getClassifFamCode() {
		return this.classifFamCode;
	}

	public void setClassifFamCode(String classifFamCode) {
		this.classifFamCode = classifFamCode;
	}

	public String getClassifFamDesc() {
		return this.classifFamDesc;
	}

	public void setClassifFamDesc(String classifFamDesc) {
		this.classifFamDesc = classifFamDesc;
	}

	public List<SiacTClassFamTreeFin> getSiacTClassFamTrees() {
		return this.siacTClassFamTrees;
	}

	public void setSiacTClassFamTrees(List<SiacTClassFamTreeFin> siacTClassFamTrees) {
		this.siacTClassFamTrees = siacTClassFamTrees;
	}

	public SiacTClassFamTreeFin addSiacTClassFamTree(SiacTClassFamTreeFin siacTClassFamTree) {
		getSiacTClassFamTrees().add(siacTClassFamTree);
		siacTClassFamTree.setSiacDClassFam(this);

		return siacTClassFamTree;
	}

	public SiacTClassFamTreeFin removeSiacTClassFamTree(SiacTClassFamTreeFin siacTClassFamTree) {
		getSiacTClassFamTrees().remove(siacTClassFamTree);
		siacTClassFamTree.setSiacDClassFam(null);

		return siacTClassFamTree;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.classifFamId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.classifFamId = uid;
	}

}