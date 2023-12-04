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
 * The persistent class for the siac_r_subdoc_modpag database table.
 * 
 */
@Entity
@Table(name="siac_r_subdoc_modpag")
public class SiacRSubdocModpagFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="subdoc_modpag_id")
	private Integer subdocModpagId;

	//bi-directional many-to-one association to SiacRSoggrelModpagFin
	@ManyToOne
	@JoinColumn(name="soggrelmpag_id")
	private SiacRSoggrelModpagFin siacRSoggrelModpag;

	//bi-directional many-to-one association to SiacTModpagFin
	@ManyToOne
	@JoinColumn(name="modpag_id")
	private SiacTModpagFin siacTModpag;

	//bi-directional many-to-one association to SiacTSubdocFin
	@ManyToOne
	@JoinColumn(name="subdoc_id")
	private SiacTSubdocFin siacTSubdoc;

	public SiacRSubdocModpagFin() {
	}

	public Integer getSubdocModpagId() {
		return this.subdocModpagId;
	}

	public void setSubdocModpagId(Integer subdocModpagId) {
		this.subdocModpagId = subdocModpagId;
	}

	public SiacRSoggrelModpagFin getSiacRSoggrelModpag() {
		return this.siacRSoggrelModpag;
	}

	public void setSiacRSoggrelModpag(SiacRSoggrelModpagFin siacRSoggrelModpag) {
		this.siacRSoggrelModpag = siacRSoggrelModpag;
	}

	public SiacTModpagFin getSiacTModpag() {
		return this.siacTModpag;
	}

	public void setSiacTModpag(SiacTModpagFin siacTModpag) {
		this.siacTModpag = siacTModpag;
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
		return this.subdocModpagId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.subdocModpagId = uid;
	}

}