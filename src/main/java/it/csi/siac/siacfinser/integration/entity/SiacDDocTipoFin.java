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
 * The persistent class for the siac_d_doc_tipo database table.
 * 
 */
@Entity
@Table(name="siac_d_doc_tipo")
public class SiacDDocTipoFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="doc_tipo_id")
	private Integer docTipoId;

	@Column(name="doc_tipo_code")
	private String docTipoCode;

	@Column(name="doc_tipo_desc")
	private String docTipoDesc;

	//bi-directional many-to-one association to SiacDDocFamTipoFin
	@ManyToOne
	@JoinColumn(name="doc_fam_tipo_id")
	private SiacDDocFamTipoFin siacDDocFamTipo;

	//bi-directional many-to-one association to SiacDDocGruppoFin
	@ManyToOne
	@JoinColumn(name="doc_gruppo_tipo_id")
	private SiacDDocGruppoFin siacDDocGruppo;

	//bi-directional many-to-one association to SiacRDocTipoAttrFin
	@OneToMany(mappedBy="siacDDocTipo")
	private List<SiacRDocTipoAttrFin> siacRDocTipoAttrs;

	//bi-directional many-to-one association to SiacTDocFin
	@OneToMany(mappedBy="siacDDocTipo")
	private List<SiacTDocFin> siacTDocs;

	//bi-directional many-to-one association to SiacTDocIvaFin
	@OneToMany(mappedBy="siacDDocTipo")
	private List<SiacTDocIvaFin> siacTDocIvas;

	public SiacDDocTipoFin() {
	}

	public Integer getDocTipoId() {
		return this.docTipoId;
	}

	public void setDocTipoId(Integer docTipoId) {
		this.docTipoId = docTipoId;
	}

	public String getDocTipoCode() {
		return this.docTipoCode;
	}

	public void setDocTipoCode(String docTipoCode) {
		this.docTipoCode = docTipoCode;
	}

	public String getDocTipoDesc() {
		return this.docTipoDesc;
	}

	public void setDocTipoDesc(String docTipoDesc) {
		this.docTipoDesc = docTipoDesc;
	}

	public SiacDDocFamTipoFin getSiacDDocFamTipo() {
		return this.siacDDocFamTipo;
	}

	public void setSiacDDocFamTipo(SiacDDocFamTipoFin siacDDocFamTipo) {
		this.siacDDocFamTipo = siacDDocFamTipo;
	}

	public SiacDDocGruppoFin getSiacDDocGruppo() {
		return this.siacDDocGruppo;
	}

	public void setSiacDDocGruppo(SiacDDocGruppoFin siacDDocGruppo) {
		this.siacDDocGruppo = siacDDocGruppo;
	}

	public List<SiacRDocTipoAttrFin> getSiacRDocTipoAttrs() {
		return this.siacRDocTipoAttrs;
	}

	public void setSiacRDocTipoAttrs(List<SiacRDocTipoAttrFin> siacRDocTipoAttrs) {
		this.siacRDocTipoAttrs = siacRDocTipoAttrs;
	}

	public SiacRDocTipoAttrFin addSiacRDocTipoAttr(SiacRDocTipoAttrFin siacRDocTipoAttr) {
		getSiacRDocTipoAttrs().add(siacRDocTipoAttr);
		siacRDocTipoAttr.setSiacDDocTipo(this);

		return siacRDocTipoAttr;
	}

	public SiacRDocTipoAttrFin removeSiacRDocTipoAttr(SiacRDocTipoAttrFin siacRDocTipoAttr) {
		getSiacRDocTipoAttrs().remove(siacRDocTipoAttr);
		siacRDocTipoAttr.setSiacDDocTipo(null);

		return siacRDocTipoAttr;
	}

	public List<SiacTDocFin> getSiacTDocs() {
		return this.siacTDocs;
	}

	public void setSiacTDocs(List<SiacTDocFin> siacTDocs) {
		this.siacTDocs = siacTDocs;
	}

	public SiacTDocFin addSiacTDoc(SiacTDocFin siacTDoc) {
		getSiacTDocs().add(siacTDoc);
		siacTDoc.setSiacDDocTipo(this);

		return siacTDoc;
	}

	public SiacTDocFin removeSiacTDoc(SiacTDocFin siacTDoc) {
		getSiacTDocs().remove(siacTDoc);
		siacTDoc.setSiacDDocTipo(null);

		return siacTDoc;
	}

	public List<SiacTDocIvaFin> getSiacTDocIvas() {
		return this.siacTDocIvas;
	}

	public void setSiacTDocIvas(List<SiacTDocIvaFin> siacTDocIvas) {
		this.siacTDocIvas = siacTDocIvas;
	}

	public SiacTDocIvaFin addSiacTDocIva(SiacTDocIvaFin siacTDocIva) {
		getSiacTDocIvas().add(siacTDocIva);
		siacTDocIva.setSiacDDocTipo(this);

		return siacTDocIva;
	}

	public SiacTDocIvaFin removeSiacTDocIva(SiacTDocIvaFin siacTDocIva) {
		getSiacTDocIvas().remove(siacTDocIva);
		siacTDocIva.setSiacDDocTipo(null);

		return siacTDocIva;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.docTipoId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.docTipoId = uid;
	}

}