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
 * The persistent class for the siac_r_movgest_bil_elem database table.
 * 
 */
@Entity
@Table(name="siac_r_movgest_bil_elem")
public class SiacRMovgestBilElemFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_MOVGEST_BIL_ELEM_MOVGEST_ATTO_AMM_ID_GENERATOR", allocationSize=1, sequenceName="siac_r_movgest_bil_elem_movgest_atto_amm_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_MOVGEST_BIL_ELEM_MOVGEST_ATTO_AMM_ID_GENERATOR")
	@Column(name="movgest_atto_amm_id")
	private Integer movgestAttoAmmId;

	//bi-directional many-to-one association to SiacTBilElemFin
	@ManyToOne
	@JoinColumn(name="elem_id")
	private SiacTBilElemFin siacTBilElem;

	//bi-directional many-to-one association to SiacTMovgestFin
	@ManyToOne
	@JoinColumn(name="movgest_id")
	private SiacTMovgestFin siacTMovgest;

	public SiacRMovgestBilElemFin() {
	}

	public Integer getMovgestAttoAmmId() {
		return this.movgestAttoAmmId;
	}

	public void setMovgestAttoAmmId(Integer movgestAttoAmmId) {
		this.movgestAttoAmmId = movgestAttoAmmId;
	}

	public SiacTBilElemFin getSiacTBilElem() {
		return this.siacTBilElem;
	}

	public void setSiacTBilElem(SiacTBilElemFin siacTBilElem) {
		this.siacTBilElem = siacTBilElem;
	}

	public SiacTMovgestFin getSiacTMovgest() {
		return this.siacTMovgest;
	}

	public void setSiacTMovgest(SiacTMovgestFin siacTMovgest) {
		this.siacTMovgest = siacTMovgest;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.movgestAttoAmmId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.movgestAttoAmmId = uid;
	}
}