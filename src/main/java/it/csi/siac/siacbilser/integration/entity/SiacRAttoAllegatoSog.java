/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import java.util.Date;

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
 * The persistent class for the siac_r_atto_allegato_sog database table.
 * 
 */
@Entity
@Table(name="siac_r_atto_allegato_sog")
@NamedQuery(name="SiacRAttoAllegatoSog.findAll", query="SELECT s FROM SiacRAttoAllegatoSog s")
public class SiacRAttoAllegatoSog extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_ATTO_ALLEGATO_SOG_ATTOALSOGID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_ATTO_ALLEGATO_SOG_ATTOAL_SOG_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_ATTO_ALLEGATO_SOG_ATTOALSOGID_GENERATOR")
	@Column(name="attoal_sog_id")
	private Integer attoalSogId;

	@Column(name="attoal_sog_causale_sosp")
	private String attoalSogCausaleSosp;

	@Column(name="attoal_sog_data_riatt")
	private Date attoalSogDataRiatt;

	@Column(name="attoal_sog_data_sosp")
	private Date attoalSogDataSosp;

	//bi-directional many-to-one association to SiacTAttoAllegato
	@ManyToOne
	@JoinColumn(name="attoal_id")
	private SiacTAttoAllegato siacTAttoAllegato;

	//bi-directional many-to-one association to SiacTSoggetto
	@ManyToOne
	@JoinColumn(name="soggetto_id")
	private SiacTSoggetto siacTSoggetto;

	public SiacRAttoAllegatoSog() {
	}

	public Integer getAttoalSogId() {
		return this.attoalSogId;
	}

	public void setAttoalSogId(Integer attoalSogId) {
		this.attoalSogId = attoalSogId;
	}

	public String getAttoalSogCausaleSosp() {
		return this.attoalSogCausaleSosp;
	}

	public void setAttoalSogCausaleSosp(String attoalSogCausaleSosp) {
		this.attoalSogCausaleSosp = attoalSogCausaleSosp;
	}

	public Date getAttoalSogDataRiatt() {
		return this.attoalSogDataRiatt;
	}

	public void setAttoalSogDataRiatt(Date attoalSogDataRiatt) {
		this.attoalSogDataRiatt = attoalSogDataRiatt;
	}

	public Date getAttoalSogDataSosp() {
		return this.attoalSogDataSosp;
	}

	public void setAttoalSogDataSosp(Date attoalSogDataSosp) {
		this.attoalSogDataSosp = attoalSogDataSosp;
	}

	public SiacTAttoAllegato getSiacTAttoAllegato() {
		return this.siacTAttoAllegato;
	}

	public void setSiacTAttoAllegato(SiacTAttoAllegato siacTAttoAllegato) {
		this.siacTAttoAllegato = siacTAttoAllegato;
	}

	public SiacTSoggetto getSiacTSoggetto() {
		return this.siacTSoggetto;
	}

	public void setSiacTSoggetto(SiacTSoggetto siacTSoggetto) {
		this.siacTSoggetto = siacTSoggetto;
	}

	@Override
	public Integer getUid() {
		return attoalSogId;
	}

	@Override
	public void setUid(Integer uid) {
		this.attoalSogId = uid;
	}

}