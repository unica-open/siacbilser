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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="siac_r_mutuo_movgest_ts")
public class SiacRMutuoMovgestT extends SiacTEnteBaseExt {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_MUTUO_MOVGEST_TS_MUTUOMOVGESTTSID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_MUTUO_MOVGEST_TS_MUTUO_MOVGEST_TS_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_MUTUO_MOVGEST_TS_MUTUOMOVGESTTSID_GENERATOR")
	@Column(name="mutuo_movgest_ts_id")
	private Integer mutuoMovgestTsId;

	@ManyToOne
	@JoinColumn(name="mutuo_id")
	private SiacTMutuo siacTMutuo;
	
	@ManyToOne
	@JoinColumn(name="movgest_ts_id")
	private SiacTMovgestT siacTMovgestT;
	
	@Column(name="mutuo_movgest_ts_importo_iniziale")
	private BigDecimal mutuoMovgestTsImportoIniziale;
	
	@Column(name="mutuo_movgest_ts_importo_finale")
	private BigDecimal mutuoMovgestTsImportoFinale;

	@Override
	public Integer getUid() {
		return getMutuoMovgestTsId();
	}

	@Override
	public void setUid(Integer uid) {
		setMutuoMovgestTsId(uid);
	}

	public Integer getMutuoMovgestTsId() {
		return mutuoMovgestTsId;
	}

	public void setMutuoMovgestTsId(Integer mutuoMovgestTsId) {
		this.mutuoMovgestTsId = mutuoMovgestTsId;
	}

	public SiacTMutuo getSiacTMutuo() {
		return siacTMutuo;
	}

	public void setSiacTMutuo(SiacTMutuo siacTMutuo) {
		this.siacTMutuo = siacTMutuo;
	}

	public SiacTMovgestT getSiacTMovgestT() {
		return siacTMovgestT;
	}

	public void setSiacTMovgestT(SiacTMovgestT siacTMovgestT) {
		this.siacTMovgestT = siacTMovgestT;
	}

	public BigDecimal getMutuoMovgestTsImportoIniziale() {
		return mutuoMovgestTsImportoIniziale;
	}

	public void setMutuoMovgestTsImportoIniziale(BigDecimal mutuoMovgestTsImportoIniziale) {
		this.mutuoMovgestTsImportoIniziale = mutuoMovgestTsImportoIniziale;
	}

	public BigDecimal getMutuoMovgestTsImportoFinale() {
		return mutuoMovgestTsImportoFinale;
	}

	public void setMutuoMovgestTsImportoFinale(BigDecimal mutuoMovgestTsImportoFinale) {
		this.mutuoMovgestTsImportoFinale = mutuoMovgestTsImportoFinale;
	}

}