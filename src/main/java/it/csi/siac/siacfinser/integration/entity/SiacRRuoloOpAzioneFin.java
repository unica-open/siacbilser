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
 * The persistent class for the siac_r_ruolo_op_azione database table.
 * 
 */
@Entity
@Table(name="siac_r_ruolo_op_azione")
public class SiacRRuoloOpAzioneFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ruolo_op_azione_id")
	private Integer ruoloOpAzioneId;
	
	//bi-directional many-to-one association to SiacDRuoloFin
	@ManyToOne
	@JoinColumn(name="ruolo_op_id")
	private SiacDRuoloOpFin siacDRuoloOp;

	//bi-directional many-to-one association to SiacTAzioneFin
	@ManyToOne
	@JoinColumn(name="azione_id")
	private SiacTAzioneFin siacTAzione;

	public SiacRRuoloOpAzioneFin() {
	}

	public Integer getRuoloOpAzioneId() {
		return this.ruoloOpAzioneId;
	}

	public void setRuoloOpAzioneId(Integer ruoloOpAzioneId) {
		this.ruoloOpAzioneId = ruoloOpAzioneId;
	}

	public SiacTAzioneFin getSiacTAzione() {
		return this.siacTAzione;
	}

	public void setSiacTAzione(SiacTAzioneFin siacTAzione) {
		this.siacTAzione = siacTAzione;
	}

	@Override
	public Integer getUid() {
		return this.ruoloOpAzioneId;
	}

	@Override
	public void setUid(Integer uid) {
		this.ruoloOpAzioneId = uid;
	}

	public SiacDRuoloOpFin getSiacDRuoloOp() {
		return siacDRuoloOp;
	}

	public void setSiacDRuoloOp(SiacDRuoloOpFin siacDRuolo) {
		this.siacDRuoloOp = siacDRuolo;
	}

}