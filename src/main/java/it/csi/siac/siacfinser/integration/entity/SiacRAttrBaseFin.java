/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity;

import java.math.BigDecimal;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import it.csi.siac.siacfinser.integration.entity.base.SiacTEnteBase;


@MappedSuperclass
public abstract class SiacRAttrBaseFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Basic
	@Column(name="boolean")
	private String boolean_;

	@Basic
	private BigDecimal numerico;

	@Basic
	private BigDecimal percentuale;

	@Basic
	@Column(name="tabella_id")
	private Integer tabellaId;

	@Basic
	private String testo;

	//bi-directional many-to-one association to SiacTAttrFin
	@ManyToOne
	@JoinColumn(name="attr_id")
	private SiacTAttrFin siacTAttr;

	public String getBoolean_() {
		return boolean_;
	}

	public void setBoolean_(String boolean_) {
		this.boolean_ = boolean_;
	}

	public BigDecimal getNumerico() {
		return numerico;
	}

	public void setNumerico(BigDecimal numerico) {
		this.numerico = numerico;
	}

	public BigDecimal getPercentuale() {
		return percentuale;
	}

	public void setPercentuale(BigDecimal percentuale) {
		this.percentuale = percentuale;
	}

	public Integer getTabellaId() {
		return tabellaId;
	}

	public void setTabellaId(Integer tabellaId) {
		this.tabellaId = tabellaId;
	}

	public String getTesto() {
		return testo;
	}

	public void setTesto(String testo) {
		this.testo = testo;
	}

	public SiacTAttrFin getSiacTAttr() {
		return siacTAttr;
	}

	public void setSiacTAttr(SiacTAttrFin siacTAttr) {
		this.siacTAttr = siacTAttr;
	}

}