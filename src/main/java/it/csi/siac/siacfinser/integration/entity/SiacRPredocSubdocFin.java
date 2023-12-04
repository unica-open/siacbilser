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
 * The persistent class for the siac_r_predoc_subdoc database table.
 * 
 */
@Entity
@Table(name="siac_r_predoc_subdoc")
public class SiacRPredocSubdocFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="predoc_subdoc_id")
	private Integer predocSubdocId;

	//bi-directional many-to-one association to SiacTPredocFin
	@ManyToOne
	@JoinColumn(name="predoc_id")
	private SiacTPredocFin siacTPredoc;

	//bi-directional many-to-one association to SiacTSubdocFin
	@ManyToOne
	@JoinColumn(name="subdoc_id")
	private SiacTSubdocFin siacTSubdoc;

	public SiacRPredocSubdocFin() {
	}

	public Integer getPredocSubdocId() {
		return this.predocSubdocId;
	}

	public void setPredocSubdocId(Integer predocSubdocId) {
		this.predocSubdocId = predocSubdocId;
	}

	public SiacTPredocFin getSiacTPredoc() {
		return this.siacTPredoc;
	}

	public void setSiacTPredoc(SiacTPredocFin siacTPredoc) {
		this.siacTPredoc = siacTPredoc;
	}

	public SiacTSubdocFin getSiacTSubdoc() {
		return this.siacTSubdoc;
	}

	public void setSiacTSubdoc(SiacTSubdocFin siacTSubdoc) {
		this.siacTSubdoc = siacTSubdoc;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.predocSubdocId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.predocSubdocId = uid;
	}

}