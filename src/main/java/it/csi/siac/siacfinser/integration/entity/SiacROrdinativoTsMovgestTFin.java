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
 * The persistent class for the siac_r_ordinativo_ts_movgest_ts database table.
 * 
 */
@Entity
@Table(name="siac_r_ordinativo_ts_movgest_ts")
public class SiacROrdinativoTsMovgestTFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_ORDINATIVO_TS_MOVGEST_TS_ID_GENERATOR", allocationSize=1, sequenceName="siac_r_ordinativo_ts_movgest_ts_ord_movgest_ts_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_ORDINATIVO_TS_MOVGEST_TS_ID_GENERATOR")
	@Column(name="ord_movgest_ts_id")
	private Integer ordMovgestTsId;

	//bi-directional many-to-one association to SiacTMovgestT
	@ManyToOne
	@JoinColumn(name="movgest_ts_id")
	private SiacTMovgestTsFin siacTMovgestT;

	//bi-directional many-to-one association to SiacTOrdinativoTFin
	@ManyToOne
	@JoinColumn(name="ord_ts_id")
	private SiacTOrdinativoTFin siacTOrdinativoT;

	public SiacROrdinativoTsMovgestTFin() {
	}

	public Integer getOrdMovgestTsId() {
		return this.ordMovgestTsId;
	}

	public void setOrdMovgestTsId(Integer ordMovgestTsId) {
		this.ordMovgestTsId = ordMovgestTsId;
	}

	public SiacTMovgestTsFin getSiacTMovgestTs() {
		return this.siacTMovgestT;
	}

	public void setSiacTMovgestTs(SiacTMovgestTsFin siacTMovgestT) {
		this.siacTMovgestT = siacTMovgestT;
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
		return this.ordMovgestTsId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.ordMovgestTsId = uid;
	}
}