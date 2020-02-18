/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity;

import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import it.csi.siac.siacfinser.integration.entity.base.SiacTEnteBase;

@Entity
@Table(name = "siac_r_class_fam_tree")
public class SiacCodificaFamigliaTree extends SiacTEnteBase{

	/** Per la serializzazione */
	private static final long serialVersionUID = -5913454899126906349L;

	@Id
	@Column(name = "classif_classif_fam_tree_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_CLASS_FAM_TREE")
	@SequenceGenerator(name = "SEQ_CLASS_FAM_TREE", sequenceName = "siac_r_class_fam_tree_classif_classif_fam_tree_id_seq")
	private int uid;

	@ManyToOne
	@JoinColumn(name = "classif_id", nullable = true)
	private SiacCodifica codifica;

	@Basic
	@Column(name = "classif_id_padre")
	private Integer codificaIdPadre;

	@Basic
	@Column(name = "classif_fam_tree_id")
	private int famigliaTreeId;

	@Basic
	@Column(name = "livello")
	private int livello;

	@Basic
	@Column(name = "ordine")
	private String ordine;

	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "classif_id_padre", insertable = false, updatable = false)
	private SiacCodificaFamigliaTree parent;
	

	@OneToMany
	@OrderColumn
	@JoinColumn(name = "classif_id_padre")
	private Set<SiacCodificaFamigliaTree> child;
	
		
	public SiacCodificaFamigliaTree getParent() {
		return parent;
	}

	public void setParent(SiacCodificaFamigliaTree parent) {
		this.parent = parent;
	}

	public Set<SiacCodificaFamigliaTree> getChild() {
		return child;
	}

	public void setChild(Set<SiacCodificaFamigliaTree> child) {
		this.child = child;
	}

	public String getOrdine() {
		return ordine;
	}

	public void setOrdine(String ordine) {
		this.ordine = ordine;
	}

	public int getLivello() {
		return livello;
	}

	public void setLivello(int livello) {
		this.livello = livello;
	}

	public SiacCodifica getCodifica() {
		return codifica;
	}

	public void setCodifica(SiacCodifica codifica) {
		this.codifica = codifica;
	}

	public int getCodificaIdPadre() {
		return codificaIdPadre;
	}

	public void setCodificaIdPadre(int codificaIdPadre) {
		this.codificaIdPadre = codificaIdPadre;
	}

	@Override
	public Integer getUid() {
		return uid;
	}

	@Override
	public void setUid(Integer uid) {
		this.uid = uid;
	}

	public int getFamigliaTreeId() {
		return famigliaTreeId;
	}

	public void setFamigliaTreeId(int famigliaTreeId) {
		this.famigliaTreeId = famigliaTreeId;
	}

}
