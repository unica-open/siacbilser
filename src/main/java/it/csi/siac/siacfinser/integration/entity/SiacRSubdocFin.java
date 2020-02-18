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
 * The persistent class for the siac_r_subdoc database table.
 * 
 */
@Entity
@Table(name="siac_r_subdoc")
public class SiacRSubdocFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="subdoc_r_id")
	private Integer subdocRId;

	//bi-directional many-to-one association to SiacTSubdocFin
	@ManyToOne
	@JoinColumn(name="subdoc_b_id")
	private SiacTSubdocFin siacTSubdoc1;

	//bi-directional many-to-one association to SiacTSubdocFin
	@ManyToOne
	@JoinColumn(name="subdoc_a_id")
	private SiacTSubdocFin siacTSubdoc2;

	public SiacRSubdocFin() {
	}

	public Integer getSubdocRId() {
		return this.subdocRId;
	}

	public void setSubdocRId(Integer subdocRId) {
		this.subdocRId = subdocRId;
	}

	public SiacTSubdocFin getSiacTSubdoc1() {
		return this.siacTSubdoc1;
	}

	public void setSiacTSubdoc1(SiacTSubdocFin siacTSubdoc1) {
		this.siacTSubdoc1 = siacTSubdoc1;
	}

	public SiacTSubdocFin getSiacTSubdoc2() {
		return this.siacTSubdoc2;
	}

	public void setSiacTSubdoc2(SiacTSubdocFin siacTSubdoc2) {
		this.siacTSubdoc2 = siacTSubdoc2;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.subdocRId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.subdocRId = uid;
	}

}