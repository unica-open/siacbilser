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
 * The persistent class for the siac_t_soggetto_mod database table.
 * 
 */
@Entity
@Table(name="siac_t_soggetto_mod")
@NamedQuery(name="SiacTSoggettoMod.findAll", query="SELECT s FROM SiacTSoggettoMod s")
public class SiacTSoggettoMod extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The sog mod id. */
	@Id
	@SequenceGenerator(name="SIAC_T_SOGGETTO_MOD_SOGMODID_GENERATOR", allocationSize=1, sequenceName="SIAC_T_SOGGETTO_MOD_SOG_MOD_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_SOGGETTO_MOD_SOGMODID_GENERATOR")
	@Column(name="sog_mod_id")
	private Integer sogModId;

	/** The ambito id. */
	@Column(name="ambito_id")
	private Integer ambitoId;

	/** The codice fiscale. */
	@Column(name="codice_fiscale")
	private String codiceFiscale;

	/** The codice fiscale estero. */
	@Column(name="codice_fiscale_estero")
	private String codiceFiscaleEstero;

	/** The partita iva. */
	@Column(name="partita_iva")
	private String partitaIva;

	/** The soggetto code. */
	@Column(name="soggetto_code")
	private String soggettoCode;

	/** The soggetto desc. */
	@Column(name="soggetto_desc")
	private String soggettoDesc;

	//bi-directional many-to-one association to SiacRFormaGiuridicaMod
	/** The siac r forma giuridica mods. */
	@OneToMany(mappedBy="siacTSoggettoMod")
	private List<SiacRFormaGiuridicaMod> siacRFormaGiuridicaMods;

	//bi-directional many-to-one association to SiacRSoggettoAttrMod
	/** The siac r soggetto attr mods. */
	@OneToMany(mappedBy="siacTSoggettoMod")
	private List<SiacRSoggettoAttrMod> siacRSoggettoAttrMods;

	//bi-directional many-to-one association to SiacRSoggettoClasseMod
	/** The siac r soggetto classe mods. */
	@OneToMany(mappedBy="siacTSoggettoMod")
	private List<SiacRSoggettoClasseMod> siacRSoggettoClasseMods;

	//bi-directional many-to-one association to SiacRSoggettoOnereMod
	/** The siac r soggetto onere mods. */
	@OneToMany(mappedBy="siacTSoggettoMod")
	private List<SiacRSoggettoOnereMod> siacRSoggettoOnereMods;

	//bi-directional many-to-one association to SiacRSoggettoTipoMod
	/** The siac r soggetto tipo mods. */
	@OneToMany(mappedBy="siacTSoggettoMod")
	private List<SiacRSoggettoTipoMod> siacRSoggettoTipoMods;

	//bi-directional many-to-one association to SiacTIndirizzoSoggettoMod
	/** The siac t indirizzo soggetto mods. */
	@OneToMany(mappedBy="siacTSoggettoMod")
	private List<SiacTIndirizzoSoggettoMod> siacTIndirizzoSoggettoMods;

	//bi-directional many-to-one association to SiacTPersonaFisicaMod
	/** The siac t persona fisica mods. */
	@OneToMany(mappedBy="siacTSoggettoMod")
	private List<SiacTPersonaFisicaMod> siacTPersonaFisicaMods;

	//bi-directional many-to-one association to SiacTPersonaGiuridicaMod
	/** The siac t persona giuridica mods. */
	@OneToMany(mappedBy="siacTSoggettoMod")
	private List<SiacTPersonaGiuridicaMod> siacTPersonaGiuridicaMods;

	//bi-directional many-to-one association to SiacTRecapitoSoggettoMod
	/** The siac t recapito soggetto mods. */
	@OneToMany(mappedBy="siacTSoggettoMod")
	private List<SiacTRecapitoSoggettoMod> siacTRecapitoSoggettoMods;

	//bi-directional many-to-one association to SiacTSoggetto
	/** The siac t soggetto. */
	@ManyToOne
	@JoinColumn(name="soggetto_id")
	private SiacTSoggetto siacTSoggetto;

	/**
	 * Instantiates a new siac t soggetto mod.
	 */
	public SiacTSoggettoMod() {
	}

	/**
	 * Gets the sog mod id.
	 *
	 * @return the sog mod id
	 */
	public Integer getSogModId() {
		return this.sogModId;
	}

	/**
	 * Sets the sog mod id.
	 *
	 * @param sogModId the new sog mod id
	 */
	public void setSogModId(Integer sogModId) {
		this.sogModId = sogModId;
	}

	/**
	 * Gets the ambito id.
	 *
	 * @return the ambito id
	 */
	public Integer getAmbitoId() {
		return this.ambitoId;
	}

	/**
	 * Sets the ambito id.
	 *
	 * @param ambitoId the new ambito id
	 */
	public void setAmbitoId(Integer ambitoId) {
		this.ambitoId = ambitoId;
	}

	/**
	 * Gets the codice fiscale.
	 *
	 * @return the codice fiscale
	 */
	public String getCodiceFiscale() {
		return this.codiceFiscale;
	}

	/**
	 * Sets the codice fiscale.
	 *
	 * @param codiceFiscale the new codice fiscale
	 */
	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}

	/**
	 * Gets the codice fiscale estero.
	 *
	 * @return the codice fiscale estero
	 */
	public String getCodiceFiscaleEstero() {
		return this.codiceFiscaleEstero;
	}

	/**
	 * Sets the codice fiscale estero.
	 *
	 * @param codiceFiscaleEstero the new codice fiscale estero
	 */
	public void setCodiceFiscaleEstero(String codiceFiscaleEstero) {
		this.codiceFiscaleEstero = codiceFiscaleEstero;
	}

	/**
	 * Gets the partita iva.
	 *
	 * @return the partita iva
	 */
	public String getPartitaIva() {
		return this.partitaIva;
	}

	/**
	 * Sets the partita iva.
	 *
	 * @param partitaIva the new partita iva
	 */
	public void setPartitaIva(String partitaIva) {
		this.partitaIva = partitaIva;
	}

	/**
	 * Gets the soggetto code.
	 *
	 * @return the soggetto code
	 */
	public String getSoggettoCode() {
		return this.soggettoCode;
	}

	/**
	 * Sets the soggetto code.
	 *
	 * @param soggettoCode the new soggetto code
	 */
	public void setSoggettoCode(String soggettoCode) {
		this.soggettoCode = soggettoCode;
	}

	/**
	 * Gets the soggetto desc.
	 *
	 * @return the soggetto desc
	 */
	public String getSoggettoDesc() {
		return this.soggettoDesc;
	}

	/**
	 * Sets the soggetto desc.
	 *
	 * @param soggettoDesc the new soggetto desc
	 */
	public void setSoggettoDesc(String soggettoDesc) {
		this.soggettoDesc = soggettoDesc;
	}

	/**
	 * Gets the siac r forma giuridica mods.
	 *
	 * @return the siac r forma giuridica mods
	 */
	public List<SiacRFormaGiuridicaMod> getSiacRFormaGiuridicaMods() {
		return this.siacRFormaGiuridicaMods;
	}

	/**
	 * Sets the siac r forma giuridica mods.
	 *
	 * @param siacRFormaGiuridicaMods the new siac r forma giuridica mods
	 */
	public void setSiacRFormaGiuridicaMods(List<SiacRFormaGiuridicaMod> siacRFormaGiuridicaMods) {
		this.siacRFormaGiuridicaMods = siacRFormaGiuridicaMods;
	}

	/**
	 * Adds the siac r forma giuridica mod.
	 *
	 * @param siacRFormaGiuridicaMod the siac r forma giuridica mod
	 * @return the siac r forma giuridica mod
	 */
	public SiacRFormaGiuridicaMod addSiacRFormaGiuridicaMod(SiacRFormaGiuridicaMod siacRFormaGiuridicaMod) {
		getSiacRFormaGiuridicaMods().add(siacRFormaGiuridicaMod);
		siacRFormaGiuridicaMod.setSiacTSoggettoMod(this);

		return siacRFormaGiuridicaMod;
	}

	/**
	 * Removes the siac r forma giuridica mod.
	 *
	 * @param siacRFormaGiuridicaMod the siac r forma giuridica mod
	 * @return the siac r forma giuridica mod
	 */
	public SiacRFormaGiuridicaMod removeSiacRFormaGiuridicaMod(SiacRFormaGiuridicaMod siacRFormaGiuridicaMod) {
		getSiacRFormaGiuridicaMods().remove(siacRFormaGiuridicaMod);
		siacRFormaGiuridicaMod.setSiacTSoggettoMod(null);

		return siacRFormaGiuridicaMod;
	}

	/**
	 * Gets the siac r soggetto attr mods.
	 *
	 * @return the siac r soggetto attr mods
	 */
	public List<SiacRSoggettoAttrMod> getSiacRSoggettoAttrMods() {
		return this.siacRSoggettoAttrMods;
	}

	/**
	 * Sets the siac r soggetto attr mods.
	 *
	 * @param siacRSoggettoAttrMods the new siac r soggetto attr mods
	 */
	public void setSiacRSoggettoAttrMods(List<SiacRSoggettoAttrMod> siacRSoggettoAttrMods) {
		this.siacRSoggettoAttrMods = siacRSoggettoAttrMods;
	}

	/**
	 * Adds the siac r soggetto attr mod.
	 *
	 * @param siacRSoggettoAttrMod the siac r soggetto attr mod
	 * @return the siac r soggetto attr mod
	 */
	public SiacRSoggettoAttrMod addSiacRSoggettoAttrMod(SiacRSoggettoAttrMod siacRSoggettoAttrMod) {
		getSiacRSoggettoAttrMods().add(siacRSoggettoAttrMod);
		siacRSoggettoAttrMod.setSiacTSoggettoMod(this);

		return siacRSoggettoAttrMod;
	}

	/**
	 * Removes the siac r soggetto attr mod.
	 *
	 * @param siacRSoggettoAttrMod the siac r soggetto attr mod
	 * @return the siac r soggetto attr mod
	 */
	public SiacRSoggettoAttrMod removeSiacRSoggettoAttrMod(SiacRSoggettoAttrMod siacRSoggettoAttrMod) {
		getSiacRSoggettoAttrMods().remove(siacRSoggettoAttrMod);
		siacRSoggettoAttrMod.setSiacTSoggettoMod(null);

		return siacRSoggettoAttrMod;
	}

	/**
	 * Gets the siac r soggetto classe mods.
	 *
	 * @return the siac r soggetto classe mods
	 */
	public List<SiacRSoggettoClasseMod> getSiacRSoggettoClasseMods() {
		return this.siacRSoggettoClasseMods;
	}

	/**
	 * Sets the siac r soggetto classe mods.
	 *
	 * @param siacRSoggettoClasseMods the new siac r soggetto classe mods
	 */
	public void setSiacRSoggettoClasseMods(List<SiacRSoggettoClasseMod> siacRSoggettoClasseMods) {
		this.siacRSoggettoClasseMods = siacRSoggettoClasseMods;
	}

	/**
	 * Adds the siac r soggetto classe mod.
	 *
	 * @param siacRSoggettoClasseMod the siac r soggetto classe mod
	 * @return the siac r soggetto classe mod
	 */
	public SiacRSoggettoClasseMod addSiacRSoggettoClasseMod(SiacRSoggettoClasseMod siacRSoggettoClasseMod) {
		getSiacRSoggettoClasseMods().add(siacRSoggettoClasseMod);
		siacRSoggettoClasseMod.setSiacTSoggettoMod(this);

		return siacRSoggettoClasseMod;
	}

	/**
	 * Removes the siac r soggetto classe mod.
	 *
	 * @param siacRSoggettoClasseMod the siac r soggetto classe mod
	 * @return the siac r soggetto classe mod
	 */
	public SiacRSoggettoClasseMod removeSiacRSoggettoClasseMod(SiacRSoggettoClasseMod siacRSoggettoClasseMod) {
		getSiacRSoggettoClasseMods().remove(siacRSoggettoClasseMod);
		siacRSoggettoClasseMod.setSiacTSoggettoMod(null);

		return siacRSoggettoClasseMod;
	}

	/**
	 * Gets the siac r soggetto onere mods.
	 *
	 * @return the siac r soggetto onere mods
	 */
	public List<SiacRSoggettoOnereMod> getSiacRSoggettoOnereMods() {
		return this.siacRSoggettoOnereMods;
	}

	/**
	 * Sets the siac r soggetto onere mods.
	 *
	 * @param siacRSoggettoOnereMods the new siac r soggetto onere mods
	 */
	public void setSiacRSoggettoOnereMods(List<SiacRSoggettoOnereMod> siacRSoggettoOnereMods) {
		this.siacRSoggettoOnereMods = siacRSoggettoOnereMods;
	}

	/**
	 * Adds the siac r soggetto onere mod.
	 *
	 * @param siacRSoggettoOnereMod the siac r soggetto onere mod
	 * @return the siac r soggetto onere mod
	 */
	public SiacRSoggettoOnereMod addSiacRSoggettoOnereMod(SiacRSoggettoOnereMod siacRSoggettoOnereMod) {
		getSiacRSoggettoOnereMods().add(siacRSoggettoOnereMod);
		siacRSoggettoOnereMod.setSiacTSoggettoMod(this);

		return siacRSoggettoOnereMod;
	}

	/**
	 * Removes the siac r soggetto onere mod.
	 *
	 * @param siacRSoggettoOnereMod the siac r soggetto onere mod
	 * @return the siac r soggetto onere mod
	 */
	public SiacRSoggettoOnereMod removeSiacRSoggettoOnereMod(SiacRSoggettoOnereMod siacRSoggettoOnereMod) {
		getSiacRSoggettoOnereMods().remove(siacRSoggettoOnereMod);
		siacRSoggettoOnereMod.setSiacTSoggettoMod(null);

		return siacRSoggettoOnereMod;
	}

	/**
	 * Gets the siac r soggetto tipo mods.
	 *
	 * @return the siac r soggetto tipo mods
	 */
	public List<SiacRSoggettoTipoMod> getSiacRSoggettoTipoMods() {
		return this.siacRSoggettoTipoMods;
	}

	/**
	 * Sets the siac r soggetto tipo mods.
	 *
	 * @param siacRSoggettoTipoMods the new siac r soggetto tipo mods
	 */
	public void setSiacRSoggettoTipoMods(List<SiacRSoggettoTipoMod> siacRSoggettoTipoMods) {
		this.siacRSoggettoTipoMods = siacRSoggettoTipoMods;
	}

	/**
	 * Adds the siac r soggetto tipo mod.
	 *
	 * @param siacRSoggettoTipoMod the siac r soggetto tipo mod
	 * @return the siac r soggetto tipo mod
	 */
	public SiacRSoggettoTipoMod addSiacRSoggettoTipoMod(SiacRSoggettoTipoMod siacRSoggettoTipoMod) {
		getSiacRSoggettoTipoMods().add(siacRSoggettoTipoMod);
		siacRSoggettoTipoMod.setSiacTSoggettoMod(this);

		return siacRSoggettoTipoMod;
	}

	/**
	 * Removes the siac r soggetto tipo mod.
	 *
	 * @param siacRSoggettoTipoMod the siac r soggetto tipo mod
	 * @return the siac r soggetto tipo mod
	 */
	public SiacRSoggettoTipoMod removeSiacRSoggettoTipoMod(SiacRSoggettoTipoMod siacRSoggettoTipoMod) {
		getSiacRSoggettoTipoMods().remove(siacRSoggettoTipoMod);
		siacRSoggettoTipoMod.setSiacTSoggettoMod(null);

		return siacRSoggettoTipoMod;
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
		siacTIndirizzoSoggettoMod.setSiacTSoggettoMod(this);

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
		siacTIndirizzoSoggettoMod.setSiacTSoggettoMod(null);

		return siacTIndirizzoSoggettoMod;
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
		siacTPersonaFisicaMod.setSiacTSoggettoMod(this);

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
		siacTPersonaFisicaMod.setSiacTSoggettoMod(null);

		return siacTPersonaFisicaMod;
	}

	/**
	 * Gets the siac t persona giuridica mods.
	 *
	 * @return the siac t persona giuridica mods
	 */
	public List<SiacTPersonaGiuridicaMod> getSiacTPersonaGiuridicaMods() {
		return this.siacTPersonaGiuridicaMods;
	}

	/**
	 * Sets the siac t persona giuridica mods.
	 *
	 * @param siacTPersonaGiuridicaMods the new siac t persona giuridica mods
	 */
	public void setSiacTPersonaGiuridicaMods(List<SiacTPersonaGiuridicaMod> siacTPersonaGiuridicaMods) {
		this.siacTPersonaGiuridicaMods = siacTPersonaGiuridicaMods;
	}

	/**
	 * Adds the siac t persona giuridica mod.
	 *
	 * @param siacTPersonaGiuridicaMod the siac t persona giuridica mod
	 * @return the siac t persona giuridica mod
	 */
	public SiacTPersonaGiuridicaMod addSiacTPersonaGiuridicaMod(SiacTPersonaGiuridicaMod siacTPersonaGiuridicaMod) {
		getSiacTPersonaGiuridicaMods().add(siacTPersonaGiuridicaMod);
		siacTPersonaGiuridicaMod.setSiacTSoggettoMod(this);

		return siacTPersonaGiuridicaMod;
	}

	/**
	 * Removes the siac t persona giuridica mod.
	 *
	 * @param siacTPersonaGiuridicaMod the siac t persona giuridica mod
	 * @return the siac t persona giuridica mod
	 */
	public SiacTPersonaGiuridicaMod removeSiacTPersonaGiuridicaMod(SiacTPersonaGiuridicaMod siacTPersonaGiuridicaMod) {
		getSiacTPersonaGiuridicaMods().remove(siacTPersonaGiuridicaMod);
		siacTPersonaGiuridicaMod.setSiacTSoggettoMod(null);

		return siacTPersonaGiuridicaMod;
	}

	/**
	 * Gets the siac t recapito soggetto mods.
	 *
	 * @return the siac t recapito soggetto mods
	 */
	public List<SiacTRecapitoSoggettoMod> getSiacTRecapitoSoggettoMods() {
		return this.siacTRecapitoSoggettoMods;
	}

	/**
	 * Sets the siac t recapito soggetto mods.
	 *
	 * @param siacTRecapitoSoggettoMods the new siac t recapito soggetto mods
	 */
	public void setSiacTRecapitoSoggettoMods(List<SiacTRecapitoSoggettoMod> siacTRecapitoSoggettoMods) {
		this.siacTRecapitoSoggettoMods = siacTRecapitoSoggettoMods;
	}

	/**
	 * Adds the siac t recapito soggetto mod.
	 *
	 * @param siacTRecapitoSoggettoMod the siac t recapito soggetto mod
	 * @return the siac t recapito soggetto mod
	 */
	public SiacTRecapitoSoggettoMod addSiacTRecapitoSoggettoMod(SiacTRecapitoSoggettoMod siacTRecapitoSoggettoMod) {
		getSiacTRecapitoSoggettoMods().add(siacTRecapitoSoggettoMod);
		siacTRecapitoSoggettoMod.setSiacTSoggettoMod(this);

		return siacTRecapitoSoggettoMod;
	}

	/**
	 * Removes the siac t recapito soggetto mod.
	 *
	 * @param siacTRecapitoSoggettoMod the siac t recapito soggetto mod
	 * @return the siac t recapito soggetto mod
	 */
	public SiacTRecapitoSoggettoMod removeSiacTRecapitoSoggettoMod(SiacTRecapitoSoggettoMod siacTRecapitoSoggettoMod) {
		getSiacTRecapitoSoggettoMods().remove(siacTRecapitoSoggettoMod);
		siacTRecapitoSoggettoMod.setSiacTSoggettoMod(null);

		return siacTRecapitoSoggettoMod;
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
		return sogModId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		sogModId = uid;
		
	}

}