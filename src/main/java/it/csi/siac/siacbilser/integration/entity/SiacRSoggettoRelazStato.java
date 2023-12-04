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
 * The persistent class for the siac_r_soggetto_relaz_stato database table.
 * 
 */
@Entity
@Table(name="siac_r_soggetto_relaz_stato")
@NamedQuery(name="SiacRSoggettoRelazStato.findAll", query="SELECT s FROM SiacRSoggettoRelazStato s")
public class SiacRSoggettoRelazStato extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The soggetto relaz stato id. */
	@Id
	@SequenceGenerator(name="SIAC_R_SOGGETTO_RELAZ_STATO_SOGGETTORELAZSTATOID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_SOGGETTO_RELAZ_STATO_SOGGETTO_RELAZ_STATO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_SOGGETTO_RELAZ_STATO_SOGGETTORELAZSTATOID_GENERATOR")
	@Column(name="soggetto_relaz_stato_id")
	private Integer soggettoRelazStatoId;

	//bi-directional many-to-one association to SiacDRelazStato
	/** The siac d relaz stato. */
	@ManyToOne
	@JoinColumn(name="relaz_stato_id")
	private SiacDRelazStato siacDRelazStato;

	//bi-directional many-to-one association to SiacRSoggettoRelaz
	/** The siac r soggetto relaz. */
	@ManyToOne
	@JoinColumn(name="soggetto_relaz_id")
	private SiacRSoggettoRelaz siacRSoggettoRelaz;

	

	/**
	 * Instantiates a new siac r soggetto relaz stato.
	 */
	public SiacRSoggettoRelazStato() {
	}

	/**
	 * Gets the soggetto relaz stato id.
	 *
	 * @return the soggetto relaz stato id
	 */
	public Integer getSoggettoRelazStatoId() {
		return this.soggettoRelazStatoId;
	}

	/**
	 * Sets the soggetto relaz stato id.
	 *
	 * @param soggettoRelazStatoId the new soggetto relaz stato id
	 */
	public void setSoggettoRelazStatoId(Integer soggettoRelazStatoId) {
		this.soggettoRelazStatoId = soggettoRelazStatoId;
	}

	

	/**
	 * Gets the siac d relaz stato.
	 *
	 * @return the siac d relaz stato
	 */
	public SiacDRelazStato getSiacDRelazStato() {
		return this.siacDRelazStato;
	}

	/**
	 * Sets the siac d relaz stato.
	 *
	 * @param siacDRelazStato the new siac d relaz stato
	 */
	public void setSiacDRelazStato(SiacDRelazStato siacDRelazStato) {
		this.siacDRelazStato = siacDRelazStato;
	}

	/**
	 * Gets the siac r soggetto relaz.
	 *
	 * @return the siac r soggetto relaz
	 */
	public SiacRSoggettoRelaz getSiacRSoggettoRelaz() {
		return this.siacRSoggettoRelaz;
	}

	/**
	 * Sets the siac r soggetto relaz.
	 *
	 * @param siacRSoggettoRelaz the new siac r soggetto relaz
	 */
	public void setSiacRSoggettoRelaz(SiacRSoggettoRelaz siacRSoggettoRelaz) {
		this.siacRSoggettoRelaz = siacRSoggettoRelaz;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return soggettoRelazStatoId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.soggettoRelazStatoId = uid;
	}

	

}