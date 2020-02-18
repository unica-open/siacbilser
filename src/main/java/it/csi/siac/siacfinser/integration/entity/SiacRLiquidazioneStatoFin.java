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
 * The persistent class for the siac_r_liquidazione_stato database table.
 * 
 */
@Entity
@Table(name="siac_r_liquidazione_stato")
public class SiacRLiquidazioneStatoFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_T_LIQUIDAZIONE_STATO_LIQ_STATO_ID_GENERATOR", allocationSize=1, sequenceName="siac_r_liquidazione_stato_liq_stato_r_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_LIQUIDAZIONE_STATO_LIQ_STATO_ID_GENERATOR")
	@Column(name="liq_stato_r_id")
	private Integer liqStatoRId;

	//bi-directional many-to-one association to SiacDLiquidazioneStatoFin
	@ManyToOne
	@JoinColumn(name="liq_stato_id")
	private SiacDLiquidazioneStatoFin siacDLiquidazioneStato;

	//bi-directional many-to-one association to SiacTLiquidazioneFin
	@ManyToOne
	@JoinColumn(name="liq_id")
	private SiacTLiquidazioneFin siacTLiquidazione;

	public SiacRLiquidazioneStatoFin() {
	}

	public Integer getLiqStatoRId() {
		return this.liqStatoRId;
	}

	public void setLiqStatoRId(Integer liqStatoRId) {
		this.liqStatoRId = liqStatoRId;
	}

	public SiacDLiquidazioneStatoFin getSiacDLiquidazioneStato() {
		return this.siacDLiquidazioneStato;
	}

	public void setSiacDLiquidazioneStato(SiacDLiquidazioneStatoFin siacDLiquidazioneStato) {
		this.siacDLiquidazioneStato = siacDLiquidazioneStato;
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
		return liqStatoRId;
	}

	@Override
	public void setUid(Integer uid) {
		this.liqStatoRId = uid;
		
	}

}