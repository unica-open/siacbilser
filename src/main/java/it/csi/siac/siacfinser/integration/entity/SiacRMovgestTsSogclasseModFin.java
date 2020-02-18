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
 * The persistent class for the siac_r_movgest_ts_sogclasse_mod database table.
 * 
 */
@Entity
@Table(name="siac_r_movgest_ts_sogclasse_mod")
public class SiacRMovgestTsSogclasseModFin extends SiacConModificaStato {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_MOVGEST_TS_SOGCLASSE_MOD_ID_GENERATOR", allocationSize=1, sequenceName="siac_r_movgest_ts_sogclasse_mod_movgest_ts_sogclasse_mod_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_MOVGEST_TS_SOGCLASSE_MOD_ID_GENERATOR")	
	@Column(name="movgest_ts_sogclasse_mod_id")
	private Integer movgestTsSogclasseModId;

	//bi-directional many-to-one association to SiacDSoggettoClasseFin
	@ManyToOne
	@JoinColumn(name="soggetto_classe_id_old")
	private SiacDSoggettoClasseFin siacDSoggettoClasse1;

	//bi-directional many-to-one association to SiacDSoggettoClasseFin
	@ManyToOne
	@JoinColumn(name="soggetto_classe_id_new")
	private SiacDSoggettoClasseFin siacDSoggettoClasse2;

//	//bi-directional many-to-one association to SiacRModificaStatoFin
//	@ManyToOne
//	@JoinColumn(name="mod_stato_r_id")
//	private SiacRModificaStatoFin siacRModificaStato;

	//bi-directional many-to-one association to SiacRMovgestTsSogclasseFin
	@ManyToOne
	@JoinColumn(name="movgest_ts_sogclasse_id")
	private SiacRMovgestTsSogclasseFin siacRMovgestTsSogclasse;

	//bi-directional many-to-one association to SiacTMovgestT
	@ManyToOne
	@JoinColumn(name="movgest_ts_id")
	private SiacTMovgestTsFin siacTMovgestT;

	public SiacRMovgestTsSogclasseModFin() {
	}

	public Integer getMovgestTsSogclasseModId() {
		return this.movgestTsSogclasseModId;
	}

	public void setMovgestTsSogclasseModId(Integer movgestTsSogclasseModId) {
		this.movgestTsSogclasseModId = movgestTsSogclasseModId;
	}

	public SiacDSoggettoClasseFin getSiacDSoggettoClasse1() {
		return this.siacDSoggettoClasse1;
	}

	public void setSiacDSoggettoClasse1(SiacDSoggettoClasseFin siacDSoggettoClasse1) {
		this.siacDSoggettoClasse1 = siacDSoggettoClasse1;
	}

	public SiacDSoggettoClasseFin getSiacDSoggettoClasse2() {
		return this.siacDSoggettoClasse2;
	}

	public void setSiacDSoggettoClasse2(SiacDSoggettoClasseFin siacDSoggettoClasse2) {
		this.siacDSoggettoClasse2 = siacDSoggettoClasse2;
	}

//	public SiacRModificaStatoFin getSiacRModificaStato() {
//		return this.siacRModificaStato;
//	}
//
//	public void setSiacRModificaStato(SiacRModificaStatoFin siacRModificaStato) {
//		this.siacRModificaStato = siacRModificaStato;
//	}

	public SiacRMovgestTsSogclasseFin getSiacRMovgestTsSogclasse() {
		return this.siacRMovgestTsSogclasse;
	}

	public void setSiacRMovgestTsSogclasse(SiacRMovgestTsSogclasseFin siacRMovgestTsSogclasse) {
		this.siacRMovgestTsSogclasse = siacRMovgestTsSogclasse;
	}

	public SiacTMovgestTsFin getSiacTMovgestT() {
		return this.siacTMovgestT;
	}

	public void setSiacTMovgestT(SiacTMovgestTsFin siacTMovgestT) {
		this.siacTMovgestT = siacTMovgestT;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.movgestTsSogclasseModId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.movgestTsSogclasseModId = uid;
	}
}