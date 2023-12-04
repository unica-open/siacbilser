/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


// TODO: Auto-generated Javadoc
/**
 * The persistent class for the siac_t_account database table.
 * 
 */
@Entity
@Table(name="siac_t_account")
public class SiacTAccount extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The account id. */
	@Id
	@SequenceGenerator(name="SIAC_T_ACCOUNT_ACCOUNTID_GENERATOR", allocationSize=1, sequenceName="SIAC_T_ACCOUNT_ACCOUNT_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_ACCOUNT_ACCOUNTID_GENERATOR")
	@Column(name="account_id")
	private Integer accountId;

	/** The account code. */
	@Column(name="account_code")
	private String accountCode;

	/** The descrizione. */
	private String descrizione;

	/** The email. */
	private String email;

	/** The nome. */
	private String nome;

	//bi-directional many-to-one association to SiacRAccountRuoloOp
	/** The siac r account ruolo ops. */
	@OneToMany(mappedBy="siacTAccount")
	private List<SiacRAccountRuoloOp> siacRAccountRuoloOps;

	//bi-directional many-to-one association to SiacRAccountCassaEcon
	@OneToMany(mappedBy="siacTAccount")
	private List<SiacRAccountCassaEcon> siacRAccountCassaEcons;

	//bi-directional many-to-one association to SiacRGruppoAccount
	/** The siac r gruppo accounts. */
	@OneToMany(mappedBy="siacTAccount")
	private List<SiacRGruppoAccount> siacRGruppoAccounts;

	//bi-directional many-to-one association to SiacRSoggettoRuolo
	/** The siac r soggetto ruolo. */
	@ManyToOne
	@JoinColumn(name="soggeto_ruolo_id")
	private SiacRSoggettoRuolo siacRSoggettoRuolo;

	//bi-directional many-to-one association to SiacTAzioneRichiesta
	/** The siac t azione richiestas. */
	@OneToMany(mappedBy="siacTAccount")
	private List<SiacTAzioneRichiesta> siacTAzioneRichiestas;

	//bi-directional many-to-one association to SiacTOperazioneAsincrona
	@OneToMany(mappedBy="siacTAccount")
	private List<SiacTOperazioneAsincrona> siacTOperazioneAsincronas;
	
	//bi-directional many-to-one association to SiacRAccountClass
	@OneToMany(mappedBy="siacTAccount")
	private List<SiacRAccountClass> siacRAccountClasses;

	/**
	 * Instantiates a new siac t account.
	 */
	public SiacTAccount() {
	}

	/**
	 * Gets the account id.
	 *
	 * @return the account id
	 */
	public Integer getAccountId() {
		return this.accountId;
	}

	/**
	 * Sets the account id.
	 *
	 * @param accountId the new account id
	 */
	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}

	/**
	 * Gets the account code.
	 *
	 * @return the account code
	 */
	public String getAccountCode() {
		return this.accountCode;
	}

	/**
	 * Sets the account code.
	 *
	 * @param accountCode the new account code
	 */
	public void setAccountCode(String accountCode) {
		this.accountCode = accountCode;
	}

	/**
	 * Gets the descrizione.
	 *
	 * @return the descrizione
	 */
	public String getDescrizione() {
		return this.descrizione;
	}

	/**
	 * Sets the descrizione.
	 *
	 * @param descrizione the new descrizione
	 */
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	/**
	 * Gets the email.
	 *
	 * @return the email
	 */
	public String getEmail() {
		return this.email;
	}

	/**
	 * Sets the email.
	 *
	 * @param email the new email
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Gets the nome.
	 *
	 * @return the nome
	 */
	public String getNome() {
		return this.nome;
	}

	/**
	 * Sets the nome.
	 *
	 * @param nome the new nome
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}

	/**
	 * Gets the siac r account ruolo ops.
	 *
	 * @return the siac r account ruolo ops
	 */
	public List<SiacRAccountRuoloOp> getSiacRAccountRuoloOps() {
		return this.siacRAccountRuoloOps;
	}

	/**
	 * Sets the siac r account ruolo ops.
	 *
	 * @param siacRAccountRuoloOps the new siac r account ruolo ops
	 */
	public void setSiacRAccountRuoloOps(List<SiacRAccountRuoloOp> siacRAccountRuoloOps) {
		this.siacRAccountRuoloOps = siacRAccountRuoloOps;
	}

	/**
	 * Adds the siac r account ruolo op.
	 *
	 * @param siacRAccountRuoloOp the siac r account ruolo op
	 * @return the siac r account ruolo op
	 */
	public SiacRAccountRuoloOp addSiacRAccountRuoloOp(SiacRAccountRuoloOp siacRAccountRuoloOp) {
		getSiacRAccountRuoloOps().add(siacRAccountRuoloOp);
		siacRAccountRuoloOp.setSiacTAccount(this);

		return siacRAccountRuoloOp;
	}

	/**
	 * Removes the siac r account ruolo op.
	 *
	 * @param siacRAccountRuoloOp the siac r account ruolo op
	 * @return the siac r account ruolo op
	 */
	public SiacRAccountRuoloOp removeSiacRAccountRuoloOp(SiacRAccountRuoloOp siacRAccountRuoloOp) {
		getSiacRAccountRuoloOps().remove(siacRAccountRuoloOp);
		siacRAccountRuoloOp.setSiacTAccount(null);

		return siacRAccountRuoloOp;
	}

	public List<SiacRAccountCassaEcon> getSiacRAccountCassaEcons() {
		return this.siacRAccountCassaEcons;
	}

	public void setSiacRAccountCassaEcons(List<SiacRAccountCassaEcon> siacRAccountCassaEcons) {
		this.siacRAccountCassaEcons = siacRAccountCassaEcons;
	}

	public SiacRAccountCassaEcon addSiacRAccountCassaEcon(SiacRAccountCassaEcon siacRAccountCassaEcon) {
		getSiacRAccountCassaEcons().add(siacRAccountCassaEcon);
		siacRAccountCassaEcon.setSiacTAccount(this);

		return siacRAccountCassaEcon;
	}

	public SiacRAccountCassaEcon removeSiacRAccountCassaEcon(SiacRAccountCassaEcon siacRAccountCassaEcon) {
		getSiacRAccountCassaEcons().remove(siacRAccountCassaEcon);
		siacRAccountCassaEcon.setSiacTAccount(null);

		return siacRAccountCassaEcon;
	}

	/**
	 * Gets the siac r gruppo accounts.
	 *
	 * @return the siac r gruppo accounts
	 */
	public List<SiacRGruppoAccount> getSiacRGruppoAccounts() {
		return this.siacRGruppoAccounts;
	}

	/**
	 * Sets the siac r gruppo accounts.
	 *
	 * @param siacRGruppoAccounts the new siac r gruppo accounts
	 */
	public void setSiacRGruppoAccounts(List<SiacRGruppoAccount> siacRGruppoAccounts) {
		this.siacRGruppoAccounts = siacRGruppoAccounts;
	}

	/**
	 * Adds the siac r gruppo account.
	 *
	 * @param siacRGruppoAccount the siac r gruppo account
	 * @return the siac r gruppo account
	 */
	public SiacRGruppoAccount addSiacRGruppoAccount(SiacRGruppoAccount siacRGruppoAccount) {
		getSiacRGruppoAccounts().add(siacRGruppoAccount);
		siacRGruppoAccount.setSiacTAccount(this);

		return siacRGruppoAccount;
	}

	/**
	 * Removes the siac r gruppo account.
	 *
	 * @param siacRGruppoAccount the siac r gruppo account
	 * @return the siac r gruppo account
	 */
	public SiacRGruppoAccount removeSiacRGruppoAccount(SiacRGruppoAccount siacRGruppoAccount) {
		getSiacRGruppoAccounts().remove(siacRGruppoAccount);
		siacRGruppoAccount.setSiacTAccount(null);

		return siacRGruppoAccount;
	}

	/**
	 * Gets the siac r soggetto ruolo.
	 *
	 * @return the siac r soggetto ruolo
	 */
	public SiacRSoggettoRuolo getSiacRSoggettoRuolo() {
		return this.siacRSoggettoRuolo;
	}

	/**
	 * Sets the siac r soggetto ruolo.
	 *
	 * @param siacRSoggettoRuolo the new siac r soggetto ruolo
	 */
	public void setSiacRSoggettoRuolo(SiacRSoggettoRuolo siacRSoggettoRuolo) {
		this.siacRSoggettoRuolo = siacRSoggettoRuolo;
	}

	/**
	 * Gets the siac t azione richiestas.
	 *
	 * @return the siac t azione richiestas
	 */
	public List<SiacTAzioneRichiesta> getSiacTAzioneRichiestas() {
		return this.siacTAzioneRichiestas;
	}

	/**
	 * Sets the siac t azione richiestas.
	 *
	 * @param siacTAzioneRichiestas the new siac t azione richiestas
	 */
	public void setSiacTAzioneRichiestas(List<SiacTAzioneRichiesta> siacTAzioneRichiestas) {
		this.siacTAzioneRichiestas = siacTAzioneRichiestas;
	}

	/**
	 * Adds the siac t azione richiesta.
	 *
	 * @param siacTAzioneRichiesta the siac t azione richiesta
	 * @return the siac t azione richiesta
	 */
	public SiacTAzioneRichiesta addSiacTAzioneRichiesta(SiacTAzioneRichiesta siacTAzioneRichiesta) {
		getSiacTAzioneRichiestas().add(siacTAzioneRichiesta);
		siacTAzioneRichiesta.setSiacTAccount(this);

		return siacTAzioneRichiesta;
	}

	/**
	 * Removes the siac t azione richiesta.
	 *
	 * @param siacTAzioneRichiesta the siac t azione richiesta
	 * @return the siac t azione richiesta
	 */
	public SiacTAzioneRichiesta removeSiacTAzioneRichiesta(SiacTAzioneRichiesta siacTAzioneRichiesta) {
		getSiacTAzioneRichiestas().remove(siacTAzioneRichiesta);
		siacTAzioneRichiesta.setSiacTAccount(null);

		return siacTAzioneRichiesta;
	}

	public List<SiacTOperazioneAsincrona> getSiacTOperazioneAsincronas() {
		return this.siacTOperazioneAsincronas;
	}

	public void setSiacTOperazioneAsincronas(List<SiacTOperazioneAsincrona> siacTOperazioneAsincronas) {
		this.siacTOperazioneAsincronas = siacTOperazioneAsincronas;
	}

	public SiacTOperazioneAsincrona addSiacTOperazioneAsincrona(SiacTOperazioneAsincrona siacTOperazioneAsincrona) {
		getSiacTOperazioneAsincronas().add(siacTOperazioneAsincrona);
		siacTOperazioneAsincrona.setSiacTAccount(this);

		return siacTOperazioneAsincrona;
	}

	public SiacTOperazioneAsincrona removeSiacTOperazioneAsincrona(SiacTOperazioneAsincrona siacTOperazioneAsincrona) {
		getSiacTOperazioneAsincronas().remove(siacTOperazioneAsincrona);
		siacTOperazioneAsincrona.setSiacTAccount(null);

		return siacTOperazioneAsincrona;
	}
	
	public List<SiacRAccountClass> getSiacRAccountClasses() {
		return this.siacRAccountClasses;
	}

	public void setSiacRAccountClasses(List<SiacRAccountClass> siacRAccountClasses) {
		this.siacRAccountClasses = siacRAccountClasses;
	}

	public SiacRAccountClass addSiacRAccountClass(SiacRAccountClass siacRAccountClass) {
		getSiacRAccountClasses().add(siacRAccountClass);
		siacRAccountClass.setSiacTAccount(this);

		return siacRAccountClass;
	}

	public SiacRAccountClass removeSiacRAccountClass(SiacRAccountClass siacRAccountClass) {
		getSiacRAccountClasses().remove(siacRAccountClass);
		siacRAccountClass.setSiacTAccount(null);

		return siacRAccountClass;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return accountId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.accountId = uid;
	}

}