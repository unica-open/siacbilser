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
 * The persistent class for the siac_r_liquidazione_class database table.
 * 
 */
@Entity
@Table(name="siac_r_liquidazione_class")
@NamedQuery(name="SiacRLiquidazioneClass.findAll", query="SELECT s FROM SiacRLiquidazioneClass s")
public class SiacRLiquidazioneClass extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The liq classif id. */
	@Id
	@SequenceGenerator(name="SIAC_R_LIQUIDAZIONE_CLASS_LIQCLASSIFID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_LIQUIDAZIONE_CLASS_LIQ_CLASSIF_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_LIQUIDAZIONE_CLASS_LIQCLASSIFID_GENERATOR")
	@Column(name="liq_classif_id")
	private Integer liqClassifId;

	//bi-directional many-to-one association to SiacTClass
	/** The siac t class. */
	@ManyToOne
	@JoinColumn(name="classif_id")
	private SiacTClass siacTClass;

	//bi-directional many-to-one association to SiacTLiquidazione
	/** The siac t liquidazione. */
	@ManyToOne
	@JoinColumn(name="liq_id")
	private SiacTLiquidazione siacTLiquidazione;

	/**
	 * Instantiates a new siac r liquidazione class.
	 */
	public SiacRLiquidazioneClass() {
	}

	/**
	 * Gets the liq classif id.
	 *
	 * @return the liq classif id
	 */
	public Integer getLiqClassifId() {
		return this.liqClassifId;
	}

	/**
	 * Sets the liq classif id.
	 *
	 * @param liqClassifId the new liq classif id
	 */
	public void setLiqClassifId(Integer liqClassifId) {
		this.liqClassifId = liqClassifId;
	}	

	/**
	 * Gets the siac t class.
	 *
	 * @return the siac t class
	 */
	public SiacTClass getSiacTClass() {
		return this.siacTClass;
	}

	/**
	 * Sets the siac t class.
	 *
	 * @param siacTClass the new siac t class
	 */
	public void setSiacTClass(SiacTClass siacTClass) {
		this.siacTClass = siacTClass;
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
		return liqClassifId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.liqClassifId = uid;
		
	}

}