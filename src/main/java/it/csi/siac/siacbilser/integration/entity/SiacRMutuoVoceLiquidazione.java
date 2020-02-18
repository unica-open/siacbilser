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
 * The persistent class for the siac_r_mutuo_voce_liquidazione database table.
 * 
 */
@Entity
@Table(name="siac_r_mutuo_voce_liquidazione")
@NamedQuery(name="SiacRMutuoVoceLiquidazione.findAll", query="SELECT s FROM SiacRMutuoVoceLiquidazione s")
public class SiacRMutuoVoceLiquidazione extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The mut liq id. */
	@Id
	@SequenceGenerator(name="SIAC_R_MUTUO_VOCE_LIQUIDAZIONE_MUTLIQID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_MUTUO_VOCE_LIQUIDAZIONE_MUT_LIQ_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_MUTUO_VOCE_LIQUIDAZIONE_MUTLIQID_GENERATOR")
	@Column(name="mut_liq_id")
	private Integer mutLiqId;


	//bi-directional many-to-one association to SiacTLiquidazione
	/** The siac t liquidazione. */
	@ManyToOne
	@JoinColumn(name="liq_id")
	private SiacTLiquidazione siacTLiquidazione;

	//bi-directional many-to-one association to SiacTMutuoVoce
	/** The siac t mutuo voce. */
	@ManyToOne
	@JoinColumn(name="mut_voce_id")
	private SiacTMutuoVoce siacTMutuoVoce;

	/**
	 * Instantiates a new siac r mutuo voce liquidazione.
	 */
	public SiacRMutuoVoceLiquidazione() {
	}

	/**
	 * Gets the mut liq id.
	 *
	 * @return the mut liq id
	 */
	public Integer getMutLiqId() {
		return this.mutLiqId;
	}

	/**
	 * Sets the mut liq id.
	 *
	 * @param mutLiqId the new mut liq id
	 */
	public void setMutLiqId(Integer mutLiqId) {
		this.mutLiqId = mutLiqId;
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
	 * Gets the siac t mutuo voce.
	 *
	 * @return the siac t mutuo voce
	 */
	public SiacTMutuoVoce getSiacTMutuoVoce() {
		return this.siacTMutuoVoce;
	}

	/**
	 * Sets the siac t mutuo voce.
	 *
	 * @param siacTMutuoVoce the new siac t mutuo voce
	 */
	public void setSiacTMutuoVoce(SiacTMutuoVoce siacTMutuoVoce) {
		this.siacTMutuoVoce = siacTMutuoVoce;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return mutLiqId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.mutLiqId = uid;
		
	}

}