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
 * The persistent class for the siac_r_soggetto_tipo_mod database table.
 * 
 */
@Entity
@Table(name="siac_r_soggetto_tipo_mod")
@NamedQuery(name="SiacRSoggettoTipoMod.findAll", query="SELECT s FROM SiacRSoggettoTipoMod s")
public class SiacRSoggettoTipoMod extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The soggetto r mod id. */
	@Id
	@SequenceGenerator(name="SIAC_R_SOGGETTO_TIPO_MOD_SOGGETTORMODID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_SOGGETTO_TIPO_MOD_SOGGETTO_R_MOD_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_SOGGETTO_TIPO_MOD_SOGGETTORMODID_GENERATOR")
	@Column(name="soggetto_r_mod_id")
	private Integer soggettoRModId;

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

	//bi-directional many-to-one association to SiacTSoggettoMod
	/** The siac t soggetto mod. */
	@ManyToOne
	@JoinColumn(name="sog_mod_id")
	private SiacTSoggettoMod siacTSoggettoMod;

	/**
	 * Instantiates a new siac r soggetto tipo mod.
	 */
	public SiacRSoggettoTipoMod() {
	}

	/**
	 * Gets the soggetto r mod id.
	 *
	 * @return the soggetto r mod id
	 */
	public Integer getSoggettoRModId() {
		return this.soggettoRModId;
	}

	/**
	 * Sets the soggetto r mod id.
	 *
	 * @param soggettoRModId the new soggetto r mod id
	 */
	public void setSoggettoRModId(Integer soggettoRModId) {
		this.soggettoRModId = soggettoRModId;
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
		return soggettoRModId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.soggettoRModId = uid;
	}

}