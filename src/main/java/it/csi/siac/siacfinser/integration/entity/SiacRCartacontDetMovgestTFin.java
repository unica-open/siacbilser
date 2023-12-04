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
 * The persistent class for the siac_r_cartacont_det_movgest_ts database table.
 * 
 */
@Entity
@Table(name="siac_r_cartacont_det_movgest_ts")
public class SiacRCartacontDetMovgestTFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_CARTACONT_DET_MOVGEST_ID_GENERATOR", allocationSize=1, sequenceName="siac_r_cartacont_det_movgest_ts_subdoc_movgest_ts_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_CARTACONT_DET_MOVGEST_ID_GENERATOR")
	@Column(name="subdoc_movgest_ts_id")
	private Integer subdocMovgestTsId;
	
	//bi-directional many-to-one association to SiacTCartacontDetFin
	@ManyToOne
	@JoinColumn(name="cartac_det_id")
	private SiacTCartacontDetFin siacTCartacontDet;

	//bi-directional many-to-one association to SiacTMovgestT
	@ManyToOne
	@JoinColumn(name="movgest_ts_id")
	// private SiacTMovgestT siacTMovgestT;
	private SiacTMovgestTsFin siacTMovgestT;
	
	public SiacRCartacontDetMovgestTFin() {
	}

	public Integer getSubdocMovgestTsId() {
		return this.subdocMovgestTsId;
	}

	public void setSubdocMovgestTsId(Integer subdocMovgestTsId) {
		this.subdocMovgestTsId = subdocMovgestTsId;
	}
	
	public SiacTCartacontDetFin getSiacTCartacontDet() {
		return this.siacTCartacontDet;
	}

	public void setSiacTCartacontDet(SiacTCartacontDetFin siacTCartacontDet) {
		this.siacTCartacontDet = siacTCartacontDet;
	}

	public SiacTMovgestTsFin getSiacTMovgestT() {
		return this.siacTMovgestT;
	}

	public void setSiacTMovgestT(SiacTMovgestTsFin siacTMovgestT) {
		this.siacTMovgestT = siacTMovgestT;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.subdocMovgestTsId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.subdocMovgestTsId = uid;
	}

}