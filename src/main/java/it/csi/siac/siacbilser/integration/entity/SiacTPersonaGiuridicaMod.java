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
 * The persistent class for the siac_t_persona_giuridica_mod database table.
 * 
 */
@Entity
@Table(name="siac_t_persona_giuridica_mod")
@NamedQuery(name="SiacTPersonaGiuridicaMod.findAll", query="SELECT s FROM SiacTPersonaGiuridicaMod s")
public class SiacTPersonaGiuridicaMod extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The perg mod id. */
	@Id
	@SequenceGenerator(name="SIAC_T_PERSONA_GIURIDICA_MOD_PERGMODID_GENERATOR", allocationSize=1, sequenceName="SIAC_T_PERSONA_GIURIDICA_MOD_PERG_MOD_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_PERSONA_GIURIDICA_MOD_PERGMODID_GENERATOR")
	@Column(name="perg_mod_id")
	private Integer pergModId;

	/** The ragione sociale. */
	@Column(name="ragione_sociale")
	private String ragioneSociale;


	//bi-directional many-to-one association to SiacTSoggetto
	/** The siac t soggetto. */
	@ManyToOne
	@JoinColumn(name="soggetto_id")
	private SiacTSoggetto siacTSoggetto;

	//bi-directional many-to-one association to SiacTSoggettoMod
	/** The siac t soggetto mod. */
	@ManyToOne
	@JoinColumn(name="sog_mod_id")
	private SiacTSoggettoMod siacTSoggettoMod;

	/**
	 * Instantiates a new siac t persona giuridica mod.
	 */
	public SiacTPersonaGiuridicaMod() {
	}

	/**
	 * Gets the perg mod id.
	 *
	 * @return the perg mod id
	 */
	public Integer getPergModId() {
		return this.pergModId;
	}

	/**
	 * Sets the perg mod id.
	 *
	 * @param pergModId the new perg mod id
	 */
	public void setPergModId(Integer pergModId) {
		this.pergModId = pergModId;
	}

	/**
	 * Gets the ragione sociale.
	 *
	 * @return the ragione sociale
	 */
	public String getRagioneSociale() {
		return this.ragioneSociale;
	}

	/**
	 * Sets the ragione sociale.
	 *
	 * @param ragioneSociale the new ragione sociale
	 */
	public void setRagioneSociale(String ragioneSociale) {
		this.ragioneSociale = ragioneSociale;
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

	/**
	 * Gets the siac t soggetto mod.
	 *
	 * @return the siac t soggetto mod
	 */
	public SiacTSoggettoMod getSiacTSoggettoMod() {
		return this.siacTSoggettoMod;
	}

	/**
	 * Sets the siac t soggetto mod.
	 *
	 * @param siacTSoggettoMod the new siac t soggetto mod
	 */
	public void setSiacTSoggettoMod(SiacTSoggettoMod siacTSoggettoMod) {
		this.siacTSoggettoMod = siacTSoggettoMod;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return pergModId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.pergModId = uid;
	}

}