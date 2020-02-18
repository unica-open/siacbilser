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
 * The persistent class for the siac_d_periodo_tipo database table.
 * 
 */
@Entity
@Table(name="siac_d_periodo_tipo")
public class SiacDPeriodoTipoFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="periodo_tipo_id")
	private Integer periodoTipoId;

	@Column(name="periodo_tipo_code")
	private String periodoTipoCode;

	@Column(name="periodo_tipo_desc")
	private String periodoTipoDesc;

	//bi-directional many-to-one association to SiacTPeriodoFin
	@OneToMany(mappedBy="siacDPeriodoTipo")
	private List<SiacTPeriodoFin> siacTPeriodos;

	public SiacDPeriodoTipoFin() {
	}

	public Integer getPeriodoTipoId() {
		return this.periodoTipoId;
	}

	public void setPeriodoTipoId(Integer periodoTipoId) {
		this.periodoTipoId = periodoTipoId;
	}

	public String getPeriodoTipoCode() {
		return this.periodoTipoCode;
	}

	public void setPeriodoTipoCode(String periodoTipoCode) {
		this.periodoTipoCode = periodoTipoCode;
	}

	public String getPeriodoTipoDesc() {
		return this.periodoTipoDesc;
	}

	public void setPeriodoTipoDesc(String periodoTipoDesc) {
		this.periodoTipoDesc = periodoTipoDesc;
	}

	public List<SiacTPeriodoFin> getSiacTPeriodos() {
		return this.siacTPeriodos;
	}

	public void setSiacTPeriodos(List<SiacTPeriodoFin> siacTPeriodos) {
		this.siacTPeriodos = siacTPeriodos;
	}

	public SiacTPeriodoFin addSiacTPeriodo(SiacTPeriodoFin siacTPeriodo) {
		getSiacTPeriodos().add(siacTPeriodo);
		siacTPeriodo.setSiacDPeriodoTipo(this);

		return siacTPeriodo;
	}

	public SiacTPeriodoFin removeSiacTPeriodo(SiacTPeriodoFin siacTPeriodo) {
		getSiacTPeriodos().remove(siacTPeriodo);
		siacTPeriodo.setSiacDPeriodoTipo(null);

		return siacTPeriodo;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.periodoTipoId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.periodoTipoId = uid;
	}
}