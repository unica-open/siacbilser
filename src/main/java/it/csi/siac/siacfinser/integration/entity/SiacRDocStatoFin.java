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
 * The persistent class for the siac_r_doc_stato database table.
 * 
 */
@Entity
@Table(name="siac_r_doc_stato")
public class SiacRDocStatoFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="doc_stato_r_id")
	private Integer docStatoRId;

	//bi-directional many-to-one association to SiacDDocStatoFin
	@ManyToOne
	@JoinColumn(name="doc_stato_id")
	private SiacDDocStatoFin siacDDocStato;

	//bi-directional many-to-one association to SiacTDocFin
	@ManyToOne
	@JoinColumn(name="doc_id")
	private SiacTDocFin siacTDoc;

	public SiacRDocStatoFin() {
	}

	public Integer getDocStatoRId() {
		return this.docStatoRId;
	}

	public void setDocStatoRId(Integer docStatoRId) {
		this.docStatoRId = docStatoRId;
	}

	public SiacDDocStatoFin getSiacDDocStato() {
		return this.siacDDocStato;
	}

	public void setSiacDDocStato(SiacDDocStatoFin siacDDocStato) {
		this.siacDDocStato = siacDDocStato;
	}

	public SiacTDocFin getSiacTDoc() {
		return this.siacTDoc;
	}

	public void setSiacTDoc(SiacTDocFin siacTDoc) {
		this.siacTDoc = siacTDoc;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.docStatoRId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.docStatoRId = uid;
	}
}