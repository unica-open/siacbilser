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
 * The persistent class for the siac_t_provincia database table.
 * 
 */
@Entity
@Table(name="siac_t_provincia")
public class SiacTProvinciaFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="provincia_id")
	private Integer provinciaId;

	@Column(name="provincia_desc")
	private String provinciaDesc;

	@Column(name="provincia_istat_code")
	private String provinciaIstatCode;

	@Column(name="sigla_automobilistica")
	private String siglaAutomobilistica;

	//bi-directional many-to-one association to SiacRComuneProvinciaFin
	@OneToMany(mappedBy="siacTProvincia")
	private List<SiacRComuneProvinciaFin> siacRComuneProvincias;

	//bi-directional many-to-one association to SiacRProvinciaRegioneFin
	@OneToMany(mappedBy="siacTProvincia")
	private List<SiacRProvinciaRegioneFin> siacRProvinciaRegiones;

	public SiacTProvinciaFin() {
	}

	public Integer getProvinciaId() {
		return this.provinciaId;
	}

	public void setProvinciaId(Integer provinciaId) {
		this.provinciaId = provinciaId;
	}

	public String getProvinciaDesc() {
		return this.provinciaDesc;
	}

	public void setProvinciaDesc(String provinciaDesc) {
		this.provinciaDesc = provinciaDesc;
	}

	public String getProvinciaIstatCode() {
		return this.provinciaIstatCode;
	}

	public void setProvinciaIstatCode(String provinciaIstatCode) {
		this.provinciaIstatCode = provinciaIstatCode;
	}

	public String getSiglaAutomobilistica() {
		return this.siglaAutomobilistica;
	}

	public void setSiglaAutomobilistica(String siglaAutomobilistica) {
		this.siglaAutomobilistica = siglaAutomobilistica;
	}

	public List<SiacRComuneProvinciaFin> getSiacRComuneProvincias() {
		return this.siacRComuneProvincias;
	}

	public void setSiacRComuneProvincias(List<SiacRComuneProvinciaFin> siacRComuneProvincias) {
		this.siacRComuneProvincias = siacRComuneProvincias;
	}

	public SiacRComuneProvinciaFin addSiacRComuneProvincia(SiacRComuneProvinciaFin siacRComuneProvincia) {
		getSiacRComuneProvincias().add(siacRComuneProvincia);
		siacRComuneProvincia.setSiacTProvincia(this);

		return siacRComuneProvincia;
	}

	public SiacRComuneProvinciaFin removeSiacRComuneProvincia(SiacRComuneProvinciaFin siacRComuneProvincia) {
		getSiacRComuneProvincias().remove(siacRComuneProvincia);
		siacRComuneProvincia.setSiacTProvincia(null);

		return siacRComuneProvincia;
	}

	public List<SiacRProvinciaRegioneFin> getSiacRProvinciaRegiones() {
		return this.siacRProvinciaRegiones;
	}

	public void setSiacRProvinciaRegiones(List<SiacRProvinciaRegioneFin> siacRProvinciaRegiones) {
		this.siacRProvinciaRegiones = siacRProvinciaRegiones;
	}

	public SiacRProvinciaRegioneFin addSiacRProvinciaRegione(SiacRProvinciaRegioneFin siacRProvinciaRegione) {
		getSiacRProvinciaRegiones().add(siacRProvinciaRegione);
		siacRProvinciaRegione.setSiacTProvincia(this);

		return siacRProvinciaRegione;
	}

	public SiacRProvinciaRegioneFin removeSiacRProvinciaRegione(SiacRProvinciaRegioneFin siacRProvinciaRegione) {
		getSiacRProvinciaRegiones().remove(siacRProvinciaRegione);
		siacRProvinciaRegione.setSiacTProvincia(null);

		return siacRProvinciaRegione;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.provinciaId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.provinciaId = uid;
	}
}