/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity;

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

import it.csi.siac.siacfinser.integration.entity.base.SiacConModificaStato;

/**
 * The persistent class for the siac_t_movgest_ts_det_mod database table.
 * 
 */
@Entity
@Table(name="siac_t_movgest_ts_det_mod")
public class SiacTMovgestTsDetModFin extends SiacConModificaStato {
	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(name="SIAC_T_MOVGEST_TS_DET_MOD_ID_GENERATOR", allocationSize=1, sequenceName="siac_t_movgest_ts_det_mod_movgest_ts_det_mod_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_MOVGEST_TS_DET_MOD_ID_GENERATOR")
	@Column(name="movgest_ts_det_mod_id")
	private Integer movgestTsDetModId;

	@Column(name="movgest_ts_det_importo")
	private BigDecimal movgestTsDetImporto;
	
	@Column(name="mtdm_reimputazione_flag")
	private Boolean mtdmReimputazioneFlag;
	
	@Column(name="mtdm_reimputazione_anno")
	private Integer mtdmReimputazioneAnno;

	//bi-directional many-to-one association to SiacDMovgestTsDetTipoFin
	@ManyToOne
	@JoinColumn(name="movgest_ts_det_tipo_id")
	private SiacDMovgestTsDetTipoFin siacDMovgestTsDetTipo;

	//bi-directional many-to-one association to SiacTMovgestT
	@ManyToOne
	@JoinColumn(name="movgest_ts_id")
	private SiacTMovgestTsFin siacTMovgestT;

	//bi-directional many-to-one association to SiacTMovgestTsDetFin
	@ManyToOne
	@JoinColumn(name="movgest_ts_det_id")
	private SiacTMovgestTsDetFin siacTMovgestTsDet;

	public SiacTMovgestTsDetModFin() {
	}

	public Integer getMovgestTsDetModId() {
		return this.movgestTsDetModId;
	}

	public void setMovgestTsDetModId(Integer movgestTsDetModId) {
		this.movgestTsDetModId = movgestTsDetModId;
	}

	public BigDecimal getMovgestTsDetImporto() {
		return this.movgestTsDetImporto;
	}

	public void setMovgestTsDetImporto(BigDecimal movgestTsDetImporto) {
		this.movgestTsDetImporto = movgestTsDetImporto;
	}

	public SiacDMovgestTsDetTipoFin getSiacDMovgestTsDetTipo() {
		return this.siacDMovgestTsDetTipo;
	}

	public void setSiacDMovgestTsDetTipo(SiacDMovgestTsDetTipoFin siacDMovgestTsDetTipo) {
		this.siacDMovgestTsDetTipo = siacDMovgestTsDetTipo;
	}

	public SiacTMovgestTsFin getSiacTMovgestT() {
		return this.siacTMovgestT;
	}

	public void setSiacTMovgestT(SiacTMovgestTsFin siacTMovgestT) {
		this.siacTMovgestT = siacTMovgestT;
	}

	public SiacTMovgestTsDetFin getSiacTMovgestTsDet() {
		return this.siacTMovgestTsDet;
	}

	public void setSiacTMovgestTsDet(SiacTMovgestTsDetFin siacTMovgestTsDet) {
		this.siacTMovgestTsDet = siacTMovgestTsDet;
	}

	public Integer getMtdmReimputazioneAnno() {
		return mtdmReimputazioneAnno;
	}

	public void setMtdmReimputazioneAnno(Integer mtdmReimputazioneAnno) {
		this.mtdmReimputazioneAnno = mtdmReimputazioneAnno;
	}

	public Boolean getMtdmReimputazioneFlag() {
		return mtdmReimputazioneFlag;
	}

	public void setMtdmReimputazioneFlag(Boolean mtdmReimputazioneFlag) {
		this.mtdmReimputazioneFlag = mtdmReimputazioneFlag;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.movgestTsDetModId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.movgestTsDetModId = uid;
	}
}