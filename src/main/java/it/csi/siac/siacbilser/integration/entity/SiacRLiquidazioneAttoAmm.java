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
 * The persistent class for the siac_r_liquidazione_atto_amm database table.
 * 
 */
@Entity
@Table(name="siac_r_liquidazione_atto_amm")
@NamedQuery(name="SiacRLiquidazioneAttoAmm.findAll", query="SELECT s FROM SiacRLiquidazioneAttoAmm s")
public class SiacRLiquidazioneAttoAmm extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The liq atto amm id. */
	@Id
	@SequenceGenerator(name="SIAC_R_LIQUIDAZIONE_ATTO_AMM_LIQATTOAMMID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_LIQUIDAZIONE_ATTO_AMM_LIQ_ATTO_AMM_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_LIQUIDAZIONE_ATTO_AMM_LIQATTOAMMID_GENERATOR")
	@Column(name="liq_atto_amm_id")
	private Integer liqAttoAmmId;

	//bi-directional many-to-one association to SiacTAttoAmm
	/** The siac t atto amm. */
	@ManyToOne
	@JoinColumn(name="attoamm_id")
	private SiacTAttoAmm siacTAttoAmm;

	//bi-directional many-to-one association to SiacTLiquidazione
	/** The siac t liquidazione. */
	@ManyToOne
	@JoinColumn(name="liq_id")
	private SiacTLiquidazione siacTLiquidazione;

	/**
	 * Instantiates a new siac r liquidazione atto amm.
	 */
	public SiacRLiquidazioneAttoAmm() {
	}

	/**
	 * Gets the liq atto amm id.
	 *
	 * @return the liq atto amm id
	 */
	public Integer getLiqAttoAmmId() {
		return this.liqAttoAmmId;
	}

	/**
	 * Sets the liq atto amm id.
	 *
	 * @param liqAttoAmmId the new liq atto amm id
	 */
	public void setLiqAttoAmmId(Integer liqAttoAmmId) {
		this.liqAttoAmmId = liqAttoAmmId;
	}

	/**
	 * Gets the siac t atto amm.
	 *
	 * @return the siac t atto amm
	 */
	public SiacTAttoAmm getSiacTAttoAmm() {
		return this.siacTAttoAmm;
	}

	/**
	 * Sets the siac t atto amm.
	 *
	 * @param siacTAttoAmm the new siac t atto amm
	 */
	public void setSiacTAttoAmm(SiacTAttoAmm siacTAttoAmm) {
		this.siacTAttoAmm = siacTAttoAmm;
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
		return liqAttoAmmId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.liqAttoAmmId = uid;
	}

}