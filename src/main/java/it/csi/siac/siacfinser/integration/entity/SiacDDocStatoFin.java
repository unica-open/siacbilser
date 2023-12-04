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
 * The persistent class for the siac_d_doc_stato database table.
 * 
 */
@Entity
@Table(name="siac_d_doc_stato")
public class SiacDDocStatoFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="doc_stato_id")
	private Integer docStatoId;

	@Column(name="doc_stato_code")
	private String docStatoCode;

	@Column(name="doc_stato_desc")
	private String docStatoDesc;

	//bi-directional many-to-one association to SiacRDocStatoFin
	@OneToMany(mappedBy="siacDDocStato")
	private List<SiacRDocStatoFin> siacRDocStatos;

	public SiacDDocStatoFin() {
	}

	public Integer getDocStatoId() {
		return this.docStatoId;
	}

	public void setDocStatoId(Integer docStatoId) {
		this.docStatoId = docStatoId;
	}

	public String getDocStatoCode() {
		return this.docStatoCode;
	}

	public void setDocStatoCode(String docStatoCode) {
		this.docStatoCode = docStatoCode;
	}

	public String getDocStatoDesc() {
		return this.docStatoDesc;
	}

	public void setDocStatoDesc(String docStatoDesc) {
		this.docStatoDesc = docStatoDesc;
	}

	public List<SiacRDocStatoFin> getSiacRDocStatos() {
		return this.siacRDocStatos;
	}

	public void setSiacRDocStatos(List<SiacRDocStatoFin> siacRDocStatos) {
		this.siacRDocStatos = siacRDocStatos;
	}

	public SiacRDocStatoFin addSiacRDocStato(SiacRDocStatoFin siacRDocStato) {
		getSiacRDocStatos().add(siacRDocStato);
		siacRDocStato.setSiacDDocStato(this);

		return siacRDocStato;
	}

	public SiacRDocStatoFin removeSiacRDocStato(SiacRDocStatoFin siacRDocStato) {
		getSiacRDocStatos().remove(siacRDocStato);
		siacRDocStato.setSiacDDocStato(null);

		return siacRDocStato;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.docStatoId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.docStatoId = uid;
	}
}