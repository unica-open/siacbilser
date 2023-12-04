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
 * The persistent class for the siac_r_provincia_regione database table.
 * 
 */
@Entity
@Table(name="siac_r_provincia_regione")
public class SiacRProvinciaRegioneFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="provincia_regione_id")
	private Integer provinciaRegioneId;

	//bi-directional many-to-one association to SiacTProvinciaFin
	@ManyToOne
	@JoinColumn(name="provincia_id")
	private SiacTProvinciaFin siacTProvincia;

	//bi-directional many-to-one association to SiacTRegioneFin
	@ManyToOne
	@JoinColumn(name="regione_id")
	private SiacTRegioneFin siacTRegione;

	public SiacRProvinciaRegioneFin() {
	}

	public Integer getProvinciaRegioneId() {
		return this.provinciaRegioneId;
	}

	public void setProvinciaRegioneId(Integer provinciaRegioneId) {
		this.provinciaRegioneId = provinciaRegioneId;
	}

	public SiacTProvinciaFin getSiacTProvincia() {
		return this.siacTProvincia;
	}

	public void setSiacTProvincia(SiacTProvinciaFin siacTProvincia) {
		this.siacTProvincia = siacTProvincia;
	}

	public SiacTRegioneFin getSiacTRegione() {
		return this.siacTRegione;
	}

	public void setSiacTRegione(SiacTRegioneFin siacTRegione) {
		this.siacTRegione = siacTRegione;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.provinciaRegioneId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.provinciaRegioneId = uid;
	}
}