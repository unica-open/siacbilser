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
 * The persistent class for the siac_r_gruppo_ruolo_op database table.
 * 
 */
@Entity
@Table(name="siac_r_gruppo_ruolo_op")
public class SiacRGruppoRuoloOpFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="gruppo_ruolo_op_id")
	private Integer gruppoRuoloOpId;

	//bi-directional many-to-one association to SiacDRuoloOpFin
	@ManyToOne
	@JoinColumn(name="ruolo_operativo_id")
	private SiacDRuoloOpFin siacDRuoloOp;

	//bi-directional many-to-one association to SiacTClassFin
	@ManyToOne
	@JoinColumn(name="classif_id")
	private SiacTClassFin siacTClass;

	//bi-directional many-to-one association to SiacTGruppoFin
	@ManyToOne
	@JoinColumn(name="gruppo_id")
	private SiacTGruppoFin siacTGruppo;

	public SiacRGruppoRuoloOpFin() {
	}

	public Integer getGruppoRuoloOpId() {
		return this.gruppoRuoloOpId;
	}

	public void setGruppoRuoloOpId(Integer gruppoRuoloOpId) {
		this.gruppoRuoloOpId = gruppoRuoloOpId;
	}

	public SiacDRuoloOpFin getSiacDRuoloOp() {
		return this.siacDRuoloOp;
	}

	public void setSiacDRuoloOp(SiacDRuoloOpFin siacDRuoloOp) {
		this.siacDRuoloOp = siacDRuoloOp;
	}

	public SiacTClassFin getSiacTClass() {
		return this.siacTClass;
	}

	public void setSiacTClass(SiacTClassFin siacTClass) {
		this.siacTClass = siacTClass;
	}

	public SiacTGruppoFin getSiacTGruppo() {
		return this.siacTGruppo;
	}

	public void setSiacTGruppo(SiacTGruppoFin siacTGruppo) {
		this.siacTGruppo = siacTGruppo;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.gruppoRuoloOpId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.gruppoRuoloOpId = uid;
	}
}