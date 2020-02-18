/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import it.csi.siac.siacfinser.integration.entity.base.SiacTEnteBase;


/**
 * The persistent class for the siac_r_class_attr database table.
 * 
 */
@Entity
@Table(name="siac_r_class_attr")
public class SiacRClassAttrFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="class_attr_id")
	private Integer classAttrId;

	@Column(name="boolean")
	private String boolean_;

	private BigDecimal numerico;

	private BigDecimal percentuale;

	@Column(name="tabella_id")
	private Integer tabellaId;

	private String testo;

	//bi-directional many-to-one association to SiacTAttrFin
	@ManyToOne
	@JoinColumn(name="attr_id")
	private SiacTAttrFin siacTAttr;

	//bi-directional many-to-one association to SiacTClassFin
	@ManyToOne
	@JoinColumn(name="classif_id")
	private SiacTClassFin siacTClass;

	public SiacRClassAttrFin() {
	}

	public Integer getClassAttrId() {
		return this.classAttrId;
	}

	public void setClassAttrId(Integer classAttrId) {
		this.classAttrId = classAttrId;
	}

	public String getBoolean_() {
		return this.boolean_;
	}

	public void setBoolean_(String boolean_) {
		this.boolean_ = boolean_;
	}

	public BigDecimal getNumerico() {
		return this.numerico;
	}

	public void setNumerico(BigDecimal numerico) {
		this.numerico = numerico;
	}

	public BigDecimal getPercentuale() {
		return this.percentuale;
	}

	public void setPercentuale(BigDecimal percentuale) {
		this.percentuale = percentuale;
	}

	public Integer getTabellaId() {
		return this.tabellaId;
	}

	public void setTabellaId(Integer tabellaId) {
		this.tabellaId = tabellaId;
	}

	public String getTesto() {
		return this.testo;
	}

	public void setTesto(String testo) {
		this.testo = testo;
	}

	public SiacTAttrFin getSiacTAttr() {
		return this.siacTAttr;
	}

	public void setSiacTAttr(SiacTAttrFin siacTAttr) {
		this.siacTAttr = siacTAttr;
	}

	public SiacTClassFin getSiacTClass() {
		return this.siacTClass;
	}

	public void setSiacTClass(SiacTClassFin siacTClass) {
		this.siacTClass = siacTClass;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.classAttrId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.classAttrId = uid;
	}
}