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
 * The persistent class for the siac_t_persona_giuridica database table.
 * 
 */
@Entity
@Table(name="siac_t_persona_giuridica")
public class SiacTPersonaGiuridicaFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_T_PERSONA_GIURIDICA_PERSONA_GIURIDICA_GENERATOR", allocationSize=1, sequenceName="siac_t_persona_giuridica_perg_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_PERSONA_GIURIDICA_PERSONA_GIURIDICA_GENERATOR")
	@Column(name="perg_id")
	private Integer pergId;

	@Column(name="ragione_sociale")
	private String ragioneSociale;

	//bi-directional many-to-one association to SiacTSoggettoFin
	// @OneToOne
	@ManyToOne
	@JoinColumn(name="soggetto_id")
	private SiacTSoggettoFin siacTSoggetto;

	public SiacTPersonaGiuridicaFin() {
	}

	public Integer getPergId() {
		return this.pergId;
	}

	public void setPergId(Integer pergId) {
		this.pergId = pergId;
	}

	public String getRagioneSociale() {
		return this.ragioneSociale;
	}

	public void setRagioneSociale(String ragioneSociale) {
		this.ragioneSociale = ragioneSociale;
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
		return this.pergId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.pergId = uid;
	}
}