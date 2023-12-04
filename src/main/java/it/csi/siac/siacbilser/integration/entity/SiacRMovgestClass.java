/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**
 * The persistent class for the siac_r_movgest_class database table.
 * 
 */
@Entity
@Table(name="siac_r_movgest_class")
@NamedQuery(name="SiacRMovgestClass.findAll", query="SELECT s FROM SiacRMovgestClass s")
public class SiacRMovgestClass extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_MOVGEST_CLASS_MOVGESTCLASSIFID_GENERATOR", allocationSize = 1, sequenceName="SIAC_R_MOVGEST_CLASS_MOVGEST_CLASSIF_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_MOVGEST_CLASS_MOVGESTCLASSIFID_GENERATOR")
	@Column(name="movgest_classif_id")
	private Integer movgestClassifId;

	//bi-directional many-to-one association to SiacTClass
	@ManyToOne
	@JoinColumn(name="classif_id")
	private SiacTClass siacTClass;

	//bi-directional many-to-one association to SiacTMovgestT
	@ManyToOne
	@JoinColumn(name="movgest_ts_id")
	private SiacTMovgestT siacTMovgestT;

	public SiacRMovgestClass() {
	}

	public Integer getMovgestClassifId() {
		return this.movgestClassifId;
	}

	public void setMovgestClassifId(Integer movgestClassifId) {
		this.movgestClassifId = movgestClassifId;
	}

	public SiacTClass getSiacTClass() {
		return this.siacTClass;
	}

	public void setSiacTClass(SiacTClass siacTClass) {
		this.siacTClass = siacTClass;
	}

	public SiacTMovgestT getSiacTMovgestT() {
		return this.siacTMovgestT;
	}

	public void setSiacTMovgestT(SiacTMovgestT siacTMovgestT) {
		this.siacTMovgestT = siacTMovgestT;
	}

	@Override
	public Integer getUid() {
		return movgestClassifId;
	}

	@Override
	public void setUid(Integer uid) {
		movgestClassifId = uid;
	}

}