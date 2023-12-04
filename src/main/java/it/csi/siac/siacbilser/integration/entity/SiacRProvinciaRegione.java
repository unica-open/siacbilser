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
 * The persistent class for the siac_r_provincia_regione database table.
 * 
 */
@Entity
@Table(name="siac_r_provincia_regione")
@NamedQuery(name="SiacRProvinciaRegione.findAll", query="SELECT s FROM SiacRProvinciaRegione s")
public class SiacRProvinciaRegione extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The provincia regione id. */
	@Id
	@SequenceGenerator(name="SIAC_R_PROVINCIA_REGIONE_PROVINCIAREGIONEID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_PROVINCIA_REGIONE_PROVINCIA_REGIONE_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_PROVINCIA_REGIONE_PROVINCIAREGIONEID_GENERATOR")
	@Column(name="provincia_regione_id")
	private Integer provinciaRegioneId;

	//bi-directional many-to-one association to SiacTProvincia
	/** The siac t provincia. */
	@ManyToOne
	@JoinColumn(name="provincia_id")
	private SiacTProvincia siacTProvincia;

	//bi-directional many-to-one association to SiacTRegione
	/** The siac t regione. */
	@ManyToOne
	@JoinColumn(name="regione_id")
	private SiacTRegione siacTRegione;

	/**
	 * Instantiates a new siac r provincia regione.
	 */
	public SiacRProvinciaRegione() {
	}

	/**
	 * Gets the provincia regione id.
	 *
	 * @return the provincia regione id
	 */
	public Integer getProvinciaRegioneId() {
		return this.provinciaRegioneId;
	}

	/**
	 * Sets the provincia regione id.
	 *
	 * @param provinciaRegioneId the new provincia regione id
	 */
	public void setProvinciaRegioneId(Integer provinciaRegioneId) {
		this.provinciaRegioneId = provinciaRegioneId;
	}

	/**
	 * Gets the siac t provincia.
	 *
	 * @return the siac t provincia
	 */
	public SiacTProvincia getSiacTProvincia() {
		return this.siacTProvincia;
	}

	/**
	 * Sets the siac t provincia.
	 *
	 * @param siacTProvincia the new siac t provincia
	 */
	public void setSiacTProvincia(SiacTProvincia siacTProvincia) {
		this.siacTProvincia = siacTProvincia;
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
		return provinciaRegioneId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.provinciaRegioneId = uid;
	}

}