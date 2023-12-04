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
 * The persistent class for the siac_r_onere_causale database table.
 * 
 */
@Entity
@Table(name="siac_r_onere_causale")
public class SiacROnereCausaleFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="onere_caus_id")
	private Integer onereCausId;

	//bi-directional many-to-one association to SiacDCausaleFin
	@ManyToOne
	@JoinColumn(name="caus_id")
	private SiacDCausaleFin siacDCausale;

	//bi-directional many-to-one association to SiacDOnereFin
	@ManyToOne
	@JoinColumn(name="onere_id")
	private SiacDOnereFin siacDOnere;

	public SiacROnereCausaleFin() {
	}

	public Integer getOnereCausId() {
		return this.onereCausId;
	}

	public void setOnereCausId(Integer onereCausId) {
		this.onereCausId = onereCausId;
	}

	public SiacDCausaleFin getSiacDCausale() {
		return this.siacDCausale;
	}

	public void setSiacDCausale(SiacDCausaleFin siacDCausale) {
		this.siacDCausale = siacDCausale;
	}

	public SiacDOnereFin getSiacDOnere() {
		return this.siacDOnere;
	}

	public void setSiacDOnere(SiacDOnereFin siacDOnere) {
		this.siacDOnere = siacDOnere;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.onereCausId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.onereCausId = uid;
	}

}