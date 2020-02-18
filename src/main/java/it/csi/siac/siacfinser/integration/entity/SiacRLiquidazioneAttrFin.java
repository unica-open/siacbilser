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


/**
 * The persistent class for the siac_r_liquidazione_attr database table.
 * 
 */
@Entity
@Table(name="siac_r_liquidazione_attr")

public class SiacRLiquidazioneAttrFin extends SiacRAttrBaseFin {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_T_LIQUIDAZIONE_ATTR_LIQ_ATTR_ID_GENERATOR", allocationSize=1, sequenceName="siac_r_liquidazione_attr_liq_attr_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_LIQUIDAZIONE_ATTR_LIQ_ATTR_ID_GENERATOR")
	
	@Column(name="liq_attr_id")
	private Integer liqAttrId;

	//bi-directional many-to-one association to SiacTLiquidazioneFin
	@ManyToOne
	@JoinColumn(name="liq_id")
	private SiacTLiquidazioneFin siacTLiquidazione;

	public SiacRLiquidazioneAttrFin() {
	}

	public Integer getLiqAttrId() {
		return this.liqAttrId;
	}

	public void setLiqAttrId(Integer liqAttrId) {
		this.liqAttrId = liqAttrId;
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
		return liqAttrId;
	}

	@Override
	public void setUid(Integer uid) {
		this.liqAttrId = uid;
		
	}

}