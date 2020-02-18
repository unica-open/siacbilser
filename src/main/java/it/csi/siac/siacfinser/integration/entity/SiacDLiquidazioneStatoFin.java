/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import it.csi.siac.siacfinser.integration.entity.base.SiacTEnteBase;


/**
 * The persistent class for the siac_d_liquidazione_stato database table.
 * 
 */
@Entity
@Table(name="siac_d_liquidazione_stato")

public class SiacDLiquidazioneStatoFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_D_LIQUIDAZIONE_STATO_LIQ_STATO_ID_GENERATOR", allocationSize=1, sequenceName="siac_d_liquidazione_stato_liq_stato_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_LIQUIDAZIONE_STATO_LIQ_STATO_ID_GENERATOR")
	@Column(name="liq_stato_id")
	private Integer liqStatoId;

	
	@Column(name="liq_stato_code")
	private String liqStatoCode;

	@Column(name="liq_stato_desc")
	private String liqStatoDesc;

	
	//bi-directional many-to-one association to SiacRLiquidazioneStatoFin
	@OneToMany(mappedBy="siacDLiquidazioneStato")
	private List<SiacRLiquidazioneStatoFin> siacRLiquidazioneStatos;

	public SiacDLiquidazioneStatoFin() {
	}

	public Integer getLiqStatoId() {
		return this.liqStatoId;
	}

	public void setLiqStatoId(Integer liqStatoId) {
		this.liqStatoId = liqStatoId;
	}


	public String getLiqStatoCode() {
		return this.liqStatoCode;
	}

	public void setLiqStatoCode(String liqStatoCode) {
		this.liqStatoCode = liqStatoCode;
	}

	public String getLiqStatoDesc() {
		return this.liqStatoDesc;
	}

	public void setLiqStatoDesc(String liqStatoDesc) {
		this.liqStatoDesc = liqStatoDesc;
	}

	
	public List<SiacRLiquidazioneStatoFin> getSiacRLiquidazioneStatos() {
		return this.siacRLiquidazioneStatos;
	}

	public void setSiacRLiquidazioneStatos(List<SiacRLiquidazioneStatoFin> siacRLiquidazioneStatos) {
		this.siacRLiquidazioneStatos = siacRLiquidazioneStatos;
	}

	public SiacRLiquidazioneStatoFin addSiacRLiquidazioneStato(SiacRLiquidazioneStatoFin siacRLiquidazioneStato) {
		getSiacRLiquidazioneStatos().add(siacRLiquidazioneStato);
		siacRLiquidazioneStato.setSiacDLiquidazioneStato(this);

		return siacRLiquidazioneStato;
	}

	public SiacRLiquidazioneStatoFin removeSiacRLiquidazioneStato(SiacRLiquidazioneStatoFin siacRLiquidazioneStato) {
		getSiacRLiquidazioneStatos().remove(siacRLiquidazioneStato);
		siacRLiquidazioneStato.setSiacDLiquidazioneStato(null);

		return siacRLiquidazioneStato;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return liqStatoId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.liqStatoId=uid;
	}

}