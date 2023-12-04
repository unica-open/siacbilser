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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


// TODO: Auto-generated Javadoc
/**
 * The persistent class for the siac_t_comune database table.
 * 
 */
@Entity
@Table(name="siac_t_comune")
@NamedQuery(name="SiacTComune.findAll", query="SELECT s FROM SiacTComune s")
public class SiacTComune extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The comune id. */
	@Id
	@SequenceGenerator(name="SIAC_T_COMUNE_COMUNEID_GENERATOR", allocationSize=1, sequenceName="SIAC_T_COMUNE_COMUNE_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_COMUNE_COMUNEID_GENERATOR")
	@Column(name="comune_id")
	private Integer comuneId;

	/** The comune desc. */
	@Column(name="comune_desc")
	private String comuneDesc;

	/** The comune istat code. */
	@Column(name="comune_istat_code")
	private String comuneIstatCode;
	
	/** The comune belfiore catastale code. */
	@Column(name="comune_belfiore_catastale_code")
	private String comuneBelfioreCatastaleCode;

	//bi-directional many-to-one association to SiacRComuneProvincia
	/** The siac r comune provincias. */
	@OneToMany(mappedBy="siacTComune")
	private List<SiacRComuneProvincia> siacRComuneProvincias;

	//bi-directional many-to-one association to SiacRComuneRegione
	/** The siac r comune regiones. */
	@OneToMany(mappedBy="siacTComune")
	private List<SiacRComuneRegione> siacRComuneRegiones;

	//bi-directional many-to-one association to SiacTNazione
	/** The siac t nazione. */
	@ManyToOne
	@JoinColumn(name="nazione_id")
	private SiacTNazione siacTNazione;

	//bi-directional many-to-one association to SiacTIndirizzoSoggetto
	/** The siac t indirizzo soggettos. */
	@OneToMany(mappedBy="siacTComune")
	private List<SiacTIndirizzoSoggetto> siacTIndirizzoSoggettos;

	//bi-directional many-to-one association to SiacTIndirizzoSoggettoMod
	/** The siac t indirizzo soggetto mods. */
	@OneToMany(mappedBy="siacTComune")
	private List<SiacTIndirizzoSoggettoMod> siacTIndirizzoSoggettoMods;

	//bi-directional many-to-one association to SiacTPersonaFisica
	/** The siac t persona fisicas. */
	@OneToMany(mappedBy="siacTComune")
	private List<SiacTPersonaFisica> siacTPersonaFisicas;

	//bi-directional many-to-one association to SiacTPersonaFisicaMod
	/** The siac t persona fisica mods. */
	@OneToMany(mappedBy="siacTComune")
	private List<SiacTPersonaFisicaMod> siacTPersonaFisicaMods;

	/**
	 * Instantiates a new siac t comune.
	 */
	public SiacTComune() {
	}

	/**
	 * Gets the comune id.
	 *
	 * @return the comune id
	 */
	public Integer getComuneId() {
		return this.comuneId;
	}

	/**
	 * Sets the comune id.
	 *
	 * @param comuneId the new comune id
	 */
	public void setComuneId(Integer comuneId) {
		this.comuneId = comuneId;
	}

	/**
	 * Gets the comune desc.
	 *
	 * @return the comune desc
	 */
	public String getComuneDesc() {
		return this.comuneDesc;
	}

	/**
	 * Sets the comune desc.
	 *
	 * @param comuneDesc the new comune desc
	 */
	public void setComuneDesc(String comuneDesc) {
		this.comuneDesc = comuneDesc;
	}

	/**
	 * Gets the comune istat code.
	 *
	 * @return the comune istat code
	 */
	public String getComuneIstatCode() {
		return this.comuneIstatCode;
	}

	/**
	 * Sets the comune istat code.
	 *
	 * @param comuneIstatCode the new comune istat code
	 */
	public void setComuneIstatCode(String comuneIstatCode) {
		this.comuneIstatCode = comuneIstatCode;
	}

	/**
	 * Gets the comune belfiore catastale code.
	 * 
	 * @return the comuneBelfioreCatastaleCode
	 */
	public String getComuneBelfioreCatastaleCode() {
		return comuneBelfioreCatastaleCode;
	}

	/**
	 * Sets the comune belfiore catastale code.
	 * 
	 * @param comuneBelfioreCatastaleCode the comuneBelfioreCatastaleCode to set
	 */
	public void setComuneBelfioreCatastaleCode(String comuneBelfioreCatastaleCode) {
		this.comuneBelfioreCatastaleCode = comuneBelfioreCatastaleCode;
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
		siacRComuneProvincia.setSiacTComune(this);

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
		siacRComuneProvincia.setSiacTComune(null);

		return siacRComuneProvincia;
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
		siacRComuneRegione.setSiacTComune(this);

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
		siacRComuneRegione.setSiacTComune(null);

		return siacRComuneRegione;
	}

	/**
	 * Gets the siac t nazione.
	 *
	 * @return the siac t nazione
	 */
	public SiacTNazione getSiacTNazione() {
		return this.siacTNazione;
	}

	/**
	 * Sets the siac t nazione.
	 *
	 * @param siacTNazione the new siac t nazione
	 */
	public void setSiacTNazione(SiacTNazione siacTNazione) {
		this.siacTNazione = siacTNazione;
	}

	/**
	 * Gets the siac t indirizzo soggettos.
	 *
	 * @return the siac t indirizzo soggettos
	 */
	public List<SiacTIndirizzoSoggetto> getSiacTIndirizzoSoggettos() {
		return this.siacTIndirizzoSoggettos;
	}

	/**
	 * Sets the siac t indirizzo soggettos.
	 *
	 * @param siacTIndirizzoSoggettos the new siac t indirizzo soggettos
	 */
	public void setSiacTIndirizzoSoggettos(List<SiacTIndirizzoSoggetto> siacTIndirizzoSoggettos) {
		this.siacTIndirizzoSoggettos = siacTIndirizzoSoggettos;
	}

	/**
	 * Adds the siac t indirizzo soggetto.
	 *
	 * @param siacTIndirizzoSoggetto the siac t indirizzo soggetto
	 * @return the siac t indirizzo soggetto
	 */
	public SiacTIndirizzoSoggetto addSiacTIndirizzoSoggetto(SiacTIndirizzoSoggetto siacTIndirizzoSoggetto) {
		getSiacTIndirizzoSoggettos().add(siacTIndirizzoSoggetto);
		siacTIndirizzoSoggetto.setSiacTComune(this);

		return siacTIndirizzoSoggetto;
	}

	/**
	 * Removes the siac t indirizzo soggetto.
	 *
	 * @param siacTIndirizzoSoggetto the siac t indirizzo soggetto
	 * @return the siac t indirizzo soggetto
	 */
	public SiacTIndirizzoSoggetto removeSiacTIndirizzoSoggetto(SiacTIndirizzoSoggetto siacTIndirizzoSoggetto) {
		getSiacTIndirizzoSoggettos().remove(siacTIndirizzoSoggetto);
		siacTIndirizzoSoggetto.setSiacTComune(null);

		return siacTIndirizzoSoggetto;
	}

	/**
	 * Gets the siac t indirizzo soggetto mods.
	 *
	 * @return the siac t indirizzo soggetto mods
	 */
	public List<SiacTIndirizzoSoggettoMod> getSiacTIndirizzoSoggettoMods() {
		return this.siacTIndirizzoSoggettoMods;
	}

	/**
	 * Sets the siac t indirizzo soggetto mods.
	 *
	 * @param siacTIndirizzoSoggettoMods the new siac t indirizzo soggetto mods
	 */
	public void setSiacTIndirizzoSoggettoMods(List<SiacTIndirizzoSoggettoMod> siacTIndirizzoSoggettoMods) {
		this.siacTIndirizzoSoggettoMods = siacTIndirizzoSoggettoMods;
	}

	/**
	 * Adds the siac t indirizzo soggetto mod.
	 *
	 * @param siacTIndirizzoSoggettoMod the siac t indirizzo soggetto mod
	 * @return the siac t indirizzo soggetto mod
	 */
	public SiacTIndirizzoSoggettoMod addSiacTIndirizzoSoggettoMod(SiacTIndirizzoSoggettoMod siacTIndirizzoSoggettoMod) {
		getSiacTIndirizzoSoggettoMods().add(siacTIndirizzoSoggettoMod);
		siacTIndirizzoSoggettoMod.setSiacTComune(this);

		return siacTIndirizzoSoggettoMod;
	}

	/**
	 * Removes the siac t indirizzo soggetto mod.
	 *
	 * @param siacTIndirizzoSoggettoMod the siac t indirizzo soggetto mod
	 * @return the siac t indirizzo soggetto mod
	 */
	public SiacTIndirizzoSoggettoMod removeSiacTIndirizzoSoggettoMod(SiacTIndirizzoSoggettoMod siacTIndirizzoSoggettoMod) {
		getSiacTIndirizzoSoggettoMods().remove(siacTIndirizzoSoggettoMod);
		siacTIndirizzoSoggettoMod.setSiacTComune(null);

		return siacTIndirizzoSoggettoMod;
	}

	/**
	 * Gets the siac t persona fisicas.
	 *
	 * @return the siac t persona fisicas
	 */
	public List<SiacTPersonaFisica> getSiacTPersonaFisicas() {
		return this.siacTPersonaFisicas;
	}

	/**
	 * Sets the siac t persona fisicas.
	 *
	 * @param siacTPersonaFisicas the new siac t persona fisicas
	 */
	public void setSiacTPersonaFisicas(List<SiacTPersonaFisica> siacTPersonaFisicas) {
		this.siacTPersonaFisicas = siacTPersonaFisicas;
	}

	/**
	 * Adds the siac t persona fisica.
	 *
	 * @param siacTPersonaFisica the siac t persona fisica
	 * @return the siac t persona fisica
	 */
	public SiacTPersonaFisica addSiacTPersonaFisica(SiacTPersonaFisica siacTPersonaFisica) {
		getSiacTPersonaFisicas().add(siacTPersonaFisica);
		siacTPersonaFisica.setSiacTComune(this);

		return siacTPersonaFisica;
	}

	/**
	 * Removes the siac t persona fisica.
	 *
	 * @param siacTPersonaFisica the siac t persona fisica
	 * @return the siac t persona fisica
	 */
	public SiacTPersonaFisica removeSiacTPersonaFisica(SiacTPersonaFisica siacTPersonaFisica) {
		getSiacTPersonaFisicas().remove(siacTPersonaFisica);
		siacTPersonaFisica.setSiacTComune(null);

		return siacTPersonaFisica;
	}

	/**
	 * Gets the siac t persona fisica mods.
	 *
	 * @return the siac t persona fisica mods
	 */
	public List<SiacTPersonaFisicaMod> getSiacTPersonaFisicaMods() {
		return this.siacTPersonaFisicaMods;
	}

	/**
	 * Sets the siac t persona fisica mods.
	 *
	 * @param siacTPersonaFisicaMods the new siac t persona fisica mods
	 */
	public void setSiacTPersonaFisicaMods(List<SiacTPersonaFisicaMod> siacTPersonaFisicaMods) {
		this.siacTPersonaFisicaMods = siacTPersonaFisicaMods;
	}

	/**
	 * Adds the siac t persona fisica mod.
	 *
	 * @param siacTPersonaFisicaMod the siac t persona fisica mod
	 * @return the siac t persona fisica mod
	 */
	public SiacTPersonaFisicaMod addSiacTPersonaFisicaMod(SiacTPersonaFisicaMod siacTPersonaFisicaMod) {
		getSiacTPersonaFisicaMods().add(siacTPersonaFisicaMod);
		siacTPersonaFisicaMod.setSiacTComune(this);

		return siacTPersonaFisicaMod;
	}

	/**
	 * Removes the siac t persona fisica mod.
	 *
	 * @param siacTPersonaFisicaMod the siac t persona fisica mod
	 * @return the siac t persona fisica mod
	 */
	public SiacTPersonaFisicaMod removeSiacTPersonaFisicaMod(SiacTPersonaFisicaMod siacTPersonaFisicaMod) {
		getSiacTPersonaFisicaMods().remove(siacTPersonaFisicaMod);
		siacTPersonaFisicaMod.setSiacTComune(null);

		return siacTPersonaFisicaMod;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return comuneId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.comuneId = uid;
	}

}