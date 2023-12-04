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
 * The persistent class for the siac_d_recapito_modo database table.
 * 
 */
@Entity
@Table(name="siac_d_recapito_modo")
@NamedQuery(name="SiacDRecapitoModo.findAll", query="SELECT s FROM SiacDRecapitoModo s")
public class SiacDRecapitoModo extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The recapito modo id. */
	@Id
	@SequenceGenerator(name="SIAC_D_RECAPITO_MODO_RECAPITOMODOID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_RECAPITO_MODO_RECAPITO_MODO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_RECAPITO_MODO_RECAPITOMODOID_GENERATOR")
	@Column(name="recapito_modo_id")
	private Integer recapitoModoId;

	
	/** The recapito modo code. */
	@Column(name="recapito_modo_code")
	private String recapitoModoCode;

	/** The recapito modo desc. */
	@Column(name="recapito_modo_desc")
	private String recapitoModoDesc;

	
	//bi-directional many-to-one association to SiacTRecapitoSoggetto
	/** The siac t recapito soggettos. */
	@OneToMany(mappedBy="siacDRecapitoModo")
	private List<SiacTRecapitoSoggetto> siacTRecapitoSoggettos;

	//bi-directional many-to-one association to SiacTRecapitoSoggettoMod
	/** The siac t recapito soggetto mods. */
	@OneToMany(mappedBy="siacDRecapitoModo")
	private List<SiacTRecapitoSoggettoMod> siacTRecapitoSoggettoMods;

	/**
	 * Instantiates a new siac d recapito modo.
	 */
	public SiacDRecapitoModo() {
	}

	/**
	 * Gets the recapito modo id.
	 *
	 * @return the recapito modo id
	 */
	public Integer getRecapitoModoId() {
		return this.recapitoModoId;
	}

	/**
	 * Sets the recapito modo id.
	 *
	 * @param recapitoModoId the new recapito modo id
	 */
	public void setRecapitoModoId(Integer recapitoModoId) {
		this.recapitoModoId = recapitoModoId;
	}

	



	/**
	 * Gets the recapito modo code.
	 *
	 * @return the recapito modo code
	 */
	public String getRecapitoModoCode() {
		return this.recapitoModoCode;
	}

	/**
	 * Sets the recapito modo code.
	 *
	 * @param recapitoModoCode the new recapito modo code
	 */
	public void setRecapitoModoCode(String recapitoModoCode) {
		this.recapitoModoCode = recapitoModoCode;
	}

	/**
	 * Gets the recapito modo desc.
	 *
	 * @return the recapito modo desc
	 */
	public String getRecapitoModoDesc() {
		return this.recapitoModoDesc;
	}

	/**
	 * Sets the recapito modo desc.
	 *
	 * @param recapitoModoDesc the new recapito modo desc
	 */
	public void setRecapitoModoDesc(String recapitoModoDesc) {
		this.recapitoModoDesc = recapitoModoDesc;
	}

	/**
	 * Gets the siac t recapito soggettos.
	 *
	 * @return the siac t recapito soggettos
	 */
	public List<SiacTRecapitoSoggetto> getSiacTRecapitoSoggettos() {
		return this.siacTRecapitoSoggettos;
	}

	/**
	 * Sets the siac t recapito soggettos.
	 *
	 * @param siacTRecapitoSoggettos the new siac t recapito soggettos
	 */
	public void setSiacTRecapitoSoggettos(List<SiacTRecapitoSoggetto> siacTRecapitoSoggettos) {
		this.siacTRecapitoSoggettos = siacTRecapitoSoggettos;
	}

	/**
	 * Adds the siac t recapito soggetto.
	 *
	 * @param siacTRecapitoSoggetto the siac t recapito soggetto
	 * @return the siac t recapito soggetto
	 */
	public SiacTRecapitoSoggetto addSiacTRecapitoSoggetto(SiacTRecapitoSoggetto siacTRecapitoSoggetto) {
		getSiacTRecapitoSoggettos().add(siacTRecapitoSoggetto);
		siacTRecapitoSoggetto.setSiacDRecapitoModo(this);

		return siacTRecapitoSoggetto;
	}

	/**
	 * Removes the siac t recapito soggetto.
	 *
	 * @param siacTRecapitoSoggetto the siac t recapito soggetto
	 * @return the siac t recapito soggetto
	 */
	public SiacTRecapitoSoggetto removeSiacTRecapitoSoggetto(SiacTRecapitoSoggetto siacTRecapitoSoggetto) {
		getSiacTRecapitoSoggettos().remove(siacTRecapitoSoggetto);
		siacTRecapitoSoggetto.setSiacDRecapitoModo(null);

		return siacTRecapitoSoggetto;
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
		siacTRecapitoSoggettoMod.setSiacDRecapitoModo(this);

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
		siacTRecapitoSoggettoMod.setSiacDRecapitoModo(null);

		return siacTRecapitoSoggettoMod;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return recapitoModoId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.recapitoModoId = uid;
	}

}