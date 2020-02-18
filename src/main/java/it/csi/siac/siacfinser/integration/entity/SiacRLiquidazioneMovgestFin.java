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
 * The persistent class for the siac_r_liquidazione_movgest database table.
 * 
 */
@Entity
@Table(name="siac_r_liquidazione_movgest")
public class SiacRLiquidazioneMovgestFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_T_LIQUIDAZIONE_MOVGEST_LIQ_MOVGEST_ID_GENERATOR", allocationSize=1, sequenceName="siac_r_liquidazione_movgest_liq_movgest_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_LIQUIDAZIONE_MOVGEST_LIQ_MOVGEST_ID_GENERATOR")
	@Column(name="liq_movgest_id")
	private Integer liqMovgestId;

	//bi-directional many-to-one association to SiacTLiquidazioneFin
	@ManyToOne
	@JoinColumn(name="liq_id")
	private SiacTLiquidazioneFin siacTLiquidazione;
	
	//bi-directional many-to-one association to SiacTMovgestT
	@ManyToOne
	@JoinColumn(name="movgest_ts_id")
	private SiacTMovgestTsFin siacTMovgestT;

	public SiacRLiquidazioneMovgestFin() {
	}

	public Integer getLiqMovgestId() {
		return this.liqMovgestId;
	}

	public void setLiqMovgestId(Integer liqMovgestId) {
		this.liqMovgestId = liqMovgestId;
	}

	public SiacTLiquidazioneFin getSiacTLiquidazione() {
		return this.siacTLiquidazione;
	}

	public void setSiacTLiquidazione(SiacTLiquidazioneFin siacTLiquidazione) {
		this.siacTLiquidazione = siacTLiquidazione;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return liqMovgestId;
	}

	@Override
	public void setUid(Integer uid) {
		this.liqMovgestId = uid;
		
	}

	public SiacTMovgestTsFin getSiacTMovgestTs() {
		return siacTMovgestT;
	}

	public void setSiacTMovgestTs(SiacTMovgestTsFin siacTMovgestT) {
		this.siacTMovgestT = siacTMovgestT;
	}
}