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
 * The persistent class for the siac_r_comune_regione database table.
 * 
 */
@Entity
@Table(name="siac_r_comune_regione")
public class SiacRComuneRegioneFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="comune_regione_id")
	private Integer comuneRegioneId;

	//bi-directional many-to-one association to SiacTComuneFin
	@ManyToOne
	@JoinColumn(name="comune_id")
	private SiacTComuneFin siacTComune;

	//bi-directional many-to-one association to SiacTRegioneFin
	@ManyToOne
	@JoinColumn(name="regione_id")
	private SiacTRegioneFin siacTRegione;

	public SiacRComuneRegioneFin() {
	}

	public Integer getComuneRegioneId() {
		return this.comuneRegioneId;
	}

	public void setComuneRegioneId(Integer comuneRegioneId) {
		this.comuneRegioneId = comuneRegioneId;
	}

	public SiacTComuneFin getSiacTComune() {
		return this.siacTComune;
	}

	public void setSiacTComune(SiacTComuneFin siacTComune) {
		this.siacTComune = siacTComune;
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
		return this.comuneRegioneId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.comuneRegioneId = uid;
	}
}