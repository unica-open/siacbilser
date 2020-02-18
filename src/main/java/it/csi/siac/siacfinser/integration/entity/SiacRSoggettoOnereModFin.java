/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import it.csi.siac.siacfinser.integration.entity.base.SiacTEnteBase;




/**
 * The persistent class for the siac_r_soggetto_onere_mod database table.
 * 
 */
@Entity
@Table(name="siac_r_soggetto_onere_mod")
public class SiacRSoggettoOnereModFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_SOGGETTO_ONERE_MOD_SOGGETTO_ONERE_MOD_ID_GENERATOR", allocationSize=1, sequenceName="siac_r_soggetto_onere_mod_soggetto_onere_mod_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_SOGGETTO_ONERE_MOD_SOGGETTO_ONERE_MOD_ID_GENERATOR")
	@Column(name="soggetto_onere_mod_id")
	private Integer soggettoOnereModId;

	//bi-directional many-to-one association to SiacDOnereFin
	@ManyToOne
	@JoinColumn(name="onere_id")
	private SiacDOnereFin siacDOnere;

//	//bi-directional many-to-one association to SiacTSoggettoFin
	@ManyToOne
	@JoinColumn(name="soggetto_id")
	private SiacTSoggettoFin siacTSoggetto;

	//bi-directional many-to-one association to SiacTSoggettoModFin
	@ManyToOne
	@JoinColumn(name="sog_mod_id")
	private SiacTSoggettoModFin siacTSoggettoMod;

	public SiacRSoggettoOnereModFin() {
	}

	public Integer getSoggettoOnereModId() {
		return this.soggettoOnereModId;
	}

	public void setSoggettoOnereModId(Integer soggettoOnereModId) {
		this.soggettoOnereModId = soggettoOnereModId;
	}

	public SiacDOnereFin getSiacDOnere() {
		return this.siacDOnere;
	}

	public void setSiacDOnere(SiacDOnereFin siacDOnere) {
		this.siacDOnere = siacDOnere;
	}

	public SiacTSoggettoFin getSiacTSoggetto() {
		return this.siacTSoggetto;
	}

	public void setSiacTSoggetto(SiacTSoggettoFin siacTSoggetto) {
		this.siacTSoggetto = siacTSoggetto;
	}

	public SiacTSoggettoModFin getSiacTSoggettoMod() {
		return this.siacTSoggettoMod;
	}

	public void setSiacTSoggettoMod(SiacTSoggettoModFin siacTSoggettoMod) {
		this.siacTSoggettoMod = siacTSoggettoMod;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.soggettoOnereModId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.soggettoOnereModId = uid;
	}
}