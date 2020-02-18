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
 * The persistent class for the siac_r_liquidazione_movgest database table.
 * 
 */
@Entity
@Table(name="siac_r_liquidazione_movgest")
@NamedQuery(name="SiacRLiquidazioneMovgest.findAll", query="SELECT s FROM SiacRLiquidazioneMovgest s")
public class SiacRLiquidazioneMovgest extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The liq movgest id. */
	@Id
	@SequenceGenerator(name="SIAC_R_LIQUIDAZIONE_MOVGEST_LIQMOVGESTID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_LIQUIDAZIONE_MOVGEST_LIQ_MOVGEST_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_LIQUIDAZIONE_MOVGEST_LIQMOVGESTID_GENERATOR")
	@Column(name="liq_movgest_id")
	private Integer liqMovgestId;

	//bi-directional many-to-one association to SiacTLiquidazione
	/** The siac t liquidazione. */
	@ManyToOne
	@JoinColumn(name="liq_id")
	private SiacTLiquidazione siacTLiquidazione;

	//bi-directional many-to-one association to SiacTMovgestT
	/** The siac t movgest t. */
	@ManyToOne
	@JoinColumn(name="movgest_ts_id")
	private SiacTMovgestT siacTMovgestT;

	/**
	 * Instantiates a new siac r liquidazione movgest.
	 */
	public SiacRLiquidazioneMovgest() {
	}

	/**
	 * Gets the liq movgest id.
	 *
	 * @return the liq movgest id
	 */
	public Integer getLiqMovgestId() {
		return this.liqMovgestId;
	}

	/**
	 * Sets the liq movgest id.
	 *
	 * @param liqMovgestId the new liq movgest id
	 */
	public void setLiqMovgestId(Integer liqMovgestId) {
		this.liqMovgestId = liqMovgestId;
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
	 * Gets the siac t movgest t.
	 *
	 * @return the siac t movgest t
	 */
	public SiacTMovgestT getSiacTMovgestT() {
		return this.siacTMovgestT;
	}

	/**
	 * Sets the siac t movgest t.
	 *
	 * @param siacTMovgestT the new siac t movgest t
	 */
	public void setSiacTMovgestT(SiacTMovgestT siacTMovgestT) {
		this.siacTMovgestT = siacTMovgestT;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return liqMovgestId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.liqMovgestId = uid;
		
	}

}