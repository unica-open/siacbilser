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
 * The persistent class for the siac_r_soggetto_tipo database table.
 * 
 */
@Entity
@Table(name="siac_r_soggetto_tipo")
public class SiacRSoggettoTipoFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_SOGGETTO_TIPO_SOGGETTO_TIPO_ID_GENERATOR", allocationSize=1, sequenceName="siac_r_soggetto_tipo_soggetto_r_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_SOGGETTO_TIPO_SOGGETTO_TIPO_ID_GENERATOR")
	@Column(name="soggetto_r_id")
	private Integer soggettoRId;

	//bi-directional many-to-one association to SiacDSoggettoTipoFin
	@ManyToOne
	@JoinColumn(name="soggetto_tipo_id")
	private SiacDSoggettoTipoFin siacDSoggettoTipo;

	//bi-directional many-to-one association to SiacTSoggettoFin
	@ManyToOne
	@JoinColumn(name="soggetto_id")
	private SiacTSoggettoFin siacTSoggetto;

	public SiacRSoggettoTipoFin() {
	}

	public Integer getSoggettoRId() {
		return this.soggettoRId;
	}

	public void setSoggettoRId(Integer soggettoRId) {
		this.soggettoRId = soggettoRId;
	}

	public SiacDSoggettoTipoFin getSiacDSoggettoTipo() {
		return this.siacDSoggettoTipo;
	}

	public void setSiacDSoggettoTipo(SiacDSoggettoTipoFin siacDSoggettoTipo) {
		this.siacDSoggettoTipo = siacDSoggettoTipo;
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
		return this.soggettoRId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.soggettoRId = uid;
	}
}