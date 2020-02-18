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
 * The persistent class for the siac_r_causale_tipo database table.
 * 
 */
@Entity
@Table(name="siac_r_causale_tipo")
public class SiacRCausaleTipoFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="caus_tipo_r_id")
	private Integer causTipoRId;

	//bi-directional many-to-one association to SiacDCausaleFin
	@ManyToOne
	@JoinColumn(name="caus_id")
	private SiacDCausaleFin siacDCausale;

	//bi-directional many-to-one association to SiacDCausaleTipoFin
	@ManyToOne
	@JoinColumn(name="caus_tipo_id")
	private SiacDCausaleTipoFin siacDCausaleTipo;

	public SiacRCausaleTipoFin() {
	}

	public Integer getCausTipoRId() {
		return this.causTipoRId;
	}

	public void setCausTipoRId(Integer causTipoRId) {
		this.causTipoRId = causTipoRId;
	}

	public SiacDCausaleFin getSiacDCausale() {
		return this.siacDCausale;
	}

	public void setSiacDCausale(SiacDCausaleFin siacDCausale) {
		this.siacDCausale = siacDCausale;
	}

	public SiacDCausaleTipoFin getSiacDCausaleTipo() {
		return this.siacDCausaleTipo;
	}

	public void setSiacDCausaleTipo(SiacDCausaleTipoFin siacDCausaleTipo) {
		this.siacDCausaleTipo = siacDCausaleTipo;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.causTipoRId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.causTipoRId = uid;
	}

}