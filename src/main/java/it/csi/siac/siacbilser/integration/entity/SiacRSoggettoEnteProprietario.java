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


// TODO: Auto-generated Javadoc
/**
 * The persistent class for the siac_r_soggetto_ente_proprietario database table.
 * 
 */
@Entity
@Table(name="siac_r_soggetto_ente_proprietario")
@NamedQuery(name="SiacRSoggettoEnteProprietario.findAll", query="SELECT s FROM SiacRSoggettoEnteProprietario s")
public class SiacRSoggettoEnteProprietario extends SiacTEnteBase {
	
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
	private SiacTSoggetto siacTSoggetto;

	/**
	 * Instantiates a new siac r soggetto ente proprietario.
	 */
	public SiacRSoggettoEnteProprietario() {
	}

	/**
	 * Gets the soggetto ente prop id.
	 *
	 * @return the soggetto ente prop id
	 */
	public Integer getSoggettoEntePropId() {
		return this.soggettoEntePropId;
	}

	/**
	 * Sets the soggetto ente prop id.
	 *
	 * @param soggettoEntePropId the new soggetto ente prop id
	 */
	public void setSoggettoEntePropId(Integer soggettoEntePropId) {
		this.soggettoEntePropId = soggettoEntePropId;
	}


	/**
	 * Gets the siac t soggetto.
	 *
	 * @return the siac t soggetto
	 */
	public SiacTSoggetto getSiacTSoggetto() {
		return this.siacTSoggetto;
	}

	/**
	 * Sets the siac t soggetto.
	 *
	 * @param siacTSoggetto the new siac t soggetto
	 */
	public void setSiacTSoggetto(SiacTSoggetto siacTSoggetto) {
		this.siacTSoggetto = siacTSoggetto;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return soggettoEntePropId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.soggettoEntePropId = uid;
	}

}