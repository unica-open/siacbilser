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
 * The persistent class for the siac_r_indirizzo_soggetto_tipo database table.
 * 
 */
@Entity
@Table(name="siac_r_indirizzo_soggetto_tipo")
@NamedQuery(name="SiacRIndirizzoSoggettoTipo.findAll", query="SELECT s FROM SiacRIndirizzoSoggettoTipo s")
public class SiacRIndirizzoSoggettoTipo extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The ind sog tipo id. */
	@Id
	@SequenceGenerator(name="SIAC_R_INDIRIZZO_SOGGETTO_TIPO_INDSOGTIPOID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_INDIRIZZO_SOGGETTO_TIPO_IND_SOG_TIPO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_INDIRIZZO_SOGGETTO_TIPO_INDSOGTIPOID_GENERATOR")
	@Column(name="ind_sog_tipo_id")
	private Integer indSogTipoId;

	//bi-directional many-to-one association to SiacDIndirizzoTipo
	/** The siac d indirizzo tipo. */
	@ManyToOne
	@JoinColumn(name="indirizzo_tipo_id")
	private SiacDIndirizzoTipo siacDIndirizzoTipo;

	//bi-directional many-to-one association to SiacTIndirizzoSoggetto
	/** The siac t indirizzo soggetto. */
	@ManyToOne
	@JoinColumn(name="indirizzo_id")
	private SiacTIndirizzoSoggetto siacTIndirizzoSoggetto;

	/**
	 * Instantiates a new siac r indirizzo soggetto tipo.
	 */
	public SiacRIndirizzoSoggettoTipo() {
	}

	/**
	 * Gets the ind sog tipo id.
	 *
	 * @return the ind sog tipo id
	 */
	public Integer getIndSogTipoId() {
		return this.indSogTipoId;
	}

	/**
	 * Sets the ind sog tipo id.
	 *
	 * @param indSogTipoId the new ind sog tipo id
	 */
	public void setIndSogTipoId(Integer indSogTipoId) {
		this.indSogTipoId = indSogTipoId;
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
	 * Gets the siac t indirizzo soggetto.
	 *
	 * @return the siac t indirizzo soggetto
	 */
	public SiacTIndirizzoSoggetto getSiacTIndirizzoSoggetto() {
		return this.siacTIndirizzoSoggetto;
	}

	/**
	 * Sets the siac t indirizzo soggetto.
	 *
	 * @param siacTIndirizzoSoggetto the new siac t indirizzo soggetto
	 */
	public void setSiacTIndirizzoSoggetto(SiacTIndirizzoSoggetto siacTIndirizzoSoggetto) {
		this.siacTIndirizzoSoggetto = siacTIndirizzoSoggetto;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return indSogTipoId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.indSogTipoId = uid;
	}

}