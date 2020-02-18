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
@Table(name="siac_r_movgest_ts")
public class SiacRMovgestTs extends SiacTEnteBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = -679356607269984316L;



	@Id
	@Column(name = "movgest_ts_r_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_FIN_SIAC_R_MOVGEST_TS")
	@SequenceGenerator(name = "SEQ_FIN_SIAC_R_MOVGEST_TS", allocationSize=1, sequenceName = "siac_r_movgest_ts_movgest_ts_r_id_seq")
	private Integer movgestTsRId;

	
	
	@Column(name="movgest_ts_importo")
	private BigDecimal movgestTsImporto;

	// B = IMPEGNI
	@ManyToOne
	@JoinColumn(name="movgest_ts_b_id")
	private SiacTMovgestT siacTMovgestTsB;
	
	
	// A = ACCERTAMENTI
	@ManyToOne
	@JoinColumn(name="movgest_ts_a_id")
	private SiacTMovgestT siacTMovgestTsA;
	
	
	public SiacRMovgestTs() {
	}
	
	
	@Override
	public Integer getUid() {
		//  Auto-generated method stub
		return movgestTsRId;
	}

	@Override
	public void setUid(Integer uid) {
		//  Auto-generated method stub
		this.movgestTsRId = uid;
	}


	public Integer getMovgestTsRId() {
		return movgestTsRId;
	}


	public void setMovgestTsRId(Integer movgestTsRId) {
		this.movgestTsRId = movgestTsRId;
	}


	

	public BigDecimal getMovgestTsImporto() {
		return movgestTsImporto;
	}


	public void setMovgestTsImporto(BigDecimal movgestTsImporto) {
		this.movgestTsImporto = movgestTsImporto;
	}


	public SiacTMovgestT getSiacTMovgestTsB() {
		return siacTMovgestTsB;
	}


	public void setSiacTMovgestTsB(SiacTMovgestT siacTMovgestTsB) {
		this.siacTMovgestTsB = siacTMovgestTsB;
	}


	public SiacTMovgestT getSiacTMovgestTsA() {
		return siacTMovgestTsA;
	}


	public void setSiacTMovgestTsA(SiacTMovgestT siacTMovgestTsA) {
		this.siacTMovgestTsA = siacTMovgestTsA;
	}


}
