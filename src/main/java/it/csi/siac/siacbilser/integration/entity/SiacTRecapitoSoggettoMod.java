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
 * The persistent class for the siac_t_recapito_soggetto_mod database table.
 * 
 */
@Entity
@Table(name="siac_t_recapito_soggetto_mod")
@NamedQuery(name="SiacTRecapitoSoggettoMod.findAll", query="SELECT s FROM SiacTRecapitoSoggettoMod s")
public class SiacTRecapitoSoggettoMod extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The recapito mod id. */
	@Id
	@SequenceGenerator(name="SIAC_T_RECAPITO_SOGGETTO_MOD_RECAPITOMODID_GENERATOR", allocationSize=1, sequenceName="SIAC_T_RECAPITO_SOGGETTO_MOD_RECAPITO_MOD_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_RECAPITO_SOGGETTO_MOD_RECAPITOMODID_GENERATOR")
	@Column(name="recapito_mod_id")
	private Integer recapitoModId;

	/** The avviso. */
	private String avviso;

	/** The recapito code. */
	@Column(name="recapito_code")
	private String recapitoCode;

	/** The recapito desc. */
	@Column(name="recapito_desc")
	private String recapitoDesc;

	//bi-directional many-to-one association to SiacDRecapitoModo
	/** The siac d recapito modo. */
	@ManyToOne
	@JoinColumn(name="recapito_modo_id")
	private SiacDRecapitoModo siacDRecapitoModo;

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
	 * Instantiates a new siac t recapito soggetto mod.
	 */
	public SiacTRecapitoSoggettoMod() {
	}

	/**
	 * Gets the recapito mod id.
	 *
	 * @return the recapito mod id
	 */
	public Integer getRecapitoModId() {
		return this.recapitoModId;
	}

	/**
	 * Sets the recapito mod id.
	 *
	 * @param recapitoModId the new recapito mod id
	 */
	public void setRecapitoModId(Integer recapitoModId) {
		this.recapitoModId = recapitoModId;
	}

	/**
	 * Gets the avviso.
	 *
	 * @return the avviso
	 */
	public String getAvviso() {
		return this.avviso;
	}

	/**
	 * Sets the avviso.
	 *
	 * @param avviso the new avviso
	 */
	public void setAvviso(String avviso) {
		this.avviso = avviso;
	}

	/**
	 * Gets the recapito code.
	 *
	 * @return the recapito code
	 */
	public String getRecapitoCode() {
		return this.recapitoCode;
	}

	/**
	 * Sets the recapito code.
	 *
	 * @param recapitoCode the new recapito code
	 */
	public void setRecapitoCode(String recapitoCode) {
		this.recapitoCode = recapitoCode;
	}

	/**
	 * Gets the recapito desc.
	 *
	 * @return the recapito desc
	 */
	public String getRecapitoDesc() {
		return this.recapitoDesc;
	}

	/**
	 * Sets the recapito desc.
	 *
	 * @param recapitoDesc the new recapito desc
	 */
	public void setRecapitoDesc(String recapitoDesc) {
		this.recapitoDesc = recapitoDesc;
	}

	/**
	 * Gets the siac d recapito modo.
	 *
	 * @return the siac d recapito modo
	 */
	public SiacDRecapitoModo getSiacDRecapitoModo() {
		return this.siacDRecapitoModo;
	}

	/**
	 * Sets the siac d recapito modo.
	 *
	 * @param siacDRecapitoModo the new siac d recapito modo
	 */
	public void setSiacDRecapitoModo(SiacDRecapitoModo siacDRecapitoModo) {
		this.siacDRecapitoModo = siacDRecapitoModo;
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
		return recapitoModId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.recapitoModId = uid;
	}

}