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
 * The persistent class for the siac_r_account_ruolo_op database table.
 * 
 */
@Entity
@Table(name="siac_r_account_ruolo_op")
public class SiacRAccountRuoloOp extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The account ruolo op id. */
	@Id// TODO Auto-generated method stub	
	@SequenceGenerator(name="SIAC_R_ACCOUNT_RUOLO_OP_ACCOUNTRUOLOOPID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_ACCOUNT_RUOLO_OP_ACCOUNT_RUOLO_OP_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_ACCOUNT_RUOLO_OP_ACCOUNTRUOLOOPID_GENERATOR")
	@Column(name="account_ruolo_op_id")
	private Integer accountRuoloOpId;

	//bi-directional many-to-one association to SiacDRuoloOp
	/** The siac d ruolo op. */
	@ManyToOne
	@JoinColumn(name="ruolo_operativo_id")
	private SiacDRuoloOp siacDRuoloOp;

	//bi-directional many-to-one association to SiacTAccount
	/** The siac t account. */
	@ManyToOne
	@JoinColumn(name="account_id")
	private SiacTAccount siacTAccount;

	//bi-directional many-to-one association to SiacTClass
	/** The siac t class. */
	@ManyToOne
	@JoinColumn(name="classif_id")
	private SiacTClass siacTClass;

	/**
	 * Instantiates a new siac r account ruolo op.
	 */
	public SiacRAccountRuoloOp() {
	}

	/**
	 * Gets the account ruolo op id.
	 *
	 * @return the account ruolo op id
	 */
	public Integer getAccountRuoloOpId() {
		return this.accountRuoloOpId;
	}

	/**
	 * Sets the account ruolo op id.
	 *
	 * @param accountRuoloOpId the new account ruolo op id
	 */
	public void setAccountRuoloOpId(Integer accountRuoloOpId) {
		this.accountRuoloOpId = accountRuoloOpId;
	}

	/**
	 * Gets the siac d ruolo op.
	 *
	 * @return the siac d ruolo op
	 */
	public SiacDRuoloOp getSiacDRuoloOp() {
		return this.siacDRuoloOp;
	}

	/**
	 * Sets the siac d ruolo op.
	 *
	 * @param siacDRuoloOp the new siac d ruolo op
	 */
	public void setSiacDRuoloOp(SiacDRuoloOp siacDRuoloOp) {
		this.siacDRuoloOp = siacDRuoloOp;
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
	 * Gets the siac t class.
	 *
	 * @return the siac t class
	 */
	public SiacTClass getSiacTClass() {
		return this.siacTClass;
	}

	/**
	 * Sets the siac t class.
	 *
	 * @param siacTClass the new siac t class
	 */
	public void setSiacTClass(SiacTClass siacTClass) {
		this.siacTClass = siacTClass;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return accountRuoloOpId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.accountRuoloOpId = uid;
	}

}