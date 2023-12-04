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
 * The persistent class for the siac_r_comune_provincia database table.
 * 
 */
@Entity
@Table(name="siac_r_comune_provincia")
@NamedQuery(name="SiacRComuneProvincia.findAll", query="SELECT s FROM SiacRComuneProvincia s")
public class SiacRComuneProvincia extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The comune provincia id. */
	@Id
	@SequenceGenerator(name="SIAC_R_COMUNE_PROVINCIA_COMUNEPROVINCIAID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_COMUNE_PROVINCIA_COMUNE_PROVINCIA_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_COMUNE_PROVINCIA_COMUNEPROVINCIAID_GENERATOR")
	@Column(name="comune_provincia_id")
	private Integer comuneProvinciaId;

	//bi-directional many-to-one association to SiacTComune
	/** The siac t comune. */
	@ManyToOne
	@JoinColumn(name="comune_id")
	private SiacTComune siacTComune;

	//bi-directional many-to-one association to SiacTProvincia
	/** The siac t provincia. */
	@ManyToOne
	@JoinColumn(name="provincia_id")
	private SiacTProvincia siacTProvincia;

	/**
	 * Instantiates a new siac r comune provincia.
	 */
	public SiacRComuneProvincia() {
	}

	/**
	 * Gets the comune provincia id.
	 *
	 * @return the comune provincia id
	 */
	public Integer getComuneProvinciaId() {
		return this.comuneProvinciaId;
	}

	/**
	 * Sets the comune provincia id.
	 *
	 * @param comuneProvinciaId the new comune provincia id
	 */
	public void setComuneProvinciaId(Integer comuneProvinciaId) {
		this.comuneProvinciaId = comuneProvinciaId;
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

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return comuneProvinciaId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.comuneProvinciaId = uid;
	}

}