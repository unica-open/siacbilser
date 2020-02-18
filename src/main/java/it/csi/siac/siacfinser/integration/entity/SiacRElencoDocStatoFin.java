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
 * The persistent class for the siac_r_elenco_doc_stato database table.
 * 
 */
@Entity
@Table(name="siac_r_elenco_doc_stato")
public class SiacRElencoDocStatoFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="eldoc_r_stato_id")
	private Integer eldocRStatoId;

	//bi-directional many-to-one association to SiacDElencoDocStatoFin
	@ManyToOne
	@JoinColumn(name="eldoc_stato_id")
	private SiacDElencoDocStatoFin siacDElencoDocStato;

	//bi-directional many-to-one association to SiacTElencoDocFin
	@ManyToOne
	@JoinColumn(name="eldoc_id")
	private SiacTElencoDocFin siacTElencoDoc;

	public SiacRElencoDocStatoFin() {
	}

	public Integer getEldocRStatoId() {
		return this.eldocRStatoId;
	}

	public void setEldocRStatoId(Integer eldocRStatoId) {
		this.eldocRStatoId = eldocRStatoId;
	}

	public SiacDElencoDocStatoFin getSiacDElencoDocStato() {
		return this.siacDElencoDocStato;
	}

	public void setSiacDElencoDocStato(SiacDElencoDocStatoFin siacDElencoDocStato) {
		this.siacDElencoDocStato = siacDElencoDocStato;
	}

	public SiacTElencoDocFin getSiacTElencoDoc() {
		return this.siacTElencoDoc;
	}

	public void setSiacTElencoDoc(SiacTElencoDocFin siacTElencoDoc) {
		this.siacTElencoDoc = siacTElencoDoc;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.eldocRStatoId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.eldocRStatoId = uid;
	}


}