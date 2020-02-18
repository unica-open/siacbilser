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
 * The persistent class for the siac_r_gruppo_account database table.
 * 
 */
@Entity
@Table(name="siac_r_gruppo_account")
public class SiacRGruppoAccountFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="gruppo_account_id")
	private Integer gruppoAccountId;

	//bi-directional many-to-one association to SiacTAccountFin
	@ManyToOne
	@JoinColumn(name="account_id")
	private SiacTAccountFin siacTAccount;

	//bi-directional many-to-one association to SiacTGruppoFin
	@ManyToOne
	@JoinColumn(name="gruppo_id")
	private SiacTGruppoFin siacTGruppo;

	public SiacRGruppoAccountFin() {
	}

	public Integer getGruppoAccountId() {
		return this.gruppoAccountId;
	}

	public void setGruppoAccountId(Integer gruppoAccountId) {
		this.gruppoAccountId = gruppoAccountId;
	}

	public SiacTAccountFin getSiacTAccount() {
		return this.siacTAccount;
	}

	public void setSiacTAccount(SiacTAccountFin siacTAccount) {
		this.siacTAccount = siacTAccount;
	}

	public SiacTGruppoFin getSiacTGruppo() {
		return this.siacTGruppo;
	}

	public void setSiacTGruppo(SiacTGruppoFin siacTGruppo) {
		this.siacTGruppo = siacTGruppo;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.gruppoAccountId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.gruppoAccountId = uid;
	}
}