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
 * The persistent class for the siac_d_iva_stampa_tipo database table.
 * 
 */
@Entity
@Table(name="siac_d_iva_stampa_tipo")
@NamedQuery(name="SiacDIvaStampaTipo.findAll", query="SELECT s FROM SiacDIvaStampaTipo s")
public class SiacDIvaStampaTipo extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_D_IVA_STAMPA_TIPO_IVASTTIPOID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_IVA_STAMPA_TIPO_IVAST_TIPO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_IVA_STAMPA_TIPO_IVASTTIPOID_GENERATOR")
	@Column(name="ivast_tipo_id")
	private Integer ivastTipoId;



	@Column(name="ivast_tipo_code")
	private String ivastTipoCode;

	@Column(name="ivast_tipo_desc")
	private String ivastTipoDesc;

	

	//bi-directional many-to-one association to SiacRIvaStampaTipoTemplate
	@OneToMany(mappedBy="siacDIvaStampaTipo")
	private List<SiacRIvaStampaTipoTemplate> siacRIvaStampaTipoTemplates;

	//bi-directional many-to-one association to SiacTIvaStampa
	@OneToMany(mappedBy="siacDIvaStampaTipo")
	private List<SiacTIvaStampa> siacTIvaStampas;

	public SiacDIvaStampaTipo() {
	}

	public Integer getIvastTipoId() {
		return this.ivastTipoId;
	}

	public void setIvastTipoId(Integer ivastTipoId) {
		this.ivastTipoId = ivastTipoId;
	}

	public String getIvastTipoCode() {
		return this.ivastTipoCode;
	}

	public void setIvastTipoCode(String ivastTipoCode) {
		this.ivastTipoCode = ivastTipoCode;
	}

	public String getIvastTipoDesc() {
		return this.ivastTipoDesc;
	}

	public void setIvastTipoDesc(String ivastTipoDesc) {
		this.ivastTipoDesc = ivastTipoDesc;
	}

	public List<SiacRIvaStampaTipoTemplate> getSiacRIvaStampaTipoTemplates() {
		return this.siacRIvaStampaTipoTemplates;
	}

	public void setSiacRIvaStampaTipoTemplates(List<SiacRIvaStampaTipoTemplate> siacRIvaStampaTipoTemplates) {
		this.siacRIvaStampaTipoTemplates = siacRIvaStampaTipoTemplates;
	}

	public SiacRIvaStampaTipoTemplate addSiacRIvaStampaTipoTemplate(SiacRIvaStampaTipoTemplate siacRIvaStampaTipoTemplate) {
		getSiacRIvaStampaTipoTemplates().add(siacRIvaStampaTipoTemplate);
		siacRIvaStampaTipoTemplate.setSiacDIvaStampaTipo(this);

		return siacRIvaStampaTipoTemplate;
	}

	public SiacRIvaStampaTipoTemplate removeSiacRIvaStampaTipoTemplate(SiacRIvaStampaTipoTemplate siacRIvaStampaTipoTemplate) {
		getSiacRIvaStampaTipoTemplates().remove(siacRIvaStampaTipoTemplate);
		siacRIvaStampaTipoTemplate.setSiacDIvaStampaTipo(null);

		return siacRIvaStampaTipoTemplate;
	}

	public List<SiacTIvaStampa> getSiacTIvaStampas() {
		return this.siacTIvaStampas;
	}

	public void setSiacTIvaStampas(List<SiacTIvaStampa> siacTIvaStampas) {
		this.siacTIvaStampas = siacTIvaStampas;
	}

	public SiacTIvaStampa addSiacTIvaStampa(SiacTIvaStampa siacTIvaStampa) {
		getSiacTIvaStampas().add(siacTIvaStampa);
		siacTIvaStampa.setSiacDIvaStampaTipo(this);

		return siacTIvaStampa;
	}

	public SiacTIvaStampa removeSiacTIvaStampa(SiacTIvaStampa siacTIvaStampa) {
		getSiacTIvaStampas().remove(siacTIvaStampa);
		siacTIvaStampa.setSiacDIvaStampaTipo(null);

		return siacTIvaStampa;
	}

	@Override
	public Integer getUid() {
		return ivastTipoId;
	}

	@Override
	public void setUid(Integer uid) {
		this.ivastTipoId = uid;
	}

}