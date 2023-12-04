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
 * The persistent class for the siac_r_atto_allegato_stato database table.
 * 
 */
@Entity
@Table(name="siac_r_atto_allegato_stato")
public class SiacRAttoAllegatoStatoFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="attoal_r_stato_id")
	private Integer attoalRStatoId;

	//bi-directional many-to-one association to SiacDAttoAllegatoStatoFin
	@ManyToOne
	@JoinColumn(name="attoal_stato_id")
	private SiacDAttoAllegatoStatoFin siacDAttoAllegatoStato;

//	//bi-directional many-to-one association to SiacRAttoAllegatoFin
//	@ManyToOne
//	@JoinColumn(name="attoal_id")
//	private SiacRAttoAllegatoFin siacRAttoAllegato;

	public SiacRAttoAllegatoStatoFin() {
	}

	public Integer getAttoalRStatoId() {
		return this.attoalRStatoId;
	}

	public void setAttoalRStatoId(Integer attoalRStatoId) {
		this.attoalRStatoId = attoalRStatoId;
	}

	public SiacDAttoAllegatoStatoFin getSiacDAttoAllegatoStato() {
		return this.siacDAttoAllegatoStato;
	}

	public void setSiacDAttoAllegatoStato(SiacDAttoAllegatoStatoFin siacDAttoAllegatoStato) {
		this.siacDAttoAllegatoStato = siacDAttoAllegatoStato;
	}

//	public SiacRAttoAllegatoFin getSiacRAttoAllegato() {
//		return this.siacRAttoAllegato;
//	}
//
//	public void setSiacRAttoAllegato(SiacRAttoAllegatoFin siacRAttoAllegato) {
//		this.siacRAttoAllegato = siacRAttoAllegato;
//	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.attoalRStatoId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.attoalRStatoId = uid;
	}

}