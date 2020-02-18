/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity;

import java.sql.Timestamp;

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


/**
 * The persistent class for the siac_t_persona_fisica database table.
 * 
 */
@Entity
@Table(name="siac_t_persona_fisica")
public class SiacTPersonaFisicaFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_T_PERSONA_FISICA_PERSONA_FISICA_GENERATOR", allocationSize=1, sequenceName="siac_t_persona_fisica_perf_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_PERSONA_FISICA_PERSONA_FISICA_GENERATOR")
	@Column(name="perf_id")
	private Integer perfId;

	private String cognome;

	//@Column(name="comune_id_nascita")
	//private Integer comuneIdNascita;

	@Column(name="nascita_data")
	private Timestamp nascitaData;

	private String nome;

	private String sesso;

	//bi-directional many-to-one association to SiacTSoggettoFin
	// @OneToOne
	@ManyToOne
	@JoinColumn(name="soggetto_id")
	private SiacTSoggettoFin siacTSoggetto;

	//bi-directional many-to-one association to SiacTComuneFin
	@ManyToOne
	@JoinColumn(name="comune_id_nascita")
	private SiacTComuneFin siacTComune;
	
	public SiacTComuneFin getSiacTComune() {
		return siacTComune;
	}

	public void setSiacTComune(SiacTComuneFin siacTComune) {
		this.siacTComune = siacTComune;
	}

	public SiacTPersonaFisicaFin() {
	}

	public Integer getPerfId() {
		return this.perfId;
	}

	public void setPerfId(Integer perfId) {
		this.perfId = perfId;
	}

	public String getCognome() {
		return this.cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	
	public Timestamp getNascitaData() {
		return this.nascitaData;
	}

	public void setNascitaData(Timestamp nascitaData) {
		this.nascitaData = nascitaData;
	}

	public String getNome() {
		return this.nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getSesso() {
		return this.sesso;
	}

	public void setSesso(String sesso) {
		this.sesso = sesso;
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
		return this.perfId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.perfId = uid;
	}

}