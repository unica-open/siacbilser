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


/**
 * The persistent class for the siac_r_movgest_class database table.
 * 
 */
@Entity
@Table(name="siac_r_movgest_class")
public class SiacRMovgestClassFin extends SiacRClassBaseFin {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_MOVGEST_CLASS_ID_GENERATOR", allocationSize=1, sequenceName="siac_r_movgest_class_movgest_classif_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_MOVGEST_CLASS_ID_GENERATOR")
	@Column(name="movgest_classif_id")
	private Integer movgestClassifId;

	//bi-directional many-to-one association to SiacTMovgestT
	@ManyToOne
	@JoinColumn(name="movgest_ts_id")
	private SiacTMovgestTsFin siacTMovgestT;

	public SiacRMovgestClassFin() {
	}

	public Integer getMovgestClassifId() {
		return this.movgestClassifId;
	}

	public void setMovgestClassifId(Integer movgestClassifId) {
		this.movgestClassifId = movgestClassifId;
	}

	public SiacTMovgestTsFin getSiacTMovgestT() {
		return this.siacTMovgestT;
	}

	public void setSiacTMovgestT(SiacTMovgestTsFin siacTMovgestT) {
		this.siacTMovgestT = siacTMovgestT;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.movgestClassifId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.movgestClassifId = uid;
	}
}