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
 * The persistent class for the siac_r_movgest_stato database table.
 * 
 */
@Entity
@Table(name="siac_r_movgest_stato")
public class SiacRMovgestStatoFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	
	@Id
	@SequenceGenerator(name="SIAC_R_MOVGEST_TS_STATO_MOVGEST_STATO_R_ID_GENERATOR", allocationSize=1, sequenceName="siac_r_movgest_ts_stato_movgest_stato_r_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_MOVGEST_TS_STATO_MOVGEST_STATO_R_ID_GENERATOR")
	@Column(name="movgest_stato_r_id")
	private Integer movgestStatoRId;

	//bi-directional many-to-one association to SiacDMovgestStatoFin
	@ManyToOne
	@JoinColumn(name="movgest_stato_id")
	private SiacDMovgestStatoFin siacDMovgestStato;

	//bi-directional many-to-one association to SiacTMovgestFin
	@ManyToOne
	@JoinColumn(name="movgest_id")
	private SiacTMovgestFin siacTMovgest;

	public SiacRMovgestStatoFin() {
	}

	public Integer getMovgestStatoRId() {
		return this.movgestStatoRId;
	}

	public void setMovgestStatoRId(Integer movgestStatoRId) {
		this.movgestStatoRId = movgestStatoRId;
	}

	public SiacDMovgestStatoFin getSiacDMovgestStato() {
		return this.siacDMovgestStato;
	}

	public void setSiacDMovgestStato(SiacDMovgestStatoFin siacDMovgestStato) {
		this.siacDMovgestStato = siacDMovgestStato;
	}

	public SiacTMovgestFin getSiacTMovgest() {
		return this.siacTMovgest;
	}

	public void setSiacTMovgest(SiacTMovgestFin siacTMovgest) {
		this.siacTMovgest = siacTMovgest;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.movgestStatoRId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.movgestStatoRId = uid;
	}
}