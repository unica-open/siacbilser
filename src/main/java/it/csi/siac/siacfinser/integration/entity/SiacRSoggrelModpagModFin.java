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
 * The persistent class for the siac_r_soggrel_modpag_mod database table.
 * 
 */
@Entity
@Table(name="siac_r_soggrel_modpag_mod")
public class SiacRSoggrelModpagModFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_SOGGREL_MODPAG_MOD_SOGRELMPAG_MOD_ID_GENERATOR", allocationSize=1, sequenceName="siac_r_soggrel_modpag_mod_sogrelmpag_mod_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_SOGGREL_MODPAG_MOD_SOGRELMPAG_MOD_ID_GENERATOR")
	@Column(name="sogrelmpag_mod_id")
	private Integer sogrelmpagModId;

	//bi-directional many-to-one association to SiacRSoggettoRelazFin
//	@ManyToOne
//	@JoinColumn(name="soggetto_relaz_id")
//	private SiacRSoggettoRelazFin siacRSoggettoRelaz;

	//bi-directional many-to-one association to SiacTModpagFin
	@ManyToOne
	@JoinColumn(name="modpag_id")
	private SiacTModpagFin siacTModpag;

	//bi-directional many-to-one association to SiacTSoggettoModFin
	@ManyToOne
	@JoinColumn(name="sog_mod_id")
	private SiacTSoggettoModFin siacTSoggettoMod;
	
	/****************************************/
	private String note;

	//bi-directional many-to-one association to SiacRSoggettoRelazModFin
	@ManyToOne
	@JoinColumn(name="soggetto_relaz_mod_id")
	private SiacRSoggettoRelazModFin siacRSoggettoRelazMod;

	//bi-directional many-to-one association to SiacRSoggrelModpagFin
	@ManyToOne
	@JoinColumn(name="soggrelmpag_id")
	private SiacRSoggrelModpagFin siacRSoggrelModpag;
	
	
	/******************************************/
	
	
	

	public SiacRSoggrelModpagModFin() {
	}

	public Integer getSogrelmpagModId() {
		return this.sogrelmpagModId;
	}

	public void setSogrelmpagModId(Integer sogrelmpagModId) {
		this.sogrelmpagModId = sogrelmpagModId;
	}


	public SiacTModpagFin getSiacTModpag() {
		return this.siacTModpag;
	}

	public void setSiacTModpag(SiacTModpagFin siacTModpag) {
		this.siacTModpag = siacTModpag;
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
		return this.sogrelmpagModId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.sogrelmpagModId = uid;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public SiacRSoggettoRelazModFin getSiacRSoggettoRelazMod() {
		return siacRSoggettoRelazMod;
	}

	public void setSiacRSoggettoRelazMod(SiacRSoggettoRelazModFin siacRSoggettoRelazMod) {
		this.siacRSoggettoRelazMod = siacRSoggettoRelazMod;
	}

	public SiacRSoggrelModpagFin getSiacRSoggrelModpag() {
		return siacRSoggrelModpag;
	}

	public void setSiacRSoggrelModpag(SiacRSoggrelModpagFin siacRSoggrelModpag) {
		this.siacRSoggrelModpag = siacRSoggrelModpag;
	}

//	public SiacRSoggettoRelazFin getSiacRSoggettoRelaz() {
//		return siacRSoggettoRelaz;
//	}
//
//	public void setSiacRSoggettoRelaz(SiacRSoggettoRelazFin siacRSoggettoRelaz) {
//		this.siacRSoggettoRelaz = siacRSoggettoRelaz;
//	}
}