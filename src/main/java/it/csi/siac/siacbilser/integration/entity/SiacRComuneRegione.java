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
 * The persistent class for the siac_r_comune_regione database table.
 * 
 */
@Entity
@Table(name="siac_r_comune_regione")
@NamedQuery(name="SiacRComuneRegione.findAll", query="SELECT s FROM SiacRComuneRegione s")
public class SiacRComuneRegione extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The comune regione id. */
	@Id
	@SequenceGenerator(name="SIAC_R_COMUNE_REGIONE_COMUNEREGIONEID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_COMUNE_REGIONE_COMUNE_REGIONE_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_COMUNE_REGIONE_COMUNEREGIONEID_GENERATOR")
	@Column(name="comune_regione_id")
	private Integer comuneRegioneId;

	//bi-directional many-to-one association to SiacTComune
	/** The siac t comune. */
	@ManyToOne
	@JoinColumn(name="comune_id")
	private SiacTComune siacTComune;

	//bi-directional many-to-one association to SiacTRegione
	/** The siac t regione. */
	@ManyToOne
	@JoinColumn(name="regione_id")
	private SiacTRegione siacTRegione;

	/**
	 * Instantiates a new siac r comune regione.
	 */
	public SiacRComuneRegione() {
	}

	/**
	 * Gets the comune regione id.
	 *
	 * @return the comune regione id
	 */
	public Integer getComuneRegioneId() {
		return this.comuneRegioneId;
	}

	/**
	 * Sets the comune regione id.
	 *
	 * @param comuneRegioneId the new comune regione id
	 */
	public void setComuneRegioneId(Integer comuneRegioneId) {
		this.comuneRegioneId = comuneRegioneId;
	}



	/**
	 * Gets the siac t comune.
	 *
	 * @return the siac t comune
	 */
	public SiacTComune getSiacTComune() {
		return this.siacTComune;
	}

	/**
	 * Sets the siac t comune.
	 *
	 * @param siacTComune the new siac t comune
	 */
	public void setSiacTComune(SiacTComune siacTComune) {
		this.siacTComune = siacTComune;
	}



	/**
	 * Gets the siac t regione.
	 *
	 * @return the siac t regione
	 */
	public SiacTRegione getSiacTRegione() {
		return this.siacTRegione;
	}

	/**
	 * Sets the siac t regione.
	 *
	 * @param siacTRegione the new siac t regione
	 */
	public void setSiacTRegione(SiacTRegione siacTRegione) {
		this.siacTRegione = siacTRegione;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return comuneRegioneId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.comuneRegioneId = uid;
	}

}