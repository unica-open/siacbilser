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
 * The persistent class for the siac_r_comune_provincia database table.
 * 
 */
@Entity
@Table(name="siac_r_comune_provincia")
public class SiacRComuneProvinciaFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="comune_provincia_id")
	private Integer comuneProvinciaId;

	//bi-directional many-to-one association to SiacTComuneFin
	@ManyToOne
	@JoinColumn(name="comune_id")
	private SiacTComuneFin siacTComune;

	//bi-directional many-to-one association to SiacTProvinciaFin
	@ManyToOne
	@JoinColumn(name="provincia_id")
	private SiacTProvinciaFin siacTProvincia;

	public SiacRComuneProvinciaFin() {
	}

	public Integer getComuneProvinciaId() {
		return this.comuneProvinciaId;
	}

	public void setComuneProvinciaId(Integer comuneProvinciaId) {
		this.comuneProvinciaId = comuneProvinciaId;
	}

	public SiacTComuneFin getSiacTComune() {
		return this.siacTComune;
	}

	public void setSiacTComune(SiacTComuneFin siacTComune) {
		this.siacTComune = siacTComune;
	}

	public SiacTProvinciaFin getSiacTProvincia() {
		return this.siacTProvincia;
	}

	public void setSiacTProvincia(SiacTProvinciaFin siacTProvincia) {
		this.siacTProvincia = siacTProvincia;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.comuneProvinciaId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.comuneProvinciaId = uid;
	}
}