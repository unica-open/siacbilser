/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity;

import java.sql.Timestamp;
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

import it.csi.siac.siacfinser.integration.entity.base.SiacTEnteBase;


/**
 * The persistent class for the siac_t_periodo database table.
 * 
 */
@Entity
@Table(name="siac_t_periodo")
public class SiacTPeriodoFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_T_PERIODO_PERIODO_ID_GENERATOR", allocationSize=1, sequenceName="siac_t_periodo_periodo_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_PERIODO_PERIODO_ID_GENERATOR")
	@Column(name="periodo_id")
	private Integer periodoId;

	private String anno;

	@Column(name="data_fine")
	private Timestamp dataFine;

	@Column(name="data_inizio")
	private Timestamp dataInizio;

	@Column(name="periodo_code")
	private String periodoCode;

	@Column(name="periodo_desc")
	private String periodoDesc;

	//bi-directional many-to-one association to SiacTBilFin
	@OneToMany(mappedBy="siacTPeriodo")
	private List<SiacTBilFin> siacTBils;

	//bi-directional many-to-one association to SiacTBilElemDetFin
	@OneToMany(mappedBy="siacTPeriodo")
	private List<SiacTBilElemDetFin> siacTBilElemDets;

	//bi-directional many-to-one association to SiacDPeriodoTipoFin
	@ManyToOne
	@JoinColumn(name="periodo_tipo_id")
	private SiacDPeriodoTipoFin siacDPeriodoTipo;

	public SiacTPeriodoFin() {
	}

	public Integer getPeriodoId() {
		return this.periodoId;
	}

	public void setPeriodoId(Integer periodoId) {
		this.periodoId = periodoId;
	}

	public String getAnno() {
		return this.anno;
	}

	public void setAnno(String anno) {
		this.anno = anno;
	}

	public Timestamp getDataFine() {
		return this.dataFine;
	}

	public void setDataFine(Timestamp dataFine) {
		this.dataFine = dataFine;
	}

	public Timestamp getDataInizio() {
		return this.dataInizio;
	}

	public void setDataInizio(Timestamp dataInizio) {
		this.dataInizio = dataInizio;
	}

	public String getPeriodoCode() {
		return this.periodoCode;
	}

	public void setPeriodoCode(String periodoCode) {
		this.periodoCode = periodoCode;
	}

	public String getPeriodoDesc() {
		return this.periodoDesc;
	}

	public void setPeriodoDesc(String periodoDesc) {
		this.periodoDesc = periodoDesc;
	}

	public List<SiacTBilFin> getSiacTBils() {
		return this.siacTBils;
	}

	public void setSiacTBils(List<SiacTBilFin> siacTBils) {
		this.siacTBils = siacTBils;
	}

	public SiacTBilFin addSiacTBil(SiacTBilFin siacTBil) {
		getSiacTBils().add(siacTBil);
		siacTBil.setSiacTPeriodo(this);

		return siacTBil;
	}

	public SiacTBilFin removeSiacTBil(SiacTBilFin siacTBil) {
		getSiacTBils().remove(siacTBil);
		siacTBil.setSiacTPeriodo(null);

		return siacTBil;
	}

	public List<SiacTBilElemDetFin> getSiacTBilElemDets() {
		return this.siacTBilElemDets;
	}

	public void setSiacTBilElemDets(List<SiacTBilElemDetFin> siacTBilElemDets) {
		this.siacTBilElemDets = siacTBilElemDets;
	}

	public SiacTBilElemDetFin addSiacTBilElemDet(SiacTBilElemDetFin siacTBilElemDet) {
		getSiacTBilElemDets().add(siacTBilElemDet);
		siacTBilElemDet.setSiacTPeriodo(this);

		return siacTBilElemDet;
	}

	public SiacTBilElemDetFin removeSiacTBilElemDet(SiacTBilElemDetFin siacTBilElemDet) {
		getSiacTBilElemDets().remove(siacTBilElemDet);
		siacTBilElemDet.setSiacTPeriodo(null);

		return siacTBilElemDet;
	}

	public SiacDPeriodoTipoFin getSiacDPeriodoTipo() {
		return this.siacDPeriodoTipo;
	}

	public void setSiacDPeriodoTipo(SiacDPeriodoTipoFin siacDPeriodoTipo) {
		this.siacDPeriodoTipo = siacDPeriodoTipo;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.periodoId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.periodoId = uid;
	}
}