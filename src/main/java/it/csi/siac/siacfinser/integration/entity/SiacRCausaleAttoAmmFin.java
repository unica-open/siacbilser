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
 * The persistent class for the siac_r_causale_atto_amm database table.
 * 
 */
@Entity
@Table(name="siac_r_causale_atto_amm")
public class SiacRCausaleAttoAmmFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="caus_attoamm_id")
	private Integer causAttoammId;

	//bi-directional many-to-one association to SiacDCausaleFin
	@ManyToOne
	@JoinColumn(name="caus_id")
	private SiacDCausaleFin siacDCausale;

	//bi-directional many-to-one association to SiacTAttoAmmFin
	@ManyToOne
	@JoinColumn(name="attoamm_id")
	private SiacTAttoAmmFin siacTAttoAmm;

	public SiacRCausaleAttoAmmFin() {
	}

	public Integer getCausAttoammId() {
		return this.causAttoammId;
	}

	public void setCausAttoammId(Integer causAttoammId) {
		this.causAttoammId = causAttoammId;
	}

	public SiacDCausaleFin getSiacDCausale() {
		return this.siacDCausale;
	}

	public void setSiacDCausale(SiacDCausaleFin siacDCausale) {
		this.siacDCausale = siacDCausale;
	}

	public SiacTAttoAmmFin getSiacTAttoAmm() {
		return this.siacTAttoAmm;
	}

	public void setSiacTAttoAmm(SiacTAttoAmmFin siacTAttoAmm) {
		this.siacTAttoAmm = siacTAttoAmm;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.causAttoammId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.causAttoammId = uid;
	}

}