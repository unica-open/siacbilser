/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import it.csi.siac.siacfinser.integration.entity.base.SiacTEnteBase;


/**
 * The persistent class for the siac_d_recapito_modo database table.
 * 
 */
@Entity
@Table(name="siac_d_recapito_modo")
public class SiacDRecapitoModoFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="recapito_modo_id")
	private Integer recapitoModoId;

	@Column(name="recapito_modo_code")
	private String recapitoModoCode;

	@Column(name="recapito_modo_desc")
	private String recapitoModoDesc;

	//bi-directional many-to-one association to SiacTRecapitoSoggettoFin
	@OneToMany(mappedBy="siacDRecapitoModo")
	private List<SiacTRecapitoSoggettoFin> siacTRecapitoSoggettos;

	//bi-directional many-to-one association to SiacTRecapitoSoggettoModFin
	@OneToMany(mappedBy="siacDRecapitoModo")
	private List<SiacTRecapitoSoggettoModFin> siacTRecapitoSoggettoMods;

	public SiacDRecapitoModoFin() {
	}

	public Integer getRecapitoModoId() {
		return this.recapitoModoId;
	}

	public void setRecapitoModoId(Integer recapitoModoId) {
		this.recapitoModoId = recapitoModoId;
	}

	public String getRecapitoModoCode() {
		return this.recapitoModoCode;
	}

	public void setRecapitoModoCode(String recapitoModoCode) {
		this.recapitoModoCode = recapitoModoCode;
	}

	public String getRecapitoModoDesc() {
		return this.recapitoModoDesc;
	}

	public void setRecapitoModoDesc(String recapitoModoDesc) {
		this.recapitoModoDesc = recapitoModoDesc;
	}

	public List<SiacTRecapitoSoggettoFin> getSiacTRecapitoSoggettos() {
		return this.siacTRecapitoSoggettos;
	}

	public void setSiacTRecapitoSoggettos(List<SiacTRecapitoSoggettoFin> siacTRecapitoSoggettos) {
		this.siacTRecapitoSoggettos = siacTRecapitoSoggettos;
	}

	public SiacTRecapitoSoggettoFin addSiacTRecapitoSoggetto(SiacTRecapitoSoggettoFin siacTRecapitoSoggetto) {
		getSiacTRecapitoSoggettos().add(siacTRecapitoSoggetto);
		siacTRecapitoSoggetto.setSiacDRecapitoModo(this);

		return siacTRecapitoSoggetto;
	}

	public SiacTRecapitoSoggettoFin removeSiacTRecapitoSoggetto(SiacTRecapitoSoggettoFin siacTRecapitoSoggetto) {
		getSiacTRecapitoSoggettos().remove(siacTRecapitoSoggetto);
		siacTRecapitoSoggetto.setSiacDRecapitoModo(null);

		return siacTRecapitoSoggetto;
	}

	public List<SiacTRecapitoSoggettoModFin> getSiacTRecapitoSoggettoMods() {
		return this.siacTRecapitoSoggettoMods;
	}

	public void setSiacTRecapitoSoggettoMods(List<SiacTRecapitoSoggettoModFin> siacTRecapitoSoggettoMods) {
		this.siacTRecapitoSoggettoMods = siacTRecapitoSoggettoMods;
	}

	public SiacTRecapitoSoggettoModFin addSiacTRecapitoSoggettoMod(SiacTRecapitoSoggettoModFin siacTRecapitoSoggettoMod) {
		getSiacTRecapitoSoggettoMods().add(siacTRecapitoSoggettoMod);
		siacTRecapitoSoggettoMod.setSiacDRecapitoModo(this);

		return siacTRecapitoSoggettoMod;
	}

	public SiacTRecapitoSoggettoModFin removeSiacTRecapitoSoggettoMod(SiacTRecapitoSoggettoModFin siacTRecapitoSoggettoMod) {
		getSiacTRecapitoSoggettoMods().remove(siacTRecapitoSoggettoMod);
		siacTRecapitoSoggettoMod.setSiacDRecapitoModo(null);

		return siacTRecapitoSoggettoMod;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.recapitoModoId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.recapitoModoId = uid;
	}

}