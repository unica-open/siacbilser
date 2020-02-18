/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import it.csi.siac.siacfinser.integration.entity.base.SiacTEnteBase;


/**
 * The persistent class for the siac_r_mutuo_soggetto database table.
 * 
 */
@Entity
@Table(name="siac_r_mutuo_soggetto")
public class SiacRMutuoSoggettoFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_MUTUO_SOGGETTO_ID_GENERATOR", allocationSize=1, sequenceName="siac_r_mutuo_soggetto_mut_soggetto_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_MUTUO_SOGGETTO_ID_GENERATOR")
	@Column(name="mut_soggetto_id")
	private Integer mutSoggettoId;

	//bi-directional many-to-one association to SiacTMutuoFin
	@ManyToOne
	@JoinColumn(name="mut_id")
	private SiacTMutuoFin siacTMutuo;

	//bi-directional many-to-one association to SiacTSoggettoFin
	@ManyToOne
	@JoinColumn(name="soggetto_id")
	private SiacTSoggettoFin siacTSoggetto;

	public SiacRMutuoSoggettoFin() {
	}

	public Integer getMutSoggettoId() {
		return this.mutSoggettoId;
	}

	public void setMutSoggettoId(Integer mutSoggettoId) {
		this.mutSoggettoId = mutSoggettoId;
	}

	public SiacTMutuoFin getSiacTMutuo() {
		return this.siacTMutuo;
	}

	public void setSiacTMutuo(SiacTMutuoFin siacTMutuo) {
		this.siacTMutuo = siacTMutuo;
	}

	public SiacTSoggettoFin getSiacTSoggetto() {
		return this.siacTSoggetto;
	}

	public void setSiacTSoggetto(SiacTSoggettoFin siacTSoggetto) {
		this.siacTSoggetto = siacTSoggetto;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.mutSoggettoId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.mutSoggettoId = uid;
	}
}