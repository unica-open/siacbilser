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
 * The persistent class for the siac_r_mutuo_voce_liquidazione database table.
 * 
 */
@Entity
@Table(name="siac_r_mutuo_voce_liquidazione")
public class SiacRMutuoVoceLiquidazioneFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_MUTUO_VOCE_LIQUIDAZIONE_ID_GENERATOR", allocationSize=1, sequenceName="siac_r_mutuo_voce_liquidazione_mut_liq_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_MUTUO_VOCE_LIQUIDAZIONE_ID_GENERATOR")
	@Column(name="mut_liq_id")
	private Integer mutLiqId;

	//bi-directional many-to-one association to SiacTLiquidazioneFin
	@ManyToOne
	@JoinColumn(name="liq_id")
	private SiacTLiquidazioneFin siacTLiquidazione;

	//bi-directional many-to-one association to SiacTMutuoVoceFin
	@ManyToOne
	@JoinColumn(name="mut_voce_id")
	private SiacTMutuoVoceFin siacTMutuoVoce;

	public SiacRMutuoVoceLiquidazioneFin() {
	}

	public Integer getMutLiqId() {
		return this.mutLiqId;
	}

	public void setMutLiqId(Integer mutLiqId) {
		this.mutLiqId = mutLiqId;
	}

	public SiacTLiquidazioneFin getSiacTLiquidazione() {
		return this.siacTLiquidazione;
	}

	public void setSiacTLiquidazione(SiacTLiquidazioneFin siacTLiquidazione) {
		this.siacTLiquidazione = siacTLiquidazione;
	}

	public SiacTMutuoVoceFin getSiacTMutuoVoce() {
		return this.siacTMutuoVoce;
	}

	public void setSiacTMutuoVoce(SiacTMutuoVoceFin siacTMutuoVoce) {
		this.siacTMutuoVoce = siacTMutuoVoce;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.mutLiqId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.mutLiqId = uid;
	}
}