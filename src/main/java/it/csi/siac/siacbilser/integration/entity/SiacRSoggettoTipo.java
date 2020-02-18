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
 * The persistent class for the siac_r_soggetto_tipo database table.
 * 
 */
@Entity
@Table(name="siac_r_soggetto_tipo")
@NamedQuery(name="SiacRSoggettoTipo.findAll", query="SELECT s FROM SiacRSoggettoTipo s")
public class SiacRSoggettoTipo extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The soggetto r id. */
	@Id
	@SequenceGenerator(name="SIAC_R_SOGGETTO_TIPO_SOGGETTORID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_SOGGETTO_TIPO_SOGGETTO_R_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_SOGGETTO_TIPO_SOGGETTORID_GENERATOR")
	@Column(name="soggetto_r_id")
	private Integer soggettoRId;

	//bi-directional many-to-one association to SiacDSoggettoTipo
	/** The siac d soggetto tipo. */
	@ManyToOne
	@JoinColumn(name="soggetto_tipo_id")
	private SiacDSoggettoTipo siacDSoggettoTipo;

	//bi-directional many-to-one association to SiacTSoggetto
	/** The siac t soggetto. */
	@ManyToOne
	@JoinColumn(name="soggetto_id")
	private SiacTSoggetto siacTSoggetto;

	/**
	 * Instantiates a new siac r soggetto tipo.
	 */
	public SiacRSoggettoTipo() {
	}

	/**
	 * Gets the soggetto r id.
	 *
	 * @return the soggetto r id
	 */
	public Integer getSoggettoRId() {
		return this.soggettoRId;
	}

	/**
	 * Sets the soggetto r id.
	 *
	 * @param soggettoRId the new soggetto r id
	 */
	public void setSoggettoRId(Integer soggettoRId) {
		this.soggettoRId = soggettoRId;
	}	

	/**
	 * Gets the siac d soggetto tipo.
	 *
	 * @return the siac d soggetto tipo
	 */
	public SiacDSoggettoTipo getSiacDSoggettoTipo() {
		return this.siacDSoggettoTipo;
	}

	/**
	 * Sets the siac d soggetto tipo.
	 *
	 * @param siacDSoggettoTipo the new siac d soggetto tipo
	 */
	public void setSiacDSoggettoTipo(SiacDSoggettoTipo siacDSoggettoTipo) {
		this.siacDSoggettoTipo = siacDSoggettoTipo;
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
		return soggettoRId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.soggettoRId = uid;
	}

}