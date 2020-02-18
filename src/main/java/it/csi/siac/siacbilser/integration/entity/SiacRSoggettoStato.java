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
 * The persistent class for the siac_r_soggetto_stato database table.
 * 
 */
@Entity
@Table(name="siac_r_soggetto_stato")
@NamedQuery(name="SiacRSoggettoStato.findAll", query="SELECT s FROM SiacRSoggettoStato s")
public class SiacRSoggettoStato extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The soggetto stato r id. */
	@Id
	@SequenceGenerator(name="SIAC_R_SOGGETTO_STATO_SOGGETTOSTATORID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_SOGGETTO_STATO_SOGGETTO_STATO_R_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_SOGGETTO_STATO_SOGGETTOSTATORID_GENERATOR")
	@Column(name="soggetto_stato_r_id")
	private Integer soggettoStatoRId;

	//bi-directional many-to-one association to SiacDSoggettoStato
	/** The siac d soggetto stato. */
	@ManyToOne
	@JoinColumn(name="soggetto_stato_id")
	private SiacDSoggettoStato siacDSoggettoStato;

	//bi-directional many-to-one association to SiacTSoggetto
	/** The siac t soggetto. */
	@ManyToOne
	@JoinColumn(name="soggetto_id")
	private SiacTSoggetto siacTSoggetto;

	/**
	 * Instantiates a new siac r soggetto stato.
	 */
	public SiacRSoggettoStato() {
	}

	/**
	 * Gets the soggetto stato r id.
	 *
	 * @return the soggetto stato r id
	 */
	public Integer getSoggettoStatoRId() {
		return this.soggettoStatoRId;
	}

	/**
	 * Sets the soggetto stato r id.
	 *
	 * @param soggettoStatoRId the new soggetto stato r id
	 */
	public void setSoggettoStatoRId(Integer soggettoStatoRId) {
		this.soggettoStatoRId = soggettoStatoRId;
	}	

	/**
	 * Gets the siac d soggetto stato.
	 *
	 * @return the siac d soggetto stato
	 */
	public SiacDSoggettoStato getSiacDSoggettoStato() {
		return this.siacDSoggettoStato;
	}

	/**
	 * Sets the siac d soggetto stato.
	 *
	 * @param siacDSoggettoStato the new siac d soggetto stato
	 */
	public void setSiacDSoggettoStato(SiacDSoggettoStato siacDSoggettoStato) {
		this.siacDSoggettoStato = siacDSoggettoStato;
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
		return soggettoStatoRId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.soggettoStatoRId = uid;
	}

}