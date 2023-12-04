/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**
 * The persistent class for the siac_d_ente_proprietario_tipo database table.
 * 
 */
@Entity
@Table(name="siac_d_ente_proprietario_tipo")
@NamedQuery(name="SiacDEnteProprietarioTipo.findAll", query="SELECT s FROM SiacDEnteProprietarioTipo s")
public class SiacDEnteProprietarioTipo extends SiacTEnteBaseExt {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_D_ENTE_PROPRIETARIO_TIPO_EPTIPOID_GENERATOR", allocationSize = 1, sequenceName="SIAC_D_ENTE_PROPRIETARIO_TIPO_EPTIPO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_ENTE_PROPRIETARIO_TIPO_EPTIPOID_GENERATOR")
	@Column(name="eptipo_id")
	private Integer eptipoId;

	@Column(name="eptipo_code")
	private String eptipoCode;

	@Column(name="eptipo_desc")
	private String eptipoDesc;

	//bi-directional many-to-one association to SiacREnteProprietarioModel
	@OneToMany(mappedBy="siacDEnteProprietarioTipo")
	private List<SiacREnteProprietarioModel> siacREnteProprietarioModels;

	//bi-directional many-to-one association to SiacREnteProprietarioTipo
	@OneToMany(mappedBy="siacDEnteProprietarioTipo")
	private List<SiacREnteProprietarioTipo> siacREnteProprietarioTipos;

	public SiacDEnteProprietarioTipo() {
	}

	public Integer getEptipoId() {
		return this.eptipoId;
	}

	public void setEptipoId(Integer eptipoId) {
		this.eptipoId = eptipoId;
	}

	public String getEptipoCode() {
		return this.eptipoCode;
	}

	public void setEptipoCode(String eptipoCode) {
		this.eptipoCode = eptipoCode;
	}

	public String getEptipoDesc() {
		return this.eptipoDesc;
	}

	public void setEptipoDesc(String eptipoDesc) {
		this.eptipoDesc = eptipoDesc;
	}

	public List<SiacREnteProprietarioModel> getSiacREnteProprietarioModels() {
		return this.siacREnteProprietarioModels;
	}

	public void setSiacREnteProprietarioModels(List<SiacREnteProprietarioModel> siacREnteProprietarioModels) {
		this.siacREnteProprietarioModels = siacREnteProprietarioModels;
	}

	public SiacREnteProprietarioModel addSiacREnteProprietarioModel(SiacREnteProprietarioModel siacREnteProprietarioModel) {
		getSiacREnteProprietarioModels().add(siacREnteProprietarioModel);
		siacREnteProprietarioModel.setSiacDEnteProprietarioTipo(this);

		return siacREnteProprietarioModel;
	}

	public SiacREnteProprietarioModel removeSiacREnteProprietarioModel(SiacREnteProprietarioModel siacREnteProprietarioModel) {
		getSiacREnteProprietarioModels().remove(siacREnteProprietarioModel);
		siacREnteProprietarioModel.setSiacDEnteProprietarioTipo(null);

		return siacREnteProprietarioModel;
	}

	public List<SiacREnteProprietarioTipo> getSiacREnteProprietarioTipos() {
		return this.siacREnteProprietarioTipos;
	}

	public void setSiacREnteProprietarioTipos(List<SiacREnteProprietarioTipo> siacREnteProprietarioTipos) {
		this.siacREnteProprietarioTipos = siacREnteProprietarioTipos;
	}

	public SiacREnteProprietarioTipo addSiacREnteProprietarioTipo(SiacREnteProprietarioTipo siacREnteProprietarioTipo) {
		getSiacREnteProprietarioTipos().add(siacREnteProprietarioTipo);
		siacREnteProprietarioTipo.setSiacDEnteProprietarioTipo(this);

		return siacREnteProprietarioTipo;
	}

	public SiacREnteProprietarioTipo removeSiacREnteProprietarioTipo(SiacREnteProprietarioTipo siacREnteProprietarioTipo) {
		getSiacREnteProprietarioTipos().remove(siacREnteProprietarioTipo);
		siacREnteProprietarioTipo.setSiacDEnteProprietarioTipo(null);

		return siacREnteProprietarioTipo;
	}

	@Override
	public Integer getUid() {
		return eptipoId;
	}

	@Override
	public void setUid(Integer uid) {
		eptipoId = uid;
	}

}