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
 * The persistent class for the siac_t_repj_template database table.
 * 
 */
@Entity
@Table(name="siac_t_repj_template")
@NamedQuery(name="SiacTRepjTemplate.findAll", query="SELECT s FROM SiacTRepjTemplate s")
public class SiacTRepjTemplate extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_T_REPJ_TEMPLATE_REPJTID_GENERATOR", allocationSize=1, sequenceName="SIAC_T_REPJ_TEMPLATE_REPJT_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_REPJ_TEMPLATE_REPJTID_GENERATOR")
	@Column(name="repjt_id")
	private Integer repjtId;

	@Column(name="repjt_code")
	private String repjtCode;

	@Column(name="repjt_desc")
	private String repjtDesc;

	@Column(name="repjt_filename")
	private String repjtFilename;

	@Column(name="repjt_path")
	private String repjtPath;

	//bi-directional many-to-one association to SiacRIvaStampaTipoTemplate
	@OneToMany(mappedBy="siacTRepjTemplate")
	private List<SiacRIvaStampaTipoTemplate> siacRIvaStampaTipoTemplates;

	public SiacTRepjTemplate() {
	}

	public Integer getRepjtId() {
		return this.repjtId;
	}

	public void setRepjtId(Integer repjtId) {
		this.repjtId = repjtId;
	}

	public String getRepjtCode() {
		return this.repjtCode;
	}

	public void setRepjtCode(String repjtCode) {
		this.repjtCode = repjtCode;
	}

	public String getRepjtDesc() {
		return this.repjtDesc;
	}

	public void setRepjtDesc(String repjtDesc) {
		this.repjtDesc = repjtDesc;
	}

	public String getRepjtFilename() {
		return this.repjtFilename;
	}

	public void setRepjtFilename(String repjtFilename) {
		this.repjtFilename = repjtFilename;
	}

	public String getRepjtPath() {
		return this.repjtPath;
	}

	public void setRepjtPath(String repjtPath) {
		this.repjtPath = repjtPath;
	}

	public List<SiacRIvaStampaTipoTemplate> getSiacRIvaStampaTipoTemplates() {
		return this.siacRIvaStampaTipoTemplates;
	}

	public void setSiacRIvaStampaTipoTemplates(List<SiacRIvaStampaTipoTemplate> siacRIvaStampaTipoTemplates) {
		this.siacRIvaStampaTipoTemplates = siacRIvaStampaTipoTemplates;
	}

	public SiacRIvaStampaTipoTemplate addSiacRIvaStampaTipoTemplate(SiacRIvaStampaTipoTemplate siacRIvaStampaTipoTemplate) {
		getSiacRIvaStampaTipoTemplates().add(siacRIvaStampaTipoTemplate);
		siacRIvaStampaTipoTemplate.setSiacTRepjTemplate(this);

		return siacRIvaStampaTipoTemplate;
	}

	public SiacRIvaStampaTipoTemplate removeSiacRIvaStampaTipoTemplate(SiacRIvaStampaTipoTemplate siacRIvaStampaTipoTemplate) {
		getSiacRIvaStampaTipoTemplates().remove(siacRIvaStampaTipoTemplate);
		siacRIvaStampaTipoTemplate.setSiacTRepjTemplate(null);

		return siacRIvaStampaTipoTemplate;
	}

	@Override
	public Integer getUid() {
		return repjtId;
	}

	@Override
	public void setUid(Integer uid) {
		this.repjtId = uid;
	}

}