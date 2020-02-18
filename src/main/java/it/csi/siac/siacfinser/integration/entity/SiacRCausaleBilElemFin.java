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
 * The persistent class for the siac_r_causale_bil_elem database table.
 * 
 */
@Entity
@Table(name="siac_r_causale_bil_elem")
public class SiacRCausaleBilElemFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="caus_elem_id")
	private Integer causElemId;

	//bi-directional many-to-one association to SiacDCausaleFin
	@ManyToOne
	@JoinColumn(name="caus_id")
	private SiacDCausaleFin siacDCausale;

	//bi-directional many-to-one association to SiacTBilElemFin
	@ManyToOne
	@JoinColumn(name="elem_id")
	private SiacTBilElemFin siacTBilElem;

	public SiacRCausaleBilElemFin() {
	}

	public Integer getCausElemId() {
		return this.causElemId;
	}

	public void setCausElemId(Integer causElemId) {
		this.causElemId = causElemId;
	}

	public SiacDCausaleFin getSiacDCausale() {
		return this.siacDCausale;
	}

	public void setSiacDCausale(SiacDCausaleFin siacDCausale) {
		this.siacDCausale = siacDCausale;
	}

	public SiacTBilElemFin getSiacTBilElem() {
		return this.siacTBilElem;
	}

	public void setSiacTBilElem(SiacTBilElemFin siacTBilElem) {
		this.siacTBilElem = siacTBilElem;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.causElemId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.causElemId = uid;
	}

}