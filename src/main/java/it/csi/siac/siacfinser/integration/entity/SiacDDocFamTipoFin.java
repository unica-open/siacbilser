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
 * The persistent class for the siac_d_doc_fam_tipo database table.
 * 
 */
@Entity
@Table(name="siac_d_doc_fam_tipo")
public class SiacDDocFamTipoFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="doc_fam_tipo_id")
	private Integer docFamTipoId;

	@Column(name="doc_fam_tipo_code")
	private String docFamTipoCode;

	@Column(name="doc_fam_tipo_desc")
	private String docFamTipoDesc;

	//bi-directional many-to-one association to SiacDDocTipoFin
	@OneToMany(mappedBy="siacDDocFamTipo")
	private List<SiacDDocTipoFin> siacDDocTipos;

	//bi-directional many-to-one association to SiacRIvaRegTipoDocFamTipoFin
	@OneToMany(mappedBy="siacDDocFamTipo")
	private List<SiacRIvaRegTipoDocFamTipoFin> siacRIvaRegTipoDocFamTipos;

	//bi-directional many-to-one association to SiacTPredocFin
	@OneToMany(mappedBy="siacDDocFamTipo")
	private List<SiacTPredocFin> siacTPredocs;

	public SiacDDocFamTipoFin() {
	}

	public Integer getDocFamTipoId() {
		return this.docFamTipoId;
	}

	public void setDocFamTipoId(Integer docFamTipoId) {
		this.docFamTipoId = docFamTipoId;
	}

	public String getDocFamTipoCode() {
		return this.docFamTipoCode;
	}

	public void setDocFamTipoCode(String docFamTipoCode) {
		this.docFamTipoCode = docFamTipoCode;
	}

	public String getDocFamTipoDesc() {
		return this.docFamTipoDesc;
	}

	public void setDocFamTipoDesc(String docFamTipoDesc) {
		this.docFamTipoDesc = docFamTipoDesc;
	}

	public List<SiacDDocTipoFin> getSiacDDocTipos() {
		return this.siacDDocTipos;
	}

	public void setSiacDDocTipos(List<SiacDDocTipoFin> siacDDocTipos) {
		this.siacDDocTipos = siacDDocTipos;
	}

	public SiacDDocTipoFin addSiacDDocTipo(SiacDDocTipoFin siacDDocTipo) {
		getSiacDDocTipos().add(siacDDocTipo);
		siacDDocTipo.setSiacDDocFamTipo(this);

		return siacDDocTipo;
	}

	public SiacDDocTipoFin removeSiacDDocTipo(SiacDDocTipoFin siacDDocTipo) {
		getSiacDDocTipos().remove(siacDDocTipo);
		siacDDocTipo.setSiacDDocFamTipo(null);

		return siacDDocTipo;
	}

	public List<SiacRIvaRegTipoDocFamTipoFin> getSiacRIvaRegTipoDocFamTipos() {
		return this.siacRIvaRegTipoDocFamTipos;
	}

	public void setSiacRIvaRegTipoDocFamTipos(List<SiacRIvaRegTipoDocFamTipoFin> siacRIvaRegTipoDocFamTipos) {
		this.siacRIvaRegTipoDocFamTipos = siacRIvaRegTipoDocFamTipos;
	}

	public SiacRIvaRegTipoDocFamTipoFin addSiacRIvaRegTipoDocFamTipo(SiacRIvaRegTipoDocFamTipoFin siacRIvaRegTipoDocFamTipo) {
		getSiacRIvaRegTipoDocFamTipos().add(siacRIvaRegTipoDocFamTipo);
		siacRIvaRegTipoDocFamTipo.setSiacDDocFamTipo(this);

		return siacRIvaRegTipoDocFamTipo;
	}

	public SiacRIvaRegTipoDocFamTipoFin removeSiacRIvaRegTipoDocFamTipo(SiacRIvaRegTipoDocFamTipoFin siacRIvaRegTipoDocFamTipo) {
		getSiacRIvaRegTipoDocFamTipos().remove(siacRIvaRegTipoDocFamTipo);
		siacRIvaRegTipoDocFamTipo.setSiacDDocFamTipo(null);

		return siacRIvaRegTipoDocFamTipo;
	}

	public List<SiacTPredocFin> getSiacTPredocs() {
		return this.siacTPredocs;
	}

	public void setSiacTPredocs(List<SiacTPredocFin> siacTPredocs) {
		this.siacTPredocs = siacTPredocs;
	}

	public SiacTPredocFin addSiacTPredoc(SiacTPredocFin siacTPredoc) {
		getSiacTPredocs().add(siacTPredoc);
		siacTPredoc.setSiacDDocFamTipo(this);

		return siacTPredoc;
	}

	public SiacTPredocFin removeSiacTPredoc(SiacTPredocFin siacTPredoc) {
		getSiacTPredocs().remove(siacTPredoc);
		siacTPredoc.setSiacDDocFamTipo(null);

		return siacTPredoc;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.docFamTipoId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.docFamTipoId = uid;
	}

}