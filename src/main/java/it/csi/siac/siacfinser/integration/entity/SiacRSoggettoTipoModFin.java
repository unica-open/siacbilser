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
 * The persistent class for the siac_r_soggetto_tipo_mod database table.
 * 
 */
@Entity
@Table(name="siac_r_soggetto_tipo_mod")
public class SiacRSoggettoTipoModFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(name="SIAC_R_SOGGETTO_TIPO_MOD_SOGGETTO_TIPO_ID_GENERATOR", allocationSize=1, sequenceName="siac_r_soggetto_tipo_mod_soggetto_r_mod_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_SOGGETTO_TIPO_MOD_SOGGETTO_TIPO_ID_GENERATOR")
	@Column(name="soggetto_r_mod_id")
	private Integer soggettoRModId;

	//bi-directional many-to-one association to SiacTSoggettoModFin
	@ManyToOne
	@JoinColumn(name="sog_mod_id")
	private SiacTSoggettoModFin siacTSoggettoMod;
		
	//bi-directional many-to-one association to SiacTSoggettoFin
	@ManyToOne
	@JoinColumn(name="soggetto_id")
	private SiacTSoggettoFin siacTSoggetto;

	//bi-directional many-to-one association to SiacDSoggettoTipoFin
	@ManyToOne
	@JoinColumn(name="soggetto_tipo_id")
	private SiacDSoggettoTipoFin siacDSoggettoTipo;

	public SiacRSoggettoTipoModFin() {
	}

	public Integer getSoggettoRModId() {
		return this.soggettoRModId;
	}

	public void setSoggettoRModId(Integer soggettoRModId) {
		this.soggettoRModId = soggettoRModId;
	}

	public SiacTSoggettoModFin getSiacTSoggettoMod() {
		return this.siacTSoggettoMod;
	}

	public void setSiacTSoggettoMod(SiacTSoggettoModFin siacTSoggettoMod) {
		this.siacTSoggettoMod = siacTSoggettoMod;
	}

	public SiacTSoggettoFin getSiacTSoggetto() {
		return this.siacTSoggetto;
	}

	public void setSiacTSoggetto(SiacTSoggettoFin siacTSoggetto) {
		this.siacTSoggetto = siacTSoggetto;
	}

	public SiacDSoggettoTipoFin getSiacDSoggettoTipo() {
		return this.siacDSoggettoTipo;
	}

	public void setSiacDSoggettoTipo(SiacDSoggettoTipoFin siacDSoggettoTipo) {
		this.siacDSoggettoTipo = siacDSoggettoTipo;
	}
	
	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.soggettoRModId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.soggettoRModId = uid;
	}
}