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


// TODO: Auto-generated Javadoc
/**
 * The persistent class for the siac_t_persona_fisica database table.
 * 
 */
@Entity
@Table(name="siac_t_persona_fisica")
@NamedQuery(name="SiacTPersonaFisica.findAll", query="SELECT s FROM SiacTPersonaFisica s")
public class SiacTPersonaFisica extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The perf id. */
	@Id
	@SequenceGenerator(name="SIAC_T_PERSONA_FISICA_PERFID_GENERATOR", allocationSize=1, sequenceName="SIAC_T_PERSONA_FISICA_PERF_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_PERSONA_FISICA_PERFID_GENERATOR")
	@Column(name="perf_id")
	private Integer perfId;

	/** The cognome. */
	private String cognome;

	/** The nascita data. */
	@Column(name="nascita_data")
	private Date nascitaData;

	/** The nome. */
	private String nome;

	/** The sesso. */
	private String sesso;

	//bi-directional many-to-one association to SiacTComune
	/** The siac t comune. */
	@ManyToOne
	@JoinColumn(name="comune_id_nascita")
	private SiacTComune siacTComune;

	//bi-directional many-to-one association to SiacTSoggetto
	/** The siac t soggetto. */
	@ManyToOne
	@JoinColumn(name="soggetto_id")
	private SiacTSoggetto siacTSoggetto;

	/**
	 * Instantiates a new siac t persona fisica.
	 */
	public SiacTPersonaFisica() {
	}

	/**
	 * Gets the perf id.
	 *
	 * @return the perf id
	 */
	public Integer getPerfId() {
		return this.perfId;
	}

	/**
	 * Sets the perf id.
	 *
	 * @param perfId the new perf id
	 */
	public void setPerfId(Integer perfId) {
		this.perfId = perfId;
	}

	/**
	 * Gets the cognome.
	 *
	 * @return the cognome
	 */
	public String getCognome() {
		return this.cognome;
	}

	/**
	 * Sets the cognome.
	 *
	 * @param cognome the new cognome
	 */
	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	/**
	 * Gets the nascita data.
	 *
	 * @return the nascita data
	 */
	public Date getNascitaData() {
		return this.nascitaData;
	}

	/**
	 * Sets the nascita data.
	 *
	 * @param nascitaData the new nascita data
	 */
	public void setNascitaData(Date nascitaData) {
		this.nascitaData = nascitaData;
	}

	/**
	 * Gets the nome.
	 *
	 * @return the nome
	 */
	public String getNome() {
		return this.nome;
	}

	/**
	 * Sets the nome.
	 *
	 * @param nome the new nome
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}

	/**
	 * Gets the sesso.
	 *
	 * @return the sesso
	 */
	public String getSesso() {
		return this.sesso;
	}

	/**
	 * Sets the sesso.
	 *
	 * @param sesso the new sesso
	 */
	public void setSesso(String sesso) {
		this.sesso = sesso;
	}

	/**
	 * Gets the siac t comune.
	 *
	 * @return the siac t comune
	 */
	public SiacTComune getSiacTComune() {
		return this.siacTComune;
	}

	/**
	 * Sets the siac t comune.
	 *
	 * @param siacTComune the new siac t comune
	 */
	public void setSiacTComune(SiacTComune siacTComune) {
		this.siacTComune = siacTComune;
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
		return perfId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.perfId = uid;
	}

}