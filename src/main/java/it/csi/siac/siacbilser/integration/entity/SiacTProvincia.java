/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


// TODO: Auto-generated Javadoc
/**
 * The persistent class for the siac_t_provincia database table.
 * 
 */
@Entity
@Table(name="siac_t_provincia")
@NamedQuery(name="SiacTProvincia.findAll", query="SELECT s FROM SiacTProvincia s")
public class SiacTProvincia extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The provincia id. */
	@Id
	@SequenceGenerator(name="SIAC_T_PROVINCIA_PROVINCIAID_GENERATOR", allocationSize=1, sequenceName="SIAC_T_PROVINCIA_PROVINCIA_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_PROVINCIA_PROVINCIAID_GENERATOR")
	@Column(name="provincia_id")
	private Integer provinciaId;

	/** The provincia desc. */
	@Column(name="provincia_desc")
	private String provinciaDesc;

	/** The provincia istat code. */
	@Column(name="provincia_istat_code")
	private String provinciaIstatCode;

	/** The sigla automobilistica. */
	@Column(name="sigla_automobilistica")
	private String siglaAutomobilistica;

	//bi-directional many-to-one association to SiacRComuneProvincia
	/** The siac r comune provincias. */
	@OneToMany(mappedBy="siacTProvincia")
	private List<SiacRComuneProvincia> siacRComuneProvincias;

	//bi-directional many-to-one association to SiacRProvinciaRegione
	/** The siac r provincia regiones. */
	@OneToMany(mappedBy="siacTProvincia")
	private List<SiacRProvinciaRegione> siacRProvinciaRegiones;



	/**
	 * Instantiates a new siac t provincia.
	 */
	public SiacTProvincia() {
	}

	/**
	 * Gets the provincia id.
	 *
	 * @return the provincia id
	 */
	public Integer getProvinciaId() {
		return this.provinciaId;
	}

	/**
	 * Sets the provincia id.
	 *
	 * @param provinciaId the new provincia id
	 */
	public void setProvinciaId(Integer provinciaId) {
		this.provinciaId = provinciaId;
	}

	

	/**
	 * Gets the provincia desc.
	 *
	 * @return the provincia desc
	 */
	public String getProvinciaDesc() {
		return this.provinciaDesc;
	}

	/**
	 * Sets the provincia desc.
	 *
	 * @param provinciaDesc the new provincia desc
	 */
	public void setProvinciaDesc(String provinciaDesc) {
		this.provinciaDesc = provinciaDesc;
	}

	/**
	 * Gets the provincia istat code.
	 *
	 * @return the provincia istat code
	 */
	public String getProvinciaIstatCode() {
		return this.provinciaIstatCode;
	}

	/**
	 * Sets the provincia istat code.
	 *
	 * @param provinciaIstatCode the new provincia istat code
	 */
	public void setProvinciaIstatCode(String provinciaIstatCode) {
		this.provinciaIstatCode = provinciaIstatCode;
	}

	/**
	 * Gets the sigla automobilistica.
	 *
	 * @return the sigla automobilistica
	 */
	public String getSiglaAutomobilistica() {
		return this.siglaAutomobilistica;
	}

	/**
	 * Sets the sigla automobilistica.
	 *
	 * @param siglaAutomobilistica the new sigla automobilistica
	 */
	public void setSiglaAutomobilistica(String siglaAutomobilistica) {
		this.siglaAutomobilistica = siglaAutomobilistica;
	}

	

	/**
	 * Gets the siac r comune provincias.
	 *
	 * @return the siac r comune provincias
	 */
	public List<SiacRComuneProvincia> getSiacRComuneProvincias() {
		return this.siacRComuneProvincias;
	}

	/**
	 * Sets the siac r comune provincias.
	 *
	 * @param siacRComuneProvincias the new siac r comune provincias
	 */
	public void setSiacRComuneProvincias(List<SiacRComuneProvincia> siacRComuneProvincias) {
		this.siacRComuneProvincias = siacRComuneProvincias;
	}

	/**
	 * Adds the siac r comune provincia.
	 *
	 * @param siacRComuneProvincia the siac r comune provincia
	 * @return the siac r comune provincia
	 */
	public SiacRComuneProvincia addSiacRComuneProvincia(SiacRComuneProvincia siacRComuneProvincia) {
		getSiacRComuneProvincias().add(siacRComuneProvincia);
		siacRComuneProvincia.setSiacTProvincia(this);

		return siacRComuneProvincia;
	}

	/**
	 * Removes the siac r comune provincia.
	 *
	 * @param siacRComuneProvincia the siac r comune provincia
	 * @return the siac r comune provincia
	 */
	public SiacRComuneProvincia removeSiacRComuneProvincia(SiacRComuneProvincia siacRComuneProvincia) {
		getSiacRComuneProvincias().remove(siacRComuneProvincia);
		siacRComuneProvincia.setSiacTProvincia(null);

		return siacRComuneProvincia;
	}

	/**
	 * Gets the siac r provincia regiones.
	 *
	 * @return the siac r provincia regiones
	 */
	public List<SiacRProvinciaRegione> getSiacRProvinciaRegiones() {
		return this.siacRProvinciaRegiones;
	}

	/**
	 * Sets the siac r provincia regiones.
	 *
	 * @param siacRProvinciaRegiones the new siac r provincia regiones
	 */
	public void setSiacRProvinciaRegiones(List<SiacRProvinciaRegione> siacRProvinciaRegiones) {
		this.siacRProvinciaRegiones = siacRProvinciaRegiones;
	}

	/**
	 * Adds the siac r provincia regione.
	 *
	 * @param siacRProvinciaRegione the siac r provincia regione
	 * @return the siac r provincia regione
	 */
	public SiacRProvinciaRegione addSiacRProvinciaRegione(SiacRProvinciaRegione siacRProvinciaRegione) {
		getSiacRProvinciaRegiones().add(siacRProvinciaRegione);
		siacRProvinciaRegione.setSiacTProvincia(this);

		return siacRProvinciaRegione;
	}

	/**
	 * Removes the siac r provincia regione.
	 *
	 * @param siacRProvinciaRegione the siac r provincia regione
	 * @return the siac r provincia regione
	 */
	public SiacRProvinciaRegione removeSiacRProvinciaRegione(SiacRProvinciaRegione siacRProvinciaRegione) {
		getSiacRProvinciaRegiones().remove(siacRProvinciaRegione);
		siacRProvinciaRegione.setSiacTProvincia(null);

		return siacRProvinciaRegione;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return provinciaId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.provinciaId = uid;
	}

}