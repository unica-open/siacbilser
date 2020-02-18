/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


// TODO: Auto-generated Javadoc
/**
 * The persistent class for the siac_t_periodo database table.
 * 
 */
@Entity
@Table(name="siac_t_periodo")
public class SiacTPeriodo extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The periodo id. */
	@Id
	@SequenceGenerator(name="SIAC_T_PERIODO_PERIODOID_GENERATOR", allocationSize=1, sequenceName="SIAC_T_PERIODO_PERIODO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_PERIODO_PERIODOID_GENERATOR")
	@Column(name="periodo_id")
	private Integer periodoId;

	/** The anno. */
	private String anno;
	

	/** The data fine. */
	@Column(name="data_fine")
	private Date dataFine;

	/** The data inizio. */
	@Column(name="data_inizio")
	private Date dataInizio;

	/** The periodo code. */
	@Column(name="periodo_code")
	private String periodoCode;

	/** The periodo desc. */
	@Column(name="periodo_desc")
	private String periodoDesc;

	//bi-directional many-to-one association to SiacTBil
	/** The siac t bils. */
	@OneToMany(mappedBy="siacTPeriodo")
	private List<SiacTBil> siacTBils;

	//bi-directional many-to-one association to SiacTBilElemDet
	/** The siac t bil elem dets. */
	@OneToMany(mappedBy="siacTPeriodo")
	private List<SiacTBilElemDet> siacTBilElemDets;

	//bi-directional many-to-one association to SiacTIvaRegistroTotale
	@OneToMany(mappedBy="siacTPeriodo")
	private List<SiacTIvaRegistroTotale> siacTIvaRegistroTotales;

	//bi-directional many-to-one association to SiacTIvaStampa
	@OneToMany(mappedBy="siacTPeriodo")
	private List<SiacTIvaStampa> siacTIvaStampas;

	//bi-directional many-to-one association to SiacDPeriodoTipo
	/** The siac d periodo tipo. */
	@ManyToOne
	@JoinColumn(name="periodo_tipo_id")
	private SiacDPeriodoTipo siacDPeriodoTipo;

	//bi-directional many-to-one association to SiacTReportImporti
	@OneToMany(mappedBy="siacTPeriodo")
	private List<SiacTReportImporti> siacTReportImportis;

	//bi-directional many-to-one association to SiacTVincolo
	@OneToMany(mappedBy="siacTPeriodo")
	private List<SiacTVincolo> siacTVincolos;

	//bi-directional many-to-one association to SiacDBilElemDetCompTipo
	@OneToMany(mappedBy="siacTPeriodo")
	private List<SiacDBilElemDetCompTipo> siacDBilElemDetCompTipos;

	/**
	 * Instantiates a new siac t periodo.
	 */
	public SiacTPeriodo() {
	}

	/**
	 * Gets the periodo id.
	 *
	 * @return the periodo id
	 */
	public Integer getPeriodoId() {
		return this.periodoId;
	}

	/**
	 * Sets the periodo id.
	 *
	 * @param periodoId the new periodo id
	 */
	public void setPeriodoId(Integer periodoId) {
		this.periodoId = periodoId;
	}

	/**
	 * Gets the anno.
	 *
	 * @return the anno
	 */
	public String getAnno() {
		return this.anno;
	}

	/**
	 * Sets the anno.
	 *
	 * @param anno the new anno
	 */
	public void setAnno(String anno) {
		this.anno = anno;
	}



	/**
	 * Gets the data fine.
	 *
	 * @return the data fine
	 */
	public Date getDataFine() {
		return this.dataFine;
	}

	/**
	 * Sets the data fine.
	 *
	 * @param dataFine the new data fine
	 */
	public void setDataFine(Date dataFine) {
		this.dataFine = dataFine;
	}

	/**
	 * Gets the data inizio.
	 *
	 * @return the data inizio
	 */
	public Date getDataInizio() {
		return this.dataInizio;
	}

	/**
	 * Sets the data inizio.
	 *
	 * @param dataInizio the new data inizio
	 */
	public void setDataInizio(Date dataInizio) {
		this.dataInizio = dataInizio;
	}



	/**
	 * Gets the periodo code.
	 *
	 * @return the periodo code
	 */
	public String getPeriodoCode() {
		return this.periodoCode;
	}

	/**
	 * Sets the periodo code.
	 *
	 * @param periodoCode the new periodo code
	 */
	public void setPeriodoCode(String periodoCode) {
		this.periodoCode = periodoCode;
	}

	/**
	 * Gets the periodo desc.
	 *
	 * @return the periodo desc
	 */
	public String getPeriodoDesc() {
		return this.periodoDesc;
	}

	/**
	 * Sets the periodo desc.
	 *
	 * @param periodoDesc the new periodo desc
	 */
	public void setPeriodoDesc(String periodoDesc) {
		this.periodoDesc = periodoDesc;
	}



	/**
	 * Gets the siac t bils.
	 *
	 * @return the siac t bils
	 */
	public List<SiacTBil> getSiacTBils() {
		return this.siacTBils;
	}

	/**
	 * Sets the siac t bils.
	 *
	 * @param siacTBils the new siac t bils
	 */
	public void setSiacTBils(List<SiacTBil> siacTBils) {
		this.siacTBils = siacTBils;
	}

	/**
	 * Adds the siac t bil.
	 *
	 * @param siacTBil the siac t bil
	 * @return the siac t bil
	 */
	public SiacTBil addSiacTBil(SiacTBil siacTBil) {
		getSiacTBils().add(siacTBil);
		siacTBil.setSiacTPeriodo(this);

		return siacTBil;
	}

	/**
	 * Removes the siac t bil.
	 *
	 * @param siacTBil the siac t bil
	 * @return the siac t bil
	 */
	public SiacTBil removeSiacTBil(SiacTBil siacTBil) {
		getSiacTBils().remove(siacTBil);
		siacTBil.setSiacTPeriodo(null);

		return siacTBil;
	}

	/**
	 * Gets the siac t bil elem dets.
	 *
	 * @return the siac t bil elem dets
	 */
	public List<SiacTBilElemDet> getSiacTBilElemDets() {
		return this.siacTBilElemDets;
	}

	/**
	 * Sets the siac t bil elem dets.
	 *
	 * @param siacTBilElemDets the new siac t bil elem dets
	 */
	public void setSiacTBilElemDets(List<SiacTBilElemDet> siacTBilElemDets) {
		this.siacTBilElemDets = siacTBilElemDets;
	}

	/**
	 * Adds the siac t bil elem det.
	 *
	 * @param siacTBilElemDet the siac t bil elem det
	 * @return the siac t bil elem det
	 */
	public SiacTBilElemDet addSiacTBilElemDet(SiacTBilElemDet siacTBilElemDet) {
		getSiacTBilElemDets().add(siacTBilElemDet);
		siacTBilElemDet.setSiacTPeriodo(this);

		return siacTBilElemDet;
	}

	/**
	 * Removes the siac t bil elem det.
	 *
	 * @param siacTBilElemDet the siac t bil elem det
	 * @return the siac t bil elem det
	 */
	public SiacTBilElemDet removeSiacTBilElemDet(SiacTBilElemDet siacTBilElemDet) {
		getSiacTBilElemDets().remove(siacTBilElemDet);
		siacTBilElemDet.setSiacTPeriodo(null);

		return siacTBilElemDet;
	}

	public List<SiacTIvaRegistroTotale> getSiacTIvaRegistroTotales() {
		return this.siacTIvaRegistroTotales;
	}

	public void setSiacTIvaRegistroTotales(List<SiacTIvaRegistroTotale> siacTIvaRegistroTotales) {
		this.siacTIvaRegistroTotales = siacTIvaRegistroTotales;
	}

	public SiacTIvaRegistroTotale addSiacTIvaRegistroTotale(SiacTIvaRegistroTotale siacTIvaRegistroTotale) {
		getSiacTIvaRegistroTotales().add(siacTIvaRegistroTotale);
		siacTIvaRegistroTotale.setSiacTPeriodo(this);

		return siacTIvaRegistroTotale;
	}

	public SiacTIvaRegistroTotale removeSiacTIvaRegistroTotale(SiacTIvaRegistroTotale siacTIvaRegistroTotale) {
		getSiacTIvaRegistroTotales().remove(siacTIvaRegistroTotale);
		siacTIvaRegistroTotale.setSiacTPeriodo(null);

		return siacTIvaRegistroTotale;
	}

	public List<SiacTIvaStampa> getSiacTIvaStampas() {
		return this.siacTIvaStampas;
	}

	public void setSiacTIvaStampas(List<SiacTIvaStampa> siacTIvaStampas) {
		this.siacTIvaStampas = siacTIvaStampas;
	}

	public SiacTIvaStampa addSiacTIvaStampa(SiacTIvaStampa siacTIvaStampa) {
		getSiacTIvaStampas().add(siacTIvaStampa);
		siacTIvaStampa.setSiacTPeriodo(this);

		return siacTIvaStampa;
	}

	public SiacTIvaStampa removeSiacTIvaStampa(SiacTIvaStampa siacTIvaStampa) {
		getSiacTIvaStampas().remove(siacTIvaStampa);
		siacTIvaStampa.setSiacTPeriodo(null);

		return siacTIvaStampa;
	}

	public SiacDPeriodoTipo getSiacDPeriodoTipo() {
		return this.siacDPeriodoTipo;
	}

	public void setSiacDPeriodoTipo(SiacDPeriodoTipo siacDPeriodoTipo) {
		this.siacDPeriodoTipo = siacDPeriodoTipo;
	}

	public List<SiacTReportImporti> getSiacTReportImportis() {
		return this.siacTReportImportis;
	}

	public void setSiacTReportImportis(List<SiacTReportImporti> siacTReportImportis) {
		this.siacTReportImportis = siacTReportImportis;
	}

	public SiacTReportImporti addSiacTReportImporti(SiacTReportImporti siacTReportImporti) {
		getSiacTReportImportis().add(siacTReportImporti);
		siacTReportImporti.setSiacTPeriodo(this);

		return siacTReportImporti;
	}

	public SiacTReportImporti removeSiacTReportImporti(SiacTReportImporti siacTReportImporti) {
		getSiacTReportImportis().remove(siacTReportImporti);
		siacTReportImporti.setSiacTPeriodo(null);

		return siacTReportImporti;
	}

	public List<SiacTVincolo> getSiacTVincolos() {
		return this.siacTVincolos;
	}

	public void setSiacTVincolos(List<SiacTVincolo> siacTVincolos) {
		this.siacTVincolos = siacTVincolos;
	}

	public SiacTVincolo addSiacTVincolo(SiacTVincolo siacTVincolo) {
		getSiacTVincolos().add(siacTVincolo);
		siacTVincolo.setSiacTPeriodo(this);

		return siacTVincolo;
	}

	public SiacTVincolo removeSiacTVincolo(SiacTVincolo siacTVincolo) {
		getSiacTVincolos().remove(siacTVincolo);
		siacTVincolo.setSiacTPeriodo(null);

		return siacTVincolo;
	}

	/**
	 * @return the siacDBilElemDetCompTipos
	 */
	public List<SiacDBilElemDetCompTipo> getSiacDBilElemDetCompTipos() {
		return this.siacDBilElemDetCompTipos;
	}

	/**
	 * @param siacDBilElemDetCompTipos the siacDBilElemDetCompTipos to set
	 */
	public void setSiacDBilElemDetCompTipos(List<SiacDBilElemDetCompTipo> siacDBilElemDetCompTipos) {
		this.siacDBilElemDetCompTipos = siacDBilElemDetCompTipos;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return periodoId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.periodoId = uid;
		
	}

}