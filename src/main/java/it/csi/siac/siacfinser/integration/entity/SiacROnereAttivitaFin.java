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
 * The persistent class for the siac_r_onere_attivita database table.
 * 
 */
@Entity
@Table(name="siac_r_onere_attivita")
public class SiacROnereAttivitaFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="onere_att_r_id")
	private Integer onereAttRId;

	//bi-directional many-to-one association to SiacDOnereFin
	@ManyToOne
	@JoinColumn(name="onere_id")
	private SiacDOnereFin siacDOnere;

	//bi-directional many-to-one association to SiacDOnereAttivitaFin
	@ManyToOne
	@JoinColumn(name="onere_att_id")
	private SiacDOnereAttivitaFin siacDOnereAttivita;

	public SiacROnereAttivitaFin() {
	}

	public Integer getOnereAttRId() {
		return this.onereAttRId;
	}

	public void setOnereAttRId(Integer onereAttRId) {
		this.onereAttRId = onereAttRId;
	}

	public SiacDOnereFin getSiacDOnere() {
		return this.siacDOnere;
	}

	public void setSiacDOnere(SiacDOnereFin siacDOnere) {
		this.siacDOnere = siacDOnere;
	}

	public SiacDOnereAttivitaFin getSiacDOnereAttivita() {
		return this.siacDOnereAttivita;
	}

	public void setSiacDOnereAttivita(SiacDOnereAttivitaFin siacDOnereAttivita) {
		this.siacDOnereAttivita = siacDOnereAttivita;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.onereAttRId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.onereAttRId = uid;
	}

}