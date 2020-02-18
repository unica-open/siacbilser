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


/**
 * The persistent class for the siac_r_movgest_ts database table.
 * 
 */
@Entity
@Table(name="SIAC_R_MOVGEST_TS_STORICO_IMP_ACC")
public class SiacRMovgestTsStoricoImpAccFin extends SiacTEnteBase {

	private static final long serialVersionUID = -1476990316873640790L;

	@Id
	@SequenceGenerator(name="SIAC_R_MOVGEST_TS_STORICO_IMP_ACCID_GENERATOR", allocationSize = 1, sequenceName="SIAC_R_MOVGEST_TS_STORICO_IMP_ACC_MOVGEST_TS_R_STORICO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_MOVGEST_TS_STORICO_IMP_ACCID_GENERATOR")
	@Column(name="movgest_ts_r_storico_id")
	private Integer movgestTsRStoricoId;

	//bi-directional many-to-one association to SiacTMovgestTsFin
	@ManyToOne
	@JoinColumn(name="movgest_ts_id")
	private SiacTMovgestTsFin siacTMovgestT;

	@Column(name="movgest_anno_acc")
	private Integer movgestAnnoAcc;

	@Column(name="movgest_numero_acc")
	private BigDecimal movgestNumeroAcc;

	@Column(name="movgest_subnumero_acc")
	private BigDecimal movgestSubnumeroAcc;

	/**
	 * @return the movgestTsRStoricoId
	 */
	public Integer getMovgestTsRStoricoId() {
		return movgestTsRStoricoId;
	}




	/**
	 * @param movgestTsRStoricoId the movgestTsRStoricoId to set
	 */
	public void setMovgestTsRStoricoId(Integer movgestTsRStoricoId) {
		this.movgestTsRStoricoId = movgestTsRStoricoId;
	}




	/**
	 * @return the siacTMovgestT
	 */
	public SiacTMovgestTsFin getSiacTMovgestT() {
		return siacTMovgestT;
	}




	/**
	 * @param siacTMovgestT the siacTMovgestT to set
	 */
	public void setSiacTMovgestT(SiacTMovgestTsFin siacTMovgestT) {
		this.siacTMovgestT = siacTMovgestT;
	}




	/**
	 * @return the movgestAnnoAcc
	 */
	public Integer getMovgestAnnoAcc() {
		return movgestAnnoAcc;
	}




	/**
	 * @param movgestAnnoAcc the movgestAnnoAcc to set
	 */
	public void setMovgestAnnoAcc(Integer movgestAnnoAcc) {
		this.movgestAnnoAcc = movgestAnnoAcc;
	}




	/**
	 * @return the movgestNumeroAcc
	 */
	public BigDecimal getMovgestNumeroAcc() {
		return movgestNumeroAcc;
	}




	/**
	 * @param movgestNumeroAcc the movgestNumeroAcc to set
	 */
	public void setMovgestNumeroAcc(BigDecimal movgestNumeroAcc) {
		this.movgestNumeroAcc = movgestNumeroAcc;
	}




	/**
	 * @return the movgestSubnumeroAcc
	 */
	public BigDecimal getMovgestSubnumeroAcc() {
		return movgestSubnumeroAcc;
	}




	/**
	 * @param movgestSubnumeroAcc the movgestSubnumeroAcc to set
	 */
	public void setMovgestSubnumeroAcc(BigDecimal movgestSubnumeroAcc) {
		this.movgestSubnumeroAcc = movgestSubnumeroAcc;
	}




	@Override
	public Integer getUid() {
		return movgestTsRStoricoId;
	}

	@Override
	public void setUid(Integer uid) {
		movgestTsRStoricoId = uid;
	}

}