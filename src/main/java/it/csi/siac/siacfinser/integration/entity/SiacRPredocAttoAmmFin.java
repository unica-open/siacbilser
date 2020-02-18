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
 * The persistent class for the siac_r_predoc_atto_amm database table.
 * 
 */
@Entity
@Table(name="siac_r_predoc_atto_amm")
public class SiacRPredocAttoAmmFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="predoc_attoamm_id")
	private Integer predocAttoammId;

	//bi-directional many-to-one association to SiacTAttoAmmFin
	@ManyToOne
	@JoinColumn(name="attoamm_id")
	private SiacTAttoAmmFin siacTAttoAmm;

	//bi-directional many-to-one association to SiacTPredocFin
	@ManyToOne
	@JoinColumn(name="predoc_id")
	private SiacTPredocFin siacTPredoc;

	public SiacRPredocAttoAmmFin() {
	}

	public Integer getPredocAttoammId() {
		return this.predocAttoammId;
	}

	public void setPredocAttoammId(Integer predocAttoammId) {
		this.predocAttoammId = predocAttoammId;
	}

	public SiacTAttoAmmFin getSiacTAttoAmm() {
		return this.siacTAttoAmm;
	}

	public void setSiacTAttoAmm(SiacTAttoAmmFin siacTAttoAmm) {
		this.siacTAttoAmm = siacTAttoAmm;
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
		return this.predocAttoammId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.predocAttoammId = uid;
	}
}