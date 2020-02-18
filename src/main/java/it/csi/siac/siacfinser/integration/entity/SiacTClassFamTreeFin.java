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
 * The persistent class for the siac_t_class_fam_tree database table.
 * 
 */
@Entity
@Table(name="siac_t_class_fam_tree")
public class SiacTClassFamTreeFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="classif_fam_tree_id")
	private Integer classifFamTreeId;

	@Column(name="class_fam_code")
	private String classFamCode;

	@Column(name="class_fam_desc")
	private String classFamDesc;

	//bi-directional many-to-one association to SiacRClassFamTreeFin
	@OneToMany(mappedBy="siacTClassFamTree")
	private List<SiacRClassFamTreeFin> siacRClassFamTrees;

	//bi-directional many-to-one association to SiacDClassFamFin
	@ManyToOne
	@JoinColumn(name="classif_fam_id")
	private SiacDClassFamFin siacDClassFam;

	public SiacTClassFamTreeFin() {
	}

	public Integer getClassifFamTreeId() {
		return this.classifFamTreeId;
	}

	public void setClassifFamTreeId(Integer classifFamTreeId) {
		this.classifFamTreeId = classifFamTreeId;
	}

	public String getClassFamCode() {
		return this.classFamCode;
	}

	public void setClassFamCode(String classFamCode) {
		this.classFamCode = classFamCode;
	}

	public String getClassFamDesc() {
		return this.classFamDesc;
	}

	public void setClassFamDesc(String classFamDesc) {
		this.classFamDesc = classFamDesc;
	}

	public List<SiacRClassFamTreeFin> getSiacRClassFamTrees() {
		return this.siacRClassFamTrees;
	}

	public void setSiacRClassFamTrees(List<SiacRClassFamTreeFin> siacRClassFamTrees) {
		this.siacRClassFamTrees = siacRClassFamTrees;
	}

	public SiacRClassFamTreeFin addSiacRClassFamTree(SiacRClassFamTreeFin siacRClassFamTree) {
		getSiacRClassFamTrees().add(siacRClassFamTree);
		siacRClassFamTree.setSiacTClassFamTree(this);

		return siacRClassFamTree;
	}

	public SiacRClassFamTreeFin removeSiacRClassFamTree(SiacRClassFamTreeFin siacRClassFamTree) {
		getSiacRClassFamTrees().remove(siacRClassFamTree);
		siacRClassFamTree.setSiacTClassFamTree(null);

		return siacRClassFamTree;
	}

	public SiacDClassFamFin getSiacDClassFam() {
		return this.siacDClassFam;
	}

	public void setSiacDClassFam(SiacDClassFamFin siacDClassFam) {
		this.siacDClassFam = siacDClassFam;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.classifFamTreeId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.classifFamTreeId = uid;
	}
}