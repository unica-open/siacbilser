/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**
 * The persistent class for the siac_r_account_ruolo_op_cassa_econ database table.
 * 
 */
@Entity
@Table(name="siac_r_account_cassa_econ")
public class SiacRAccountCassaEcon extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_ACCOUNT_CASSA_ECON_ACCOUNTRUOLOOPCEID_GENERATOR", allocationSize = 1, sequenceName="siac_r_account_cassa_econ_account_cassaecon_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_ACCOUNT_CASSA_ECON_ACCOUNTRUOLOOPCEID_GENERATOR")
	@Column(name="account_cassaecon_id")
	private Integer accountCassaeconId;

	//bi-directional many-to-one association to SiacTAccount
	@ManyToOne
	@JoinColumn(name="account_id")
	private SiacTAccount siacTAccount;

	//bi-directional many-to-one association to SiacTCassaEcon
	@ManyToOne
	@JoinColumn(name="cassaecon_id")
	private SiacTCassaEcon siacTCassaEcon;

	public SiacRAccountCassaEcon() {
	}

	public Integer getAccountCassaeconId() {
		return this.accountCassaeconId;
	}

	public void setAccountCassaeconId(Integer accountRuoloOpCeId) {
		this.accountCassaeconId = accountRuoloOpCeId;
	}
	
	public SiacTAccount getSiacTAccount() {
		return this.siacTAccount;
	}

	public void setSiacTAccount(SiacTAccount siacTAccount) {
		this.siacTAccount = siacTAccount;
	}

	public SiacTCassaEcon getSiacTCassaEcon() {
		return this.siacTCassaEcon;
	}

	public void setSiacTCassaEcon(SiacTCassaEcon siacTCassaEcon) {
		this.siacTCassaEcon = siacTCassaEcon;
	}

	@Override
	public Integer getUid() {
		return accountCassaeconId;
	}

	@Override
	public void setUid(Integer uid) {
		accountCassaeconId = uid;
	}

}