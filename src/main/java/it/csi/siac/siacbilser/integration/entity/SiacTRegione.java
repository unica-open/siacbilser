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
 * The persistent class for the siac_t_regione database table.
 * 
 */
@Entity
@Table(name="siac_t_regione")
@NamedQuery(name="SiacTRegione.findAll", query="SELECT s FROM SiacTRegione s")
public class SiacTRegione extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The regione id. */
	@Id
	@SequenceGenerator(name="SIAC_T_REGIONE_REGIONEID_GENERATOR", allocationSize=1, sequenceName="SIAC_T_REGIONE_REGIONE_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_REGIONE_REGIONEID_GENERATOR")
	@Column(name="regione_id")
	private Integer regioneId;

	/** The regione denominazione. */
	@Column(name="regione_denominazione")
	private String regioneDenominazione;

	/** The regione istat codice. */
	@Column(name="regione_istat_codice")
	private String regioneIstatCodice;

	//bi-directional many-to-one association to SiacRComuneRegione
	/** The siac r comune regiones. */
	@OneToMany(mappedBy="siacTRegione")
	private List<SiacRComuneRegione> siacRComuneRegiones;

	//bi-directional many-to-one association to SiacRProvinciaRegione
	/** The siac r provincia regiones. */
	@OneToMany(mappedBy="siacTRegione")
	private List<SiacRProvinciaRegione> siacRProvinciaRegiones;

	/**
	 * Instantiates a new siac t regione.
	 */
	public SiacTRegione() {
	}

	/**
	 * Gets the regione id.
	 *
	 * @return the regione id
	 */
	public Integer getRegioneId() {
		return this.regioneId;
	}

	/**
	 * Sets the regione id.
	 *
	 * @param regioneId the new regione id
	 */
	public void setRegioneId(Integer regioneId) {
		this.regioneId = regioneId;
	}

	

	/**
	 * Gets the regione denominazione.
	 *
	 * @return the regione denominazione
	 */
	public String getRegioneDenominazione() {
		return this.regioneDenominazione;
	}

	/**
	 * Sets the regione denominazione.
	 *
	 * @param regioneDenominazione the new regione denominazione
	 */
	public void setRegioneDenominazione(String regioneDenominazione) {
		this.regioneDenominazione = regioneDenominazione;
	}

	/**
	 * Gets the regione istat codice.
	 *
	 * @return the regione istat codice
	 */
	public String getRegioneIstatCodice() {
		return this.regioneIstatCodice;
	}

	/**
	 * Sets the regione istat codice.
	 *
	 * @param regioneIstatCodice the new regione istat codice
	 */
	public void setRegioneIstatCodice(String regioneIstatCodice) {
		this.regioneIstatCodice = regioneIstatCodice;
	}

	

	/**
	 * Gets the siac r comune regiones.
	 *
	 * @return the siac r comune regiones
	 */
	public List<SiacRComuneRegione> getSiacRComuneRegiones() {
		return this.siacRComuneRegiones;
	}

	/**
	 * Sets the siac r comune regiones.
	 *
	 * @param siacRComuneRegiones the new siac r comune regiones
	 */
	public void setSiacRComuneRegiones(List<SiacRComuneRegione> siacRComuneRegiones) {
		this.siacRComuneRegiones = siacRComuneRegiones;
	}

	/**
	 * Adds the siac r comune regione.
	 *
	 * @param siacRComuneRegione the siac r comune regione
	 * @return the siac r comune regione
	 */
	public SiacRComuneRegione addSiacRComuneRegione(SiacRComuneRegione siacRComuneRegione) {
		getSiacRComuneRegiones().add(siacRComuneRegione);
		siacRComuneRegione.setSiacTRegione(this);

		return siacRComuneRegione;
	}

	/**
	 * Removes the siac r comune regione.
	 *
	 * @param siacRComuneRegione the siac r comune regione
	 * @return the siac r comune regione
	 */
	public SiacRComuneRegione removeSiacRComuneRegione(SiacRComuneRegione siacRComuneRegione) {
		getSiacRComuneRegiones().remove(siacRComuneRegione);
		siacRComuneRegione.setSiacTRegione(null);

		return siacRComuneRegione;
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
		siacRProvinciaRegione.setSiacTRegione(this);

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
		siacRProvinciaRegione.setSiacTRegione(null);

		return siacRProvinciaRegione;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return regioneId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.regioneId = uid;
	}

}