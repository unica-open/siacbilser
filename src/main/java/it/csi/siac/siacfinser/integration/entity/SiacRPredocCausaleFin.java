/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import it.csi.siac.siacfinser.integration.entity.base.SiacTEnteBase;


/**
 * The persistent class for the siac_r_predoc_causale database table.
 * 
 */
@Entity
@Table(name="siac_r_predoc_causale")
public class SiacRPredocCausaleFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="predoc_caus_id")
	private Integer predocCausId;

	//bi-directional many-to-one association to SiacDCausaleFin
	@ManyToOne
	@JoinColumn(name="caus_id")
	private SiacDCausaleFin siacDCausale;

	//bi-directional many-to-one association to SiacTPredocFin
	@ManyToOne
	@JoinColumn(name="predoc_id")
	private SiacTPredocFin siacTPredoc;

	public SiacRPredocCausaleFin() {
	}

	public Integer getPredocCausId() {
		return this.predocCausId;
	}

	public void setPredocCausId(Integer predocCausId) {
		this.predocCausId = predocCausId;
	}

	public SiacDCausaleFin getSiacDCausale() {
		return this.siacDCausale;
	}

	public void setSiacDCausale(SiacDCausaleFin siacDCausale) {
		this.siacDCausale = siacDCausale;
	}

	public SiacTPredocFin getSiacTPredoc() {
		return this.siacTPredoc;
	}

	public void setSiacTPredoc(SiacTPredocFin siacTPredoc) {
		this.siacTPredoc = siacTPredoc;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.predocCausId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.predocCausId = uid;
	}

}