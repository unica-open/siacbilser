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
 * The persistent class for the siac_r_class_fam_tree database table.
 * 
 */
@Entity
@Table(name="siac_r_class_fam_tree")
public class SiacRClassFamTreeFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="classif_classif_fam_tree_id")
	private Integer classifClassifFamTreeId;

	@Column(name="classif_id_padre")
	private Integer classifIdPadre;

	private Integer livello;

	private String ordine;

	//bi-directional many-to-one association to SiacTClassFin
	@ManyToOne
	@JoinColumn(name="classif_id")
	private SiacTClassFin siacTClass1;

	

	//bi-directional many-to-one association to SiacTClassFamTreeFin
	@ManyToOne
	@JoinColumn(name="classif_fam_tree_id")
	private SiacTClassFamTreeFin siacTClassFamTree;

	public SiacRClassFamTreeFin() {
	}

	public Integer getClassifClassifFamTreeId() {
		return this.classifClassifFamTreeId;
	}

	public void setClassifClassifFamTreeId(Integer classifClassifFamTreeId) {
		this.classifClassifFamTreeId = classifClassifFamTreeId;
	}

	public Integer getClassifIdPadre() {
		return this.classifIdPadre;
	}

	public void setClassifIdPadre(Integer classifIdPadre) {
		this.classifIdPadre = classifIdPadre;
	}

	public Integer getLivello() {
		return this.livello;
	}

	public void setLivello(Integer livello) {
		this.livello = livello;
	}

	public String getOrdine() {
		return this.ordine;
	}

	public void setOrdine(String ordine) {
		this.ordine = ordine;
	}

	public SiacTClassFin getSiacTClass1() {
		return this.siacTClass1;
	}

	public void setSiacTClass1(SiacTClassFin siacTClass1) {
		this.siacTClass1 = siacTClass1;
	}

	
	public SiacTClassFamTreeFin getSiacTClassFamTree() {
		return this.siacTClassFamTree;
	}

	public void setSiacTClassFamTree(SiacTClassFamTreeFin siacTClassFamTree) {
		this.siacTClassFamTree = siacTClassFamTree;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.classifClassifFamTreeId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.classifClassifFamTreeId = uid;
	}
}