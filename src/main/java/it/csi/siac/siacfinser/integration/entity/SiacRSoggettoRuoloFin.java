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
 * The persistent class for the siac_r_soggetto_ruolo database table.
 * 
 */
@Entity
@Table(name="siac_r_soggetto_ruolo")
public class SiacRSoggettoRuoloFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="soggeto_ruolo_id")
	private Integer soggetoRuoloId;

	//bi-directional many-to-one association to SiacDRuoloFin
	@ManyToOne
	@JoinColumn(name="ruolo_id")
	private SiacDRuoloFin siacDRuolo;

	//bi-directional many-to-one association to SiacTSoggettoFin
	@ManyToOne
	@JoinColumn(name="soggetto_id")
	private SiacTSoggettoFin siacTSoggetto;

	//bi-directional many-to-one association to SiacTAccountFin
	@OneToMany(mappedBy="siacRSoggettoRuolo")
	private List<SiacTAccountFin> siacTAccounts;

	public SiacRSoggettoRuoloFin() {
	}

	public Integer getSoggetoRuoloId() {
		return this.soggetoRuoloId;
	}

	public void setSoggetoRuoloId(Integer soggetoRuoloId) {
		this.soggetoRuoloId = soggetoRuoloId;
	}

	public SiacDRuoloFin getSiacDRuolo() {
		return this.siacDRuolo;
	}

	public void setSiacDRuolo(SiacDRuoloFin siacDRuolo) {
		this.siacDRuolo = siacDRuolo;
	}

	public SiacTSoggettoFin getSiacTSoggetto() {
		return this.siacTSoggetto;
	}

	public void setSiacTSoggetto(SiacTSoggettoFin siacTSoggetto) {
		this.siacTSoggetto = siacTSoggetto;
	}

	public List<SiacTAccountFin> getSiacTAccounts() {
		return this.siacTAccounts;
	}

	public void setSiacTAccounts(List<SiacTAccountFin> siacTAccounts) {
		this.siacTAccounts = siacTAccounts;
	}

	public SiacTAccountFin addSiacTAccount(SiacTAccountFin siacTAccount) {
		getSiacTAccounts().add(siacTAccount);
		siacTAccount.setSiacRSoggettoRuolo(this);

		return siacTAccount;
	}

	public SiacTAccountFin removeSiacTAccount(SiacTAccountFin siacTAccount) {
		getSiacTAccounts().remove(siacTAccount);
		siacTAccount.setSiacRSoggettoRuolo(null);

		return siacTAccount;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.soggetoRuoloId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.soggetoRuoloId = uid;
	}
}