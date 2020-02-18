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
 * The persistent class for the siac_r_account_ruolo_op database table.
 * 
 */
@Entity
@Table(name="siac_r_account_ruolo_op")
public class SiacRAccountRuoloOpFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="account_ruolo_op_id")
	private Integer accountRuoloOpId;

	@Column(name="classif_id")
	private Integer classifId;
	
	//bi-directional many-to-one association to SiacDRuoloFin
	@ManyToOne
	@JoinColumn(name="ruolo_operativo_id")
	private SiacDRuoloOpFin siacDRuoloOp;

	//bi-directional many-to-one association to SiacTAccountFin
	@ManyToOne
	@JoinColumn(name="account_id")
	private SiacTAccountFin siacTAccount;

	public SiacRAccountRuoloOpFin() {
	}

	public Integer getAccountRuoloOpId() {
		return this.accountRuoloOpId;
	}

	public void setAccountRuoloOpId(Integer accountRuoloOpId) {
		this.accountRuoloOpId = accountRuoloOpId;
	}

	public Integer getClassifId() {
		return this.classifId;
	}

	public void setClassifId(Integer classifId) {
		this.classifId = classifId;
	}

	public SiacTAccountFin getSiacTAccount() {
		return this.siacTAccount;
	}

	public void setSiacTAccount(SiacTAccountFin siacTAccount) {
		this.siacTAccount = siacTAccount;
	}

	@Override
	public Integer getUid() {
		return this.accountRuoloOpId;
	}

	@Override
	public void setUid(Integer uid) {
		this.accountRuoloOpId = uid;
	}

	public SiacDRuoloOpFin getSiacDRuoloOp() {
		return siacDRuoloOp;
	}

	public void setSiacDRuoloOp(SiacDRuoloOpFin siacDRuolo) {
		this.siacDRuoloOp = siacDRuolo;
	}

}