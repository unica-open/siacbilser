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
 * The persistent class for the siac_r_liquidazione_soggetto database table.
 * 
 */
@Entity
@Table(name="siac_r_liquidazione_soggetto")
@NamedQuery(name="SiacRLiquidazioneSoggetto.findAll", query="SELECT s FROM SiacRLiquidazioneSoggetto s")
public class SiacRLiquidazioneSoggetto extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The liq soggetto id. */
	@Id
	@SequenceGenerator(name="SIAC_R_LIQUIDAZIONE_SOGGETTO_LIQSOGGETTOID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_LIQUIDAZIONE_SOGGETTO_LIQ_SOGGETTO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_LIQUIDAZIONE_SOGGETTO_LIQSOGGETTOID_GENERATOR")
	@Column(name="liq_soggetto_id")
	private Integer liqSoggettoId;

	//bi-directional many-to-one association to SiacTLiquidazione
	/** The siac t liquidazione. */
	@ManyToOne
	@JoinColumn(name="liq_id")
	private SiacTLiquidazione siacTLiquidazione;

	//bi-directional many-to-one association to SiacTSoggetto
	/** The siac t soggetto. */
	@ManyToOne
	@JoinColumn(name="soggetto_id")
	private SiacTSoggetto siacTSoggetto;

	/**
	 * Instantiates a new siac r liquidazione soggetto.
	 */
	public SiacRLiquidazioneSoggetto() {
	}

	/**
	 * Gets the liq soggetto id.
	 *
	 * @return the liq soggetto id
	 */
	public Integer getLiqSoggettoId() {
		return this.liqSoggettoId;
	}

	/**
	 * Sets the liq soggetto id.
	 *
	 * @param liqSoggettoId the new liq soggetto id
	 */
	public void setLiqSoggettoId(Integer liqSoggettoId) {
		this.liqSoggettoId = liqSoggettoId;
	}

	/**
	 * Gets the siac t liquidazione.
	 *
	 * @return the siac t liquidazione
	 */
	public SiacTLiquidazione getSiacTLiquidazione() {
		return this.siacTLiquidazione;
	}

	/**
	 * Sets the siac t liquidazione.
	 *
	 * @param siacTLiquidazione the new siac t liquidazione
	 */
	public void setSiacTLiquidazione(SiacTLiquidazione siacTLiquidazione) {
		this.siacTLiquidazione = siacTLiquidazione;
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

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return liqSoggettoId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.liqSoggettoId = uid;
		
	}

}