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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


// TODO: Auto-generated Javadoc
/**
 * The persistent class for the siac_t_gruppo database table.
 * 
 */
@Entity
@Table(name="siac_t_gruppo")
public class SiacTGruppo extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The gruppo id. */
	@Id
	@SequenceGenerator(name="SIAC_T_GRUPPO_GRUPPOID_GENERATOR", allocationSize=1, sequenceName="SIAC_T_GRUPPO_GRUPPO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_GRUPPO_GRUPPOID_GENERATOR")
	@Column(name="gruppo_id")
	private Integer gruppoId;

	/** The gruppo code. */
	@Column(name="gruppo_code")
	private String gruppoCode;

	/** The gruppo desc. */
	@Column(name="gruppo_desc")
	private String gruppoDesc;

	//bi-directional many-to-one association to SiacRGruppoAccount
	/** The siac r gruppo accounts. */
	@OneToMany(mappedBy="siacTGruppo")
	private List<SiacRGruppoAccount> siacRGruppoAccounts;

	//bi-directional many-to-one association to SiacRGruppoRuoloOp
	/** The siac r gruppo ruolo ops. */
	@OneToMany(mappedBy="siacTGruppo")
	private List<SiacRGruppoRuoloOp> siacRGruppoRuoloOps;

	//bi-directional many-to-one association to SiacRGruppoRuoloOpCassaEcon
	@OneToMany(mappedBy="siacTGruppo")
	private List<SiacRGruppoRuoloOpCassaEcon> siacRGruppoRuoloOpCassaEcons;

	/**
	 * Instantiates a new siac t gruppo.
	 */
	public SiacTGruppo() {
	}

	/**
	 * Gets the gruppo id.
	 *
	 * @return the gruppo id
	 */
	public Integer getGruppoId() {
		return this.gruppoId;
	}

	/**
	 * Sets the gruppo id.
	 *
	 * @param gruppoId the new gruppo id
	 */
	public void setGruppoId(Integer gruppoId) {
		this.gruppoId = gruppoId;
	}



	/**
	 * Gets the gruppo code.
	 *
	 * @return the gruppo code
	 */
	public String getGruppoCode() {
		return this.gruppoCode;
	}

	/**
	 * Sets the gruppo code.
	 *
	 * @param gruppoCode the new gruppo code
	 */
	public void setGruppoCode(String gruppoCode) {
		this.gruppoCode = gruppoCode;
	}

	/**
	 * Gets the gruppo desc.
	 *
	 * @return the gruppo desc
	 */
	public String getGruppoDesc() {
		return this.gruppoDesc;
	}

	/**
	 * Sets the gruppo desc.
	 *
	 * @param gruppoDesc the new gruppo desc
	 */
	public void setGruppoDesc(String gruppoDesc) {
		this.gruppoDesc = gruppoDesc;
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
		siacRGruppoAccount.setSiacTGruppo(this);

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
		siacRGruppoAccount.setSiacTGruppo(null);

		return siacRGruppoAccount;
	}

	/**
	 * Gets the siac r gruppo ruolo ops.
	 *
	 * @return the siac r gruppo ruolo ops
	 */
	public List<SiacRGruppoRuoloOp> getSiacRGruppoRuoloOps() {
		return this.siacRGruppoRuoloOps;
	}

	/**
	 * Sets the siac r gruppo ruolo ops.
	 *
	 * @param siacRGruppoRuoloOps the new siac r gruppo ruolo ops
	 */
	public void setSiacRGruppoRuoloOps(List<SiacRGruppoRuoloOp> siacRGruppoRuoloOps) {
		this.siacRGruppoRuoloOps = siacRGruppoRuoloOps;
	}

	/**
	 * Adds the siac r gruppo ruolo op.
	 *
	 * @param siacRGruppoRuoloOp the siac r gruppo ruolo op
	 * @return the siac r gruppo ruolo op
	 */
	public SiacRGruppoRuoloOp addSiacRGruppoRuoloOp(SiacRGruppoRuoloOp siacRGruppoRuoloOp) {
		getSiacRGruppoRuoloOps().add(siacRGruppoRuoloOp);
		siacRGruppoRuoloOp.setSiacTGruppo(this);

		return siacRGruppoRuoloOp;
	}

	/**
	 * Removes the siac r gruppo ruolo op.
	 *
	 * @param siacRGruppoRuoloOp the siac r gruppo ruolo op
	 * @return the siac r gruppo ruolo op
	 */
	public SiacRGruppoRuoloOp removeSiacRGruppoRuoloOp(SiacRGruppoRuoloOp siacRGruppoRuoloOp) {
		getSiacRGruppoRuoloOps().remove(siacRGruppoRuoloOp);
		siacRGruppoRuoloOp.setSiacTGruppo(null);

		return siacRGruppoRuoloOp;
	}

	public List<SiacRGruppoRuoloOpCassaEcon> getSiacRGruppoRuoloOpCassaEcons() {
		return this.siacRGruppoRuoloOpCassaEcons;
	}

	public void setSiacRGruppoRuoloOpCassaEcons(List<SiacRGruppoRuoloOpCassaEcon> siacRGruppoRuoloOpCassaEcons) {
		this.siacRGruppoRuoloOpCassaEcons = siacRGruppoRuoloOpCassaEcons;
	}

	public SiacRGruppoRuoloOpCassaEcon addSiacRGruppoRuoloOpCassaEcon(SiacRGruppoRuoloOpCassaEcon siacRGruppoRuoloOpCassaEcon) {
		getSiacRGruppoRuoloOpCassaEcons().add(siacRGruppoRuoloOpCassaEcon);
		siacRGruppoRuoloOpCassaEcon.setSiacTGruppo(this);

		return siacRGruppoRuoloOpCassaEcon;
	}

	public SiacRGruppoRuoloOpCassaEcon removeSiacRGruppoRuoloOpCassaEcon(SiacRGruppoRuoloOpCassaEcon siacRGruppoRuoloOpCassaEcon) {
		getSiacRGruppoRuoloOpCassaEcons().remove(siacRGruppoRuoloOpCassaEcon);
		siacRGruppoRuoloOpCassaEcon.setSiacTGruppo(null);

		return siacRGruppoRuoloOpCassaEcon;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return gruppoId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.gruppoId = uid;
		
	}

	

}