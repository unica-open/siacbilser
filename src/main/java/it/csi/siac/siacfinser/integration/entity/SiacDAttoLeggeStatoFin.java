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
 * The persistent class for the siac_d_atto_legge_stato database table.
 * 
 */
@Entity
@Table(name="siac_d_atto_legge_stato")
public class SiacDAttoLeggeStatoFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="attolegge_stato_id")
	private Integer attoleggeStatoId;

	@Column(name="attolegge_stato_code")
	private String attoleggeStatoCode;

	@Column(name="attolegge_stato_desc")
	private String attoleggeStatoDesc;

	//bi-directional many-to-one association to SiacRAttoLeggeStatoFin
	@OneToMany(mappedBy="siacDAttoLeggeStato")
	private List<SiacRAttoLeggeStatoFin> siacRAttoLeggeStatos;

	public SiacDAttoLeggeStatoFin() {
	}

	public Integer getAttoleggeStatoId() {
		return this.attoleggeStatoId;
	}

	public void setAttoleggeStatoId(Integer attoleggeStatoId) {
		this.attoleggeStatoId = attoleggeStatoId;
	}

	public String getAttoleggeStatoCode() {
		return this.attoleggeStatoCode;
	}

	public void setAttoleggeStatoCode(String attoleggeStatoCode) {
		this.attoleggeStatoCode = attoleggeStatoCode;
	}

	public String getAttoleggeStatoDesc() {
		return this.attoleggeStatoDesc;
	}

	public void setAttoleggeStatoDesc(String attoleggeStatoDesc) {
		this.attoleggeStatoDesc = attoleggeStatoDesc;
	}

	public List<SiacRAttoLeggeStatoFin> getSiacRAttoLeggeStatos() {
		return this.siacRAttoLeggeStatos;
	}

	public void setSiacRAttoLeggeStatos(List<SiacRAttoLeggeStatoFin> siacRAttoLeggeStatos) {
		this.siacRAttoLeggeStatos = siacRAttoLeggeStatos;
	}

	public SiacRAttoLeggeStatoFin addSiacRAttoLeggeStato(SiacRAttoLeggeStatoFin siacRAttoLeggeStato) {
		getSiacRAttoLeggeStatos().add(siacRAttoLeggeStato);
		siacRAttoLeggeStato.setSiacDAttoLeggeStato(this);

		return siacRAttoLeggeStato;
	}

	public SiacRAttoLeggeStatoFin removeSiacRAttoLeggeStato(SiacRAttoLeggeStatoFin siacRAttoLeggeStato) {
		getSiacRAttoLeggeStatos().remove(siacRAttoLeggeStato);
		siacRAttoLeggeStato.setSiacDAttoLeggeStato(null);

		return siacRAttoLeggeStato;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.attoleggeStatoId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.attoleggeStatoId = uid;
	}
}