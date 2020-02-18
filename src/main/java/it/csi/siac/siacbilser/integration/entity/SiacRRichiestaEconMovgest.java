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
 * The persistent class for the siac_r_richiesta_econ_movgest database table.
 * 
 */
@Entity
@Table(name="siac_r_richiesta_econ_movgest")
@NamedQuery(name="SiacRRichiestaEconMovgest.findAll", query="SELECT s FROM SiacRRichiestaEconMovgest s")
public class SiacRRichiestaEconMovgest extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_RICHIESTA_ECON_MOVGEST_RICECONSOGID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_RICHIESTA_ECON_MOVGEST_RICECONSOG_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_RICHIESTA_ECON_MOVGEST_RICECONSOGID_GENERATOR")
	@Column(name="riceconsog_id")
	private Integer riceconsogId;

	//bi-directional many-to-one association to SiacTMovgestT
	@ManyToOne
	@JoinColumn(name="movgest_ts_id")
	private SiacTMovgestT siacTMovgestT;

	//bi-directional many-to-one association to SiacTRichiestaEcon
	@ManyToOne
	@JoinColumn(name="ricecon_id")
	private SiacTRichiestaEcon siacTRichiestaEcon;

	public SiacRRichiestaEconMovgest() {
	}

	public Integer getRiceconsogId() {
		return this.riceconsogId;
	}

	public void setRiceconsogId(Integer riceconsogId) {
		this.riceconsogId = riceconsogId;
	}

	public SiacTMovgestT getSiacTMovgestT() {
		return this.siacTMovgestT;
	}

	public void setSiacTMovgestT(SiacTMovgestT siacTMovgestT) {
		this.siacTMovgestT = siacTMovgestT;
	}

	public SiacTRichiestaEcon getSiacTRichiestaEcon() {
		return this.siacTRichiestaEcon;
	}

	public void setSiacTRichiestaEcon(SiacTRichiestaEcon siacTRichiestaEcon) {
		this.siacTRichiestaEcon = siacTRichiestaEcon;
	}
	
	@Override
	public Integer getUid() {
		return riceconsogId;
	}

	@Override
	public void setUid(Integer uid) {
		this.riceconsogId = uid;
	}

}