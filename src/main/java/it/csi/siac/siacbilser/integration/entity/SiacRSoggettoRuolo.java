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
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


// TODO: Auto-generated Javadoc
/**
 * The persistent class for the siac_r_soggetto_ruolo database table.
 * 
 */
@Entity
@Table(name="siac_r_soggetto_ruolo")
@NamedQuery(name="SiacRSoggettoRuolo.findAll", query="SELECT s FROM SiacRSoggettoRuolo s")
public class SiacRSoggettoRuolo extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The soggeto ruolo id. */
	@Id
	@SequenceGenerator(name="SIAC_R_SOGGETTO_RUOLO_SOGGETORUOLOID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_SOGGETTO_RUOLO_SOGGETO_RUOLO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_SOGGETTO_RUOLO_SOGGETORUOLOID_GENERATOR")
	@Column(name="soggeto_ruolo_id")
	private Integer soggetoRuoloId;

	//bi-directional many-to-one association to SiacDRuolo
	/** The siac d ruolo. */
	@ManyToOne
	@JoinColumn(name="ruolo_id")
	private SiacDRuolo siacDRuolo;

	//bi-directional many-to-one association to SiacTSoggetto
	/** The siac t soggetto. */
	@ManyToOne
	@JoinColumn(name="soggetto_id")
	private SiacTSoggetto siacTSoggetto;

	//bi-directional many-to-one association to SiacTAccount
	/** The siac t accounts. */
	@OneToMany(mappedBy="siacRSoggettoRuolo")
	private List<SiacTAccount> siacTAccounts;

	/**
	 * Instantiates a new siac r soggetto ruolo.
	 */
	public SiacRSoggettoRuolo() {
	}

	/**
	 * Gets the soggeto ruolo id.
	 *
	 * @return the soggeto ruolo id
	 */
	public Integer getSoggetoRuoloId() {
		return this.soggetoRuoloId;
	}

	/**
	 * Sets the soggeto ruolo id.
	 *
	 * @param soggetoRuoloId the new soggeto ruolo id
	 */
	public void setSoggetoRuoloId(Integer soggetoRuoloId) {
		this.soggetoRuoloId = soggetoRuoloId;
	}

	

	/**
	 * Gets the siac d ruolo.
	 *
	 * @return the siac d ruolo
	 */
	public SiacDRuolo getSiacDRuolo() {
		return this.siacDRuolo;
	}

	/**
	 * Sets the siac d ruolo.
	 *
	 * @param siacDRuolo the new siac d ruolo
	 */
	public void setSiacDRuolo(SiacDRuolo siacDRuolo) {
		this.siacDRuolo = siacDRuolo;
	}

	

	/**
	 * Gets the siac t soggetto.
	 *
	 * @return the siac t soggetto
	 */
	public SiacTSoggetto getSiacTSoggetto() {
		return this.siacTSoggetto;
	}

	/**
	 * Sets the siac t soggetto.
	 *
	 * @param siacTSoggetto the new siac t soggetto
	 */
	public void setSiacTSoggetto(SiacTSoggetto siacTSoggetto) {
		this.siacTSoggetto = siacTSoggetto;
	}

	/**
	 * Gets the siac t accounts.
	 *
	 * @return the siac t accounts
	 */
	public List<SiacTAccount> getSiacTAccounts() {
		return this.siacTAccounts;
	}

	/**
	 * Sets the siac t accounts.
	 *
	 * @param siacTAccounts the new siac t accounts
	 */
	public void setSiacTAccounts(List<SiacTAccount> siacTAccounts) {
		this.siacTAccounts = siacTAccounts;
	}

	/**
	 * Adds the siac t account.
	 *
	 * @param siacTAccount the siac t account
	 * @return the siac t account
	 */
	public SiacTAccount addSiacTAccount(SiacTAccount siacTAccount) {
		getSiacTAccounts().add(siacTAccount);
		siacTAccount.setSiacRSoggettoRuolo(this);

		return siacTAccount;
	}

	/**
	 * Removes the siac t account.
	 *
	 * @param siacTAccount the siac t account
	 * @return the siac t account
	 */
	public SiacTAccount removeSiacTAccount(SiacTAccount siacTAccount) {
		getSiacTAccounts().remove(siacTAccount);
		siacTAccount.setSiacRSoggettoRuolo(null);

		return siacTAccount;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return soggetoRuoloId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.soggetoRuoloId = uid;
	}

}