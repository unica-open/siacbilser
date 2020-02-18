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
 * The persistent class for the siac_r_movgest_ts_programma database table.
 * 
 */
@Entity
@Table(name="siac_r_movgest_ts_programma")
public class SiacRMovgestTsProgrammaFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_MOVGEST_TS_PROGRAMMA_ID_GENERATOR", allocationSize=1, sequenceName="siac_r_movgest_ts_programma_movgest_ts_programma_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_MOVGEST_TS_PROGRAMMA_ID_GENERATOR")
	@Column(name="movgest_ts_programma_id")
	private Integer movgestTsProgrammaId;

	//bi-directional many-to-one association to SiacTMovgestT
	@ManyToOne
	@JoinColumn(name="movgest_ts_id")
	private SiacTMovgestTsFin siacTMovgestT;

	//bi-directional many-to-one association to SiacTProgrammaFin
	@ManyToOne
	@JoinColumn(name="programma_id")
	private SiacTProgrammaFin siacTProgramma;

	public SiacRMovgestTsProgrammaFin() {
	}

	public Integer getMovgestTsProgrammaId() {
		return this.movgestTsProgrammaId;
	}

	public void setMovgestTsProgrammaId(Integer movgestTsProgrammaId) {
		this.movgestTsProgrammaId = movgestTsProgrammaId;
	}

	public SiacTMovgestTsFin getSiacTMovgestT() {
		return this.siacTMovgestT;
	}

	public void setSiacTMovgestT(SiacTMovgestTsFin siacTMovgestT) {
		this.siacTMovgestT = siacTMovgestT;
	}

	public SiacTProgrammaFin getSiacTProgramma() {
		return this.siacTProgramma;
	}

	public void setSiacTProgramma(SiacTProgrammaFin siacTProgramma) {
		this.siacTProgramma = siacTProgramma;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.movgestTsProgrammaId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.movgestTsProgrammaId = uid;
	}
}