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
 * The persistent class for the siac_r_indirizzo_soggetto_tipo database table.
 * 
 */
@Entity
@Table(name="siac_r_indirizzo_soggetto_tipo")
public class SiacRIndirizzoSoggettoTipoFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_INDIRIZZO_SOGGETTO_TIPO_IND_SOG_TIPO_ID_GENERATOR", allocationSize=1, sequenceName="siac_r_indirizzo_soggetto_tipo_ind_sog_tipo_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_INDIRIZZO_SOGGETTO_TIPO_IND_SOG_TIPO_ID_GENERATOR")
	@Column(name="ind_sog_tipo_id")
	private Integer indSogTipoId;

	//bi-directional many-to-one association to SiacDIndirizzoTipoFin
	@ManyToOne
	@JoinColumn(name="indirizzo_tipo_id")
	private SiacDIndirizzoTipoFin siacDIndirizzoTipo;

	//bi-directional many-to-one association to SiacTIndirizzoSoggettoFin
	@ManyToOne
	@JoinColumn(name="indirizzo_id")
	private SiacTIndirizzoSoggettoFin siacTIndirizzoSoggetto;

	public SiacRIndirizzoSoggettoTipoFin() {
	}

	public Integer getIndSogTipoId() {
		return this.indSogTipoId;
	}

	public void setIndSogTipoId(Integer indSogTipoId) {
		this.indSogTipoId = indSogTipoId;
	}

	public SiacDIndirizzoTipoFin getSiacDIndirizzoTipo() {
		return this.siacDIndirizzoTipo;
	}

	public void setSiacDIndirizzoTipo(SiacDIndirizzoTipoFin siacDIndirizzoTipo) {
		this.siacDIndirizzoTipo = siacDIndirizzoTipo;
	}

	public SiacTIndirizzoSoggettoFin getSiacTIndirizzoSoggetto() {
		return this.siacTIndirizzoSoggetto;
	}

	public void setSiacTIndirizzoSoggetto(SiacTIndirizzoSoggettoFin siacTIndirizzoSoggetto) {
		this.siacTIndirizzoSoggetto = siacTIndirizzoSoggetto;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.indSogTipoId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.indSogTipoId = uid;
	}
}