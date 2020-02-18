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
 * The persistent class for the siac_d_soggetto_tipo database table.
 * 
 */
@Entity
@Table(name="siac_d_soggetto_tipo")
public class SiacDSoggettoTipoFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="soggetto_tipo_id")
	private Integer soggettoTipoId;

	@Column(name="soggetto_tipo_code")
	private String soggettoTipoCode;

	@Column(name="soggetto_tipo_desc")
	private String soggettoTipoDesc;

	//bi-directional many-to-one association to SiacRSoggettoTipoFin
	@OneToMany(mappedBy="siacDSoggettoTipo")
	private List<SiacRSoggettoTipoFin> siacRSoggettoTipos;

	public SiacDSoggettoTipoFin() {
	}

	public Integer getSoggettoTipoId() {
		return this.soggettoTipoId;
	}

	public void setSoggettoTipoId(Integer soggettoTipoId) {
		this.soggettoTipoId = soggettoTipoId;
	}

	public String getSoggettoTipoCode() {
		return this.soggettoTipoCode;
	}

	public void setSoggettoTipoCode(String soggettoTipoCode) {
		this.soggettoTipoCode = soggettoTipoCode;
	}

	public String getSoggettoTipoDesc() {
		return this.soggettoTipoDesc;
	}

	public void setSoggettoTipoDesc(String soggettoTipoDesc) {
		this.soggettoTipoDesc = soggettoTipoDesc;
	}

	public List<SiacRSoggettoTipoFin> getSiacRSoggettoTipos() {
		return this.siacRSoggettoTipos;
	}

	public void setSiacRSoggettoTipos(List<SiacRSoggettoTipoFin> siacRSoggettoTipos) {
		this.siacRSoggettoTipos = siacRSoggettoTipos;
	}

	public SiacRSoggettoTipoFin addSiacRSoggettoTipo(SiacRSoggettoTipoFin siacRSoggettoTipo) {
		getSiacRSoggettoTipos().add(siacRSoggettoTipo);
		siacRSoggettoTipo.setSiacDSoggettoTipo(this);

		return siacRSoggettoTipo;
	}

	public SiacRSoggettoTipoFin removeSiacRSoggettoTipo(SiacRSoggettoTipoFin siacRSoggettoTipo) {
		getSiacRSoggettoTipos().remove(siacRSoggettoTipo);
		siacRSoggettoTipo.setSiacDSoggettoTipo(null);

		return siacRSoggettoTipo;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.soggettoTipoId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.soggettoTipoId = uid;
	}
}