/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import it.csi.siac.siacfinser.integration.entity.base.SiacTEnteBase;


/**
 * The persistent class for the siac_d_via_tipo database table.
 * 
 */
@Entity
@Table(name="siac_d_via_tipo")
public class SiacDViaTipoFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_D_VIA_TIPO_VIA_TIPO_ID_GENERATOR", allocationSize=1, sequenceName="siac_d_via_tipo_via_tipo_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_VIA_TIPO_VIA_TIPO_ID_GENERATOR")
	@Column(name="via_tipo_id")
	private Integer viaTipoId;

	@Column(name="via_tipo_code")
	private String viaTipoCode;

	@Column(name="via_tipo_desc")
	private String viaTipoDesc;

	//bi-directional many-to-one association to SiacTIndirizzoSoggettoFin
	@OneToMany(mappedBy="siacDViaTipo")
	private List<SiacTIndirizzoSoggettoFin> siacTIndirizzoSoggettos;

	//bi-directional many-to-one association to SiacTIndirizzoSoggettoModFin
	@OneToMany(mappedBy="siacDViaTipo")
	private List<SiacTIndirizzoSoggettoModFin> siacTIndirizzoSoggettoMods;

	public SiacDViaTipoFin() {
	}

	public Integer getViaTipoId() {
		return this.viaTipoId;
	}

	public void setViaTipoId(Integer viaTipoId) {
		this.viaTipoId = viaTipoId;
	}

	public String getViaTipoCode() {
		return this.viaTipoCode;
	}

	public void setViaTipoCode(String viaTipoCode) {
		this.viaTipoCode = viaTipoCode;
	}

	public String getViaTipoDesc() {
		return this.viaTipoDesc;
	}

	public void setViaTipoDesc(String viaTipoDesc) {
		this.viaTipoDesc = viaTipoDesc;
	}

	public List<SiacTIndirizzoSoggettoFin> getSiacTIndirizzoSoggettos() {
		return this.siacTIndirizzoSoggettos;
	}

	public void setSiacTIndirizzoSoggettos(List<SiacTIndirizzoSoggettoFin> siacTIndirizzoSoggettos) {
		this.siacTIndirizzoSoggettos = siacTIndirizzoSoggettos;
	}

	public SiacTIndirizzoSoggettoFin addSiacTIndirizzoSoggetto(SiacTIndirizzoSoggettoFin siacTIndirizzoSoggetto) {
		getSiacTIndirizzoSoggettos().add(siacTIndirizzoSoggetto);
		siacTIndirizzoSoggetto.setSiacDViaTipo(this);

		return siacTIndirizzoSoggetto;
	}

	public SiacTIndirizzoSoggettoFin removeSiacTIndirizzoSoggetto(SiacTIndirizzoSoggettoFin siacTIndirizzoSoggetto) {
		getSiacTIndirizzoSoggettos().remove(siacTIndirizzoSoggetto);
		siacTIndirizzoSoggetto.setSiacDViaTipo(null);

		return siacTIndirizzoSoggetto;
	}

	public List<SiacTIndirizzoSoggettoModFin> getSiacTIndirizzoSoggettoMods() {
		return this.siacTIndirizzoSoggettoMods;
	}

	public void setSiacTIndirizzoSoggettoMods(List<SiacTIndirizzoSoggettoModFin> siacTIndirizzoSoggettoMods) {
		this.siacTIndirizzoSoggettoMods = siacTIndirizzoSoggettoMods;
	}

	public SiacTIndirizzoSoggettoModFin addSiacTIndirizzoSoggettoMod(SiacTIndirizzoSoggettoModFin siacTIndirizzoSoggettoMod) {
		getSiacTIndirizzoSoggettoMods().add(siacTIndirizzoSoggettoMod);
		siacTIndirizzoSoggettoMod.setSiacDViaTipo(this);

		return siacTIndirizzoSoggettoMod;
	}

	public SiacTIndirizzoSoggettoModFin removeSiacTIndirizzoSoggettoMod(SiacTIndirizzoSoggettoModFin siacTIndirizzoSoggettoMod) {
		getSiacTIndirizzoSoggettoMods().remove(siacTIndirizzoSoggettoMod);
		siacTIndirizzoSoggettoMod.setSiacDViaTipo(null);

		return siacTIndirizzoSoggettoMod;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.viaTipoId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.viaTipoId = uid;
	}
}