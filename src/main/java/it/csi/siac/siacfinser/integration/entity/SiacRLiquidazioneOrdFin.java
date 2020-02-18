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
 * The persistent class for the siac_r_liquidazione_ord database table.
 * 
 */
@Entity
@Table(name="siac_r_liquidazione_ord")
public class SiacRLiquidazioneOrdFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_LIQUIDAZIONE_ORD_ID_GENERATOR", allocationSize=1, sequenceName="siac_r_liquidazione_ord_liq_ord_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_LIQUIDAZIONE_ORD_ID_GENERATOR")
	@Column(name="liq_ord_id")
	private Integer liqOrdId;

	//bi-directional many-to-one association to SiacTLiquidazioneFin
	@ManyToOne
	@JoinColumn(name="liq_id")
	private SiacTLiquidazioneFin siacTLiquidazione;

	//bi-directional many-to-one association to SiacTOrdinativoTFin
	@ManyToOne
	@JoinColumn(name="sord_id")
	private SiacTOrdinativoTFin siacTOrdinativoT;

	public SiacRLiquidazioneOrdFin() {
	}

	public Integer getLiqOrdId() {
		return this.liqOrdId;
	}

	public void setLiqOrdId(Integer liqOrdId) {
		this.liqOrdId = liqOrdId;
	}

	public SiacTLiquidazioneFin getSiacTLiquidazione() {
		return this.siacTLiquidazione;
	}

	public void setSiacTLiquidazione(SiacTLiquidazioneFin siacTLiquidazione) {
		this.siacTLiquidazione = siacTLiquidazione;
	}

	public SiacTOrdinativoTFin getSiacTOrdinativoT() {
		return this.siacTOrdinativoT;
	}

	public void setSiacTOrdinativoT(SiacTOrdinativoTFin siacTOrdinativoT) {
		this.siacTOrdinativoT = siacTOrdinativoT;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.liqOrdId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.liqOrdId = uid;
	}

}