/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**
 * The persistent class for the siac_r_cartacont_det_soggetto database table.
 * 
 */
@Entity
@Table(name="siac_r_cartacont_det_soggetto")
@NamedQuery(name="SiacRCartacontDetSoggetto.findAll", query="SELECT s FROM SiacRCartacontDetSoggetto s")
public class SiacRCartacontDetSoggetto extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_CARTACONT_DET_SOGGETTO_ORDSOGGETTOID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_CARTACONT_DET_SOGGETTO_ORD_SOGGETTO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_CARTACONT_DET_SOGGETTO_ORDSOGGETTOID_GENERATOR")
	@Column(name="ord_soggetto_id")
	private Integer ordSoggettoId;

	//bi-directional many-to-one association to SiacTCartacontDet
	@ManyToOne
	@JoinColumn(name="cartac_det_id")
	private SiacTCartacontDet siacTCartacontDet;

	//bi-directional many-to-one association to SiacTSoggetto
	@ManyToOne
	@JoinColumn(name="soggetto_id")
	private SiacTSoggetto siacTSoggetto;

	public SiacRCartacontDetSoggetto() {
	}

	public Integer getOrdSoggettoId() {
		return this.ordSoggettoId;
	}

	public void setOrdSoggettoId(Integer ordSoggettoId) {
		this.ordSoggettoId = ordSoggettoId;
	}
	
	public SiacTCartacontDet getSiacTCartacontDet() {
		return this.siacTCartacontDet;
	}

	public void setSiacTCartacontDet(SiacTCartacontDet siacTCartacontDet) {
		this.siacTCartacontDet = siacTCartacontDet;
	}

	public SiacTSoggetto getSiacTSoggetto() {
		return this.siacTSoggetto;
	}

	public void setSiacTSoggetto(SiacTSoggetto siacTSoggetto) {
		this.siacTSoggetto = siacTSoggetto;
	}

	@Override
	public Integer getUid() {
		return ordSoggettoId;
	}

	@Override
	public void setUid(Integer uid) {
		this.ordSoggettoId = uid;
	}

}