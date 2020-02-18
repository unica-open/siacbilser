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
 * The persistent class for the siac_d_atto_allegato_stampa_tipo database table.
 * 
 */
@Entity
@Table(name="siac_d_atto_allegato_stampa_tipo")
@NamedQuery(name="SiacDAttoAllegatoStampaTipo.findAll", query="SELECT s FROM SiacDAttoAllegatoStampaTipo s")
public class SiacDAttoAllegatoStampaTipo extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_D_ATTO_ALLEGATO_STAMPA_TIPO_ATTOALSTTIPOID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_ATTO_ALLEGATO_STAMPA_TIPO_ATTOALST_TIPO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_ATTO_ALLEGATO_STAMPA_TIPO_ATTOALSTTIPOID_GENERATOR")
	@Column(name="attoalst_tipo_id")
	private Integer attoalstTipoId;

	@Column(name="attoalst_tipo_code")
	private String attoalstTipoCode;

	@Column(name="attoalst_tipo_desc")
	private String attoalstTipoDesc;

	//bi-directional many-to-one association to SiacRAttoAllegatoStampaTipoTemplate
	@OneToMany(mappedBy="siacDAttoAllegatoStampaTipo")
	private List<SiacRAttoAllegatoStampaTipoTemplate> siacRAttoAllegatoStampaTipoTemplates;

	//bi-directional many-to-one association to SiacTAttoAllegatoStampa
	@OneToMany(mappedBy="siacDAttoAllegatoStampaTipo")
	private List<SiacTAttoAllegatoStampa> siacTAttoAllegatoStampas;

	public SiacDAttoAllegatoStampaTipo() {
	}

	public Integer getAttoalstTipoId() {
		return this.attoalstTipoId;
	}

	public void setAttoalstTipoId(Integer attoalstTipoId) {
		this.attoalstTipoId = attoalstTipoId;
	}

	public String getAttoalstTipoCode() {
		return this.attoalstTipoCode;
	}

	public void setAttoalstTipoCode(String attoalstTipoCode) {
		this.attoalstTipoCode = attoalstTipoCode;
	}

	public String getAttoalstTipoDesc() {
		return this.attoalstTipoDesc;
	}

	public void setAttoalstTipoDesc(String attoalstTipoDesc) {
		this.attoalstTipoDesc = attoalstTipoDesc;
	}

	public List<SiacRAttoAllegatoStampaTipoTemplate> getSiacRAttoAllegatoStampaTipoTemplates() {
		return this.siacRAttoAllegatoStampaTipoTemplates;
	}

	public void setSiacRAttoAllegatoStampaTipoTemplates(List<SiacRAttoAllegatoStampaTipoTemplate> siacRAttoAllegatoStampaTipoTemplates) {
		this.siacRAttoAllegatoStampaTipoTemplates = siacRAttoAllegatoStampaTipoTemplates;
	}

	public SiacRAttoAllegatoStampaTipoTemplate addSiacRAttoAllegatoStampaTipoTemplate(SiacRAttoAllegatoStampaTipoTemplate siacRAttoAllegatoStampaTipoTemplate) {
		getSiacRAttoAllegatoStampaTipoTemplates().add(siacRAttoAllegatoStampaTipoTemplate);
		siacRAttoAllegatoStampaTipoTemplate.setSiacDAttoAllegatoStampaTipo(this);

		return siacRAttoAllegatoStampaTipoTemplate;
	}

	public SiacRAttoAllegatoStampaTipoTemplate removeSiacRAttoAllegatoStampaTipoTemplate(SiacRAttoAllegatoStampaTipoTemplate siacRAttoAllegatoStampaTipoTemplate) {
		getSiacRAttoAllegatoStampaTipoTemplates().remove(siacRAttoAllegatoStampaTipoTemplate);
		siacRAttoAllegatoStampaTipoTemplate.setSiacDAttoAllegatoStampaTipo(null);

		return siacRAttoAllegatoStampaTipoTemplate;
	}

	public List<SiacTAttoAllegatoStampa> getSiacTAttoAllegatoStampas() {
		return this.siacTAttoAllegatoStampas;
	}

	public void setSiacTAttoAllegatoStampas(List<SiacTAttoAllegatoStampa> siacTAttoAllegatoStampas) {
		this.siacTAttoAllegatoStampas = siacTAttoAllegatoStampas;
	}

	public SiacTAttoAllegatoStampa addSiacTAttoAllegatoStampa(SiacTAttoAllegatoStampa siacTAttoAllegatoStampa) {
		getSiacTAttoAllegatoStampas().add(siacTAttoAllegatoStampa);
		siacTAttoAllegatoStampa.setSiacDAttoAllegatoStampaTipo(this);

		return siacTAttoAllegatoStampa;
	}

	public SiacTAttoAllegatoStampa removeSiacTAttoAllegatoStampa(SiacTAttoAllegatoStampa siacTAttoAllegatoStampa) {
		getSiacTAttoAllegatoStampas().remove(siacTAttoAllegatoStampa);
		siacTAttoAllegatoStampa.setSiacDAttoAllegatoStampaTipo(null);

		return siacTAttoAllegatoStampa;
	}

	@Override
	public Integer getUid() {
		return this.attoalstTipoId;
	}

	@Override
	public void setUid(Integer uid) {
		this.attoalstTipoId = uid;
	}

}