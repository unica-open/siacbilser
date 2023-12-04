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
 * The persistent class for the siac_r_attr_entita database table.
 * 
 */
@Entity
@Table(name="siac_r_attr_entita")
public class SiacRAttrEntitaFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="attr_entita_id")
	private Integer attrEntitaId;

	//bi-directional many-to-one association to SiacDEntitaFin
	@ManyToOne
	@JoinColumn(name="entita_id")
	private SiacDEntitaFin siacDEntita;

	//bi-directional many-to-one association to SiacTAttrFin
	@ManyToOne
	@JoinColumn(name="attr_id")
	private SiacTAttrFin siacTAttr;

	public SiacRAttrEntitaFin() {
	}

	public Integer getAttrEntitaId() {
		return this.attrEntitaId;
	}

	public void setAttrEntitaId(Integer attrEntitaId) {
		this.attrEntitaId = attrEntitaId;
	}

	public SiacDEntitaFin getSiacDEntita() {
		return this.siacDEntita;
	}

	public void setSiacDEntita(SiacDEntitaFin siacDEntita) {
		this.siacDEntita = siacDEntita;
	}

	public SiacTAttrFin getSiacTAttr() {
		return this.siacTAttr;
	}

	public void setSiacTAttr(SiacTAttrFin siacTAttr) {
		this.siacTAttr = siacTAttr;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.attrEntitaId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.attrEntitaId = uid;
	}
}