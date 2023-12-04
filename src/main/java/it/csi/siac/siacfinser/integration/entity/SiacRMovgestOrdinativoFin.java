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
 * The persistent class for the siac_r_movgest_ordinativo database table.
 * 
 */
@Entity
@Table(name="siac_r_movgest_ordinativo")
public class SiacRMovgestOrdinativoFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_MOVGEST_ORDINATIVO_ID_GENERATOR", allocationSize=1, sequenceName="siac_r_movgest_ordinativo_movgest_ord_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_MOVGEST_ORDINATIVO_ID_GENERATOR")
	@Column(name="movgest_ord_id")
	private Integer movgestOrdId;

	//bi-directional many-to-one association to SiacTMovgestFin
	@ManyToOne
	@JoinColumn(name="movgest_id")
	private SiacTMovgestFin siacTMovgest;

	//bi-directional many-to-one association to SiacTOrdinativoFin
	@ManyToOne
	@JoinColumn(name="ord_id")
	private SiacTOrdinativoFin siacTOrdinativo;

	
	public SiacRMovgestOrdinativoFin() {
	}

	public Integer getMovgestOrdId() {
		return this.movgestOrdId;
	}

	public void setMovgestOrdId(Integer movgestOrdId) {
		this.movgestOrdId = movgestOrdId;
	}

	public SiacTMovgestFin getSiacTMovgest() {
		return this.siacTMovgest;
	}

	public void setSiacTMovgest(SiacTMovgestFin siacTMovgest) {
		this.siacTMovgest = siacTMovgest;
	}

	public SiacTOrdinativoFin getSiacTOrdinativo() {
		return this.siacTOrdinativo;
	}

	public void setSiacTOrdinativo(SiacTOrdinativoFin siacTOrdinativo) {
		this.siacTOrdinativo = siacTOrdinativo;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.movgestOrdId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.movgestOrdId = uid;
	}
}