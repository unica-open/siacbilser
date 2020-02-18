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
 * The persistent class for the siac_r_cartacont_det_movgest_ts database table.
 * 
 */
@Entity
@Table(name="siac_r_cartacont_det_movgest_ts")
@NamedQuery(name="SiacRCartacontDetMovgestT.findAll", query="SELECT s FROM SiacRCartacontDetMovgestT s")
public class SiacRCartacontDetMovgestT extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_CARTACONT_DET_MOVGEST_TS_SUBDOCMOVGESTTSID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_CARTACONT_DET_MOVGEST_TS_SUBDOC_MOVGEST_TS_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_CARTACONT_DET_MOVGEST_TS_SUBDOCMOVGESTTSID_GENERATOR")
	@Column(name="subdoc_movgest_ts_id")
	private Integer subdocMovgestTsId;

	//bi-directional many-to-one association to SiacTCartacontDet
	@ManyToOne
	@JoinColumn(name="cartac_det_id")
	private SiacTCartacontDet siacTCartacontDet;

	//bi-directional many-to-one association to SiacTMovgestT
	@ManyToOne
	@JoinColumn(name="movgest_ts_id")
	private SiacTMovgestT siacTMovgestT;

	public SiacRCartacontDetMovgestT() {
	}

	public Integer getSubdocMovgestTsId() {
		return this.subdocMovgestTsId;
	}

	public void setSubdocMovgestTsId(Integer subdocMovgestTsId) {
		this.subdocMovgestTsId = subdocMovgestTsId;
	}

	public SiacTCartacontDet getSiacTCartacontDet() {
		return this.siacTCartacontDet;
	}

	public void setSiacTCartacontDet(SiacTCartacontDet siacTCartacontDet) {
		this.siacTCartacontDet = siacTCartacontDet;
	}

	public SiacTMovgestT getSiacTMovgestT() {
		return this.siacTMovgestT;
	}

	public void setSiacTMovgestT(SiacTMovgestT siacTMovgestT) {
		this.siacTMovgestT = siacTMovgestT;
	}

	@Override
	public Integer getUid() {
		return subdocMovgestTsId;
	}

	@Override
	public void setUid(Integer uid) {
		this.subdocMovgestTsId = uid;
	}

}