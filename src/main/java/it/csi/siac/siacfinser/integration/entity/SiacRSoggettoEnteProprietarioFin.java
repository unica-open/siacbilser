/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import it.csi.siac.siacfinser.integration.entity.base.SiacTEnteBase;


// TODO: Auto-generated Javadoc
/**
 * The persistent class for the siac_r_soggetto_ente_proprietario database table.
 * 
 */
@Entity
@Table(name="siac_r_soggetto_ente_proprietario")
public class SiacRSoggettoEnteProprietarioFin extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The soggetto ente prop id. */
	@Id
	@SequenceGenerator(name="SIAC_R_SOGGETTO_ENTE_PROPRIETARIO_SOGGETTOENTEPROPID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_SOGGETTO_ENTE_PROPRIETARIO_SOGGETTO_ENTE_PROP_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_SOGGETTO_ENTE_PROPRIETARIO_SOGGETTOENTEPROPID_GENERATOR")
	@Column(name="soggetto_ente_prop_id")
	private Integer soggettoEntePropId;

	/** The siac t soggetto. */
	@ManyToOne
	@JoinColumn(name="soggetto_id")
	private SiacTSoggettoFin siacTSoggetto;

	@Override
	public Integer getUid() {
		return this.soggettoEntePropId;
	}

	@Override
	public void setUid(Integer uid) {
		this.soggettoEntePropId = uid;
	}

	public Integer getSoggettoEntePropId() {
		return soggettoEntePropId;
	}

	public void setSoggettoEntePropId(Integer soggettoEntePropId) {
		this.soggettoEntePropId = soggettoEntePropId;
	}

	public SiacTSoggettoFin getSiacTSoggetto() {
		return siacTSoggetto;
	}

	public void setSiacTSoggetto(SiacTSoggettoFin siacTSoggetto) {
		this.siacTSoggetto = siacTSoggetto;
	}

}