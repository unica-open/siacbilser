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
 * The persistent class for the siac_r_subdoc_sog database table.
 * 
 */
@Entity
@Table(name="siac_r_subdoc_sog")
public class SiacRSubdocSogFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="subdoc_sog_id")
	private Integer subdocSogId;

	//bi-directional many-to-one association to SiacDRuoloFin
	@ManyToOne
	@JoinColumn(name="ruolo_id")
	private SiacDRuoloFin siacDRuolo;

	//bi-directional many-to-one association to SiacTSoggettoFin
	@ManyToOne
	@JoinColumn(name="soggetto_id")
	private SiacTSoggettoFin siacTSoggetto;

	//bi-directional many-to-one association to SiacTSubdocFin
	@ManyToOne
	@JoinColumn(name="subdoc_id")
	private SiacTSubdocFin siacTSubdoc;

	public SiacRSubdocSogFin() {
	}

	public Integer getSubdocSogId() {
		return this.subdocSogId;
	}

	public void setSubdocSogId(Integer subdocSogId) {
		this.subdocSogId = subdocSogId;
	}

	public SiacDRuoloFin getSiacDRuolo() {
		return this.siacDRuolo;
	}

	public void setSiacDRuolo(SiacDRuoloFin siacDRuolo) {
		this.siacDRuolo = siacDRuolo;
	}

	public SiacTSoggettoFin getSiacTSoggetto() {
		return this.siacTSoggetto;
	}

	public void setSiacTSoggetto(SiacTSoggettoFin siacTSoggetto) {
		this.siacTSoggetto = siacTSoggetto;
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
		return this.subdocSogId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.subdocSogId = uid;
	}

}