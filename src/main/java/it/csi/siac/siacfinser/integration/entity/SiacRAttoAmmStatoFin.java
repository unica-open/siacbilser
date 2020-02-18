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
 * The persistent class for the siac_r_atto_amm_stato database table.
 * 
 */
@Entity
@Table(name="siac_r_atto_amm_stato")
public class SiacRAttoAmmStatoFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="att_attoamm_stato_id")
	private Integer attAttoammStatoId;

	//bi-directional many-to-one association to SiacDAttoAmmStatoFin
	@ManyToOne
	@JoinColumn(name="attoamm_stato_id")
	private SiacDAttoAmmStatoFin siacDAttoAmmStato;

	//bi-directional many-to-one association to SiacTAttoAmmFin
	@ManyToOne
	@JoinColumn(name="attoamm_id")
	private SiacTAttoAmmFin siacTAttoAmm;

	public SiacRAttoAmmStatoFin() {
	}

	public Integer getAttAttoammStatoId() {
		return this.attAttoammStatoId;
	}

	public void setAttAttoammStatoId(Integer attAttoammStatoId) {
		this.attAttoammStatoId = attAttoammStatoId;
	}

	public SiacDAttoAmmStatoFin getSiacDAttoAmmStato() {
		return this.siacDAttoAmmStato;
	}

	public void setSiacDAttoAmmStato(SiacDAttoAmmStatoFin siacDAttoAmmStato) {
		this.siacDAttoAmmStato = siacDAttoAmmStato;
	}

	public SiacTAttoAmmFin getSiacTAttoAmm() {
		return this.siacTAttoAmm;
	}

	public void setSiacTAttoAmm(SiacTAttoAmmFin siacTAttoAmm) {
		this.siacTAttoAmm = siacTAttoAmm;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.attAttoammStatoId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.attAttoammStatoId = uid;
	}
}