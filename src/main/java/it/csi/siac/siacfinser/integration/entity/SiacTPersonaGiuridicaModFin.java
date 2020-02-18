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




/**
 * The persistent class for the siac_t_persona_giuridica_mod database table.
 * 
 */
@Entity
@Table(name="siac_t_persona_giuridica_mod")
public class SiacTPersonaGiuridicaModFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_T_PERSONA_GIURIDICA_MOD_PERG_MOD_ID_GENERATOR", allocationSize=1, sequenceName="siac_t_persona_giuridica_mod_perg_mod_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_PERSONA_GIURIDICA_MOD_PERG_MOD_ID_GENERATOR")
	@Column(name="perg_mod_id")
	private Integer pergModId;

	@Column(name="ragione_sociale")
	private String ragioneSociale;

	//bi-directional many-to-one association to SiacTSoggettoFin
	@ManyToOne
	@JoinColumn(name="soggetto_id")
	private SiacTSoggettoFin siacTSoggetto;

	//bi-directional many-to-one association to SiacTSoggettoModFin
	// @OneToOne
	@ManyToOne
	@JoinColumn(name="sog_mod_id")
	private SiacTSoggettoModFin siacTSoggettoMod;

	public SiacTPersonaGiuridicaModFin() {
	}

	public Integer getPergModId() {
		return this.pergModId;
	}

	public void setPergModId(Integer pergModId) {
		this.pergModId = pergModId;
	}

	public String getRagioneSociale() {
		return this.ragioneSociale;
	}

	public void setRagioneSociale(String ragioneSociale) {
		this.ragioneSociale = ragioneSociale;
	}

//	public SiacTSoggettoFin getSiacTSoggetto() {
//		return this.siacTSoggetto;
//	}
//
//	public void setSiacTSoggetto(SiacTSoggettoFin siacTSoggetto) {
//		this.siacTSoggetto = siacTSoggetto;
//	}

//	public SiacTSoggettoModFin getSiacTSoggettoMod() {
//		return this.siacTSoggettoMod;
//	}
//
//	public void setSiacTSoggettoMod(SiacTSoggettoModFin siacTSoggettoMod) {
//		this.siacTSoggettoMod = siacTSoggettoMod;
//	}
	
	

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.pergModId;
	}

	public SiacTSoggettoFin getSiacTSoggetto() {
		return siacTSoggetto;
	}

	public void setSiacTSoggetto(SiacTSoggettoFin siacTSoggetto) {
		this.siacTSoggetto = siacTSoggetto;
	}

	public SiacTSoggettoModFin getSiacTSoggettoMod() {
		return siacTSoggettoMod;
	}

	public void setSiacTSoggettoMod(SiacTSoggettoModFin siacTSoggettoMod) {
		this.siacTSoggettoMod = siacTSoggettoMod;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.pergModId = uid;
	}
}