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
 * The persistent class for the siac_r_atto_legge_stato database table.
 * 
 */
@Entity
@Table(name="siac_r_atto_legge_stato")
public class SiacRAttoLeggeStatoFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="att_attolegge_stato_id")
	private Integer attAttoleggeStatoId;

	//bi-directional many-to-one association to SiacDAttoLeggeStatoFin
	@ManyToOne
	@JoinColumn(name="attolegge_stato_id")
	private SiacDAttoLeggeStatoFin siacDAttoLeggeStato;

	//bi-directional many-to-one association to SiacTAttoLeggeFin
	@ManyToOne
	@JoinColumn(name="attolegge_id")
	private SiacTAttoLeggeFin siacTAttoLegge;

	public SiacRAttoLeggeStatoFin() {
	}

	public Integer getAttAttoleggeStatoId() {
		return this.attAttoleggeStatoId;
	}

	public void setAttAttoleggeStatoId(Integer attAttoleggeStatoId) {
		this.attAttoleggeStatoId = attAttoleggeStatoId;
	}

	public SiacDAttoLeggeStatoFin getSiacDAttoLeggeStato() {
		return this.siacDAttoLeggeStato;
	}

	public void setSiacDAttoLeggeStato(SiacDAttoLeggeStatoFin siacDAttoLeggeStato) {
		this.siacDAttoLeggeStato = siacDAttoLeggeStato;
	}

	public SiacTAttoLeggeFin getSiacTAttoLegge() {
		return this.siacTAttoLegge;
	}

	public void setSiacTAttoLegge(SiacTAttoLeggeFin siacTAttoLegge) {
		this.siacTAttoLegge = siacTAttoLegge;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.attAttoleggeStatoId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.attAttoleggeStatoId = uid;
	}
}