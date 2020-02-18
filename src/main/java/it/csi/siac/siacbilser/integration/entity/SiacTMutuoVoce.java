/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import java.math.BigDecimal;
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
 * The persistent class for the siac_t_mutuo_voce database table.
 * 
 */
@Entity
@Table(name="siac_t_mutuo_voce")
@NamedQuery(name="SiacTMutuoVoce.findAll", query="SELECT s FROM SiacTMutuoVoce s")
public class SiacTMutuoVoce extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The mut voce id. */
	@Id
	@SequenceGenerator(name="SIAC_T_MUTUO_VOCE_MUTVOCEID_GENERATOR", allocationSize=1, sequenceName="SIAC_T_MUTUO_VOCE_MUT_VOCE_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_MUTUO_VOCE_MUTVOCEID_GENERATOR")
	@Column(name="mut_voce_id")
	private Integer mutVoceId;

	/** The mut voce code. */
	@Column(name="mut_voce_code")
	private String mutVoceCode;

	/** The mut voce desc. */
	@Column(name="mut_voce_desc")
	private String mutVoceDesc;

	/** The mut voce importo attuale. */
	@Column(name="mut_voce_importo_attuale")
	private BigDecimal mutVoceImportoAttuale;

	/** The mut voce importo iniziale. */
	@Column(name="mut_voce_importo_iniziale")
	private BigDecimal mutVoceImportoIniziale;

	//bi-directional many-to-one association to SiacRMutuoVoceLiquidazione
	/** The siac r mutuo voce liquidaziones. */
	@OneToMany(mappedBy="siacTMutuoVoce")
	private List<SiacRMutuoVoceLiquidazione> siacRMutuoVoceLiquidaziones;

	//bi-directional many-to-one association to SiacRMutuoVoceMovgest
	/** The siac r mutuo voce movgests. */
	@OneToMany(mappedBy="siacTMutuoVoce")
	private List<SiacRMutuoVoceMovgest> siacRMutuoVoceMovgests;

	//bi-directional many-to-one association to SiacDMutuoVoceTipo
	/** The siac d mutuo voce tipo. */
	@ManyToOne
	@JoinColumn(name="mut_voce_tipo_id")
	private SiacDMutuoVoceTipo siacDMutuoVoceTipo;

	//bi-directional many-to-one association to SiacTMutuo
	/** The siac t mutuo. */
	@ManyToOne
	@JoinColumn(name="mut_id")
	private SiacTMutuo siacTMutuo;

	//bi-directional many-to-one association to SiacTMutuoVoceVar
	/** The siac t mutuo voce vars. */
	@OneToMany(mappedBy="siacTMutuoVoce")
	private List<SiacTMutuoVoceVar> siacTMutuoVoceVars;

	/**
	 * Instantiates a new siac t mutuo voce.
	 */
	public SiacTMutuoVoce() {
	}

	/**
	 * Gets the mut voce id.
	 *
	 * @return the mut voce id
	 */
	public Integer getMutVoceId() {
		return this.mutVoceId;
	}

	/**
	 * Sets the mut voce id.
	 *
	 * @param mutVoceId the new mut voce id
	 */
	public void setMutVoceId(Integer mutVoceId) {
		this.mutVoceId = mutVoceId;
	}

	/**
	 * Gets the mut voce code.
	 *
	 * @return the mut voce code
	 */
	public String getMutVoceCode() {
		return this.mutVoceCode;
	}

	/**
	 * Sets the mut voce code.
	 *
	 * @param mutVoceCode the new mut voce code
	 */
	public void setMutVoceCode(String mutVoceCode) {
		this.mutVoceCode = mutVoceCode;
	}

	/**
	 * Gets the mut voce desc.
	 *
	 * @return the mut voce desc
	 */
	public String getMutVoceDesc() {
		return this.mutVoceDesc;
	}

	/**
	 * Sets the mut voce desc.
	 *
	 * @param mutVoceDesc the new mut voce desc
	 */
	public void setMutVoceDesc(String mutVoceDesc) {
		this.mutVoceDesc = mutVoceDesc;
	}

	/**
	 * Gets the mut voce importo attuale.
	 *
	 * @return the mut voce importo attuale
	 */
	public BigDecimal getMutVoceImportoAttuale() {
		return this.mutVoceImportoAttuale;
	}

	/**
	 * Sets the mut voce importo attuale.
	 *
	 * @param mutVoceImportoAttuale the new mut voce importo attuale
	 */
	public void setMutVoceImportoAttuale(BigDecimal mutVoceImportoAttuale) {
		this.mutVoceImportoAttuale = mutVoceImportoAttuale;
	}

	/**
	 * Gets the mut voce importo iniziale.
	 *
	 * @return the mut voce importo iniziale
	 */
	public BigDecimal getMutVoceImportoIniziale() {
		return this.mutVoceImportoIniziale;
	}

	/**
	 * Sets the mut voce importo iniziale.
	 *
	 * @param mutVoceImportoIniziale the new mut voce importo iniziale
	 */
	public void setMutVoceImportoIniziale(BigDecimal mutVoceImportoIniziale) {
		this.mutVoceImportoIniziale = mutVoceImportoIniziale;
	}

	/**
	 * Gets the siac r mutuo voce liquidaziones.
	 *
	 * @return the siac r mutuo voce liquidaziones
	 */
	public List<SiacRMutuoVoceLiquidazione> getSiacRMutuoVoceLiquidaziones() {
		return this.siacRMutuoVoceLiquidaziones;
	}

	/**
	 * Sets the siac r mutuo voce liquidaziones.
	 *
	 * @param siacRMutuoVoceLiquidaziones the new siac r mutuo voce liquidaziones
	 */
	public void setSiacRMutuoVoceLiquidaziones(List<SiacRMutuoVoceLiquidazione> siacRMutuoVoceLiquidaziones) {
		this.siacRMutuoVoceLiquidaziones = siacRMutuoVoceLiquidaziones;
	}

	/**
	 * Adds the siac r mutuo voce liquidazione.
	 *
	 * @param siacRMutuoVoceLiquidazione the siac r mutuo voce liquidazione
	 * @return the siac r mutuo voce liquidazione
	 */
	public SiacRMutuoVoceLiquidazione addSiacRMutuoVoceLiquidazione(SiacRMutuoVoceLiquidazione siacRMutuoVoceLiquidazione) {
		getSiacRMutuoVoceLiquidaziones().add(siacRMutuoVoceLiquidazione);
		siacRMutuoVoceLiquidazione.setSiacTMutuoVoce(this);

		return siacRMutuoVoceLiquidazione;
	}

	/**
	 * Removes the siac r mutuo voce liquidazione.
	 *
	 * @param siacRMutuoVoceLiquidazione the siac r mutuo voce liquidazione
	 * @return the siac r mutuo voce liquidazione
	 */
	public SiacRMutuoVoceLiquidazione removeSiacRMutuoVoceLiquidazione(SiacRMutuoVoceLiquidazione siacRMutuoVoceLiquidazione) {
		getSiacRMutuoVoceLiquidaziones().remove(siacRMutuoVoceLiquidazione);
		siacRMutuoVoceLiquidazione.setSiacTMutuoVoce(null);

		return siacRMutuoVoceLiquidazione;
	}

	/**
	 * Gets the siac r mutuo voce movgests.
	 *
	 * @return the siac r mutuo voce movgests
	 */
	public List<SiacRMutuoVoceMovgest> getSiacRMutuoVoceMovgests() {
		return this.siacRMutuoVoceMovgests;
	}

	/**
	 * Sets the siac r mutuo voce movgests.
	 *
	 * @param siacRMutuoVoceMovgests the new siac r mutuo voce movgests
	 */
	public void setSiacRMutuoVoceMovgests(List<SiacRMutuoVoceMovgest> siacRMutuoVoceMovgests) {
		this.siacRMutuoVoceMovgests = siacRMutuoVoceMovgests;
	}

	/**
	 * Adds the siac r mutuo voce movgest.
	 *
	 * @param siacRMutuoVoceMovgest the siac r mutuo voce movgest
	 * @return the siac r mutuo voce movgest
	 */
	public SiacRMutuoVoceMovgest addSiacRMutuoVoceMovgest(SiacRMutuoVoceMovgest siacRMutuoVoceMovgest) {
		getSiacRMutuoVoceMovgests().add(siacRMutuoVoceMovgest);
		siacRMutuoVoceMovgest.setSiacTMutuoVoce(this);

		return siacRMutuoVoceMovgest;
	}

	/**
	 * Removes the siac r mutuo voce movgest.
	 *
	 * @param siacRMutuoVoceMovgest the siac r mutuo voce movgest
	 * @return the siac r mutuo voce movgest
	 */
	public SiacRMutuoVoceMovgest removeSiacRMutuoVoceMovgest(SiacRMutuoVoceMovgest siacRMutuoVoceMovgest) {
		getSiacRMutuoVoceMovgests().remove(siacRMutuoVoceMovgest);
		siacRMutuoVoceMovgest.setSiacTMutuoVoce(null);

		return siacRMutuoVoceMovgest;
	}

	/**
	 * Gets the siac d mutuo voce tipo.
	 *
	 * @return the siac d mutuo voce tipo
	 */
	public SiacDMutuoVoceTipo getSiacDMutuoVoceTipo() {
		return this.siacDMutuoVoceTipo;
	}

	/**
	 * Sets the siac d mutuo voce tipo.
	 *
	 * @param siacDMutuoVoceTipo the new siac d mutuo voce tipo
	 */
	public void setSiacDMutuoVoceTipo(SiacDMutuoVoceTipo siacDMutuoVoceTipo) {
		this.siacDMutuoVoceTipo = siacDMutuoVoceTipo;
	}



	/**
	 * Gets the siac t mutuo.
	 *
	 * @return the siac t mutuo
	 */
	public SiacTMutuo getSiacTMutuo() {
		return this.siacTMutuo;
	}

	/**
	 * Sets the siac t mutuo.
	 *
	 * @param siacTMutuo the new siac t mutuo
	 */
	public void setSiacTMutuo(SiacTMutuo siacTMutuo) {
		this.siacTMutuo = siacTMutuo;
	}

	/**
	 * Gets the siac t mutuo voce vars.
	 *
	 * @return the siac t mutuo voce vars
	 */
	public List<SiacTMutuoVoceVar> getSiacTMutuoVoceVars() {
		return this.siacTMutuoVoceVars;
	}

	/**
	 * Sets the siac t mutuo voce vars.
	 *
	 * @param siacTMutuoVoceVars the new siac t mutuo voce vars
	 */
	public void setSiacTMutuoVoceVars(List<SiacTMutuoVoceVar> siacTMutuoVoceVars) {
		this.siacTMutuoVoceVars = siacTMutuoVoceVars;
	}

	/**
	 * Adds the siac t mutuo voce var.
	 *
	 * @param siacTMutuoVoceVar the siac t mutuo voce var
	 * @return the siac t mutuo voce var
	 */
	public SiacTMutuoVoceVar addSiacTMutuoVoceVar(SiacTMutuoVoceVar siacTMutuoVoceVar) {
		getSiacTMutuoVoceVars().add(siacTMutuoVoceVar);
		siacTMutuoVoceVar.setSiacTMutuoVoce(this);

		return siacTMutuoVoceVar;
	}

	/**
	 * Removes the siac t mutuo voce var.
	 *
	 * @param siacTMutuoVoceVar the siac t mutuo voce var
	 * @return the siac t mutuo voce var
	 */
	public SiacTMutuoVoceVar removeSiacTMutuoVoceVar(SiacTMutuoVoceVar siacTMutuoVoceVar) {
		getSiacTMutuoVoceVars().remove(siacTMutuoVoceVar);
		siacTMutuoVoceVar.setSiacTMutuoVoce(null);

		return siacTMutuoVoceVar;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return mutVoceId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.mutVoceId = uid;
	}

}