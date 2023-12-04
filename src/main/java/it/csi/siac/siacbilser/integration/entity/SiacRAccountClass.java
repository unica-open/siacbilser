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
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**
 * The persistent class for the siac_r_account_class database table.
 * 
 */
@Entity
@Table(name="siac_r_account_class")
@NamedQuery(name="SiacRAccountClass.findAll", query="SELECT s FROM SiacRAccountClass s")
public class SiacRAccountClass extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_ACCOUNT_CLASS_ACCOUNTCLASSID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_ACCOUNT_CLASS_ACCOUNT_CLASS_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_ACCOUNT_CLASS_ACCOUNTCLASSID_GENERATOR")
	@Column(name="account_class_id")
	private Integer accountClassId;

	//bi-directional many-to-one association to SiacTAccount
	@ManyToOne
	@JoinColumn(name="account_id")
	private SiacTAccount siacTAccount;

	//bi-directional many-to-one association to SiacTClass
	@ManyToOne
	@JoinColumn(name="classif_id")
	private SiacTClass siacTClass;

	public SiacRAccountClass() {
	}

	public Integer getAccountClassId() {
		return this.accountClassId;
	}

	public void setAccountClassId(Integer accountClassId) {
		this.accountClassId = accountClassId;
	}

	public SiacTAccount getSiacTAccount() {
		return this.siacTAccount;
	}

	public void setSiacTAccount(SiacTAccount siacTAccount) {
		this.siacTAccount = siacTAccount;
	}

	public SiacTClass getSiacTClass() {
		return this.siacTClass;
	}

	public void setSiacTClass(SiacTClass siacTClass) {
		this.siacTClass = siacTClass;
	}

	@Override
	public Integer getUid() {
		return this.accountClassId;
	}

	@Override
	public void setUid(Integer uid) {
		this.accountClassId = uid;
	}

}