/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**
 * The persistent class for the siac_r_modpag_ordine database table.
 * 
 */
@Entity
@Table(name="siac_r_modpag_ordine")
@NamedQuery(name="SiacRModpagOrdine.findAll", query="SELECT s FROM SiacRModpagOrdine s")
public class SiacRModpagOrdine extends SiacTEnteBaseExt {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_MODPAG_ORDINE_MODPAGORDID_GENERATOR", allocationSize = 1, sequenceName="SIAC_R_MODPAG_ORDINE_MODPAGORD_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_MODPAG_ORDINE_MODPAGORDID_GENERATOR")
	@Column(name="modpagord_id")
	private Integer modpagordId;

	private Integer ordine;

	//bi-directional many-to-one association to SiacRSoggrelModpag
	@ManyToOne
	@JoinColumn(name="soggrelmpag_id")
	private SiacRSoggrelModpag siacRSoggrelModpag;

	//bi-directional many-to-one association to SiacTModpag
	@ManyToOne
	@JoinColumn(name="modpag_id")
	private SiacTModpag siacTModpag;

	//bi-directional many-to-one association to SiacTSoggetto
	@ManyToOne
	@JoinColumn(name="soggetto_id")
	private SiacTSoggetto siacTSoggetto;

	public SiacRModpagOrdine() {
	}

	public Integer getModpagordId() {
		return this.modpagordId;
	}

	public void setModpagordId(Integer modpagordId) {
		this.modpagordId = modpagordId;
	}

	public Integer getOrdine() {
		return this.ordine;
	}

	public void setOrdine(Integer ordine) {
		this.ordine = ordine;
	}

	public SiacRSoggrelModpag getSiacRSoggrelModpag() {
		return this.siacRSoggrelModpag;
	}

	public void setSiacRSoggrelModpag(SiacRSoggrelModpag siacRSoggrelModpag) {
		this.siacRSoggrelModpag = siacRSoggrelModpag;
	}

	public SiacTModpag getSiacTModpag() {
		return this.siacTModpag;
	}

	public void setSiacTModpag(SiacTModpag siacTModpag) {
		this.siacTModpag = siacTModpag;
	}

	public SiacTSoggetto getSiacTSoggetto() {
		return this.siacTSoggetto;
	}

	public void setSiacTSoggetto(SiacTSoggetto siacTSoggetto) {
		this.siacTSoggetto = siacTSoggetto;
	}

	@Override
	public Integer getUid() {
		return modpagordId;
	}

	@Override
	public void setUid(Integer uid) {
		modpagordId = uid;
	}
	
}