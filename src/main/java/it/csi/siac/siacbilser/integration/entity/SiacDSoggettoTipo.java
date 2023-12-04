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
 * The persistent class for the siac_d_soggetto_tipo database table.
 * 
 */
@Entity
@Table(name="siac_d_soggetto_tipo")
@NamedQuery(name="SiacDSoggettoTipo.findAll", query="SELECT s FROM SiacDSoggettoTipo s")
public class SiacDSoggettoTipo extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The soggetto tipo id. */
	@Id
	@SequenceGenerator(name="SIAC_D_SOGGETTO_TIPO_SOGGETTOTIPOID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_SOGGETTO_TIPO_SOGGETTO_TIPO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_SOGGETTO_TIPO_SOGGETTOTIPOID_GENERATOR")
	@Column(name="soggetto_tipo_id")
	private Integer soggettoTipoId;

	/** The soggetto tipo code. */
	@Column(name="soggetto_tipo_code")
	private String soggettoTipoCode;

	/** The soggetto tipo desc. */
	@Column(name="soggetto_tipo_desc")
	private String soggettoTipoDesc;


	//bi-directional many-to-one association to SiacRSoggettoTipo
	/** The siac r soggetto tipos. */
	@OneToMany(mappedBy="siacDSoggettoTipo")
	private List<SiacRSoggettoTipo> siacRSoggettoTipos;

	//bi-directional many-to-one association to SiacRSoggettoTipoMod
	/** The siac r soggetto tipo mods. */
	@OneToMany(mappedBy="siacDSoggettoTipo")
	private List<SiacRSoggettoTipoMod> siacRSoggettoTipoMods;

	/**
	 * Instantiates a new siac d soggetto tipo.
	 */
	public SiacDSoggettoTipo() {
	}

	/**
	 * Gets the soggetto tipo id.
	 *
	 * @return the soggetto tipo id
	 */
	public Integer getSoggettoTipoId() {
		return this.soggettoTipoId;
	}

	/**
	 * Sets the soggetto tipo id.
	 *
	 * @param soggettoTipoId the new soggetto tipo id
	 */
	public void setSoggettoTipoId(Integer soggettoTipoId) {
		this.soggettoTipoId = soggettoTipoId;
	}

	/**
	 * Gets the soggetto tipo code.
	 *
	 * @return the soggetto tipo code
	 */
	public String getSoggettoTipoCode() {
		return this.soggettoTipoCode;
	}

	/**
	 * Sets the soggetto tipo code.
	 *
	 * @param soggettoTipoCode the new soggetto tipo code
	 */
	public void setSoggettoTipoCode(String soggettoTipoCode) {
		this.soggettoTipoCode = soggettoTipoCode;
	}

	/**
	 * Gets the soggetto tipo desc.
	 *
	 * @return the soggetto tipo desc
	 */
	public String getSoggettoTipoDesc() {
		return this.soggettoTipoDesc;
	}

	/**
	 * Sets the soggetto tipo desc.
	 *
	 * @param soggettoTipoDesc the new soggetto tipo desc
	 */
	public void setSoggettoTipoDesc(String soggettoTipoDesc) {
		this.soggettoTipoDesc = soggettoTipoDesc;
	}



	/**
	 * Gets the siac r soggetto tipos.
	 *
	 * @return the siac r soggetto tipos
	 */
	public List<SiacRSoggettoTipo> getSiacRSoggettoTipos() {
		return this.siacRSoggettoTipos;
	}

	/**
	 * Sets the siac r soggetto tipos.
	 *
	 * @param siacRSoggettoTipos the new siac r soggetto tipos
	 */
	public void setSiacRSoggettoTipos(List<SiacRSoggettoTipo> siacRSoggettoTipos) {
		this.siacRSoggettoTipos = siacRSoggettoTipos;
	}

	/**
	 * Adds the siac r soggetto tipo.
	 *
	 * @param siacRSoggettoTipo the siac r soggetto tipo
	 * @return the siac r soggetto tipo
	 */
	public SiacRSoggettoTipo addSiacRSoggettoTipo(SiacRSoggettoTipo siacRSoggettoTipo) {
		getSiacRSoggettoTipos().add(siacRSoggettoTipo);
		siacRSoggettoTipo.setSiacDSoggettoTipo(this);

		return siacRSoggettoTipo;
	}

	/**
	 * Removes the siac r soggetto tipo.
	 *
	 * @param siacRSoggettoTipo the siac r soggetto tipo
	 * @return the siac r soggetto tipo
	 */
	public SiacRSoggettoTipo removeSiacRSoggettoTipo(SiacRSoggettoTipo siacRSoggettoTipo) {
		getSiacRSoggettoTipos().remove(siacRSoggettoTipo);
		siacRSoggettoTipo.setSiacDSoggettoTipo(null);

		return siacRSoggettoTipo;
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
		siacRSoggettoTipoMod.setSiacDSoggettoTipo(this);

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
		siacRSoggettoTipoMod.setSiacDSoggettoTipo(null);

		return siacRSoggettoTipoMod;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return soggettoTipoId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.soggettoTipoId = uid;
		
	}

}