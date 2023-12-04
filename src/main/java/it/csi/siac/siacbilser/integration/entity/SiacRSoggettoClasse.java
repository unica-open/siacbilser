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
 * The persistent class for the siac_r_soggetto_classe database table.
 * 
 */
@Entity
@Table(name="siac_r_soggetto_classe")
@NamedQuery(name="SiacRSoggettoClasse.findAll", query="SELECT s FROM SiacRSoggettoClasse s")
public class SiacRSoggettoClasse extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The soggetto classe r id. */
	@Id
	@SequenceGenerator(name="SIAC_R_SOGGETTO_CLASSE_SOGGETTOCLASSERID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_SOGGETTO_CLASSE_SOGGETTO_CLASSE_R_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_SOGGETTO_CLASSE_SOGGETTOCLASSERID_GENERATOR")
	@Column(name="soggetto_classe_r_id")
	private Integer soggettoClasseRId;


	//bi-directional many-to-one association to SiacDSoggettoClasse
	/** The siac d soggetto classe. */
	@ManyToOne
	@JoinColumn(name="soggetto_classe_id")
	private SiacDSoggettoClasse siacDSoggettoClasse;

	//bi-directional many-to-one association to SiacTSoggetto
	/** The siac t soggetto. */
	@ManyToOne
	@JoinColumn(name="soggetto_id")
	private SiacTSoggetto siacTSoggetto;

	/**
	 * Instantiates a new siac r soggetto classe.
	 */
	public SiacRSoggettoClasse() {
	}

	/**
	 * Gets the soggetto classe r id.
	 *
	 * @return the soggetto classe r id
	 */
	public Integer getSoggettoClasseRId() {
		return this.soggettoClasseRId;
	}

	/**
	 * Sets the soggetto classe r id.
	 *
	 * @param soggettoClasseRId the new soggetto classe r id
	 */
	public void setSoggettoClasseRId(Integer soggettoClasseRId) {
		this.soggettoClasseRId = soggettoClasseRId;
	}

	/**
	 * Gets the siac d soggetto classe.
	 *
	 * @return the siac d soggetto classe
	 */
	public SiacDSoggettoClasse getSiacDSoggettoClasse() {
		return this.siacDSoggettoClasse;
	}

	/**
	 * Sets the siac d soggetto classe.
	 *
	 * @param siacDSoggettoClasse the new siac d soggetto classe
	 */
	public void setSiacDSoggettoClasse(SiacDSoggettoClasse siacDSoggettoClasse) {
		this.siacDSoggettoClasse = siacDSoggettoClasse;
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
		return soggettoClasseRId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.soggettoClasseRId = uid;		
	}

}