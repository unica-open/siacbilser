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
 * The persistent class for the siac_r_ivamov database table.
 * 
 */
@Entity
@Table(name="siac_r_ivamov")
public class SiacRIvamovFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="subdocivamov_id")
	private Integer subdocivamovId;

	//bi-directional many-to-one association to SiacTIvamovFin
	@ManyToOne
	@JoinColumn(name="ivamov_id")
	private SiacTIvamovFin siacTIvamov;

	//bi-directional many-to-one association to SiacTSubdocIvaFin
	@ManyToOne
	@JoinColumn(name="subdociva_id")
	private SiacTSubdocIvaFin siacTSubdocIva;

	public SiacRIvamovFin() {
	}

	public Integer getSubdocivamovId() {
		return this.subdocivamovId;
	}

	public void setSubdocivamovId(Integer subdocivamovId) {
		this.subdocivamovId = subdocivamovId;
	}

	public SiacTIvamovFin getSiacTIvamov() {
		return this.siacTIvamov;
	}

	public void setSiacTIvamov(SiacTIvamovFin siacTIvamov) {
		this.siacTIvamov = siacTIvamov;
	}

	public SiacTSubdocIvaFin getSiacTSubdocIva() {
		return this.siacTSubdocIva;
	}

	public void setSiacTSubdocIva(SiacTSubdocIvaFin siacTSubdocIva) {
		this.siacTSubdocIva = siacTSubdocIva;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.subdocivamovId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.subdocivamovId = uid;
	}

}