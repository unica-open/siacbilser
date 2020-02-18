/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
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
import javax.persistence.Transient;


// TODO: Auto-generated Javadoc
/**
 * The persistent class for the siac_d_onere database table.
 * 
 */
@Entity
@Table(name="siac_d_onere")
@NamedQuery(name="SiacDOnere.findAll", query="SELECT s FROM SiacDOnere s")
public class SiacDOnere extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The onere id. */
	@Id
	@SequenceGenerator(name="SIAC_D_ONERE_ONEREID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_ONERE_ONERE_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_ONERE_ONEREID_GENERATOR")
	@Column(name="onere_id")
	private Integer onereId;

	/** The onere code. */
	@Column(name="onere_code")
	private String onereCode;

	/** The onere desc. */
	@Column(name="onere_desc")
	private String onereDesc;	

	//bi-directional many-to-one association to SiacDOnereTipo
	/** The siac d onere tipo. */
	@ManyToOne
	@JoinColumn(name="onere_tipo_id")
	private SiacDOnereTipo siacDOnereTipo;

	//bi-directional many-to-one association to SiacRDocOnere
	/** The siac r doc oneres. */
	@OneToMany(mappedBy="siacDOnere")
	private List<SiacRDocOnere> siacRDocOneres;

	//bi-directional many-to-one association to SiacROnereAttivita
	/** The siac r onere attivitas. */
	@OneToMany(mappedBy="siacDOnere",  cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacROnereAttivita> siacROnereAttivitas;

	//bi-directional many-to-one association to SiacROnereAttr
	/** The siac r onere attrs. */
	@OneToMany(mappedBy="siacDOnere",  cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacROnereAttr> siacROnereAttrs;

	//bi-directional many-to-one association to SiacROnereCausale
	/** The siac r onere causales. */
	@OneToMany(mappedBy="siacDOnere",  cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacROnereCausale> siacROnereCausales;
	
	//bi-directional many-to-one association to SiacROnereCausale
	/** The siac r onere causales. */
	@OneToMany(mappedBy="siacDOnere",  cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacROnereSommaNonSoggettaTipo> siacROnereSommaNonSoggettaTipos;

	//bi-directional many-to-one association to SiacRSoggettoOnere
	/** The siac r soggetto oneres. */
	@OneToMany(mappedBy="siacDOnere")
	private List<SiacRSoggettoOnere> siacRSoggettoOneres;

	//bi-directional many-to-one association to SiacRSoggettoOnereMod
	/** The siac r soggetto onere mods. */
	@OneToMany(mappedBy="siacDOnere")
	private List<SiacRSoggettoOnereMod> siacRSoggettoOnereMods;
	
	//bi-directional many-to-one association to SiacROnereSplitreverseIvaTipo
	@OneToMany(mappedBy="siacDOnere", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacROnereSplitreverseIvaTipo> siacROnereSplitreverseIvaTipos;
	
	/** The date to extract. */
	@Transient
	private Date dateToExtract;
	

	/**
	 * Instantiates a new siac d onere.
	 */
	public SiacDOnere() {
	}

	/**
	 * Gets the onere id.
	 *
	 * @return the onere id
	 */
	public Integer getOnereId() {
		return this.onereId;
	}

	/**
	 * Sets the onere id.
	 *
	 * @param onereId the new onere id
	 */
	public void setOnereId(Integer onereId) {
		this.onereId = onereId;
	}

	/**
	 * Gets the onere code.
	 *
	 * @return the onere code
	 */
	public String getOnereCode() {
		return this.onereCode;
	}

	/**
	 * Sets the onere code.
	 *
	 * @param onereCode the new onere code
	 */
	public void setOnereCode(String onereCode) {
		this.onereCode = onereCode;
	}

	/**
	 * Gets the onere desc.
	 *
	 * @return the onere desc
	 */
	public String getOnereDesc() {
		return this.onereDesc;
	}

	/**
	 * Sets the onere desc.
	 *
	 * @param onereDesc the new onere desc
	 */
	public void setOnereDesc(String onereDesc) {
		this.onereDesc = onereDesc;
	}


	/**
	 * Gets the siac d onere tipo.
	 *
	 * @return the siac d onere tipo
	 */
	public SiacDOnereTipo getSiacDOnereTipo() {
		return this.siacDOnereTipo;
	}

	/**
	 * Sets the siac d onere tipo.
	 *
	 * @param siacDOnereTipo the new siac d onere tipo
	 */
	public void setSiacDOnereTipo(SiacDOnereTipo siacDOnereTipo) {
		this.siacDOnereTipo = siacDOnereTipo;
	}

	/**
	 * Gets the siac r doc oneres.
	 *
	 * @return the siac r doc oneres
	 */
	public List<SiacRDocOnere> getSiacRDocOneres() {
		return this.siacRDocOneres;
	}

	/**
	 * Sets the siac r doc oneres.
	 *
	 * @param siacRDocOneres the new siac r doc oneres
	 */
	public void setSiacRDocOneres(List<SiacRDocOnere> siacRDocOneres) {
		this.siacRDocOneres = siacRDocOneres;
	}

	/**
	 * Adds the siac r doc onere.
	 *
	 * @param siacRDocOnere the siac r doc onere
	 * @return the siac r doc onere
	 */
	public SiacRDocOnere addSiacRDocOnere(SiacRDocOnere siacRDocOnere) {
		getSiacRDocOneres().add(siacRDocOnere);
		siacRDocOnere.setSiacDOnere(this);

		return siacRDocOnere;
	}

	/**
	 * Removes the siac r doc onere.
	 *
	 * @param siacRDocOnere the siac r doc onere
	 * @return the siac r doc onere
	 */
	public SiacRDocOnere removeSiacRDocOnere(SiacRDocOnere siacRDocOnere) {
		getSiacRDocOneres().remove(siacRDocOnere);
		siacRDocOnere.setSiacDOnere(null);

		return siacRDocOnere;
	}

	/**
	 * Gets the siac r onere attivitas.
	 *
	 * @return the siac r onere attivitas
	 */
	public List<SiacROnereAttivita> getSiacROnereAttivitas() {
		return this.siacROnereAttivitas;
	}

	/**
	 * Sets the siac r onere attivitas.
	 *
	 * @param siacROnereAttivitas the new siac r onere attivitas
	 */
	public void setSiacROnereAttivitas(List<SiacROnereAttivita> siacROnereAttivitas) {
		this.siacROnereAttivitas = siacROnereAttivitas;
	}

	/**
	 * Adds the siac r onere attivita.
	 *
	 * @param siacROnereAttivita the siac r onere attivita
	 * @return the siac r onere attivita
	 */
	public SiacROnereAttivita addSiacROnereAttivita(SiacROnereAttivita siacROnereAttivita) {
		getSiacROnereAttivitas().add(siacROnereAttivita);
		siacROnereAttivita.setSiacDOnere(this);

		return siacROnereAttivita;
	}

	/**
	 * Removes the siac r onere attivita.
	 *
	 * @param siacROnereAttivita the siac r onere attivita
	 * @return the siac r onere attivita
	 */
	public SiacROnereAttivita removeSiacROnereAttivita(SiacROnereAttivita siacROnereAttivita) {
		getSiacROnereAttivitas().remove(siacROnereAttivita);
		siacROnereAttivita.setSiacDOnere(null);

		return siacROnereAttivita;
	}

	/**
	 * Gets the siac r onere attrs.
	 *
	 * @return the siac r onere attrs
	 */
	public List<SiacROnereAttr> getSiacROnereAttrs() {
		return this.siacROnereAttrs;
	}

	/**
	 * Sets the siac r onere attrs.
	 *
	 * @param siacROnereAttrs the new siac r onere attrs
	 */
	public void setSiacROnereAttrs(List<SiacROnereAttr> siacROnereAttrs) {
		this.siacROnereAttrs = siacROnereAttrs;
	}

	/**
	 * Adds the siac r onere attr.
	 *
	 * @param siacROnereAttr the siac r onere attr
	 * @return the siac r onere attr
	 */
	public SiacROnereAttr addSiacROnereAttr(SiacROnereAttr siacROnereAttr) {
		getSiacROnereAttrs().add(siacROnereAttr);
		siacROnereAttr.setSiacDOnere(this);

		return siacROnereAttr;
	}

	/**
	 * Removes the siac r onere attr.
	 *
	 * @param siacROnereAttr the siac r onere attr
	 * @return the siac r onere attr
	 */
	public SiacROnereAttr removeSiacROnereAttr(SiacROnereAttr siacROnereAttr) {
		getSiacROnereAttrs().remove(siacROnereAttr);
		siacROnereAttr.setSiacDOnere(null);

		return siacROnereAttr;
	}

	/**
	 * Gets the siac r onere causales.
	 *
	 * @return the siac r onere causales
	 */
	public List<SiacROnereCausale> getSiacROnereCausales() {
		return this.siacROnereCausales;
	}

	/**
	 * Sets the siac r onere causales.
	 *
	 * @param siacROnereCausales the new siac r onere causales
	 */
	public void setSiacROnereCausales(List<SiacROnereCausale> siacROnereCausales) {
		this.siacROnereCausales = siacROnereCausales;
	}

	/**
	 * Adds the siac r onere causale.
	 *
	 * @param siacROnereCausale the siac r onere causale
	 * @return the siac r onere causale
	 */
	public SiacROnereCausale addSiacROnereCausale(SiacROnereCausale siacROnereCausale) {
		getSiacROnereCausales().add(siacROnereCausale);
		siacROnereCausale.setSiacDOnere(this);

		return siacROnereCausale;
	}

	/**
	 * Removes the siac r onere causale.
	 *
	 * @param siacROnereCausale the siac r onere causale
	 * @return the siac r onere causale
	 */
	public SiacROnereCausale removeSiacROnereCausale(SiacROnereCausale siacROnereCausale) {
		getSiacROnereCausales().remove(siacROnereCausale);
		siacROnereCausale.setSiacDOnere(null);

		return siacROnereCausale;
	}
	
	/**
	 * @return the siacROnereSommaNonSoggettaTipos
	 */
	public List<SiacROnereSommaNonSoggettaTipo> getSiacROnereSommaNonSoggettaTipos() {
		return siacROnereSommaNonSoggettaTipos;
	}

	/**
	 * @param siacROnereSommaNonSoggettaTipos the siacROnereSommaNonSoggettaTipos to set
	 */
	public void setSiacROnereSommaNonSoggettaTipos(
			List<SiacROnereSommaNonSoggettaTipo> siacROnereSommaNonSoggettaTipos) {
		this.siacROnereSommaNonSoggettaTipos = siacROnereSommaNonSoggettaTipos;
	}

	/**
	 * Gets the siac r soggetto oneres.
	 *
	 * @return the siac r soggetto oneres
	 */
	public List<SiacRSoggettoOnere> getSiacRSoggettoOneres() {
		return this.siacRSoggettoOneres;
	}

	/**
	 * Sets the siac r soggetto oneres.
	 *
	 * @param siacRSoggettoOneres the new siac r soggetto oneres
	 */
	public void setSiacRSoggettoOneres(List<SiacRSoggettoOnere> siacRSoggettoOneres) {
		this.siacRSoggettoOneres = siacRSoggettoOneres;
	}

	/**
	 * Adds the siac r soggetto onere.
	 *
	 * @param siacRSoggettoOnere the siac r soggetto onere
	 * @return the siac r soggetto onere
	 */
	public SiacRSoggettoOnere addSiacRSoggettoOnere(SiacRSoggettoOnere siacRSoggettoOnere) {
		getSiacRSoggettoOneres().add(siacRSoggettoOnere);
		siacRSoggettoOnere.setSiacDOnere(this);

		return siacRSoggettoOnere;
	}

	/**
	 * Removes the siac r soggetto onere.
	 *
	 * @param siacRSoggettoOnere the siac r soggetto onere
	 * @return the siac r soggetto onere
	 */
	public SiacRSoggettoOnere removeSiacRSoggettoOnere(SiacRSoggettoOnere siacRSoggettoOnere) {
		getSiacRSoggettoOneres().remove(siacRSoggettoOnere);
		siacRSoggettoOnere.setSiacDOnere(null);

		return siacRSoggettoOnere;
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
		siacRSoggettoOnereMod.setSiacDOnere(this);

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
		siacRSoggettoOnereMod.setSiacDOnere(null);

		return siacRSoggettoOnereMod;
	}
	
	public List<SiacROnereSplitreverseIvaTipo> getSiacROnereSplitreverseIvaTipos() {
		return this.siacROnereSplitreverseIvaTipos;
	}

	public void setSiacROnereSplitreverseIvaTipos(List<SiacROnereSplitreverseIvaTipo> siacROnereSplitreverseIvaTipos) {
		this.siacROnereSplitreverseIvaTipos = siacROnereSplitreverseIvaTipos;
	}

	public SiacROnereSplitreverseIvaTipo addSiacROnereSplitreverseIvaTipo(SiacROnereSplitreverseIvaTipo siacROnereSplitreverseIvaTipo) {
		getSiacROnereSplitreverseIvaTipos().add(siacROnereSplitreverseIvaTipo);
		siacROnereSplitreverseIvaTipo.setSiacDOnere(this);

		return siacROnereSplitreverseIvaTipo;
	}

	public SiacROnereSplitreverseIvaTipo removeSiacROnereSplitreverseIvaTipo(SiacROnereSplitreverseIvaTipo siacROnereSplitreverseIvaTipo) {
		getSiacROnereSplitreverseIvaTipos().remove(siacROnereSplitreverseIvaTipo);
		siacROnereSplitreverseIvaTipo.setSiacDOnere(null);

		return siacROnereSplitreverseIvaTipo;
	}
	
	/**
	 * @return the dateToExtract
	 */
	public Date getDateToExtract() {
		return dateToExtract;
	}

	/**
	 * @param dateToExtract the dateToExtract to set
	 */
	public void setDateToExtract(Date dateToExtract) {
		this.dateToExtract = dateToExtract;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return onereId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.onereId = uid;
	}



}