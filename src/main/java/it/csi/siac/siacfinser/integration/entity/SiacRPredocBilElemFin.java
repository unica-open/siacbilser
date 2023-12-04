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
 * The persistent class for the siac_r_predoc_bil_elem database table.
 * 
 */
@Entity
@Table(name="siac_r_predoc_bil_elem")
public class SiacRPredocBilElemFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="predoc_elem_id")
	private Integer predocElemId;

	//bi-directional many-to-one association to SiacTBilElemFin
	@ManyToOne
	@JoinColumn(name="elem_id")
	private SiacTBilElemFin siacTBilElem;

	//bi-directional many-to-one association to SiacTPredocFin
	@ManyToOne
	@JoinColumn(name="predoc_id")
	private SiacTPredocFin siacTPredoc;

	public SiacRPredocBilElemFin() {
	}

	public Integer getPredocElemId() {
		return this.predocElemId;
	}

	public void setPredocElemId(Integer predocElemId) {
		this.predocElemId = predocElemId;
	}

	public SiacTBilElemFin getSiacTBilElem() {
		return this.siacTBilElem;
	}

	public void setSiacTBilElem(SiacTBilElemFin siacTBilElem) {
		this.siacTBilElem = siacTBilElem;
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
		return this.predocElemId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.predocElemId = uid;
	}
}