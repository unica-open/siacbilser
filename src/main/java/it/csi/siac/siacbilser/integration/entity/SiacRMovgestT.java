/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import java.math.BigDecimal;

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
 * The persistent class for the siac_r_movgest_ts database table.
 * 
 */
@Entity
@Table(name="siac_r_movgest_ts")
@NamedQuery(name="SiacRMovgestT.findAll", query="SELECT s FROM SiacRMovgestT s")
public class SiacRMovgestT extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_MOVGEST_TS_MOVGESTTSRID_GENERATOR", allocationSize = 1, sequenceName="SIAC_R_MOVGEST_TS_MOVGEST_TS_R_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_MOVGEST_TS_MOVGESTTSRID_GENERATOR")
	@Column(name="movgest_ts_r_id")
	private Integer movgestTsRId;

	@Column(name="movgest_ts_importo")
	private BigDecimal movgestTsImporto;

	//bi-directional many-to-one association to SiacTMovgestT
	@ManyToOne
	@JoinColumn(name="movgest_ts_b_id")
	private SiacTMovgestT siacTMovgestT1;

	//bi-directional many-to-one association to SiacTMovgestT
	@ManyToOne
	@JoinColumn(name="movgest_ts_a_id")
	private SiacTMovgestT siacTMovgestT2;

	@ManyToOne
	@JoinColumn(name="avav_id")
	private SiacTAvanzovincolo siacTAvanzovincolo;
	
	
	public SiacRMovgestT() {
	}

	public Integer getMovgestTsRId() {
		return this.movgestTsRId;
	}

	public void setMovgestTsRId(Integer movgestTsRId) {
		this.movgestTsRId = movgestTsRId;
	}

	public BigDecimal getMovgestTsImporto() {
		return this.movgestTsImporto;
	}

	public void setMovgestTsImporto(BigDecimal movgestTsImporto) {
		this.movgestTsImporto = movgestTsImporto;
	}

	public SiacTMovgestT getSiacTMovgestT1() {
		return this.siacTMovgestT1;
	}

	public void setSiacTMovgestT1(SiacTMovgestT siacTMovgestT1) {
		this.siacTMovgestT1 = siacTMovgestT1;
	}

	public SiacTMovgestT getSiacTMovgestT2() {
		return this.siacTMovgestT2;
	}

	public void setSiacTMovgestT2(SiacTMovgestT siacTMovgestT2) {
		this.siacTMovgestT2 = siacTMovgestT2;
	}

	@Override
	public Integer getUid() {
		return movgestTsRId;
	}

	@Override
	public void setUid(Integer uid) {
		movgestTsRId = uid;
	}

	public SiacTAvanzovincolo getSiacTAvanzovincolo() {
		return siacTAvanzovincolo;
	}

	public void setSiacTAvanzovincolo(SiacTAvanzovincolo siacTAvanzovincolo) {
		this.siacTAvanzovincolo = siacTAvanzovincolo;
	}

}