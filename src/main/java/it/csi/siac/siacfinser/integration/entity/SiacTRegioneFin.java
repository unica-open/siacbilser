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
 * The persistent class for the siac_t_regione database table.
 * 
 */
@Entity
@Table(name="siac_t_regione")
public class SiacTRegioneFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="regione_id")
	private Integer regioneId;

	@Column(name="regione_denominazione")
	private String regioneDenominazione;

	@Column(name="regione_istat_codice")
	private String regioneIstatCodice;

	//bi-directional many-to-one association to SiacRComuneRegioneFin
	@OneToMany(mappedBy="siacTRegione")
	private List<SiacRComuneRegioneFin> siacRComuneRegiones;

	//bi-directional many-to-one association to SiacRProvinciaRegioneFin
	@OneToMany(mappedBy="siacTRegione")
	private List<SiacRProvinciaRegioneFin> siacRProvinciaRegiones;

	public SiacTRegioneFin() {
	}

	public Integer getRegioneId() {
		return this.regioneId;
	}

	public void setRegioneId(Integer regioneId) {
		this.regioneId = regioneId;
	}

	public String getRegioneDenominazione() {
		return this.regioneDenominazione;
	}

	public void setRegioneDenominazione(String regioneDenominazione) {
		this.regioneDenominazione = regioneDenominazione;
	}

	public String getRegioneIstatCodice() {
		return this.regioneIstatCodice;
	}

	public void setRegioneIstatCodice(String regioneIstatCodice) {
		this.regioneIstatCodice = regioneIstatCodice;
	}

	public List<SiacRComuneRegioneFin> getSiacRComuneRegiones() {
		return this.siacRComuneRegiones;
	}

	public void setSiacRComuneRegiones(List<SiacRComuneRegioneFin> siacRComuneRegiones) {
		this.siacRComuneRegiones = siacRComuneRegiones;
	}

	public SiacRComuneRegioneFin addSiacRComuneRegione(SiacRComuneRegioneFin siacRComuneRegione) {
		getSiacRComuneRegiones().add(siacRComuneRegione);
		siacRComuneRegione.setSiacTRegione(this);

		return siacRComuneRegione;
	}

	public SiacRComuneRegioneFin removeSiacRComuneRegione(SiacRComuneRegioneFin siacRComuneRegione) {
		getSiacRComuneRegiones().remove(siacRComuneRegione);
		siacRComuneRegione.setSiacTRegione(null);

		return siacRComuneRegione;
	}

	public List<SiacRProvinciaRegioneFin> getSiacRProvinciaRegiones() {
		return this.siacRProvinciaRegiones;
	}

	public void setSiacRProvinciaRegiones(List<SiacRProvinciaRegioneFin> siacRProvinciaRegiones) {
		this.siacRProvinciaRegiones = siacRProvinciaRegiones;
	}

	public SiacRProvinciaRegioneFin addSiacRProvinciaRegione(SiacRProvinciaRegioneFin siacRProvinciaRegione) {
		getSiacRProvinciaRegiones().add(siacRProvinciaRegione);
		siacRProvinciaRegione.setSiacTRegione(this);

		return siacRProvinciaRegione;
	}

	public SiacRProvinciaRegioneFin removeSiacRProvinciaRegione(SiacRProvinciaRegioneFin siacRProvinciaRegione) {
		getSiacRProvinciaRegiones().remove(siacRProvinciaRegione);
		siacRProvinciaRegione.setSiacTRegione(null);

		return siacRProvinciaRegione;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.regioneId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.regioneId = uid;
	}
}