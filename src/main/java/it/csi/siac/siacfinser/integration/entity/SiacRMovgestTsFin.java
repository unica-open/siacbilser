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

import it.csi.siac.siacfinser.integration.entity.base.SiacTEnteBase;


@Entity
@Table(name="siac_r_movgest_ts")
public class SiacRMovgestTsFin extends SiacTEnteBase {

	/** Per la serializzazione */
	private static final long serialVersionUID = -8318905170046222231L;



	@Id
	@Column(name = "movgest_ts_r_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_FIN_SIAC_R_MOVGEST_TS")
	@SequenceGenerator(name = "SEQ_FIN_SIAC_R_MOVGEST_TS", allocationSize=1, sequenceName = "siac_r_movgest_ts_movgest_ts_r_id_seq")
	private int movgestTsRId;

	
	
	@Column(name="movgest_ts_importo")
	private BigDecimal movgestTsImporto;

	// B = IMPEGNI
	@ManyToOne
	@JoinColumn(name="movgest_ts_b_id")
	private SiacTMovgestTsFin siacTMovgestTsB;
	
	
	// A = ACCERTAMENTI
	@ManyToOne
	@JoinColumn(name="movgest_ts_a_id")
	private SiacTMovgestTsFin siacTMovgestTsA;
	
	//FPV / Avanzo:
	@ManyToOne
	@JoinColumn(name="avav_id")
	private SiacTAvanzovincoloFin siacTAvanzovincoloFin;
	
	//
	
	
	public SiacRMovgestTsFin() {
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


	public int getMovgestTsRId() {
		return movgestTsRId;
	}


	public void setMovgestTsRId(int movgestTsRId) {
		this.movgestTsRId = movgestTsRId;
	}


	

	public BigDecimal getMovgestTsImporto() {
		return movgestTsImporto;
	}


	public void setMovgestTsImporto(BigDecimal movgestTsImporto) {
		this.movgestTsImporto = movgestTsImporto;
	}


	public SiacTMovgestTsFin getSiacTMovgestTsB() {
		return siacTMovgestTsB;
	}


	public void setSiacTMovgestTsB(SiacTMovgestTsFin siacTMovgestTsB) {
		this.siacTMovgestTsB = siacTMovgestTsB;
	}


	public SiacTMovgestTsFin getSiacTMovgestTsA() {
		return siacTMovgestTsA;
	}


	public void setSiacTMovgestTsA(SiacTMovgestTsFin siacTMovgestTsA) {
		this.siacTMovgestTsA = siacTMovgestTsA;
	}


	public SiacTAvanzovincoloFin getSiacTAvanzovincoloFin() {
		return siacTAvanzovincoloFin;
	}


	public void setSiacTAvanzovincoloFin(SiacTAvanzovincoloFin siacTAvanzovincoloFin) {
		this.siacTAvanzovincoloFin = siacTAvanzovincoloFin;
	}

}
