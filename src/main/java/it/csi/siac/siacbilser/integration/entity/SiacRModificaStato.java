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
 * The persistent class for the siac_r_modifica_stato database table.
 * 
 */
@Entity
@Table(name="siac_r_modifica_stato")
@NamedQuery(name="SiacRModificaStato.findAll", query="SELECT s FROM SiacRModificaStato s")
public class SiacRModificaStato extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The mod stato r id. */
	@Id
	@SequenceGenerator(name="SIAC_R_MODIFICA_STATO_MODSTATORID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_MODIFICA_STATO_MOD_STATO_R_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_MODIFICA_STATO_MODSTATORID_GENERATOR")
	@Column(name="mod_stato_r_id")
	private Integer modStatoRId;

	//bi-directional many-to-one association to SiacDModificaStato
	/** The siac d modifica stato. */
	@ManyToOne
	@JoinColumn(name="mod_stato_id")
	private SiacDModificaStato siacDModificaStato;

	//bi-directional many-to-one association to SiacTModifica
	/** The siac t modifica. */
	@ManyToOne
	@JoinColumn(name="mod_id")
	private SiacTModifica siacTModifica;

	//bi-directional many-to-one association to SiacRMovgestTsSogMod
	/** The siac r movgest ts sog mods. */
	@OneToMany(mappedBy="siacRModificaStato")
	private List<SiacRMovgestTsSogMod> siacRMovgestTsSogMods;

	//bi-directional many-to-one association to SiacRMovgestTsSogclasseMod
	/** The siac r movgest ts sogclasse mods. */
	@OneToMany(mappedBy="siacRModificaStato")
	private List<SiacRMovgestTsSogclasseMod> siacRMovgestTsSogclasseMods;

	//bi-directional many-to-one association to SiacTMovgestTsDetMod
	/** The siac t movgest ts det mods. */
	@OneToMany(mappedBy="siacRModificaStato")
	private List<SiacTMovgestTsDetMod> siacTMovgestTsDetMods;

	/**
	 * Instantiates a new siac r modifica stato.
	 */
	public SiacRModificaStato() {
	}

	/**
	 * Gets the mod stato r id.
	 *
	 * @return the mod stato r id
	 */
	public Integer getModStatoRId() {
		return this.modStatoRId;
	}

	/**
	 * Sets the mod stato r id.
	 *
	 * @param modStatoRId the new mod stato r id
	 */
	public void setModStatoRId(Integer modStatoRId) {
		this.modStatoRId = modStatoRId;
	}

	

	/**
	 * Gets the siac d modifica stato.
	 *
	 * @return the siac d modifica stato
	 */
	public SiacDModificaStato getSiacDModificaStato() {
		return this.siacDModificaStato;
	}

	/**
	 * Sets the siac d modifica stato.
	 *
	 * @param siacDModificaStato the new siac d modifica stato
	 */
	public void setSiacDModificaStato(SiacDModificaStato siacDModificaStato) {
		this.siacDModificaStato = siacDModificaStato;
	}

	

	/**
	 * Gets the siac t modifica.
	 *
	 * @return the siac t modifica
	 */
	public SiacTModifica getSiacTModifica() {
		return this.siacTModifica;
	}

	/**
	 * Sets the siac t modifica.
	 *
	 * @param siacTModifica the new siac t modifica
	 */
	public void setSiacTModifica(SiacTModifica siacTModifica) {
		this.siacTModifica = siacTModifica;
	}

	/**
	 * Gets the siac r movgest ts sog mods.
	 *
	 * @return the siac r movgest ts sog mods
	 */
	public List<SiacRMovgestTsSogMod> getSiacRMovgestTsSogMods() {
		return this.siacRMovgestTsSogMods;
	}

	/**
	 * Sets the siac r movgest ts sog mods.
	 *
	 * @param siacRMovgestTsSogMods the new siac r movgest ts sog mods
	 */
	public void setSiacRMovgestTsSogMods(List<SiacRMovgestTsSogMod> siacRMovgestTsSogMods) {
		this.siacRMovgestTsSogMods = siacRMovgestTsSogMods;
	}

	/**
	 * Adds the siac r movgest ts sog mod.
	 *
	 * @param siacRMovgestTsSogMod the siac r movgest ts sog mod
	 * @return the siac r movgest ts sog mod
	 */
	public SiacRMovgestTsSogMod addSiacRMovgestTsSogMod(SiacRMovgestTsSogMod siacRMovgestTsSogMod) {
		getSiacRMovgestTsSogMods().add(siacRMovgestTsSogMod);
		siacRMovgestTsSogMod.setSiacRModificaStato(this);

		return siacRMovgestTsSogMod;
	}

	/**
	 * Removes the siac r movgest ts sog mod.
	 *
	 * @param siacRMovgestTsSogMod the siac r movgest ts sog mod
	 * @return the siac r movgest ts sog mod
	 */
	public SiacRMovgestTsSogMod removeSiacRMovgestTsSogMod(SiacRMovgestTsSogMod siacRMovgestTsSogMod) {
		getSiacRMovgestTsSogMods().remove(siacRMovgestTsSogMod);
		siacRMovgestTsSogMod.setSiacRModificaStato(null);

		return siacRMovgestTsSogMod;
	}

	/**
	 * Gets the siac r movgest ts sogclasse mods.
	 *
	 * @return the siac r movgest ts sogclasse mods
	 */
	public List<SiacRMovgestTsSogclasseMod> getSiacRMovgestTsSogclasseMods() {
		return this.siacRMovgestTsSogclasseMods;
	}

	/**
	 * Sets the siac r movgest ts sogclasse mods.
	 *
	 * @param siacRMovgestTsSogclasseMods the new siac r movgest ts sogclasse mods
	 */
	public void setSiacRMovgestTsSogclasseMods(List<SiacRMovgestTsSogclasseMod> siacRMovgestTsSogclasseMods) {
		this.siacRMovgestTsSogclasseMods = siacRMovgestTsSogclasseMods;
	}

	/**
	 * Adds the siac r movgest ts sogclasse mod.
	 *
	 * @param siacRMovgestTsSogclasseMod the siac r movgest ts sogclasse mod
	 * @return the siac r movgest ts sogclasse mod
	 */
	public SiacRMovgestTsSogclasseMod addSiacRMovgestTsSogclasseMod(SiacRMovgestTsSogclasseMod siacRMovgestTsSogclasseMod) {
		getSiacRMovgestTsSogclasseMods().add(siacRMovgestTsSogclasseMod);
		siacRMovgestTsSogclasseMod.setSiacRModificaStato(this);

		return siacRMovgestTsSogclasseMod;
	}

	/**
	 * Removes the siac r movgest ts sogclasse mod.
	 *
	 * @param siacRMovgestTsSogclasseMod the siac r movgest ts sogclasse mod
	 * @return the siac r movgest ts sogclasse mod
	 */
	public SiacRMovgestTsSogclasseMod removeSiacRMovgestTsSogclasseMod(SiacRMovgestTsSogclasseMod siacRMovgestTsSogclasseMod) {
		getSiacRMovgestTsSogclasseMods().remove(siacRMovgestTsSogclasseMod);
		siacRMovgestTsSogclasseMod.setSiacRModificaStato(null);

		return siacRMovgestTsSogclasseMod;
	}

	/**
	 * Gets the siac t movgest ts det mods.
	 *
	 * @return the siac t movgest ts det mods
	 */
	public List<SiacTMovgestTsDetMod> getSiacTMovgestTsDetMods() {
		return this.siacTMovgestTsDetMods;
	}

	/**
	 * Sets the siac t movgest ts det mods.
	 *
	 * @param siacTMovgestTsDetMods the new siac t movgest ts det mods
	 */
	public void setSiacTMovgestTsDetMods(List<SiacTMovgestTsDetMod> siacTMovgestTsDetMods) {
		this.siacTMovgestTsDetMods = siacTMovgestTsDetMods;
	}

	/**
	 * Adds the siac t movgest ts det mod.
	 *
	 * @param siacTMovgestTsDetMod the siac t movgest ts det mod
	 * @return the siac t movgest ts det mod
	 */
	public SiacTMovgestTsDetMod addSiacTMovgestTsDetMod(SiacTMovgestTsDetMod siacTMovgestTsDetMod) {
		getSiacTMovgestTsDetMods().add(siacTMovgestTsDetMod);
		siacTMovgestTsDetMod.setSiacRModificaStato(this);

		return siacTMovgestTsDetMod;
	}

	/**
	 * Removes the siac t movgest ts det mod.
	 *
	 * @param siacTMovgestTsDetMod the siac t movgest ts det mod
	 * @return the siac t movgest ts det mod
	 */
	public SiacTMovgestTsDetMod removeSiacTMovgestTsDetMod(SiacTMovgestTsDetMod siacTMovgestTsDetMod) {
		getSiacTMovgestTsDetMods().remove(siacTMovgestTsDetMod);
		siacTMovgestTsDetMod.setSiacRModificaStato(null);

		return siacTMovgestTsDetMod;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return modStatoRId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.modStatoRId = uid;
		
	}

}