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

import it.csi.siac.siacfinser.integration.entity.base.SiacConModificaStato;

/**
 * The persistent class for the siac_r_movgest_ts_sog_mod database table.
 * 
 */
@Entity
@Table(name="siac_r_movgest_ts_sog_mod")
public class SiacRMovgestTsSogModFin extends SiacConModificaStato {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_MOVGEST_TS_SOG_MOD_ID_GENERATOR", allocationSize=1, sequenceName="siac_r_movgest_ts_sog_mod_movgest_ts_sog_mod_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_MOVGEST_TS_SOG_MOD_ID_GENERATOR")	
	@Column(name="movgest_ts_sog_mod_id")
	private Integer movgestTsSogModId;

	//bi-directional many-to-one association to SiacRModificaStatoFin
//	@ManyToOne
//	@JoinColumn(name="mod_stato_r_id")
//	private SiacRModificaStatoFin siacRModificaStato;

	//bi-directional many-to-one association to SiacRMovgestTsSogFin
	@ManyToOne
	@JoinColumn(name="movgest_ts_sog_id")
	private SiacRMovgestTsSogFin siacRMovgestTsSog;

	//bi-directional many-to-one association to SiacTMovgestT
	@ManyToOne
	@JoinColumn(name="movgest_ts_id")
	private SiacTMovgestTsFin siacTMovgestT;

	//bi-directional many-to-one association to SiacTSoggettoFin
	@ManyToOne
	@JoinColumn(name="soggetto_id_old")
	private SiacTSoggettoFin siacTSoggetto1;

	//bi-directional many-to-one association to SiacTSoggettoFin
	@ManyToOne
	@JoinColumn(name="soggetto_id_new")
	private SiacTSoggettoFin siacTSoggetto2;

	public SiacRMovgestTsSogModFin() {
	}

	public Integer getMovgestTsSogModId() {
		return this.movgestTsSogModId;
	}

	public void setMovgestTsSogModId(Integer movgestTsSogModId) {
		this.movgestTsSogModId = movgestTsSogModId;
	}

//	public SiacRModificaStatoFin getSiacRModificaStato() {
//		return this.siacRModificaStato;
//	}
//
//	public void setSiacRModificaStato(SiacRModificaStatoFin siacRModificaStato) {
//		this.siacRModificaStato = siacRModificaStato;
//	}

	public SiacRMovgestTsSogFin getSiacRMovgestTsSog() {
		return this.siacRMovgestTsSog;
	}

	public void setSiacRMovgestTsSog(SiacRMovgestTsSogFin siacRMovgestTsSog) {
		this.siacRMovgestTsSog = siacRMovgestTsSog;
	}

	public SiacTMovgestTsFin getSiacTMovgestT() {
		return this.siacTMovgestT;
	}

	public void setSiacTMovgestT(SiacTMovgestTsFin siacTMovgestT) {
		this.siacTMovgestT = siacTMovgestT;
	}

	public SiacTSoggettoFin getSiacTSoggetto1() {
		return this.siacTSoggetto1;
	}

	public void setSiacTSoggetto1(SiacTSoggettoFin siacTSoggetto1) {
		this.siacTSoggetto1 = siacTSoggetto1;
	}

	public SiacTSoggettoFin getSiacTSoggetto2() {
		return this.siacTSoggetto2;
	}

	public void setSiacTSoggetto2(SiacTSoggettoFin siacTSoggetto2) {
		this.siacTSoggetto2 = siacTSoggetto2;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.movgestTsSogModId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.movgestTsSogModId = uid;
	}
}