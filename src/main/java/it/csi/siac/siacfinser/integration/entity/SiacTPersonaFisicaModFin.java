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
 * The persistent class for the siac_t_persona_fisica_mod database table.
 * 
 */
@Entity
@Table(name="siac_t_persona_fisica_mod")
public class SiacTPersonaFisicaModFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_T_PERSONA_FISICA_MOD_PERF_MOD_ID_GENERATOR", allocationSize=1, sequenceName="siac_t_persona_fisica_mod_perf_mod_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_PERSONA_FISICA_MOD_PERF_MOD_ID_GENERATOR")
	@Column(name="perf_mod_id")
	private Integer perfModId;

	private String cognome;

	@Column(name="nascita_data")
	private Timestamp nascitaData;

	private String nome;

	private String sesso;

	//bi-directional many-to-one association to SiacTComuneFin
	@ManyToOne
	@JoinColumn(name="comune_id_nascita")
	private SiacTComuneFin siacTComune;

//	//bi-directional many-to-one association to SiacTSoggettoFin
	@ManyToOne
	@JoinColumn(name="soggetto_id")
	private SiacTSoggettoFin siacTSoggetto;
//
//	//bi-directional many-to-one association to SiacTSoggettoModFin
	
	
	// @OneToOne
	@ManyToOne
	@JoinColumn(name="sog_mod_id")
	private SiacTSoggettoModFin siacTSoggettoMod;

	public SiacTPersonaFisicaModFin() {
	}

	public Integer getPerfModId() {
		return this.perfModId;
	}

	public void setPerfModId(Integer perfModId) {
		this.perfModId = perfModId;
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

	public SiacTComuneFin getSiacTComune() {
		return this.siacTComune;
	}

	public void setSiacTComune(SiacTComuneFin siacTComune) {
		this.siacTComune = siacTComune;
	}

	public SiacTSoggettoFin getSiacTSoggetto() {
		return this.siacTSoggetto;
	}

	public void setSiacTSoggetto(SiacTSoggettoFin siacTSoggetto) {
		this.siacTSoggetto = siacTSoggetto;
	}

	public SiacTSoggettoModFin getSiacTSoggettoMod() {
		return this.siacTSoggettoMod;
	}

	public void setSiacTSoggettoMod(SiacTSoggettoModFin siacTSoggettoMod) {
		this.siacTSoggettoMod = siacTSoggettoMod;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.perfModId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.perfModId = uid;
	}
}