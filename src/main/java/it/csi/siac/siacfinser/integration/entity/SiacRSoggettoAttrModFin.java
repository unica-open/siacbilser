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


/**
 * The persistent class for the siac_r_soggetto_attr_mod database table.
 * 
 */
@Entity
@Table(name="siac_r_soggetto_attr_mod")
public class SiacRSoggettoAttrModFin extends SiacRAttrBaseFin {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_SOGGETTO_ATTR_MOD_SOGGETTO_ATTR_MOD_ID_GENERATOR", allocationSize=1, sequenceName="siac_r_soggetto_attr_mod_soggetto_attr_mod_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_SOGGETTO_ATTR_MOD_SOGGETTO_ATTR_MOD_ID_GENERATOR")
	@Column(name="soggetto_attr_mod_id")
	private Integer soggettoAttrModId;

//	//bi-directional many-to-one association to SiacTSoggettoFin
	@ManyToOne
	@JoinColumn(name="soggetto_id")
	private SiacTSoggettoFin siacTSoggetto;
//
//	//bi-directional many-to-one association to SiacTSoggettoModFin
	@ManyToOne
	@JoinColumn(name="sog_mod_id")
	private SiacTSoggettoModFin siacTSoggettoMod;

	public SiacRSoggettoAttrModFin() {
	}

	public Integer getSoggettoAttrModId() {
		return this.soggettoAttrModId;
	}

	public void setSoggettoAttrModId(Integer soggettoAttrModId) {
		this.soggettoAttrModId = soggettoAttrModId;
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
		return this.soggettoAttrModId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.soggettoAttrModId = uid;
	}
}