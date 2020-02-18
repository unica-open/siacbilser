/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import it.csi.siac.siacfinser.integration.entity.base.SiacTEnteBase;




/**
 * The persistent class for the siac_r_modulo_tabella database table.
 * 
 */
@Entity
@Table(name="siac_r_modulo_tabella")
public class SiacRModuloTabellaFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="modulo_tabella_id")
	private Integer moduloTabellaId;

	//bi-directional many-to-one association to SiacTModuloFin
	@ManyToOne
	@JoinColumn(name="modulo_id")
	private SiacTModuloFin siacTModulo;

	//bi-directional many-to-one association to SiacTTabellaFin
	@ManyToOne
	@JoinColumn(name="tabella_id")
	private SiacTTabellaFin siacTTabella;

	public SiacRModuloTabellaFin() {
	}

	public Integer getModuloTabellaId() {
		return this.moduloTabellaId;
	}

	public void setModuloTabellaId(Integer moduloTabellaId) {
		this.moduloTabellaId = moduloTabellaId;
	}

	public SiacTModuloFin getSiacTModulo() {
		return this.siacTModulo;
	}

	public void setSiacTModulo(SiacTModuloFin siacTModulo) {
		this.siacTModulo = siacTModulo;
	}

	public SiacTTabellaFin getSiacTTabella() {
		return this.siacTTabella;
	}

	public void setSiacTTabella(SiacTTabellaFin siacTTabella) {
		this.siacTTabella = siacTTabella;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.moduloTabellaId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.moduloTabellaId = uid;
	}
}