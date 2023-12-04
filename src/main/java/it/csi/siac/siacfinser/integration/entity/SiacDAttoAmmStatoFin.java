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
 * The persistent class for the siac_d_atto_amm_stato database table.
 * 
 */
@Entity
@Table(name="siac_d_atto_amm_stato")
public class SiacDAttoAmmStatoFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="attoamm_stato_id")
	private Integer attoammStatoId;

	@Column(name="attoamm_stato_code")
	private String attoammStatoCode;

	@Column(name="attoamm_stato_desc")
	private String attoammStatoDesc;

	//bi-directional many-to-one association to SiacRAttoAmmStatoFin
	@OneToMany(mappedBy="siacDAttoAmmStato")
	private List<SiacRAttoAmmStatoFin> siacRAttoAmmStatos;

	public SiacDAttoAmmStatoFin() {
	}

	public Integer getAttoammStatoId() {
		return this.attoammStatoId;
	}

	public void setAttoammStatoId(Integer attoammStatoId) {
		this.attoammStatoId = attoammStatoId;
	}

	public String getAttoammStatoCode() {
		return this.attoammStatoCode;
	}

	public void setAttoammStatoCode(String attoammStatoCode) {
		this.attoammStatoCode = attoammStatoCode;
	}

	public String getAttoammStatoDesc() {
		return this.attoammStatoDesc;
	}

	public void setAttoammStatoDesc(String attoammStatoDesc) {
		this.attoammStatoDesc = attoammStatoDesc;
	}

	public List<SiacRAttoAmmStatoFin> getSiacRAttoAmmStatos() {
		return this.siacRAttoAmmStatos;
	}

	public void setSiacRAttoAmmStatos(List<SiacRAttoAmmStatoFin> siacRAttoAmmStatos) {
		this.siacRAttoAmmStatos = siacRAttoAmmStatos;
	}

	public SiacRAttoAmmStatoFin addSiacRAttoAmmStato(SiacRAttoAmmStatoFin siacRAttoAmmStato) {
		getSiacRAttoAmmStatos().add(siacRAttoAmmStato);
		siacRAttoAmmStato.setSiacDAttoAmmStato(this);

		return siacRAttoAmmStato;
	}

	public SiacRAttoAmmStatoFin removeSiacRAttoAmmStato(SiacRAttoAmmStatoFin siacRAttoAmmStato) {
		getSiacRAttoAmmStatos().remove(siacRAttoAmmStato);
		siacRAttoAmmStato.setSiacDAttoAmmStato(null);

		return siacRAttoAmmStato;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.attoammStatoId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.attoammStatoId = uid;
	}
}