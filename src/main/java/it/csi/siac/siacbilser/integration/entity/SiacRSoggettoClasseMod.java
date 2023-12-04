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
 * The persistent class for the siac_r_soggetto_classe_mod database table.
 * 
 */
@Entity
@Table(name="siac_r_soggetto_classe_mod")
@NamedQuery(name="SiacRSoggettoClasseMod.findAll", query="SELECT s FROM SiacRSoggettoClasseMod s")
public class SiacRSoggettoClasseMod extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The soggetto classe r mod id. */
	@Id
	@SequenceGenerator(name="SIAC_R_SOGGETTO_CLASSE_MOD_SOGGETTOCLASSERMODID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_SOGGETTO_CLASSE_MOD_SOGGETTO_CLASSE_R_MOD_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_SOGGETTO_CLASSE_MOD_SOGGETTOCLASSERMODID_GENERATOR")
	@Column(name="soggetto_classe_r_mod_id")
	private Integer soggettoClasseRModId;

	//bi-directional many-to-one association to SiacDSoggettoClasse
	/** The siac d soggetto classe. */
	@ManyToOne
	@JoinColumn(name="soggetto_classe_id")
	private SiacDSoggettoClasse siacDSoggettoClasse;

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
	 * Instantiates a new siac r soggetto classe mod.
	 */
	public SiacRSoggettoClasseMod() {
	}

	/**
	 * Gets the soggetto classe r mod id.
	 *
	 * @return the soggetto classe r mod id
	 */
	public Integer getSoggettoClasseRModId() {
		return this.soggettoClasseRModId;
	}

	/**
	 * Sets the soggetto classe r mod id.
	 *
	 * @param soggettoClasseRModId the new soggetto classe r mod id
	 */
	public void setSoggettoClasseRModId(Integer soggettoClasseRModId) {
		this.soggettoClasseRModId = soggettoClasseRModId;
	}

	


	/**
	 * Gets the siac d soggetto classe.
	 *
	 * @return the siac d soggetto classe
	 */
	public SiacDSoggettoClasse getSiacDSoggettoClasse() {
		return this.siacDSoggettoClasse;
	}

	/**
	 * Sets the siac d soggetto classe.
	 *
	 * @param siacDSoggettoClasse the new siac d soggetto classe
	 */
	public void setSiacDSoggettoClasse(SiacDSoggettoClasse siacDSoggettoClasse) {
		this.siacDSoggettoClasse = siacDSoggettoClasse;
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
		return soggettoClasseRModId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.soggettoClasseRModId = uid;
		
	}

}