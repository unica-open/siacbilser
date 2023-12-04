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


// TODO: Auto-generated Javadoc
/**
 * The persistent class for the siac_r_gruppo_account database table.
 * 
 */
@Entity
@Table(name="siac_r_gruppo_account")
public class SiacRGruppoAccount extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The gruppo account id. */
	@Id
	@SequenceGenerator(name="SIAC_R_GRUPPO_ACCOUNT_GRUPPOACCOUNTID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_GRUPPO_ACCOUNT_GRUPPO_ACCOUNT_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_GRUPPO_ACCOUNT_GRUPPOACCOUNTID_GENERATOR")
	@Column(name="gruppo_account_id")
	private Integer gruppoAccountId;


	//bi-directional many-to-one association to SiacTAccount
	/** The siac t account. */
	@ManyToOne
	@JoinColumn(name="account_id")
	private SiacTAccount siacTAccount;

	//bi-directional many-to-one association to SiacTGruppo
	/** The siac t gruppo. */
	@ManyToOne
	@JoinColumn(name="gruppo_id")
	private SiacTGruppo siacTGruppo;

	/**
	 * Instantiates a new siac r gruppo account.
	 */
	public SiacRGruppoAccount() {
	}

	/**
	 * Gets the gruppo account id.
	 *
	 * @return the gruppo account id
	 */
	public Integer getGruppoAccountId() {
		return this.gruppoAccountId;
	}

	/**
	 * Sets the gruppo account id.
	 *
	 * @param gruppoAccountId the new gruppo account id
	 */
	public void setGruppoAccountId(Integer gruppoAccountId) {
		this.gruppoAccountId = gruppoAccountId;
	}

	/**
	 * Gets the siac t account.
	 *
	 * @return the siac t account
	 */
	public SiacTAccount getSiacTAccount() {
		return this.siacTAccount;
	}

	/**
	 * Sets the siac t account.
	 *
	 * @param siacTAccount the new siac t account
	 */
	public void setSiacTAccount(SiacTAccount siacTAccount) {
		this.siacTAccount = siacTAccount;
	}


	/**
	 * Gets the siac t gruppo.
	 *
	 * @return the siac t gruppo
	 */
	public SiacTGruppo getSiacTGruppo() {
		return this.siacTGruppo;
	}

	/**
	 * Sets the siac t gruppo.
	 *
	 * @param siacTGruppo the new siac t gruppo
	 */
	public void setSiacTGruppo(SiacTGruppo siacTGruppo) {
		this.siacTGruppo = siacTGruppo;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return gruppoAccountId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.gruppoAccountId = uid;
		
	}

}