/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import it.csi.siac.siacfinser.integration.entity.base.SiacTEnteBase;


/**
 * The persistent class for the siac_r_atto_allegato_sog database table.
 * 
 */
@Entity
@Table(name="siac_r_atto_allegato_sog")
public class SiacRAttoAllegatoSogFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="attoal_sog_id")
	private Integer attoalSogId;

	@Column(name="attoal_sog_causale_sosp")
	private String attoalSogCausaleSosp;

	@Column(name="attoal_sog_data_riatt")
	private Timestamp attoalSogDataRiatt;

	@Column(name="attoal_sog_data_sosp")
	private Timestamp attoalSogDataSosp;

//	//bi-directional many-to-one association to SiacRAttoAllegatoFin
//	@ManyToOne
//	@JoinColumn(name="attoal_id")
//	private SiacRAttoAllegatoFin siacRAttoAllegato;

	//bi-directional many-to-one association to SiacTSoggettoFin
	@ManyToOne
	@JoinColumn(name="soggetto_id")
	private SiacTSoggettoFin siacTSoggetto;

	public SiacRAttoAllegatoSogFin() {
	}

	public Integer getAttoalSogId() {
		return this.attoalSogId;
	}

	public void setAttoalSogId(Integer attoalSogId) {
		this.attoalSogId = attoalSogId;
	}

	public String getAttoalSogCausaleSosp() {
		return this.attoalSogCausaleSosp;
	}

	public void setAttoalSogCausaleSosp(String attoalSogCausaleSosp) {
		this.attoalSogCausaleSosp = attoalSogCausaleSosp;
	}

	public Timestamp getAttoalSogDataRiatt() {
		return this.attoalSogDataRiatt;
	}

	public void setAttoalSogDataRiatt(Timestamp attoalSogDataRiatt) {
		this.attoalSogDataRiatt = attoalSogDataRiatt;
	}

	public Timestamp getAttoalSogDataSosp() {
		return this.attoalSogDataSosp;
	}

	public void setAttoalSogDataSosp(Timestamp attoalSogDataSosp) {
		this.attoalSogDataSosp = attoalSogDataSosp;
	}

//	public SiacRAttoAllegatoFin getSiacRAttoAllegato() {
//		return this.siacRAttoAllegato;
//	}
//
//	public void setSiacRAttoAllegato(SiacRAttoAllegatoFin siacRAttoAllegato) {
//		this.siacRAttoAllegato = siacRAttoAllegato;
//	}

	public SiacTSoggettoFin getSiacTSoggetto() {
		return this.siacTSoggetto;
	}

	public void setSiacTSoggetto(SiacTSoggettoFin siacTSoggetto) {
		this.siacTSoggetto = siacTSoggetto;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.attoalSogId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.attoalSogId = uid;
	}

}