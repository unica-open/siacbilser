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
 * The persistent class for the siac_r_causale_soggetto database table.
 * 
 */
@Entity
@Table(name="siac_r_causale_soggetto")
public class SiacRCausaleSoggettoFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="caus_soggetto_id")
	private Integer causSoggettoId;

	//bi-directional many-to-one association to SiacDCausaleFin
	@ManyToOne
	@JoinColumn(name="caus_id")
	private SiacDCausaleFin siacDCausale;

	//bi-directional many-to-one association to SiacTSoggettoFin
	@ManyToOne
	@JoinColumn(name="soggetto_id")
	private SiacTSoggettoFin siacTSoggetto;

	public SiacRCausaleSoggettoFin() {
	}

	public Integer getCausSoggettoId() {
		return this.causSoggettoId;
	}

	public void setCausSoggettoId(Integer causSoggettoId) {
		this.causSoggettoId = causSoggettoId;
	}

	public SiacDCausaleFin getSiacDCausale() {
		return this.siacDCausale;
	}

	public void setSiacDCausale(SiacDCausaleFin siacDCausale) {
		this.siacDCausale = siacDCausale;
	}

	public SiacTSoggettoFin getSiacTSoggetto() {
		return this.siacTSoggetto;
	}

	public void setSiacTSoggetto(SiacTSoggettoFin siacTSoggetto) {
		this.siacTSoggetto = siacTSoggetto;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.causSoggettoId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.causSoggettoId = uid;
	}

}