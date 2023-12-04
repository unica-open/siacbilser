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
 * The persistent class for the siac_r_forma_giuridica_mod database table.
 * 
 */
@Entity
@Table(name="siac_r_forma_giuridica_mod")
public class SiacRFormaGiuridicaModFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_FORMA_GIURIDICA_MOD_FORMA_GIURIDICA_GENERATOR", allocationSize=1, sequenceName="siac_r_forma_giuridica_mod_soggetto_forma_giuridica_mod_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_FORMA_GIURIDICA_MOD_FORMA_GIURIDICA_GENERATOR")
	@Column(name="soggetto_forma_giuridica_mod_id")
	private Integer soggettoFormaGiuridicaModId;

	//bi-directional many-to-one association to SiacTFormaGiuridicaFin
	@ManyToOne
	@JoinColumn(name="forma_giuridica_id")
	private SiacTFormaGiuridicaFin siacTFormaGiuridica;
		
	//bi-directional many-to-one association to SiacTSoggettoModFin
	@ManyToOne
	@JoinColumn(name="sog_mod_id")
	private SiacTSoggettoModFin siacTSoggettoMod;
		
	//bi-directional many-to-one association to SiacTSoggettoFin
	@ManyToOne
	@JoinColumn(name="soggetto_id")
	private SiacTSoggettoFin siacTSoggetto;

	public SiacRFormaGiuridicaModFin() {
	}

	public Integer getSoggettoFormaGiuridicaModId() {
		return this.soggettoFormaGiuridicaModId;
	}

	public void setSoggettoFormaGiuridicaModId(Integer soggettoFormaGiuridicaModId) {
		this.soggettoFormaGiuridicaModId = soggettoFormaGiuridicaModId;
	}

	public SiacTFormaGiuridicaFin getSiacTFormaGiuridica() {
		return this.siacTFormaGiuridica;
	}

	public void setSiacTFormaGiuridica(SiacTFormaGiuridicaFin siacTFormaGiuridica) {
		this.siacTFormaGiuridica = siacTFormaGiuridica;
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
	
	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.soggettoFormaGiuridicaModId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.soggettoFormaGiuridicaModId = uid;
	}
}