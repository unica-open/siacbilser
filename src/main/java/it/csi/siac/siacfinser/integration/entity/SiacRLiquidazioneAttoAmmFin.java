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
 * The persistent class for the siac_r_liquidazione_atto_amm database table.
 * 
 */
@Entity
@Table(name="siac_r_liquidazione_atto_amm")

public class SiacRLiquidazioneAttoAmmFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_LIQUIDAZIONE_ATTO_AMM_LIQ_ATTO_AMM_ID_GENERATOR", allocationSize=1, sequenceName="siac_r_liquidazione_atto_amm_liq_atto_amm_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_LIQUIDAZIONE_ATTO_AMM_LIQ_ATTO_AMM_ID_GENERATOR")
	
	@Column(name="liq_atto_amm_id")
	private Integer liqAttoAmmId;

	//bi-directional many-to-one association to SiacTAttoAmmFin
	@ManyToOne
	@JoinColumn(name="attoamm_id")
	private SiacTAttoAmmFin siacTAttoAmm;


	//bi-directional many-to-one association to SiacTLiquidazioneFin
	@ManyToOne
	@JoinColumn(name="liq_id")
	private SiacTLiquidazioneFin siacTLiquidazione;

	public SiacRLiquidazioneAttoAmmFin() {
	}

	public Integer getLiqAttoAmmId() {
		return this.liqAttoAmmId;
	}

	public void setLiqAttoAmmId(Integer liqAttoAmmId) {
		this.liqAttoAmmId = liqAttoAmmId;
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
		return liqAttoAmmId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.liqAttoAmmId=uid;
	}

	public SiacTAttoAmmFin getSiacTAttoAmm() {
		return siacTAttoAmm;
	}

	public void setSiacTAttoAmm(SiacTAttoAmmFin siacTAttoAmm) {
		this.siacTAttoAmm = siacTAttoAmm;
	}

}