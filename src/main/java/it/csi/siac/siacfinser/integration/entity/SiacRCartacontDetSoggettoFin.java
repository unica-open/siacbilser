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
 * The persistent class for the siac_r_cartacont_det_soggetto database table.
 * 
 */
@Entity
@Table(name="siac_r_cartacont_det_soggetto")
public class SiacRCartacontDetSoggettoFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_CARTACONT_DET_SOGGETTO_ID_GENERATOR", allocationSize=1, sequenceName="siac_r_cartacont_det_soggetto_ord_soggetto_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_CARTACONT_DET_SOGGETTO_ID_GENERATOR")
	@Column(name="ord_soggetto_id")
	private Integer ordSoggettoId;

	//bi-directional many-to-one association to SiacTCartacontDetFin
	@ManyToOne
	@JoinColumn(name="cartac_det_id")
	private SiacTCartacontDetFin siacTCartacontDet;

	//bi-directional many-to-one association to SiacTSoggettoFin
	@ManyToOne
	@JoinColumn(name="soggetto_id")
	private SiacTSoggettoFin siacTSoggetto;

	public SiacRCartacontDetSoggettoFin() {
	}

	public Integer getOrdSoggettoId() {
		return this.ordSoggettoId;
	}

	public void setOrdSoggettoId(Integer ordSoggettoId) {
		this.ordSoggettoId = ordSoggettoId;
	}

	public SiacTCartacontDetFin getSiacTCartacontDet() {
		return this.siacTCartacontDet;
	}

	public void setSiacTCartacontDet(SiacTCartacontDetFin siacTCartacontDet) {
		this.siacTCartacontDet = siacTCartacontDet;
	}

	public SiacTSoggettoFin getSiacTSoggetto() {
		return this.siacTSoggetto;
	}

	public void setSiacTSoggetto(SiacTSoggettoFin siacTSoggetto) {
		this.siacTSoggetto = siacTSoggetto;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.ordSoggettoId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.ordSoggettoId = uid;
	}

}