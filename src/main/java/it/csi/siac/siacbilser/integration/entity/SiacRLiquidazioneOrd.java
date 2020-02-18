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
 * The persistent class for the siac_r_liquidazione_ord database table.
 * 
 */
@Entity
@Table(name="siac_r_liquidazione_ord")
@NamedQuery(name="SiacRLiquidazioneOrd.findAll", query="SELECT s FROM SiacRLiquidazioneOrd s")
public class SiacRLiquidazioneOrd extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The liq ord id. */
	@Id
	@SequenceGenerator(name="SIAC_R_LIQUIDAZIONE_ORD_LIQORDID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_LIQUIDAZIONE_ORD_LIQ_ORD_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_LIQUIDAZIONE_ORD_LIQORDID_GENERATOR")
	@Column(name="liq_ord_id")
	private Integer liqOrdId;

	//bi-directional many-to-one association to SiacTLiquidazione
	/** The siac t liquidazione. */
	@ManyToOne
	@JoinColumn(name="liq_id")
	private SiacTLiquidazione siacTLiquidazione;

	//bi-directional many-to-one association to SiacTOrdinativoT
	/** The siac t ordinativo t. */
	@ManyToOne
	@JoinColumn(name="sord_id")
	private SiacTOrdinativoT siacTOrdinativoT;

	/**
	 * Instantiates a new siac r liquidazione ord.
	 */
	public SiacRLiquidazioneOrd() {
	}

	/**
	 * Gets the liq ord id.
	 *
	 * @return the liq ord id
	 */
	public Integer getLiqOrdId() {
		return this.liqOrdId;
	}

	/**
	 * Sets the liq ord id.
	 *
	 * @param liqOrdId the new liq ord id
	 */
	public void setLiqOrdId(Integer liqOrdId) {
		this.liqOrdId = liqOrdId;
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
	 * Gets the siac t ordinativo t.
	 *
	 * @return the siac t ordinativo t
	 */
	public SiacTOrdinativoT getSiacTOrdinativoT() {
		return this.siacTOrdinativoT;
	}

	/**
	 * Sets the siac t ordinativo t.
	 *
	 * @param siacTOrdinativoT the new siac t ordinativo t
	 */
	public void setSiacTOrdinativoT(SiacTOrdinativoT siacTOrdinativoT) {
		this.siacTOrdinativoT = siacTOrdinativoT;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return liqOrdId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.liqOrdId = uid;
	}

}