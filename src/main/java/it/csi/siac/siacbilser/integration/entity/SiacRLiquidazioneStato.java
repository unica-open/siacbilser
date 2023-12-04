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
 * The persistent class for the siac_r_liquidazione_stato database table.
 * 
 */
@Entity
@Table(name="siac_r_liquidazione_stato")
@NamedQuery(name="SiacRLiquidazioneStato.findAll", query="SELECT s FROM SiacRLiquidazioneStato s")
public class SiacRLiquidazioneStato extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The liq stato r id. */
	@Id
	@SequenceGenerator(name="SIAC_R_LIQUIDAZIONE_STATO_LIQSTATORID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_LIQUIDAZIONE_STATO_LIQ_STATO_R_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_LIQUIDAZIONE_STATO_LIQSTATORID_GENERATOR")
	@Column(name="liq_stato_r_id")
	private Integer liqStatoRId;

	//bi-directional many-to-one association to SiacDLiquidazioneStato
	/** The siac d liquidazione stato. */
	@ManyToOne
	@JoinColumn(name="liq_stato_id")
	private SiacDLiquidazioneStato siacDLiquidazioneStato;

	//bi-directional many-to-one association to SiacTLiquidazione
	/** The siac t liquidazione. */
	@ManyToOne
	@JoinColumn(name="liq_id")
	private SiacTLiquidazione siacTLiquidazione;

	/**
	 * Instantiates a new siac r liquidazione stato.
	 */
	public SiacRLiquidazioneStato() {
	}

	/**
	 * Gets the liq stato r id.
	 *
	 * @return the liq stato r id
	 */
	public Integer getLiqStatoRId() {
		return this.liqStatoRId;
	}

	/**
	 * Sets the liq stato r id.
	 *
	 * @param liqStatoRId the new liq stato r id
	 */
	public void setLiqStatoRId(Integer liqStatoRId) {
		this.liqStatoRId = liqStatoRId;
	}

	/**
	 * Gets the siac d liquidazione stato.
	 *
	 * @return the siac d liquidazione stato
	 */
	public SiacDLiquidazioneStato getSiacDLiquidazioneStato() {
		return this.siacDLiquidazioneStato;
	}

	/**
	 * Sets the siac d liquidazione stato.
	 *
	 * @param siacDLiquidazioneStato the new siac d liquidazione stato
	 */
	public void setSiacDLiquidazioneStato(SiacDLiquidazioneStato siacDLiquidazioneStato) {
		this.siacDLiquidazioneStato = siacDLiquidazioneStato;
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

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return liqStatoRId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.liqStatoRId = uid;
		
	}

}