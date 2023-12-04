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
 * The persistent class for the siac_d_subdoc_tipo database table.
 * 
 */
@Entity
@Table(name="siac_d_subdoc_tipo")
public class SiacDSubdocTipoFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="subdoc_tipo_id")
	private Integer subdocTipoId;

	@Column(name="subdoc_tipo_code")
	private String subdocTipoCode;

	@Column(name="subdoc_tipo_desc")
	private String subdocTipoDesc;

	//bi-directional many-to-one association to SiacRSubdocTipoAttrFin
	@OneToMany(mappedBy="siacDSubdocTipo")
	private List<SiacRSubdocTipoAttrFin> siacRSubdocTipoAttrs;

	//bi-directional many-to-one association to SiacTSubdocFin
	@OneToMany(mappedBy="siacDSubdocTipo")
	private List<SiacTSubdocFin> siacTSubdocs;

	public SiacDSubdocTipoFin() {
	}

	public Integer getSubdocTipoId() {
		return this.subdocTipoId;
	}

	public void setSubdocTipoId(Integer subdocTipoId) {
		this.subdocTipoId = subdocTipoId;
	}

	public String getSubdocTipoCode() {
		return this.subdocTipoCode;
	}

	public void setSubdocTipoCode(String subdocTipoCode) {
		this.subdocTipoCode = subdocTipoCode;
	}

	public String getSubdocTipoDesc() {
		return this.subdocTipoDesc;
	}

	public void setSubdocTipoDesc(String subdocTipoDesc) {
		this.subdocTipoDesc = subdocTipoDesc;
	}

	public List<SiacRSubdocTipoAttrFin> getSiacRSubdocTipoAttrs() {
		return this.siacRSubdocTipoAttrs;
	}

	public void setSiacRSubdocTipoAttrs(List<SiacRSubdocTipoAttrFin> siacRSubdocTipoAttrs) {
		this.siacRSubdocTipoAttrs = siacRSubdocTipoAttrs;
	}

	public SiacRSubdocTipoAttrFin addSiacRSubdocTipoAttr(SiacRSubdocTipoAttrFin siacRSubdocTipoAttr) {
		getSiacRSubdocTipoAttrs().add(siacRSubdocTipoAttr);
		siacRSubdocTipoAttr.setSiacDSubdocTipo(this);

		return siacRSubdocTipoAttr;
	}

	public SiacRSubdocTipoAttrFin removeSiacRSubdocTipoAttr(SiacRSubdocTipoAttrFin siacRSubdocTipoAttr) {
		getSiacRSubdocTipoAttrs().remove(siacRSubdocTipoAttr);
		siacRSubdocTipoAttr.setSiacDSubdocTipo(null);

		return siacRSubdocTipoAttr;
	}

	public List<SiacTSubdocFin> getSiacTSubdocs() {
		return this.siacTSubdocs;
	}

	public void setSiacTSubdocs(List<SiacTSubdocFin> siacTSubdocs) {
		this.siacTSubdocs = siacTSubdocs;
	}

	public SiacTSubdocFin addSiacTSubdoc(SiacTSubdocFin siacTSubdoc) {
		getSiacTSubdocs().add(siacTSubdoc);
		siacTSubdoc.setSiacDSubdocTipo(this);

		return siacTSubdoc;
	}

	public SiacTSubdocFin removeSiacTSubdoc(SiacTSubdocFin siacTSubdoc) {
		getSiacTSubdocs().remove(siacTSubdoc);
		siacTSubdoc.setSiacDSubdocTipo(null);

		return siacTSubdoc;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.subdocTipoId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.subdocTipoId = uid;
	}

}