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

@Entity
@Table(name = "siac_r_movgest_ts_cronop_elem")
public class SiacRMovgestTsCronopElemFin extends SiacTEnteBase {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The cronop elem bil elem id. */
	@Id
	@SequenceGenerator(name = "siac_r_movgest_ts_cronop_elem_movgest_ts_cronop_elem_id_GENERATOR", allocationSize = 1, sequenceName = "siac_r_movgest_ts_cronop_elem_movgest_ts_cronop_elem_id_seq")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "siac_r_movgest_ts_cronop_elem_movgest_ts_cronop_elem_id_GENERATOR")
	@Column(name = "movgest_ts_cronop_elem_id")
	private Integer movgestTsCronopElemId;

	// bi-directional many-to-one association to SiacTBilElem
	/** The siac t bil elem. */
	@ManyToOne
	@JoinColumn(name = "movgest_ts_id")
	private SiacTMovgestTsFin siacTMovgestT;

	// bi-directional many-to-one association to SiacTCronopElem
	/** The siac t cronop elem. */
	@ManyToOne
	@JoinColumn(name = "cronop_elem_id")
	private SiacTCronopElemFin siacTCronopElem;

	// bi-directional many-to-one association to SiacTCronopElem
	/** The siac t cronop elem. */
	@ManyToOne
	@JoinColumn(name = "cronop_id")
	private SiacTCronopFin siacTCronop;

	/**
	 * Instantiates a new siac r cronop elem bil elem.
	 */
	public SiacRMovgestTsCronopElemFin() {
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return movgestTsCronopElemId;
	}

	@Override
	public void setUid(Integer uid) {
		movgestTsCronopElemId = uid;
	}

	public Integer getMovgestTsCronopElemId() {
		return movgestTsCronopElemId;
	}

	public void setMovgestTsCronopElemId(Integer movgestTsCronopElemId) {
		this.movgestTsCronopElemId = movgestTsCronopElemId;
	}


	public SiacTCronopElemFin getSiacTCronopElem() {
		return siacTCronopElem;
	}

	public void setSiacTCronopElem(SiacTCronopElemFin siacTCronopElem) {
		this.siacTCronopElem = siacTCronopElem;
	}

	public SiacTCronopFin getSiacTCronop() {
		return siacTCronop;
	}

	public void setSiacTCronop(SiacTCronopFin siacTCronop) {
		this.siacTCronop = siacTCronop;
	}

	public SiacTMovgestTsFin getSiacTMovgestT() {
		return siacTMovgestT;
	}

	public void setSiacTMovgestT(SiacTMovgestTsFin siacTMovgestT) {
		this.siacTMovgestT = siacTMovgestT;
	}



}