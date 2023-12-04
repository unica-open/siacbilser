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
 * The persistent class for the siac_r_richiesta_econ_sog database table.
 * 
 */
@Entity
@Table(name="siac_r_richiesta_econ_sog")
@NamedQuery(name="SiacRRichiestaEconSog.findAll", query="SELECT s FROM SiacRRichiestaEconSog s")
public class SiacRRichiestaEconSog extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_RICHIESTA_ECON_SOG_RICECONSOGID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_RICHIESTA_ECON_SOG_RICECONSOG_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_RICHIESTA_ECON_SOG_RICECONSOGID_GENERATOR")
	@Column(name="riceconsog_id")
	private Integer riceconsogId;

	//bi-directional many-to-one association to SiacTRichiestaEcon
	@ManyToOne
	@JoinColumn(name="ricecon_id")
	private SiacTRichiestaEcon siacTRichiestaEcon;

	//bi-directional many-to-one association to SiacTSoggetto
	@ManyToOne
	@JoinColumn(name="soggetto_id")
	private SiacTSoggetto siacTSoggetto;

	public SiacRRichiestaEconSog() {
	}

	public Integer getRiceconsogId() {
		return this.riceconsogId;
	}

	public void setRiceconsogId(Integer riceconsogId) {
		this.riceconsogId = riceconsogId;
	}

	public SiacTRichiestaEcon getSiacTRichiestaEcon() {
		return this.siacTRichiestaEcon;
	}

	public void setSiacTRichiestaEcon(SiacTRichiestaEcon siacTRichiestaEcon) {
		this.siacTRichiestaEcon = siacTRichiestaEcon;
	}

	public SiacTSoggetto getSiacTSoggetto() {
		return this.siacTSoggetto;
	}

	public void setSiacTSoggetto(SiacTSoggetto siacTSoggetto) {
		this.siacTSoggetto = siacTSoggetto;
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