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
 * The persistent class for the siac_r_doc_iva_sog database table.
 * 
 */
@Entity
@Table(name="siac_r_doc_iva_sog")
public class SiacRDocIvaSogFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="doc_sog_id")
	private Integer docSogId;

	//bi-directional many-to-one association to SiacDRuoloFin
	@ManyToOne
	@JoinColumn(name="ruolo_id")
	private SiacDRuoloFin siacDRuolo;

	//bi-directional many-to-one association to SiacTDocIvaFin
	@ManyToOne
	@JoinColumn(name="dociva_id")
	private SiacTDocIvaFin siacTDocIva;

	//bi-directional many-to-one association to SiacTSoggettoFin
	@ManyToOne
	@JoinColumn(name="soggetto_id")
	private SiacTSoggettoFin siacTSoggetto;

	public SiacRDocIvaSogFin() {
	}

	public Integer getDocSogId() {
		return this.docSogId;
	}

	public void setDocSogId(Integer docSogId) {
		this.docSogId = docSogId;
	}

	public SiacDRuoloFin getSiacDRuolo() {
		return this.siacDRuolo;
	}

	public void setSiacDRuolo(SiacDRuoloFin siacDRuolo) {
		this.siacDRuolo = siacDRuolo;
	}

	public SiacTDocIvaFin getSiacTDocIva() {
		return this.siacTDocIva;
	}

	public void setSiacTDocIva(SiacTDocIvaFin siacTDocIva) {
		this.siacTDocIva = siacTDocIva;
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
		return this.docSogId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.docSogId = uid;
	}

}