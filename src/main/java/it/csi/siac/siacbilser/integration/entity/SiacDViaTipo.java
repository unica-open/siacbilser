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
 * The persistent class for the siac_d_via_tipo database table.
 * 
 */
@Entity
@Table(name="siac_d_via_tipo")
@NamedQuery(name="SiacDViaTipo.findAll", query="SELECT s FROM SiacDViaTipo s")
public class SiacDViaTipo extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The via tipo id. */
	@Id
	@SequenceGenerator(name="SIAC_D_VIA_TIPO_VIATIPOID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_VIA_TIPO_VIA_TIPO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_VIA_TIPO_VIATIPOID_GENERATOR")
	@Column(name="via_tipo_id")
	private Integer viaTipoId;

	

	/** The via tipo code. */
	@Column(name="via_tipo_code")
	private String viaTipoCode;

	/** The via tipo desc. */
	@Column(name="via_tipo_desc")
	private String viaTipoDesc;

	
	//bi-directional many-to-one association to SiacTIndirizzoSoggetto
	/** The siac t indirizzo soggettos. */
	@OneToMany(mappedBy="siacDViaTipo")
	private List<SiacTIndirizzoSoggetto> siacTIndirizzoSoggettos;

	//bi-directional many-to-one association to SiacTIndirizzoSoggettoMod
	/** The siac t indirizzo soggetto mods. */
	@OneToMany(mappedBy="siacDViaTipo")
	private List<SiacTIndirizzoSoggettoMod> siacTIndirizzoSoggettoMods;

	/**
	 * Instantiates a new siac d via tipo.
	 */
	public SiacDViaTipo() {
	}

	/**
	 * Gets the via tipo id.
	 *
	 * @return the via tipo id
	 */
	public Integer getViaTipoId() {
		return this.viaTipoId;
	}

	/**
	 * Sets the via tipo id.
	 *
	 * @param viaTipoId the new via tipo id
	 */
	public void setViaTipoId(Integer viaTipoId) {
		this.viaTipoId = viaTipoId;
	}

	/**
	 * Gets the via tipo code.
	 *
	 * @return the via tipo code
	 */
	public String getViaTipoCode() {
		return this.viaTipoCode;
	}

	/**
	 * Sets the via tipo code.
	 *
	 * @param viaTipoCode the new via tipo code
	 */
	public void setViaTipoCode(String viaTipoCode) {
		this.viaTipoCode = viaTipoCode;
	}

	/**
	 * Gets the via tipo desc.
	 *
	 * @return the via tipo desc
	 */
	public String getViaTipoDesc() {
		return this.viaTipoDesc;
	}

	/**
	 * Sets the via tipo desc.
	 *
	 * @param viaTipoDesc the new via tipo desc
	 */
	public void setViaTipoDesc(String viaTipoDesc) {
		this.viaTipoDesc = viaTipoDesc;
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
		siacTIndirizzoSoggetto.setSiacDViaTipo(this);

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
		siacTIndirizzoSoggetto.setSiacDViaTipo(null);

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
		siacTIndirizzoSoggettoMod.setSiacDViaTipo(this);

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
		siacTIndirizzoSoggettoMod.setSiacDViaTipo(null);

		return siacTIndirizzoSoggettoMod;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return viaTipoId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.viaTipoId = uid;
	}

}