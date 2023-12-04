/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import it.csi.siac.siacfinser.integration.entity.base.SiacTEnteBase;


/**
 * The persistent class for the siac_t_account database table.
 * 
 */
@Entity
@Table(name="siac_t_account")
public class SiacTAccountFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="account_id")
	private Integer accountId;

	@Column(name="account_code")
	private String accountCode;

	private String descrizione;

	private String email;

	private String nome;

	//bi-directional many-to-one association to SiacRGruppoAccountFin
	@OneToMany(mappedBy="siacTAccount")
	private List<SiacRGruppoAccountFin> siacRGruppoAccounts;

	//bi-directional many-to-one association to SiacRSoggettoRuoloFin
	@ManyToOne
	@JoinColumn(name="soggeto_ruolo_id")
	private SiacRSoggettoRuoloFin siacRSoggettoRuolo;

	public SiacTAccountFin() {
	}

	public Integer getAccountId() {
		return this.accountId;
	}

	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}

	public String getAccountCode() {
		return this.accountCode;
	}

	public void setAccountCode(String accountCode) {
		this.accountCode = accountCode;
	}

	public String getDescrizione() {
		return this.descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNome() {
		return this.nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public List<SiacRGruppoAccountFin> getSiacRGruppoAccounts() {
		return this.siacRGruppoAccounts;
	}

	public void setSiacRGruppoAccounts(List<SiacRGruppoAccountFin> siacRGruppoAccounts) {
		this.siacRGruppoAccounts = siacRGruppoAccounts;
	}

	public SiacRGruppoAccountFin addSiacRGruppoAccount(SiacRGruppoAccountFin siacRGruppoAccount) {
		getSiacRGruppoAccounts().add(siacRGruppoAccount);
		siacRGruppoAccount.setSiacTAccount(this);

		return siacRGruppoAccount;
	}

	public SiacRGruppoAccountFin removeSiacRGruppoAccount(SiacRGruppoAccountFin siacRGruppoAccount) {
		getSiacRGruppoAccounts().remove(siacRGruppoAccount);
		siacRGruppoAccount.setSiacTAccount(null);

		return siacRGruppoAccount;
	}

	public SiacRSoggettoRuoloFin getSiacRSoggettoRuolo() {
		return this.siacRSoggettoRuolo;
	}

	public void setSiacRSoggettoRuolo(SiacRSoggettoRuoloFin siacRSoggettoRuolo) {
		this.siacRSoggettoRuolo = siacRSoggettoRuolo;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.accountId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.accountId = uid;
	}
}