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
 * The persistent class for the siac_r_indirizzo_soggetto_tipo_mod database table.
 * 
 */
@Entity
@Table(name="siac_r_indirizzo_soggetto_tipo_mod")
@NamedQuery(name="SiacRIndirizzoSoggettoTipoMod.findAll", query="SELECT s FROM SiacRIndirizzoSoggettoTipoMod s")
public class SiacRIndirizzoSoggettoTipoMod extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The ind sog tipo mod id. */
	@Id
	@SequenceGenerator(name="SIAC_R_INDIRIZZO_SOGGETTO_TIPO_MOD_INDSOGTIPOMODID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_INDIRIZZO_SOGGETTO_TIPO_MOD_IND_SOG_TIPO_MOD_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_INDIRIZZO_SOGGETTO_TIPO_MOD_INDSOGTIPOMODID_GENERATOR")
	@Column(name="ind_sog_tipo_mod_id")
	private Integer indSogTipoModId;

	//bi-directional many-to-one association to SiacDIndirizzoTipo
	/** The siac d indirizzo tipo. */
	@ManyToOne
	@JoinColumn(name="indirizzo_tipo_id")
	private SiacDIndirizzoTipo siacDIndirizzoTipo;

	//bi-directional many-to-one association to SiacTIndirizzoSoggettoMod
	/** The siac t indirizzo soggetto mod. */
	@ManyToOne
	@JoinColumn(name="indirizzo_mod_id")
	private SiacTIndirizzoSoggettoMod siacTIndirizzoSoggettoMod;

	/**
	 * Instantiates a new siac r indirizzo soggetto tipo mod.
	 */
	public SiacRIndirizzoSoggettoTipoMod() {
	}

	/**
	 * Gets the ind sog tipo mod id.
	 *
	 * @return the ind sog tipo mod id
	 */
	public Integer getIndSogTipoModId() {
		return this.indSogTipoModId;
	}

	/**
	 * Sets the ind sog tipo mod id.
	 *
	 * @param indSogTipoModId the new ind sog tipo mod id
	 */
	public void setIndSogTipoModId(Integer indSogTipoModId) {
		this.indSogTipoModId = indSogTipoModId;
	}

	/**
	 * Gets the siac d indirizzo tipo.
	 *
	 * @return the siac d indirizzo tipo
	 */
	public SiacDIndirizzoTipo getSiacDIndirizzoTipo() {
		return this.siacDIndirizzoTipo;
	}

	/**
	 * Sets the siac d indirizzo tipo.
	 *
	 * @param siacDIndirizzoTipo the new siac d indirizzo tipo
	 */
	public void setSiacDIndirizzoTipo(SiacDIndirizzoTipo siacDIndirizzoTipo) {
		this.siacDIndirizzoTipo = siacDIndirizzoTipo;
	}

	/**
	 * Gets the siac t indirizzo soggetto mod.
	 *
	 * @return the siac t indirizzo soggetto mod
	 */
	public SiacTIndirizzoSoggettoMod getSiacTIndirizzoSoggettoMod() {
		return this.siacTIndirizzoSoggettoMod;
	}

	/**
	 * Sets the siac t indirizzo soggetto mod.
	 *
	 * @param siacTIndirizzoSoggettoMod the new siac t indirizzo soggetto mod
	 */
	public void setSiacTIndirizzoSoggettoMod(SiacTIndirizzoSoggettoMod siacTIndirizzoSoggettoMod) {
		this.siacTIndirizzoSoggettoMod = siacTIndirizzoSoggettoMod;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return indSogTipoModId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.indSogTipoModId = uid;
	}

}