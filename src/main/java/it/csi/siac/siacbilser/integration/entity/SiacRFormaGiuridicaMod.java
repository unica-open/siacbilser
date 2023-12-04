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
 * The persistent class for the siac_r_forma_giuridica_mod database table.
 * 
 */
@Entity
@Table(name="siac_r_forma_giuridica_mod")
@NamedQuery(name="SiacRFormaGiuridicaMod.findAll", query="SELECT s FROM SiacRFormaGiuridicaMod s")
public class SiacRFormaGiuridicaMod extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The soggetto forma giuridica mod id. */
	@Id
	@SequenceGenerator(name="SIAC_R_FORMA_GIURIDICA_MOD_SOGGETTOFORMAGIURIDICAMODID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_FORMA_GIURIDICA_MOD_SOGGETTO_FORMA_GIURIDICA_MOD_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_FORMA_GIURIDICA_MOD_SOGGETTOFORMAGIURIDICAMODID_GENERATOR")
	@Column(name="soggetto_forma_giuridica_mod_id")
	private Integer soggettoFormaGiuridicaModId;

	//bi-directional many-to-one association to SiacTFormaGiuridica
	/** The siac t forma giuridica. */
	@ManyToOne
	@JoinColumn(name="forma_giuridica_id")
	private SiacTFormaGiuridica siacTFormaGiuridica;

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
	 * Instantiates a new siac r forma giuridica mod.
	 */
	public SiacRFormaGiuridicaMod() {
	}

	/**
	 * Gets the soggetto forma giuridica mod id.
	 *
	 * @return the soggetto forma giuridica mod id
	 */
	public Integer getSoggettoFormaGiuridicaModId() {
		return this.soggettoFormaGiuridicaModId;
	}

	/**
	 * Sets the soggetto forma giuridica mod id.
	 *
	 * @param soggettoFormaGiuridicaModId the new soggetto forma giuridica mod id
	 */
	public void setSoggettoFormaGiuridicaModId(Integer soggettoFormaGiuridicaModId) {
		this.soggettoFormaGiuridicaModId = soggettoFormaGiuridicaModId;
	}

	
	/**
	 * Gets the siac t forma giuridica.
	 *
	 * @return the siac t forma giuridica
	 */
	public SiacTFormaGiuridica getSiacTFormaGiuridica() {
		return this.siacTFormaGiuridica;
	}

	/**
	 * Sets the siac t forma giuridica.
	 *
	 * @param siacTFormaGiuridica the new siac t forma giuridica
	 */
	public void setSiacTFormaGiuridica(SiacTFormaGiuridica siacTFormaGiuridica) {
		this.siacTFormaGiuridica = siacTFormaGiuridica;
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
		return soggettoFormaGiuridicaModId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.soggettoFormaGiuridicaModId = uid;
	}

}