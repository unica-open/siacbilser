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
 * The persistent class for the siac_r_soggetto_relaz_mod database table.
 * 
 */
@Entity
@Table(name="siac_r_soggetto_relaz_mod")
public class SiacRSoggettoRelazModFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;	

	@Id
	@SequenceGenerator(name="SIAC_R_SOGGETTO_RELAZ_MOD_SOGGETTO_RELAZ_MOD_ID_GENERATOR", allocationSize=1, sequenceName="siac_r_soggetto_relaz_mod_soggetto_relaz_mod_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_SOGGETTO_RELAZ_MOD_SOGGETTO_RELAZ_MOD_ID_GENERATOR")
	@Column(name="soggetto_relaz_mod_id")
	private Integer soggettoRelazModId;

	//bi-directional many-to-one association to SiacDRuoloFin
	@ManyToOne
	@JoinColumn(name="soggetto_relaz_id")
	private SiacRSoggettoRelazFin siacRSoggettoRelaz;
	
	//bi-directional many-to-one association to SiacDRelazTipoFin
	@ManyToOne
	@JoinColumn(name="relaz_tipo_id")
	private SiacDRelazTipoFin siacDRelazTipo;

	//bi-directional many-to-one association to SiacDRuoloFin
	@ManyToOne
	@JoinColumn(name="ruolo_id_a")
	private SiacDRuoloFin siacDRuolo1;

	//bi-directional many-to-one association to SiacDRuoloFin
	@ManyToOne
	@JoinColumn(name="ruolo_id_da")
	private SiacDRuoloFin siacDRuolo2;

	//bi-directional many-to-one association to SiacTSoggettoFin
	@ManyToOne
	@JoinColumn(name="soggetto_id_da")
	private SiacTSoggettoFin siacTSoggetto1;

	//bi-directional many-to-one association to SiacTSoggettoFin
	@ManyToOne
	@JoinColumn(name="soggetto_id_a")
	private SiacTSoggettoFin siacTSoggetto2;

//	//bi-directional many-to-one association to SiacTSoggettoModFin
	@ManyToOne
	@JoinColumn(name="sog_mod_id")
	private SiacTSoggettoModFin siacTSoggettoMod;

	public SiacRSoggettoRelazModFin() {
	}

	public Integer getSoggettoRelazModId() {
		return this.soggettoRelazModId;
	}

	public void setSoggettoRelazModId(Integer soggettoRelazModId) {
		this.soggettoRelazModId = soggettoRelazModId;
	}

	public SiacDRelazTipoFin getSiacDRelazTipo() {
		return this.siacDRelazTipo;
	}

	public void setSiacDRelazTipo(SiacDRelazTipoFin siacDRelazTipo) {
		this.siacDRelazTipo = siacDRelazTipo;
	}

	public SiacDRuoloFin getSiacDRuolo1() {
		return this.siacDRuolo1;
	}

	public void setSiacDRuolo1(SiacDRuoloFin siacDRuolo1) {
		this.siacDRuolo1 = siacDRuolo1;
	}

	public SiacDRuoloFin getSiacDRuolo2() {
		return this.siacDRuolo2;
	}

	public void setSiacDRuolo2(SiacDRuoloFin siacDRuolo2) {
		this.siacDRuolo2 = siacDRuolo2;
	}

	public SiacTSoggettoFin getSiacTSoggetto1() {
		return this.siacTSoggetto1;
	}

	public void setSiacTSoggetto1(SiacTSoggettoFin siacTSoggetto1) {
		this.siacTSoggetto1 = siacTSoggetto1;
	}

	public SiacTSoggettoFin getSiacTSoggetto2() {
		return this.siacTSoggetto2;
	}

	public void setSiacTSoggetto2(SiacTSoggettoFin siacTSoggetto2) {
		this.siacTSoggetto2 = siacTSoggetto2;
	}

	public SiacTSoggettoModFin getSiacTSoggettoMod() {
		return this.siacTSoggettoMod;
	}

	public void setSiacTSoggettoMod(SiacTSoggettoModFin siacTSoggettoMod) {
		this.siacTSoggettoMod = siacTSoggettoMod;
	}

	public SiacRSoggettoRelazFin getSiacRSoggettoRelaz() {
		return siacRSoggettoRelaz;
	}

	public void setSiacRSoggettoRelaz(SiacRSoggettoRelazFin siacRSoggettoRelaz) {
		this.siacRSoggettoRelaz = siacRSoggettoRelaz;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.soggettoRelazModId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.soggettoRelazModId = uid;
	}
}