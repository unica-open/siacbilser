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
 * The persistent class for the siac_r_causale_modpag database table.
 * 
 */
@Entity
@Table(name="siac_r_causale_modpag")
public class SiacRCausaleModpagFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="caus_modpag_id")
	private Integer causModpagId;

	//bi-directional many-to-one association to SiacDCausaleFin
	@ManyToOne
	@JoinColumn(name="caus_id")
	private SiacDCausaleFin siacDCausale;

	//bi-directional many-to-one association to SiacTModpagFin
	@ManyToOne
	@JoinColumn(name="modpag_id")
	private SiacTModpagFin siacTModpag;

	public SiacRCausaleModpagFin() {
	}

	public Integer getCausModpagId() {
		return this.causModpagId;
	}

	public void setCausModpagId(Integer causModpagId) {
		this.causModpagId = causModpagId;
	}

	public SiacDCausaleFin getSiacDCausale() {
		return this.siacDCausale;
	}

	public void setSiacDCausale(SiacDCausaleFin siacDCausale) {
		this.siacDCausale = siacDCausale;
	}

	public SiacTModpagFin getSiacTModpag() {
		return this.siacTModpag;
	}

	public void setSiacTModpag(SiacTModpagFin siacTModpag) {
		this.siacTModpag = siacTModpag;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.causModpagId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.causModpagId = uid;
	}

}