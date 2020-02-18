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
 * The persistent class for the siac_r_indirizzo_soggetto_tipo_mod database table.
 * 
 */
@Entity
@Table(name="siac_r_indirizzo_soggetto_tipo_mod")
public class SiacRIndirizzoSoggettoTipoModFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_INDIRIZZO_SOGGETTO_TIPO_MOD_IND_SOG_TIPO_ID_GENERATOR", allocationSize=1, sequenceName="siac_r_indirizzo_soggetto_tipo_mod_ind_sog_tipo_mod_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_INDIRIZZO_SOGGETTO_TIPO_MOD_IND_SOG_TIPO_ID_GENERATOR")
	@Column(name="ind_sog_tipo_mod_id")
	private Integer indSogTipoModId;

	//bi-directional many-to-one association to SiacDIndirizzoTipoFin
	@ManyToOne
	@JoinColumn(name="indirizzo_tipo_id")
	private SiacDIndirizzoTipoFin siacDIndirizzoTipo;

	//bi-directional many-to-one association to SiacTIndirizzoSoggettoModFin
	@ManyToOne
	@JoinColumn(name="indirizzo_mod_id")
	private SiacTIndirizzoSoggettoModFin siacTIndirizzoSoggettoMod;

	public SiacRIndirizzoSoggettoTipoModFin() {
	}

	public Integer getIndSogTipoModId() {
		return this.indSogTipoModId;
	}

	public void setIndSogTipoModId(Integer indSogTipoModId) {
		this.indSogTipoModId = indSogTipoModId;
	}

	public SiacDIndirizzoTipoFin getSiacDIndirizzoTipo() {
		return this.siacDIndirizzoTipo;
	}

	public void setSiacDIndirizzoTipo(SiacDIndirizzoTipoFin siacDIndirizzoTipo) {
		this.siacDIndirizzoTipo = siacDIndirizzoTipo;
	}

	public SiacTIndirizzoSoggettoModFin getSiacTIndirizzoSoggettoMod() {
		return this.siacTIndirizzoSoggettoMod;
	}

	public void setSiacTIndirizzoSoggettoMod(SiacTIndirizzoSoggettoModFin siacTIndirizzoSoggettoMod) {
		this.siacTIndirizzoSoggettoMod = siacTIndirizzoSoggettoMod;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.indSogTipoModId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.indSogTipoModId = uid;
	}
}