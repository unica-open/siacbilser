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
 * The persistent class for the siac_d_cassa_econ_stampa_tipo database table.
 * 
 */
@Entity
@Table(name="siac_d_cassa_econ_stampa_tipo")
@NamedQuery(name="SiacDCassaEconStampaTipo.findAll", query="SELECT s FROM SiacDCassaEconStampaTipo s")
public class SiacDCassaEconStampaTipo extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_D_CASSA_ECON_STAMPA_TIPO_CESTTIPOID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_CASSA_ECON_STAMPA_TIPO_CEST_TIPO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_CASSA_ECON_STAMPA_TIPO_CESTTIPOID_GENERATOR")
	@Column(name="cest_tipo_id")
	private Integer cestTipoId;

	@Column(name="cest_tipo_code")
	private String cestTipoCode;

	@Column(name="cest_tipo_desc")
	private String cestTipoDesc;

	

	//bi-directional many-to-one association to SiacRCassaEconStampaTipoTemplate
	@OneToMany(mappedBy="siacDCassaEconStampaTipo")
	private List<SiacRCassaEconStampaTipoTemplate> siacRCassaEconStampaTipoTemplates;

	//bi-directional many-to-one association to SiacTCassaEconStampa
	@OneToMany(mappedBy="siacDCassaEconStampaTipo")
	private List<SiacTCassaEconStampa> siacTCassaEconStampas;

	public SiacDCassaEconStampaTipo() {
	}

	public Integer getCestTipoId() {
		return this.cestTipoId;
	}

	public void setCestTipoId(Integer cestTipoId) {
		this.cestTipoId = cestTipoId;
	}

	public String getCestTipoCode() {
		return this.cestTipoCode;
	}

	public void setCestTipoCode(String cestTipoCode) {
		this.cestTipoCode = cestTipoCode;
	}

	public String getCestTipoDesc() {
		return this.cestTipoDesc;
	}

	public void setCestTipoDesc(String cestTipoDesc) {
		this.cestTipoDesc = cestTipoDesc;
	}

	public List<SiacRCassaEconStampaTipoTemplate> getSiacRCassaEconStampaTipoTemplates() {
		return this.siacRCassaEconStampaTipoTemplates;
	}

	public void setSiacRCassaEconStampaTipoTemplates(List<SiacRCassaEconStampaTipoTemplate> siacRCassaEconStampaTipoTemplates) {
		this.siacRCassaEconStampaTipoTemplates = siacRCassaEconStampaTipoTemplates;
	}

	public SiacRCassaEconStampaTipoTemplate addSiacRCassaEconStampaTipoTemplate(SiacRCassaEconStampaTipoTemplate siacRCassaEconStampaTipoTemplate) {
		getSiacRCassaEconStampaTipoTemplates().add(siacRCassaEconStampaTipoTemplate);
		siacRCassaEconStampaTipoTemplate.setSiacDCassaEconStampaTipo(this);

		return siacRCassaEconStampaTipoTemplate;
	}

	public SiacRCassaEconStampaTipoTemplate removeSiacRCassaEconStampaTipoTemplate(SiacRCassaEconStampaTipoTemplate siacRCassaEconStampaTipoTemplate) {
		getSiacRCassaEconStampaTipoTemplates().remove(siacRCassaEconStampaTipoTemplate);
		siacRCassaEconStampaTipoTemplate.setSiacDCassaEconStampaTipo(null);

		return siacRCassaEconStampaTipoTemplate;
	}

	public List<SiacTCassaEconStampa> getSiacTCassaEconStampas() {
		return this.siacTCassaEconStampas;
	}

	public void setSiacTCassaEconStampas(List<SiacTCassaEconStampa> siacTCassaEconStampas) {
		this.siacTCassaEconStampas = siacTCassaEconStampas;
	}

	public SiacTCassaEconStampa addSiacTCassaEconStampa(SiacTCassaEconStampa siacTCassaEconStampa) {
		getSiacTCassaEconStampas().add(siacTCassaEconStampa);
		siacTCassaEconStampa.setSiacDCassaEconStampaTipo(this);

		return siacTCassaEconStampa;
	}

	public SiacTCassaEconStampa removeSiacTCassaEconStampa(SiacTCassaEconStampa siacTCassaEconStampa) {
		getSiacTCassaEconStampas().remove(siacTCassaEconStampa);
		siacTCassaEconStampa.setSiacDCassaEconStampaTipo(null);

		return siacTCassaEconStampa;
	}

	@Override
	public Integer getUid() {
		return this.cestTipoId;
	}

	@Override
	public void setUid(Integer uid) {
		this.cestTipoId = uid;
	}

}