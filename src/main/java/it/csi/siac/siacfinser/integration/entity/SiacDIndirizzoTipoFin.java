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
 * The persistent class for the siac_d_indirizzo_tipo database table.
 * 
 */
@Entity
@Table(name="siac_d_indirizzo_tipo")
public class SiacDIndirizzoTipoFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="indirizzo_tipo_id")
	private Integer indirizzoTipoId;

	@Column(name="indirizzo_tipo_code")
	private String indirizzoTipoCode;

	@Column(name="indirizzo_tipo_desc")
	private String indirizzoTipoDesc;

	//bi-directional many-to-one association to SiacRIndirizzoSoggettoTipoFin
	@OneToMany(mappedBy="siacDIndirizzoTipo")
	private List<SiacRIndirizzoSoggettoTipoFin> siacRIndirizzoSoggettoTipos;

	public SiacDIndirizzoTipoFin() {
	}

	public Integer getIndirizzoTipoId() {
		return this.indirizzoTipoId;
	}

	public void setIndirizzoTipoId(Integer indirizzoTipoId) {
		this.indirizzoTipoId = indirizzoTipoId;
	}

	public String getIndirizzoTipoCode() {
		return this.indirizzoTipoCode;
	}

	public void setIndirizzoTipoCode(String indirizzoTipoCode) {
		this.indirizzoTipoCode = indirizzoTipoCode;
	}

	public String getIndirizzoTipoDesc() {
		return this.indirizzoTipoDesc;
	}

	public void setIndirizzoTipoDesc(String indirizzoTipoDesc) {
		this.indirizzoTipoDesc = indirizzoTipoDesc;
	}

	public List<SiacRIndirizzoSoggettoTipoFin> getSiacRIndirizzoSoggettoTipos() {
		return this.siacRIndirizzoSoggettoTipos;
	}

	public void setSiacRIndirizzoSoggettoTipos(List<SiacRIndirizzoSoggettoTipoFin> siacRIndirizzoSoggettoTipos) {
		this.siacRIndirizzoSoggettoTipos = siacRIndirizzoSoggettoTipos;
	}

	public SiacRIndirizzoSoggettoTipoFin addSiacRIndirizzoSoggettoTipo(SiacRIndirizzoSoggettoTipoFin siacRIndirizzoSoggettoTipo) {
		getSiacRIndirizzoSoggettoTipos().add(siacRIndirizzoSoggettoTipo);
		siacRIndirizzoSoggettoTipo.setSiacDIndirizzoTipo(this);

		return siacRIndirizzoSoggettoTipo;
	}

	public SiacRIndirizzoSoggettoTipoFin removeSiacRIndirizzoSoggettoTipo(SiacRIndirizzoSoggettoTipoFin siacRIndirizzoSoggettoTipo) {
		getSiacRIndirizzoSoggettoTipos().remove(siacRIndirizzoSoggettoTipo);
		siacRIndirizzoSoggettoTipo.setSiacDIndirizzoTipo(null);

		return siacRIndirizzoSoggettoTipo;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.indirizzoTipoId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.indirizzoTipoId = uid;
	}

}