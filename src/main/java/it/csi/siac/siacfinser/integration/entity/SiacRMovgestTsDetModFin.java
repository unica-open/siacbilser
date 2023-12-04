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
import it.csi.siac.siacfinser.integration.entity.base.SiacTEnteBase;


@Entity
@Table(name="siac_r_movgest_ts_det_mod")
public class SiacRMovgestTsDetModFin extends SiacTEnteBase {


	/** Per la serializzazione */
	private static final long serialVersionUID = -6006439623719562240L;


 
	@Id
	@SequenceGenerator(name="SEQ_FIN_SIAC_R_MOVGEST_TS_DET_MOD_GENERATOR", allocationSize=1, sequenceName="siac_r_movgest_ts_det_mod_movgest_ts_det_mod_r_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_FIN_SIAC_R_MOVGEST_TS_DET_MOD_GENERATOR")
	@Column(name="movgest_ts_det_mod_r_id")
	private Integer movgestTsDetModRId;
	
	
	@Column(name="movgest_ts_det_mod_importo")
	private BigDecimal movgestTsModImporto;
	
	
	@Column(name="movgest_ts_det_mod_impo_residuo")
	private BigDecimal movgestTsModImpoResiduo;

	// B = IMPEGNI
	@ManyToOne
	@JoinColumn(name="movgest_ts_det_mod_spesa_id")
	private SiacTMovgestTsDetModFin siacTMovgestTsDetModSpesa;
	
	
	// A = ACCERTAMENTI
	@ManyToOne
	@JoinColumn(name="movgest_ts_det_mod_entrata_id")
	private SiacTMovgestTsDetModFin siacTMovgestTsDetModEntrata;
	

	
	//
	public SiacRMovgestTsDetModFin() {
	}
	
	
	@Override
	public Integer getUid() {
		//  Auto-generated method stub
		return movgestTsDetModRId;
	}

	@Override
	public void setUid(Integer uid) {
		//  Auto-generated method stub
		this.movgestTsDetModRId = uid;
	}


	/**
	 * @return the movgestTsDetModRId
	 */
	public Integer getMovgestTsDetModRId() {
		return movgestTsDetModRId;
	}


	/**
	 * @param movgestTsDetModRId the movgestTsDetModRId to set
	 */
	public void setMovgestTsDetModRId(Integer movgestTsDetModRId) {
		this.movgestTsDetModRId = movgestTsDetModRId;
	}


	/**
	 * @return the movgestTsModImporto
	 */
	public BigDecimal getMovgestTsModImporto() {
		return movgestTsModImporto;
	}


	/**
	 * @param movgestTsModImporto the movgestTsModImporto to set
	 */
	public void setMovgestTsModImporto(BigDecimal movgestTsModImporto) {
		this.movgestTsModImporto = movgestTsModImporto;
	}


	/**
	 * @return the siacTMovgestTsDetModSpesa
	 */
	public SiacTMovgestTsDetModFin getSiacTMovgestTsDetModSpesa() {
		return siacTMovgestTsDetModSpesa;
	}


	/**
	 * @param siacTMovgestTsDetModSpesa the siacTMovgestTsDetModSpesa to set
	 */
	public void setSiacTMovgestTsDetModSpesa(SiacTMovgestTsDetModFin siacTMovgestTsDetModSpesa) {
		this.siacTMovgestTsDetModSpesa = siacTMovgestTsDetModSpesa;
	}


	/**
	 * @return the siacTMovgestTsDetModEntrata
	 */
	public SiacTMovgestTsDetModFin getSiacTMovgestTsDetModEntrata() {
		return siacTMovgestTsDetModEntrata;
	}


	/**
	 * @param siacTMovgestTsDetModEntrata the siacTMovgestTsDetModEntrata to set
	 */
	public void setSiacTMovgestTsDetModEntrata(SiacTMovgestTsDetModFin siacTMovgestTsDetModEntrata) {
		this.siacTMovgestTsDetModEntrata = siacTMovgestTsDetModEntrata;
	}


	/**
	 * @return the movgestTsModImpoResiduo
	 */
	public BigDecimal getMovgestTsModImpoResiduo() {
		return movgestTsModImpoResiduo;
	}


	/**
	 * @param movgestTsModImpoResiduo the movgestTsModImpoResiduo to set
	 */
	public void setMovgestTsModImpoResiduo(BigDecimal movgestTsModImpoResiduo) {
		this.movgestTsModImpoResiduo = movgestTsModImpoResiduo;
	}

 
 

}
