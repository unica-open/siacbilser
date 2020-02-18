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
 * The persistent class for the siac_d_indirizzo_tipo database table.
 * 
 */
@Entity
@Table(name="siac_d_indirizzo_tipo")
@NamedQuery(name="SiacDIndirizzoTipo.findAll", query="SELECT s FROM SiacDIndirizzoTipo s")
public class SiacDIndirizzoTipo extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The indirizzo tipo id. */
	@Id
	@SequenceGenerator(name="SIAC_D_INDIRIZZO_TIPO_INDIRIZZOTIPOID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_INDIRIZZO_TIPO_INDIRIZZO_TIPO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_INDIRIZZO_TIPO_INDIRIZZOTIPOID_GENERATOR")
	@Column(name="indirizzo_tipo_id")
	private Integer indirizzoTipoId;

	
	/** The indirizzo tipo code. */
	@Column(name="indirizzo_tipo_code")
	private String indirizzoTipoCode;

	/** The indirizzo tipo desc. */
	@Column(name="indirizzo_tipo_desc")
	private String indirizzoTipoDesc;

	

	//bi-directional many-to-one association to SiacRIndirizzoSoggettoTipo
	/** The siac r indirizzo soggetto tipos. */
	@OneToMany(mappedBy="siacDIndirizzoTipo")
	private List<SiacRIndirizzoSoggettoTipo> siacRIndirizzoSoggettoTipos;

	//bi-directional many-to-one association to SiacRIndirizzoSoggettoTipoMod
	/** The siac r indirizzo soggetto tipo mods. */
	@OneToMany(mappedBy="siacDIndirizzoTipo")
	private List<SiacRIndirizzoSoggettoTipoMod> siacRIndirizzoSoggettoTipoMods;

	/**
	 * Instantiates a new siac d indirizzo tipo.
	 */
	public SiacDIndirizzoTipo() {
	}

	/**
	 * Gets the indirizzo tipo id.
	 *
	 * @return the indirizzo tipo id
	 */
	public Integer getIndirizzoTipoId() {
		return this.indirizzoTipoId;
	}

	/**
	 * Sets the indirizzo tipo id.
	 *
	 * @param indirizzoTipoId the new indirizzo tipo id
	 */
	public void setIndirizzoTipoId(Integer indirizzoTipoId) {
		this.indirizzoTipoId = indirizzoTipoId;
	}

	

	/**
	 * Gets the indirizzo tipo code.
	 *
	 * @return the indirizzo tipo code
	 */
	public String getIndirizzoTipoCode() {
		return this.indirizzoTipoCode;
	}

	/**
	 * Sets the indirizzo tipo code.
	 *
	 * @param indirizzoTipoCode the new indirizzo tipo code
	 */
	public void setIndirizzoTipoCode(String indirizzoTipoCode) {
		this.indirizzoTipoCode = indirizzoTipoCode;
	}

	/**
	 * Gets the indirizzo tipo desc.
	 *
	 * @return the indirizzo tipo desc
	 */
	public String getIndirizzoTipoDesc() {
		return this.indirizzoTipoDesc;
	}

	/**
	 * Sets the indirizzo tipo desc.
	 *
	 * @param indirizzoTipoDesc the new indirizzo tipo desc
	 */
	public void setIndirizzoTipoDesc(String indirizzoTipoDesc) {
		this.indirizzoTipoDesc = indirizzoTipoDesc;
	}

	/**
	 * Gets the siac r indirizzo soggetto tipos.
	 *
	 * @return the siac r indirizzo soggetto tipos
	 */
	public List<SiacRIndirizzoSoggettoTipo> getSiacRIndirizzoSoggettoTipos() {
		return this.siacRIndirizzoSoggettoTipos;
	}

	/**
	 * Sets the siac r indirizzo soggetto tipos.
	 *
	 * @param siacRIndirizzoSoggettoTipos the new siac r indirizzo soggetto tipos
	 */
	public void setSiacRIndirizzoSoggettoTipos(List<SiacRIndirizzoSoggettoTipo> siacRIndirizzoSoggettoTipos) {
		this.siacRIndirizzoSoggettoTipos = siacRIndirizzoSoggettoTipos;
	}

	/**
	 * Adds the siac r indirizzo soggetto tipo.
	 *
	 * @param siacRIndirizzoSoggettoTipo the siac r indirizzo soggetto tipo
	 * @return the siac r indirizzo soggetto tipo
	 */
	public SiacRIndirizzoSoggettoTipo addSiacRIndirizzoSoggettoTipo(SiacRIndirizzoSoggettoTipo siacRIndirizzoSoggettoTipo) {
		getSiacRIndirizzoSoggettoTipos().add(siacRIndirizzoSoggettoTipo);
		siacRIndirizzoSoggettoTipo.setSiacDIndirizzoTipo(this);

		return siacRIndirizzoSoggettoTipo;
	}

	/**
	 * Removes the siac r indirizzo soggetto tipo.
	 *
	 * @param siacRIndirizzoSoggettoTipo the siac r indirizzo soggetto tipo
	 * @return the siac r indirizzo soggetto tipo
	 */
	public SiacRIndirizzoSoggettoTipo removeSiacRIndirizzoSoggettoTipo(SiacRIndirizzoSoggettoTipo siacRIndirizzoSoggettoTipo) {
		getSiacRIndirizzoSoggettoTipos().remove(siacRIndirizzoSoggettoTipo);
		siacRIndirizzoSoggettoTipo.setSiacDIndirizzoTipo(null);

		return siacRIndirizzoSoggettoTipo;
	}

	/**
	 * Gets the siac r indirizzo soggetto tipo mods.
	 *
	 * @return the siac r indirizzo soggetto tipo mods
	 */
	public List<SiacRIndirizzoSoggettoTipoMod> getSiacRIndirizzoSoggettoTipoMods() {
		return this.siacRIndirizzoSoggettoTipoMods;
	}

	/**
	 * Sets the siac r indirizzo soggetto tipo mods.
	 *
	 * @param siacRIndirizzoSoggettoTipoMods the new siac r indirizzo soggetto tipo mods
	 */
	public void setSiacRIndirizzoSoggettoTipoMods(List<SiacRIndirizzoSoggettoTipoMod> siacRIndirizzoSoggettoTipoMods) {
		this.siacRIndirizzoSoggettoTipoMods = siacRIndirizzoSoggettoTipoMods;
	}

	/**
	 * Adds the siac r indirizzo soggetto tipo mod.
	 *
	 * @param siacRIndirizzoSoggettoTipoMod the siac r indirizzo soggetto tipo mod
	 * @return the siac r indirizzo soggetto tipo mod
	 */
	public SiacRIndirizzoSoggettoTipoMod addSiacRIndirizzoSoggettoTipoMod(SiacRIndirizzoSoggettoTipoMod siacRIndirizzoSoggettoTipoMod) {
		getSiacRIndirizzoSoggettoTipoMods().add(siacRIndirizzoSoggettoTipoMod);
		siacRIndirizzoSoggettoTipoMod.setSiacDIndirizzoTipo(this);

		return siacRIndirizzoSoggettoTipoMod;
	}

	/**
	 * Removes the siac r indirizzo soggetto tipo mod.
	 *
	 * @param siacRIndirizzoSoggettoTipoMod the siac r indirizzo soggetto tipo mod
	 * @return the siac r indirizzo soggetto tipo mod
	 */
	public SiacRIndirizzoSoggettoTipoMod removeSiacRIndirizzoSoggettoTipoMod(SiacRIndirizzoSoggettoTipoMod siacRIndirizzoSoggettoTipoMod) {
		getSiacRIndirizzoSoggettoTipoMods().remove(siacRIndirizzoSoggettoTipoMod);
		siacRIndirizzoSoggettoTipoMod.setSiacDIndirizzoTipo(null);

		return siacRIndirizzoSoggettoTipoMod;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return indirizzoTipoId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.indirizzoTipoId = uid;
	}

}