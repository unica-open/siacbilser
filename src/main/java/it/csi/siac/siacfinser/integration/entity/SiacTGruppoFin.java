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
 * The persistent class for the siac_t_gruppo database table.
 * 
 */
@Entity
@Table(name="siac_t_gruppo")
public class SiacTGruppoFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="gruppo_id")
	private Integer gruppoId;

	@Column(name="gruppo_code")
	private String gruppoCode;

	@Column(name="gruppo_desc")
	private String gruppoDesc;

	//bi-directional many-to-one association to SiacRGruppoAccountFin
	@OneToMany(mappedBy="siacTGruppo")
	private List<SiacRGruppoAccountFin> siacRGruppoAccounts;

	//bi-directional many-to-one association to SiacRGruppoRuoloOpFin
	@OneToMany(mappedBy="siacTGruppo")
	private List<SiacRGruppoRuoloOpFin> siacRGruppoRuoloOps;

	public SiacTGruppoFin() {
	}

	public Integer getGruppoId() {
		return this.gruppoId;
	}

	public void setGruppoId(Integer gruppoId) {
		this.gruppoId = gruppoId;
	}

	public String getGruppoCode() {
		return this.gruppoCode;
	}

	public void setGruppoCode(String gruppoCode) {
		this.gruppoCode = gruppoCode;
	}

	public String getGruppoDesc() {
		return this.gruppoDesc;
	}

	public void setGruppoDesc(String gruppoDesc) {
		this.gruppoDesc = gruppoDesc;
	}

	public List<SiacRGruppoAccountFin> getSiacRGruppoAccounts() {
		return this.siacRGruppoAccounts;
	}

	public void setSiacRGruppoAccounts(List<SiacRGruppoAccountFin> siacRGruppoAccounts) {
		this.siacRGruppoAccounts = siacRGruppoAccounts;
	}

	public SiacRGruppoAccountFin addSiacRGruppoAccount(SiacRGruppoAccountFin siacRGruppoAccount) {
		getSiacRGruppoAccounts().add(siacRGruppoAccount);
		siacRGruppoAccount.setSiacTGruppo(this);

		return siacRGruppoAccount;
	}

	public SiacRGruppoAccountFin removeSiacRGruppoAccount(SiacRGruppoAccountFin siacRGruppoAccount) {
		getSiacRGruppoAccounts().remove(siacRGruppoAccount);
		siacRGruppoAccount.setSiacTGruppo(null);

		return siacRGruppoAccount;
	}

	public List<SiacRGruppoRuoloOpFin> getSiacRGruppoRuoloOps() {
		return this.siacRGruppoRuoloOps;
	}

	public void setSiacRGruppoRuoloOps(List<SiacRGruppoRuoloOpFin> siacRGruppoRuoloOps) {
		this.siacRGruppoRuoloOps = siacRGruppoRuoloOps;
	}

	public SiacRGruppoRuoloOpFin addSiacRGruppoRuoloOp(SiacRGruppoRuoloOpFin siacRGruppoRuoloOp) {
		getSiacRGruppoRuoloOps().add(siacRGruppoRuoloOp);
		siacRGruppoRuoloOp.setSiacTGruppo(this);

		return siacRGruppoRuoloOp;
	}

	public SiacRGruppoRuoloOpFin removeSiacRGruppoRuoloOp(SiacRGruppoRuoloOpFin siacRGruppoRuoloOp) {
		getSiacRGruppoRuoloOps().remove(siacRGruppoRuoloOp);
		siacRGruppoRuoloOp.setSiacTGruppo(null);

		return siacRGruppoRuoloOp;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.gruppoId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.gruppoId = uid;
	}
}