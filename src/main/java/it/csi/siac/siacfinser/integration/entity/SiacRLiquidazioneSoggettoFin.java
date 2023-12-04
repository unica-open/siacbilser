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
 * The persistent class for the siac_r_liquidazione_soggetto database table.
 * 
 */
@Entity
@Table(name="siac_r_liquidazione_soggetto")
public class SiacRLiquidazioneSoggettoFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_T_LIQUIDAZIONE_SOGGETTO_LIQ_SOGGETTO_ID_GENERATOR", allocationSize=1, sequenceName="siac_r_liquidazione_soggetto_liq_soggetto_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_LIQUIDAZIONE_SOGGETTO_LIQ_SOGGETTO_ID_GENERATOR")
	@Column(name="liq_soggetto_id")
	private Integer liqSoggettoId;

	//bi-directional many-to-one association to SiacTSoggettoFin
	@ManyToOne
	@JoinColumn(name="soggetto_id")
	private SiacTSoggettoFin siacTSoggetto;

	//bi-directional many-to-one association to SiacTLiquidazioneFin
	@ManyToOne
	@JoinColumn(name="liq_id")
	private SiacTLiquidazioneFin siacTLiquidazione;

	public SiacRLiquidazioneSoggettoFin() {
	}

	public Integer getLiqSoggettoId() {
		return this.liqSoggettoId;
	}

	public void setLiqSoggettoId(Integer liqSoggettoId) {
		this.liqSoggettoId = liqSoggettoId;
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
		return liqSoggettoId;
	}

	@Override
	public void setUid(Integer uid) {
		this.liqSoggettoId = uid;
		
	}

	public SiacTSoggettoFin getSiacTSoggetto() {
		return siacTSoggetto;
	}

	public void setSiacTSoggetto(SiacTSoggettoFin siacTSoggetto) {
		this.siacTSoggetto = siacTSoggetto;
	}

}