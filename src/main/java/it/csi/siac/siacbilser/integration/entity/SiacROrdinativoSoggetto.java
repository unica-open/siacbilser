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
 * The persistent class for the siac_r_ordinativo_soggetto database table.
 * 
 */
@Entity
@Table(name="siac_r_ordinativo_soggetto")
@NamedQuery(name="SiacROrdinativoSoggetto.findAll", query="SELECT s FROM SiacROrdinativoSoggetto s")
public class SiacROrdinativoSoggetto extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The ord soggetto id. */
	@Id
	@SequenceGenerator(name="SIAC_R_ORDINATIVO_SOGGETTO_ORDSOGGETTOID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_ORDINATIVO_SOGGETTO_ORD_SOGGETTO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_ORDINATIVO_SOGGETTO_ORDSOGGETTOID_GENERATOR")
	@Column(name="ord_soggetto_id")
	private Integer ordSoggettoId;

	//bi-directional many-to-one association to SiacTOrdinativo
	/** The siac t ordinativo. */
	@ManyToOne
	@JoinColumn(name="ord_id")
	private SiacTOrdinativo siacTOrdinativo;

	//bi-directional many-to-one association to SiacTSoggetto
	/** The siac t soggetto. */
	@ManyToOne
	@JoinColumn(name="soggetto_id")
	private SiacTSoggetto siacTSoggetto;

	/**
	 * Instantiates a new siac r ordinativo soggetto.
	 */
	public SiacROrdinativoSoggetto() {
	}

	/**
	 * Gets the ord soggetto id.
	 *
	 * @return the ord soggetto id
	 */
	public Integer getOrdSoggettoId() {
		return this.ordSoggettoId;
	}

	/**
	 * Sets the ord soggetto id.
	 *
	 * @param ordSoggettoId the new ord soggetto id
	 */
	public void setOrdSoggettoId(Integer ordSoggettoId) {
		this.ordSoggettoId = ordSoggettoId;
	}

	/**
	 * Gets the siac t ordinativo.
	 *
	 * @return the siac t ordinativo
	 */
	public SiacTOrdinativo getSiacTOrdinativo() {
		return this.siacTOrdinativo;
	}

	/**
	 * Sets the siac t ordinativo.
	 *
	 * @param siacTOrdinativo the new siac t ordinativo
	 */
	public void setSiacTOrdinativo(SiacTOrdinativo siacTOrdinativo) {
		this.siacTOrdinativo = siacTOrdinativo;
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
		return ordSoggettoId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.ordSoggettoId = uid;
	}

}